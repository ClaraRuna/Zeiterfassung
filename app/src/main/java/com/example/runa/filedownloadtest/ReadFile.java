package com.example.runa.filedownloadtest;

import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.in;

/**
 * Created by runa on 21.09.17.
 */

public class ReadFile {

    String filePath;
    File file=null;

     public ReadFile(String filePath){
         this.filePath=filePath;
         this.file=new File(filePath);
     }

    public Set<Customer> execute (){
        Set <Customer> customers = new HashSet<Customer>();
        if(file.exists()){
            try{

                Log.d("file found at", filePath);
                printFile();
                customers=readCustomerArray();
            }
            catch (Exception e){
                Log.d("ERR",e.getMessage());
            }
        }
        else{
            Log.d("no file found at", filePath);
        }
        return customers;
    }

    public void printFile() {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            Log.d("contents of file", text.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set <Customer> readCustomerArray(){
        Set <Customer> customers = new HashSet<Customer>();
        try{
            InputStream in = new FileInputStream(file);
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.setLenient(false);
            reader.beginArray();
            while (reader.hasNext()) {
                customers.add(readCustomer(reader));
            }
            reader.endArray();
        }
        catch (Exception e){
            e.getMessage();
        }
        return customers;
    }

    private Customer readCustomer(JsonReader reader){
        //Log.d("readcustomer()", "entered");
        Integer cNr=-1;
        String cName = null;
        try{
            //Log.d("reader.peek", reader.peek().toString());
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                //Log.d("reader.nextName()", name);
                if (name.equals("Kunde")) {
                    cName= reader.nextString();
                    //Log.d(name, cName);
                } else if (name.equals("Kundennummer")) {
                    cNr = reader.nextInt();
                    //Log.d(name, Integer.toString(cNr));
                } else {
                    reader.skipValue();
                    Log.d("readCustomer", "name " + name + " can not be evaluated.");
                }
            }
            Log.d("1 Kunde ausgewertet", "Kunde: " + cName + "\n Kundennummer: " + cNr);
            reader.endObject();
        }
        catch (Exception e){
            e.getMessage();
        }
        return new Customer(cNr, cName);
    }
}
