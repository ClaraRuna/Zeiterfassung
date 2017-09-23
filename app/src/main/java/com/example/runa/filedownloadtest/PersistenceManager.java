package com.example.runa.filedownloadtest;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by runa on 22.09.17.
 * This class is responsible for reading/writing customers to/from JSON files
 */

public class PersistenceManager {

    private Context context;
    private String folderPath;
    private File folder;

    public PersistenceManager (Context context){
        this.context= context;
        this.folderPath= context.getFilesDir().toString()
                + "/customers";
        folder= new File(folderPath);

    }

    public Set<Customer> loadCustomers(){
        Set <Customer> customers = new HashSet<Customer>() ;
        //read all the files in the customers directory and construct each customer
        for (File file : folder.listFiles()){
            customers.add(readCustomer(file));
        }
        return customers;
    }

    private Customer readCustomer(File file){
        Customer customer = new Customer();
        try {
            InputStream in = new FileInputStream(file);
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.beginObject();
            while (reader.hasNext()) {
                if (reader.nextName()=="Number"){
                    customer.setNumber(reader.nextInt());
                }
                else if (reader.nextName()=="Name"){
                    customer.setName(reader.nextString());
                }
                else if (reader.nextName()=="Tasks"){
                    reader.beginArray();
                    while (reader.hasNext()){
                        reader.beginObject();
                        String task="default";
                        Integer count = -1;
                        while (reader.hasNext()){
                            if (reader.nextName()=="Name"){
                                task=reader.nextString();
                            }
                            else if (reader.nextName()=="Count"){
                                count=(reader.nextInt());
                            }
                            else{
                                Log.d("ERRR", "cannot evaluate JSON name (@task lvl) ");
                            }
                        }
                        customer.setTasks(task, count);
                        reader.endObject();
                    }
                    reader.endArray();
                }
                else{
                    Log.d("ERRR", "cannot evaluate JSON name (@customer lvl)");
                }
            }
            reader.endObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    private String writeCustomer (Customer customer){
        String out ="";
        //File to store customer information in
        String filePath=context.getFilesDir().toString()
                + "/" + customer.getNumber().toString();
        File file = new File (filePath);
        //json String representing the customer
        //TODO write JSON, maybe not with FileWriter but with JsonWriter?
        try{
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("Number").value(customer.getNumber());
            writer.name("Name").value(customer.getName());
            writer.name("Tasks");
            writer.beginArray();
                writeTasks(writer, customer.getTasks());
            writer.endArray();
            writer.endObject();
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ReadFile fileContent = new ReadFile(filePath);
        Log.d("created customer file", filePath);
        fileContent.printFile();
        return out;
    }

    private void writeTasks(JsonWriter writer, HashMap<String, Integer> tasks) throws IOException{
        for (Map.Entry<String, Integer> task : tasks.entrySet()) {
            writer.beginObject();
            writer.name("Name").value(task.getKey());
            writer.name("Count").value(task.getValue());
            writer.endObject();
        }
    }

}

