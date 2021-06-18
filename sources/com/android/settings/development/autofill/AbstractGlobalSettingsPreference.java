package com.android.settings.development.autofill;

import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.android.settings.Utils;
import com.android.settingslib.CustomEditTextPreferenceCompat;

abstract class AbstractGlobalSettingsPreference extends CustomEditTextPreferenceCompat {
    private final int mDefaultValue;
    private final String mKey;
    private final AutofillDeveloperSettingsObserver mObserver;

    protected AbstractGlobalSettingsPreference(Context context, AttributeSet attributeSet, String str, int i) {
        super(context, attributeSet);
        this.mKey = str;
        this.mDefaultValue = i;
        this.mObserver = new AutofillDeveloperSettingsObserver(context, new AbstractGlobalSettingsPreference$$ExternalSyntheticLambda0(this));
    }

    public void onAttached() {
        super.onAttached();
        this.mObserver.register();
        lambda$new$0();
    }

    public void onDetached() {
        this.mObserver.unregister();
        super.onDetached();
    }

    private String getCurrentValue() {
        return Integer.toString(Settings.Global.getInt(getContext().getContentResolver(), this.mKey, this.mDefaultValue));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateSummary */
    public void lambda$new$0() {
        setSummary((CharSequence) getCurrentValue());
    }

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = (EditText) view.findViewById(16908291);
        if (editText != null) {
            editText.setInputType(2);
            editText.setText(getCurrentValue());
            Utils.setEditTextCursorPosition(editText);
        }
    }

    /* access modifiers changed from: protected */
    public void onDialogClosed(boolean z) {
        if (z) {
            String text = getText();
            int i = this.mDefaultValue;
            try {
                i = Integer.parseInt(text);
            } catch (Exception unused) {
                Log.e("AbstractGlobalSettingsPreference", "Error converting '" + text + "' to integer. Using " + this.mDefaultValue + " instead");
            }
            Settings.Global.putInt(getContext().getContentResolver(), this.mKey, i);
        }
    }
}
