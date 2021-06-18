package com.android.settings.users;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.security.OwnerInfoPreferenceController;

public class OwnerInfoSettings extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private LockPatternUtils mLockPatternUtils;
    private EditText mOwnerInfo;
    private int mUserId;
    private View mView;

    public int getMetricsCategory() {
        return 531;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUserId = UserHandle.myUserId();
        this.mLockPatternUtils = new LockPatternUtils(getActivity());
    }

    public Dialog onCreateDialog(Bundle bundle) {
        this.mView = LayoutInflater.from(getActivity()).inflate(R.layout.ownerinfo, (ViewGroup) null);
        initView();
        return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.owner_info_settings_title).setView(this.mView).setPositiveButton((int) R.string.save, (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) this).show();
    }

    private void initView() {
        String ownerInfo = this.mLockPatternUtils.getOwnerInfo(this.mUserId);
        this.mOwnerInfo = (EditText) this.mView.findViewById(R.id.owner_info_edit_text);
        if (!TextUtils.isEmpty(ownerInfo)) {
            this.mOwnerInfo.setText(ownerInfo);
            this.mOwnerInfo.setSelection(ownerInfo.length());
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            String obj = this.mOwnerInfo.getText().toString();
            this.mLockPatternUtils.setOwnerInfoEnabled(!TextUtils.isEmpty(obj), this.mUserId);
            this.mLockPatternUtils.setOwnerInfo(obj, this.mUserId);
            if (getTargetFragment() instanceof OwnerInfoPreferenceController.OwnerInfoCallback) {
                ((OwnerInfoPreferenceController.OwnerInfoCallback) getTargetFragment()).onOwnerInfoUpdated();
            }
        }
    }

    public static void show(Fragment fragment) {
        if (fragment.isAdded()) {
            OwnerInfoSettings ownerInfoSettings = new OwnerInfoSettings();
            ownerInfoSettings.setTargetFragment(fragment, 0);
            ownerInfoSettings.show(fragment.getFragmentManager(), "ownerInfo");
        }
    }
}
