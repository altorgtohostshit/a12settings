package com.android.settings.sim.smartForwarding;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settingslib.core.instrumentation.Instrumentable;

public class MDNHandlerFragment extends Fragment implements Instrumentable {
    public int getMetricsCategory() {
        return 1571;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.xml.smart_forwarding_mdn_handler, viewGroup, false);
        getActivity().getActionBar().setTitle(getResources().getString(R.string.smart_forwarding_input_mdn_title));
        ((Button) inflate.findViewById(R.id.process)).setOnClickListener(new MDNHandlerFragment$$ExternalSyntheticLambda2(this));
        ((Button) inflate.findViewById(R.id.cancel)).setOnClickListener(new MDNHandlerFragment$$ExternalSyntheticLambda1(this));
        return inflate;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$0(View view) {
        pressButtonOnClick();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$1(View view) {
        switchToMainFragment(true);
    }

    private void pressButtonOnClick() {
        String str;
        MDNHandlerHeaderFragment mDNHandlerHeaderFragment = (MDNHandlerHeaderFragment) getChildFragmentManager().findFragmentById(R.id.fragment_settings);
        String str2 = "";
        if (mDNHandlerHeaderFragment != null) {
            String charSequence = mDNHandlerHeaderFragment.findPreference("slot0_phone_number").getSummary().toString();
            str2 = mDNHandlerHeaderFragment.findPreference("slot1_phone_number").getSummary().toString();
            str = charSequence;
        } else {
            str = str2;
        }
        String[] strArr = {str2, str};
        if (TextUtils.isEmpty(strArr[0]) || TextUtils.isEmpty(strArr[1])) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.smart_forwarding_failed_title).setMessage(R.string.smart_forwarding_missing_mdn_text).setPositiveButton(R.string.smart_forwarding_missing_alert_dialog_text, MDNHandlerFragment$$ExternalSyntheticLambda0.INSTANCE).create().show();
            return;
        }
        switchToMainFragment(false);
        ((SmartForwardingActivity) getActivity()).enableSmartForwarding(strArr);
    }

    private void switchToMainFragment(boolean z) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SmartForwardingFragment(z)).commit();
    }
}
