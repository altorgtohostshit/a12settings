package com.android.settings.wifi;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.wifi.LongPressWifiEntryPreference;
import com.android.wifitrackerlib.WifiEntry;

public class ConnectedWifiEntryPreference extends LongPressWifiEntryPreference {
    private OnGearClickListener mOnGearClickListener;

    public interface OnGearClickListener {
        void onGearClick(ConnectedWifiEntryPreference connectedWifiEntryPreference);
    }

    public ConnectedWifiEntryPreference(Context context, WifiEntry wifiEntry, Fragment fragment) {
        super(context, wifiEntry, fragment);
        setWidgetLayoutResource(R.layout.preference_widget_gear_optional_background);
    }

    public void setOnGearClickListener(OnGearClickListener onGearClickListener) {
        this.mOnGearClickListener = onGearClickListener;
        notifyChanged();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R.id.settings_button);
        findViewById.setOnClickListener(this);
        boolean canSignIn = getWifiEntry().canSignIn();
        int i = 4;
        preferenceViewHolder.findViewById(R.id.settings_button_no_background).setVisibility(canSignIn ? 4 : 0);
        findViewById.setVisibility(canSignIn ? 0 : 4);
        View findViewById2 = preferenceViewHolder.findViewById(R.id.two_target_divider);
        if (canSignIn) {
            i = 0;
        }
        findViewById2.setVisibility(i);
    }

    public void onClick(View view) {
        OnGearClickListener onGearClickListener;
        if (view.getId() == R.id.settings_button && (onGearClickListener = this.mOnGearClickListener) != null) {
            onGearClickListener.onGearClick(this);
        }
    }
}
