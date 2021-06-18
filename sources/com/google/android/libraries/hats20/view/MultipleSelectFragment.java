package com.google.android.libraries.hats20.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.google.android.libraries.hats20.R$id;
import com.google.android.libraries.hats20.R$layout;
import com.google.android.libraries.hats20.R$string;
import com.google.android.libraries.hats20.answer.QuestionResponse;
import com.google.android.libraries.hats20.model.Question;
import com.google.android.libraries.hats20.model.QuestionMultipleSelect;
import com.google.android.libraries.hats20.view.FragmentViewDelegate;
import java.util.ArrayList;

public class MultipleSelectFragment extends ScrollableAnswerFragment {
    private ArrayList<String> answers;
    /* access modifiers changed from: private */
    public ViewGroup answersContainer;
    private FragmentViewDelegate fragmentViewDelegate = new FragmentViewDelegate();
    /* access modifiers changed from: private */
    public boolean isNoneOfTheAboveChecked;
    private ArrayList<Integer> ordering;
    private QuestionMetrics questionMetrics;
    private String questionText;
    /* access modifiers changed from: private */
    public boolean[] responses;

    public static MultipleSelectFragment newInstance(Question question) {
        MultipleSelectFragment multipleSelectFragment = new MultipleSelectFragment();
        QuestionMultipleSelect questionMultipleSelect = (QuestionMultipleSelect) question;
        Bundle bundle = new Bundle();
        bundle.putString("QuestionText", question.getQuestionText());
        bundle.putStringArrayList("AnswersAsArray", questionMultipleSelect.getAnswers());
        bundle.putIntegerArrayList("OrderingAsArray", questionMultipleSelect.getOrdering());
        multipleSelectFragment.setArguments(bundle);
        return multipleSelectFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        this.questionText = arguments.getString("QuestionText");
        this.answers = arguments.getStringArrayList("AnswersAsArray");
        this.ordering = arguments.getIntegerArrayList("OrderingAsArray");
        if (bundle != null) {
            this.isNoneOfTheAboveChecked = bundle.getBoolean("NoneOfTheAboveAsBoolean", false);
            this.questionMetrics = (QuestionMetrics) bundle.getParcelable("QuestionMetrics");
            this.responses = bundle.getBooleanArray("ResponsesAsArray");
        }
        if (this.questionMetrics == null) {
            this.questionMetrics = new QuestionMetrics();
        }
        boolean[] zArr = this.responses;
        if (zArr == null) {
            this.responses = new boolean[this.answers.size()];
        } else if (zArr.length != this.answers.size()) {
            int length = this.responses.length;
            StringBuilder sb = new StringBuilder(64);
            sb.append("Saved instance state responses had incorrect length: ");
            sb.append(length);
            Log.e("HatsLibMultiSelectFrag", sb.toString());
            this.responses = new boolean[this.answers.size()];
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("NoneOfTheAboveAsBoolean", this.isNoneOfTheAboveChecked);
        bundle.putParcelable("QuestionMetrics", this.questionMetrics);
        bundle.putBooleanArray("ResponsesAsArray", this.responses);
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

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((OnQuestionProgressableChangeListener) getActivity()).onQuestionProgressableChanged(isResponseSatisfactory(), this);
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
        this.answersContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R$layout.hats_survey_scrollable_answer_content_container, (ViewGroup) null).findViewById(R$id.hats_lib_survey_answers_container);
        for (int i = 0; i < this.answers.size(); i++) {
            addCheckboxToAnswersContainer(this.answers.get(i), this.responses[i], i, (String) null);
        }
        addCheckboxToAnswersContainer(getResources().getString(R$string.hats_lib_none_of_the_above), this.isNoneOfTheAboveChecked, this.answers.size(), "NoneOfTheAbove");
        return this.answersContainer;
    }

    private void addCheckboxToAnswersContainer(String str, boolean z, int i, String str2) {
        LayoutInflater.from(getContext()).inflate(R$layout.hats_survey_question_multiple_select_item, this.answersContainer, true);
        FrameLayout frameLayout = (FrameLayout) this.answersContainer.getChildAt(i);
        final CheckBox checkBox = (CheckBox) frameLayout.findViewById(R$id.hats_lib_multiple_select_checkbox);
        checkBox.setText(str);
        checkBox.setContentDescription(str);
        checkBox.setChecked(z);
        checkBox.setOnCheckedChangeListener(new CheckboxChangeListener(i));
        frameLayout.setOnClickListener(new View.OnClickListener(this) {
            public void onClick(View view) {
                checkBox.performClick();
            }
        });
        if (str2 != null) {
            checkBox.setTag(str2);
        }
    }

    public QuestionResponse computeQuestionResponse() {
        QuestionResponse.Builder builder = QuestionResponse.builder();
        if (this.questionMetrics.isShown()) {
            ArrayList<Integer> arrayList = this.ordering;
            if (arrayList != null) {
                builder.setOrdering(arrayList);
            }
            if (!this.isNoneOfTheAboveChecked) {
                int i = 0;
                while (true) {
                    boolean[] zArr = this.responses;
                    if (i >= zArr.length) {
                        break;
                    }
                    if (zArr[i]) {
                        builder.addResponse(this.answers.get(i));
                        this.questionMetrics.markAsAnswered();
                    }
                    i++;
                }
            } else {
                this.questionMetrics.markAsAnswered();
            }
            builder.setDelayMs(this.questionMetrics.getDelayMs());
        }
        return builder.build();
    }

    private class CheckboxChangeListener implements CompoundButton.OnCheckedChangeListener {
        private final int index;

        CheckboxChangeListener(int i) {
            this.index = i;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if ("NoneOfTheAbove".equals(compoundButton.getTag())) {
                boolean unused = MultipleSelectFragment.this.isNoneOfTheAboveChecked = z;
                if (z) {
                    uncheckAllButNoneOfAbove();
                }
            } else {
                MultipleSelectFragment.this.responses[this.index] = z;
                if (z) {
                    ((CheckBox) MultipleSelectFragment.this.answersContainer.findViewWithTag("NoneOfTheAbove")).setChecked(false);
                }
            }
            OnQuestionProgressableChangeListener onQuestionProgressableChangeListener = (OnQuestionProgressableChangeListener) MultipleSelectFragment.this.getActivity();
            if (onQuestionProgressableChangeListener != null) {
                onQuestionProgressableChangeListener.onQuestionProgressableChanged(MultipleSelectFragment.this.isResponseSatisfactory(), MultipleSelectFragment.this);
            }
        }

        private void uncheckAllButNoneOfAbove() {
            if (MultipleSelectFragment.this.answersContainer.getChildCount() != MultipleSelectFragment.this.responses.length + 1) {
                Log.e("HatsLibMultiSelectFrag", "Number of children (checkboxes) contained in the answers container was not equal to the number of possible responses including \"None of the Above\". Note this is not expected to happen in prod.");
            }
            for (int i = 0; i < MultipleSelectFragment.this.answersContainer.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) MultipleSelectFragment.this.answersContainer.getChildAt(i).findViewById(R$id.hats_lib_multiple_select_checkbox);
                if (!"NoneOfTheAbove".equals(checkBox.getTag())) {
                    checkBox.setChecked(false);
                }
            }
        }
    }

    public boolean isResponseSatisfactory() {
        if (this.isNoneOfTheAboveChecked) {
            return true;
        }
        for (boolean z : this.responses) {
            if (z) {
                return true;
            }
        }
        return false;
    }
}
