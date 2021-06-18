package com.google.android.settings.dashboard.suggestions;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import com.android.settings.dashboard.suggestions.SuggestionFeatureProviderImpl;
import com.android.settings.overlay.FeatureFactory;
import com.google.android.settings.aware.AwareSettingsActivity;
import com.google.android.settings.aware.WakeScreenSuggestionActivity;

public class SuggestionFeatureProviderGoogleImpl extends SuggestionFeatureProviderImpl {
    public SuggestionFeatureProviderGoogleImpl(Context context) {
        super(context);
    }

    public ComponentName getSuggestionServiceComponent() {
        return new ComponentName("com.google.android.settings.intelligence", "com.google.android.settings.intelligence.modules.suggestions.SuggestionService");
    }

    public boolean isSuggestionComplete(Context context, ComponentName componentName) {
        String className = componentName.getClassName();
        if (className.equals("com.google.android.settings.gestures.AssistGestureSuggestion")) {
            boolean isSupported = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider().isSupported(context);
            boolean z = Settings.Secure.getInt(context.getContentResolver(), "assist_gesture_setup_complete", 0) != 0;
            boolean z2 = Settings.Secure.getInt(context.getContentResolver(), "assist_gesture_enabled", 1) != 0;
            if (!isSupported || z || !z2) {
                return true;
            }
            return false;
        } else if (className.equals(AwareSettingsActivity.class.getName())) {
            return AwareSettingsActivity.isSuggestionComplete(context);
        } else {
            if (className.equals(WakeScreenSuggestionActivity.class.getName())) {
                return WakeScreenSuggestionActivity.isSuggestionComplete(context);
            }
            return super.isSuggestionComplete(context, componentName);
        }
    }

    public Class<? extends Fragment> getContextualSuggestionFragment() {
        return ContextualSuggestionFragment.class;
    }
}
