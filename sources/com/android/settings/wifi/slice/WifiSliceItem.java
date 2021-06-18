package com.android.settings.wifi.slice;

import android.content.Context;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.wifitrackerlib.WifiEntry;

public class WifiSliceItem {
    private static final int[] WIFI_CONNECTION_STRENGTH = {R.string.accessibility_no_wifi, R.string.accessibility_wifi_one_bar, R.string.accessibility_wifi_two_bars, R.string.accessibility_wifi_three_bars, R.string.accessibility_wifi_signal_full};
    private final int mConnectedState;
    private final Context mContext;
    private final boolean mHasInternetAccess;
    private final String mKey;
    private final int mLevel;
    private final int mSecurity;
    private final boolean mShouldEditBeforeConnect;
    private final boolean mShouldShowXLevelIcon;
    private final String mSummary;
    private final String mTitle;

    public WifiSliceItem(Context context, WifiEntry wifiEntry) {
        this.mContext = context;
        this.mKey = wifiEntry.getKey();
        this.mTitle = wifiEntry.getTitle();
        this.mSecurity = wifiEntry.getSecurity();
        this.mConnectedState = wifiEntry.getConnectedState();
        this.mLevel = wifiEntry.getLevel();
        this.mShouldShowXLevelIcon = wifiEntry.shouldShowXLevelIcon();
        this.mShouldEditBeforeConnect = wifiEntry.shouldEditBeforeConnect();
        this.mHasInternetAccess = wifiEntry.hasInternetAccess();
        this.mSummary = wifiEntry.getSummary(false);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WifiSliceItem)) {
            return false;
        }
        WifiSliceItem wifiSliceItem = (WifiSliceItem) obj;
        if (TextUtils.equals(getKey(), wifiSliceItem.getKey()) && getConnectedState() == wifiSliceItem.getConnectedState() && getLevel() == wifiSliceItem.getLevel() && shouldShowXLevelIcon() == wifiSliceItem.shouldShowXLevelIcon() && TextUtils.equals(getSummary(), wifiSliceItem.getSummary())) {
            return true;
        }
        return false;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public int getSecurity() {
        return this.mSecurity;
    }

    public int getConnectedState() {
        return this.mConnectedState;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public boolean shouldShowXLevelIcon() {
        return this.mShouldShowXLevelIcon;
    }

    public boolean hasInternetAccess() {
        return this.mHasInternetAccess;
    }

    public boolean shouldEditBeforeConnect() {
        return this.mShouldEditBeforeConnect;
    }

    public String getSummary() {
        return this.mSummary;
    }

    public CharSequence getContentDescription() {
        String str;
        CharSequence charSequence = this.mTitle;
        if (!TextUtils.isEmpty(this.mSummary)) {
            charSequence = TextUtils.concat(new CharSequence[]{charSequence, ",", this.mSummary});
        }
        int i = this.mLevel;
        if (i >= 0) {
            int[] iArr = WIFI_CONNECTION_STRENGTH;
            if (i < iArr.length) {
                charSequence = TextUtils.concat(new CharSequence[]{charSequence, ",", this.mContext.getString(iArr[i])});
            }
        }
        CharSequence[] charSequenceArr = new CharSequence[3];
        charSequenceArr[0] = charSequence;
        charSequenceArr[1] = ",";
        if (this.mSecurity == 0) {
            str = this.mContext.getString(R.string.accessibility_wifi_security_type_none);
        } else {
            str = this.mContext.getString(R.string.accessibility_wifi_security_type_secured);
        }
        charSequenceArr[2] = str;
        return TextUtils.concat(charSequenceArr);
    }
}
