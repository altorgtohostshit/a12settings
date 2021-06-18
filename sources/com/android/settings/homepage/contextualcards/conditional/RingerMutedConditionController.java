package com.android.settings.homepage.contextualcards.conditional;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.conditional.ConditionalContextualCard;
import java.util.Objects;

public class RingerMutedConditionController extends AbnormalRingerConditionController {

    /* renamed from: ID */
    static final int f81ID = Objects.hash(new Object[]{"RingerMutedConditionController"});
    private final Context mAppContext;

    public RingerMutedConditionController(Context context, ConditionManager conditionManager) {
        super(context, conditionManager);
        this.mAppContext = context;
    }

    public long getId() {
        return (long) f81ID;
    }

    public boolean isDisplayable() {
        return this.mAudioManager.getRingerModeInternal() == 0;
    }

    public ContextualCard buildContextualCard() {
        ConditionalContextualCard.Builder actionText = new ConditionalContextualCard.Builder().setConditionId((long) f81ID).setMetricsConstant(1368).setActionText(this.mAppContext.getText(R.string.condition_device_muted_action_turn_on_sound));
        return actionText.setName(this.mAppContext.getPackageName() + "/" + this.mAppContext.getText(R.string.condition_device_muted_title)).setTitleText(this.mAppContext.getText(R.string.condition_device_muted_title).toString()).setSummaryText(this.mAppContext.getText(R.string.condition_device_muted_summary).toString()).setIconDrawable(this.mAppContext.getDrawable(R.drawable.ic_notifications_off_24dp)).setViewType(R.layout.conditional_card_half_tile).build();
    }
}
