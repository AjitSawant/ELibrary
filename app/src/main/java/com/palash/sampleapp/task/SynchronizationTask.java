package com.palash.sampleapp.task;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.buzzbox.mob.android.scheduler.NotificationMessage;
import com.buzzbox.mob.android.scheduler.Task;
import com.buzzbox.mob.android.scheduler.TaskResult;
import com.palash.sampleapp.api.JsonObjectMapper;
import com.palash.sampleapp.api.WebServiceConsumer;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.entiry.ELSync;
import com.palash.sampleapp.utilities.Constants;
import com.squareup.okhttp.Response;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SynchronizationTask implements Task {

    private Context context;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.SyncAdapter syncAdapter;
    private DatabaseAdapter.LoginAdapter loginAdapter;

    private WebServiceConsumer serviceConsumer;
    private Response response;
    private JsonObjectMapper objectMapper;

    private ELSync elSync = null;
    private ArrayList<ELSync> elSyncArrayList;

    private ELLogin elLogin = null;
    private ArrayList<ELLogin> elLoginArrayList;

    private TaskResult taskResult;

    private String responseString;
    private int responseCode;
    private int currentTask;

    @Override
    public TaskResult doWork(ContextWrapper contextWrapper) {
        Log.d(Constants.TAG, "SERVICE Time Start: " + DateFormat.getDateTimeInstance().format(new Date()));

        //Initialization
        responseCode = 0;
        responseString = null;
        context = contextWrapper;
        taskResult = new TaskResult();
        objectMapper = new JsonObjectMapper();

        //Database initialization
        databaseContract = new DatabaseContract(contextWrapper);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        syncAdapter = databaseAdapter.new SyncAdapter();
        loginAdapter = databaseAdapter.new LoginAdapter();

        elLoginArrayList = loginAdapter.listAll();
        elLogin = new ELLogin();
        if (elLoginArrayList != null && elLoginArrayList.size() > 0) {
            elLogin = elLoginArrayList.get(0);
            if (elLogin.getLoginStatus() != null && elLogin.getLoginStatus().equals("login")) {
                elSync = syncAdapter.GetSync();
                elSync.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
                elSync.setIsSyncing("1");
                syncAdapter.Add(elSync);

                if (elSync.getCurrentSync() != null && elSync.getCurrentSync().trim().length() > 0) {
                    currentTask = Integer.valueOf(elSync.getCurrentSync());
                }

                switch (currentTask) {
                    /*case Constants.SYNC_PAIN_ASSESSMENT:
                        GetPatientList();
                        StopSync();
                        break;*/
                    case Constants.SYNC_STOP:
                        StopSync();
                        Log.d(Constants.TAG, "Synchronization stopped for least data consumption");
                        break;
                }
                Log.d(Constants.TAG, "SERVICE Time End: " + DateFormat.getDateTimeInstance().format(new Date()));
            }
        }
        return taskResult;
    }

    /*private void GetPatientList() {
        try {
            profile = elProfileArrayList.get(0);
            responseCode = 0;
            responseString = null;
            //patient list
            Log.d(Constants.TAG, "Time Start: " + DateFormat.getDateTimeInstance().format(new Date()));
            serviceConsumer = new WebServiceConsumer(context, null, null, null);
            if (LocalSetting.checkUserType(profile)) {
                response = serviceConsumer.GET(Constants.PATIENT_LIST_URL + profile.getUserId());
            } else {
                response = serviceConsumer.GET(Constants.PATIENT_LIST_URL + "0");
            }
            Log.d(Constants.TAG, "Time End: " + DateFormat.getDateTimeInstance().format(new Date()));
            if (response != null) {
                responseString = response.body().string();
                responseCode = response.code();
                if (responseCode == Constants.HTTP_OK_200) {
                    Log.d(Constants.TAG, "Response String : " + responseString);
                    patientList = objectMapper.map(responseString, ELPatient.class);
                    if (patientList != null) {
                        if (patientList.size() != patientAdapter.TotalCount()) {
                            patientAdapter.delete();
                            for (int index = 0; index < patientList.size(); index++) {
                                patientAdapter.create(patientList.get(index));
                            }
                        } else {
                            Log.d(Constants.TAG, "Patient list count same");
                        }
                    }
                } else if (responseCode == Constants.HTTP_NO_RECORD_FOUND_OK_204) {
                    patientAdapter.delete();
                    Log.d(Constants.TAG, "No Patient list found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void StopSync() {
        try {
            elSync = syncAdapter.GetSync();
            if (elSync != null) {
                elSync.setCurrentSync("" + Constants.SYNC_STOP);
                syncAdapter.Update(elSync);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTitle() {
        return "SynchronizationTask";
    }

    @Override
    public String getId() {
        return "SynchronizationTask";
    }
}
