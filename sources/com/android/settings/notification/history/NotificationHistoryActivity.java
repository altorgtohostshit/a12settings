package com.android.settings.notification.history;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.INotificationManager;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.Slog;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.widget.NotificationExpandButton;
import com.android.settings.R;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.notification.history.HistoryLoader;
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.MainSwitchBar;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class NotificationHistoryActivity extends CollapsingToolbarBaseActivity {
    /* access modifiers changed from: private */
    public static String TAG = "NotifHistory";
    private Future mCountdownFuture;
    /* access modifiers changed from: private */
    public CountDownLatch mCountdownLatch;
    /* access modifiers changed from: private */
    public ViewGroup mDismissView;
    private ViewGroup mHistoryEmpty;
    private HistoryLoader mHistoryLoader;
    private ViewGroup mHistoryOff;
    private ViewGroup mHistoryOn;
    private final NotificationListenerService mListener = new NotificationListenerService() {
        private RecyclerView mDismissedRv;
        private RecyclerView mSnoozedRv;

        public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        }

        /* JADX WARNING: Removed duplicated region for block: B:13:0x0070  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0085  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x00d1  */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x00ef  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onListenerConnected() {
            /*
                r12 = this;
                r0 = 0
                r1 = 0
                android.service.notification.StatusBarNotification[] r2 = r12.getSnoozedNotifications()     // Catch:{ RemoteException | SecurityException -> 0x001e }
                com.android.settings.notification.history.NotificationHistoryActivity r3 = com.android.settings.notification.history.NotificationHistoryActivity.this     // Catch:{ RemoteException | SecurityException -> 0x001f }
                android.app.INotificationManager r3 = r3.mNm     // Catch:{ RemoteException | SecurityException -> 0x001f }
                com.android.settings.notification.history.NotificationHistoryActivity r4 = com.android.settings.notification.history.NotificationHistoryActivity.this     // Catch:{ RemoteException | SecurityException -> 0x001f }
                java.lang.String r4 = r4.getPackageName()     // Catch:{ RemoteException | SecurityException -> 0x001f }
                com.android.settings.notification.history.NotificationHistoryActivity r5 = com.android.settings.notification.history.NotificationHistoryActivity.this     // Catch:{ RemoteException | SecurityException -> 0x001f }
                java.lang.String r5 = r5.getAttributionTag()     // Catch:{ RemoteException | SecurityException -> 0x001f }
                r6 = 6
                android.service.notification.StatusBarNotification[] r0 = r3.getHistoricalNotificationsWithAttribution(r4, r5, r6, r1)     // Catch:{ RemoteException | SecurityException -> 0x001f }
                goto L_0x0028
            L_0x001e:
                r2 = r0
            L_0x001f:
                java.lang.String r3 = com.android.settings.notification.history.NotificationHistoryActivity.TAG
                java.lang.String r4 = "OnPaused called while trying to retrieve notifications"
                android.util.Log.d(r3, r4)
            L_0x0028:
                com.android.settings.notification.history.NotificationHistoryActivity r3 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.view.ViewGroup r3 = r3.mSnoozeView
                r4 = 2131559363(0x7f0d03c3, float:1.8744068E38)
                android.view.View r3 = r3.findViewById(r4)
                androidx.recyclerview.widget.RecyclerView r3 = (androidx.recyclerview.widget.RecyclerView) r3
                r12.mSnoozedRv = r3
                androidx.recyclerview.widget.LinearLayoutManager r3 = new androidx.recyclerview.widget.LinearLayoutManager
                com.android.settings.notification.history.NotificationHistoryActivity r5 = com.android.settings.notification.history.NotificationHistoryActivity.this
                r3.<init>(r5)
                androidx.recyclerview.widget.RecyclerView r5 = r12.mSnoozedRv
                r5.setLayoutManager(r3)
                androidx.recyclerview.widget.RecyclerView r3 = r12.mSnoozedRv
                com.android.settings.notification.history.NotificationSbnAdapter r11 = new com.android.settings.notification.history.NotificationSbnAdapter
                com.android.settings.notification.history.NotificationHistoryActivity r6 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.content.pm.PackageManager r7 = r6.mPm
                com.android.settings.notification.history.NotificationHistoryActivity r5 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.os.UserManager r8 = r5.mUm
                r9 = 1
                com.android.settings.notification.history.NotificationHistoryActivity r5 = com.android.settings.notification.history.NotificationHistoryActivity.this
                com.android.internal.logging.UiEventLogger r10 = r5.mUiEventLogger
                r5 = r11
                r5.<init>(r6, r7, r8, r9, r10)
                r3.setAdapter(r11)
                androidx.recyclerview.widget.RecyclerView r3 = r12.mSnoozedRv
                r3.setNestedScrollingEnabled(r1)
                r3 = 8
                if (r2 == 0) goto L_0x0085
                int r5 = r2.length
                if (r5 != 0) goto L_0x0070
                goto L_0x0085
            L_0x0070:
                androidx.recyclerview.widget.RecyclerView r5 = r12.mSnoozedRv
                androidx.recyclerview.widget.RecyclerView$Adapter r5 = r5.getAdapter()
                com.android.settings.notification.history.NotificationSbnAdapter r5 = (com.android.settings.notification.history.NotificationSbnAdapter) r5
                java.util.ArrayList r6 = new java.util.ArrayList
                java.util.List r2 = java.util.Arrays.asList(r2)
                r6.<init>(r2)
                r5.onRebuildComplete(r6)
                goto L_0x008e
            L_0x0085:
                com.android.settings.notification.history.NotificationHistoryActivity r2 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.view.ViewGroup r2 = r2.mSnoozeView
                r2.setVisibility(r3)
            L_0x008e:
                com.android.settings.notification.history.NotificationHistoryActivity r2 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.view.ViewGroup r2 = r2.mDismissView
                android.view.View r2 = r2.findViewById(r4)
                androidx.recyclerview.widget.RecyclerView r2 = (androidx.recyclerview.widget.RecyclerView) r2
                r12.mDismissedRv = r2
                androidx.recyclerview.widget.LinearLayoutManager r2 = new androidx.recyclerview.widget.LinearLayoutManager
                com.android.settings.notification.history.NotificationHistoryActivity r4 = com.android.settings.notification.history.NotificationHistoryActivity.this
                r2.<init>(r4)
                androidx.recyclerview.widget.RecyclerView r4 = r12.mDismissedRv
                r4.setLayoutManager(r2)
                androidx.recyclerview.widget.RecyclerView r2 = r12.mDismissedRv
                com.android.settings.notification.history.NotificationSbnAdapter r10 = new com.android.settings.notification.history.NotificationSbnAdapter
                com.android.settings.notification.history.NotificationHistoryActivity r5 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.content.pm.PackageManager r6 = r5.mPm
                com.android.settings.notification.history.NotificationHistoryActivity r4 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.os.UserManager r7 = r4.mUm
                r8 = 0
                com.android.settings.notification.history.NotificationHistoryActivity r4 = com.android.settings.notification.history.NotificationHistoryActivity.this
                com.android.internal.logging.UiEventLogger r9 = r4.mUiEventLogger
                r4 = r10
                r4.<init>(r5, r6, r7, r8, r9)
                r2.setAdapter(r10)
                androidx.recyclerview.widget.RecyclerView r2 = r12.mDismissedRv
                r2.setNestedScrollingEnabled(r1)
                if (r0 == 0) goto L_0x00ef
                int r2 = r0.length
                if (r2 != 0) goto L_0x00d1
                goto L_0x00ef
            L_0x00d1:
                com.android.settings.notification.history.NotificationHistoryActivity r2 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.view.ViewGroup r2 = r2.mDismissView
                r2.setVisibility(r1)
                androidx.recyclerview.widget.RecyclerView r1 = r12.mDismissedRv
                androidx.recyclerview.widget.RecyclerView$Adapter r1 = r1.getAdapter()
                com.android.settings.notification.history.NotificationSbnAdapter r1 = (com.android.settings.notification.history.NotificationSbnAdapter) r1
                java.util.ArrayList r2 = new java.util.ArrayList
                java.util.List r0 = java.util.Arrays.asList(r0)
                r2.<init>(r0)
                r1.onRebuildComplete(r2)
                goto L_0x00f8
            L_0x00ef:
                com.android.settings.notification.history.NotificationHistoryActivity r0 = com.android.settings.notification.history.NotificationHistoryActivity.this
                android.view.ViewGroup r0 = r0.mDismissView
                r0.setVisibility(r3)
            L_0x00f8:
                com.android.settings.notification.history.NotificationHistoryActivity r12 = com.android.settings.notification.history.NotificationHistoryActivity.this
                java.util.concurrent.CountDownLatch r12 = r12.mCountdownLatch
                r12.countDown()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.history.NotificationHistoryActivity.C11162.onListenerConnected():void");
        }

        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            if (i == 18) {
                ((NotificationSbnAdapter) this.mSnoozedRv.getAdapter()).addSbn(statusBarNotification);
                NotificationHistoryActivity.this.mSnoozeView.setVisibility(0);
                return;
            }
            ((NotificationSbnAdapter) this.mDismissedRv.getAdapter()).addSbn(statusBarNotification);
            NotificationHistoryActivity.this.mDismissView.setVisibility(0);
        }
    };
    /* access modifiers changed from: private */
    public INotificationManager mNm;
    private HistoryLoader.OnHistoryLoaderListener mOnHistoryLoaderListener = new NotificationHistoryActivity$$ExternalSyntheticLambda1(this);
    private final OnMainSwitchChangeListener mOnSwitchClickListener = new NotificationHistoryActivity$$ExternalSyntheticLambda3(this);
    private final ViewOutlineProvider mOutlineProvider = new ViewOutlineProvider() {
        public void getOutline(View view, Outline outline) {
            TypedArray obtainStyledAttributes = NotificationHistoryActivity.this.obtainStyledAttributes(new int[]{16844145});
            float dimension = obtainStyledAttributes.getDimension(0, 0.0f);
            obtainStyledAttributes.recycle();
            TypedValue typedValue = new TypedValue();
            NotificationHistoryActivity.this.getTheme().resolveAttribute(16843284, typedValue, true);
            Outline outline2 = outline;
            outline2.setRoundRect(0, 0, view.getWidth(), view.getHeight() - NotificationHistoryActivity.this.getDrawable(typedValue.resourceId).getIntrinsicHeight(), dimension);
        }
    };
    /* access modifiers changed from: private */
    public PackageManager mPm;
    /* access modifiers changed from: private */
    public ViewGroup mSnoozeView;
    private MainSwitchBar mSwitchBar;
    private ViewGroup mTodayView;
    /* access modifiers changed from: private */
    public UiEventLogger mUiEventLogger = new UiEventLoggerImpl();
    /* access modifiers changed from: private */
    public UserManager mUm;

    enum NotificationHistoryEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_HISTORY_ON(504),
        NOTIFICATION_HISTORY_OFF(505),
        NOTIFICATION_HISTORY_OPEN(506),
        NOTIFICATION_HISTORY_CLOSE(507),
        NOTIFICATION_HISTORY_RECENT_ITEM_CLICK(508),
        NOTIFICATION_HISTORY_SNOOZED_ITEM_CLICK(509),
        NOTIFICATION_HISTORY_PACKAGE_HISTORY_OPEN(510),
        NOTIFICATION_HISTORY_PACKAGE_HISTORY_CLOSE(511),
        NOTIFICATION_HISTORY_OLDER_ITEM_CLICK(512),
        NOTIFICATION_HISTORY_OLDER_ITEM_DELETE(513);
        
        private int mId;

        private NotificationHistoryEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(List list) {
        String str;
        int i = 8;
        boolean z = false;
        findViewById(R.id.today_list).setVisibility(list.isEmpty() ? 8 : 0);
        this.mCountdownLatch.countDown();
        this.mTodayView.findViewById(R.id.apps).setClipToOutline(true);
        this.mTodayView.setOutlineProvider(this.mOutlineProvider);
        int size = list.size();
        int i2 = 0;
        while (i2 < size) {
            NotificationHistoryPackage notificationHistoryPackage = (NotificationHistoryPackage) list.get(i2);
            View inflate = LayoutInflater.from(this).inflate(R.layout.notification_history_app_layout, (ViewGroup) null);
            View findViewById = inflate.findViewById(R.id.notification_list);
            findViewById.setVisibility(i);
            View findViewById2 = inflate.findViewById(R.id.app_header);
            NotificationExpandButton findViewById3 = inflate.findViewById(16908954);
            int obtainThemeColor = obtainThemeColor(16842806);
            findViewById3.setDefaultPillColor(obtainThemeColor(16844002));
            findViewById3.setDefaultTextColor(obtainThemeColor);
            findViewById3.setExpanded(z);
            if (findViewById.getVisibility() == 0) {
                str = getString(R.string.condition_expand_hide);
            } else {
                str = getString(R.string.condition_expand_show);
            }
            findViewById2.setStateDescription(str);
            NotificationHistoryActivity$$ExternalSyntheticLambda0 notificationHistoryActivity$$ExternalSyntheticLambda0 = r0;
            NotificationHistoryActivity$$ExternalSyntheticLambda0 notificationHistoryActivity$$ExternalSyntheticLambda02 = new NotificationHistoryActivity$$ExternalSyntheticLambda0(this, findViewById, findViewById3, findViewById2, notificationHistoryPackage, i2);
            findViewById2.setOnClickListener(notificationHistoryActivity$$ExternalSyntheticLambda0);
            TextView textView = (TextView) inflate.findViewById(R.id.label);
            CharSequence charSequence = notificationHistoryPackage.label;
            if (charSequence == null) {
                charSequence = notificationHistoryPackage.pkgName;
            }
            textView.setText(charSequence);
            textView.setContentDescription(this.mUm.getBadgedLabelForUser(textView.getText(), UserHandle.getUserHandleForUid(notificationHistoryPackage.uid)));
            ((ImageView) inflate.findViewById(R.id.icon)).setImageDrawable(notificationHistoryPackage.icon);
            TextView textView2 = (TextView) inflate.findViewById(R.id.count);
            textView2.setText(getResources().getQuantityString(R.plurals.notification_history_count, notificationHistoryPackage.notifications.size(), new Object[]{Integer.valueOf(notificationHistoryPackage.notifications.size())}));
            NotificationHistoryRecyclerView notificationHistoryRecyclerView = (NotificationHistoryRecyclerView) inflate.findViewById(R.id.notification_list);
            notificationHistoryRecyclerView.setAdapter(new NotificationHistoryAdapter(this.mNm, notificationHistoryRecyclerView, new NotificationHistoryActivity$$ExternalSyntheticLambda2(this, textView2, inflate), this.mUiEventLogger));
            ((NotificationHistoryAdapter) notificationHistoryRecyclerView.getAdapter()).onRebuildComplete(new ArrayList(notificationHistoryPackage.notifications));
            this.mTodayView.addView(inflate);
            i2++;
            z = false;
            i = 8;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, NotificationExpandButton notificationExpandButton, View view2, NotificationHistoryPackage notificationHistoryPackage, int i, View view3) {
        String str;
        NotificationHistoryEvent notificationHistoryEvent;
        boolean z = false;
        view.setVisibility(view.getVisibility() == 0 ? 8 : 0);
        if (view.getVisibility() == 0) {
            z = true;
        }
        notificationExpandButton.setExpanded(z);
        if (view.getVisibility() == 0) {
            str = getString(R.string.condition_expand_hide);
        } else {
            str = getString(R.string.condition_expand_show);
        }
        view2.setStateDescription(str);
        view2.sendAccessibilityEvent(32768);
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        if (view.getVisibility() == 0) {
            notificationHistoryEvent = NotificationHistoryEvent.NOTIFICATION_HISTORY_PACKAGE_HISTORY_OPEN;
        } else {
            notificationHistoryEvent = NotificationHistoryEvent.NOTIFICATION_HISTORY_PACKAGE_HISTORY_CLOSE;
        }
        uiEventLogger.logWithPosition(notificationHistoryEvent, notificationHistoryPackage.uid, notificationHistoryPackage.pkgName, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(TextView textView, View view, int i) {
        textView.setText(getResources().getQuantityString(R.plurals.notification_history_count, i, new Object[]{Integer.valueOf(i)}));
        if (i == 0) {
            view.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle((int) R.string.notification_history);
        setContentView((int) R.layout.notification_history);
        this.mTodayView = (ViewGroup) findViewById(R.id.apps);
        this.mSnoozeView = (ViewGroup) findViewById(R.id.snoozed_list);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.recently_dismissed_list);
        this.mDismissView = viewGroup;
        View findViewById = viewGroup.findViewById(R.id.notification_list);
        findViewById.setClipToOutline(true);
        findViewById.setOutlineProvider(this.mOutlineProvider);
        this.mHistoryOff = (ViewGroup) findViewById(R.id.history_off);
        this.mHistoryOn = (ViewGroup) findViewById(R.id.history_on);
        this.mHistoryEmpty = (ViewGroup) findViewById(R.id.history_on_empty);
        this.mSwitchBar = (MainSwitchBar) findViewById(R.id.main_switch_bar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mPm = getPackageManager();
        this.mUm = (UserManager) getSystemService(UserManager.class);
        this.mCountdownLatch = new CountDownLatch(2);
        this.mTodayView.removeAllViews();
        HistoryLoader historyLoader = new HistoryLoader(this, new NotificationBackend(), this.mPm);
        this.mHistoryLoader = historyLoader;
        historyLoader.load(this.mOnHistoryLoaderListener);
        this.mNm = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        try {
            this.mListener.registerAsSystemService(this, new ComponentName(getPackageName(), getClass().getCanonicalName()), ActivityManager.getCurrentUser());
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot register listener", e);
        }
        bindSwitch();
        this.mCountdownFuture = ThreadUtils.postOnBackgroundThread((Runnable) new NotificationHistoryActivity$$ExternalSyntheticLambda4(this));
        this.mUiEventLogger.log(NotificationHistoryEvent.NOTIFICATION_HISTORY_OPEN);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$4() {
        try {
            this.mCountdownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Slog.e(TAG, "timed out waiting for loading", e);
        }
        ThreadUtils.postOnMainThread(new NotificationHistoryActivity$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$3() {
        if (this.mSwitchBar.isChecked() && findViewById(R.id.today_list).getVisibility() == 8 && this.mSnoozeView.getVisibility() == 8 && this.mDismissView.getVisibility() == 8) {
            this.mHistoryOn.setVisibility(8);
            this.mHistoryEmpty.setVisibility(0);
        }
    }

    public void onPause() {
        try {
            this.mListener.unregisterAsSystemService();
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot unregister listener", e);
        }
        this.mUiEventLogger.log(NotificationHistoryEvent.NOTIFICATION_HISTORY_CLOSE);
        super.onPause();
    }

    public void onDestroy() {
        Future future = this.mCountdownFuture;
        if (future != null) {
            future.cancel(true);
        }
        super.onDestroy();
    }

    public boolean onNavigateUp() {
        finish();
        return true;
    }

    private int obtainThemeColor(int i) {
        int i2 = 0;
        TypedArray obtainStyledAttributes = new ContextThemeWrapper(this, 16974563).getTheme().obtainStyledAttributes(new int[]{i});
        if (obtainStyledAttributes != null) {
            try {
                i2 = obtainStyledAttributes.getColor(0, 0);
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
        if (obtainStyledAttributes != null) {
            obtainStyledAttributes.close();
        }
        return i2;
        throw th;
    }

    private void bindSwitch() {
        MainSwitchBar mainSwitchBar = this.mSwitchBar;
        if (mainSwitchBar != null) {
            mainSwitchBar.show();
            this.mSwitchBar.setTitle(getString(R.string.notification_history_toggle));
            try {
                this.mSwitchBar.addOnSwitchChangeListener(this.mOnSwitchClickListener);
            } catch (IllegalStateException unused) {
            }
            MainSwitchBar mainSwitchBar2 = this.mSwitchBar;
            boolean z = false;
            if (Settings.Secure.getInt(getContentResolver(), "notification_history_enabled", 0) == 1) {
                z = true;
            }
            mainSwitchBar2.setChecked(z);
            toggleViews(this.mSwitchBar.isChecked());
        }
    }

    private void toggleViews(boolean z) {
        if (z) {
            this.mHistoryOff.setVisibility(8);
            this.mHistoryOn.setVisibility(0);
        } else {
            this.mHistoryOn.setVisibility(8);
            this.mHistoryOff.setVisibility(0);
            this.mTodayView.removeAllViews();
        }
        this.mHistoryEmpty.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(Switch switchR, boolean z) {
        int i;
        NotificationHistoryEvent notificationHistoryEvent;
        try {
            i = Settings.Secure.getInt(getContentResolver(), "notification_history_enabled");
        } catch (Settings.SettingNotFoundException unused) {
            i = 0;
        }
        if (i != z) {
            Settings.Secure.putInt(getContentResolver(), "notification_history_enabled", z ? 1 : 0);
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            if (z) {
                notificationHistoryEvent = NotificationHistoryEvent.NOTIFICATION_HISTORY_ON;
            } else {
                notificationHistoryEvent = NotificationHistoryEvent.NOTIFICATION_HISTORY_OFF;
            }
            uiEventLogger.log(notificationHistoryEvent);
            Log.d(TAG, "onSwitchChange history to " + z);
        }
        this.mHistoryOn.setVisibility(8);
        if (z) {
            this.mHistoryEmpty.setVisibility(0);
            this.mHistoryOff.setVisibility(8);
        } else {
            this.mHistoryOff.setVisibility(0);
            this.mHistoryEmpty.setVisibility(8);
        }
        this.mTodayView.removeAllViews();
    }
}
