package com.android.settings.bluetooth;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;

public class CompanionAppWidgetPreference extends Preference {
    private int mImageButtonPadding;
    private Drawable mWidgetIcon;
    private View.OnClickListener mWidgetListener;

    public CompanionAppWidgetPreference(Drawable drawable, View.OnClickListener onClickListener, Context context) {
        super(context);
        this.mWidgetIcon = drawable;
        this.mWidgetListener = onClickListener;
        this.mImageButtonPadding = context.getResources().getDimensionPixelSize(R.dimen.bluetooth_companion_app_widget);
        setWidgetLayoutResource(R.layout.companion_apps_remove_button_widget);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageButton imageButton = (ImageButton) preferenceViewHolder.findViewById(R.id.remove_button);
        int i = this.mImageButtonPadding;
        imageButton.setPadding(i, i, i, i);
        imageButton.setColorFilter(getContext().getColor(17170432));
        imageButton.setImageDrawable(this.mWidgetIcon);
        imageButton.setOnClickListener(this.mWidgetListener);
    }
}
