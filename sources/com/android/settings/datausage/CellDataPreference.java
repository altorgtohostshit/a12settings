package com.android.settings.datausage;

import android.content.Context;
import android.content.DialogInterface;
import android.net.NetworkTemplate;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.datausage.TemplatePreference;
import com.android.settings.network.MobileDataEnabledListener;
import com.android.settings.network.ProxySubscriptionManager;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.CustomDialogPreferenceCompat;

public class CellDataPreference extends CustomDialogPreferenceCompat implements TemplatePreference, MobileDataEnabledListener.Client {
    public boolean mChecked;
    private MobileDataEnabledListener mDataStateListener;
    public boolean mMultiSimDialog;
    final ProxySubscriptionManager.OnActiveSubscriptionChangedListener mOnSubscriptionsChangeListener = new ProxySubscriptionManager.OnActiveSubscriptionChangedListener() {
        public void onChanged() {
            CellDataPreference.this.updateEnabled();
        }
    };
    public int mSubId = -1;

    public CellDataPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.switchPreferenceStyle, 16843629));
        this.mDataStateListener = new MobileDataEnabledListener(context, this);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        CellDataState cellDataState = (CellDataState) parcelable;
        super.onRestoreInstanceState(cellDataState.getSuperState());
        this.mChecked = cellDataState.mChecked;
        this.mMultiSimDialog = cellDataState.mMultiSimDialog;
        if (this.mSubId == -1) {
            this.mSubId = cellDataState.mSubId;
            setKey(getKey() + this.mSubId);
        }
        notifyChanged();
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        CellDataState cellDataState = new CellDataState(super.onSaveInstanceState());
        cellDataState.mChecked = this.mChecked;
        cellDataState.mMultiSimDialog = this.mMultiSimDialog;
        cellDataState.mSubId = this.mSubId;
        return cellDataState;
    }

    public void onAttached() {
        super.onAttached();
        this.mDataStateListener.start(this.mSubId);
        getProxySubscriptionManager().addActiveSubscriptionsListener(this.mOnSubscriptionsChangeListener);
    }

    public void onDetached() {
        this.mDataStateListener.stop();
        getProxySubscriptionManager().removeActiveSubscriptionsListener(this.mOnSubscriptionsChangeListener);
        super.onDetached();
    }

    public void setTemplate(NetworkTemplate networkTemplate, int i, TemplatePreference.NetworkServices networkServices) {
        if (i != -1) {
            getProxySubscriptionManager().addActiveSubscriptionsListener(this.mOnSubscriptionsChangeListener);
            if (this.mSubId == -1) {
                this.mSubId = i;
                setKey(getKey() + i);
            }
            updateEnabled();
            updateChecked();
            return;
        }
        throw new IllegalArgumentException("CellDataPreference needs a SubscriptionInfo");
    }

    /* access modifiers changed from: package-private */
    public ProxySubscriptionManager getProxySubscriptionManager() {
        return ProxySubscriptionManager.getInstance(getContext());
    }

    /* access modifiers changed from: package-private */
    public SubscriptionInfo getActiveSubscriptionInfo(int i) {
        return getProxySubscriptionManager().getActiveSubscriptionInfo(i);
    }

    private void updateChecked() {
        setChecked(((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).getDataEnabled(this.mSubId));
    }

    /* access modifiers changed from: private */
    public void updateEnabled() {
        setEnabled(getActiveSubscriptionInfo(this.mSubId) != null);
    }

    /* access modifiers changed from: protected */
    public void performClick(View view) {
        Context context = getContext();
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(context, 178, !this.mChecked);
        SubscriptionInfo activeSubscriptionInfo = getActiveSubscriptionInfo(this.mSubId);
        SubscriptionInfo activeSubscriptionInfo2 = getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        if (this.mChecked) {
            setMobileDataEnabled(false);
            if (activeSubscriptionInfo2 != null && activeSubscriptionInfo != null && activeSubscriptionInfo.getSubscriptionId() == activeSubscriptionInfo2.getSubscriptionId()) {
                disableDataForOtherSubscriptions(this.mSubId);
                return;
            }
            return;
        }
        setMobileDataEnabled(true);
    }

    private void setMobileDataEnabled(boolean z) {
        ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).setDataEnabled(this.mSubId, z);
        setChecked(z);
    }

    private void setChecked(boolean z) {
        if (this.mChecked != z) {
            this.mChecked = z;
            notifyChanged();
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(16908352);
        findViewById.setClickable(false);
        ((Checkable) findViewById).setChecked(this.mChecked);
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        if (this.mMultiSimDialog) {
            showMultiSimDialog(builder, onClickListener);
        } else {
            showDisableDialog(builder, onClickListener);
        }
    }

    private void showDisableDialog(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        builder.setTitle((CharSequence) null).setMessage((int) R.string.data_usage_disable_mobile).setPositiveButton(17039370, onClickListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
    }

    private void showMultiSimDialog(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        String str;
        SubscriptionInfo activeSubscriptionInfo = getActiveSubscriptionInfo(this.mSubId);
        SubscriptionInfo activeSubscriptionInfo2 = getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        if (activeSubscriptionInfo2 == null) {
            str = getContext().getResources().getString(R.string.sim_selection_required_pref);
        } else {
            str = SubscriptionUtil.getUniqueSubscriptionDisplayName(activeSubscriptionInfo2, getContext()).toString();
        }
        builder.setTitle((int) R.string.sim_change_data_title);
        Context context = getContext();
        Object[] objArr = new Object[2];
        objArr[0] = String.valueOf(activeSubscriptionInfo != null ? SubscriptionUtil.getUniqueSubscriptionDisplayName(activeSubscriptionInfo, getContext()) : null);
        objArr[1] = str;
        builder.setMessage((CharSequence) context.getString(R.string.sim_change_data_message, objArr));
        builder.setPositiveButton((int) R.string.okay, onClickListener);
        builder.setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null);
    }

    private void disableDataForOtherSubscriptions(int i) {
        if (getActiveSubscriptionInfo(i) != null) {
            ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).setDataEnabled(i, false);
        }
    }

    /* access modifiers changed from: protected */
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            if (this.mMultiSimDialog) {
                getProxySubscriptionManager().get().setDefaultDataSubId(this.mSubId);
                setMobileDataEnabled(true);
                disableDataForOtherSubscriptions(this.mSubId);
                return;
            }
            setMobileDataEnabled(false);
        }
    }

    public void onMobileDataEnabledChange() {
        updateChecked();
    }

    public static class CellDataState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<CellDataState> CREATOR = new Parcelable.Creator<CellDataState>() {
            public CellDataState createFromParcel(Parcel parcel) {
                return new CellDataState(parcel);
            }

            public CellDataState[] newArray(int i) {
                return new CellDataState[i];
            }
        };
        public boolean mChecked;
        public boolean mMultiSimDialog;
        public int mSubId;

        public CellDataState(Parcelable parcelable) {
            super(parcelable);
        }

        public CellDataState(Parcel parcel) {
            super(parcel);
            boolean z = true;
            this.mChecked = parcel.readByte() != 0;
            this.mMultiSimDialog = parcel.readByte() == 0 ? false : z;
            this.mSubId = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.mChecked ? (byte) 1 : 0);
            parcel.writeByte(this.mMultiSimDialog ? (byte) 1 : 0);
            parcel.writeInt(this.mSubId);
        }
    }
}
