package com.android.settings.network;

import android.content.Context;
import android.content.Intent;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.MobileNetworkActivity;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.AddPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.List;
import java.util.stream.Collectors;

public class MobileNetworkSummaryController extends AbstractPreferenceController implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient, LifecycleObserver, PreferenceControllerMixin {
    private SubscriptionsChangeListener mChangeListener;
    private final MetricsFeatureProvider mMetricsFeatureProvider = FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider();
    private AddPreference mPreference;
    private SubscriptionManager mSubscriptionManager;
    private UserManager mUserManager;

    public String getPreferenceKey() {
        return "mobile_network_list";
    }

    public MobileNetworkSummaryController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        if (lifecycle != null) {
            this.mChangeListener = new SubscriptionsChangeListener(context, this);
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mChangeListener.start();
        update();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mChangeListener.stop();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (AddPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public CharSequence getSummary() {
        List<SubscriptionInfo> availableSubscriptions = SubscriptionUtil.getAvailableSubscriptions(this.mContext);
        if (availableSubscriptions.isEmpty()) {
            if (MobileNetworkUtils.showEuiccSettings(this.mContext)) {
                return this.mContext.getResources().getString(R.string.mobile_network_summary_add_a_network);
            }
            return null;
        } else if (availableSubscriptions.size() == 1) {
            SubscriptionInfo subscriptionInfo = availableSubscriptions.get(0);
            CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext);
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            if (subscriptionInfo.isEmbedded() || this.mSubscriptionManager.isActiveSubscriptionId(subscriptionId) || SubscriptionUtil.showToggleForPhysicalSim(this.mSubscriptionManager)) {
                return uniqueSubscriptionDisplayName;
            }
            return this.mContext.getString(R.string.mobile_network_tap_to_activate, new Object[]{uniqueSubscriptionDisplayName});
        } else if (Utils.isProviderModelEnabled(this.mContext)) {
            return getSummaryForProviderModel(availableSubscriptions);
        } else {
            int size = availableSubscriptions.size();
            return this.mContext.getResources().getQuantityString(R.plurals.mobile_network_summary_count, size, new Object[]{Integer.valueOf(size)});
        }
    }

    private CharSequence getSummaryForProviderModel(List<SubscriptionInfo> list) {
        return String.join(", ", (Iterable) list.stream().map(new MobileNetworkSummaryController$$ExternalSyntheticLambda3(this)).collect(Collectors.toList()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ CharSequence lambda$getSummaryForProviderModel$0(SubscriptionInfo subscriptionInfo) {
        return SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext);
    }

    private void startAddSimFlow() {
        Intent intent = new Intent("android.telephony.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION");
        intent.putExtra("android.telephony.euicc.extra.FORCE_PROVISION", true);
        this.mContext.startActivity(intent);
    }

    private void update() {
        AddPreference addPreference = this.mPreference;
        if (addPreference != null && !addPreference.isDisabledByAdmin()) {
            refreshSummary(this.mPreference);
            this.mPreference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) null);
            this.mPreference.setOnAddClickListener((AddPreference.OnAddClickListener) null);
            this.mPreference.setFragment((String) null);
            this.mPreference.setEnabled(!this.mChangeListener.isAirplaneModeOn());
            List<SubscriptionInfo> availableSubscriptions = SubscriptionUtil.getAvailableSubscriptions(this.mContext);
            if (!availableSubscriptions.isEmpty()) {
                if (MobileNetworkUtils.showEuiccSettings(this.mContext)) {
                    this.mPreference.setAddWidgetEnabled(!this.mChangeListener.isAirplaneModeOn());
                    this.mPreference.setOnAddClickListener(new MobileNetworkSummaryController$$ExternalSyntheticLambda2(this));
                }
                if (availableSubscriptions.size() == 1) {
                    this.mPreference.setOnPreferenceClickListener(new MobileNetworkSummaryController$$ExternalSyntheticLambda1(this, availableSubscriptions));
                } else {
                    this.mPreference.setFragment(MobileNetworkListFragment.class.getCanonicalName());
                }
            } else if (MobileNetworkUtils.showEuiccSettings(this.mContext)) {
                this.mPreference.setOnPreferenceClickListener(new MobileNetworkSummaryController$$ExternalSyntheticLambda0(this));
            } else {
                this.mPreference.setEnabled(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$update$1(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, preference.getExtras().getInt("category"));
        startAddSimFlow();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$update$2(AddPreference addPreference) {
        this.mMetricsFeatureProvider.logClickedPreference(addPreference, addPreference.getExtras().getInt("category"));
        startAddSimFlow();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$update$3(List list, Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, preference.getExtras().getInt("category"));
        SubscriptionInfo subscriptionInfo = (SubscriptionInfo) list.get(0);
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        if (subscriptionInfo.isEmbedded() || this.mSubscriptionManager.isActiveSubscriptionId(subscriptionId) || SubscriptionUtil.showToggleForPhysicalSim(this.mSubscriptionManager)) {
            Intent intent = new Intent(this.mContext, MobileNetworkActivity.class);
            intent.putExtra("android.provider.extra.SUB_ID", ((SubscriptionInfo) list.get(0)).getSubscriptionId());
            this.mContext.startActivity(intent);
        } else {
            SubscriptionUtil.startToggleSubscriptionDialogActivity(this.mContext, subscriptionId, true);
        }
        return true;
    }

    public boolean isAvailable() {
        return !com.android.settingslib.Utils.isWifiOnly(this.mContext) && this.mUserManager.isAdminUser();
    }

    public void onAirplaneModeChanged(boolean z) {
        update();
    }

    public void onSubscriptionsChanged() {
        refreshSummary(this.mPreference);
        update();
    }
}
