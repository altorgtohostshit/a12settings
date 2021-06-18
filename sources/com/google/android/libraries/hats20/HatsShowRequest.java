package com.google.android.libraries.hats20;

import android.app.Activity;
import android.util.Log;
import androidx.appcompat.R$styleable;
import java.util.Objects;

public class HatsShowRequest {
    private final boolean bottomSheet;
    private final Activity clientActivity;
    private final Integer maxPromptWidth;
    private final int parentResId;
    private final Integer requestCode;
    private final String siteId;

    private HatsShowRequest(Builder builder) {
        this.clientActivity = builder.clientActivity;
        this.siteId = builder.siteId;
        this.requestCode = builder.requestCode;
        this.parentResId = builder.parentResId;
        this.maxPromptWidth = builder.maxPromptWidth;
        this.bottomSheet = builder.bottomSheet;
    }

    public Activity getClientActivity() {
        return this.clientActivity;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public Integer getRequestCode() {
        return this.requestCode;
    }

    public int getParentResId() {
        return this.parentResId;
    }

    public Integer getMaxPromptWidth() {
        return this.maxPromptWidth;
    }

    public boolean isBottomSheet() {
        return this.bottomSheet;
    }

    public String toString() {
        String valueOf = String.valueOf(this.clientActivity.getLocalClassName());
        String str = this.siteId;
        String valueOf2 = String.valueOf(this.requestCode);
        int i = this.parentResId;
        String valueOf3 = String.valueOf(this.maxPromptWidth);
        boolean z = this.bottomSheet;
        StringBuilder sb = new StringBuilder(valueOf.length() + R$styleable.AppCompatTheme_windowActionBarOverlay + String.valueOf(str).length() + valueOf2.length() + valueOf3.length());
        sb.append("HatsShowRequest{clientActivity=");
        sb.append(valueOf);
        sb.append(", siteId='");
        sb.append(str);
        sb.append("'");
        sb.append(", requestCode=");
        sb.append(valueOf2);
        sb.append(", parentResId=");
        sb.append(i);
        sb.append(", maxPromptWidth=");
        sb.append(valueOf3);
        sb.append(", bottomSheet=");
        sb.append(z);
        sb.append("}");
        return sb.toString();
    }

    public static Builder builder(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean bottomSheet;
        /* access modifiers changed from: private */
        public final Activity clientActivity;
        /* access modifiers changed from: private */
        public Integer maxPromptWidth;
        /* access modifiers changed from: private */
        public int parentResId;
        /* access modifiers changed from: private */
        public Integer requestCode;
        /* access modifiers changed from: private */
        public String siteId;

        Builder(Activity activity) {
            if (activity != null) {
                this.clientActivity = activity;
                return;
            }
            throw new IllegalArgumentException("Client activity is missing.");
        }

        public Builder forSiteId(String str) {
            Objects.requireNonNull(str, "Site ID cannot be set to null.");
            if (this.siteId == null) {
                this.siteId = str;
                return this;
            }
            throw new UnsupportedOperationException("Currently don't support multiple site IDs.");
        }

        public HatsShowRequest build() {
            if (this.siteId == null) {
                Log.d("HatsLibShowRequest", "Site ID was not set, no survey will be shown.");
                this.siteId = "-1";
            }
            return new HatsShowRequest(this);
        }
    }
}
