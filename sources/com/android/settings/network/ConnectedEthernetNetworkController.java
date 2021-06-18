package com.android.settings.network;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.lifecycle.Lifecycle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.network.InternetUpdater;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;

public class ConnectedEthernetNetworkController extends AbstractPreferenceController implements InternetUpdater.InternetChangeListener {
    private int mInternetType;
    private InternetUpdater mInternetUpdater;
    private Preference mPreference;

    public String getPreferenceKey() {
        return "connected_ethernet_network";
    }

    public ConnectedEthernetNetworkController(Context context, Lifecycle lifecycle) {
        super(context);
        InternetUpdater internetUpdater = new InternetUpdater(context, lifecycle, this);
        this.mInternetUpdater = internetUpdater;
        this.mInternetType = internetUpdater.getInternetType();
    }

    public boolean isAvailable() {
        return this.mInternetType == 4;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference("connected_ethernet_network");
        Drawable drawable = this.mContext.getDrawable(R.drawable.ic_settings_ethernet);
        if (drawable != null) {
            drawable.setTintList(Utils.getColorAttr(this.mContext, 16843818));
            this.mPreference.setIcon(drawable);
        }
    }

    public void onInternetTypeChanged(int i) {
        this.mInternetType = i;
        Preference preference = this.mPreference;
        if (preference != null) {
            preference.setVisible(isAvailable());
        }
    }
}
