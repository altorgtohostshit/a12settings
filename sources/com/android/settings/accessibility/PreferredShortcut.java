package com.android.settings.accessibility;

import android.text.TextUtils;
import com.google.common.base.Objects;

public class PreferredShortcut {
    private static final TextUtils.SimpleStringSplitter sStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
    private String mComponentName;
    private int mType;

    public static PreferredShortcut fromString(String str) {
        TextUtils.SimpleStringSplitter simpleStringSplitter = sStringColonSplitter;
        simpleStringSplitter.setString(str);
        if (simpleStringSplitter.hasNext()) {
            return new PreferredShortcut(simpleStringSplitter.next(), Integer.parseInt(simpleStringSplitter.next()));
        }
        throw new IllegalArgumentException("Invalid PreferredShortcut string: " + str);
    }

    public PreferredShortcut(String str, int i) {
        this.mComponentName = str;
        this.mType = i;
    }

    public String getComponentName() {
        return this.mComponentName;
    }

    public int getType() {
        return this.mType;
    }

    public String toString() {
        return this.mComponentName + ':' + this.mType;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || PreferredShortcut.class != obj.getClass()) {
            return false;
        }
        PreferredShortcut preferredShortcut = (PreferredShortcut) obj;
        if (this.mType != preferredShortcut.mType || !Objects.equal(this.mComponentName, preferredShortcut.mComponentName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hashCode(this.mComponentName, Integer.valueOf(this.mType));
    }
}
