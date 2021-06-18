package com.google.android.settings.gamemode;

import android.app.GameManager;
import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.RadioButtonPreference;

public class GameModeController extends BasePreferenceController implements RadioButtonPreference.OnClickListener {
    @VisibleForTesting
    static final String GAME_MODE_BATTERY_PREFERENCE_KEY = "game_mode_battery";
    @VisibleForTesting
    static final String GAME_MODE_PERFORMANCE_PREFERENCE_KEY = "game_mode_performance";
    @VisibleForTesting
    static final String GAME_MODE_STANDARD_PREFERENCE_KEY = "game_mode_standard";
    private static final String TAG = "GameModeController";
    @VisibleForTesting
    RadioButtonPreference mBatteryRadioButtonPref;
    private GameManager mGameManager;
    private String mPackageName;
    @VisibleForTesting
    RadioButtonPreference mPerformanceRadioButtonPref;
    @VisibleForTesting
    RadioButtonPreference mStandardRadioButtonPref;

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

    public GameModeController(Context context, String str) {
        super(context, str);
        this.mGameManager = (GameManager) context.getSystemService(GameManager.class);
    }

    public void init(String str) {
        this.mPackageName = str;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mStandardRadioButtonPref = (RadioButtonPreference) preferenceScreen.findPreference(GAME_MODE_STANDARD_PREFERENCE_KEY);
        this.mPerformanceRadioButtonPref = (RadioButtonPreference) preferenceScreen.findPreference(GAME_MODE_PERFORMANCE_PREFERENCE_KEY);
        this.mBatteryRadioButtonPref = (RadioButtonPreference) preferenceScreen.findPreference(GAME_MODE_BATTERY_PREFERENCE_KEY);
        int gameMode = this.mGameManager.getGameMode(this.mPackageName);
        boolean z = false;
        this.mStandardRadioButtonPref.setChecked(gameMode == 1);
        this.mPerformanceRadioButtonPref.setChecked(gameMode == 2);
        RadioButtonPreference radioButtonPreference = this.mBatteryRadioButtonPref;
        if (gameMode == 3) {
            z = true;
        }
        radioButtonPreference.setChecked(z);
        this.mStandardRadioButtonPref.setOnClickListener(this);
        this.mPerformanceRadioButtonPref.setOnClickListener(this);
        this.mBatteryRadioButtonPref.setOnClickListener(this);
    }

    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        boolean z = true;
        if (radioButtonPreference.getKey().equals(GAME_MODE_STANDARD_PREFERENCE_KEY)) {
            this.mGameManager.setGameMode(this.mPackageName, 1);
        } else if (radioButtonPreference.getKey().equals(GAME_MODE_PERFORMANCE_PREFERENCE_KEY)) {
            this.mGameManager.setGameMode(this.mPackageName, 2);
        } else if (radioButtonPreference.getKey().equals(GAME_MODE_BATTERY_PREFERENCE_KEY)) {
            this.mGameManager.setGameMode(this.mPackageName, 3);
        }
        int gameMode = this.mGameManager.getGameMode(this.mPackageName);
        this.mStandardRadioButtonPref.setChecked(gameMode == 1);
        this.mPerformanceRadioButtonPref.setChecked(gameMode == 2);
        RadioButtonPreference radioButtonPreference2 = this.mBatteryRadioButtonPref;
        if (gameMode != 3) {
            z = false;
        }
        radioButtonPreference2.setChecked(z);
    }
}
