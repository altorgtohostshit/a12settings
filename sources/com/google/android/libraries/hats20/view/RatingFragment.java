package com.google.android.libraries.hats20.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.libraries.hats20.R$dimen;
import com.google.android.libraries.hats20.R$id;
import com.google.android.libraries.hats20.R$layout;
import com.google.android.libraries.hats20.SurveyPromptActivity;
import com.google.android.libraries.hats20.answer.QuestionResponse;
import com.google.android.libraries.hats20.model.QuestionRating;
import com.google.android.libraries.hats20.p004ui.StarRatingBar;
import com.google.android.libraries.hats20.util.LayoutUtils;
import com.google.android.libraries.hats20.util.TextFormatUtil;
import com.google.android.libraries.hats20.view.FragmentViewDelegate;
import com.google.android.libraries.material.autoresizetext.AutoResizeTextView;

public class RatingFragment extends BaseFragment {
    private FragmentViewDelegate fragmentViewDelegate = new FragmentViewDelegate();
    /* access modifiers changed from: private */
    public QuestionRating question;
    /* access modifiers changed from: private */
    public QuestionMetrics questionMetrics;
    /* access modifiers changed from: private */
    public String selectedResponse;

    public static RatingFragment newInstance(QuestionRating questionRating) {
        RatingFragment ratingFragment = new RatingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Question", questionRating);
        ratingFragment.setArguments(bundle);
        return ratingFragment;
    }

    public void updateRatingQuestionTextSize(AutoResizeTextView autoResizeTextView) {
        Resources resources = getResources();
        int size = View.MeasureSpec.getSize(((FragmentViewDelegate.MeasurementSurrogate) getActivity()).getMeasureSpecs().x);
        LayoutUtils.fitTextInTextViewWrapIfNeeded(((float) size) - ((((float) (resources.getDimensionPixelSize(R$dimen.hats_lib_rating_container_padding) * 2)) + TypedValue.applyDimension(1, 24.0f, resources.getDisplayMetrics())) + TypedValue.applyDimension(1, 40.0f, resources.getDisplayMetrics())), 20, 16, this.question.getQuestionText(), autoResizeTextView);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.question = (QuestionRating) getArguments().getParcelable("Question");
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

    public void onDetach() {
        this.fragmentViewDelegate.cleanUp();
        super.onDetach();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R$layout.hats_survey_question_rating, viewGroup, false);
        inflate.setContentDescription(this.question.getQuestionText());
        TextView textView = (TextView) inflate.findViewById(R$id.hats_lib_survey_question_text);
        textView.setText(TextFormatUtil.format(this.question.getQuestionText()));
        textView.setContentDescription(this.question.getQuestionText());
        setTextAndContentDescription((TextView) inflate.findViewById(R$id.hats_lib_survey_rating_low_value_text), this.question.getLowValueText());
        setTextAndContentDescription((TextView) inflate.findViewById(R$id.hats_lib_survey_rating_high_value_text), this.question.getHighValueText());
        final ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(R$id.hats_lib_survey_rating_images_container);
        final StarRatingBar starRatingBar = (StarRatingBar) inflate.findViewById(R$id.hats_lib_star_rating_bar);
        int i = C15643.f113xffbb3005[this.question.getSprite().ordinal()];
        if (i == 1) {
            viewGroup2.setVisibility(0);
            int i2 = 0;
            while (i2 < 5) {
                View inflate2 = layoutInflater.inflate(R$layout.hats_survey_question_rating_item, viewGroup2, false);
                ((ImageView) inflate2.findViewById(R$id.hats_lib_survey_rating_icon)).setImageResource(QuestionRating.READONLY_SURVEY_RATING_ICON_RESOURCE_MAP.get(Integer.valueOf(i2)).intValue());
                final int i3 = i2 + 1;
                inflate2.setTag(Integer.valueOf(i3));
                setDescriptionForTalkBack(inflate2, i3, 5);
                inflate2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        RatingFragment.this.removeOnClickListenersAndDisableClickEvents(viewGroup2);
                        int i = i3;
                        StringBuilder sb = new StringBuilder(35);
                        sb.append("Rating selected, value: ");
                        sb.append(i);
                        Log.d("HatsLibRatingFragment", sb.toString());
                        RatingFragment.this.questionMetrics.markAsAnswered();
                        String unused = RatingFragment.this.selectedResponse = Integer.toString(i3);
                        ((SurveyPromptActivity) RatingFragment.this.getActivity()).nextPageOrSubmit();
                    }
                });
                removeMarginIfNeeded(inflate2, i2, 5);
                viewGroup2.addView(inflate2);
                i2 = i3;
            }
        } else if (i == 2) {
            starRatingBar.setVisibility(0);
            starRatingBar.setNumStars(this.question.getNumIcons());
            starRatingBar.setOnRatingChangeListener(new StarRatingBar.OnRatingChangeListener() {
                public void onRatingChanged(int i) {
                    RatingFragment ratingFragment = RatingFragment.this;
                    ratingFragment.setDescriptionForTalkBack(starRatingBar, i, ratingFragment.question.getNumIcons());
                    RatingFragment.this.questionMetrics.markAsAnswered();
                    String unused = RatingFragment.this.selectedResponse = Integer.toString(i);
                    ((OnQuestionProgressableChangeListener) RatingFragment.this.getActivity()).onQuestionProgressableChanged(RatingFragment.this.isResponseSatisfactory(), RatingFragment.this);
                }
            });
        } else {
            String valueOf = String.valueOf(this.question.getSprite());
            StringBuilder sb = new StringBuilder(valueOf.length() + 15);
            sb.append("Unknown sprite ");
            sb.append(valueOf);
            throw new IllegalStateException(sb.toString());
        }
        updateRatingQuestionTextSize((AutoResizeTextView) inflate.findViewById(R$id.hats_lib_survey_question_text));
        if (!isDetached()) {
            this.fragmentViewDelegate.watch((FragmentViewDelegate.MeasurementSurrogate) getActivity(), inflate);
        }
        return inflate;
    }

    /* renamed from: com.google.android.libraries.hats20.view.RatingFragment$3 */
    static /* synthetic */ class C15643 {

        /* renamed from: $SwitchMap$com$google$android$libraries$hats20$model$QuestionRating$Sprite */
        static final /* synthetic */ int[] f113xffbb3005;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.google.android.libraries.hats20.model.QuestionRating$Sprite[] r0 = com.google.android.libraries.hats20.model.QuestionRating.Sprite.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f113xffbb3005 = r0
                com.google.android.libraries.hats20.model.QuestionRating$Sprite r1 = com.google.android.libraries.hats20.model.QuestionRating.Sprite.SMILEYS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f113xffbb3005     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.android.libraries.hats20.model.QuestionRating$Sprite r1 = com.google.android.libraries.hats20.model.QuestionRating.Sprite.STARS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.libraries.hats20.view.RatingFragment.C15643.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public void removeOnClickListenersAndDisableClickEvents(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setOnClickListener((View.OnClickListener) null);
            viewGroup.getChildAt(i).setClickable(false);
        }
    }

    private void setTextAndContentDescription(TextView textView, String str) {
        textView.setText(str);
        textView.setContentDescription(str);
    }

    /* access modifiers changed from: private */
    public void setDescriptionForTalkBack(View view, int i, int i2) {
        String format = String.format("%d of %d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        if (i == 1) {
            String valueOf = String.valueOf(format);
            String valueOf2 = String.valueOf(this.question.getLowValueText());
            StringBuilder sb = new StringBuilder(valueOf.length() + 1 + valueOf2.length());
            sb.append(valueOf);
            sb.append(" ");
            sb.append(valueOf2);
            format = sb.toString();
        } else if (i == i2) {
            String valueOf3 = String.valueOf(format);
            String valueOf4 = String.valueOf(this.question.getHighValueText());
            StringBuilder sb2 = new StringBuilder(valueOf3.length() + 1 + valueOf4.length());
            sb2.append(valueOf3);
            sb2.append(" ");
            sb2.append(valueOf4);
            format = sb2.toString();
        }
        view.setContentDescription(format);
    }

    private void removeMarginIfNeeded(View view, int i, int i2) {
        if (i == 0 || i == i2 - 1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (i == 0) {
                layoutParams.setMargins(0, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
            } else if (i == i2 - 1) {
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, 0, layoutParams.bottomMargin);
            }
            view.setLayoutParams(layoutParams);
        }
    }

    public QuestionResponse computeQuestionResponse() {
        QuestionResponse.Builder builder = QuestionResponse.builder();
        if (this.questionMetrics.isShown()) {
            builder.setDelayMs(this.questionMetrics.getDelayMs());
            String str = this.selectedResponse;
            if (str != null) {
                builder.addResponse(str);
                String valueOf = String.valueOf(this.selectedResponse);
                Log.d("HatsLibRatingFragment", valueOf.length() != 0 ? "Selected response: ".concat(valueOf) : new String("Selected response: "));
            }
        }
        return builder.build();
    }

    public boolean isResponseSatisfactory() {
        return this.selectedResponse != null;
    }
}
