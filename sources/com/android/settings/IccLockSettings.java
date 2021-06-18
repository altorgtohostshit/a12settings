package com.android.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.PinResult;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.R$styleable;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.EditPinPreference;
import com.android.settings.network.ProxySubscriptionManager;
import com.android.settings.network.SubscriptionUtil;
import java.util.ArrayList;
import java.util.List;

public class IccLockSettings extends SettingsPreferenceFragment implements EditPinPreference.OnPinEnteredListener {
    private int mDialogState = 0;
    private TabHost.TabContentFactory mEmptyTabContent = new TabHost.TabContentFactory() {
        public View createTabContent(String str) {
            return new View(IccLockSettings.this.mTabHost.getContext());
        }
    };
    private String mError;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 102) {
                IccLockSettings.this.updatePreferences();
            }
        }
    };
    private ListView mListView;
    private String mNewPin;
    private String mOldPin;
    private String mPin;
    private EditPinPreference mPinDialog;
    private SwitchPreference mPinToggle;
    private ProxySubscriptionManager mProxySubscriptionMgr;
    private Resources mRes;
    private final BroadcastReceiver mSimStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                IccLockSettings.this.mHandler.sendMessage(IccLockSettings.this.mHandler.obtainMessage(R$styleable.Constraint_layout_goneMarginStart));
            }
        }
    };
    /* access modifiers changed from: private */
    public int mSlotId = -1;
    /* access modifiers changed from: private */
    public int mSubId;
    /* access modifiers changed from: private */
    public TabHost mTabHost;
    private TabHost.OnTabChangeListener mTabListener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String str) {
            IccLockSettings iccLockSettings = IccLockSettings.this;
            int unused = iccLockSettings.mSlotId = iccLockSettings.getSlotIndexFromTag(str);
            IccLockSettings.this.updatePreferences();
        }
    };
    private TabWidget mTabWidget;
    /* access modifiers changed from: private */
    public TelephonyManager mTelephonyManager;
    private boolean mToState;

    public int getHelpResource() {
        return R.string.help_url_icc_lock;
    }

    public int getMetricsCategory() {
        return 56;
    }

    private boolean isIccLockEnabled() {
        TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(this.mSubId);
        this.mTelephonyManager = createForSubscriptionId;
        return createForSubscriptionId.isIccLockEnabled();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Utils.isMonkeyRunning()) {
            finish();
            return;
        }
        ProxySubscriptionManager instance = ProxySubscriptionManager.getInstance(getContext());
        this.mProxySubscriptionMgr = instance;
        instance.setLifecycle(getLifecycle());
        this.mTelephonyManager = (TelephonyManager) getContext().getSystemService(TelephonyManager.class);
        addPreferencesFromResource(R.xml.sim_lock_settings);
        this.mPinDialog = (EditPinPreference) findPreference("sim_pin");
        this.mPinToggle = (SwitchPreference) findPreference("sim_toggle");
        if (bundle != null) {
            if (bundle.containsKey("dialogState") && restoreDialogStates(bundle)) {
                Log.d("IccLockSettings", "onCreate: restore dialog for slotId=" + this.mSlotId + ", subId=" + this.mSubId);
            } else if (bundle.containsKey("currentTab") && restoreTabFocus(bundle)) {
                Log.d("IccLockSettings", "onCreate: restore focus on slotId=" + this.mSlotId + ", subId=" + this.mSubId);
            }
        }
        this.mPinDialog.setOnPinEnteredListener(this);
        getPreferenceScreen().setPersistent(false);
        this.mRes = getResources();
    }

    private boolean restoreDialogStates(Bundle bundle) {
        SubscriptionInfo visibleSubscriptionInfoForSimSlotIndex;
        SubscriptionInfo activeSubscriptionInfo = this.mProxySubscriptionMgr.getActiveSubscriptionInfo(bundle.getInt("dialogSubId"));
        if (activeSubscriptionInfo == null || (visibleSubscriptionInfoForSimSlotIndex = getVisibleSubscriptionInfoForSimSlotIndex(activeSubscriptionInfo.getSimSlotIndex())) == null || visibleSubscriptionInfoForSimSlotIndex.getSubscriptionId() != activeSubscriptionInfo.getSubscriptionId()) {
            return false;
        }
        this.mSlotId = activeSubscriptionInfo.getSimSlotIndex();
        this.mSubId = activeSubscriptionInfo.getSubscriptionId();
        this.mDialogState = bundle.getInt("dialogState");
        this.mPin = bundle.getString("dialogPin");
        this.mError = bundle.getString("dialogError");
        this.mToState = bundle.getBoolean("enableState");
        int i = this.mDialogState;
        if (i == 3) {
            this.mOldPin = bundle.getString("oldPinCode");
            return true;
        } else if (i != 4) {
            return true;
        } else {
            this.mOldPin = bundle.getString("oldPinCode");
            this.mNewPin = bundle.getString("newPinCode");
            return true;
        }
    }

    private boolean restoreTabFocus(Bundle bundle) {
        try {
            SubscriptionInfo visibleSubscriptionInfoForSimSlotIndex = getVisibleSubscriptionInfoForSimSlotIndex(Integer.parseInt(bundle.getString("currentTab")));
            if (visibleSubscriptionInfoForSimSlotIndex == null) {
                return false;
            }
            this.mSlotId = visibleSubscriptionInfoForSimSlotIndex.getSimSlotIndex();
            this.mSubId = visibleSubscriptionInfoForSimSlotIndex.getSubscriptionId();
            TabHost tabHost = this.mTabHost;
            if (tabHost == null) {
                return true;
            }
            tabHost.setCurrentTabByTag(getTagForSlotId(this.mSlotId));
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int activeSubscriptionInfoCountMax = this.mProxySubscriptionMgr.getActiveSubscriptionInfoCountMax();
        ArrayList<SubscriptionInfo> arrayList = new ArrayList<>();
        for (int i = 0; i < activeSubscriptionInfoCountMax; i++) {
            SubscriptionInfo visibleSubscriptionInfoForSimSlotIndex = getVisibleSubscriptionInfoForSimSlotIndex(i);
            if (visibleSubscriptionInfoForSimSlotIndex != null) {
                arrayList.add(visibleSubscriptionInfoForSimSlotIndex);
            }
        }
        if (arrayList.size() == 0) {
            Log.e("IccLockSettings", "onCreateView: no sim info");
            return super.onCreateView(layoutInflater, viewGroup, bundle);
        }
        if (this.mSlotId < 0) {
            this.mSlotId = ((SubscriptionInfo) arrayList.get(0)).getSimSlotIndex();
            this.mSubId = ((SubscriptionInfo) arrayList.get(0)).getSubscriptionId();
            Log.d("IccLockSettings", "onCreateView: default slotId=" + this.mSlotId + ", subId=" + this.mSubId);
        }
        if (arrayList.size() <= 1) {
            return super.onCreateView(layoutInflater, viewGroup, bundle);
        }
        View inflate = layoutInflater.inflate(R.layout.icc_lock_tabs, viewGroup, false);
        ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(R.id.prefs_container);
        Utils.prepareCustomPreferencesList(viewGroup, inflate, viewGroup2, false);
        viewGroup2.addView(super.onCreateView(layoutInflater, viewGroup2, bundle));
        this.mTabHost = (TabHost) inflate.findViewById(16908306);
        this.mTabWidget = (TabWidget) inflate.findViewById(16908307);
        this.mListView = (ListView) inflate.findViewById(16908298);
        this.mTabHost.setup();
        this.mTabHost.clearAllTabs();
        for (SubscriptionInfo subscriptionInfo : arrayList) {
            this.mTabHost.addTab(buildTabSpec(getTagForSlotId(subscriptionInfo.getSimSlotIndex()), String.valueOf(SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, getContext()))));
        }
        this.mTabHost.setCurrentTabByTag(getTagForSlotId(this.mSlotId));
        this.mTabHost.setOnTabChangedListener(this.mTabListener);
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        updatePreferences();
    }

    /* access modifiers changed from: private */
    public void updatePreferences() {
        SubscriptionInfo visibleSubscriptionInfoForSimSlotIndex = getVisibleSubscriptionInfoForSimSlotIndex(this.mSlotId);
        int subscriptionId = visibleSubscriptionInfoForSimSlotIndex != null ? visibleSubscriptionInfoForSimSlotIndex.getSubscriptionId() : -1;
        if (this.mSubId != subscriptionId) {
            this.mSubId = subscriptionId;
            resetDialogState();
            EditPinPreference editPinPreference = this.mPinDialog;
            if (editPinPreference != null && editPinPreference.isDialogOpen()) {
                this.mPinDialog.getDialog().dismiss();
            }
        }
        EditPinPreference editPinPreference2 = this.mPinDialog;
        boolean z = true;
        if (editPinPreference2 != null) {
            editPinPreference2.setEnabled(visibleSubscriptionInfoForSimSlotIndex != null);
        }
        SwitchPreference switchPreference = this.mPinToggle;
        if (switchPreference != null) {
            if (visibleSubscriptionInfoForSimSlotIndex == null) {
                z = false;
            }
            switchPreference.setEnabled(z);
            if (visibleSubscriptionInfoForSimSlotIndex != null) {
                this.mPinToggle.setChecked(isIccLockEnabled());
            }
        }
    }

    public void onResume() {
        super.onResume();
        getContext().registerReceiver(this.mSimStateReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"));
        if (this.mDialogState != 0) {
            showPinDialog();
        } else {
            resetDialogState();
        }
    }

    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(this.mSimStateReceiver);
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.mPinDialog.isDialogOpen()) {
            bundle.putInt("dialogSubId", this.mSubId);
            bundle.putInt("dialogState", this.mDialogState);
            bundle.putString("dialogPin", this.mPinDialog.getEditText().getText().toString());
            bundle.putString("dialogError", this.mError);
            bundle.putBoolean("enableState", this.mToState);
            int i = this.mDialogState;
            if (i == 3) {
                bundle.putString("oldPinCode", this.mOldPin);
            } else if (i == 4) {
                bundle.putString("oldPinCode", this.mOldPin);
                bundle.putString("newPinCode", this.mNewPin);
            }
        } else {
            super.onSaveInstanceState(bundle);
        }
        TabHost tabHost = this.mTabHost;
        if (tabHost != null) {
            bundle.putString("currentTab", tabHost.getCurrentTabTag());
        }
    }

    private void showPinDialog() {
        if (this.mDialogState != 0) {
            setDialogValues();
            this.mPinDialog.showPinDialog();
            EditText editText = this.mPinDialog.getEditText();
            if (!TextUtils.isEmpty(this.mPin) && editText != null) {
                editText.setSelection(this.mPin.length());
            }
        }
    }

    private void setDialogValues() {
        String str;
        String str2;
        this.mPinDialog.setText(this.mPin);
        int i = this.mDialogState;
        if (i == 1) {
            str = this.mRes.getString(R.string.sim_enter_pin);
            EditPinPreference editPinPreference = this.mPinDialog;
            if (this.mToState) {
                str2 = this.mRes.getString(R.string.sim_enable_sim_lock);
            } else {
                str2 = this.mRes.getString(R.string.sim_disable_sim_lock);
            }
            editPinPreference.setDialogTitle(str2);
        } else if (i == 2) {
            str = this.mRes.getString(R.string.sim_enter_old);
            this.mPinDialog.setDialogTitle(this.mRes.getString(R.string.sim_change_pin));
        } else if (i == 3) {
            str = this.mRes.getString(R.string.sim_enter_new);
            this.mPinDialog.setDialogTitle(this.mRes.getString(R.string.sim_change_pin));
        } else if (i != 4) {
            str = "";
        } else {
            str = this.mRes.getString(R.string.sim_reenter_new);
            this.mPinDialog.setDialogTitle(this.mRes.getString(R.string.sim_change_pin));
        }
        if (this.mError != null) {
            str = this.mError + "\n" + str;
            this.mError = null;
        }
        this.mPinDialog.setDialogMessage(str);
    }

    public void onPinEntered(EditPinPreference editPinPreference, boolean z) {
        if (!z) {
            resetDialogState();
            return;
        }
        String text = editPinPreference.getText();
        this.mPin = text;
        if (!reasonablePin(text)) {
            this.mError = this.mRes.getString(R.string.sim_bad_pin);
            showPinDialog();
            return;
        }
        int i = this.mDialogState;
        if (i == 1) {
            tryChangeIccLockState();
        } else if (i == 2) {
            this.mOldPin = this.mPin;
            this.mDialogState = 3;
            this.mError = null;
            this.mPin = null;
            showPinDialog();
        } else if (i == 3) {
            this.mNewPin = this.mPin;
            this.mDialogState = 4;
            this.mPin = null;
            showPinDialog();
        } else if (i == 4) {
            if (!this.mPin.equals(this.mNewPin)) {
                this.mError = this.mRes.getString(R.string.sim_pins_dont_match);
                this.mDialogState = 3;
                this.mPin = null;
                showPinDialog();
                return;
            }
            this.mError = null;
            tryChangePin();
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        SwitchPreference switchPreference = this.mPinToggle;
        if (preference == switchPreference) {
            boolean isChecked = switchPreference.isChecked();
            this.mToState = isChecked;
            this.mPinToggle.setChecked(!isChecked);
            this.mDialogState = 1;
            showPinDialog();
        } else if (preference == this.mPinDialog) {
            this.mDialogState = 2;
            return false;
        }
        return true;
    }

    private void tryChangeIccLockState() {
        new SetIccLockEnabled(this.mToState, this.mPin).execute(new Void[0]);
        this.mPinToggle.setEnabled(false);
    }

    private class SetIccLockEnabled extends AsyncTask<Void, Void, PinResult> {
        private final String mPin;
        private final boolean mState;

        private SetIccLockEnabled(boolean z, String str) {
            this.mState = z;
            this.mPin = str;
        }

        /* access modifiers changed from: protected */
        public PinResult doInBackground(Void... voidArr) {
            IccLockSettings iccLockSettings = IccLockSettings.this;
            TelephonyManager unused = iccLockSettings.mTelephonyManager = iccLockSettings.mTelephonyManager.createForSubscriptionId(IccLockSettings.this.mSubId);
            return IccLockSettings.this.mTelephonyManager.setIccLockEnabled(this.mState, this.mPin);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(PinResult pinResult) {
            IccLockSettings.this.iccLockChanged(pinResult.getResult() == 0, pinResult.getAttemptsRemaining());
        }
    }

    /* access modifiers changed from: private */
    public void iccLockChanged(boolean z, int i) {
        Log.d("IccLockSettings", "iccLockChanged: success = " + z);
        if (z) {
            this.mPinToggle.setChecked(this.mToState);
        } else if (i >= 0) {
            createCustomTextToast(getPinPasswordErrorMessage(i));
        } else if (this.mToState) {
            Toast.makeText(getContext(), this.mRes.getString(R.string.sim_pin_enable_failed), 1).show();
        } else {
            Toast.makeText(getContext(), this.mRes.getString(R.string.sim_pin_disable_failed), 1).show();
        }
        this.mPinToggle.setEnabled(true);
        resetDialogState();
    }

    private void createCustomTextToast(CharSequence charSequence) {
        final View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(17367351, (ViewGroup) null);
        ((TextView) inflate.findViewById(16908299)).setText(charSequence);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int absoluteGravity = Gravity.getAbsoluteGravity(getContext().getResources().getInteger(17694925), inflate.getContext().getResources().getConfiguration().getLayoutDirection());
        layoutParams.gravity = absoluteGravity;
        if ((absoluteGravity & 7) == 7) {
            layoutParams.horizontalWeight = 1.0f;
        }
        if ((absoluteGravity & 112) == 112) {
            layoutParams.verticalWeight = 1.0f;
        }
        layoutParams.y = getContext().getResources().getDimensionPixelSize(17105579);
        layoutParams.height = -2;
        layoutParams.width = -2;
        layoutParams.format = -3;
        layoutParams.windowAnimations = 16973828;
        layoutParams.type = 2017;
        layoutParams.setFitInsetsTypes(layoutParams.getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
        layoutParams.setTitle(charSequence);
        layoutParams.flags = 152;
        final WindowManager windowManager = (WindowManager) getSystemService("window");
        windowManager.addView(inflate, layoutParams);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                windowManager.removeViewImmediate(inflate);
            }
        }, 7000);
    }

    /* access modifiers changed from: private */
    public void iccPinChanged(boolean z, int i) {
        Log.d("IccLockSettings", "iccPinChanged: success = " + z);
        if (!z) {
            createCustomTextToast(getPinPasswordErrorMessage(i));
        } else {
            Toast.makeText(getContext(), this.mRes.getString(R.string.sim_change_succeeded), 0).show();
        }
        resetDialogState();
    }

    private void tryChangePin() {
        new ChangeIccLockPin(this.mOldPin, this.mNewPin).execute(new Void[0]);
    }

    private class ChangeIccLockPin extends AsyncTask<Void, Void, PinResult> {
        private final String mNewPin;
        private final String mOldPin;

        private ChangeIccLockPin(String str, String str2) {
            this.mOldPin = str;
            this.mNewPin = str2;
        }

        /* access modifiers changed from: protected */
        public PinResult doInBackground(Void... voidArr) {
            IccLockSettings iccLockSettings = IccLockSettings.this;
            TelephonyManager unused = iccLockSettings.mTelephonyManager = iccLockSettings.mTelephonyManager.createForSubscriptionId(IccLockSettings.this.mSubId);
            return IccLockSettings.this.mTelephonyManager.changeIccLockPin(this.mOldPin, this.mNewPin);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(PinResult pinResult) {
            IccLockSettings.this.iccPinChanged(pinResult.getResult() == 0, pinResult.getAttemptsRemaining());
        }
    }

    private String getPinPasswordErrorMessage(int i) {
        if (i == 0) {
            return this.mRes.getString(R.string.wrong_pin_code_pukked);
        }
        if (i == 1) {
            return this.mRes.getString(R.string.wrong_pin_code_one, new Object[]{Integer.valueOf(i)});
        } else if (i <= 1) {
            return this.mRes.getString(R.string.pin_failed);
        } else {
            return this.mRes.getQuantityString(R.plurals.wrong_pin_code, i, new Object[]{Integer.valueOf(i)});
        }
    }

    private boolean reasonablePin(String str) {
        return str != null && str.length() >= 4 && str.length() <= 8;
    }

    private void resetDialogState() {
        this.mError = null;
        this.mDialogState = 2;
        this.mPin = "";
        setDialogValues();
        this.mDialogState = 0;
    }

    private String getTagForSlotId(int i) {
        return String.valueOf(i);
    }

    /* access modifiers changed from: private */
    public int getSlotIndexFromTag(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    private SubscriptionInfo getVisibleSubscriptionInfoForSimSlotIndex(int i) {
        List<SubscriptionInfo> activeSubscriptionsInfo = this.mProxySubscriptionMgr.getActiveSubscriptionsInfo();
        if (activeSubscriptionsInfo == null) {
            return null;
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) getContext().getSystemService(CarrierConfigManager.class);
        for (SubscriptionInfo next : activeSubscriptionsInfo) {
            if (isSubscriptionVisible(carrierConfigManager, next) && next.getSimSlotIndex() == i) {
                return next;
            }
        }
        return null;
    }

    private boolean isSubscriptionVisible(CarrierConfigManager carrierConfigManager, SubscriptionInfo subscriptionInfo) {
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(subscriptionInfo.getSubscriptionId());
        if (configForSubId == null) {
            return false;
        }
        return !configForSubId.getBoolean("hide_sim_lock_settings_bool");
    }

    private TabHost.TabSpec buildTabSpec(String str, String str2) {
        return this.mTabHost.newTabSpec(str).setIndicator(str2).setContent(this.mEmptyTabContent);
    }
}
