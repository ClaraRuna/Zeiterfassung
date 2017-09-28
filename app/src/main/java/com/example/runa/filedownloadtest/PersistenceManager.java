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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by runa on 22.09.17.
 * This class is responsible for reading/writing customers to/from JSON files
 */

public class PersistenceManager {

    private Context context;
    private String folderPath;
    private File folder;

    public PersistenceManager (Context context){
        //create the folder with the customers if it does not exist jet
        this.context= context;
        this.folderPath= context.getFilesDir().toString()
                + "/customers";
        folder= new File(folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }
        Log.d("folder.exists()", Boolean.toString(folder.exists()));

    }

    public ArrayList<Customer> loadCustomers(){
        ArrayList <Customer> customers = new ArrayList<Customer>() ;
        //read all the files in the customers directory and construct each customer
        Log.d("reading", "customers");
        Log.d("nr of customer files", Integer.toString(folder.listFiles().length));
        for (File file : folder.listFiles()){
            if (!(readCustomer(file)==null)){
                customers.add(readCustomer(file));
            }
            Log.d("found file", file.getName());
            ReadFile fileContent = new ReadFile(file.getPath());
            fileContent.printFile();
        }
        return customers;
    }

    private Customer readCustomer(File file){
        Log.d("PersistenceManager", "readCustomer()");
        Customer customer = new Customer();
        try {
            InputStream in = new FileInputStream(file);
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.beginObject();
            while (reader.hasNext()) {
                Log.d("reader.peek()", reader.peek().toString());
                String name=reader.nextName();
                Log.d("reader.nextName()", name);
                if (name.equals("Number")){
                    customer.setNumber(reader.nextInt());
                }
                else if (name.equals("Name")){
                    customer.setName(reader.nextString());
                }
                else if (name.equals("Tasks")){
                    reader.beginArray();
                    while (reader.hasNext()){
                        reader.beginObject();
                        Task task = new Task();
                        while (reader.hasNext()){
                            name = reader.nextName();
                            Log.d("reader.nextName()", name);

                            if (name.equals("Name")){
                                task.setName(reader.nextString());
                            }
                            else if (name.equals("Count")){
                                task.setCount(reader.nextInt());
                            }
                            else{
                                Log.d("ERRR", "cannot evaluate JSON name (@task lvl) ");
                            }
                        }
                        customer.setTasks(task);
                        reader.endObject();
                    }
                    reader.endArray();
                }
                else{
                    Log.d("ERRR", "cannot evaluate JSON name (@customer lvl)");
                }
            }
            reader.endObject();
            Log.d("PersistenceManager", "evaluated customer " +customer.toString());
            Log.d("TaskList", ":::::::::::::::::::::::::::::::");
            for (Task t : customer.getTasks()){
                Log.d(t.getName(), Integer.toString(t.getCount()));
            }
            return customer;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String writeCustomer (Customer customer){
        String out ="";
        //File to store customer information in
        String filePath=context.getFilesDir().toString()
                + "/customers/" + customer.getNumber().toString();
        File file = new File (filePath);
        //json String representing the customer
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

    private void writeTasks(JsonWriter writer, SortedSet<Task> tasks) throws IOException{
        for (Task t : tasks){
            writer.beginObject();
            writer.name("Name").value(t.getName());
            writer.name("Count").value(t.getCount());
            writer.endObject();
        }
    }

}

