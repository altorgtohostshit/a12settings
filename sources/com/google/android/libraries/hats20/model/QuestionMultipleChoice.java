package com.google.android.libraries.hats20.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import com.google.android.libraries.hats20.R$drawable;
import com.google.android.libraries.hats20.model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionMultipleChoice extends Question implements Question.QuestionWithSelectableAnswers {
    public static final Parcelable.Creator<QuestionMultipleChoice> CREATOR = new Parcelable.Creator<QuestionMultipleChoice>() {
        public QuestionMultipleChoice createFromParcel(Parcel parcel) {
            return new QuestionMultipleChoice(parcel);
        }

        public QuestionMultipleChoice[] newArray(int i) {
            return new QuestionMultipleChoice[i];
        }
    };
    public static final Map<Integer, Integer> READONLY_SURVEY_RATING_ICON_RESOURCE_MAP;
    private ArrayList<String> answers;
    private ArrayList<Integer> ordering;
    private String spriteName;

    public int describeContents() {
        return 0;
    }

    public int getType() {
        return 1;
    }

    static {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(0, Integer.valueOf(R$drawable.hats_smiley_5));
        arrayMap.put(1, Integer.valueOf(R$drawable.hats_smiley_4));
        arrayMap.put(2, Integer.valueOf(R$drawable.hats_smiley_3));
        arrayMap.put(3, Integer.valueOf(R$drawable.hats_smiley_2));
        arrayMap.put(4, Integer.valueOf(R$drawable.hats_smiley_1));
        READONLY_SURVEY_RATING_ICON_RESOURCE_MAP = Collections.unmodifiableMap(arrayMap);
    }

    QuestionMultipleChoice(JSONObject jSONObject) throws JSONException {
        this.answers = new ArrayList<>();
        this.ordering = new ArrayList<>();
        this.questionText = jSONObject.optString("question");
        JSONArray emptyArrayIfNull = Question.toEmptyArrayIfNull(jSONObject.optJSONArray("ordering"));
        JSONArray emptyArrayIfNull2 = Question.toEmptyArrayIfNull(jSONObject.optJSONArray("answers"));
        for (int i = 0; i < emptyArrayIfNull2.length(); i++) {
            this.answers.add(emptyArrayIfNull2.getString(i));
        }
        for (int i2 = 0; i2 < emptyArrayIfNull.length(); i2++) {
            this.ordering.add(Integer.valueOf(emptyArrayIfNull.getInt(i2)));
        }
        this.spriteName = jSONObject.optString("sprite_name");
    }

    public ArrayList<String> getAnswers() {
        return this.answers;
    }

    public ArrayList<Integer> getOrdering() {
        return this.ordering;
    }

    public String getSpriteName() {
        return this.spriteName;
    }

    public String toString() {
        String str = this.questionText;
        String valueOf = String.valueOf(this.answers);
        String valueOf2 = String.valueOf(this.ordering);
        String str2 = this.spriteName;
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 71 + valueOf.length() + valueOf2.length() + String.valueOf(str2).length());
        sb.append("QuestionMultipleChoice{questionText=");
        sb.append(str);
        sb.append(", answers=");
        sb.append(valueOf);
        sb.append(", ordering=");
        sb.append(valueOf2);
        sb.append(", spriteName=");
        sb.append(str2);
        sb.append("}");
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(this.answers);
        parcel.writeList(this.ordering);
        parcel.writeString(this.questionText);
        parcel.writeString(this.spriteName);
    }

    private QuestionMultipleChoice(Parcel parcel) {
        this.answers = new ArrayList<>();
        this.ordering = new ArrayList<>();
        parcel.readStringList(this.answers);
        parcel.readList(this.ordering, Integer.class.getClassLoader());
        this.questionText = parcel.readString();
        this.spriteName = parcel.readString();
    }
}
