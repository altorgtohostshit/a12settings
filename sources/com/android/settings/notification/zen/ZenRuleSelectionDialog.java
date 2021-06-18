package com.android.settings.notification.zen;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.ZenModeConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.utils.ZenServiceListing;
import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class ZenRuleSelectionDialog extends InstrumentedDialogFragment {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = ZenModeSettingsBase.DEBUG;
    /* access modifiers changed from: private */
    public static final Comparator<ZenRuleInfo> RULE_TYPE_COMPARATOR = new Comparator<ZenRuleInfo>() {
        private final Collator mCollator = Collator.getInstance();

        public int compare(ZenRuleInfo zenRuleInfo, ZenRuleInfo zenRuleInfo2) {
            int compare = this.mCollator.compare(zenRuleInfo.packageLabel, zenRuleInfo2.packageLabel);
            if (compare != 0) {
                return compare;
            }
            return this.mCollator.compare(zenRuleInfo.title, zenRuleInfo2.title);
        }
    };
    private static Context mContext;
    /* access modifiers changed from: private */
    public static NotificationManager mNm;
    /* access modifiers changed from: private */
    public static PackageManager mPm;
    protected static PositiveClickListener mPositiveClickListener;
    private static ZenServiceListing mServiceListing;
    private LinearLayout mRuleContainer;
    private final ZenServiceListing.Callback mServiceListingCallback = new ZenServiceListing.Callback() {
        public void onComponentsReloaded(Set<ComponentInfo> set) {
            if (ZenRuleSelectionDialog.DEBUG) {
                Log.d("ZenRuleSelectionDialog", "Reloaded: count=" + set.size());
            }
            TreeSet treeSet = new TreeSet(ZenRuleSelectionDialog.RULE_TYPE_COMPARATOR);
            for (ComponentInfo next : set) {
                ZenRuleInfo ruleInfo = AbstractZenModeAutomaticRulePreferenceController.getRuleInfo(ZenRuleSelectionDialog.mPm, next);
                if (!(ruleInfo == null || ruleInfo.configurationActivity == null || !ZenRuleSelectionDialog.mNm.isNotificationPolicyAccessGrantedForPackage(ruleInfo.packageName))) {
                    int i = ruleInfo.ruleInstanceLimit;
                    if (i <= 0 || i >= ZenRuleSelectionDialog.mNm.getRuleInstanceCount(next.getComponentName()) + 1) {
                        treeSet.add(ruleInfo);
                    }
                }
            }
            ZenRuleSelectionDialog.this.bindExternalRules(treeSet);
        }
    };

    public interface PositiveClickListener {
        void onExternalRuleSelected(ZenRuleInfo zenRuleInfo, Fragment fragment);

        void onSystemRuleSelected(ZenRuleInfo zenRuleInfo, Fragment fragment);
    }

    public int getMetricsCategory() {
        return 1270;
    }

    public static void show(Context context, Fragment fragment, PositiveClickListener positiveClickListener, ZenServiceListing zenServiceListing) {
        mPositiveClickListener = positiveClickListener;
        mContext = context;
        mPm = context.getPackageManager();
        mNm = (NotificationManager) mContext.getSystemService("notification");
        mServiceListing = zenServiceListing;
        ZenRuleSelectionDialog zenRuleSelectionDialog = new ZenRuleSelectionDialog();
        zenRuleSelectionDialog.setTargetFragment(fragment, 0);
        zenRuleSelectionDialog.show(fragment.getFragmentManager(), "ZenRuleSelectionDialog");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.zen_rule_type_selection, (ViewGroup) null, false);
        this.mRuleContainer = (LinearLayout) inflate.findViewById(R.id.rule_container);
        if (mServiceListing != null) {
            bindType(defaultNewEvent());
            bindType(defaultNewSchedule());
            mServiceListing.addZenCallback(this.mServiceListingCallback);
            mServiceListing.reloadApprovedServices();
        }
        return new AlertDialog.Builder(getContext()).setTitle((int) R.string.zen_mode_choose_rule_type).setView(inflate).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ZenServiceListing zenServiceListing = mServiceListing;
        if (zenServiceListing != null) {
            zenServiceListing.removeZenCallback(this.mServiceListingCallback);
        }
    }

    private void bindType(final ZenRuleInfo zenRuleInfo) {
        try {
            ApplicationInfo applicationInfo = mPm.getApplicationInfo(zenRuleInfo.packageName, 0);
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.zen_rule_type, (ViewGroup) null, false);
            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.icon);
            ((TextView) linearLayout.findViewById(R.id.title)).setText(zenRuleInfo.title);
            if (!zenRuleInfo.isSystem) {
                new LoadIconTask(imageView).execute(new ApplicationInfo[]{applicationInfo});
                TextView textView = (TextView) linearLayout.findViewById(R.id.subtitle);
                textView.setText(applicationInfo.loadLabel(mPm));
                textView.setVisibility(0);
            } else if (ZenModeConfig.isValidScheduleConditionId(zenRuleInfo.defaultConditionId)) {
                imageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_timelapse));
            } else if (ZenModeConfig.isValidEventConditionId(zenRuleInfo.defaultConditionId)) {
                imageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_event));
            }
            linearLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ZenRuleSelectionDialog.this.dismiss();
                    ZenRuleInfo zenRuleInfo = zenRuleInfo;
                    if (zenRuleInfo.isSystem) {
                        ZenRuleSelectionDialog.mPositiveClickListener.onSystemRuleSelected(zenRuleInfo, ZenRuleSelectionDialog.this.getTargetFragment());
                    } else {
                        ZenRuleSelectionDialog.mPositiveClickListener.onExternalRuleSelected(zenRuleInfo, ZenRuleSelectionDialog.this.getTargetFragment());
                    }
                }
            });
            this.mRuleContainer.addView(linearLayout);
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    private ZenRuleInfo defaultNewSchedule() {
        ZenModeConfig.ScheduleInfo scheduleInfo = new ZenModeConfig.ScheduleInfo();
        scheduleInfo.days = ZenModeConfig.ALL_DAYS;
        scheduleInfo.startHour = 22;
        scheduleInfo.endHour = 7;
        ZenRuleInfo zenRuleInfo = new ZenRuleInfo();
        zenRuleInfo.settingsAction = "android.settings.ZEN_MODE_SCHEDULE_RULE_SETTINGS";
        zenRuleInfo.title = mContext.getString(R.string.zen_schedule_rule_type_name);
        zenRuleInfo.packageName = ZenModeConfig.getEventConditionProvider().getPackageName();
        zenRuleInfo.defaultConditionId = ZenModeConfig.toScheduleConditionId(scheduleInfo);
        zenRuleInfo.serviceComponent = ZenModeConfig.getScheduleConditionProvider();
        zenRuleInfo.isSystem = true;
        return zenRuleInfo;
    }

    private ZenRuleInfo defaultNewEvent() {
        ZenModeConfig.EventInfo eventInfo = new ZenModeConfig.EventInfo();
        eventInfo.calName = null;
        eventInfo.calendarId = null;
        eventInfo.reply = 0;
        ZenRuleInfo zenRuleInfo = new ZenRuleInfo();
        zenRuleInfo.settingsAction = "android.settings.ZEN_MODE_EVENT_RULE_SETTINGS";
        zenRuleInfo.title = mContext.getString(R.string.zen_event_rule_type_name);
        zenRuleInfo.packageName = ZenModeConfig.getScheduleConditionProvider().getPackageName();
        zenRuleInfo.defaultConditionId = ZenModeConfig.toEventConditionId(eventInfo);
        zenRuleInfo.serviceComponent = ZenModeConfig.getEventConditionProvider();
        zenRuleInfo.isSystem = true;
        return zenRuleInfo;
    }

    /* access modifiers changed from: private */
    public void bindExternalRules(Set<ZenRuleInfo> set) {
        for (ZenRuleInfo bindType : set) {
            bindType(bindType);
        }
    }

    private class LoadIconTask extends AsyncTask<ApplicationInfo, Void, Drawable> {
        private final WeakReference<ImageView> viewReference;

        public LoadIconTask(ImageView imageView) {
            this.viewReference = new WeakReference<>(imageView);
        }

        /* access modifiers changed from: protected */
        public Drawable doInBackground(ApplicationInfo... applicationInfoArr) {
            return applicationInfoArr[0].loadIcon(ZenRuleSelectionDialog.mPm);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Drawable drawable) {
            ImageView imageView;
            if (drawable != null && (imageView = (ImageView) this.viewReference.get()) != null) {
                imageView.setImageDrawable(drawable);
            }
        }
    }
}
