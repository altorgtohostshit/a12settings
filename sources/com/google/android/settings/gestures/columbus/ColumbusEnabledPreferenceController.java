package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Pair;
import android.widget.Switch;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.google.android.settings.gestures.columbus.ColumbusGestureHelper;

public class ColumbusEnabledPreferenceController extends BasePreferenceController implements OnMainSwitchChangeListener, ColumbusGestureHelper.GestureListener, LifecycleObserver, OnPause, OnResume {
    static final int COLUMBUS_DISABLED = 0;
    static final int COLUMBUS_ENABLED = 1;
    static final String SECURE_KEY_COLUMBUS_ENABLED = "columbus_enabled";
    static final String SECURE_KEY_COLUMBUS_GESTURE_SETUP_COMPLETE = "columbus_gesture_setup_complete";
    private final ColumbusGestureHelper mColumbusGestureHelper;
    private final Handler mHandler = new Handler(Looper.myLooper());
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private MainSwitchPreference mSwitchBar;

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

    public ColumbusEnabledPreferenceController(Context context, String str) {
        super(context, str);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mColumbusGestureHelper = new ColumbusGestureHelper(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
            this.mSwitchBar = mainSwitchPreference;
            if (mainSwitchPreference != null) {
                mainSwitchPreference.addOnSwitchChangeListener(this);
            }
        }
    }

    public void updateState(Preference preference) {
        if (this.mSwitchBar != null) {
            this.mSwitchBar.updateStatus(ColumbusPreferenceController.isColumbusEnabled(this.mContext));
        }
    }

    public int getAvailabilityStatus() {
        return ColumbusPreferenceController.isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_ENABLED, z ? 1 : 0);
        if (z) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_GESTURE_SETUP_COMPLETE, 1);
        }
        this.mMetricsFeatureProvider.action(this.mContext, z ? 1740 : 1741, (Pair<Integer, Object>[]) new Pair[0]);
    }

    public void onResume() {
        this.mColumbusGestureHelper.bindToColumbusServiceProxy();
        this.mColumbusGestureHelper.setListener(this);
    }

    public void onPause() {
        this.mColumbusGestureHelper.setListener((ColumbusGestureHelper.GestureListener) null);
        this.mColumbusGestureHelper.unbindFromColumbusServiceProxy();
    }

    public void onTrigger() {
        this.mHandler.post(new ColumbusEnabledPreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrigger$0() {
        Toast.makeText(this.mSwitchBar.getContext(), R.string.columbus_gesture_detected, 0).show();
    }
}
