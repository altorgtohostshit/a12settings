package com.android.settings.inputmethod;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.inputmethod.InputMethodPreference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class InputMethodPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart {
    private DevicePolicyManager mDpm;
    private InputMethodManager mImm;
    private Preference mPreference;
    PreferenceScreen mScreen;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public InputMethodPreferenceController(Context context, String str) {
        super(context, str);
        this.mImm = (InputMethodManager) context.getSystemService(InputMethodManager.class);
        this.mDpm = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        updateInputMethodPreferenceViews();
    }

    private void updateInputMethodPreferenceViews() {
        int i;
        ArrayList arrayList = new ArrayList();
        List permittedInputMethodsForCurrentUser = this.mDpm.getPermittedInputMethodsForCurrentUser();
        List<InputMethodInfo> enabledInputMethodList = this.mImm.getEnabledInputMethodList();
        if (enabledInputMethodList == null) {
            i = 0;
        } else {
            i = enabledInputMethodList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            InputMethodInfo inputMethodInfo = enabledInputMethodList.get(i2);
            boolean z = permittedInputMethodsForCurrentUser == null || permittedInputMethodsForCurrentUser.contains(inputMethodInfo.getPackageName());
            Drawable loadIcon = inputMethodInfo.loadIcon(this.mContext.getPackageManager());
            InputMethodPreference inputMethodPreference = new InputMethodPreference(this.mScreen.getContext(), inputMethodInfo, false, z, (InputMethodPreference.OnSavePreferenceListener) null);
            inputMethodPreference.setIcon(loadIcon);
            arrayList.add(inputMethodPreference);
        }
        arrayList.sort(new InputMethodPreferenceController$$ExternalSyntheticLambda0(Collator.getInstance()));
        this.mScreen.removeAll();
        for (int i3 = 0; i3 < i; i3++) {
            InputMethodPreference inputMethodPreference2 = (InputMethodPreference) arrayList.get(i3);
            inputMethodPreference2.setOrder(i3);
            this.mScreen.addPreference(inputMethodPreference2);
            inputMethodPreference2.updatePreferenceViews();
        }
        this.mScreen.addPreference(this.mPreference);
    }
}
