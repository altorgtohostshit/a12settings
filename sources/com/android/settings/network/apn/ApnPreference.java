package com.android.settings.network.apn;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;

public class ApnPreference extends Preference implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static CompoundButton sCurrentChecked;
    private static String sSelectedKey;
    private boolean mHideDetails;
    private boolean mProtectFromCheckedChange;
    private boolean mSelectable;
    private int mSubId;

    public ApnPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSubId = -1;
        this.mProtectFromCheckedChange = false;
        this.mSelectable = true;
        this.mHideDetails = false;
    }

    public ApnPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.apnPreferenceStyle);
    }

    public ApnPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((RelativeLayout) preferenceViewHolder.findViewById(R.id.text_layout)).setOnClickListener(this);
        View findViewById = preferenceViewHolder.findViewById(R.id.apn_radiobutton);
        if (findViewById != null && (findViewById instanceof RadioButton)) {
            RadioButton radioButton = (RadioButton) findViewById;
            if (this.mSelectable) {
                radioButton.setOnCheckedChangeListener(this);
                boolean equals = getKey().equals(sSelectedKey);
                if (equals) {
                    sCurrentChecked = radioButton;
                    sSelectedKey = getKey();
                }
                this.mProtectFromCheckedChange = true;
                radioButton.setChecked(equals);
                this.mProtectFromCheckedChange = false;
                radioButton.setVisibility(0);
                return;
            }
            radioButton.setVisibility(8);
        }
    }

    public void setChecked() {
        sSelectedKey = getKey();
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        Log.i("ApnPreference", "ID: " + getKey() + " :" + z);
        if (!this.mProtectFromCheckedChange) {
            if (z) {
                CompoundButton compoundButton2 = sCurrentChecked;
                if (compoundButton2 != null) {
                    compoundButton2.setChecked(false);
                }
                sCurrentChecked = compoundButton;
                String key = getKey();
                sSelectedKey = key;
                callChangeListener(key);
                return;
            }
            sCurrentChecked = null;
            sSelectedKey = null;
        }
    }

    public void onClick(View view) {
        super.onClick();
        Context context = getContext();
        int parseInt = Integer.parseInt(getKey());
        if (context == null) {
            Log.w("ApnPreference", "No context available for pos=" + parseInt);
        } else if (this.mHideDetails) {
            Toast.makeText(context, context.getString(R.string.cannot_change_apn_toast), 1).show();
        } else {
            Intent intent = new Intent("android.intent.action.EDIT", ContentUris.withAppendedId(Telephony.Carriers.CONTENT_URI, (long) parseInt));
            intent.putExtra("sub_id", this.mSubId);
            intent.addFlags(1);
            context.startActivity(intent);
        }
    }

    public void setSelectable(boolean z) {
        this.mSelectable = z;
    }

    public void setSubId(int i) {
        this.mSubId = i;
    }

    public void setHideDetails() {
        this.mHideDetails = true;
    }
}
