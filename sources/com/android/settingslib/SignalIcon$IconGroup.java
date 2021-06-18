package com.android.settingslib;

public class SignalIcon$IconGroup {
    public final int[] contentDesc;
    public final int discContentDesc;
    public final String name;
    public final int qsDiscState;
    public final int[][] qsIcons;
    public final int qsNullState;
    public final int sbDiscState;
    public final int[][] sbIcons;
    public final int sbNullState;

    public SignalIcon$IconGroup(String str, int[][] iArr, int[][] iArr2, int[] iArr3, int i, int i2, int i3, int i4, int i5) {
        this.name = str;
        this.sbIcons = iArr;
        this.qsIcons = iArr2;
        this.contentDesc = iArr3;
        this.sbNullState = i;
        this.qsNullState = i2;
        this.sbDiscState = i3;
        this.qsDiscState = i4;
        this.discContentDesc = i5;
    }

    public String toString() {
        return "IconGroup(" + this.name + ")";
    }
}
