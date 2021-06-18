package com.android.settingslib.transition;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.view.animation.AnimationUtils;
import androidx.core.p002os.BuildCompat;
import com.google.android.material.transition.platform.FadeThroughProvider;
import com.google.android.material.transition.platform.MaterialSharedAxis;
import com.google.android.material.transition.platform.SlideDistanceProvider;

public class SettingsTransitionHelper {
    private static MaterialSharedAxis createSettingsSharedAxis(Context context, boolean z) {
        MaterialSharedAxis materialSharedAxis = new MaterialSharedAxis(0, z);
        materialSharedAxis.excludeTarget(16908335, true);
        materialSharedAxis.excludeTarget(16908336, true);
        ((SlideDistanceProvider) materialSharedAxis.getPrimaryAnimatorProvider()).setSlideDistance(context.getResources().getDimensionPixelSize(R$dimen.settings_shared_axis_x_slide_distance));
        materialSharedAxis.setDuration(450);
        ((FadeThroughProvider) materialSharedAxis.getSecondaryAnimatorProvider()).setProgressThreshold(0.22f);
        materialSharedAxis.setInterpolator(AnimationUtils.loadInterpolator(context, R$interpolator.fast_out_extra_slow_in));
        return materialSharedAxis;
    }

    public static void applyForwardTransition(Activity activity) {
        if (BuildCompat.isAtLeastS()) {
            if (activity == null) {
                Log.w("SettingsTransitionHelper", "applyForwardTransition: Invalid activity!");
                return;
            }
            Window window = activity.getWindow();
            if (window == null) {
                Log.w("SettingsTransitionHelper", "applyForwardTransition: Invalid window!");
                return;
            }
            MaterialSharedAxis createSettingsSharedAxis = createSettingsSharedAxis(activity, true);
            window.setExitTransition(createSettingsSharedAxis);
            window.setEnterTransition(createSettingsSharedAxis);
        }
    }

    public static void applyBackwardTransition(Activity activity) {
        if (BuildCompat.isAtLeastS()) {
            if (activity == null) {
                Log.w("SettingsTransitionHelper", "applyBackwardTransition: Invalid activity!");
                return;
            }
            Window window = activity.getWindow();
            if (window == null) {
                Log.w("SettingsTransitionHelper", "applyBackwardTransition: Invalid window!");
                return;
            }
            MaterialSharedAxis createSettingsSharedAxis = createSettingsSharedAxis(activity, false);
            window.setReturnTransition(createSettingsSharedAxis);
            window.setReenterTransition(createSettingsSharedAxis);
        }
    }
}
