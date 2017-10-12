package com.example.runa.filedownloadtest;

import java.io.Serializable;

/**
 * Created by runa on 25.09.17.
 */

public class TaskTemplate implements Serializable, Comparable<TaskTemplate>{
    private String name;
    private int count;

    public TaskTemplate(){
        //if a task with these values is created something somewhere went wrong
        name="default";
        count=0;
    }

    public TaskTemplate(TaskTemplate task){
        name=task.getName();
        count=0;
    }

    public void setCount(int count){
        this.count=count;
    }

    public int getCount (){
        return count;
    }

    public void incrementCount(){
        count++;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public int compareTo(TaskTemplate t){
        if (name==t.getName()) return 0;
        else return count-t.getCount();
    }

    public String toString(){
        return name;
    }


}
