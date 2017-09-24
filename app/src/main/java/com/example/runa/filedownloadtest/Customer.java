package com.example.runa.filedownloadtest;

import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by runa on 21.09.17.
 */

public class Customer implements Serializable{

    String name;
    Integer number;
    File file;          //File, in wich customer information is stored
    HashMap<String, Integer> tasks; //Map that stores how often Tasks were executed at the customer

    public Customer (Integer cNr, String cName){
        name=cName;
        number=cNr;
    }

    public Customer(){
        name="defaultname";
        number=-1;
    }

    public String getName() {
        return name;
    }
    public boolean setName ( String name ){ this.name=name; return true; }

    public Integer getNumber() {
        return number;
    }
    public boolean setNumber(Integer number) {this.number=number; return true;}

    public HashMap<String, Integer> getTasks(){
        return tasks;
    }

    public String toString(){
        return"Number: " + Integer.toString(number) + " Name: " + name;
    }

    //addTask ( task, count ) is for constructing the tasklist from json
    public boolean setTasks (String task, Integer count){
        tasks.put(task, count);
        return true;
    }

    //addTask (task) is for counting up during usage of app
    public boolean addTask (String task){
        //if the task does not jet exist, put it to map
        if (!tasks.containsKey(task)){
            tasks.put(task, 1);
        }
        //otherwise, increment task-counter
        else{
            tasks.put(task, tasks.get(task)+1);
        }
        return true;
    }

}
