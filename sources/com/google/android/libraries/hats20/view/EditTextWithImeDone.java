package com.google.android.libraries.hats20.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class EditTextWithImeDone extends EditText {
    public EditTextWithImeDone(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        int i = editorInfo.imeOptions & -256;
        editorInfo.imeOptions = i;
        int i2 = i | 6;
        editorInfo.imeOptions = i2;
        editorInfo.imeOptions = i2 & -1073741825;
        editorInfo.actionId = 6;
        return onCreateInputConnection;
    }
}
