package com.google.android.libraries.hats20.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import com.google.android.libraries.hats20.R$drawable;
import java.util.Collections;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionRating extends Question {
    public static final Parcelable.Creator<QuestionRating> CREATOR = new Parcelable.Creator<QuestionRating>() {
        public QuestionRating createFromParcel(Parcel parcel) {
            return new QuestionRating(parcel);
        }

        public QuestionRating[] newArray(int i) {
            return new QuestionRating[i];
        }
    };
    public static final Map<Integer, Integer> READONLY_SURVEY_RATING_ICON_RESOURCE_MAP;
    private final String highValueText;
    private final String lowValueText;
    private final int numIcons;
    private final Sprite sprite;

    public enum Sprite {
        STARS,
        SMILEYS
    }

    public int describeContents() {
        return 0;
    }

    public int getType() {
        return 4;
    }

    static {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(0, Integer.valueOf(R$drawable.hats_smiley_1));
        arrayMap.put(1, Integer.valueOf(R$drawable.hats_smiley_2));
        arrayMap.put(2, Integer.valueOf(R$drawable.hats_smiley_3));
        arrayMap.put(3, Integer.valueOf(R$drawable.hats_smiley_4));
        arrayMap.put(4, Integer.valueOf(R$drawable.hats_smiley_5));
        READONLY_SURVEY_RATING_ICON_RESOURCE_MAP = Collections.unmodifiableMap(arrayMap);
    }

    QuestionRating(JSONObject jSONObject) throws JSONException {
        this.questionText = jSONObject.optString("question");
        this.lowValueText = jSONObject.optString("low_value");
        this.highValueText = jSONObject.optString("high_value");
        int i = jSONObject.getInt("num_stars");
        this.numIcons = i;
        this.sprite = i == 5 ? Sprite.SMILEYS : Sprite.STARS;
    }

    public String getLowValueText() {
        return this.lowValueText;
    }

    public String getHighValueText() {
        return this.highValueText;
    }

    public int getNumIcons() {
        return this.numIcons;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public String toString() {
        String str = this.questionText;
        String str2 = this.lowValueText;
        String str3 = this.highValueText;
        int i = this.numIcons;
        String valueOf = String.valueOf(this.sprite);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 97 + String.valueOf(str2).length() + String.valueOf(str3).length() + valueOf.length());
        sb.append("QuestionRating{questionText='");
        sb.append(str);
        sb.append("'");
        sb.append(", lowValueText='");
        sb.append(str2);
        sb.append("'");
        sb.append(", highValueText='");
        sb.append(str3);
        sb.append("'");
        sb.append(", numIcons=");
        sb.append(i);
        sb.append(", sprite=");
        sb.append(valueOf);
        sb.append("}");
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.lowValueText);
        parcel.writeString(this.highValueText);
        parcel.writeInt(this.numIcons);
        parcel.writeString(this.questionText);
        parcel.writeSerializable(this.sprite);
    }

    private QuestionRating(Parcel parcel) {
        this.lowValueText = parcel.readString();
        this.highValueText = parcel.readString();
        this.numIcons = parcel.readInt();
        this.questionText = parcel.readString();
        this.sprite = (Sprite) parcel.readSerializable();
    }
}
