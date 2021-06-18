package com.google.android.settings.connecteddevice.dock;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class DockAsyncQueryHandler extends AsyncQueryHandler {
    private OnQueryListener mListener = null;

    public interface OnQueryListener {
        void onQueryComplete(int i, List<DockDevice> list);
    }

    public DockAsyncQueryHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    public static List<DockDevice> parseCursorToDockDevice(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                arrayList.add(new DockDevice(cursor.getString(cursor.getColumnIndex("dockId")), cursor.getString(cursor.getColumnIndex("dockName"))));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public void onQueryComplete(int i, Object obj, Cursor cursor) {
        super.onQueryComplete(i, obj, cursor);
        OnQueryListener onQueryListener = this.mListener;
        if (onQueryListener != null) {
            onQueryListener.onQueryComplete(i, parseCursorToDockDevice(cursor));
        }
    }

    public void setOnQueryListener(OnQueryListener onQueryListener) {
        this.mListener = onQueryListener;
    }
}
