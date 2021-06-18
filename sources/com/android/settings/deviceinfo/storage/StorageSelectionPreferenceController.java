package com.android.settings.deviceinfo.storage;

import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.SettingsSpinnerPreference;
import com.android.settingslib.widget.settingsspinner.SettingsSpinnerAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageSelectionPreferenceController extends BasePreferenceController implements AdapterView.OnItemSelectedListener {
    private OnItemSelectedListener mOnItemSelectedListener;
    SettingsSpinnerPreference mSpinnerPreference;
    StorageAdapter mStorageAdapter;
    private final List<StorageEntry> mStorageEntries = new ArrayList();

    public interface OnItemSelectedListener {
        void onItemSelected(StorageEntry storageEntry);
    }

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

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public StorageSelectionPreferenceController(Context context, String str) {
        super(context, str);
        this.mStorageAdapter = new StorageAdapter(context);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    public void setStorageEntries(List<StorageEntry> list) {
        this.mStorageAdapter.clear();
        this.mStorageEntries.clear();
        if (list != null && !list.isEmpty()) {
            Collections.sort(this.mStorageEntries);
            this.mStorageEntries.addAll(list);
            this.mStorageAdapter.addAll(list);
            SettingsSpinnerPreference settingsSpinnerPreference = this.mSpinnerPreference;
            if (settingsSpinnerPreference != null) {
                boolean z = true;
                if (this.mStorageAdapter.getCount() <= 1) {
                    z = false;
                }
                settingsSpinnerPreference.setClickable(z);
            }
        }
    }

    public void setSelectedStorageEntry(StorageEntry storageEntry) {
        if (this.mSpinnerPreference != null && this.mStorageEntries.contains(storageEntry)) {
            this.mSpinnerPreference.setSelection(this.mStorageAdapter.getPosition(storageEntry));
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        SettingsSpinnerPreference settingsSpinnerPreference = (SettingsSpinnerPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mSpinnerPreference = settingsSpinnerPreference;
        settingsSpinnerPreference.setAdapter(this.mStorageAdapter);
        this.mSpinnerPreference.setOnItemSelectedListener(this);
        SettingsSpinnerPreference settingsSpinnerPreference2 = this.mSpinnerPreference;
        boolean z = true;
        if (this.mStorageAdapter.getCount() <= 1) {
            z = false;
        }
        settingsSpinnerPreference2.setClickable(z);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        OnItemSelectedListener onItemSelectedListener = this.mOnItemSelectedListener;
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected((StorageEntry) this.mSpinnerPreference.getSelectedItem());
        }
    }

    class StorageAdapter extends SettingsSpinnerAdapter<StorageEntry> {
        StorageAdapter(Context context) {
            super(context);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getDefaultView(i, view, viewGroup);
            }
            try {
                TextView textView = (TextView) view;
                textView.setText(((StorageEntry) getItem(i)).getDescription());
                return textView;
            } catch (ClassCastException e) {
                throw new IllegalStateException("Default view should be a TextView, ", e);
            }
        }

        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getDefaultDropDownView(i, view, viewGroup);
            }
            try {
                TextView textView = (TextView) view;
                textView.setText(((StorageEntry) getItem(i)).getDescription());
                return textView;
            } catch (ClassCastException e) {
                throw new IllegalStateException("Default drop down view should be a TextView, ", e);
            }
        }
    }
}
