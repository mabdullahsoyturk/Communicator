package com.example.muhammet.communicator.sync;


import android.app.job.JobParameters;

import com.example.muhammet.communicator.utilities.NotificationUtilities;
import com.firebase.jobdispatcher.JobService;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public class CommunicatorFirebaseJobService extends JobService{

    private AsyncTask<Void, Void, Void> mFetchSpendingTask;

    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {
        mFetchSpendingTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                NotificationUtilities.remindUserForShoppingDay(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //  COMPLETED (6) Once the weather data is sync'd, call jobFinished with the appropriate arguements
                jobFinished(job, false);
            }
        };

        mFetchSpendingTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if (mFetchSpendingTask != null) {
            mFetchSpendingTask.cancel(true);
        }
        return true;
    }
}
