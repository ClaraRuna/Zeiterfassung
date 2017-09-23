package com.example.runa.filedownloadtest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    /*
        TODO Customer and PersistenceManager: change time to count (don't count how long, but how often the task is executed)
     * TODO complete Customer, add Activities
     * TODO automatic Mail after Completion of Activity
     * TODO GUI
      */

    Context context;
    // File url to download
    private static String file_url = "https://erp.sonith.de/app/kunden.json";
    // Path, where file is stored
    private String filePath = null;
    // Set with customers
    private Set<Customer> customers;
    // List with all tasks
    private HashSet<String> allTasks;

    //GUI Elements


    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //TODO update button -> update only on demand
        updateCustomers();



    }

    private void updateCustomers(){
        //Set to store customers that are currently in the database
        Set <Customer> newCustomers = null;

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


}
