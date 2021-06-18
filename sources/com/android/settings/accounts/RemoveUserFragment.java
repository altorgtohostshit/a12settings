package com.android.settings.accounts;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.UserManager;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.users.UserDialogs;

public class RemoveUserFragment extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 534;
    }

    static RemoveUserFragment newInstance(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("userId", i);
        RemoveUserFragment removeUserFragment = new RemoveUserFragment();
        removeUserFragment.setArguments(bundle);
        return removeUserFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        final int i = getArguments().getInt("userId");
        return UserDialogs.createRemoveDialog(getActivity(), i, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ((UserManager) RemoveUserFragment.this.getActivity().getSystemService("user")).removeUser(i);
            }
        });
    }
}
