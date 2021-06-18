package com.android.settings.password;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Pair;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.settings.R;

abstract class SaveChosenLockWorkerBase extends Fragment {
    private boolean mBlocking;
    private boolean mFinished;
    private Listener mListener;
    protected boolean mRequestGatekeeperPassword;
    private Intent mResultData;
    protected LockscreenCredential mUnificationProfileCredential;
    protected int mUnificationProfileId = -10000;
    protected int mUserId;
    protected LockPatternUtils mUtils;
    protected boolean mWasSecureBefore;

    interface Listener {
        void onChosenLockSaveFinished(boolean z, Intent intent);
    }

    /* access modifiers changed from: protected */
    public abstract Pair<Boolean, Intent> saveAndVerifyInBackground();

    SaveChosenLockWorkerBase() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setListener(Listener listener) {
        if (this.mListener != listener) {
            this.mListener = listener;
            if (this.mFinished && listener != null) {
                listener.onChosenLockSaveFinished(this.mWasSecureBefore, this.mResultData);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void prepare(LockPatternUtils lockPatternUtils, boolean z, boolean z2, int i) {
        this.mUtils = lockPatternUtils;
        this.mUserId = i;
        this.mRequestGatekeeperPassword = z2;
        this.mWasSecureBefore = lockPatternUtils.isSecure(i);
        Context context = getContext();
        if (context == null || UserManager.get(context).getUserInfo(this.mUserId).isPrimary()) {
            this.mUtils.setCredentialRequiredToDecrypt(z);
        }
        this.mFinished = false;
        this.mResultData = null;
    }

    /* access modifiers changed from: protected */
    public void start() {
        if (this.mBlocking) {
            finish((Intent) saveAndVerifyInBackground().second);
        } else {
            new Task().execute(new Void[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void finish(Intent intent) {
        this.mFinished = true;
        this.mResultData = intent;
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onChosenLockSaveFinished(this.mWasSecureBefore, intent);
        }
        LockscreenCredential lockscreenCredential = this.mUnificationProfileCredential;
        if (lockscreenCredential != null) {
            lockscreenCredential.zeroize();
        }
    }

    public void setBlocking(boolean z) {
        this.mBlocking = z;
    }

    public void setProfileToUnify(int i, LockscreenCredential lockscreenCredential) {
        this.mUnificationProfileId = i;
        this.mUnificationProfileCredential = lockscreenCredential.duplicate();
    }

    /* access modifiers changed from: protected */
    public void unifyProfileCredentialIfRequested() {
        int i = this.mUnificationProfileId;
        if (i != -10000) {
            this.mUtils.setSeparateProfileChallengeEnabled(i, false, this.mUnificationProfileCredential);
        }
    }

    private class Task extends AsyncTask<Void, Void, Pair<Boolean, Intent>> {
        private Task() {
        }

        /* access modifiers changed from: protected */
        public Pair<Boolean, Intent> doInBackground(Void... voidArr) {
            return SaveChosenLockWorkerBase.this.saveAndVerifyInBackground();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Pair<Boolean, Intent> pair) {
            if (!((Boolean) pair.first).booleanValue()) {
                Toast.makeText(SaveChosenLockWorkerBase.this.getContext(), R.string.lockpassword_credential_changed, 1).show();
            }
            SaveChosenLockWorkerBase.this.finish((Intent) pair.second);
        }
    }
}
