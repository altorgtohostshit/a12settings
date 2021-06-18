package com.android.settings.notification;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RemoteVolumeGroupController extends BasePreferenceController implements Preference.OnPreferenceChangeListener, LifecycleObserver, OnDestroy, LocalMediaManager.DeviceCallback {
    private static final String KEY_REMOTE_VOLUME_GROUP = "remote_media_group";
    static final String SWITCHER_PREFIX = "OUTPUT_SWITCHER";
    private static final String TAG = "RemoteVolumePrefCtr";
    LocalMediaManager mLocalMediaManager;
    private PreferenceCategory mPreferenceCategory;
    MediaRouter2Manager mRouterManager;
    private List<RoutingSessionInfo> mRoutingSessionInfos = new ArrayList();

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_REMOTE_VOLUME_GROUP;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ void onDeviceAttributesChanged() {
        super.onDeviceAttributesChanged();
    }

    public /* bridge */ /* synthetic */ void onRequestFailed(int i) {
        super.onRequestFailed(i);
    }

    public void onSelectedDeviceStateChanged(MediaDevice mediaDevice, int i) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RemoteVolumeGroupController(Context context, String str) {
        super(context, str);
        if (this.mLocalMediaManager == null) {
            LocalMediaManager localMediaManager = new LocalMediaManager(this.mContext, (String) null, (Notification) null);
            this.mLocalMediaManager = localMediaManager;
            localMediaManager.registerCallback(this);
            this.mLocalMediaManager.startScan();
        }
        this.mRouterManager = MediaRouter2Manager.getInstance(context);
    }

    public int getAvailabilityStatus() {
        return this.mRoutingSessionInfos.isEmpty() ? 2 : 1;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        initRemoteMediaSession();
        refreshPreference();
    }

    private void initRemoteMediaSession() {
        this.mRoutingSessionInfos.clear();
        for (RoutingSessionInfo next : this.mLocalMediaManager.getActiveMediaSession()) {
            if (!next.isSystemSession()) {
                this.mRoutingSessionInfos.add(next);
            }
        }
    }

    public void onDestroy() {
        this.mLocalMediaManager.unregisterCallback(this);
        this.mLocalMediaManager.stopScan();
    }

    private synchronized void refreshPreference() {
        boolean z;
        if (!isAvailable()) {
            this.mPreferenceCategory.setVisible(false);
            return;
        }
        CharSequence text = this.mContext.getText(R.string.remote_media_volume_option_title);
        this.mPreferenceCategory.setVisible(true);
        for (RoutingSessionInfo next : this.mRoutingSessionInfos) {
            CharSequence applicationLabel = Utils.getApplicationLabel(this.mContext, next.getClientPackageName());
            RemoteVolumeSeekBarPreference remoteVolumeSeekBarPreference = (RemoteVolumeSeekBarPreference) this.mPreferenceCategory.findPreference(next.getId());
            if (remoteVolumeSeekBarPreference == null) {
                RemoteVolumeSeekBarPreference remoteVolumeSeekBarPreference2 = new RemoteVolumeSeekBarPreference(this.mContext);
                remoteVolumeSeekBarPreference2.setKey(next.getId());
                remoteVolumeSeekBarPreference2.setTitle(text);
                remoteVolumeSeekBarPreference2.setMax(next.getVolumeMax());
                remoteVolumeSeekBarPreference2.setProgress(next.getVolume());
                remoteVolumeSeekBarPreference2.setMin(0);
                remoteVolumeSeekBarPreference2.setOnPreferenceChangeListener(this);
                remoteVolumeSeekBarPreference2.setIcon((int) R.drawable.ic_volume_remote);
                this.mPreferenceCategory.addPreference(remoteVolumeSeekBarPreference2);
            } else if (remoteVolumeSeekBarPreference.getProgress() != next.getVolume()) {
                remoteVolumeSeekBarPreference.setProgress(next.getVolume());
            }
            Preference findPreference = this.mPreferenceCategory.findPreference(SWITCHER_PREFIX + next.getId());
            boolean shouldDisableMediaOutput = this.mLocalMediaManager.shouldDisableMediaOutput(next.getClientPackageName());
            String string = this.mContext.getString(R.string.media_output_label_title, new Object[]{applicationLabel});
            if (findPreference != null) {
                if (!shouldDisableMediaOutput) {
                    applicationLabel = string;
                }
                findPreference.setTitle(applicationLabel);
                findPreference.setSummary(next.getName());
                findPreference.setEnabled(!shouldDisableMediaOutput);
            } else {
                Preference preference = new Preference(this.mContext);
                preference.setKey(SWITCHER_PREFIX + next.getId());
                if (!shouldDisableMediaOutput) {
                    applicationLabel = string;
                }
                preference.setTitle(applicationLabel);
                preference.setSummary(next.getName());
                preference.setEnabled(!shouldDisableMediaOutput);
                this.mPreferenceCategory.addPreference(preference);
            }
        }
        for (int i = 0; i < this.mPreferenceCategory.getPreferenceCount(); i += 2) {
            Preference preference2 = this.mPreferenceCategory.getPreference(i);
            Iterator<RoutingSessionInfo> it = this.mRoutingSessionInfos.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (TextUtils.equals(preference2.getKey(), it.next().getId())) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                Preference preference3 = this.mPreferenceCategory.getPreference(i + 1);
                if (preference3 != null) {
                    this.mPreferenceCategory.removePreference(preference2);
                    this.mPreferenceCategory.removePreference(preference3);
                }
            }
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        ThreadUtils.postOnBackgroundThread((Runnable) new RemoteVolumeGroupController$$ExternalSyntheticLambda1(this, preference, obj));
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceChange$0(Preference preference, Object obj) {
        this.mLocalMediaManager.adjustSessionVolume(preference.getKey(), ((Integer) obj).intValue());
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!preference.getKey().startsWith(SWITCHER_PREFIX)) {
            return false;
        }
        for (RoutingSessionInfo next : this.mRoutingSessionInfos) {
            if (TextUtils.equals(next.getId(), preference.getKey().substring(15))) {
                this.mContext.sendBroadcast(new Intent().setAction("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG").setPackage("com.android.systemui").putExtra("package_name", next.getClientPackageName()));
                return true;
            }
        }
        return false;
    }

    public void onDeviceListUpdate(List<MediaDevice> list) {
        if (this.mPreferenceCategory != null) {
            ThreadUtils.postOnMainThread(new RemoteVolumeGroupController$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDeviceListUpdate$1() {
        initRemoteMediaSession();
        refreshPreference();
    }
}
