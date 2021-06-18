package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkProviderBackupCallingPreferenceController extends BasePreferenceController implements LifecycleObserver {
    private static final String TAG = "NetProvBackupCallingCtrl";
    private Context mContext;
    private PreferenceCategory mPreferenceCategory;

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

    public NetworkProviderBackupCallingPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
    }

    public void init(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    public int getAvailabilityStatus() {
        List<SubscriptionInfo> activeSubscriptions = getActiveSubscriptions();
        if (activeSubscriptions.size() >= 2 && getPreferences(activeSubscriptions).size() >= 1) {
            return 0;
        }
        return 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        updatePreferenceList(preferenceCategory);
        preferenceCategory.setVisible(isAvailable());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            updatePreferenceList((PreferenceCategory) preference);
        }
    }

    private String getPreferenceKey(int i) {
        return getPreferenceKey() + "_subId_" + i;
    }

    /* access modifiers changed from: private */
    /* renamed from: getPreference */
    public SwitchPreference lambda$getPreferences$0(SubscriptionInfo subscriptionInfo) {
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        BackupCallingPreferenceController backupCallingPreferenceController = new BackupCallingPreferenceController(this.mContext, getPreferenceKey(subscriptionId));
        backupCallingPreferenceController.init(subscriptionId);
        if (backupCallingPreferenceController.getAvailabilityStatus(subscriptionId) != 0) {
            return null;
        }
        SwitchPreference switchPreference = new SwitchPreference(this.mContext);
        backupCallingPreferenceController.updateState(switchPreference);
        switchPreference.setTitle(SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext));
        return switchPreference;
    }

    private List<SwitchPreference> getPreferences(List<SubscriptionInfo> list) {
        return (List) list.stream().map(new C1050x9b4a5889(this)).filter(C1051x9b4a588a.INSTANCE).collect(Collectors.toList());
    }

    private List<SubscriptionInfo> getActiveSubscriptions() {
        return SubscriptionUtil.getActiveSubscriptions((SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class));
    }

    private void updatePreferenceList(PreferenceCategory preferenceCategory) {
        List<SwitchPreference> preferences = getPreferences(getActiveSubscriptions());
        preferenceCategory.removeAll();
        for (SwitchPreference addPreference : preferences) {
            preferenceCategory.addPreference(addPreference);
        }
    }
}
