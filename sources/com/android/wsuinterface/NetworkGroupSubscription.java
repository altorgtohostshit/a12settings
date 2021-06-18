package com.android.wsuinterface;

import android.app.PendingIntent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiNetworkSuggestion;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public final class NetworkGroupSubscription implements Parcelable {
    public static final Parcelable.Creator<NetworkGroupSubscription> CREATOR = new Parcelable.Creator<NetworkGroupSubscription>() {
        public NetworkGroupSubscription createFromParcel(Parcel parcel) {
            int i;
            PendingIntent pendingIntent;
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            int readInt = parcel.readInt();
            ArrayList arrayList = new ArrayList();
            parcel.readParcelableList(arrayList, PasspointConfiguration.class.getClassLoader());
            ArrayList arrayList2 = new ArrayList();
            parcel.readParcelableList(arrayList2, WifiConfiguration.class.getClassLoader());
            ArrayList arrayList3 = new ArrayList();
            parcel.readParcelableList(arrayList3, WifiNetworkSuggestion.class.getClassLoader());
            PendingIntent pendingIntent2 = null;
            if (parcel.dataAvail() > 0) {
                int readInt2 = parcel.readInt();
                pendingIntent2 = (PendingIntent) parcel.readParcelable(PendingIntent.class.getClassLoader());
                pendingIntent = (PendingIntent) parcel.readParcelable(PendingIntent.class.getClassLoader());
                i = readInt2;
            } else {
                i = -1;
                pendingIntent = null;
            }
            NetworkGroupSubscription networkGroupSubscription = new NetworkGroupSubscription(readString, readString2, readString3, readString4, arrayList3, pendingIntent2, pendingIntent);
            networkGroupSubscription.setProvisionStatus(readInt);
            networkGroupSubscription.updateProvisionedWifiConfigurations(arrayList2);
            networkGroupSubscription.updateProvisionedPasspointConfigurations(arrayList);
            int unused = networkGroupSubscription.mApiVersion = i;
            return networkGroupSubscription;
        }

        public NetworkGroupSubscription[] newArray(int i) {
            return new NetworkGroupSubscription[i];
        }
    };
    public final String helpUriString;
    /* access modifiers changed from: private */
    public int mApiVersion;
    private PendingIntent mManageSubscriptionAction;
    private int mProvisionStatus;
    private List<PasspointConfiguration> mProvisionedPasspointConfigurations;
    private List<WifiConfiguration> mProvisionedWifiConfigurations;
    private PendingIntent mSignUpAction;
    public final List<WifiNetworkSuggestion> matchingSuggestions;
    public final String subscriptionAuthenticator;
    public final String subscriptionProviderName;
    public final String uniqueIdentifier;

    public int describeContents() {
        return 0;
    }

    public int getApiVersion() {
        return this.mApiVersion;
    }

    public PendingIntent getSignUpAction() {
        return this.mSignUpAction;
    }

    public PendingIntent getManageSubscriptionAction() {
        return this.mManageSubscriptionAction;
    }

    private NetworkGroupSubscription(String str, String str2, String str3, String str4, List<WifiNetworkSuggestion> list, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        this.mProvisionedPasspointConfigurations = new ArrayList();
        this.mProvisionedWifiConfigurations = new ArrayList();
        this.mProvisionStatus = 0;
        this.mSignUpAction = null;
        this.mManageSubscriptionAction = null;
        this.mApiVersion = 2;
        this.uniqueIdentifier = str;
        this.subscriptionAuthenticator = str2;
        this.subscriptionProviderName = str3;
        this.helpUriString = str4;
        this.matchingSuggestions = list;
        this.mSignUpAction = pendingIntent;
        this.mManageSubscriptionAction = pendingIntent2;
    }

    public void updateProvisionedWifiConfigurations(List<WifiConfiguration> list) {
        this.mProvisionedWifiConfigurations = list;
    }

    public void updateProvisionedPasspointConfigurations(List<PasspointConfiguration> list) {
        this.mProvisionedPasspointConfigurations = list;
    }

    public void setProvisionStatus(int i) {
        this.mProvisionStatus = i;
    }

    public int getProvisionStatus() {
        return this.mProvisionStatus;
    }

    public List<WifiConfiguration> getProvisionedWifiConfigurations() {
        return this.mProvisionedWifiConfigurations;
    }

    public List<PasspointConfiguration> getProvisionedPasspoints() {
        return this.mProvisionedPasspointConfigurations;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NetworkGroupSubscription)) {
            return false;
        }
        return TextUtils.equals(this.uniqueIdentifier, ((NetworkGroupSubscription) obj).uniqueIdentifier);
    }

    public int hashCode() {
        return this.uniqueIdentifier.hashCode();
    }

    public String toString() {
        return "WSU - Service Identifier: " + this.uniqueIdentifier + "Account Authenticator: " + this.subscriptionAuthenticator + "Service Provider: " + this.subscriptionProviderName;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.uniqueIdentifier);
        parcel.writeString(this.subscriptionAuthenticator);
        parcel.writeString(this.subscriptionProviderName);
        parcel.writeString(this.helpUriString);
        parcel.writeInt(this.mProvisionStatus);
        parcel.writeParcelableList(this.mProvisionedPasspointConfigurations, i);
        parcel.writeParcelableList(this.mProvisionedWifiConfigurations, i);
        parcel.writeParcelableList(this.matchingSuggestions, i);
        parcel.writeInt(this.mApiVersion);
        parcel.writeParcelable(this.mSignUpAction, i);
        parcel.writeParcelable(this.mManageSubscriptionAction, i);
    }
}
