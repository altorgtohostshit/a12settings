package com.android.settings.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.CustomEditTextPreferenceCompat;

public class ValidatedEditTextPreference extends CustomEditTextPreferenceCompat {
    private boolean mIsPassword;
    private boolean mIsSummaryPassword;
    private final EditTextWatcher mTextWatcher = new EditTextWatcher();
    /* access modifiers changed from: private */
    public Validator mValidator;

    public interface Validator {
        boolean isTextValid(String str);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ValidatedEditTextPreference(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = (EditText) view.findViewById(16908291);
        if (editText != null && !TextUtils.isEmpty(editText.getText())) {
            editText.setSelection(editText.getText().length());
        }
        if (this.mValidator != null && editText != null) {
            editText.removeTextChangedListener(this.mTextWatcher);
            if (this.mIsPassword) {
                editText.setInputType(145);
                editText.setMaxLines(1);
            }
            editText.addTextChangedListener(this.mTextWatcher);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.findViewById(16908304);
        if (textView != null) {
            if (this.mIsSummaryPassword) {
                textView.setInputType(129);
            } else {
                textView.setInputType(524289);
            }
        }
    }

    public void setIsPassword(boolean z) {
        this.mIsPassword = z;
    }

    public void setIsSummaryPassword(boolean z) {
        this.mIsSummaryPassword = z;
    }

    public boolean isPassword() {
        return this.mIsPassword;
    }

    public void setValidator(Validator validator) {
        this.mValidator = validator;
    }

    private class EditTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private EditTextWatcher() {
        }

        public void afterTextChanged(Editable editable) {
            EditText editText = ValidatedEditTextPreference.this.getEditText();
            if (ValidatedEditTextPreference.this.mValidator != null && editText != null) {
                ((AlertDialog) ValidatedEditTextPreference.this.getDialog()).getButton(-1).setEnabled(ValidatedEditTextPreference.this.mValidator.isTextValid(editText.getText().toString()));
            }
        }
    }
}
