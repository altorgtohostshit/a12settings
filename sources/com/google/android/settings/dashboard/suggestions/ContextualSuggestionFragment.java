package com.google.android.settings.dashboard.suggestions;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.service.settings.suggestions.Suggestion;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.homepage.SettingsHomepageActivity;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.suggestions.SuggestionController;
import com.android.settingslib.utils.ThreadUtils;
import java.util.List;

public class ContextualSuggestionFragment extends InstrumentedFragment implements SuggestionController.ServiceConnectionListener {
    static final String SUGGESTIONS = "suggestions";
    static final String SUW_PACKAGE = "com.google.android.setupwizard";
    static final String TIPS_PACKAGE = "com.google.android.apps.tips";
    private ImageView mIcon;
    private long mStartTime;
    SuggestionController mSuggestionController;
    private View mSuggestionTile;
    private List<Suggestion> mSuggestions;
    private boolean mSuggestionsRestored;
    private TextView mSummary;
    private TextView mTitle;

    public int getMetricsCategory() {
        return 1502;
    }

    public void onServiceDisconnected() {
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mSuggestionController = new SuggestionController(context, FeatureFactory.getFactory(context).getSuggestionFeatureProvider(context).getSuggestionServiceComponent(), this);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.contextual_suggestion_tile, viewGroup, true);
        this.mSuggestionTile = inflate;
        this.mIcon = (ImageView) inflate.findViewById(16908294);
        this.mTitle = (TextView) this.mSuggestionTile.findViewById(16908310);
        this.mSummary = (TextView) this.mSuggestionTile.findViewById(16908304);
        if (bundle != null) {
            this.mSuggestionsRestored = true;
            lambda$loadSuggestions$0(bundle.getParcelableArrayList(SUGGESTIONS));
        }
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableList(SUGGESTIONS, this.mSuggestions);
        super.onSaveInstanceState(bundle);
    }

    public void onStart() {
        super.onStart();
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
        super.onStop();
    }

    public void onServiceConnected() {
        loadSuggestions();
    }

    /* access modifiers changed from: package-private */
    public void loadSuggestions() {
        if (this.mSuggestionsRestored) {
            this.mSuggestionsRestored = false;
            return;
        }
        Log.d("ContextualSuggestFrag", "loadSuggestions");
        this.mStartTime = SystemClock.uptimeMillis();
        if (this.mSuggestionController == null) {
            Log.w("ContextualSuggestFrag", "Cannot get SuggestionController");
            showSuggestionTile(false);
            return;
        }
        ThreadUtils.postOnBackgroundThread((Runnable) new ContextualSuggestionFragment$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSuggestions$1() {
        String str;
        Log.d("ContextualSuggestFrag", "Start loading suggestions");
        List<Suggestion> suggestions = this.mSuggestionController.getSuggestions();
        if (suggestions == null) {
            str = "null";
        } else {
            str = String.valueOf(suggestions.size());
        }
        Log.d("ContextualSuggestFrag", "Loaded suggests: " + str);
        ThreadUtils.postOnMainThread(new ContextualSuggestionFragment$$ExternalSyntheticLambda2(this, suggestions));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: updateState */
    public void lambda$loadSuggestions$0(List<Suggestion> list) {
        this.mSuggestions = list;
        if (list == null || list.isEmpty()) {
            Log.d("ContextualSuggestFrag", "Remove suggestion");
            showSuggestionTile(false);
            return;
        }
        Suggestion suggestion = list.get(0);
        ImageView imageView = this.mIcon;
        if (imageView != null) {
            imageView.setImageIcon(suggestion.getIcon());
        }
        String packageName = ComponentName.unflattenFromString(suggestion.getId()).getPackageName();
        TextView textView = this.mTitle;
        if (textView != null) {
            textView.setText(getSuggestionTitle(packageName));
        }
        TextView textView2 = this.mSummary;
        if (textView2 != null) {
            textView2.setText(getSuggestionSummary(packageName));
        }
        this.mSuggestionTile.setOnClickListener(new ContextualSuggestionFragment$$ExternalSyntheticLambda0(suggestion));
        showSuggestionTile(true);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateState$2(Suggestion suggestion, View view) {
        try {
            suggestion.getPendingIntent().send();
        } catch (PendingIntent.CanceledException unused) {
            Log.w("ContextualSuggestFrag", "Failed to start suggestion " + suggestion.getTitle());
        }
    }

    /* access modifiers changed from: package-private */
    public int getSuggestionTitle(String str) {
        str.hashCode();
        if (str.equals(SUW_PACKAGE)) {
            return R.string.setup_suggestion_title;
        }
        if (!str.equals(TIPS_PACKAGE)) {
            return 0;
        }
        return R.string.tips_suggestion_title;
    }

    /* access modifiers changed from: package-private */
    public int getSuggestionSummary(String str) {
        str.hashCode();
        if (str.equals(SUW_PACKAGE)) {
            return R.string.setup_suggestion_summary;
        }
        if (!str.equals(TIPS_PACKAGE)) {
            return 0;
        }
        return R.string.tips_suggestion_summary;
    }

    /* access modifiers changed from: package-private */
    public void showSuggestionTile(boolean z) {
        long uptimeMillis = SystemClock.uptimeMillis() - this.mStartTime;
        Log.d("ContextualSuggestFrag", "Total loading time: " + uptimeMillis + " ms");
        this.mMetricsFeatureProvider.action(getContext(), 1662, (int) uptimeMillis);
        FragmentActivity activity = getActivity();
        if (activity != null && (activity instanceof SettingsHomepageActivity)) {
            ((SettingsHomepageActivity) activity).showHomepageWithSuggestion(z);
        }
    }
}
