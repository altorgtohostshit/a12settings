package com.android.settings.wifi.calling;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsMmTelManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.network.ims.WifiCallingQueryImsState;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.SliceBroadcastReceiver;
import com.android.settingslib.Utils;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WifiCallingSliceHelper {
    private final Context mContext;

    public WifiCallingSliceHelper(Context context) {
        this.mContext = context;
    }

    public Slice createWifiCallingSlice(Uri uri) {
        int defaultVoiceSubId = getDefaultVoiceSubId();
        if (!SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            Log.d("WifiCallingSliceHelper", "Invalid subscription Id");
            return null;
        } else if (!queryImsState(defaultVoiceSubId).isWifiCallingProvisioned()) {
            Log.d("WifiCallingSliceHelper", "Wifi calling is either not provisioned or not enabled by Platform");
            return null;
        } else {
            boolean isWifiCallingEnabled = isWifiCallingEnabled();
            if (getWifiCallingCarrierActivityIntent(defaultVoiceSubId) == null || isWifiCallingEnabled) {
                return getWifiCallingSlice(uri, isWifiCallingEnabled, defaultVoiceSubId);
            }
            Log.d("WifiCallingSliceHelper", "Needs Activation");
            Resources resourcesForSubId = getResourcesForSubId(defaultVoiceSubId);
            return getNonActionableWifiCallingSlice(resourcesForSubId.getText(R.string.wifi_calling_settings_title), resourcesForSubId.getText(R.string.wifi_calling_settings_activation_instructions), uri, getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"));
        }
    }

    private boolean isWifiCallingEnabled() {
        WifiCallingQueryImsState queryImsState = queryImsState(getDefaultVoiceSubId());
        return queryImsState.isEnabledByUser() && queryImsState.isAllowUserControl();
    }

    private Slice getWifiCallingSlice(Uri uri, boolean z, int i) {
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.wifi_signal);
        Resources resourcesForSubId = getResourcesForSubId(i);
        return new ListBuilder(this.mContext, uri, -1).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext)).addRow(new ListBuilder.RowBuilder().setTitle(resourcesForSubId.getText(R.string.wifi_calling_settings_title)).addEndItem(SliceAction.createToggle(getBroadcastIntent("com.android.settings.wifi.calling.action.WIFI_CALLING_CHANGED"), (CharSequence) null, z)).setPrimaryAction(SliceAction.createDeeplink(getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"), createWithResource, 0, resourcesForSubId.getText(R.string.wifi_calling_settings_title)))).build();
    }

    public Slice createWifiCallingPreferenceSlice(Uri uri) {
        int defaultVoiceSubId = getDefaultVoiceSubId();
        if (!SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            Log.d("WifiCallingSliceHelper", "Invalid Subscription Id");
            return null;
        }
        boolean isCarrierConfigManagerKeyEnabled = isCarrierConfigManagerKeyEnabled("editable_wfc_mode_bool", defaultVoiceSubId, false);
        boolean isCarrierConfigManagerKeyEnabled2 = isCarrierConfigManagerKeyEnabled("carrier_wfc_supports_wifi_only_bool", defaultVoiceSubId, true);
        if (!isCarrierConfigManagerKeyEnabled) {
            Log.d("WifiCallingSliceHelper", "Wifi calling preference is not editable");
            return null;
        } else if (!queryImsState(defaultVoiceSubId).isWifiCallingProvisioned()) {
            Log.d("WifiCallingSliceHelper", "Wifi calling is either not provisioned or not enabled by platform");
            return null;
        } else {
            try {
                ImsMmTelManager imsMmTelManager = getImsMmTelManager(defaultVoiceSubId);
                boolean isWifiCallingEnabled = isWifiCallingEnabled();
                int wfcMode = getWfcMode(imsMmTelManager);
                if (isWifiCallingEnabled) {
                    return getWifiCallingPreferenceSlice(isCarrierConfigManagerKeyEnabled2, wfcMode, uri, defaultVoiceSubId);
                }
                Resources resourcesForSubId = getResourcesForSubId(defaultVoiceSubId);
                return getNonActionableWifiCallingSlice(resourcesForSubId.getText(R.string.wifi_calling_mode_title), resourcesForSubId.getText(R.string.wifi_calling_turn_on), uri, getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Log.e("WifiCallingSliceHelper", "Unable to get wifi calling preferred mode", e);
                return null;
            }
        }
    }

    private Slice getWifiCallingPreferenceSlice(boolean z, int i, Uri uri, int i2) {
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.wifi_signal);
        Resources resourcesForSubId = getResourcesForSubId(i2);
        ListBuilder accentColor = new ListBuilder(this.mContext, uri, -1).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext));
        ListBuilder.HeaderBuilder primaryAction = new ListBuilder.HeaderBuilder().setTitle(resourcesForSubId.getText(R.string.wifi_calling_mode_title)).setPrimaryAction(SliceAction.createDeeplink(getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"), createWithResource, 0, resourcesForSubId.getText(R.string.wifi_calling_mode_title)));
        if (!com.android.settings.Utils.isSettingsIntelligence(this.mContext)) {
            primaryAction.setSubtitle(getWifiCallingPreferenceSummary(i, i2));
        }
        accentColor.setHeader(primaryAction);
        if (z) {
            accentColor.addRow(wifiPreferenceRowBuilder(accentColor, 17041599, "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_ONLY", i == 0, i2));
        }
        accentColor.addRow(wifiPreferenceRowBuilder(accentColor, 17041600, "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_PREFERRED", i == 2, i2));
        accentColor.addRow(wifiPreferenceRowBuilder(accentColor, 17041598, "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_CELLULAR_PREFERRED", i == 1, i2));
        return accentColor.build();
    }

    private ListBuilder.RowBuilder wifiPreferenceRowBuilder(ListBuilder listBuilder, int i, String str, boolean z, int i2) {
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.radio_button_check);
        Resources resourcesForSubId = getResourcesForSubId(i2);
        return new ListBuilder.RowBuilder().setTitle(resourcesForSubId.getText(i)).setTitleItem(SliceAction.createToggle(getBroadcastIntent(str), createWithResource, resourcesForSubId.getText(i), z));
    }

    private CharSequence getWifiCallingPreferenceSummary(int i, int i2) {
        Resources resourcesForSubId = getResourcesForSubId(i2);
        if (i == 0) {
            return resourcesForSubId.getText(17041599);
        }
        if (i == 1) {
            return resourcesForSubId.getText(17041598);
        }
        if (i != 2) {
            return null;
        }
        return resourcesForSubId.getText(17041600);
    }

    /* access modifiers changed from: protected */
    public ImsMmTelManager getImsMmTelManager(int i) {
        return ImsMmTelManager.createForSubscriptionId(i);
    }

    private int getWfcMode(final ImsMmTelManager imsMmTelManager) throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask futureTask = new FutureTask(new Callable<Integer>() {
            public Integer call() {
                return Integer.valueOf(imsMmTelManager.getVoWiFiModeSetting());
            }
        });
        Executors.newSingleThreadExecutor().execute(futureTask);
        return ((Integer) futureTask.get(2000, TimeUnit.MILLISECONDS)).intValue();
    }

    public void handleWifiCallingChanged(Intent intent) {
        int defaultVoiceSubId = getDefaultVoiceSubId();
        if (SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            WifiCallingQueryImsState queryImsState = queryImsState(defaultVoiceSubId);
            if (queryImsState.isWifiCallingProvisioned()) {
                boolean z = queryImsState.isEnabledByUser() && queryImsState.isAllowUserControl();
                boolean booleanExtra = intent.getBooleanExtra("android.app.slice.extra.TOGGLE_STATE", z);
                Intent wifiCallingCarrierActivityIntent = getWifiCallingCarrierActivityIntent(defaultVoiceSubId);
                if ((!booleanExtra || wifiCallingCarrierActivityIntent == null) && booleanExtra != z) {
                    getImsMmTelManager(defaultVoiceSubId).setVoWiFiSettingEnabled(booleanExtra);
                }
            }
        }
        this.mContext.getContentResolver().notifyChange(CustomSliceRegistry.WIFI_CALLING_URI, (ContentObserver) null);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0070, code lost:
        if (r3 != false) goto L_0x0076;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleWifiCallingPreferenceChanged(android.content.Intent r9) {
        /*
            r8 = this;
            int r0 = r8.getDefaultVoiceSubId()
            boolean r1 = android.telephony.SubscriptionManager.isValidSubscriptionId(r0)
            if (r1 == 0) goto L_0x007d
            java.lang.String r1 = "editable_wfc_mode_bool"
            r2 = 0
            boolean r1 = r8.isCarrierConfigManagerKeyEnabled(r1, r0, r2)
            java.lang.String r3 = "carrier_wfc_supports_wifi_only_bool"
            r4 = 1
            boolean r3 = r8.isCarrierConfigManagerKeyEnabled(r3, r0, r4)
            com.android.settings.network.ims.WifiCallingQueryImsState r5 = r8.queryImsState(r0)
            if (r1 == 0) goto L_0x007d
            boolean r1 = r5.isWifiCallingProvisioned()
            if (r1 == 0) goto L_0x007d
            boolean r1 = r5.isEnabledByUser()
            if (r1 == 0) goto L_0x007d
            boolean r1 = r5.isAllowUserControl()
            if (r1 == 0) goto L_0x007d
            android.telephony.ims.ImsMmTelManager r0 = r8.getImsMmTelManager(r0)
            int r1 = r0.getVoWiFiModeSetting()
            java.lang.String r9 = r9.getAction()
            r9.hashCode()
            int r5 = r9.hashCode()
            r6 = 2
            r7 = -1
            switch(r5) {
                case -86230637: goto L_0x0060;
                case 176882490: goto L_0x0055;
                case 495970216: goto L_0x004a;
                default: goto L_0x0048;
            }
        L_0x0048:
            r9 = r7
            goto L_0x006a
        L_0x004a:
            java.lang.String r5 = "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_CELLULAR_PREFERRED"
            boolean r9 = r9.equals(r5)
            if (r9 != 0) goto L_0x0053
            goto L_0x0048
        L_0x0053:
            r9 = r6
            goto L_0x006a
        L_0x0055:
            java.lang.String r5 = "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_ONLY"
            boolean r9 = r9.equals(r5)
            if (r9 != 0) goto L_0x005e
            goto L_0x0048
        L_0x005e:
            r9 = r4
            goto L_0x006a
        L_0x0060:
            java.lang.String r5 = "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_PREFERRED"
            boolean r9 = r9.equals(r5)
            if (r9 != 0) goto L_0x0069
            goto L_0x0048
        L_0x0069:
            r9 = r2
        L_0x006a:
            switch(r9) {
                case 0: goto L_0x0075;
                case 1: goto L_0x0070;
                case 2: goto L_0x006e;
                default: goto L_0x006d;
            }
        L_0x006d:
            goto L_0x0073
        L_0x006e:
            r2 = r4
            goto L_0x0076
        L_0x0070:
            if (r3 == 0) goto L_0x0073
            goto L_0x0076
        L_0x0073:
            r2 = r7
            goto L_0x0076
        L_0x0075:
            r2 = r6
        L_0x0076:
            if (r2 == r7) goto L_0x007d
            if (r2 == r1) goto L_0x007d
            r0.setVoWiFiModeSetting(r2)
        L_0x007d:
            android.content.Context r8 = r8.mContext
            android.content.ContentResolver r8 = r8.getContentResolver()
            android.net.Uri r9 = com.android.settings.slices.CustomSliceRegistry.WIFI_CALLING_PREFERENCE_URI
            r0 = 0
            r8.notifyChange(r9, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.calling.WifiCallingSliceHelper.handleWifiCallingPreferenceChanged(android.content.Intent):void");
    }

    private Slice getNonActionableWifiCallingSlice(CharSequence charSequence, CharSequence charSequence2, Uri uri, PendingIntent pendingIntent) {
        ListBuilder.RowBuilder primaryAction = new ListBuilder.RowBuilder().setTitle(charSequence).setPrimaryAction(SliceAction.createDeeplink(pendingIntent, IconCompat.createWithResource(this.mContext, R.drawable.wifi_signal), 1, charSequence));
        if (!com.android.settings.Utils.isSettingsIntelligence(this.mContext)) {
            primaryAction.setSubtitle(charSequence2);
        }
        return new ListBuilder(this.mContext, uri, -1).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext)).addRow(primaryAction).build();
    }

    /* access modifiers changed from: protected */
    public boolean isCarrierConfigManagerKeyEnabled(String str, int i, boolean z) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = getCarrierConfigManager(this.mContext);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean(str, z);
    }

    /* access modifiers changed from: protected */
    public CarrierConfigManager getCarrierConfigManager(Context context) {
        return (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    /* access modifiers changed from: protected */
    public int getDefaultVoiceSubId() {
        return SubscriptionManager.getDefaultVoiceSubscriptionId();
    }

    /* access modifiers changed from: protected */
    public Intent getWifiCallingCarrierActivityIntent(int i) {
        PersistableBundle configForSubId;
        ComponentName unflattenFromString;
        CarrierConfigManager carrierConfigManager = getCarrierConfigManager(this.mContext);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return null;
        }
        String string = configForSubId.getString("wfc_emergency_address_carrier_app_string");
        if (TextUtils.isEmpty(string) || (unflattenFromString = ComponentName.unflattenFromString(string)) == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.setComponent(unflattenFromString);
        return intent;
    }

    private PendingIntent getBroadcastIntent(String str) {
        Intent intent = new Intent(str);
        intent.setClass(this.mContext, SliceBroadcastReceiver.class);
        intent.addFlags(268435456);
        return PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320);
    }

    private PendingIntent getActivityIntent(String str) {
        Intent intent = new Intent(str);
        intent.setPackage("com.android.settings");
        intent.addFlags(268435456);
        return PendingIntent.getActivity(this.mContext, 0, intent, 67108864);
    }

    private Resources getResourcesForSubId(int i) {
        return SubscriptionManager.getResourcesForSubId(this.mContext, i);
    }

    /* access modifiers changed from: package-private */
    public WifiCallingQueryImsState queryImsState(int i) {
        return new WifiCallingQueryImsState(this.mContext, i);
    }
}
