package com.android.settings.network.telephony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.ims.ImsManager;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class ContactDiscoveryDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private CharSequence mCarrierName;
    private ImsManager mImsManager;
    private int mSubId;

    public int getMetricsCategory() {
        return 0;
    }

    public static ContactDiscoveryDialogFragment newInstance(int i, CharSequence charSequence) {
        ContactDiscoveryDialogFragment contactDiscoveryDialogFragment = new ContactDiscoveryDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("sub_id_key", i);
        bundle.putCharSequence("carrier_name_key", charSequence);
        contactDiscoveryDialogFragment.setArguments(bundle);
        return contactDiscoveryDialogFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        this.mSubId = arguments.getInt("sub_id_key");
        this.mCarrierName = arguments.getCharSequence("carrier_name_key");
        this.mImsManager = getImsManager(context);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str;
        String str2;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (!TextUtils.isEmpty(this.mCarrierName)) {
            str2 = getContext().getString(R.string.contact_discovery_opt_in_dialog_title, new Object[]{this.mCarrierName});
            str = getContext().getString(R.string.contact_discovery_opt_in_dialog_message, new Object[]{this.mCarrierName});
        } else {
            str2 = getContext().getString(R.string.contact_discovery_opt_in_dialog_title_no_carrier_defined);
            str = getContext().getString(R.string.contact_discovery_opt_in_dialog_message_no_carrier_defined);
        }
        builder.setMessage(str).setTitle(str2).setIconAttribute(16843605).setPositiveButton(R.string.confirmation_turn_on, this).setNegativeButton(17039360, this);
        return builder.create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            MobileNetworkUtils.setContactDiscoveryEnabled(this.mImsManager, this.mSubId, true);
        }
    }

    public ImsManager getImsManager(Context context) {
        return (ImsManager) context.getSystemService(ImsManager.class);
    }

    public static String getFragmentTag(int i) {
        return "discovery_dialog:" + i;
    }
}
