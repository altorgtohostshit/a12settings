package com.google.android.libraries.hats20.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

public class QuestionMetrics implements Parcelable {
    public static final Parcelable.Creator<QuestionMetrics> CREATOR = new Parcelable.Creator<QuestionMetrics>() {
        public QuestionMetrics createFromParcel(Parcel parcel) {
            return new QuestionMetrics(parcel);
        }

        public QuestionMetrics[] newArray(int i) {
            return new QuestionMetrics[i];
        }
    };
    private long delayEndMs;
    private long delayStartMs;

    public int describeContents() {
        return 0;
    }

    QuestionMetrics() {
        this.delayStartMs = -1;
        this.delayEndMs = -1;
    }

    private QuestionMetrics(Parcel parcel) {
        this.delayStartMs = parcel.readLong();
        this.delayEndMs = parcel.readLong();
    }

    /* access modifiers changed from: package-private */
    public void markAsShown() {
        if (!isShown()) {
            this.delayStartMs = SystemClock.elapsedRealtime();
        }
    }

    /* access modifiers changed from: package-private */
    public void markAsAnswered() {
        if (!isShown()) {
            Log.e("HatsLibQuestionMetrics", "Question was marked as answered but was never marked as shown.");
        } else if (isAnswered()) {
            Log.d("HatsLibQuestionMetrics", "Question was already marked as answered.");
        } else {
            this.delayEndMs = SystemClock.elapsedRealtime();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isShown() {
        return this.delayStartMs >= 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isAnswered() {
        return this.delayEndMs >= 0;
    }

    /* access modifiers changed from: package-private */
    public long getDelayMs() {
        if (isAnswered()) {
            return this.delayEndMs - this.delayStartMs;
        }
        return -1;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.delayStartMs);
        parcel.writeLong(this.delayEndMs);
    }
}
