package com.google.android.settings.biometrics.face;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class FaceEnrollDialogFactory {

    public interface OnBackKeyListener {
        void onBackKeyUp(DialogInterface dialogInterface, KeyEvent keyEvent);
    }

    public static DialogBuilder newBuilder(Context context) {
        return new DialogBuilder(context);
    }

    public static class DialogBuilder {
        private AlertDialog.Builder mBuilder;
        /* access modifiers changed from: private */
        public OnBackKeyListener mOnBackKeyListener;

        private DialogBuilder(Context context) {
            this.mBuilder = new AlertDialog.Builder(context);
        }

        public DialogBuilder setTitle(int i) {
            this.mBuilder.setTitle(i);
            return this;
        }

        public DialogBuilder setMessage(int i) {
            this.mBuilder.setMessage(i);
            return this;
        }

        public DialogBuilder setMessage(CharSequence charSequence) {
            this.mBuilder.setMessage(charSequence);
            return this;
        }

        public DialogBuilder setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
            this.mBuilder.setPositiveButton(i, onClickListener);
            return this;
        }

        public DialogBuilder setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
            this.mBuilder.setNegativeButton(i, onClickListener);
            return this;
        }

        public DialogBuilder setOnBackKeyListener(OnBackKeyListener onBackKeyListener) {
            this.mOnBackKeyListener = onBackKeyListener;
            return this;
        }

        public Dialog build() {
            AlertDialog create = this.mBuilder.setCancelable(false).create();
            create.setCanceledOnTouchOutside(false);
            if (this.mOnBackKeyListener != null) {
                create.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    private boolean mCanceled = false;

                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (i != 4) {
                            return false;
                        }
                        if (keyEvent.getAction() == 1 && !this.mCanceled) {
                            this.mCanceled = true;
                            DialogBuilder.this.mOnBackKeyListener.onBackKeyUp(dialogInterface, keyEvent);
                        }
                        return true;
                    }
                });
            }
            return create;
        }
    }
}
