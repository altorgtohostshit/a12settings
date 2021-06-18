package com.android.settings.slices;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.android.settings.overlay.FeatureFactory;
import java.util.List;

class SlicesIndexer implements Runnable {
    private Context mContext;
    private SlicesDatabaseHelper mHelper;

    public SlicesIndexer(Context context) {
        this.mContext = context;
        this.mHelper = SlicesDatabaseHelper.getInstance(context);
    }

    public void run() {
        indexSliceData();
    }

    /* access modifiers changed from: protected */
    public void indexSliceData() {
        if (this.mHelper.isSliceDataIndexed()) {
            Log.d("SlicesIndexer", "Slices already indexed - returning.");
            return;
        }
        SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
        long currentTimeMillis = System.currentTimeMillis();
        writableDatabase.beginTransaction();
        try {
            this.mHelper.reconstruct(writableDatabase);
            insertSliceData(writableDatabase, getSliceData());
            this.mHelper.setIndexedState();
            Log.d("SlicesIndexer", "Indexing slices database took: " + (System.currentTimeMillis() - currentTimeMillis));
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    /* access modifiers changed from: package-private */
    public List<SliceData> getSliceData() {
        return FeatureFactory.getFactory(this.mContext).getSlicesFeatureProvider().getSliceDataConverter(this.mContext).getSliceData();
    }

    /* access modifiers changed from: package-private */
    public void insertSliceData(SQLiteDatabase sQLiteDatabase, List<SliceData> list) {
        for (SliceData next : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("key", next.getKey());
            contentValues.put("slice_uri", next.getUri().toSafeString());
            contentValues.put("title", next.getTitle());
            contentValues.put("summary", next.getSummary());
            CharSequence screenTitle = next.getScreenTitle();
            if (screenTitle != null) {
                contentValues.put("screentitle", screenTitle.toString());
            }
            contentValues.put("keywords", next.getKeywords());
            contentValues.put("icon", Integer.valueOf(next.getIconResource()));
            contentValues.put("fragment", next.getFragmentClassName());
            contentValues.put("controller", next.getPreferenceController());
            contentValues.put("slice_type", Integer.valueOf(next.getSliceType()));
            contentValues.put("unavailable_slice_subtitle", next.getUnavailableSliceSubtitle());
            contentValues.put("public_slice", Boolean.valueOf(next.isPublicSlice()));
            sQLiteDatabase.replaceOrThrow("slices_index", (String) null, contentValues);
        }
    }
}
