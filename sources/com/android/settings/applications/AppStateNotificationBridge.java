package com.android.settings.applications;

import android.app.usage.IUsageStatsManager;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.utils.StringUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppStateNotificationBridge extends AppStateBaseBridge {
    public static final ApplicationsState.AppFilter FILTER_APP_NOTIFICATION_BLOCKED = new ApplicationsState.AppFilter() {
        public void init() {
        }

        public boolean filterApp(ApplicationsState.AppEntry appEntry) {
            NotificationsSentState access$000 = AppStateNotificationBridge.getNotificationsSentState(appEntry);
            if (access$000 != null) {
                return access$000.blocked;
            }
            return false;
        }
    };
    public static final ApplicationsState.AppFilter FILTER_APP_NOTIFICATION_FREQUENCY = new ApplicationsState.AppFilter() {
        public void init() {
        }

        public boolean filterApp(ApplicationsState.AppEntry appEntry) {
            NotificationsSentState access$000 = AppStateNotificationBridge.getNotificationsSentState(appEntry);
            if (access$000 == null || access$000.sentCount == 0) {
                return false;
            }
            return true;
        }
    };
    public static final ApplicationsState.AppFilter FILTER_APP_NOTIFICATION_RECENCY = new ApplicationsState.AppFilter() {
        public void init() {
        }

        public boolean filterApp(ApplicationsState.AppEntry appEntry) {
            NotificationsSentState access$000 = AppStateNotificationBridge.getNotificationsSentState(appEntry);
            if (access$000 == null || access$000.lastSent == 0) {
                return false;
            }
            return true;
        }
    };
    public static final Comparator<ApplicationsState.AppEntry> FREQUENCY_NOTIFICATION_COMPARATOR = new Comparator<ApplicationsState.AppEntry>() {
        public int compare(ApplicationsState.AppEntry appEntry, ApplicationsState.AppEntry appEntry2) {
            NotificationsSentState access$000 = AppStateNotificationBridge.getNotificationsSentState(appEntry);
            NotificationsSentState access$0002 = AppStateNotificationBridge.getNotificationsSentState(appEntry2);
            if (access$000 == null && access$0002 != null) {
                return -1;
            }
            if (access$000 != null && access$0002 == null) {
                return 1;
            }
            if (!(access$000 == null || access$0002 == null)) {
                int i = access$000.sentCount;
                int i2 = access$0002.sentCount;
                if (i < i2) {
                    return 1;
                }
                if (i > i2) {
                    return -1;
                }
            }
            return ApplicationsState.ALPHA_COMPARATOR.compare(appEntry, appEntry2);
        }
    };
    public static final Comparator<ApplicationsState.AppEntry> RECENT_NOTIFICATION_COMPARATOR = new Comparator<ApplicationsState.AppEntry>() {
        public int compare(ApplicationsState.AppEntry appEntry, ApplicationsState.AppEntry appEntry2) {
            NotificationsSentState access$000 = AppStateNotificationBridge.getNotificationsSentState(appEntry);
            NotificationsSentState access$0002 = AppStateNotificationBridge.getNotificationsSentState(appEntry2);
            if (access$000 == null && access$0002 != null) {
                return -1;
            }
            if (access$000 != null && access$0002 == null) {
                return 1;
            }
            if (!(access$000 == null || access$0002 == null)) {
                long j = access$000.lastSent;
                long j2 = access$0002.lastSent;
                if (j < j2) {
                    return 1;
                }
                if (j > j2) {
                    return -1;
                }
            }
            return ApplicationsState.ALPHA_COMPARATOR.compare(appEntry, appEntry2);
        }
    };
    private final boolean DEBUG = false;
    private final String TAG = "AppStateNotificationBridge";
    private NotificationBackend mBackend;
    private final Context mContext;
    private IUsageStatsManager mUsageStatsManager;
    protected List<Integer> mUserIds;

    public static class NotificationsSentState {
        public int avgSentDaily = 0;
        public int avgSentWeekly = 0;
        public boolean blockable;
        public boolean blocked;
        public long lastSent = 0;
        public int sentCount = 0;
        public boolean systemApp;
    }

    public AppStateNotificationBridge(Context context, ApplicationsState applicationsState, AppStateBaseBridge.Callback callback, IUsageStatsManager iUsageStatsManager, UserManager userManager, NotificationBackend notificationBackend) {
        super(applicationsState, callback);
        this.mContext = context;
        this.mUsageStatsManager = iUsageStatsManager;
        this.mBackend = notificationBackend;
        ArrayList arrayList = new ArrayList();
        this.mUserIds = arrayList;
        arrayList.add(Integer.valueOf(context.getUserId()));
        int managedProfileId = Utils.getManagedProfileId(userManager, context.getUserId());
        if (managedProfileId != -10000) {
            this.mUserIds.add(Integer.valueOf(managedProfileId));
        }
    }

    /* access modifiers changed from: protected */
    public void loadAllExtraInfo() {
        ArrayList<ApplicationsState.AppEntry> allApps = this.mAppSession.getAllApps();
        if (allApps != null) {
            Map<String, NotificationsSentState> aggregatedUsageEvents = getAggregatedUsageEvents();
            Iterator<ApplicationsState.AppEntry> it = allApps.iterator();
            while (it.hasNext()) {
                ApplicationsState.AppEntry next = it.next();
                NotificationsSentState notificationsSentState = aggregatedUsageEvents.get(getKey(UserHandle.getUserId(next.info.uid), next.info.packageName));
                if (notificationsSentState == null) {
                    notificationsSentState = new NotificationsSentState();
                }
                calculateAvgSentCounts(notificationsSentState);
                addBlockStatus(next, notificationsSentState);
                next.extraInfo = notificationsSentState;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateExtraInfo(ApplicationsState.AppEntry appEntry, String str, int i) {
        NotificationsSentState aggregatedUsageEvents = getAggregatedUsageEvents(UserHandle.getUserId(appEntry.info.uid), appEntry.info.packageName);
        calculateAvgSentCounts(aggregatedUsageEvents);
        addBlockStatus(appEntry, aggregatedUsageEvents);
        appEntry.extraInfo = aggregatedUsageEvents;
    }

    public static CharSequence getSummary(Context context, NotificationsSentState notificationsSentState, int i) {
        if (i == R.id.sort_order_recent_notification) {
            if (notificationsSentState.lastSent == 0) {
                return context.getString(R.string.notifications_sent_never);
            }
            return StringUtil.formatRelativeTime(context, (double) (System.currentTimeMillis() - notificationsSentState.lastSent), true);
        } else if (i != R.id.sort_order_frequent_notification) {
            return "";
        } else {
            if (notificationsSentState.avgSentDaily > 0) {
                Resources resources = context.getResources();
                int i2 = notificationsSentState.avgSentDaily;
                return resources.getQuantityString(R.plurals.notifications_sent_daily, i2, new Object[]{Integer.valueOf(i2)});
            }
            Resources resources2 = context.getResources();
            int i3 = notificationsSentState.avgSentWeekly;
            return resources2.getQuantityString(R.plurals.notifications_sent_weekly, i3, new Object[]{Integer.valueOf(i3)});
        }
    }

    private void addBlockStatus(ApplicationsState.AppEntry appEntry, NotificationsSentState notificationsSentState) {
        if (notificationsSentState != null) {
            NotificationBackend notificationBackend = this.mBackend;
            ApplicationInfo applicationInfo = appEntry.info;
            notificationsSentState.blocked = notificationBackend.getNotificationsBanned(applicationInfo.packageName, applicationInfo.uid);
            boolean isSystemApp = this.mBackend.isSystemApp(this.mContext, appEntry.info);
            notificationsSentState.systemApp = isSystemApp;
            notificationsSentState.blockable = !isSystemApp || (isSystemApp && notificationsSentState.blocked);
        }
    }

    private void calculateAvgSentCounts(NotificationsSentState notificationsSentState) {
        if (notificationsSentState != null) {
            notificationsSentState.avgSentDaily = Math.round(((float) notificationsSentState.sentCount) / 7.0f);
            int i = notificationsSentState.sentCount;
            if (i < 7) {
                notificationsSentState.avgSentWeekly = i;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, NotificationsSentState> getAggregatedUsageEvents() {
        ArrayMap arrayMap = new ArrayMap();
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - 604800000;
        for (Integer intValue : this.mUserIds) {
            int intValue2 = intValue.intValue();
            UsageEvents usageEvents = null;
            try {
                usageEvents = this.mUsageStatsManager.queryEventsForUser(j, currentTimeMillis, intValue2, this.mContext.getPackageName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (usageEvents != null) {
                UsageEvents.Event event = new UsageEvents.Event();
                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(event);
                    NotificationsSentState notificationsSentState = (NotificationsSentState) arrayMap.get(getKey(intValue2, event.getPackageName()));
                    if (notificationsSentState == null) {
                        notificationsSentState = new NotificationsSentState();
                        arrayMap.put(getKey(intValue2, event.getPackageName()), notificationsSentState);
                    }
                    if (event.getEventType() == 12) {
                        if (event.getTimeStamp() > notificationsSentState.lastSent) {
                            notificationsSentState.lastSent = event.getTimeStamp();
                        }
                        notificationsSentState.sentCount++;
                    }
                }
            }
        }
        return arrayMap;
    }

    /* access modifiers changed from: protected */
    public NotificationsSentState getAggregatedUsageEvents(int i, String str) {
        UsageEvents usageEvents;
        long currentTimeMillis = System.currentTimeMillis();
        NotificationsSentState notificationsSentState = null;
        try {
            usageEvents = this.mUsageStatsManager.queryEventsForPackageForUser(currentTimeMillis - 604800000, currentTimeMillis, i, str, this.mContext.getPackageName());
        } catch (RemoteException e) {
            e.printStackTrace();
            usageEvents = null;
        }
        if (usageEvents != null) {
            UsageEvents.Event event = new UsageEvents.Event();
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == 12) {
                    if (notificationsSentState == null) {
                        notificationsSentState = new NotificationsSentState();
                    }
                    if (event.getTimeStamp() > notificationsSentState.lastSent) {
                        notificationsSentState.lastSent = event.getTimeStamp();
                    }
                    notificationsSentState.sentCount++;
                }
            }
        }
        return notificationsSentState;
    }

    /* access modifiers changed from: private */
    public static NotificationsSentState getNotificationsSentState(ApplicationsState.AppEntry appEntry) {
        Object obj;
        if (appEntry == null || (obj = appEntry.extraInfo) == null || !(obj instanceof NotificationsSentState)) {
            return null;
        }
        return (NotificationsSentState) obj;
    }

    protected static String getKey(int i, String str) {
        return i + "|" + str;
    }

    public View.OnClickListener getSwitchOnClickListener(ApplicationsState.AppEntry appEntry) {
        if (appEntry != null) {
            return new AppStateNotificationBridge$$ExternalSyntheticLambda0(this, appEntry);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSwitchOnClickListener$0(ApplicationsState.AppEntry appEntry, View view) {
        Switch switchR = (Switch) ((ViewGroup) view).findViewById(R.id.switchWidget);
        if (switchR != null && switchR.isEnabled()) {
            switchR.toggle();
            NotificationBackend notificationBackend = this.mBackend;
            ApplicationInfo applicationInfo = appEntry.info;
            notificationBackend.setNotificationsEnabledForPackage(applicationInfo.packageName, applicationInfo.uid, switchR.isChecked());
            NotificationsSentState notificationsSentState = getNotificationsSentState(appEntry);
            if (notificationsSentState != null) {
                notificationsSentState.blocked = !switchR.isChecked();
            }
        }
    }

    public static final boolean enableSwitch(ApplicationsState.AppEntry appEntry) {
        NotificationsSentState notificationsSentState = getNotificationsSentState(appEntry);
        if (notificationsSentState == null) {
            return false;
        }
        return notificationsSentState.blockable;
    }

    public static final boolean checkSwitch(ApplicationsState.AppEntry appEntry) {
        NotificationsSentState notificationsSentState = getNotificationsSentState(appEntry);
        if (notificationsSentState == null) {
            return false;
        }
        return !notificationsSentState.blocked;
    }
}
