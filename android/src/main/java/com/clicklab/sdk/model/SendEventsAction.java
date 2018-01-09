package com.clicklab.sdk.model;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.clicklab.sdk.SyncJobService;
import com.clicklab.sdk.model.event.Event;
import com.clicklab.sdk.repository.DefaultDispatcher;
import com.clicklab.sdk.repository.Dispatcher;
import com.clicklab.sdk.repository.EventRepository;
import com.clicklab.sdk.repository.SdkRepository;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SendEventsAction implements Dispatcher.Callback {
    private static final int JOB_ID = 0;
    private WeakReference<Context> contextWeakReference;
    private EventRepository repository;
    private SdkRepository sdkRepository;
    private AtomicBoolean isSendingData;
    private WorkerThread handler;
    private Dispatcher dispatcher;

    public interface Callback {
        void finished();
    }

    private class DummyCallback implements Callback {
        @Override
        public void finished() {
        }
    }

    public SendEventsAction(EventRepository eventRepository, SdkRepository sdkRepository, Dispatcher dispatcher, Context context) {
        this.repository = eventRepository;
        this.sdkRepository = sdkRepository;
        this.dispatcher = dispatcher;
        this.contextWeakReference = new WeakReference<>(context);
        dispatcher.setCallback(this);
        isSendingData = new AtomicBoolean(false);
        handler = new WorkerThread(DefaultDispatcher.class.getName());
        handler.start();
    }

    public void execute() {
        execute(new DummyCallback());
    }

    public void execute(final Callback callback) {
        if (!repository.isEmpty() && isSendingData.compareAndSet(false, true)) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    List<Event> eventList;
                    try {
                        while (!repository.isEmpty()) {
                            eventList = repository.getEvents();
                            sendBunchOfEvents(eventList);
                        }
                    } catch (NetworkErrorSendException e) {
                        scheduleJob(contextWeakReference);
                    } finally {
                        isSendingData.set(false);
                        callback.finished();
                    }
                }
            };
            handler.post(run);
        }
    }

    @Override
    public void cookieRecieved(String cookie) {
        sdkRepository.setCookie(cookie);
    }

    private void sendBunchOfEvents(List<Event> eventList) throws NetworkErrorSendException {
        for (Event event : eventList) {
            event.setDataBeforeSend(sdkRepository.getCookie());
            try {
                dispatcher.sendOneEvent(event.getEventString(), event.getEventType());
                repository.removeEvent(event);
            } catch (NetworkErrorSendException e) {
                event.addRetryCount();
                repository.updateEvent(event);
                throw e;
            }
        }
    }

    private void scheduleJob(WeakReference<Context> context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context.get() != null) {

            JobScheduler jobScheduler = (JobScheduler)
                    context.get().getSystemService(Context.JOB_SCHEDULER_SERVICE);

            jobScheduler.schedule(buildJobInfo(context.get()));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private JobInfo buildJobInfo(Context context) {
        ComponentName serviceComponent = new ComponentName(context, SyncJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);
        builder.setMinimumLatency(2000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        return builder.build();
    }
}
