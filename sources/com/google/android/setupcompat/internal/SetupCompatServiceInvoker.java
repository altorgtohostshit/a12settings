package com.google.android.setupcompat.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.setupcompat.ISetupCompatService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SetupCompatServiceInvoker {
    private static final long MAX_WAIT_TIME_FOR_CONNECTION_MS = TimeUnit.SECONDS.toMillis(10);
    @SuppressLint({"StaticFieldLeak"})
    private static SetupCompatServiceInvoker instance;
    private final Context context;
    private final ExecutorService loggingExecutor = ExecutorProvider.setupCompatServiceInvoker.get();
    private final ExecutorService setupCompatExecutor = ExecutorProvider.setupCompatExecutor.get();
    private final long waitTimeInMillisForServiceConnection = MAX_WAIT_TIME_FOR_CONNECTION_MS;

    public void logMetricEvent(int i, Bundle bundle) {
        try {
            this.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda0(this, i, bundle));
        } catch (RejectedExecutionException e) {
            Log.e("SucServiceInvoker", String.format("Metric of type %d dropped since queue is full.", new Object[]{Integer.valueOf(i)}), e);
        }
    }

    public void bindBack(String str, Bundle bundle) {
        try {
            this.setupCompatExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda1(this, str, bundle));
        } catch (RejectedExecutionException e) {
            Log.e("SucServiceInvoker", String.format("Screen %s bind back fail.", new Object[]{str}), e);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeLogMetric */
    public void lambda$logMetricEvent$0(int i, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.logMetric(i, bundle, Bundle.EMPTY);
            } else {
                Log.w("SucServiceInvoker", "logMetric failed since service reference is null. Are the permissions valid?");
            }
        } catch (RemoteException | IllegalStateException | InterruptedException | TimeoutException e) {
            Log.e("SucServiceInvoker", String.format("Exception occurred while trying to log metric = [%s]", new Object[]{bundle}), e);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeBindBack */
    public void lambda$bindBack$1(String str, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.validateActivity(str, bundle);
            } else {
                Log.w("SucServiceInvoker", "BindBack failed since service reference is null. Are the permissions valid?");
            }
        } catch (RemoteException | InterruptedException | TimeoutException e) {
            Log.e("SucServiceInvoker", String.format("Exception occurred while %s trying bind back to SetupWizard.", new Object[]{str}), e);
        }
    }

    private SetupCompatServiceInvoker(Context context2) {
        this.context = context2;
    }

    public static synchronized SetupCompatServiceInvoker get(Context context2) {
        SetupCompatServiceInvoker setupCompatServiceInvoker;
        synchronized (SetupCompatServiceInvoker.class) {
            if (instance == null) {
                instance = new SetupCompatServiceInvoker(context2.getApplicationContext());
            }
            setupCompatServiceInvoker = instance;
        }
        return setupCompatServiceInvoker;
    }

    static void setInstanceForTesting(SetupCompatServiceInvoker setupCompatServiceInvoker) {
        instance = setupCompatServiceInvoker;
    }
}
