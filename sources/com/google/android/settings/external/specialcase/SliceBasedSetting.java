package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBuilderUtils;
import com.android.settings.slices.SliceData;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class SliceBasedSetting implements Queryable {
    static int mapAvailability(int i) {
        if (i == 0 || i == 1) {
            return 0;
        }
        if (i != 4) {
            return i != 5 ? 2 : 1;
        }
        return 6;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        BasePreferenceController preferenceController = SliceBuilderUtils.getPreferenceController(context, sliceData);
        int isChecked = preferenceController instanceof TogglePreferenceController ? ((TogglePreferenceController) preferenceController).isChecked() : -1;
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(isChecked)).add("availability", Integer.valueOf(mapAvailability(preferenceController.getAvailabilityStatus()))).add("intent", getIntentString(context, sliceData.getKey(), sliceData.getFragmentClassName(), sliceData.getScreenTitle().toString())).add("icon", Integer.valueOf(sliceData.getIconResource()));
        return matrixCursor;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002b, code lost:
        if (r1.setChecked(r5) != false) goto L_0x002f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.database.Cursor getUpdateCursor(android.content.Context r6, com.android.settings.slices.SliceData r7, int r8) {
        /*
            r5 = this;
            com.android.settings.core.BasePreferenceController r0 = com.android.settings.slices.SliceBuilderUtils.getPreferenceController(r6, r7)
            boolean r1 = r0 instanceof com.android.settings.core.TogglePreferenceController
            if (r1 != 0) goto L_0x0010
            android.database.MatrixCursor r5 = new android.database.MatrixCursor
            java.lang.String[] r6 = com.google.android.settings.external.ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS
            r5.<init>(r6)
            return r5
        L_0x0010:
            r1 = r0
            com.android.settings.core.TogglePreferenceController r1 = (com.android.settings.core.TogglePreferenceController) r1
            boolean r2 = r1.isChecked()
            int r0 = r0.getAvailabilityStatus()
            boolean r5 = r5.shouldChangeValue(r0, r2, r8)
            r3 = 0
            if (r5 == 0) goto L_0x002e
            r5 = 1
            if (r8 != r5) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r5 = r3
        L_0x0027:
            boolean r5 = r1.setChecked(r5)
            if (r5 == 0) goto L_0x002e
            goto L_0x002f
        L_0x002e:
            r8 = r2
        L_0x002f:
            android.database.MatrixCursor r5 = new android.database.MatrixCursor
            java.lang.String[] r1 = com.google.android.settings.external.ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS
            r5.<init>(r1)
            android.database.MatrixCursor$RowBuilder r1 = r5.newRow()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            java.lang.String r4 = "newValue"
            android.database.MatrixCursor$RowBuilder r8 = r1.add(r4, r8)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
            java.lang.String r2 = "existing_value"
            android.database.MatrixCursor$RowBuilder r8 = r8.add(r2, r1)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.String r1 = "availability"
            android.database.MatrixCursor$RowBuilder r8 = r8.add(r1, r0)
            android.content.Intent r6 = com.android.settings.slices.SliceBuilderUtils.getContentIntent(r6, r7)
            java.lang.String r6 = r6.toUri(r3)
            java.lang.String r0 = "intent"
            android.database.MatrixCursor$RowBuilder r6 = r8.add(r0, r6)
            int r7 = r7.getIconResource()
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            java.lang.String r8 = "icon"
            r6.add(r8, r7)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.settings.external.specialcase.SliceBasedSetting.getUpdateCursor(android.content.Context, com.android.settings.slices.SliceData, int):android.database.Cursor");
    }

    private static String getIntentString(Context context, String str, String str2, String str3) {
        return SliceBuilderUtils.buildSearchResultPageIntent(context, str2, str, str3, 1033).toUri(0);
    }
}
