package com.android.settings.homepage.contextualcards.legacysuggestion;

import android.app.PendingIntent;
import android.content.Context;
import android.service.settings.suggestions.Suggestion;
import android.util.ArrayMap;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ContextualCardController;
import com.android.settings.homepage.contextualcards.ContextualCardUpdateListener;
import com.android.settings.homepage.contextualcards.legacysuggestion.LegacySuggestionContextualCard;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.suggestions.SuggestionController;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LegacySuggestionContextualCardController implements ContextualCardController, LifecycleObserver, OnStart, OnStop, SuggestionController.ServiceConnectionListener {
    private ContextualCardUpdateListener mCardUpdateListener;
    private final Context mContext;
    SuggestionController mSuggestionController;
    final List<ContextualCard> mSuggestions = new ArrayList();

    public void onActionClick(ContextualCard contextualCard) {
    }

    public void onServiceDisconnected() {
    }

    public LegacySuggestionContextualCardController(Context context) {
        this.mContext = context;
        if (!context.getResources().getBoolean(R.bool.config_use_legacy_suggestion)) {
            Log.w("LegacySuggestCardCtrl", "Legacy suggestion contextual card disabled, skipping.");
        } else {
            this.mSuggestionController = new SuggestionController(context, FeatureFactory.getFactory(context).getSuggestionFeatureProvider(context).getSuggestionServiceComponent(), this);
        }
    }

    public void onPrimaryClick(ContextualCard contextualCard) {
        try {
            ((LegacySuggestionContextualCard) contextualCard).getPendingIntent().send();
        } catch (PendingIntent.CanceledException unused) {
            Log.w("LegacySuggestCardCtrl", "Failed to start suggestion " + contextualCard.getTitleText());
        }
    }

    public void onDismissed(ContextualCard contextualCard) {
        this.mSuggestionController.dismissSuggestions(((LegacySuggestionContextualCard) contextualCard).getSuggestion());
        this.mSuggestions.remove(contextualCard);
        updateAdapter();
    }

    public void setCardUpdateListener(ContextualCardUpdateListener contextualCardUpdateListener) {
        this.mCardUpdateListener = contextualCardUpdateListener;
    }

    public void onStart() {
        SuggestionController suggestionController = this.mSuggestionController;
        if (suggestionController != null) {
            suggestionController.start();
        }
    }

    public void onStop() {
        SuggestionController suggestionController = this.mSuggestionController;
        if (suggestionController != null) {
            suggestionController.stop();
        }
    }

    public void onServiceConnected() {
        loadSuggestions();
    }

    private void loadSuggestions() {
        ThreadUtils.postOnBackgroundThread((Runnable) new C0966x4d51e8b9(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSuggestions$0() {
        String str;
        SuggestionController suggestionController = this.mSuggestionController;
        if (suggestionController != null && this.mCardUpdateListener != null) {
            List<Suggestion> suggestions = suggestionController.getSuggestions();
            if (suggestions == null) {
                str = "null";
            } else {
                str = String.valueOf(suggestions.size());
            }
            Log.d("LegacySuggestCardCtrl", "Loaded suggests: " + str);
            ArrayList arrayList = new ArrayList();
            if (suggestions != null) {
                for (Suggestion next : suggestions) {
                    LegacySuggestionContextualCard.Builder builder = new LegacySuggestionContextualCard.Builder();
                    if (next.getIcon() != null) {
                        builder.setIconDrawable(next.getIcon().loadDrawable(this.mContext));
                    }
                    builder.setPendingIntent(next.getPendingIntent()).setSuggestion(next).setName(next.getId()).setTitleText(next.getTitle().toString()).setSummaryText(next.getSummary().toString()).setViewType(R.layout.legacy_suggestion_tile);
                    arrayList.add(builder.build());
                }
            }
            this.mSuggestions.clear();
            this.mSuggestions.addAll(arrayList);
            updateAdapter();
        }
    }

    private void updateAdapter() {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(2, this.mSuggestions);
        ThreadUtils.postOnMainThread(new C0967x4d51e8ba(this, arrayMap));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAdapter$1(Map map) {
        this.mCardUpdateListener.onContextualCardUpdated(map);
    }
}
