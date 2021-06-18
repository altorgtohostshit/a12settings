package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.TetheringManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.network.TetherEnabler;
import com.android.settings.slices.SliceBackgroundWorker;

public abstract class TetherBasePreferenceController extends TogglePreferenceController implements LifecycleObserver, DataSaverBackend.Listener, TetherEnabler.OnTetherStateUpdateListener {
    private static final String TAG = "TetherBasePreferenceController";
    private final DataSaverBackend mDataSaverBackend;
    private boolean mDataSaverEnabled;
    Preference mPreference;
    private TetherEnabler mTetherEnabler;
    int mTetheringState;
    final TetheringManager mTm;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public abstract int getTetherType();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public void onAllowlistStatusChanged(int i, boolean z) {
    }

    public void onDenylistStatusChanged(int i, boolean z) {
    }

    public abstract boolean shouldEnable();

    public abstract boolean shouldShow();

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    TetherBasePreferenceController(Context context, String str) {
        super(context, str);
        this.mTm = (TetheringManager) context.getSystemService(TetheringManager.class);
        DataSaverBackend dataSaverBackend = new DataSaverBackend(context);
        this.mDataSaverBackend = dataSaverBackend;
        this.mDataSaverEnabled = dataSaverBackend.isDataSaverEnabled();
    }

    public void setTetherEnabler(TetherEnabler tetherEnabler) {
        this.mTetherEnabler = tetherEnabler;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        TetherEnabler tetherEnabler = this.mTetherEnabler;
        if (tetherEnabler != null) {
            tetherEnabler.addListener(this);
        }
        this.mDataSaverBackend.addListener(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        TetherEnabler tetherEnabler = this.mTetherEnabler;
        if (tetherEnabler != null) {
            tetherEnabler.removeListener(this);
        }
        this.mDataSaverBackend.remListener(this);
    }

    public boolean isChecked() {
        return TetherEnabler.isTethering(this.mTetheringState, getTetherType());
    }

    public boolean setChecked(boolean z) {
        TetherEnabler tetherEnabler = this.mTetherEnabler;
        if (tetherEnabler == null) {
            return false;
        }
        if (z) {
            tetherEnabler.startTethering(getTetherType());
            return true;
        }
        tetherEnabler.stopTethering(getTetherType());
        return true;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(this.mPreferenceKey);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (isAvailable()) {
            preference.setEnabled(getAvailabilityStatus() != 5);
        }
    }

    public int getAvailabilityStatus() {
        if (!shouldShow()) {
            return 2;
        }
        return (this.mDataSaverEnabled || !shouldEnable()) ? 5 : 0;
    }

    public void onTetherStateUpdated(int i) {
        this.mTetheringState = i;
        updateState(this.mPreference);
    }

    public void onDataSaverChanged(boolean z) {
        this.mDataSaverEnabled = z;
    }
}
