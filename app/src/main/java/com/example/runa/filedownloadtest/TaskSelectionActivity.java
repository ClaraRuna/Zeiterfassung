package com.example.runa.filedownloadtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by runa on 24.09.17.
 */

public class TaskSelectionActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Task> customerTasks;
    private ArrayList<Task> allTasks;
    private ArrayList<Task> tasks; //the tasks that the adapter is working with: if search is empty: customer task, otherwise allTasks (filtered)
    private ArrayAdapter taskListAdapter;
    private Customer customer;
    private PersistenceManager persistenceManager;

    //GUI Elements
    TextView tvSelectTask;
    EditText etSearch;
    ListView lvTasks;
    Button btnNewTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        persistenceManager = new PersistenceManager(context);
        super.onCreate(savedInstanceState);

        customer = (Customer) getIntent().getExtras().get("customer") ;
        Log.d("selected customer", customer.toString());
        customerTasks=new ArrayList<Task>(customer.getTasks());
        allTasks=new ArrayList<Task>((ArrayList<Task>)getIntent().getExtras().get("allTasks"));
        //sort tasks by count
        Collections.sort(customerTasks);
        Collections.sort(allTasks);

        setContentView(R.layout.activity_task_selection);

        initializeGUI();
    }

    private void initializeGUI(){
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnNewTask = (Button) findViewById(R.id.btnNew);
        btnNewTask.setClickable(false);
        btnNewTask.setVisibility(View.INVISIBLE);
        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new task
                Task task = new Task ();
                task.setName(etSearch.getText().toString());
                startTimeTakingActivity(task);
                btnNewTask.setVisibility(View.INVISIBLE);
            }
        });
        tasks=customerTasks;
        //simple_list_item_2 would also be possible
        taskListAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1,  tasks);
        update();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                update();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        //Initialize ListView
        tvSelectTask = (TextView) findViewById(R.id.tvSelectTask);
        tvSelectTask.setText(getResources().getString(R.string.selectTask));

        //tvSelectTask.setText(R.string.selectTask);
        lvTasks = (ListView) findViewById(R.id.lvTasks);
        lvTasks.setAdapter(taskListAdapter);
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startTimeTakingActivity(position);
            }
        });

    }

    private void update(){
        //if nothing is searched for, show customers tasks
        if (etSearch.getText().length()==0){
            tasks=customerTasks;
            taskListAdapter.notifyDataSetChanged();
            //newTask button is not clickable
            btnNewTask.setClickable(false);
            btnNewTask.setVisibility(View.INVISIBLE);
        }
        else {
            tasks=allTasks;
            taskListAdapter.getFilter().filter(etSearch.getText());
            //if no task matches the search criteria, offer to create a new one
            if (taskListAdapter.isEmpty()){
                //button new Task is clickable
                btnNewTask.setClickable(true);
                btnNewTask.setVisibility(View.VISIBLE);

            }
            else {
                // button new task not clickable
                btnNewTask.setClickable(false);
                btnNewTask.setVisibility(View.INVISIBLE);

            }
        }
        Log.d("c.getTasks().size()", Integer.toString(customer.getTasks().size()));
        Log.d("customerTasks.size()", Integer.toString(customerTasks.size()));
        Log.d("allTasks.size()", Integer.toString(allTasks.size()));
        for (Task t : customer.getTasks()){
            Log.d(t.getName(), Integer.toString(t.getCount()));
        }
    }

    private void startTimeTakingActivity(int position){
        Task task = tasks.get(position);
        startTimeTakingActivity(task);
    }

    private void startTimeTakingActivity (Task task){
        customer.doTask(task);
        Intent i = new Intent ( this, TimeTakingActivity.class);
        i.putExtra("task",task );
        startActivity(i);
    }



}
