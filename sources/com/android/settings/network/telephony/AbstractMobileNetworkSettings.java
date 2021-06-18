package com.android.settings.network.telephony;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settings.network.telephony.TelephonyStatusControlSession;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class AbstractMobileNetworkSettings extends RestrictedDashboardFragment {
    private List<AbstractPreferenceController> mHiddenControllerList = new ArrayList();
    private boolean mIsRedrawRequired;

    AbstractMobileNetworkSettings(String str) {
        super(str);
    }

    /* access modifiers changed from: package-private */
    public List<AbstractPreferenceController> getPreferenceControllersAsList() {
        ArrayList arrayList = new ArrayList();
        getPreferenceControllers().forEach(new AbstractMobileNetworkSettings$$ExternalSyntheticLambda3(arrayList));
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public Preference searchForPreference(PreferenceScreen preferenceScreen, AbstractPreferenceController abstractPreferenceController) {
        String preferenceKey = abstractPreferenceController.getPreferenceKey();
        if (TextUtils.isEmpty(preferenceKey)) {
            return null;
        }
        return preferenceScreen.findPreference(preferenceKey);
    }

    /* access modifiers changed from: package-private */
    public TelephonyStatusControlSession setTelephonyAvailabilityStatus(Collection<AbstractPreferenceController> collection) {
        return new TelephonyStatusControlSession.Builder(collection).build();
    }

    public void onExpandButtonClick() {
        this.mHiddenControllerList.stream().filter(AbstractMobileNetworkSettings$$ExternalSyntheticLambda4.INSTANCE).forEach(new AbstractMobileNetworkSettings$$ExternalSyntheticLambda0(getPreferenceScreen()));
        super.onExpandButtonClick();
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceStates() {
        this.mHiddenControllerList.clear();
        if (this.mIsRedrawRequired) {
            redrawPreferenceControllers();
            return;
        }
        getPreferenceControllersAsList().forEach(new AbstractMobileNetworkSettings$$ExternalSyntheticLambda2(this, getPreferenceScreen()));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateVisiblePreferenceControllers */
    public void lambda$updatePreferenceStates$3(PreferenceScreen preferenceScreen, AbstractPreferenceController abstractPreferenceController) {
        Preference searchForPreference = searchForPreference(preferenceScreen, abstractPreferenceController);
        if (searchForPreference != null) {
            if (!isPreferenceExpanded(searchForPreference)) {
                this.mHiddenControllerList.add(abstractPreferenceController);
            } else if (abstractPreferenceController.isAvailable()) {
                abstractPreferenceController.updateState(searchForPreference);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void redrawPreferenceControllers() {
        this.mHiddenControllerList.clear();
        if (!isResumed()) {
            this.mIsRedrawRequired = true;
            return;
        }
        this.mIsRedrawRequired = false;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        List<AbstractPreferenceController> preferenceControllersAsList = getPreferenceControllersAsList();
        TelephonyStatusControlSession telephonyAvailabilityStatus = setTelephonyAvailabilityStatus(preferenceControllersAsList);
        preferenceControllersAsList.forEach(new AbstractMobileNetworkSettings$$ExternalSyntheticLambda1(this, getPreferenceScreen()));
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        Log.d("AbsNetworkSettings", "redraw fragment: +" + (elapsedRealtime2 - elapsedRealtime) + "ms");
        telephonyAvailabilityStatus.close();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$redrawPreferenceControllers$4(PreferenceScreen preferenceScreen, AbstractPreferenceController abstractPreferenceController) {
        abstractPreferenceController.displayPreference(preferenceScreen);
        lambda$updatePreferenceStates$3(preferenceScreen, abstractPreferenceController);
    }
}
