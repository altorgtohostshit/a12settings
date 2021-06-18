package com.android.settings.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.wifi.dpp.WifiDppUtils;

public class AddWifiNetworkPreference extends Preference {
    private final Drawable mScanIconDrawable = getDrawable(R.drawable.ic_scan_24dp);

    public AddWifiNetworkPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.preference_access_point);
        setWidgetLayoutResource(R.layout.wifi_button_preference_widget);
        setIcon((int) R.drawable.ic_add_24dp);
        setTitle((int) R.string.wifi_add_network);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageButton imageButton = (ImageButton) preferenceViewHolder.findViewById(R.id.button_icon);
        imageButton.setImageDrawable(this.mScanIconDrawable);
        imageButton.setContentDescription(getContext().getString(R.string.wifi_dpp_scan_qr_code));
        imageButton.setOnClickListener(new AddWifiNetworkPreference$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        getContext().startActivity(WifiDppUtils.getEnrolleeQrCodeScannerIntent((String) null));
    }

    private Drawable getDrawable(int i) {
        try {
            return getContext().getDrawable(i);
        } catch (Resources.NotFoundException unused) {
            Log.e("AddWifiNetworkPreference", "Resource does not exist: " + i);
            return null;
        }
    }
}
