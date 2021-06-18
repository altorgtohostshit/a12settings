package com.android.settings.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.widget.LayoutPreference;
import java.util.List;

public class MoreSettingsPreferenceController extends BasePreferenceController implements View.OnClickListener {
    private static final String EXTRA_KEY_ATTRIBUTION = "attribution";
    private static final String TAG = "MoreSettingsPrefCtrl";
    Intent mIntent;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public MoreSettingsPreferenceController(Context context, String str) {
        super(context, str);
        String string = this.mContext.getResources().getString(R.string.config_emergency_package_name);
        if (!TextUtils.isEmpty(string)) {
            this.mIntent = new Intent("android.intent.action.MAIN").setPackage(string);
            List<ResolveInfo> queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 1048576);
            if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
                this.mIntent = null;
            } else {
                this.mIntent.setClassName(string, queryIntentActivities.get(0).activityInfo.name);
            }
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Button button = (Button) ((LayoutPreference) preferenceScreen.findPreference(getPreferenceKey())).findViewById(R.id.button);
        Drawable icon = getIcon();
        button.setText(getButtonText());
        if (icon != null) {
            button.setCompoundDrawablesWithIntrinsicBounds(icon, (Drawable) null, (Drawable) null, (Drawable) null);
            button.setVisibility(0);
        }
        button.setOnClickListener(this);
    }

    public int getAvailabilityStatus() {
        return this.mIntent == null ? 3 : 0;
    }

    public void onClick(View view) {
        Intent flags = new Intent(this.mIntent).addCategory("android.intent.category.LAUNCHER").setFlags(67108864);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_KEY_ATTRIBUTION, this.mContext.getPackageName());
        this.mContext.startActivity(flags, bundle);
    }

    private Drawable getIcon() {
        try {
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(this.mContext.getResources().getString(R.string.config_emergency_package_name), 33280);
            Context context = this.mContext;
            return getScaledDrawable(context, Utils.getBadgedIcon(context, applicationInfo), 24, 24);
        } catch (Exception e) {
            Log.d(TAG, "Failed to get open app button icon", e);
            return null;
        }
    }

    private CharSequence getButtonText() {
        String string = this.mContext.getResources().getString(R.string.config_emergency_package_name);
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(string, 33280);
            return this.mContext.getString(R.string.open_app_button, new Object[]{applicationInfo.loadLabel(packageManager)});
        } catch (Exception unused) {
            Log.d(TAG, "Failed to get open app button text, falling back.");
            return "";
        }
    }

    private static Drawable getScaledDrawable(Context context, Drawable drawable, int i, int i2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return new BitmapDrawable(context.getResources(), convertToBitmap(drawable, (int) TypedValue.applyDimension(1, (float) i, displayMetrics), (int) TypedValue.applyDimension(1, (float) i2, displayMetrics)));
    }

    private static Bitmap convertToBitmap(Drawable drawable, int i, int i2) {
        if (drawable == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, i, i2);
        drawable.draw(canvas);
        return createBitmap;
    }
}
