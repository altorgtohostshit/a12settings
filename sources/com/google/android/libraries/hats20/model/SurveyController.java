package com.google.android.libraries.hats20.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.libraries.hats20.model.Question;
import com.google.android.libraries.hats20.model.QuestionRating;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class SurveyController implements Parcelable {
    public static final Parcelable.Creator<SurveyController> CREATOR = new Parcelable.Creator<SurveyController>() {
        public SurveyController createFromParcel(Parcel parcel) {
            return new SurveyController(parcel);
        }

        public SurveyController[] newArray(int i) {
            return new SurveyController[i];
        }
    };
    private String answerUrl;
    private String promptMessage;
    private String promptParams;
    private Question[] questions;
    private boolean showInvitation;
    private String thankYouMessage;

    public int describeContents() {
        return 0;
    }

    private SurveyController() {
        this.showInvitation = true;
    }

    public Question[] getQuestions() {
        return this.questions;
    }

    public boolean showInvitation() {
        return this.showInvitation;
    }

    public boolean shouldIncludeSurveyControls() {
        Question[] questionArr = this.questions;
        if (questionArr.length == 1 && questionArr[0].getType() == 4 && ((QuestionRating) this.questions[0]).getSprite() == QuestionRating.Sprite.SMILEYS) {
            return false;
        }
        return true;
    }

    public String getPromptMessage() {
        return this.promptMessage;
    }

    public String getThankYouMessage() {
        return this.thankYouMessage;
    }

    public String getPromptParams() {
        return this.promptParams;
    }

    public String getAnswerUrl() {
        return this.answerUrl;
    }

    public static SurveyController initWithSurveyFromJson(String str, Resources resources) throws JSONException, MalformedSurveyException {
        JSONObject jSONObject = new JSONObject(str).getJSONObject("params");
        SurveyController surveyController = new SurveyController();
        retrieveTagDataFromJson(surveyController, jSONObject.getJSONArray("tags"), resources);
        surveyController.questions = Question.getQuestionsFromSurveyDefinition(jSONObject);
        surveyController.promptParams = jSONObject.optString("promptParams");
        surveyController.answerUrl = jSONObject.optString("answerUrl");
        assertSurveyIsValid(surveyController);
        return surveyController;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006f, code lost:
        if (r5.equals("thankYouMessage") == false) goto L_0x003b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void retrieveTagDataFromJson(com.google.android.libraries.hats20.model.SurveyController r9, org.json.JSONArray r10, android.content.res.Resources r11) throws org.json.JSONException {
        /*
            r0 = 0
            r1 = r0
        L_0x0002:
            int r2 = r10.length()
            java.lang.String r3 = "HatsLibSurveyController"
            r4 = 1
            if (r1 >= r2) goto L_0x00ac
            java.lang.String r2 = r10.getString(r1)
            java.lang.String r5 = "="
            java.lang.String[] r2 = r2.split(r5)
            int r5 = r2.length
            r6 = 2
            if (r5 == r6) goto L_0x002c
            java.lang.Object[] r2 = new java.lang.Object[r4]
            java.lang.String r4 = r10.getString(r1)
            r2[r0] = r4
            java.lang.String r4 = "Tag couldn't be split: %s"
            java.lang.String r2 = java.lang.String.format(r4, r2)
            android.util.Log.e(r3, r2)
            goto L_0x00a8
        L_0x002c:
            r5 = r2[r0]
            r2 = r2[r4]
            r5.hashCode()
            r7 = -1
            int r8 = r5.hashCode()
            switch(r8) {
                case -1765207296: goto L_0x007d;
                case -1505536394: goto L_0x0072;
                case -1336354446: goto L_0x0069;
                case -1268779017: goto L_0x005e;
                case -1224386186: goto L_0x0053;
                case -1179592925: goto L_0x0048;
                case -453401085: goto L_0x003d;
                default: goto L_0x003b;
            }
        L_0x003b:
            r6 = r7
            goto L_0x0087
        L_0x003d:
            java.lang.String r6 = "promptMessage"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x0046
            goto L_0x003b
        L_0x0046:
            r6 = 6
            goto L_0x0087
        L_0x0048:
            java.lang.String r6 = "hatsClient"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x0051
            goto L_0x003b
        L_0x0051:
            r6 = 5
            goto L_0x0087
        L_0x0053:
            java.lang.String r6 = "hats20"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x005c
            goto L_0x003b
        L_0x005c:
            r6 = 4
            goto L_0x0087
        L_0x005e:
            java.lang.String r6 = "format"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x0067
            goto L_0x003b
        L_0x0067:
            r6 = 3
            goto L_0x0087
        L_0x0069:
            java.lang.String r8 = "thankYouMessage"
            boolean r8 = r5.equals(r8)
            if (r8 != 0) goto L_0x0087
            goto L_0x003b
        L_0x0072:
            java.lang.String r6 = "showInvitation"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x007b
            goto L_0x003b
        L_0x007b:
            r6 = r4
            goto L_0x0087
        L_0x007d:
            java.lang.String r6 = "hatsNoRateLimiting"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x0086
            goto L_0x003b
        L_0x0086:
            r6 = r0
        L_0x0087:
            switch(r6) {
                case 0: goto L_0x00a8;
                case 1: goto L_0x009e;
                case 2: goto L_0x009b;
                case 3: goto L_0x00a8;
                case 4: goto L_0x00a8;
                case 5: goto L_0x00a8;
                case 6: goto L_0x0098;
                default: goto L_0x008a;
            }
        L_0x008a:
            java.lang.Object[] r2 = new java.lang.Object[r4]
            r2[r0] = r5
            java.lang.String r4 = "Skipping unknown tag '%s'"
            java.lang.String r2 = java.lang.String.format(r4, r2)
            android.util.Log.w(r3, r2)
            goto L_0x00a8
        L_0x0098:
            r9.promptMessage = r2
            goto L_0x00a8
        L_0x009b:
            r9.thankYouMessage = r2
            goto L_0x00a8
        L_0x009e:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
            boolean r2 = r2.booleanValue()
            r9.showInvitation = r2
        L_0x00a8:
            int r1 = r1 + 1
            goto L_0x0002
        L_0x00ac:
            boolean r10 = r9.showInvitation
            if (r10 != 0) goto L_0x00c7
            java.lang.String r10 = r9.promptMessage
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            if (r10 != 0) goto L_0x00c7
            java.lang.Object[] r10 = new java.lang.Object[r4]
            java.lang.String r1 = r9.promptMessage
            r10[r0] = r1
            java.lang.String r0 = "Survey is promptless but a prompt message was parsed: %s"
            java.lang.String r10 = java.lang.String.format(r0, r10)
            android.util.Log.w(r3, r10)
        L_0x00c7:
            boolean r10 = r9.showInvitation
            if (r10 == 0) goto L_0x00db
            java.lang.String r10 = r9.promptMessage
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            if (r10 == 0) goto L_0x00db
            int r10 = com.google.android.libraries.hats20.R$string.hats_lib_default_prompt_title
            java.lang.String r10 = r11.getString(r10)
            r9.promptMessage = r10
        L_0x00db:
            java.lang.String r10 = r9.thankYouMessage
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            if (r10 == 0) goto L_0x00eb
            int r10 = com.google.android.libraries.hats20.R$string.hats_lib_default_thank_you
            java.lang.String r10 = r11.getString(r10)
            r9.thankYouMessage = r10
        L_0x00eb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.libraries.hats20.model.SurveyController.retrieveTagDataFromJson(com.google.android.libraries.hats20.model.SurveyController, org.json.JSONArray, android.content.res.Resources):void");
    }

    private static void assertSurveyIsValid(SurveyController surveyController) throws MalformedSurveyException {
        if (surveyController.getQuestions().length == 0) {
            throw new MalformedSurveyException("Survey has no questions.");
        } else if (TextUtils.isEmpty(surveyController.getAnswerUrl())) {
            throw new MalformedSurveyException("Survey did not have an AnswerUrl, this is a GCS issue.");
        } else if (!TextUtils.isEmpty(surveyController.getPromptParams())) {
            int i = 0;
            while (i < surveyController.getQuestions().length) {
                Question question = surveyController.questions[i];
                if (!TextUtils.isEmpty(question.questionText)) {
                    if (question instanceof Question.QuestionWithSelectableAnswers) {
                        Question.QuestionWithSelectableAnswers questionWithSelectableAnswers = (Question.QuestionWithSelectableAnswers) question;
                        List<String> answers = questionWithSelectableAnswers.getAnswers();
                        List<Integer> ordering = questionWithSelectableAnswers.getOrdering();
                        if (answers.isEmpty()) {
                            StringBuilder sb = new StringBuilder(42);
                            sb.append("Question #");
                            sb.append(i + 1);
                            sb.append(" was missing answers.");
                            throw new MalformedSurveyException(sb.toString());
                        } else if (ordering.isEmpty()) {
                            StringBuilder sb2 = new StringBuilder(74);
                            sb2.append("Question #");
                            sb2.append(i + 1);
                            sb2.append(" was missing an ordering, this likely is a GCS issue.");
                            throw new MalformedSurveyException(sb2.toString());
                        }
                    }
                    if (question.getType() == 4) {
                        QuestionRating questionRating = (QuestionRating) question;
                        if (TextUtils.isEmpty(questionRating.getLowValueText()) || TextUtils.isEmpty(questionRating.getHighValueText())) {
                            throw new MalformedSurveyException("A rating question was missing its high/low text.");
                        }
                        QuestionRating.Sprite sprite = questionRating.getSprite();
                        QuestionRating.Sprite sprite2 = QuestionRating.Sprite.SMILEYS;
                        if (sprite != sprite2 || questionRating.getNumIcons() == 5) {
                            QuestionRating.Sprite sprite3 = questionRating.getSprite();
                            if (!(sprite3 == QuestionRating.Sprite.STARS || sprite3 == sprite2)) {
                                String valueOf = String.valueOf(sprite3);
                                StringBuilder sb3 = new StringBuilder(valueOf.length() + 40);
                                sb3.append("Rating question has unsupported sprite: ");
                                sb3.append(valueOf);
                                throw new MalformedSurveyException(sb3.toString());
                            }
                        } else {
                            throw new MalformedSurveyException("Smiley surveys must have 5 options.");
                        }
                    }
                    i++;
                } else {
                    StringBuilder sb4 = new StringBuilder(43);
                    sb4.append("Question #");
                    sb4.append(i + 1);
                    sb4.append(" had no question text.");
                    throw new MalformedSurveyException(sb4.toString());
                }
            }
        } else {
            throw new MalformedSurveyException("Survey did not have prompt params, this is a GCS issue.");
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.showInvitation ? (byte) 1 : 0);
        parcel.writeInt(this.questions.length);
        for (Question writeParcelable : this.questions) {
            parcel.writeParcelable(writeParcelable, i);
        }
        parcel.writeString(this.promptMessage);
        parcel.writeString(this.thankYouMessage);
        parcel.writeString(this.promptParams);
        parcel.writeString(this.answerUrl);
    }

    private SurveyController(Parcel parcel) {
        boolean z = true;
        this.showInvitation = true;
        this.showInvitation = parcel.readByte() == 0 ? false : z;
        int readInt = parcel.readInt();
        this.questions = new Question[readInt];
        for (int i = 0; i < readInt; i++) {
            this.questions[i] = (Question) parcel.readParcelable(Question.class.getClassLoader());
        }
        this.promptMessage = parcel.readString();
        this.thankYouMessage = parcel.readString();
        this.promptParams = parcel.readString();
        this.answerUrl = parcel.readString();
    }

    public static class MalformedSurveyException extends Exception {
        public MalformedSurveyException(String str) {
            super(str);
        }
    }
}
