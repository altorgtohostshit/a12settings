package com.android.settings.inputmethod;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;

public class InputMethodAndSubtypeEnabler extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "InputMethodAndSubtypeEnabler";
    }

    public int getMetricsCategory() {
        return 60;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.input_methods_subtype;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((InputMethodAndSubtypePreferenceController) use(InputMethodAndSubtypePreferenceController.class)).initialize(this, getStringExtraFromIntentOrArguments("input_method_id"));
    }

    private String getStringExtraFromIntentOrArguments(String str) {
        String stringExtra = getActivity().getIntent().getStringExtra(str);
        if (stringExtra != null) {
            return stringExtra;
        }
        Bundle arguments = getArguments();
        if (arguments == null) {
            return null;
        }
        return arguments.getString(str);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        String stringExtraFromIntentOrArguments = getStringExtraFromIntentOrArguments("android.intent.extra.TITLE");
        if (!TextUtils.isEmpty(stringExtraFromIntentOrArguments)) {
            getActivity().setTitle(stringExtraFromIntentOrArguments);
        }
    }
}
