package com.example.runa.filedownloadtest;

import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by runa on 21.09.17.
 */

public class Customer implements Serializable{

    String name;
    Integer number;
    File file;          //File, in wich customer information is stored
    SortedSet<TaskTemplate> tasks;

    public Customer (Integer cNr, String cName){
        name=cName;
        number=cNr;
        tasks = new TreeSet<TaskTemplate>();
    }

    public Customer(){
        Log.d("customer()", "called");
        name="defaultname";
        number=-1;
        tasks = new TreeSet<TaskTemplate>();
    }

    public String getName() {
        return name;
    }
    public boolean setName ( String name ){ this.name=name; return true; }

    public Integer getNumber() {
        return number;
    }
    public boolean setNumber(Integer number) {this.number=number; return true;}

    public SortedSet<TaskTemplate> getTasks(){ return tasks;
    }

    public String toString(){
        return"Number: " + Integer.toString(number) + " Name: " + name;
    }

    //addTask ( task, count ) is for constructing the tasklist from json
    public boolean setTasks (TaskTemplate task){
        tasks.add(task);
        return true;
    }

    //addTask (task) is for counting up during usage of app
    public TaskTemplate doTask (TaskTemplate task){
        Boolean isTaskNew = true;
        //as task implements comparable this should work
        for (TaskTemplate t: tasks ) {
            if(task.getName().toLowerCase().equals(t.getName().toLowerCase())){
                t.setCount(t.getCount()+1);
                isTaskNew = false;
            }
        }
        //if the task is taken from alltasks list the task needs to be copied
        if (isTaskNew==true){
            TaskTemplate newTask = new TaskTemplate(task);
            tasks.add(newTask);
            newTask.incrementCount();
            return newTask;
        }
        return task;
    }

}
