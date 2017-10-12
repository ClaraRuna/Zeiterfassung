package com.example.runa.filedownloadtest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by runa on 12.10.17.
 * Session has a start and end time and subtasks wich also have start and end time
 */

public class Session extends TimeMeasuredAction{

    ArrayList<Task> tasks;

    public void startTask(TaskTemplate t){
        // if the task already exists in the tasklist, only add a new intervall
        for (Task task:tasks){
            if (task.toString().equals(t.toString())){
                task.start();
                return;
            }
        }
        //if no such task exists, add it
        tasks.add(new Task(t));
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }
}
