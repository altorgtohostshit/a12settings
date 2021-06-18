package com.android.settings;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.CustomDialogPreferenceCompat;

public class BugreportPreference extends CustomDialogPreferenceCompat {
    /* access modifiers changed from: private */
    public TextView mFullSummary;
    /* access modifiers changed from: private */
    public CheckedTextView mFullTitle;
    /* access modifiers changed from: private */
    public TextView mInteractiveSummary;
    /* access modifiers changed from: private */
    public CheckedTextView mInteractiveTitle;

    public BugreportPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        super.onPrepareDialogBuilder(builder, onClickListener);
        View inflate = View.inflate(getContext(), R.layout.bugreport_options_dialog, (ViewGroup) null);
        this.mInteractiveTitle = (CheckedTextView) inflate.findViewById(R.id.bugreport_option_interactive_title);
        this.mInteractiveSummary = (TextView) inflate.findViewById(R.id.bugreport_option_interactive_summary);
        this.mFullTitle = (CheckedTextView) inflate.findViewById(R.id.bugreport_option_full_title);
        this.mFullSummary = (TextView) inflate.findViewById(R.id.bugreport_option_full_summary);
        C05241 r1 = new View.OnClickListener() {
            public void onClick(View view) {
                if (view == BugreportPreference.this.mFullTitle || view == BugreportPreference.this.mFullSummary) {
                    BugreportPreference.this.mInteractiveTitle.setChecked(false);
                    BugreportPreference.this.mFullTitle.setChecked(true);
                }
                if (view == BugreportPreference.this.mInteractiveTitle || view == BugreportPreference.this.mInteractiveSummary) {
                    BugreportPreference.this.mInteractiveTitle.setChecked(true);
                    BugreportPreference.this.mFullTitle.setChecked(false);
                }
            }
        };
        this.mInteractiveTitle.setOnClickListener(r1);
        this.mFullTitle.setOnClickListener(r1);
        this.mInteractiveSummary.setOnClickListener(r1);
        this.mFullSummary.setOnClickListener(r1);
        builder.setPositiveButton(17041256, onClickListener);
        builder.setView(inflate);
    }

    /* access modifiers changed from: protected */
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            Context context = getContext();
            if (this.mFullTitle.isChecked()) {
                Log.v("BugreportPreference", "Taking full bugreport right away");
                FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(context, 295, (Pair<Integer, Object>[]) new Pair[0]);
                try {
                    ActivityManager.getService().requestFullBugReport();
                } catch (RemoteException e) {
                    Log.e("BugreportPreference", "error taking bugreport (bugreportType=Full)", e);
                }
            } else {
                Log.v("BugreportPreference", "Taking interactive bugreport right away");
                FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(context, 294, (Pair<Integer, Object>[]) new Pair[0]);
                try {
                    ActivityManager.getService().requestInteractiveBugReport();
                } catch (RemoteException e2) {
                    Log.e("BugreportPreference", "error taking bugreport (bugreportType=Interactive)", e2);
                }
            }
        }
    }
}
