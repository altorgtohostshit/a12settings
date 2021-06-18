package com.google.android.libraries.hats20;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.cardview.widget.CardView;
import com.google.android.libraries.hats20.answer.AnswerBeacon;
import com.google.android.libraries.hats20.answer.AnswerBeaconTransmitter;
import com.google.android.libraries.hats20.model.SurveyController;
import com.google.android.libraries.hats20.storage.HatsDataStore;
import com.google.android.libraries.hats20.util.LayoutDimensions;
import com.google.android.libraries.hats20.util.LayoutUtils;
import com.google.android.libraries.material.autoresizetext.AutoResizeTextView;

class PromptDialogDelegate {
    /* access modifiers changed from: private */
    public AnswerBeacon answerBeacon;
    private boolean areDimensionsValid = false;
    private DimensionConfigurationHelper configurationHelper;
    private Context context;
    /* access modifiers changed from: private */
    public final DialogFragmentInterface dialogFragment;
    /* access modifiers changed from: private */
    public boolean isBottomSheet = false;
    /* access modifiers changed from: private */
    public boolean isStartingSurvey = false;
    private boolean isTwoLinePrompt = false;
    private LayoutDimensions layoutDimensions;
    private int maxPromptWidth;
    /* access modifiers changed from: private */
    public SurveyController surveyController;

    public interface DialogFragmentInterface {
        void dismissAllowingStateLoss();

        Activity getActivity();

        Bundle getArguments();

        Dialog getDialog();

        boolean getShowsDialog();
    }

    public PromptDialogDelegate(DialogFragmentInterface dialogFragmentInterface) {
        this.dialogFragment = dialogFragmentInterface;
    }

    public static Bundle createArgs(String str, SurveyController surveyController2, AnswerBeacon answerBeacon2, Integer num, Integer num2, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putString("SiteId", str);
        bundle.putParcelable("SurveyController", surveyController2);
        bundle.putParcelable("AnswerBeacon", answerBeacon2);
        if (num != null) {
            bundle.putInt("RequestCode", num.intValue());
        }
        if (num2 != null) {
            bundle.putInt("MaxPromptWidth", num2.intValue());
        }
        bundle.putBoolean("BottomSheet", z);
        return bundle;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Activity activity = this.dialogFragment.getActivity();
        this.context = activity;
        this.layoutDimensions = new LayoutDimensions(activity.getResources());
        Bundle arguments = this.dialogFragment.getArguments();
        final String string = arguments.getString("SiteId");
        final int i = arguments.getInt("RequestCode", -1);
        this.maxPromptWidth = arguments.getInt("MaxPromptWidth", this.layoutDimensions.getPromptMaxWidth());
        this.surveyController = (SurveyController) arguments.getParcelable("SurveyController");
        this.answerBeacon = (AnswerBeacon) arguments.getParcelable("AnswerBeacon");
        this.isBottomSheet = arguments.getBoolean("BottomSheet");
        if (this.dialogFragment.getShowsDialog()) {
            this.dialogFragment.getDialog().requestWindowFeature(1);
        }
        HatsClient.markSurveyRunning();
        View inflate = layoutInflater.inflate(R$layout.hats_prompt_banner, viewGroup, false);
        updatePromptBannerText(inflate, this.surveyController.getPromptMessage());
        inflate.findViewById(R$id.hats_lib_prompt_banner).setMinimumHeight(this.layoutDimensions.getPromptBannerHeight(this.isTwoLinePrompt));
        this.configurationHelper = new DimensionConfigurationHelper((CardView) inflate, this.dialogFragment.getDialog(), this.layoutDimensions, this.isBottomSheet, this.isTwoLinePrompt);
        final Button button = (Button) inflate.findViewById(R$id.hats_lib_prompt_no_thanks_button);
        final Button button2 = (Button) inflate.findViewById(R$id.hats_lib_prompt_take_survey_button);
        inflate.findViewById(R$id.hats_lib_prompt_no_thanks_wrapper).setOnTouchListener(new View.OnTouchListener(this) {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button.onTouchEvent(motionEvent);
                return true;
            }
        });
        inflate.findViewById(R$id.hats_lib_prompt_take_survey_wrapper).setOnTouchListener(new View.OnTouchListener(this) {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button2.onTouchEvent(motionEvent);
                return true;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SurveyPromptActivity.startSurveyActivity(PromptDialogDelegate.this.dialogFragment.getActivity(), string, PromptDialogDelegate.this.surveyController, PromptDialogDelegate.this.answerBeacon, Integer.valueOf(i), PromptDialogDelegate.this.isBottomSheet);
                boolean unused = PromptDialogDelegate.this.isStartingSurvey = true;
                PromptDialogDelegate.this.dialogFragment.dismissAllowingStateLoss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PromptDialogDelegate.this.transmitOtherAccessBeacon();
                PromptDialogDelegate.this.dialogFragment.dismissAllowingStateLoss();
            }
        });
        return inflate;
    }

    public void onStart() {
        configureDimensions();
    }

    public void onResume() {
        configureDimensions();
    }

    public void onPause() {
        this.areDimensionsValid = false;
    }

    private void configureDimensions() {
        if (!this.areDimensionsValid) {
            this.configurationHelper.configureDimensions();
        }
        this.areDimensionsValid = true;
    }

    private void updatePromptBannerText(View view, String str) {
        AutoResizeTextView autoResizeTextView = (AutoResizeTextView) view.findViewById(R$id.hats_lib_prompt_title_text);
        Resources resources = this.context.getResources();
        LayoutUtils.fitTextInTextViewWrapIfNeeded((float) (DimensionConfigurationHelper.getPromptWidthPx(this.context, this.maxPromptWidth) - (resources.getDimensionPixelSize(R$dimen.hats_lib_prompt_banner_left_padding) + resources.getDimensionPixelSize(R$dimen.hats_lib_prompt_banner_right_padding))), 20, 16, str, autoResizeTextView);
        if (autoResizeTextView.getMaxLines() == 2) {
            this.isTwoLinePrompt = true;
        }
        autoResizeTextView.setText(str);
    }

    public void onDestroy() {
        if (!this.isStartingSurvey) {
            HatsClient.markSurveyFinished();
        }
    }

    /* access modifiers changed from: private */
    public void transmitOtherAccessBeacon() {
        this.answerBeacon.setBeaconType("o");
        new AnswerBeaconTransmitter(this.surveyController.getAnswerUrl(), HatsDataStore.buildFromContext(this.context)).transmit(this.answerBeacon);
    }
}
