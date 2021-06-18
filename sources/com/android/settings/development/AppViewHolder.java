package com.android.settings.development;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settings.R;

public class AppViewHolder {
    public ImageView appIcon;
    public TextView appName;
    public TextView disabled;
    public View rootView;
    public TextView summary;
    public View widget;

    public static AppViewHolder createOrRecycle(LayoutInflater layoutInflater, View view) {
        if (view != null) {
            return (AppViewHolder) view.getTag();
        }
        View inflate = layoutInflater.inflate(R.layout.preference_app, (ViewGroup) null);
        AppViewHolder appViewHolder = new AppViewHolder();
        appViewHolder.rootView = inflate;
        appViewHolder.appName = (TextView) inflate.findViewById(16908310);
        appViewHolder.appIcon = (ImageView) inflate.findViewById(16908294);
        appViewHolder.summary = (TextView) inflate.findViewById(16908304);
        appViewHolder.disabled = (TextView) inflate.findViewById(R.id.appendix);
        appViewHolder.widget = inflate.findViewById(16908312);
        inflate.setTag(appViewHolder);
        return appViewHolder;
    }
}
