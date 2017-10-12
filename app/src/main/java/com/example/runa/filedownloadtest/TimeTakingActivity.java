package com.example.runa.filedownloadtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by runa on 26.09.17.
 */

public class TimeTakingActivity extends AppCompatActivity {

    private Context context;
    private Customer customer;
    private TaskTemplate task;
    private long startTime;   //last starting time
    private long totalTime;  //total time in milliseconds
    private boolean isPaused;
    private CountDownTimer countDownTimer; //used to update the view

    //GUI
    private Button btnFinish;
    private Button btnPause;
    private TextView tvInfo;
    private TextView tvTotalTime;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_taking);
        initializeData();
        initializeGUI();

        updateView();
    }

    private void initializeData(){
        startTime=System.currentTimeMillis();
        totalTime=0;
        isPaused=false;
        task=(TaskTemplate)getIntent().getExtras().get("task");
        customer=(Customer)getIntent().getExtras().get("customer");
        countDownTimer=new CountDownTimer(86400000, 1000) {
            @Override
            public void onTick(long l) {
                updateTotalTime();
                updateView();
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void initializeGUI(){
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setText(getResources().getText(R.string.finishTask));
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setText(getResources().getText(R.string.pauseTask));
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                startMailActivity();
            }
        });

       btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPaused){
                    //run
                    isPaused=false;
                    btnPause.setText(R.string.pauseTask);
                    startTime=System.currentTimeMillis();
                    updateView();
                    countDownTimer.start();
                }
                else {
                    isPaused = true;
                    btnPause.setText(R.string.continueTask);
                    updateTotalTime();
                    countDownTimer.cancel();
                }
            }
        });



        tvInfo.setText(getResources().getString(R.string.taskInfoCustomer) + customer.getName() + getResources().getString(R.string.taskInfoTask) + task.getName());

    }

    private long updateTotalTime(){
        long now =System.currentTimeMillis();
        totalTime = totalTime + now - startTime;
        startTime = now;
        return totalTime;
    }

    private void updateView(){

            long hours;
            long minutes;
            long seconds;

            //calculate whole seconds/min/hrs
            seconds = totalTime / 1000;
            minutes = seconds / 60;
            hours = minutes / 60;

            //subtract whole hours from minutes
            minutes = minutes % 60;
            //subtract whole minutes from seconds
            seconds = seconds % 60;


            tvTotalTime.setText(String.format("%02d", (hours )) + ":" + String.format("%02d", (minutes )) + ":" + String.format("%02d", (seconds) ));


    }

    private void startMailActivity(){
        // get the data to write into the mail
        String date = Calendar.getInstance().getTime().toString();
        String text =
                date + "\n"+ getResources().getString(R.string.customer) + ": " +
                        customer.getName() + " (" + customer.getNumber() + ") \n" +
                        getResources().getString(R.string.task) + ": " + task.getName() + "\n" +
                        getResources().getString(R.string.duration) + ": " + tvTotalTime.getText();
        String [] recipient = {getResources().getString(R.string.eMailAdress)};
        // start the new intent and hand the data over
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setData(Uri.parse("mailto:"));
        mailIntent.setType("text/plain");
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + ": " + customer.getName() + " (" + customer.getNumber() + ")");
        mailIntent.putExtra(Intent.EXTRA_TEXT, text);
        mailIntent.putExtra(Intent.EXTRA_EMAIL, recipient);
        startActivity(mailIntent);
        //after the mail was sent, go back to main activity
        //recreate (reset) the activity
        this.setResult(RESULT_OK);
        this.finish();
    }

}
