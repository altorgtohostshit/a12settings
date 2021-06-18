package com.android.settings.wifi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import java.util.List;

public abstract class NetworkRequestDialogBaseFragment extends InstrumentedDialogFragment {
    static final String EXTRA_APP_NAME = "com.android.settings.wifi.extra.APP_NAME";
    NetworkRequestDialogActivity mActivity = null;
    private String mAppName = "";

    public int getMetricsCategory() {
        return 1373;
    }

    /* access modifiers changed from: protected */
    public void onMatch(List<ScanResult> list) {
    }

    /* access modifiers changed from: protected */
    public void onUserSelectionCallbackRegistration(WifiManager.NetworkRequestUserSelectionCallback networkRequestUserSelectionCallback) {
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetworkRequestDialogActivity) {
            this.mActivity = (NetworkRequestDialogActivity) context;
        }
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            this.mAppName = intent.getStringExtra(EXTRA_APP_NAME);
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        NetworkRequestDialogActivity networkRequestDialogActivity = this.mActivity;
        if (networkRequestDialogActivity != null) {
            networkRequestDialogActivity.onCancel();
        }
    }

    /* access modifiers changed from: protected */
    public String getTitle() {
        return getString(R.string.network_connection_request_dialog_title);
    }

    /* access modifiers changed from: protected */
    public String getSummary() {
        return getString(R.string.network_connection_request_dialog_summary, this.mAppName);
    }
}
