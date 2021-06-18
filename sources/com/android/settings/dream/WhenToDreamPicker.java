package com.android.settings.dream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.settings.R;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.dream.DreamBackend;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.List;

public class WhenToDreamPicker extends RadioButtonPickerFragment {
    private DreamBackend mBackend;

    public int getMetricsCategory() {
        return 47;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.when_to_dream_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mBackend = DreamBackend.getInstance(context);
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        String[] entries = entries();
        String[] keys = keys();
        ArrayList arrayList = new ArrayList();
        if (entries == null || entries.length <= 0) {
            return null;
        }
        if (keys == null || keys.length != entries.length) {
            throw new IllegalArgumentException("Entries and values must be of the same length.");
        }
        for (int i = 0; i < entries.length; i++) {
            arrayList.add(new WhenToDreamCandidateInfo(entries[i], keys[i]));
        }
        return arrayList;
    }

    private String[] entries() {
        return getResources().getStringArray(R.array.when_to_start_screensaver_entries);
    }

    private String[] keys() {
        return getResources().getStringArray(R.array.when_to_start_screensaver_values);
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return DreamSettings.getKeyFromSetting(this.mBackend.getWhenToDreamSetting());
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        this.mBackend.setWhenToDream(DreamSettings.getSettingFromPrefKey(str));
        return true;
    }

    /* access modifiers changed from: protected */
    public void onSelectionPerformed(boolean z) {
        super.onSelectionPerformed(z);
        getActivity().finish();
    }

    private final class WhenToDreamCandidateInfo extends CandidateInfo {
        private final String key;
        private final String name;

        public Drawable loadIcon() {
            return null;
        }

        WhenToDreamCandidateInfo(String str, String str2) {
            super(true);
            this.name = str;
            this.key = str2;
        }

        public CharSequence loadLabel() {
            return this.name;
        }

        public String getKey() {
            return this.key;
        }
    }
}
