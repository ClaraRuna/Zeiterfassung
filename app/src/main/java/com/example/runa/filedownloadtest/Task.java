package com.example.runa.filedownloadtest;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by runa on 12.10.17.
 *
 */


public class Task extends TimeMeasuredAction{
    //the Template with the name
    TaskTemplate template;

    public Task (TaskTemplate template){
        this.template=template;
    }

    public String toString(){
        return template.toString();
    }
}
