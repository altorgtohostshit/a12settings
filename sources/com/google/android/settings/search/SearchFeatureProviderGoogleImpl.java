package com.google.android.settings.search;

import android.content.Context;
import com.android.settings.search.SearchFeatureProviderImpl;
import com.google.android.settings.external.SignatureVerifier;

public class SearchFeatureProviderGoogleImpl extends SearchFeatureProviderImpl {
    /* access modifiers changed from: protected */
    public boolean isSignatureAllowlisted(Context context, String str) {
        return SignatureVerifier.isPackageAllowlisted(context, str);
    }
}
