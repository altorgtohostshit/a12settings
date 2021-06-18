package com.android.settings.sound;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.AppSwitchPreference;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ResumableMediaAppsController extends BasePreferenceController {
    private static final String TAG = "ResumableMediaAppsCtrl";
    private PackageManager mPackageManager = this.mContext.getPackageManager();
    private PreferenceGroup mPreferenceGroup;
    private List<ResolveInfo> mResumeInfo = this.mPackageManager.queryIntentServices(new Intent("android.media.browse.MediaBrowserService"), 0);

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ResumableMediaAppsController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        List<ResolveInfo> queryIntentServices = this.mPackageManager.queryIntentServices(new Intent("android.media.browse.MediaBrowserService"), 0);
        this.mResumeInfo = queryIntentServices;
        if (queryIntentServices.size() > 0) {
            return 0;
        }
        return 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        CharSequence charSequence;
        super.displayPreference(preferenceScreen);
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
        Set<String> blockedMediaApps = getBlockedMediaApps();
        for (ResolveInfo componentInfo : this.mResumeInfo) {
            String str = componentInfo.getComponentInfo().packageName;
            MediaSwitchPreference mediaSwitchPreference = new MediaSwitchPreference(this.mContext, str);
            try {
                PackageManager packageManager = this.mPackageManager;
                charSequence = packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 0));
                try {
                    mediaSwitchPreference.setIcon(this.mPackageManager.getApplicationIcon(str));
                } catch (PackageManager.NameNotFoundException e) {
                    e = e;
                }
            } catch (PackageManager.NameNotFoundException e2) {
                e = e2;
                charSequence = str;
                Log.e(TAG, "Couldn't get app title", e);
                mediaSwitchPreference.setTitle(charSequence);
                mediaSwitchPreference.setOnPreferenceChangeListener(new ResumableMediaAppsController$$ExternalSyntheticLambda0(this, blockedMediaApps));
                mediaSwitchPreference.setChecked(!blockedMediaApps.contains(str));
                this.mPreferenceGroup.addPreference(mediaSwitchPreference);
            }
            mediaSwitchPreference.setTitle(charSequence);
            mediaSwitchPreference.setOnPreferenceChangeListener(new ResumableMediaAppsController$$ExternalSyntheticLambda0(this, blockedMediaApps));
            mediaSwitchPreference.setChecked(!blockedMediaApps.contains(str));
            this.mPreferenceGroup.addPreference(mediaSwitchPreference);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayPreference$0(Set set, Preference preference, Object obj) {
        MediaSwitchPreference mediaSwitchPreference = (MediaSwitchPreference) preference;
        boolean booleanValue = ((Boolean) obj).booleanValue();
        Log.d(TAG, "preference " + mediaSwitchPreference + " changed " + booleanValue);
        if (booleanValue) {
            set.remove(mediaSwitchPreference.getPackageName());
        } else {
            set.add(mediaSwitchPreference.getPackageName());
        }
        setBlockedMediaApps(set);
        return true;
    }

    class MediaSwitchPreference extends AppSwitchPreference {
        private String mPackageName;

        MediaSwitchPreference(Context context, String str) {
            super(context);
            this.mPackageName = str;
        }

        public String getPackageName() {
            return this.mPackageName;
        }
    }

    private Set<String> getBlockedMediaApps() {
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "qs_media_resumption_blocked");
        if (TextUtils.isEmpty(string)) {
            return new ArraySet();
        }
        String[] split = string.split(":");
        ArraySet arraySet = new ArraySet(split.length);
        Collections.addAll(arraySet, split);
        return arraySet;
    }

    private void setBlockedMediaApps(Set<String> set) {
        if (set == null || set.size() == 0) {
            Settings.Secure.putString(this.mContext.getContentResolver(), "qs_media_resumption_blocked", "");
            return;
        }
        Settings.Secure.putString(this.mContext.getContentResolver(), "qs_media_resumption_blocked", String.join(":", set));
    }
}
