package com.clicklab.sdk;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@PrepareForTest({ClickLab.class})
public class SyncJobServiceTest {
    SyncJobService service;

    @Before
    public void setUp() {
        service = new SyncJobService();
    }

    @Test
    public void whenOnStartJobThenFalseIsReturned() throws Exception {
        ClicklabClient mockedClient = mock(ClicklabClient.class);
        PowerMockito.mockStatic(ClickLab.class);
        when(ClickLab.get()).thenReturn(mockedClient);

        assertFalse(service.onStartJob(mock(JobParameters.class)));
        verify(mockedClient, times(1)).syncEvents(any(ClicklabClient.SyncEventsCallback.class));
    }

    @Test
    public void whenOnStopJobThenFalseIsReturned() throws Exception {
        assertFalse(service.onStopJob(mock(JobParameters.class)));
    }
}