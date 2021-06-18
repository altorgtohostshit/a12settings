package com.android.settings.core.instrumentation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.android.settings.R;
import com.android.settings.intelligence.LogProto$SettingsLog;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.LogWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class SettingsIntelligenceLogWriter implements LogWriter {
    private SendLogHandler mLogHandler;
    /* access modifiers changed from: private */
    public final Runnable mSendLogsRunnable = new SettingsIntelligenceLogWriter$$ExternalSyntheticLambda0(this);
    private List<LogProto$SettingsLog> mSettingsLogList = new LinkedList();

    public SettingsIntelligenceLogWriter() {
        HandlerThread handlerThread = new HandlerThread("SettingsIntelligenceLogWriter", 10);
        handlerThread.start();
        this.mLogHandler = new SendLogHandler(handlerThread.getLooper());
    }

    public void visible(Context context, int i, int i2, int i3) {
        action(i, 1, i2, "", i3);
    }

    public void hidden(Context context, int i, int i2) {
        action(0, 2, i, "", i2);
    }

    public void action(Context context, int i, Pair<Integer, Object>... pairArr) {
        action(0, i, 0, "", 0);
    }

    public void action(Context context, int i, int i2) {
        action(0, i, 0, "", i2);
    }

    public void action(Context context, int i, boolean z) {
        action(0, i, 0, "", z ? 1 : 0);
    }

    public void action(Context context, int i, String str) {
        action(0, i, 0, str, 1);
    }

    public void action(int i, int i2, int i3, String str, int i4) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        LogProto$SettingsLog.Builder pageId = LogProto$SettingsLog.newBuilder().setAttribution(i).setAction(i2).setPageId(i3);
        if (str == null) {
            str = "";
        }
        this.mLogHandler.post(new SettingsIntelligenceLogWriter$$ExternalSyntheticLambda1(this, (LogProto$SettingsLog) pageId.setChangedPreferenceKey(str).setChangedPreferenceIntValue(i4).setTimestamp(now.toString()).build()));
        if (i2 == 1665) {
            this.mLogHandler.sendLog();
        } else {
            this.mLogHandler.scheduleSendLog();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$action$0(LogProto$SettingsLog logProto$SettingsLog) {
        this.mSettingsLogList.add(logProto$SettingsLog);
    }

    static byte[] serialize(List<LogProto$SettingsLog> list) {
        int size = list.size();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeInt(size);
            for (LogProto$SettingsLog byteArray : list) {
                byte[] byteArray2 = byteArray.toByteArray();
                dataOutputStream.writeInt(byteArray2.length);
                dataOutputStream.write(byteArray2);
            }
            byte[] byteArray3 = byteArrayOutputStream.toByteArray();
            try {
                dataOutputStream.close();
            } catch (Exception e) {
                Log.e("IntelligenceLogWriter", "close error", e);
            }
            return byteArray3;
        } catch (Exception e2) {
            Log.e("IntelligenceLogWriter", "serialize error", e2);
            try {
                dataOutputStream.close();
            } catch (Exception e3) {
                Log.e("IntelligenceLogWriter", "close error", e3);
            }
            return null;
        } catch (Throwable th) {
            try {
                dataOutputStream.close();
            } catch (Exception e4) {
                Log.e("IntelligenceLogWriter", "close error", e4);
            }
            throw th;
        }
    }

    private class SendLogHandler extends Handler {
        SendLogHandler(Looper looper) {
            super(looper);
        }

        /* access modifiers changed from: package-private */
        public void scheduleSendLog() {
            removeCallbacks(SettingsIntelligenceLogWriter.this.mSendLogsRunnable);
            postDelayed(SettingsIntelligenceLogWriter.this.mSendLogsRunnable, 60000);
        }

        /* access modifiers changed from: package-private */
        public void sendLog() {
            removeCallbacks(SettingsIntelligenceLogWriter.this.mSendLogsRunnable);
            post(SettingsIntelligenceLogWriter.this.mSendLogsRunnable);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        Context appContext = FeatureFactory.getAppContext();
        if (appContext == null) {
            Log.e("IntelligenceLogWriter", "context is null");
            return;
        }
        String string = appContext.getString(R.string.config_settingsintelligence_log_action);
        if (!TextUtils.isEmpty(string) && !this.mSettingsLogList.isEmpty()) {
            Intent intent = new Intent();
            intent.setPackage(appContext.getString(R.string.config_settingsintelligence_package_name));
            intent.setAction(string);
            intent.putExtra("logs", serialize(this.mSettingsLogList));
            appContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
            this.mSettingsLogList.clear();
        }
    }
}
