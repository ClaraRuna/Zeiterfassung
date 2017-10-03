package com.example.runa.filedownloadtest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    /*
     * TODO : Feature: Weitere Tätigkeiten zu laufender Tätigkeit hinzufügen können
     * TODO : Bug: Beenden hält Task nicht an
     * TODO : Feature: Auswahl von zwischenzeiten beim Hinzufügen von Untertasks
     * TODO : Feature: Toast ob Update der Kundenliste erfolgt
     * TODO : Feature: Start und Endzeit Manuell anpassen
     * TODO : Bug: Übernimmt Name nach Update der Kundendb in der Anzeige in welcher die Uhr läuft nich korrekt
     *  Für den Bug hilft nur der Workarround die App abzuschießen und neu zu starten. Hängt wohl mit dem Beenden zusammen.
     * TODO : Feature: Icon in Leiste oben wenn aktiv
     * TODO : authentifizierung
     * TODO : android keystore
      */

    Context context;
    // File url to download
    private static String file_url = "https://erp.sonith.de/app/kunden.json";
    // Path, where file is stored
    private String filePath = null;
    // List with customers
    private ArrayList<Customer> customers;
    // List with all tasks
    private ArrayList<Task> allTasks;
    private PersistenceManager persistenceManager;

    //GUI Elements
    private TextView tvSelectCustomer;
    private EditText etSearch;
    private CustomerAdapter cListAdapter;
    private ListView lvCustomers;
    private Button btnUpdate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        //load customers
        Log.d("creating", "persistenceManager");
        persistenceManager = new PersistenceManager(context);
        Log.d("MainActivity", "customers=persistenceManager.loadCustomers()");
        customers=persistenceManager.loadCustomers();

        setContentView(R.layout.activity_main);

        initialize();


    }

    private void updateCustomers(){
        Log.d("MainActivity", "updateCustomers()");

        //List to store customers that are currently in the database
        ArrayList <Customer> newCustomers = null;

        //get json file  with all customers from database
        DownloadFileFromURL downloader = new DownloadFileFromURL(context, "test.json");
        filePath = downloader.getFilePath();
        downloader.execute(file_url);
        try{
            downloader.get(1000, TimeUnit.MILLISECONDS);
        }
        catch (Exception e){
           Log.d("ERRR", "iwie wurde auf den download nicht gewartet oder so...");
        }

        ReadFile readFile = new ReadFile(filePath);
        Log.d("contents of file", filePath);
        readFile.printFile();
        newCustomers = readFile.execute();

        //compare servers customers (old and new ones) with apps customers (old ones) and add new customers on server to app
        //if a customer list already exists
        if (customers!=null){
            for (Customer newC : newCustomers ){
                Log.d("customers.size()", Integer.toString(customers.size()));
                Log.d("compare newC", newC.toString());
                boolean isNew = true;
                for (Customer c : customers){
                    Log.d("to oldC", c.toString());
                    if (c.getName().equals(newC.getName()) && c.getNumber().equals(newC.getNumber())){
                        isNew = false;
                        break;
                    }
                }
                Log.d("resulting isNew", Boolean.toString(isNew));
                if (isNew == true){
                    customers.add(newC);
                    Log.d("Added new Customer", newC.toString());
                }

            }
        }
        //if no customer list exists, just copy the one from the server
        else{
            Log.d("INFO", "no customer list existed, added all new ones to the list");
            customers=newCustomers;
        }
        Log.d("customers.size()", Integer.toString(customers.size()));
        cListAdapter.notifyDataSetChanged();
        for (Customer c : customers){
            persistenceManager.writeCustomer(c);
        }
    }

    private void buildTaskList() {
        allTasks=new ArrayList<Task>();
        for (Customer c : customers) {
            //if a task in a customer's task list is not yet in the list of all tasks, add it
            for (Task task : c.getTasks()) {
                Boolean isTaskNew = true;
                for (Task t : allTasks) {
                    if (t.getName().equals(task.getName())) { //this should work as task is comparable
                        isTaskNew = false;
                    }
                }
                if (isTaskNew == true) {
                    allTasks.add(task);
                }
            }
        }
    }

    private void initialize() {
        buildTaskList(); //init list of all tasks
        //Initialize SearchField (EditText)
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnUpdate =(Button) findViewById(R.id.btnUpdate);
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
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCustomers();
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
        //put the last recently used customer on top of the list
        customers.remove(customer);
        customers.add(0, customer);
        Intent i = new Intent(this, TaskSelectionActivity.class);
        i.putExtra("customer", customer);
        i.putExtra("allTasks", allTasks);
        startActivity(i);
    }

}
