package com.example.runa.filedownloadtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*

     * TODO Current task (start time, end time, total time, automatic send mail after ending)
     *
     * TODO GUI
     *  TODO make array adapter work (we need a custom adapter)
     *  TODO editText (Customer search): update selectable customers automaticaly
     *  TODO history: last used customer at top of customerlist
     *  TODO task selection activity
     *  TODO start/pause/finish task
      */

    Context context;
    // File url to download
    private static String file_url = "https://erp.sonith.de/app/kunden.json";
    // Path, where file is stored
    private String filePath = null;
    // List with customers
    private ArrayList<Customer> customers;
    // List with all tasks
    private ArrayList<String> allTasks;

    //GUI Elements
    private TextView tvSelectCustomer;
    private EditText etSearch;
    private CustomerAdapter cListAdapter;
    private ListView lvCustomers;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //TODO update button -> update only on demand
        updateCustomers();

        initializeGUI();


    }

    private void updateCustomers(){
        //List to store customers that are currently in the database
        ArrayList <Customer> newCustomers = null;

        //get json file  with all customers from database
        DownloadFileFromURL downloader = new DownloadFileFromURL(context, "test.json");
        filePath = downloader.getFilePath();
        downloader.execute(file_url);

        //read the file
        ReadFile readFile = new ReadFile(filePath);
        newCustomers = readFile.execute();

        //compare servers customers (old and new ones) with apps customers (old ones) and add new customers on server to app
        //if a customer list already exists
        if (customers!=null){
            for (Customer newC : newCustomers ){
                boolean isNew = true;
                for (Customer C : customers){
                    if (C.getName()==newC.getName() && C.getNumber()==newC.getNumber()){
                        isNew = false;
                        break;
                    }
                }
                if (isNew = true){
                    Customer newCustomer = new Customer(newC.getNumber(), newC.getName());
                    Log.d("Added new Customer", newCustomer.toString());
                }

            }
        }
        //if no customer list exists, just copy the one from the server
        else{
            Log.d("INFO", "no customer list existed, added all new ones to the list");
            customers=newCustomers;
        }
    }

    private void buildTaskList(){
        //TODO read all tasks from customers and build list
    }

    private void initializeGUI() {
        //Initialize SearchField (EditText)
        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cListAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //Initialize ListView
        Log.d("customers.size()", Integer.toString(customers.size()));
        tvSelectCustomer = (TextView) findViewById(R.id.tvSelectCustomer);
        tvSelectCustomer.setText(R.string.selectCustomer);
        cListAdapter =
                new CustomerAdapter(this, R.layout.customer_view, customers);
        lvCustomers = (ListView) findViewById(R.id.lvCustomer);
        lvCustomers.setAdapter(cListAdapter);
        //this is unbelieveably ugly and could for sure have been easier to implement
        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startTaskSelectionActivity(position);
            }
        });



    }

    private void startTaskSelectionActivity(int position){
        Customer customer = customers.get(position);
        Intent i = new Intent(this, TaskSelectionActivity.class);
        i.putExtra("customer", customer);
        startActivityForResult(i, 42); //nr 42 is random, only to determine wich new activity returned
        //TODO when task is selected, update the customer and persist it in order to keep taskcounter up to date
    }




        //TODO add on text change listener to etSearch


}
