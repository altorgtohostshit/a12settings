package com.android.settings.display;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.List;

public class VrDisplayPreferencePicker extends RadioButtonPickerFragment {
    public int getMetricsCategory() {
        return 921;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.vr_display_settings;
    }

    /* access modifiers changed from: protected */
    public List<VrCandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        Context context = getContext();
        arrayList.add(new VrCandidateInfo(context, 0, R.string.display_vr_pref_low_persistence));
        arrayList.add(new VrCandidateInfo(context, 1, R.string.display_vr_pref_off));
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        int intForUser = Settings.Secure.getIntForUser(getContext().getContentResolver(), "vr_display_mode", 0, this.mUserId);
        return "vr_display_pref_" + intForUser;
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        str.hashCode();
        if (str.equals("vr_display_pref_0")) {
            return Settings.Secure.putIntForUser(getContext().getContentResolver(), "vr_display_mode", 0, this.mUserId);
        }
        if (!str.equals("vr_display_pref_1")) {
            return false;
        }
        return Settings.Secure.putIntForUser(getContext().getContentResolver(), "vr_display_mode", 1, this.mUserId);
    }

    static class VrCandidateInfo extends CandidateInfo {
        public final String label;
        public final int value;

        public Drawable loadIcon() {
            return null;
        }

        public VrCandidateInfo(Context context, int i, int i2) {
            super(true);
            this.value = i;
            this.label = context.getString(i2);
        }

        public CharSequence loadLabel() {
            return this.label;
        }

        public String getKey() {
            return "vr_display_pref_" + this.value;
        }
    }
}
