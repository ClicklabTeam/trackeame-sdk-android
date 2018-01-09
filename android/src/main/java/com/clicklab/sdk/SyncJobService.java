package com.clicklab.sdk;


import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;

@TargetApi(21)
public class SyncJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        ClickLab.get().syncEvents(new ClicklabClient.SyncEventsCallback() {
            @Override
            public void finished() {
                SyncJobService.this.jobFinished(jobParameters, false);
            }
        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
