package com.android.settings.gestures;

import android.content.Context;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class AssistGestureFeatureProviderImpl implements AssistGestureFeatureProvider {
    public boolean isSensorAvailable(Context context) {
        return false;
    }

    public boolean isSupported(Context context) {
        return false;
    }

    public List<AbstractPreferenceController> getControllers(Context context, Lifecycle lifecycle) {
        return new ArrayList();
    }
}
