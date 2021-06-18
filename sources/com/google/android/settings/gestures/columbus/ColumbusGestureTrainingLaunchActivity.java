package com.google.android.settings.gestures.columbus;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.R$styleable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ColumbusGestureTrainingLaunchActivity extends ColumbusGestureTrainingBase {
    private AppAdapter mAppAdapter;

    public int getMetricsCategory() {
        return 1759;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        setContentView((int) R.layout.columbus_gesture_training_launch_activity);
        super.onCreate(bundle);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.apps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PackageManager packageManager = getPackageManager();
        AppAdapter appAdapter = new AppAdapter(packageManager, (List) packageManager.getInstalledApplications(128).stream().filter(new ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda3(packageManager)).sorted(Comparator.comparing(new ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda2(packageManager))).collect(Collectors.toList()), getColor(R.color.columbus_highlight));
        this.mAppAdapter = appAdapter;
        recyclerView.setAdapter(appAdapter);
        GlifLayout glifLayout = (GlifLayout) findViewById(R.id.layout);
        glifLayout.setDescriptionText((int) R.string.columbus_gesture_training_launch_text);
        FooterBarMixin footerBarMixin = (FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class);
        footerBarMixin.setPrimaryButton(new FooterButton.Builder(this).setText(R.string.wizard_next).setListener(new ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda0(this)).setButtonType(5).setTheme(2131952136).build());
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.columbus_gesture_enrollment_do_it_later).setListener(new ColumbusGestureTrainingLaunchActivity$$ExternalSyntheticLambda1(this)).setButtonType(2).setTheme(2131952137).build());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onCreate$0(PackageManager packageManager, ApplicationInfo applicationInfo) {
        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
        if (launchIntentForPackage == null) {
            return false;
        }
        return launchIntentForPackage.getCategories().contains("android.intent.category.LAUNCHER");
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            setResult(i2, intent);
            finishAndRemoveTask();
        }
    }

    /* access modifiers changed from: private */
    public void onNextButtonClicked(View view) {
        String selectedPackage = this.mAppAdapter.getSelectedPackage();
        if (selectedPackage == null) {
            Toast.makeText(this, R.string.columbus_gesture_training_launch_no_selection_error, 0).show();
            return;
        }
        Settings.Secure.putString(getContentResolver(), "columbus_launch_app", selectedPackage);
        startFinishedActivity();
    }

    /* access modifiers changed from: private */
    public void onCancelButtonClicked(View view) {
        setResult(R$styleable.Constraint_layout_goneMarginRight);
        finishAndRemoveTask();
    }

    private void startFinishedActivity() {
        Intent intent = new Intent(this, ColumbusGestureTrainingFinishedActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivityForResult(intent, 1);
    }

    private static class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {
        private final List<ApplicationInfo> mApps;
        private final int mHighlightColor;
        private final PackageManager mPackageManager;
        private int mSelectedPosition = -1;

        AppAdapter(PackageManager packageManager, List<ApplicationInfo> list, int i) {
            this.mPackageManager = packageManager;
            this.mApps = list;
            this.mHighlightColor = i;
        }

        public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new AppViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.columbus_app_list_item, viewGroup, false), this.mHighlightColor);
        }

        public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
            ApplicationInfo applicationInfo = this.mApps.get(i);
            appViewHolder.setIcon(this.mPackageManager.getApplicationIcon(applicationInfo));
            appViewHolder.setText(this.mPackageManager.getApplicationLabel(applicationInfo));
            appViewHolder.setClickListener(new C1862xd1fb7844(this, i));
            appViewHolder.setHighlighted(this.mSelectedPosition == i);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(int i, View view) {
            int i2 = this.mSelectedPosition;
            if (i2 >= 0 && i2 < getItemCount()) {
                notifyItemChanged(this.mSelectedPosition);
            }
            this.mSelectedPosition = i;
            notifyItemChanged(i);
        }

        public int getItemCount() {
            return this.mApps.size();
        }

        /* access modifiers changed from: package-private */
        public String getSelectedPackage() {
            int i = this.mSelectedPosition;
            if (i < 0 || i >= this.mApps.size()) {
                return null;
            }
            return this.mApps.get(this.mSelectedPosition).packageName;
        }
    }

    private static class AppViewHolder extends RecyclerView.ViewHolder {
        private final int mHighlightColor;
        private final ImageView mImageView;
        private final TextView mTextView;
        private final View mView;

        AppViewHolder(View view, int i) {
            super(view);
            this.mView = view;
            this.mImageView = (ImageView) view.findViewById(R.id.icon);
            this.mTextView = (TextView) view.findViewById(R.id.text);
            this.mHighlightColor = i;
        }

        public void setIcon(Drawable drawable) {
            this.mImageView.setImageDrawable(drawable);
        }

        public void setText(CharSequence charSequence) {
            this.mTextView.setText(charSequence);
        }

        public void setClickListener(View.OnClickListener onClickListener) {
            this.mView.setOnClickListener(onClickListener);
        }

        public void setHighlighted(boolean z) {
            this.mView.setBackgroundColor(z ? this.mHighlightColor : 0);
        }
    }
}
