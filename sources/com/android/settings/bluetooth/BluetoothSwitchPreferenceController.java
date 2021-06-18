package com.android.settings.bluetooth;

import android.content.Context;
import android.view.View;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.location.BluetoothScanningFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.utils.AnnotationSpan;
import com.android.settings.widget.SwitchWidgetController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.FooterPreference;

public class BluetoothSwitchPreferenceController implements LifecycleObserver, OnStart, OnStop, SwitchWidgetController.OnSwitchChangeListener, View.OnClickListener {
    AlwaysDiscoverable mAlwaysDiscoverable;
    private BluetoothEnabler mBluetoothEnabler;
    private Context mContext;
    private FooterPreference mFooterPreference;
    private RestrictionUtils mRestrictionUtils;
    private SwitchWidgetController mSwitch;

    public BluetoothSwitchPreferenceController(Context context, SwitchWidgetController switchWidgetController, FooterPreference footerPreference) {
        this(context, new RestrictionUtils(), switchWidgetController, footerPreference);
    }

    public BluetoothSwitchPreferenceController(Context context, RestrictionUtils restrictionUtils, SwitchWidgetController switchWidgetController, FooterPreference footerPreference) {
        this.mRestrictionUtils = restrictionUtils;
        this.mSwitch = switchWidgetController;
        this.mContext = context;
        this.mFooterPreference = footerPreference;
        switchWidgetController.setupView();
        updateText(this.mSwitch.isChecked());
        BluetoothEnabler bluetoothEnabler = new BluetoothEnabler(context, switchWidgetController, FeatureFactory.getFactory(context).getMetricsFeatureProvider(), 870, this.mRestrictionUtils);
        this.mBluetoothEnabler = bluetoothEnabler;
        bluetoothEnabler.setToggleCallback(this);
        this.mAlwaysDiscoverable = new AlwaysDiscoverable(context);
    }

    public void onStart() {
        this.mBluetoothEnabler.resume(this.mContext);
        this.mAlwaysDiscoverable.start();
        SwitchWidgetController switchWidgetController = this.mSwitch;
        if (switchWidgetController != null) {
            updateText(switchWidgetController.isChecked());
        }
    }

    public void onStop() {
        this.mBluetoothEnabler.pause();
        this.mAlwaysDiscoverable.stop();
    }

    public boolean onSwitchToggled(boolean z) {
        updateText(z);
        return true;
    }

    public void onClick(View view) {
        new SubSettingLauncher(this.mContext).setDestination(BluetoothScanningFragment.class.getName()).setSourceMetricsCategory(1390).launch();
    }

    /* access modifiers changed from: package-private */
    public void updateText(boolean z) {
        if (z || !Utils.isBluetoothScanningEnabled(this.mContext)) {
            this.mFooterPreference.setTitle((int) R.string.bluetooth_empty_list_bluetooth_off);
            return;
        }
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo("link", this);
        this.mFooterPreference.setTitle(AnnotationSpan.linkify(this.mContext.getText(R.string.bluetooth_scanning_on_info_message), linkInfo));
    }
}
