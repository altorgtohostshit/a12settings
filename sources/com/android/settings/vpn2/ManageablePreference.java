package com.android.settings.vpn2;

import android.content.Context;
import android.content.res.Resources;
import android.os.UserHandle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import com.android.settings.R;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.Utils;

public abstract class ManageablePreference extends GearPreference {
    public static int STATE_NONE = -1;
    boolean mIsAlwaysOn = false;
    boolean mIsInsecureVpn = false;
    int mState = STATE_NONE;
    int mUserId;

    public ManageablePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setPersistent(false);
        setOrder(0);
        setUserId(UserHandle.myUserId());
    }

    public int getUserId() {
        return this.mUserId;
    }

    public void setUserId(int i) {
        this.mUserId = i;
        checkRestrictionAndSetDisabled("no_config_vpn", i);
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int i) {
        if (this.mState != i) {
            this.mState = i;
            updateSummary();
            notifyHierarchyChanged();
        }
    }

    public void setAlwaysOn(boolean z) {
        if (this.mIsAlwaysOn != z) {
            this.mIsAlwaysOn = z;
            updateSummary();
        }
    }

    public void setInsecureVpn(boolean z) {
        if (this.mIsInsecureVpn != z) {
            this.mIsInsecureVpn = z;
            updateSummary();
        }
    }

    /* access modifiers changed from: protected */
    public void updateSummary() {
        Resources resources = getContext().getResources();
        String[] stringArray = resources.getStringArray(R.array.vpn_states);
        int i = this.mState;
        String str = i == STATE_NONE ? "" : stringArray[i];
        if (this.mIsInsecureVpn) {
            String string = resources.getString(R.string.vpn_insecure_summary);
            if (!TextUtils.isEmpty(str)) {
                string = str + " / " + string;
            }
            SpannableString spannableString = new SpannableString(string);
            spannableString.setSpan(new ForegroundColorSpan(Utils.getColorErrorDefaultColor(getContext())), 0, string.length(), 34);
            setSummary((CharSequence) spannableString);
        } else if (this.mIsAlwaysOn) {
            String string2 = resources.getString(R.string.vpn_always_on_summary_active);
            if (!TextUtils.isEmpty(str)) {
                string2 = str + " / " + string2;
            }
            setSummary((CharSequence) string2);
        } else {
            setSummary((CharSequence) str);
        }
    }
}
