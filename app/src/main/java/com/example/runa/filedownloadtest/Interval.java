package com.example.runa.filedownloadtest;

import java.util.GregorianCalendar;

/**
 * Created by runa on 12.10.17.
 */

public class Interval {
    GregorianCalendar startTime;
    GregorianCalendar endTime;

    public Interval(){
        this.startTime=new GregorianCalendar();
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void start(){
        startTime=new GregorianCalendar();
    }

    public void end(){
        endTime=new GregorianCalendar();
    }

    public long getTotalTime(){
        //if the ending of the intervall was not registered, return a total time of zero, corresponding to
        // "no specific time measured for this activitiy"
        if (endTime==null){
            return 0;
        }
        return endTime.getTimeInMillis()-startTime.getTimeInMillis();
    }

    public int getTotalMinutes(){
        //divide by 1000 for millis and 60 for seconds to get minutes
        return (int)(getTotalTime()/60000);
    }

    public int getTotalHours(){
        return getTotalMinutes()/60;
    }

}
