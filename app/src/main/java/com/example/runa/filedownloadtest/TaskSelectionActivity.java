package com.example.runa.filedownloadtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Filter;

/**
 * Created by runa on 24.09.17.
 */

public class TaskSelectionActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Task> customerTasks;
    private ArrayList<Task> allTasks;
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

        setContentView(R.layout.activity_task_selection);

        initializeData();
        initializeGUI();
    }

    private void initializeData(){
        customer = (Customer) getIntent().getExtras().get("customer") ;
        Log.d("selected customer", customer.toString());
        customerTasks=new ArrayList<Task>(customer.getTasks());
        Log.d("customerTasks.size()", Integer.toString(customerTasks.size()));
        allTasks=new ArrayList<Task>((ArrayList<Task>)getIntent().getExtras().get("allTasks"));
        //sort tasks by count
        Collections.sort(customerTasks);
        Collections.sort(allTasks);
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
        //simple_list_item_2 would also be possible
        taskListAdapter =
                new TaskAdapter(this, android.R.layout.simple_list_item_1,  customerTasks, allTasks);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                update();
            }
        });
        //Initialize lnfo textField ("select task @ customer")
        tvSelectTask = (TextView) findViewById(R.id.tvInfo);
        tvSelectTask.setText(getResources().getString(R.string.customer) + ": " + customer.getName() +"\n" + getResources().getString(R.string.selectTask));

        lvTasks = (ListView) findViewById(R.id.lvTasks);
        lvTasks.setAdapter(taskListAdapter);
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startTimeTakingActivity(position);
            }
        });

    }

    private class Updater implements android.widget.Filter.FilterListener{
        @Override
        public void onFilterComplete(int i) {
            update();
        }
    }

    private void update(){
        taskListAdapter.getFilter().filter(etSearch.getText(), lvTasks);
        //i tried some stuff here, nothing worked
        Log.d("tlAdapter.getCount()", Integer.toString(taskListAdapter.getCount()));
        Log.d("lvTasks.getCount()", Integer.toString(lvTasks.getCount()));

    }

    private void startTimeTakingActivity(int position){
        Task task = (Task) taskListAdapter.getItem(position);
        startTimeTakingActivity(task);
    }

    private void startTimeTakingActivity (Task task){
        customer.doTask(task);
        Log.d("executing", "customer.doTask(task)");
        Log.d("c.getTasks.size()", Integer.toString(customer.getTasks().size()));
        Intent i = new Intent ( this, TimeTakingActivity.class);
        persistenceManager.writeCustomer(customer);
        i.putExtra("task",task );
        i.putExtra("customer", customer);
        startActivity(i);
    }



}
