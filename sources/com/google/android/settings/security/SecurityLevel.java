package com.google.android.settings.security;

import com.android.settings.R;

enum SecurityLevel {
    INFO(R.drawable.ic_security_info),
    RECOMMENDATION(R.drawable.ic_security_recommendation),
    WARN(R.drawable.ic_security_warn);
    
    private final int mIconResId;

    private SecurityLevel(int i) {
        this.mIconResId = i;
    }

    public int getIconResId() {
        return this.mIconResId;
    }
}
