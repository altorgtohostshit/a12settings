package com.android.settings.homepage.contextualcards.slices;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ContextualCardController;
import com.android.settings.homepage.contextualcards.ContextualCardFeedbackDialog;
import com.android.settings.homepage.contextualcards.ContextualCardUpdateListener;
import com.android.settings.homepage.contextualcards.logging.ContextualCardLogUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.utils.ThreadUtils;

public class SliceContextualCardController implements ContextualCardController {
    private ContextualCardUpdateListener mCardUpdateListener;
    private final Context mContext;

    public void onActionClick(ContextualCard contextualCard) {
    }

    public void onPrimaryClick(ContextualCard contextualCard) {
    }

    public SliceContextualCardController(Context context) {
        this.mContext = context;
    }

    public void onDismissed(ContextualCard contextualCard) {
        ThreadUtils.postOnBackgroundThread((Runnable) new SliceContextualCardController$$ExternalSyntheticLambda0(this, contextualCard));
        showFeedbackDialog(contextualCard);
        FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, 1665, ContextualCardLogUtils.buildCardDismissLog(contextualCard));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDismissed$0(ContextualCard contextualCard) {
        FeatureFactory.getFactory(this.mContext).getContextualCardFeatureProvider(this.mContext).markCardAsDismissed(this.mContext, contextualCard.getName());
    }

    public void setCardUpdateListener(ContextualCardUpdateListener contextualCardUpdateListener) {
        this.mCardUpdateListener = contextualCardUpdateListener;
    }

    /* access modifiers changed from: package-private */
    public void showFeedbackDialog(ContextualCard contextualCard) {
        String string = this.mContext.getString(R.string.config_contextual_card_feedback_email);
        if (isFeedbackEnabled(string)) {
            Intent intent = new Intent(this.mContext, ContextualCardFeedbackDialog.class);
            intent.putExtra("card_name", getSimpleCardName(contextualCard));
            intent.putExtra("feedback_email", string);
            intent.addFlags(268435456);
            this.mContext.startActivity(intent);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isFeedbackEnabled(String str) {
        return !TextUtils.isEmpty(str) && Build.IS_DEBUGGABLE;
    }

    private String getSimpleCardName(ContextualCard contextualCard) {
        String[] split = contextualCard.getName().split("/");
        return split[split.length - 1];
    }
}
