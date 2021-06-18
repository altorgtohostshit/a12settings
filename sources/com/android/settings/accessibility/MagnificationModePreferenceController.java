package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.DialogCreatable;
import com.android.settings.R;
import com.android.settings.accessibility.ItemInfoArrayAdapter;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MagnificationModePreferenceController extends BasePreferenceController implements DialogCreatable, LifecycleObserver, OnCreate, OnResume, OnSaveInstanceState {
    private static final char COMPONENT_NAME_SEPARATOR = ':';
    private static final int DIALOG_ID_BASE = 10;
    static final int DIALOG_MAGNIFICATION_MODE = 11;
    static final int DIALOG_MAGNIFICATION_SWITCH_SHORTCUT = 12;
    static final String EXTRA_MODE = "mode";
    static final String PREF_KEY = "screen_magnification_mode";
    private static final String TAG = "MagnificationModePreferenceController";
    private DialogHelper mDialogHelper;
    ListView mMagnificationModesListView;
    private int mMode = 0;
    private final List<MagnificationModeInfo> mModeInfos = new ArrayList();
    private Preference mModePreference;

    interface DialogHelper extends DialogCreatable {
        void setDialogDelegate(DialogCreatable dialogCreatable);

        void showDialog(int i);
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public int getDialogMetricsCategory(int i) {
        if (i != 11) {
            return i != 12 ? 0 : 1849;
        }
        return 1816;
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

    public MagnificationModePreferenceController(Context context, String str) {
        super(context, str);
        initModeInfos();
    }

    private void initModeInfos() {
        this.mModeInfos.add(new MagnificationModeInfo(this.mContext.getText(R.string.accessibility_magnification_mode_dialog_option_full_screen), (CharSequence) null, R.drawable.ic_illustration_fullscreen, 1));
        this.mModeInfos.add(new MagnificationModeInfo(this.mContext.getText(R.string.accessibility_magnification_mode_dialog_option_window), (CharSequence) null, R.drawable.ic_illustration_window, 2));
        this.mModeInfos.add(new MagnificationModeInfo(this.mContext.getText(R.string.accessibility_magnification_mode_dialog_option_switch), this.mContext.getText(R.string.accessibility_magnification_area_settings_mode_switch_summary), R.drawable.ic_illustration_switch, 3));
    }

    public CharSequence getSummary() {
        return MagnificationCapabilities.getSummary(this.mContext, MagnificationCapabilities.getCapabilities(this.mContext));
    }

    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            this.mMode = bundle.getInt(EXTRA_MODE, 0);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mModePreference = findPreference;
        findPreference.setOnPreferenceClickListener(new MagnificationModePreferenceController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$displayPreference$0(Preference preference) {
        this.mMode = MagnificationCapabilities.getCapabilities(this.mContext);
        this.mDialogHelper.showDialog(11);
        return true;
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(EXTRA_MODE, this.mMode);
    }

    public void setDialogHelper(DialogHelper dialogHelper) {
        this.mDialogHelper = dialogHelper;
        dialogHelper.setDialogDelegate(this);
    }

    public Dialog onCreateDialog(int i) {
        if (i == 11) {
            return createMagnificationModeDialog();
        }
        if (i != 12) {
            return null;
        }
        return createMagnificationShortCutConfirmDialog();
    }

    private Dialog createMagnificationModeDialog() {
        this.mMagnificationModesListView = AccessibilityEditDialogUtils.createSingleChoiceListView(this.mContext, this.mModeInfos, new MagnificationModePreferenceController$$ExternalSyntheticLambda1(this));
        this.mMagnificationModesListView.addHeaderView(LayoutInflater.from(this.mContext).inflate(R.layout.accessibility_magnification_mode_header, this.mMagnificationModesListView, false), (Object) null, false);
        this.mMagnificationModesListView.setItemChecked(computeSelectionIndex(), true);
        return AccessibilityEditDialogUtils.createCustomDialog(this.mContext, this.mContext.getString(R.string.accessibility_magnification_mode_dialog_title), this.mMagnificationModesListView, new MagnificationModePreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public void onMagnificationModeDialogPositiveButtonClicked(DialogInterface dialogInterface, int i) {
        int checkedItemPosition = this.mMagnificationModesListView.getCheckedItemPosition();
        if (checkedItemPosition != -1) {
            setMode(((MagnificationModeInfo) this.mMagnificationModesListView.getItemAtPosition(checkedItemPosition)).mMagnificationMode);
        } else {
            Log.w(TAG, "invalid index");
        }
    }

    private void setMode(int i) {
        this.mMode = i;
        MagnificationCapabilities.setCapabilities(this.mContext, i);
        this.mModePreference.setSummary((CharSequence) MagnificationCapabilities.getSummary(this.mContext, this.mMode));
    }

    /* access modifiers changed from: private */
    public void onMagnificationModeSelected(AdapterView<?> adapterView, View view, int i, long j) {
        int i2 = ((MagnificationModeInfo) this.mMagnificationModesListView.getItemAtPosition(i)).mMagnificationMode;
        if (i2 != this.mMode) {
            this.mMode = i2;
            if (isTripleTapEnabled(this.mContext) && this.mMode != 1) {
                this.mDialogHelper.showDialog(12);
            }
        }
    }

    private int computeSelectionIndex() {
        int size = this.mModeInfos.size();
        for (int i = 0; i < size; i++) {
            if (this.mModeInfos.get(i).mMagnificationMode == this.mMode) {
                return i + this.mMagnificationModesListView.getHeaderViewsCount();
            }
        }
        Log.w(TAG, "computeSelectionIndex failed");
        return 0;
    }

    static boolean isTripleTapEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "accessibility_display_magnification_enabled", 0) == 1;
    }

    private Dialog createMagnificationShortCutConfirmDialog() {
        return AccessibilityEditDialogUtils.createMagnificationSwitchShortcutDialog(this.mContext, new MagnificationModePreferenceController$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    public void onSwitchShortcutDialogButtonClicked(int i) {
        optOutMagnificationFromTripleTap();
        optInMagnificationToAccessibilityButton();
    }

    private void optOutMagnificationFromTripleTap() {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "accessibility_display_magnification_enabled", 0);
    }

    private void optInMagnificationToAccessibilityButton() {
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "accessibility_button_targets");
        if (string == null || !string.contains("com.android.server.accessibility.MagnificationController")) {
            StringJoiner stringJoiner = new StringJoiner(String.valueOf(COMPONENT_NAME_SEPARATOR));
            if (!TextUtils.isEmpty(string)) {
                stringJoiner.add(string);
            }
            stringJoiner.add("com.android.server.accessibility.MagnificationController");
            Settings.Secure.putString(this.mContext.getContentResolver(), "accessibility_button_targets", stringJoiner.toString());
        }
    }

    public void onResume() {
        updateState(this.mModePreference);
    }

    static class MagnificationModeInfo extends ItemInfoArrayAdapter.ItemInfo {
        public final int mMagnificationMode;

        MagnificationModeInfo(CharSequence charSequence, CharSequence charSequence2, int i, int i2) {
            super(charSequence, charSequence2, i);
            this.mMagnificationMode = i2;
        }
    }
}
