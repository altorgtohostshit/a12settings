package com.google.android.libraries.hats20.view;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.libraries.hats20.R$id;
import com.google.android.libraries.hats20.R$layout;
import com.google.android.libraries.hats20.SurveyPromptActivity;
import com.google.android.libraries.hats20.answer.QuestionResponse;
import com.google.android.libraries.hats20.model.Question;
import com.google.android.libraries.hats20.model.QuestionMultipleChoice;
import com.google.android.libraries.hats20.view.FragmentViewDelegate;
import java.util.ArrayList;

public class MultipleChoiceFragment extends ScrollableAnswerFragment {
    /* access modifiers changed from: private */
    public ArrayList<String> answers;
    private FragmentViewDelegate fragmentViewDelegate = new FragmentViewDelegate();
    private boolean hasSmileys = false;
    private ArrayList<Integer> ordering;
    /* access modifiers changed from: private */
    public QuestionMetrics questionMetrics;
    private String questionText;
    /* access modifiers changed from: private */
    public String selectedResponse;

    public boolean isResponseSatisfactory() {
        return false;
    }

    public static MultipleChoiceFragment newInstance(Question question) {
        MultipleChoiceFragment multipleChoiceFragment = new MultipleChoiceFragment();
        QuestionMultipleChoice questionMultipleChoice = (QuestionMultipleChoice) question;
        String spriteName = questionMultipleChoice.getSpriteName();
        boolean z = false;
        boolean z2 = spriteName != null && spriteName.equals("smileys");
        if (!z2 || questionMultipleChoice.getAnswers().size() == 5) {
            z = z2;
        } else {
            Log.e("HatsLibMultiChoiceFrag", "Multiple choice with smileys survey must have exactly five answers.");
        }
        Bundle bundle = new Bundle();
        bundle.putString("QuestionText", question.getQuestionText());
        QuestionMultipleChoice questionMultipleChoice2 = (QuestionMultipleChoice) question;
        bundle.putStringArrayList("AnswersAsArray", questionMultipleChoice2.getAnswers());
        bundle.putIntegerArrayList("OrderingAsArray", questionMultipleChoice2.getOrdering());
        bundle.putBoolean("Smileys", z);
        multipleChoiceFragment.setArguments(bundle);
        return multipleChoiceFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        this.questionText = arguments.getString("QuestionText");
        this.answers = arguments.getStringArrayList("AnswersAsArray");
        this.ordering = arguments.getIntegerArrayList("OrderingAsArray");
        this.hasSmileys = arguments.getBoolean("Smileys");
        if (bundle != null) {
            this.selectedResponse = bundle.getString("SelectedResponse", (String) null);
            this.questionMetrics = (QuestionMetrics) bundle.getParcelable("QuestionMetrics");
        }
        if (this.questionMetrics == null) {
            this.questionMetrics = new QuestionMetrics();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("SelectedResponse", this.selectedResponse);
        bundle.putParcelable("QuestionMetrics", this.questionMetrics);
    }

    public void onPageScrolledIntoView() {
        this.questionMetrics.markAsShown();
        ((OnQuestionProgressableChangeListener) getActivity()).onQuestionProgressableChanged(isResponseSatisfactory(), this);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        onCreateView.setContentDescription(this.questionText);
        if (!isDetached()) {
            this.fragmentViewDelegate.watch((FragmentViewDelegate.MeasurementSurrogate) getActivity(), onCreateView);
        }
        return onCreateView;
    }

    public void onDetach() {
        this.fragmentViewDelegate.cleanUp();
        super.onDetach();
    }

    /* access modifiers changed from: package-private */
    public String getQuestionText() {
        return this.questionText;
    }

    public View createScrollViewContents() {
        LayoutInflater from = LayoutInflater.from(getContext());
        View inflate = from.inflate(R$layout.hats_survey_scrollable_answer_content_container, (ViewGroup) null);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R$id.hats_lib_survey_answers_container);
        final View[] viewArr = new View[this.answers.size()];
        for (final int i = 0; i < this.answers.size(); i++) {
            if (this.hasSmileys) {
                from.inflate(R$layout.hats_survey_question_multiple_choice_with_smileys_item, linearLayout, true);
                viewArr[i] = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
                TextView textView = (TextView) viewArr[i].findViewById(R$id.hats_lib_survey_multiple_choice_text);
                textView.setText(this.answers.get(i));
                textView.setContentDescription(this.answers.get(i));
                ((ImageView) viewArr[i].findViewById(R$id.hats_lib_survey_multiple_choice_icon)).setImageResource(QuestionMultipleChoice.READONLY_SURVEY_RATING_ICON_RESOURCE_MAP.get(Integer.valueOf(i)).intValue());
            } else {
                from.inflate(R$layout.hats_survey_question_multiple_choice_item, linearLayout, true);
                viewArr[i] = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
                ((Button) viewArr[i]).setText(this.answers.get(i));
                ((Button) viewArr[i]).setContentDescription(this.answers.get(i));
            }
            viewArr[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MultipleChoiceFragment.this.removeOnClickListenersAndDisableClickEvents(viewArr);
                    ((SurveyPromptActivity) MultipleChoiceFragment.this.getActivity()).setIsMultipleChoiceSelectionAnimating(true);
                    view.postOnAnimationDelayed(new Runnable() {
                        public void run() {
                            SurveyPromptActivity surveyPromptActivity = (SurveyPromptActivity) MultipleChoiceFragment.this.getActivity();
                            boolean isDestroyed = Build.VERSION.SDK_INT >= 17 ? surveyPromptActivity.isDestroyed() : false;
                            if (surveyPromptActivity == null || surveyPromptActivity.isFinishing() || isDestroyed) {
                                Log.w("HatsLibMultiChoiceFrag", "Activity was null, finishing or destroyed while attempting to navigate to the next next page. Likely the user rotated the device before the Runnable executed.");
                                return;
                            }
                            MultipleChoiceFragment multipleChoiceFragment = MultipleChoiceFragment.this;
                            String unused = multipleChoiceFragment.selectedResponse = (String) multipleChoiceFragment.answers.get(i);
                            MultipleChoiceFragment.this.questionMetrics.markAsAnswered();
                            String valueOf = String.valueOf((String) MultipleChoiceFragment.this.answers.get(i));
                            Log.d("HatsLibMultiChoiceFrag", valueOf.length() != 0 ? "User selected response: ".concat(valueOf) : new String("User selected response: "));
                            surveyPromptActivity.nextPageOrSubmit();
                            surveyPromptActivity.setIsMultipleChoiceSelectionAnimating(false);
                        }
                    }, 200);
                }
            });
        }
        return inflate;
    }

    public QuestionResponse computeQuestionResponse() {
        QuestionResponse.Builder builder = QuestionResponse.builder();
        if (this.questionMetrics.isShown()) {
            String str = this.selectedResponse;
            if (str != null) {
                builder.addResponse(str);
            }
            builder.setDelayMs(this.questionMetrics.getDelayMs());
            ArrayList<Integer> arrayList = this.ordering;
            if (arrayList != null) {
                builder.setOrdering(arrayList);
            }
        }
        return builder.build();
    }

    /* access modifiers changed from: private */
    public void removeOnClickListenersAndDisableClickEvents(View[] viewArr) {
        for (View view : viewArr) {
            view.setOnClickListener((View.OnClickListener) null);
            view.setClickable(false);
        }
    }
}
