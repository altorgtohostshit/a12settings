package com.google.android.wifitrackerlib;

import android.text.TextUtils;
import java.util.Objects;

public class WsuProvider {
    public final String helpUriString;
    public final String networkGroupIdentity;
    public final String servicePackageName;
    public final String wsuProviderName;

    WsuProvider(String str, String str2, String str3, String str4) {
        this.wsuProviderName = str3;
        this.servicePackageName = str;
        this.networkGroupIdentity = str2;
        this.helpUriString = str4;
    }

    /* access modifiers changed from: package-private */
    public String getWsuIdentity() {
        return this.servicePackageName + "," + this.networkGroupIdentity;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WsuProvider)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        WsuProvider wsuProvider = (WsuProvider) obj;
        if (!TextUtils.equals(this.servicePackageName, wsuProvider.servicePackageName) || !TextUtils.equals(this.networkGroupIdentity, wsuProvider.networkGroupIdentity)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.servicePackageName, this.networkGroupIdentity});
    }

    public String toString() {
        return this.servicePackageName + ":" + this.networkGroupIdentity + ":" + this.wsuProviderName;
    }
}
