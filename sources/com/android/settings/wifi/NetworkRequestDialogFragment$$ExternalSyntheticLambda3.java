package com.android.settings.wifi;

import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.app.AlertDialog;

public final /* synthetic */ class NetworkRequestDialogFragment$$ExternalSyntheticLambda3 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ NetworkRequestDialogFragment f$0;
    public final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ NetworkRequestDialogFragment$$ExternalSyntheticLambda3(NetworkRequestDialogFragment networkRequestDialogFragment, AlertDialog alertDialog) {
        this.f$0 = networkRequestDialogFragment;
        this.f$1 = alertDialog;
    }

    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        this.f$0.lambda$onCreateDialog$1(this.f$1, adapterView, view, i, j);
    }
}
