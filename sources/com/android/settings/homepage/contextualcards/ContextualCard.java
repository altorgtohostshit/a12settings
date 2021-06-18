package com.android.settings.homepage.contextualcards;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import androidx.slice.Slice;
import com.android.settings.R;

public class ContextualCard {
    private final long mAppVersion;
    private final Builder mBuilder;
    private final int mCardType;
    private final int mCategory;
    private final boolean mHasInlineAction;
    private final Drawable mIconDrawable;
    private final boolean mIsLargeCard;
    private final boolean mIsPendingDismiss;
    private final String mName;
    private final String mPackageName;
    private final double mRankingScore;
    private final Slice mSlice;
    private final String mSliceUri;
    private final String mSummaryText;
    private final String mTitleText;
    private final int mViewType;

    private int getViewTypeByCardType(int i) {
        if (i == 1) {
            return R.layout.contextual_slice_full_tile;
        }
        return 0;
    }

    public String getName() {
        return this.mName;
    }

    public int getCardType() {
        return this.mCardType;
    }

    public double getRankingScore() {
        return this.mRankingScore;
    }

    public String getTextSliceUri() {
        return this.mSliceUri;
    }

    public Uri getSliceUri() {
        return Uri.parse(this.mSliceUri);
    }

    public int getCategory() {
        return this.mCategory;
    }

    public String getTitleText() {
        return this.mTitleText;
    }

    public String getSummaryText() {
        return this.mSummaryText;
    }

    public Drawable getIconDrawable() {
        return this.mIconDrawable;
    }

    public boolean isLargeCard() {
        return this.mIsLargeCard;
    }

    public int getViewType() {
        return this.mViewType;
    }

    public boolean isPendingDismiss() {
        return this.mIsPendingDismiss;
    }

    public boolean hasInlineAction() {
        return this.mHasInlineAction;
    }

    public Slice getSlice() {
        return this.mSlice;
    }

    public Builder mutate() {
        return this.mBuilder;
    }

    public ContextualCard(Builder builder) {
        this.mBuilder = builder;
        this.mName = builder.mName;
        this.mCardType = builder.mCardType;
        this.mRankingScore = builder.mRankingScore;
        this.mSliceUri = builder.mSliceUri;
        this.mCategory = builder.mCategory;
        this.mPackageName = builder.mPackageName;
        this.mAppVersion = builder.mAppVersion;
        this.mTitleText = builder.mTitleText;
        this.mSummaryText = builder.mSummaryText;
        this.mIconDrawable = builder.mIconDrawable;
        this.mIsLargeCard = builder.mIsLargeCard;
        this.mViewType = builder.mViewType;
        this.mIsPendingDismiss = builder.mIsPendingDismiss;
        this.mHasInlineAction = builder.mHasInlineAction;
        this.mSlice = builder.mSlice;
    }

    ContextualCard(Cursor cursor) {
        Builder builder = new Builder();
        this.mBuilder = builder;
        String string = cursor.getString(cursor.getColumnIndex("name"));
        this.mName = string;
        builder.setName(string);
        int i = cursor.getInt(cursor.getColumnIndex("type"));
        this.mCardType = i;
        builder.setCardType(i);
        double d = cursor.getDouble(cursor.getColumnIndex("score"));
        this.mRankingScore = d;
        builder.setRankingScore(d);
        String string2 = cursor.getString(cursor.getColumnIndex("slice_uri"));
        this.mSliceUri = string2;
        builder.setSliceUri(Uri.parse(string2));
        int i2 = cursor.getInt(cursor.getColumnIndex("category"));
        this.mCategory = i2;
        builder.setCategory(i2);
        String string3 = cursor.getString(cursor.getColumnIndex("package_name"));
        this.mPackageName = string3;
        builder.setPackageName(string3);
        long j = cursor.getLong(cursor.getColumnIndex("app_version"));
        this.mAppVersion = j;
        builder.setAppVersion(j);
        this.mTitleText = "";
        builder.setTitleText("");
        this.mSummaryText = "";
        builder.setTitleText("");
        this.mIsLargeCard = false;
        builder.setIsLargeCard(false);
        this.mIconDrawable = null;
        builder.setIconDrawable((Drawable) null);
        int viewTypeByCardType = getViewTypeByCardType(i);
        this.mViewType = viewTypeByCardType;
        builder.setViewType(viewTypeByCardType);
        this.mIsPendingDismiss = false;
        builder.setIsPendingDismiss(false);
        this.mHasInlineAction = false;
        builder.setHasInlineAction(false);
        this.mSlice = null;
        builder.setSlice((Slice) null);
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ContextualCard)) {
            return false;
        }
        return TextUtils.equals(this.mName, ((ContextualCard) obj).mName);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public long mAppVersion;
        /* access modifiers changed from: private */
        public int mCardType;
        /* access modifiers changed from: private */
        public int mCategory;
        /* access modifiers changed from: private */
        public boolean mHasInlineAction;
        /* access modifiers changed from: private */
        public Drawable mIconDrawable;
        /* access modifiers changed from: private */
        public boolean mIsLargeCard;
        /* access modifiers changed from: private */
        public boolean mIsPendingDismiss;
        /* access modifiers changed from: private */
        public String mName;
        /* access modifiers changed from: private */
        public String mPackageName;
        /* access modifiers changed from: private */
        public double mRankingScore;
        /* access modifiers changed from: private */
        public Slice mSlice;
        /* access modifiers changed from: private */
        public String mSliceUri;
        /* access modifiers changed from: private */
        public String mSummaryText;
        /* access modifiers changed from: private */
        public String mTitleText;
        /* access modifiers changed from: private */
        public int mViewType;

        public Builder setName(String str) {
            this.mName = str;
            return this;
        }

        public Builder setCardType(int i) {
            this.mCardType = i;
            return this;
        }

        public Builder setRankingScore(double d) {
            this.mRankingScore = d;
            return this;
        }

        public Builder setSliceUri(Uri uri) {
            this.mSliceUri = uri.toString();
            return this;
        }

        public Builder setCategory(int i) {
            this.mCategory = i;
            return this;
        }

        public Builder setPackageName(String str) {
            this.mPackageName = str;
            return this;
        }

        public Builder setAppVersion(long j) {
            this.mAppVersion = j;
            return this;
        }

        public Builder setTitleText(String str) {
            this.mTitleText = str;
            return this;
        }

        public Builder setSummaryText(String str) {
            this.mSummaryText = str;
            return this;
        }

        public Builder setIconDrawable(Drawable drawable) {
            this.mIconDrawable = drawable;
            return this;
        }

        public Builder setIsLargeCard(boolean z) {
            this.mIsLargeCard = z;
            return this;
        }

        public Builder setViewType(int i) {
            this.mViewType = i;
            return this;
        }

        public Builder setIsPendingDismiss(boolean z) {
            this.mIsPendingDismiss = z;
            return this;
        }

        public Builder setHasInlineAction(boolean z) {
            this.mHasInlineAction = z;
            return this;
        }

        public Builder setSlice(Slice slice) {
            this.mSlice = slice;
            return this;
        }

        public ContextualCard build() {
            return new ContextualCard(this);
        }
    }
}
