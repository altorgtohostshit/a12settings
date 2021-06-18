package com.android.settings.vpn2;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.net.VpnManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.vpn2.AppDialog;
import java.util.List;

public class AppDialogFragment extends InstrumentedDialogFragment implements AppDialog.Listener {
    private DevicePolicyManager mDevicePolicyManager;
    private Listener mListener;
    private PackageInfo mPackageInfo;
    private UserManager mUserManager;
    private VpnManager mVpnManager;

    public interface Listener {
        void onCancel();

        void onForget();
    }

    public int getMetricsCategory() {
        return 546;
    }

    public static void show(Fragment fragment, PackageInfo packageInfo, String str, boolean z, boolean z2) {
        if (z || z2) {
            show(fragment, (Listener) null, packageInfo, str, z, z2);
        }
    }

    public static void show(Fragment fragment, Listener listener, PackageInfo packageInfo, String str, boolean z, boolean z2) {
        if (fragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("package", packageInfo);
            bundle.putString("label", str);
            bundle.putBoolean("managing", z);
            bundle.putBoolean("connected", z2);
            AppDialogFragment appDialogFragment = new AppDialogFragment();
            appDialogFragment.mListener = listener;
            appDialogFragment.setArguments(bundle);
            appDialogFragment.setTargetFragment(fragment, 0);
            appDialogFragment.show(fragment.getFragmentManager(), "vpnappdialog");
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPackageInfo = (PackageInfo) getArguments().getParcelable("package");
        this.mUserManager = UserManager.get(getContext());
        this.mDevicePolicyManager = (DevicePolicyManager) getContext().createContextAsUser(UserHandle.of(getUserId()), 0).getSystemService(DevicePolicyManager.class);
        this.mVpnManager = (VpnManager) getContext().getSystemService(VpnManager.class);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        String string = arguments.getString("label");
        boolean z = arguments.getBoolean("managing");
        boolean z2 = arguments.getBoolean("connected");
        if (z) {
            return new AppDialog(getActivity(), this, this.mPackageInfo, string);
        }
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(getActivity()).setTitle((CharSequence) string).setMessage((CharSequence) getActivity().getString(R.string.vpn_disconnect_confirm)).setNegativeButton((CharSequence) getActivity().getString(R.string.vpn_cancel), (DialogInterface.OnClickListener) null);
        if (z2 && !isUiRestricted()) {
            negativeButton.setPositiveButton((CharSequence) getActivity().getString(R.string.vpn_disconnect), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AppDialogFragment.this.onDisconnect(dialogInterface);
                }
            });
        }
        return negativeButton.create();
    }

    public void onCancel(DialogInterface dialogInterface) {
        dismiss();
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onCancel();
        }
        super.onCancel(dialogInterface);
    }

    public void onForget(DialogInterface dialogInterface) {
        if (!isUiRestricted()) {
            this.mVpnManager.setVpnPackageAuthorization(this.mPackageInfo.packageName, getUserId(), -1);
            onDisconnect(dialogInterface);
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onForget();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDisconnect(DialogInterface dialogInterface) {
        if (!isUiRestricted()) {
            int userId = getUserId();
            if (this.mPackageInfo.packageName.equals(VpnUtils.getConnectedPackage(this.mVpnManager, userId))) {
                this.mVpnManager.setAlwaysOnVpnPackageForUser(userId, (String) null, false, (List) null);
                this.mVpnManager.prepareVpn(this.mPackageInfo.packageName, "[Legacy VPN]", userId);
            }
        }
    }

    private boolean isUiRestricted() {
        if (this.mUserManager.hasUserRestriction("no_config_vpn", UserHandle.of(getUserId()))) {
            return true;
        }
        return this.mPackageInfo.packageName.equals(this.mDevicePolicyManager.getAlwaysOnVpnPackage());
    }

    private int getUserId() {
        return UserHandle.getUserId(this.mPackageInfo.applicationInfo.uid);
    }
}
