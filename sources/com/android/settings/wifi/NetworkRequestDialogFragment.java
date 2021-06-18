package com.android.settings.wifi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.internal.PreferenceImageView;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.Utils;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkRequestDialogFragment extends NetworkRequestDialogBaseFragment implements DialogInterface.OnClickListener, WifiPickerTracker.WifiPickerTrackerCallback {
    private WifiEntryAdapter mDialogAdapter;
    List<WifiEntry> mFilteredWifiEntries = new ArrayList();
    List<ScanResult> mMatchedScanResults = new ArrayList();
    private boolean mShowLimitedItem = true;
    private WifiManager.NetworkRequestUserSelectionCallback mUserSelectionCallback;
    WifiPickerTracker mWifiPickerTracker;
    private HandlerThread mWorkerThread;

    public void onNumSavedNetworksChanged() {
    }

    public void onNumSavedSubscriptionsChanged() {
    }

    public static NetworkRequestDialogFragment newInstance() {
        return new NetworkRequestDialogFragment();
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [com.android.settings.wifi.NetworkRequestDialogFragment$1, java.time.Clock] */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        HandlerThread handlerThread = new HandlerThread("NetworkRequestDialogFragment{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        ? r7 = new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        Context context = getContext();
        this.mWifiPickerTracker = FeatureFactory.getFactory(context).getWifiTrackerLibProvider().createWifiPickerTracker(getSettingsLifecycle(), context, new Handler(Looper.getMainLooper()), this.mWorkerThread.getThreadHandler(), r7, 15000, 10000, this);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Context context = getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.network_request_dialog_title, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.network_request_title_text)).setText(getTitle());
        ((TextView) inflate.findViewById(R.id.network_request_summary_text)).setText(getSummary());
        ((ProgressBar) inflate.findViewById(R.id.network_request_title_progress)).setVisibility(0);
        this.mDialogAdapter = new WifiEntryAdapter(context, R.layout.preference_access_point, this.mFilteredWifiEntries);
        AlertDialog create = new AlertDialog.Builder(context).setCustomTitle(inflate).setAdapter(this.mDialogAdapter, this).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new NetworkRequestDialogFragment$$ExternalSyntheticLambda0(this)).setNeutralButton(R.string.network_connection_request_dialog_showall, (DialogInterface.OnClickListener) null).create();
        create.getListView().setOnItemClickListener(new NetworkRequestDialogFragment$$ExternalSyntheticLambda3(this, create));
        setCancelable(false);
        create.setOnShowListener(new NetworkRequestDialogFragment$$ExternalSyntheticLambda1(this, create));
        return create;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        onCancel(dialogInterface);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(AlertDialog alertDialog, AdapterView adapterView, View view, int i, long j) {
        onClick(alertDialog, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$3(AlertDialog alertDialog, DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(-3);
        button.setVisibility(8);
        button.setOnClickListener(new NetworkRequestDialogFragment$$ExternalSyntheticLambda2(this, button));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$2(Button button, View view) {
        this.mShowLimitedItem = false;
        updateWifiEntries();
        updateUi();
        button.setVisibility(8);
    }

    private BaseAdapter getDialogAdapter() {
        return this.mDialogAdapter;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.mFilteredWifiEntries.size() != 0 && i < this.mFilteredWifiEntries.size() && this.mUserSelectionCallback != null) {
            WifiEntry wifiEntry = this.mFilteredWifiEntries.get(i);
            WifiConfiguration wifiConfiguration = wifiEntry.getWifiConfiguration();
            if (wifiConfiguration == null) {
                wifiConfiguration = WifiUtils.getWifiConfig(wifiEntry, (ScanResult) null);
            }
            this.mUserSelectionCallback.select(wifiConfiguration);
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        WifiManager.NetworkRequestUserSelectionCallback networkRequestUserSelectionCallback = this.mUserSelectionCallback;
        if (networkRequestUserSelectionCallback != null) {
            networkRequestUserSelectionCallback.reject();
        }
    }

    public void onDestroy() {
        this.mWorkerThread.quit();
        super.onDestroy();
    }

    private void showAllButton() {
        Button button;
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null && (button = alertDialog.getButton(-3)) != null) {
            button.setVisibility(0);
        }
    }

    private void hideProgressIcon() {
        View findViewById;
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null && (findViewById = alertDialog.findViewById(R.id.network_request_title_progress)) != null) {
            findViewById.setVisibility(8);
        }
    }

    public void onWifiStateChanged() {
        if (this.mMatchedScanResults.size() != 0) {
            updateWifiEntries();
            updateUi();
        }
    }

    public void onWifiEntriesChanged() {
        if (this.mMatchedScanResults.size() != 0) {
            updateWifiEntries();
            updateUi();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateWifiEntries() {
        ArrayList arrayList = new ArrayList();
        if (this.mWifiPickerTracker.getConnectedWifiEntry() != null) {
            arrayList.add(this.mWifiPickerTracker.getConnectedWifiEntry());
        }
        arrayList.addAll(this.mWifiPickerTracker.getWifiEntries());
        this.mFilteredWifiEntries.clear();
        this.mFilteredWifiEntries.addAll((Collection) arrayList.stream().filter(new NetworkRequestDialogFragment$$ExternalSyntheticLambda4(this)).limit(this.mShowLimitedItem ? 5 : Long.MAX_VALUE).collect(Collectors.toList()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateWifiEntries$4(WifiEntry wifiEntry) {
        for (ScanResult next : this.mMatchedScanResults) {
            if (TextUtils.equals(wifiEntry.getSsid(), next.SSID) && wifiEntry.getSecurity() == WifiUtils.getWifiEntrySecurity(next)) {
                return true;
            }
        }
        return false;
    }

    private class WifiEntryAdapter extends ArrayAdapter<WifiEntry> {
        private final LayoutInflater mInflater;
        private final int mResourceId;

        WifiEntryAdapter(Context context, int i, List<WifiEntry> list) {
            super(context, i, list);
            this.mResourceId = i;
            this.mInflater = LayoutInflater.from(context);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(this.mResourceId, viewGroup, false);
                view.findViewById(R.id.two_target_divider).setVisibility(8);
            }
            WifiEntry wifiEntry = (WifiEntry) getItem(i);
            TextView textView = (TextView) view.findViewById(16908310);
            if (textView != null) {
                textView.setSingleLine(false);
                textView.setText(wifiEntry.getTitle());
            }
            TextView textView2 = (TextView) view.findViewById(16908304);
            if (textView2 != null) {
                String summary = wifiEntry.getSummary();
                if (TextUtils.isEmpty(summary)) {
                    textView2.setVisibility(8);
                } else {
                    textView2.setVisibility(0);
                    textView2.setText(summary);
                }
            }
            PreferenceImageView preferenceImageView = (PreferenceImageView) view.findViewById(16908294);
            int level = wifiEntry.getLevel();
            if (preferenceImageView != null) {
                Drawable drawable = getContext().getDrawable(Utils.getWifiIconResource(level));
                drawable.setTintList(Utils.getColorAttr(getContext(), 16843817));
                preferenceImageView.setImageDrawable(drawable);
            }
            return view;
        }
    }

    public void onUserSelectionCallbackRegistration(WifiManager.NetworkRequestUserSelectionCallback networkRequestUserSelectionCallback) {
        this.mUserSelectionCallback = networkRequestUserSelectionCallback;
    }

    public void onMatch(List<ScanResult> list) {
        this.mMatchedScanResults = list;
        updateWifiEntries();
        updateUi();
    }

    /* access modifiers changed from: package-private */
    public void updateUi() {
        if (this.mShowLimitedItem && this.mFilteredWifiEntries.size() >= 5) {
            showAllButton();
        }
        if (this.mFilteredWifiEntries.size() > 0) {
            hideProgressIcon();
        }
        if (getDialogAdapter() != null) {
            getDialogAdapter().notifyDataSetChanged();
        }
    }
}
