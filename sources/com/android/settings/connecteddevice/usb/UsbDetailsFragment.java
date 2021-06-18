package com.android.settings.connecteddevice.usb;

import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.connecteddevice.usb.UsbConnectionBroadcastReceiver;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class UsbDetailsFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.usb_details_fragment) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return RestrictedLockUtilsInternal.checkIfUsbDataSignalingIsDisabled(context, UserHandle.myUserId()) != null;
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return new ArrayList(UsbDetailsFragment.createControllerList(context, new UsbBackend(context), (UsbDetailsFragment) null));
        }
    };
    private static final String TAG = UsbDetailsFragment.class.getSimpleName();
    private List<UsbDetailsController> mControllers;
    private UsbBackend mUsbBackend;
    private UsbConnectionBroadcastReceiver.UsbConnectionListener mUsbConnectionListener = new UsbDetailsFragment$$ExternalSyntheticLambda0(this);
    UsbConnectionBroadcastReceiver mUsbReceiver;

    public int getMetricsCategory() {
        return 1291;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.usb_details_fragment;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(boolean z, long j, int i, int i2) {
        for (UsbDetailsController refresh : this.mControllers) {
            refresh.refresh(z, j, i, i2);
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Utils.setActionBarShadowAnimation(getActivity(), getSettingsLifecycle(), getListView());
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        UsbBackend usbBackend = new UsbBackend(context);
        this.mUsbBackend = usbBackend;
        this.mControllers = createControllerList(context, usbBackend, this);
        this.mUsbReceiver = new UsbConnectionBroadcastReceiver(context, this.mUsbConnectionListener, this.mUsbBackend);
        getSettingsLifecycle().addObserver(this.mUsbReceiver);
        return new ArrayList(this.mControllers);
    }

    /* access modifiers changed from: private */
    public static List<UsbDetailsController> createControllerList(Context context, UsbBackend usbBackend, UsbDetailsFragment usbDetailsFragment) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new UsbDetailsHeaderController(context, usbDetailsFragment, usbBackend));
        arrayList.add(new UsbDetailsDataRoleController(context, usbDetailsFragment, usbBackend));
        arrayList.add(new UsbDetailsFunctionsController(context, usbDetailsFragment, usbBackend));
        arrayList.add(new UsbDetailsPowerRoleController(context, usbDetailsFragment, usbBackend));
        arrayList.add(new UsbDetailsTranscodeMtpController(context, usbDetailsFragment, usbBackend));
        return arrayList;
    }
}
