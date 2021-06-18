package com.android.settings.biometrics.face;

import android.content.Context;

public class FaceFeatureProviderImpl implements FaceFeatureProvider {
    public boolean isAttentionSupported(Context context) {
        return true;
    }
}
