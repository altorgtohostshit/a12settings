package com.android.settings.datausage;

import android.content.Context;
import android.net.NetworkPolicyManager;
import android.util.SparseIntArray;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;

public class DataSaverBackend {
    private boolean mAllowlistInitialized;
    private final Context mContext;
    private boolean mDenylistInitialized;
    private final ArrayList<Listener> mListeners = new ArrayList<>();
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final NetworkPolicyManager.Listener mPolicyListener = new NetworkPolicyManager.Listener() {
        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onUidPoliciesChanged$0(int i, int i2) {
            DataSaverBackend.this.handleUidPoliciesChanged(i, i2);
        }

        public void onUidPoliciesChanged(int i, int i2) {
            ThreadUtils.postOnMainThread(new DataSaverBackend$1$$ExternalSyntheticLambda0(this, i, i2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRestrictBackgroundChanged$1(boolean z) {
            DataSaverBackend.this.handleRestrictBackgroundChanged(z);
        }

        public void onRestrictBackgroundChanged(boolean z) {
            ThreadUtils.postOnMainThread(new DataSaverBackend$1$$ExternalSyntheticLambda1(this, z));
        }
    };
    private final NetworkPolicyManager mPolicyManager;
    private SparseIntArray mUidPolicies = new SparseIntArray();

    public interface Listener {
        void onAllowlistStatusChanged(int i, boolean z);

        void onDataSaverChanged(boolean z);

        void onDenylistStatusChanged(int i, boolean z);
    }

    public DataSaverBackend(Context context) {
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mPolicyManager = NetworkPolicyManager.from(context);
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
        if (this.mListeners.size() == 1) {
            this.mPolicyManager.registerListener(this.mPolicyListener);
        }
        listener.onDataSaverChanged(isDataSaverEnabled());
    }

    public void remListener(Listener listener) {
        this.mListeners.remove(listener);
        if (this.mListeners.size() == 0) {
            this.mPolicyManager.unregisterListener(this.mPolicyListener);
        }
    }

    public boolean isDataSaverEnabled() {
        return this.mPolicyManager.getRestrictBackground();
    }

    public void setDataSaverEnabled(boolean z) {
        this.mPolicyManager.setRestrictBackground(z);
        this.mMetricsFeatureProvider.action(this.mContext, 394, z ? 1 : 0);
    }

    public void refreshAllowlist() {
        loadAllowlist();
    }

    public void setIsAllowlisted(int i, String str, boolean z) {
        int i2 = z ? 4 : 0;
        this.mPolicyManager.setUidPolicy(i, i2);
        this.mUidPolicies.put(i, i2);
        if (z) {
            this.mMetricsFeatureProvider.action(this.mContext, 395, str);
        }
    }

    public boolean isAllowlisted(int i) {
        loadAllowlist();
        return this.mUidPolicies.get(i, 0) == 4;
    }

    private void loadAllowlist() {
        if (!this.mAllowlistInitialized) {
            for (int put : this.mPolicyManager.getUidsWithPolicy(4)) {
                this.mUidPolicies.put(put, 4);
            }
            this.mAllowlistInitialized = true;
        }
    }

    public void refreshDenylist() {
        loadDenylist();
    }

    public void setIsDenylisted(int i, String str, boolean z) {
        this.mPolicyManager.setUidPolicy(i, z ? 1 : 0);
        this.mUidPolicies.put(i, z);
        if (z) {
            this.mMetricsFeatureProvider.action(this.mContext, 396, str);
        }
    }

    public boolean isDenylisted(int i) {
        loadDenylist();
        return this.mUidPolicies.get(i, 0) == 1;
    }

    private void loadDenylist() {
        if (!this.mDenylistInitialized) {
            for (int put : this.mPolicyManager.getUidsWithPolicy(1)) {
                this.mUidPolicies.put(put, 1);
            }
            this.mDenylistInitialized = true;
        }
    }

    /* access modifiers changed from: private */
    public void handleRestrictBackgroundChanged(boolean z) {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onDataSaverChanged(z);
        }
    }

    private void handleAllowlistChanged(int i, boolean z) {
        for (int i2 = 0; i2 < this.mListeners.size(); i2++) {
            this.mListeners.get(i2).onAllowlistStatusChanged(i, z);
        }
    }

    private void handleDenylistChanged(int i, boolean z) {
        for (int i2 = 0; i2 < this.mListeners.size(); i2++) {
            this.mListeners.get(i2).onDenylistStatusChanged(i, z);
        }
    }

    /* access modifiers changed from: private */
    public void handleUidPoliciesChanged(int i, int i2) {
        loadAllowlist();
        loadDenylist();
        boolean z = false;
        int i3 = this.mUidPolicies.get(i, 0);
        if (i2 == 0) {
            this.mUidPolicies.delete(i);
        } else {
            this.mUidPolicies.put(i, i2);
        }
        boolean z2 = i3 == 4;
        boolean z3 = i3 == 1;
        boolean z4 = i2 == 4;
        if (i2 == 1) {
            z = true;
        }
        if (z2 != z4) {
            handleAllowlistChanged(i, z4);
        }
        if (z3 != z) {
            handleDenylistChanged(i, z);
        }
    }
}
