package com.example.runa.filedownloadtest;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by runa on 12.10.17.
 *
 * superclass for session and task: both need to measure Intervals of time
 *
 */

public class TimeMeasuredAction {

    ArrayList<Interval> intervals;
    Interval currentInterval;

    public TimeMeasuredAction (){
        currentInterval = new Interval();
    }

    public void endInterval(){
        currentInterval.end();
        intervals.add(currentInterval);
        currentInterval=null;
    }

    public void start(){
        //if the last interval has not been stopped, add it to the interval-list without calling end()
        //this signals that time was not properly measured
        if (currentInterval!=null){
            intervals.add(currentInterval);
        }
        //start measuring a new interval
        currentInterval=new Interval();
    }

    public ArrayList<Interval> getIntervals(){
        return intervals;
    }

}
