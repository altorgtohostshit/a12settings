package com.google.android.settings.gamemode;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.widget.LayoutPreference;

public class GameModeHeaderController extends BasePreferenceController {
    private static final String TAG = "GameModeHeaderController";
    private ApplicationsState.AppEntry mAppEntry;
    private EntityHeaderController mEntityHeaderController;
    private LayoutPreference mHeader;
    private PackageInfo mPackageInfo;
    private String mPackageName;
    private GameModeSettings mParent;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 1;
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

    public GameModeHeaderController(Context context, String str) {
        super(context, str);
    }

    public void init(GameModeSettings gameModeSettings, String str) {
        this.mParent = gameModeSettings;
        this.mPackageName = str;
        FragmentActivity activity = gameModeSettings.getActivity();
        if (activity == null) {
            Log.e(TAG, "Activity is null");
            return;
        }
        ApplicationsState instance = ApplicationsState.getInstance(activity.getApplication());
        UserHandle.myUserId();
        ApplicationsState.AppEntry entry = instance.getEntry(this.mPackageName, UserHandle.myUserId());
        this.mAppEntry = entry;
        if (entry != null) {
            try {
                PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(this.mPackageName, 0);
                this.mPackageInfo = packageInfo;
                AppUtils.isInstant(packageInfo.applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Exception when retrieving package:" + this.mAppEntry.info.packageName, e);
            }
        } else {
            this.mPackageInfo = null;
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mHeader = (LayoutPreference) preferenceScreen.findPreference(getPreferenceKey());
        FragmentActivity activity = this.mParent.getActivity();
        this.mEntityHeaderController = EntityHeaderController.newInstance(activity, this.mParent, this.mHeader.findViewById(R.id.entity_header)).setPackageName(this.mPackageName);
        this.mEntityHeaderController.setLabel(this.mAppEntry).setIcon(this.mAppEntry).setIsInstantApp(AppUtils.isInstant(this.mPackageInfo.applicationInfo)).done((Activity) activity, false);
    }
}
