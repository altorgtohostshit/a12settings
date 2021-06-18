package com.android.settings.inputmethod;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.ListFormatter;
import android.text.BidiFormatter;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class VirtualKeyboardPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final DevicePolicyManager mDpm;
    private final InputMethodManager mImm;
    private final PackageManager mPm = this.mContext.getPackageManager();

    public String getPreferenceKey() {
        return "virtual_keyboard_pref";
    }

    public VirtualKeyboardPreferenceController(Context context) {
        super(context);
        this.mDpm = (DevicePolicyManager) context.getSystemService("device_policy");
        this.mImm = (InputMethodManager) this.mContext.getSystemService("input_method");
    }

    public boolean isAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_virtual_keyboard_pref);
    }

    public void updateState(Preference preference) {
        List<InputMethodInfo> enabledInputMethodList = this.mImm.getEnabledInputMethodList();
        if (enabledInputMethodList == null) {
            preference.setSummary((int) R.string.summary_empty);
            return;
        }
        List permittedInputMethodsForCurrentUser = this.mDpm.getPermittedInputMethodsForCurrentUser();
        ArrayList<String> arrayList = new ArrayList<>();
        for (InputMethodInfo next : enabledInputMethodList) {
            if (permittedInputMethodsForCurrentUser == null || permittedInputMethodsForCurrentUser.contains(next.getPackageName())) {
                arrayList.add(next.loadLabel(this.mPm).toString());
            }
        }
        if (arrayList.isEmpty()) {
            preference.setSummary((int) R.string.summary_empty);
            return;
        }
        BidiFormatter instance = BidiFormatter.getInstance();
        ArrayList arrayList2 = new ArrayList();
        for (String unicodeWrap : arrayList) {
            arrayList2.add(instance.unicodeWrap(unicodeWrap));
        }
        preference.setSummary((CharSequence) ListFormatter.getInstance().format(arrayList2));
    }
}
