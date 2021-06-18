package com.google.android.settings.biometrics.face.anim.curve;

public class DirectionIndicationHelper {
    private static final int[][] BUCKET_PRIORITY = {new int[]{12, 0}, new int[]{2, 0}, new int[]{7, 0}, new int[]{3, 23}, new int[]{4, 45}, new int[]{8, 45}, new int[]{9, 68}, new int[]{14, 90}, new int[]{13, 90}, new int[]{19, 113}, new int[]{24, 135}, new int[]{18, 135}, new int[]{23, 158}, new int[]{22, 180}, new int[]{17, 180}, new int[]{21, 203}, new int[]{20, 225}, new int[]{16, 225}, new int[]{15, 248}, new int[]{10, 270}, new int[]{11, 270}, new int[]{5, 293}, new int[]{0, 315}, new int[]{6, 315}, new int[]{1, 338}};

    public int getNoProgressPulseAngle(boolean[] zArr) {
        int i = 1;
        while (true) {
            int[][] iArr = BUCKET_PRIORITY;
            if (i >= iArr.length) {
                return 0;
            }
            if (!zArr[iArr[i][0]]) {
                return iArr[i][1];
            }
            i++;
        }
    }

    public int getNoProgressBucket(boolean[] zArr) {
        int i = 1;
        while (true) {
            int[][] iArr = BUCKET_PRIORITY;
            if (i >= iArr.length) {
                return 0;
            }
            if (!zArr[iArr[i][0]]) {
                return iArr[i][0];
            }
            i++;
        }
    }
}
