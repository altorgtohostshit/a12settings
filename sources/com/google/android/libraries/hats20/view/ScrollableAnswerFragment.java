package com.google.android.libraries.hats20.view;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.android.libraries.hats20.R$dimen;
import com.google.android.libraries.hats20.R$id;
import com.google.android.libraries.hats20.R$layout;
import com.google.android.libraries.hats20.SurveyPromptActivity;
import com.google.android.libraries.hats20.util.TextFormatUtil;
import com.google.android.libraries.hats20.view.ScrollViewWithSizeCallback;

public abstract class ScrollableAnswerFragment extends BaseFragment {
    private boolean isOnScrollChangedListenerAttached = false;
    /* access modifiers changed from: private */
    public TextView questionTextView;
    private ScrollShadowHandler scrollShadowHandler = new ScrollShadowHandler();
    /* access modifiers changed from: private */
    public ScrollViewWithSizeCallback scrollView;
    /* access modifiers changed from: private */
    public View scrollViewContents;
    /* access modifiers changed from: private */
    public View surveyControlsContainer;

    /* access modifiers changed from: package-private */
    public abstract View createScrollViewContents();

    /* access modifiers changed from: package-private */
    public abstract String getQuestionText();

    public void onDetach() {
        stopRespondingToScrollChanges();
        super.onDetach();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R$layout.hats_survey_question_with_scrollable_content, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R$id.hats_lib_survey_question_text);
        this.questionTextView = textView;
        textView.setText(TextFormatUtil.format(getQuestionText()));
        this.questionTextView.setContentDescription(getQuestionText());
        this.scrollViewContents = createScrollViewContents();
        ScrollViewWithSizeCallback scrollViewWithSizeCallback = (ScrollViewWithSizeCallback) inflate.findViewById(R$id.hats_survey_question_scroll_view);
        this.scrollView = scrollViewWithSizeCallback;
        scrollViewWithSizeCallback.addView(this.scrollViewContents);
        this.scrollView.setOnHeightChangedListener(this.scrollShadowHandler);
        startRespondingToScrollChanges();
        this.surveyControlsContainer = ((SurveyPromptActivity) viewGroup.getContext()).getSurveyContainer().findViewById(R$id.hats_lib_survey_controls_container);
        return inflate;
    }

    private void startRespondingToScrollChanges() {
        ScrollViewWithSizeCallback scrollViewWithSizeCallback;
        if (!this.isOnScrollChangedListenerAttached && (scrollViewWithSizeCallback = this.scrollView) != null) {
            scrollViewWithSizeCallback.getViewTreeObserver().addOnScrollChangedListener(this.scrollShadowHandler);
            this.isOnScrollChangedListenerAttached = true;
        }
    }

    private void stopRespondingToScrollChanges() {
        ScrollViewWithSizeCallback scrollViewWithSizeCallback;
        if (this.isOnScrollChangedListenerAttached && (scrollViewWithSizeCallback = this.scrollView) != null) {
            scrollViewWithSizeCallback.getViewTreeObserver().removeOnScrollChangedListener(this.scrollShadowHandler);
            this.isOnScrollChangedListenerAttached = false;
        }
    }

    private class ScrollShadowHandler implements ViewTreeObserver.OnScrollChangedListener, ScrollViewWithSizeCallback.OnHeightChangedListener {
        private ScrollShadowHandler() {
        }

        public void onScrollChanged() {
            updateShadowVisibility(ScrollableAnswerFragment.this.scrollView.getHeight());
        }

        public void onHeightChanged(int i) {
            if (i != 0) {
                updateShadowVisibility(i);
            }
        }

        private void updateShadowVisibility(int i) {
            if (ScrollableAnswerFragment.this.getUserVisibleHint()) {
                boolean z = true;
                boolean z2 = ScrollableAnswerFragment.this.scrollView.getScrollY() == 0;
                boolean z3 = ScrollableAnswerFragment.this.scrollViewContents.getBottom() == ScrollableAnswerFragment.this.scrollView.getScrollY() + i;
                if (ScrollableAnswerFragment.this.scrollViewContents.getBottom() <= i) {
                    z = false;
                }
                if (!z || z2) {
                    hideTopShadow();
                } else {
                    showTopShadow();
                }
                if (!z || z3) {
                    hideBottomShadow();
                } else {
                    showBottomShadow();
                }
            }
        }

        private void hideBottomShadow() {
            setElevation(ScrollableAnswerFragment.this.surveyControlsContainer, 0.0f);
        }

        private void showTopShadow() {
            setElevation(ScrollableAnswerFragment.this.questionTextView, (float) ScrollableAnswerFragment.this.getResources().getDimensionPixelSize(R$dimen.hats_lib_question_view_elevation));
        }

        private void hideTopShadow() {
            setElevation(ScrollableAnswerFragment.this.questionTextView, 0.0f);
        }

        private void showBottomShadow() {
            setElevation(ScrollableAnswerFragment.this.surveyControlsContainer, (float) ScrollableAnswerFragment.this.getResources().getDimensionPixelSize(R$dimen.hats_lib_survey_controls_view_elevation));
        }

        private void setElevation(View view, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(f);
            }
        }
    }
}
