package com.android.settings.deviceinfo.imei;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.slices.Sliceable;
import com.android.settingslib.Utils;
import java.util.ArrayList;
import java.util.List;

public class ImeiInfoPreferenceController extends BasePreferenceController {
    private Fragment mFragment;
    private final boolean mIsMultiSim;
    private final List<Preference> mPreferenceList = new ArrayList();
    private final TelephonyManager mTelephonyManager;

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

    public boolean useDynamicSliceSummary() {
        return true;
    }

    public ImeiInfoPreferenceController(Context context, String str) {
        super(context, str);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        this.mIsMultiSim = telephonyManager.getPhoneCount() <= 1 ? false : true;
    }

    public void setHost(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceList.add(findPreference);
        updatePreference(findPreference, 0);
        int order = findPreference.getOrder();
        for (int i = 1; i < this.mTelephonyManager.getPhoneCount(); i++) {
            Preference createNewPreference = createNewPreference(preferenceScreen.getContext());
            createNewPreference.setOrder(order + i);
            createNewPreference.setKey(getPreferenceKey() + i);
            preferenceScreen.addPreference(createNewPreference);
            this.mPreferenceList.add(createNewPreference);
            updatePreference(createNewPreference, i);
        }
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            int size = this.mPreferenceList.size();
            for (int i = 0; i < size; i++) {
                updatePreference(this.mPreferenceList.get(i), i);
            }
        }
    }

    public CharSequence getSummary() {
        return getSummary(0);
    }

    private CharSequence getSummary(int i) {
        if (getPhoneType(i) == 2) {
            return this.mTelephonyManager.getMeid(i);
        }
        return this.mTelephonyManager.getImei(i);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        int indexOf = this.mPreferenceList.indexOf(preference);
        if (indexOf == -1) {
            return false;
        }
        ImeiInfoDialogFragment.show(this.mFragment, indexOf, preference.getTitle().toString());
        return true;
    }

    public int getAvailabilityStatus() {
        return (!((UserManager) this.mContext.getSystemService(UserManager.class)).isAdminUser() || Utils.isWifiOnly(this.mContext)) ? 3 : 0;
    }

    public void copy() {
        Sliceable.setCopyContent(this.mContext, getSummary(0), getTitle(0));
    }

    private void updatePreference(Preference preference, int i) {
        preference.setTitle(getTitle(i));
        preference.setSummary(getSummary(i));
    }

    private CharSequence getTitleForGsmPhone(int i) {
        if (!this.mIsMultiSim) {
            return this.mContext.getString(R.string.status_imei);
        }
        return this.mContext.getString(R.string.imei_multi_sim, new Object[]{Integer.valueOf(i + 1)});
    }

    private CharSequence getTitleForCdmaPhone(int i) {
        if (!this.mIsMultiSim) {
            return this.mContext.getString(R.string.status_meid_number);
        }
        return this.mContext.getString(R.string.meid_multi_sim, new Object[]{Integer.valueOf(i + 1)});
    }

    private CharSequence getTitle(int i) {
        if (getPhoneType(i) == 2) {
            return getTitleForCdmaPhone(i);
        }
        return getTitleForGsmPhone(i);
    }

    private int getPhoneType(int i) {
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(i);
        return this.mTelephonyManager.getCurrentPhoneType(activeSubscriptionInfoForSimSlotIndex != null ? activeSubscriptionInfoForSimSlotIndex.getSubscriptionId() : Integer.MAX_VALUE);
    }

    /* access modifiers changed from: package-private */
    public Preference createNewPreference(Context context) {
        return new Preference(context);
    }
}
