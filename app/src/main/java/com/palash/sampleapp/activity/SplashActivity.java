package com.palash.sampleapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.sampleapp.R;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.task.SynchronizationTask;
import com.palash.sampleapp.utilities.Constants;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private Context context;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.LoginAdapter loginAdapter;

    private ArrayList<ELLogin> listLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        InitScheduler();
        Init();
        InitView();
    }

    private void InitScheduler() {
        try {
            SchedulerManager.getInstance().saveTask(this, "*/1 * * * *", SynchronizationTask.class);
            SchedulerManager.getInstance().restart(this, SynchronizationTask.class);
            SchedulerManager.getInstance().runNow(this, SynchronizationTask.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Init() {
        try {
            context = this;
            getSupportActionBar().hide();
            databaseContract = new DatabaseContract(context);
            databaseAdapter = new DatabaseAdapter(databaseContract);
            loginAdapter = databaseAdapter.new LoginAdapter();
            listLogin = loginAdapter.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitView() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listLogin != null && listLogin.size() > 0 && listLogin.get(0).getLoginStatus().equals(Constants.STATUS_LOG_IN)) {
                        startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {
                        startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                    finish();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
