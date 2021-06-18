package com.android.settings.homepage.contextualcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import com.android.settingslib.utils.ThreadUtils;
import java.util.concurrent.Callable;

public class ContextualCardFeatureProviderImpl implements ContextualCardFeatureProvider {
    private final Context mContext;

    public ContextualCardFeatureProviderImpl(Context context) {
        this.mContext = context;
    }

    public Cursor getContextualCards() {
        SQLiteDatabase readableDatabase = CardDatabaseHelper.getInstance(this.mContext).getReadableDatabase();
        long currentTimeMillis = System.currentTimeMillis() - 86400000;
        Cursor query = readableDatabase.query("cards", (String[]) null, "dismissed_timestamp < ? OR dismissed_timestamp IS NULL", new String[]{String.valueOf(currentTimeMillis)}, (String) null, (String) null, "score DESC");
        ThreadUtils.postOnBackgroundThread((Callable) new ContextualCardFeatureProviderImpl$$ExternalSyntheticLambda0(this, currentTimeMillis));
        return query;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$getContextualCards$0(long j) throws Exception {
        return Integer.valueOf(resetDismissedTime(j));
    }

    public int markCardAsDismissed(Context context, String str) {
        SQLiteDatabase writableDatabase = CardDatabaseHelper.getInstance(this.mContext).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dismissed_timestamp", Long.valueOf(System.currentTimeMillis()));
        int update = writableDatabase.update("cards", contentValues, "name=?", new String[]{str});
        context.getContentResolver().notifyChange(CardContentProvider.DELETE_CARD_URI, (ContentObserver) null);
        return update;
    }

    /* access modifiers changed from: package-private */
    public int resetDismissedTime(long j) {
        SQLiteDatabase writableDatabase = CardDatabaseHelper.getInstance(this.mContext).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("dismissed_timestamp");
        int update = writableDatabase.update("cards", contentValues, "dismissed_timestamp < ? AND dismissed_timestamp IS NOT NULL", new String[]{String.valueOf(j)});
        if (Build.IS_DEBUGGABLE) {
            Log.d("ContextualCardFeatureProvider", "Reset " + update + " records of dismissed time.");
        }
        return update;
    }
}
