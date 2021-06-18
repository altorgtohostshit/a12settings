package com.google.android.settings.external;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import androidx.fragment.app.Fragment;
import com.android.settings.slices.SliceBuilderUtils;
import com.android.settings.slices.SliceData;

public interface Queryable {
    boolean shouldChangeValue(int i, int i2, int i3) {
        return i == 0 && i2 != i3;
    }

    Cursor getUpdateCursor(Context context, SliceData sliceData, String str) {
        try {
            return getUpdateCursor(context, sliceData, Integer.valueOf(str).intValue());
        } catch (NumberFormatException unused) {
            return new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        }
    }

    String getIntentString(Context context, String str, Class<? extends Fragment> cls, String str2) {
        return SliceBuilderUtils.buildSearchResultPageIntent(context, cls.getName(), str, str2, 1033).toUri(0);
    }

    Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        throw new UnsupportedOperationException("Method not supported");
    }
}
