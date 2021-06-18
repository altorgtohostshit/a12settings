package com.android.settings.notification.history;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Slog;
import android.view.View;
import android.widget.DateTimeView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;
import com.android.settings.R;
import com.android.settings.notification.history.NotificationHistoryActivity;

public class NotificationSbnViewHolder extends RecyclerView.ViewHolder {
    private final View mDivider;
    private final ImageView mIcon;
    private final TextView mPkgName;
    private final ImageView mProfileBadge;
    private final TextView mSummary;
    private final DateTimeView mTime;
    private final TextView mTitle;

    NotificationSbnViewHolder(View view) {
        super(view);
        this.mPkgName = (TextView) view.findViewById(R.id.pkgname);
        this.mIcon = (ImageView) view.findViewById(R.id.icon);
        this.mTime = view.findViewById(R.id.timestamp);
        this.mTitle = (TextView) view.findViewById(R.id.title);
        this.mSummary = (TextView) view.findViewById(R.id.text);
        this.mProfileBadge = (ImageView) view.findViewById(R.id.profile_badge);
        this.mDivider = view.findViewById(R.id.divider);
    }

    /* access modifiers changed from: package-private */
    public void setSummary(CharSequence charSequence) {
        this.mSummary.setVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
        this.mSummary.setText(charSequence);
    }

    /* access modifiers changed from: package-private */
    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mTitle.setText(charSequence);
        }
    }

    /* access modifiers changed from: package-private */
    public void setIcon(Drawable drawable) {
        this.mIcon.setImageDrawable(drawable);
    }

    /* access modifiers changed from: package-private */
    public void setIconBackground(Drawable drawable) {
        this.mIcon.setBackground(drawable);
    }

    /* access modifiers changed from: package-private */
    public void setPackageLabel(String str) {
        this.mPkgName.setText(str);
    }

    /* access modifiers changed from: package-private */
    public void setPostedTime(long j) {
        this.mTime.setTime(j);
    }

    /* access modifiers changed from: package-private */
    public void setProfileBadge(Drawable drawable) {
        this.mProfileBadge.setImageDrawable(drawable);
        this.mProfileBadge.setVisibility(drawable != null ? 0 : 8);
    }

    /* access modifiers changed from: package-private */
    public void setDividerVisible(boolean z) {
        this.mDivider.setVisibility(z ? 0 : 8);
    }

    /* access modifiers changed from: package-private */
    public void addOnClick(int i, String str, int i2, int i3, PendingIntent pendingIntent, InstanceId instanceId, boolean z, UiEventLogger uiEventLogger) {
        Intent launchIntentForPackage = this.itemView.getContext().getPackageManager().getLaunchIntentForPackage(str);
        boolean z2 = false;
        if (!(pendingIntent == null || PendingIntent.getActivity(this.itemView.getContext(), 0, pendingIntent.getIntent(), 603979776) == null)) {
            z2 = true;
        }
        boolean z3 = z2;
        if (z3 || launchIntentForPackage != null) {
            this.itemView.setOnClickListener(new NotificationSbnViewHolder$$ExternalSyntheticLambda0(this, uiEventLogger, z, i2, str, instanceId, i, pendingIntent, z3, launchIntentForPackage, i3));
            ViewCompat.setAccessibilityDelegate(this.itemView, new AccessibilityDelegateCompat() {
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                    accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, view.getResources().getText(R.string.notification_history_open_notification)));
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addOnClick$0(UiEventLogger uiEventLogger, boolean z, int i, String str, InstanceId instanceId, int i2, PendingIntent pendingIntent, boolean z2, Intent intent, int i3, View view) {
        UiEventLogger.UiEventEnum uiEventEnum;
        Intent intent2 = intent;
        if (z) {
            uiEventEnum = NotificationHistoryActivity.NotificationHistoryEvent.NOTIFICATION_HISTORY_SNOOZED_ITEM_CLICK;
        } else {
            uiEventEnum = NotificationHistoryActivity.NotificationHistoryEvent.NOTIFICATION_HISTORY_RECENT_ITEM_CLICK;
        }
        uiEventLogger.logWithInstanceIdAndPosition(uiEventEnum, i, str, instanceId, i2);
        if (pendingIntent != null && z2) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Slog.e("SbnViewHolder", "Could not launch", e);
            }
        } else if (intent2 != null) {
            intent2.addFlags(268435456);
            try {
                this.itemView.getContext().startActivityAsUser(intent2, UserHandle.of(i3));
            } catch (ActivityNotFoundException e2) {
                Slog.e("SbnViewHolder", "no launch activity", e2);
            }
        }
    }
}
