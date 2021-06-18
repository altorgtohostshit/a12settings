package com.android.settings.panel;

import android.content.Intent;
import android.net.Uri;
import androidx.core.graphics.drawable.IconCompat;
import com.android.settingslib.core.instrumentation.Instrumentable;
import java.util.List;

public interface PanelContent extends Instrumentable {
    CharSequence getCustomizedButtonTitle() {
        return null;
    }

    Intent getHeaderIconIntent() {
        return null;
    }

    IconCompat getIcon() {
        return null;
    }

    Intent getSeeMoreIntent();

    List<Uri> getSlices();

    CharSequence getSubTitle() {
        return null;
    }

    CharSequence getTitle();

    int getViewType() {
        return 0;
    }

    boolean isCustomizedButtonUsed() {
        return false;
    }

    boolean isProgressBarVisible() {
        return false;
    }

    void onClickCustomizedButton() {
    }

    void registerCallback(PanelContentCallback panelContentCallback) {
    }
}
