package com.android.settings.notification.app;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.role.RoleManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.notification.ConversationIconFactory;
import java.util.ArrayList;
import java.util.List;

public abstract class NotificationSettings extends DashboardFragment {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable("NotifiSettingsBase", 3);
    protected NotificationBackend.AppRow mAppRow;
    protected Bundle mArgs;
    protected NotificationBackend mBackend = new NotificationBackend();
    protected NotificationChannel mChannel;
    protected NotificationChannelGroup mChannelGroup;
    protected Context mContext;
    protected List<NotificationPreferenceController> mControllers = new ArrayList();
    protected Drawable mConversationDrawable;
    protected ShortcutInfo mConversationInfo;
    protected DependentFieldListener mDependentFieldListener = new DependentFieldListener();
    protected Intent mIntent;
    /* access modifiers changed from: private */
    public ViewGroup mLayoutView;
    protected boolean mListeningToPackageRemove;
    protected NotificationManager mNm;
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            NotificationSettings.this.animateIn();
            if (NotificationSettings.this.mLayoutView != null) {
                NotificationSettings.this.mLayoutView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    };
    protected final BroadcastReceiver mPackageRemovedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            PackageInfo packageInfo = NotificationSettings.this.mPkgInfo;
            if (packageInfo == null || TextUtils.equals(packageInfo.packageName, schemeSpecificPart)) {
                if (NotificationSettings.DEBUG) {
                    Log.d("NotifiSettingsBase", "Package (" + schemeSpecificPart + ") removed. RemovingNotificationSettingsBase.");
                }
                NotificationSettings.this.onPackageRemoved();
            }
        }
    };
    protected String mPkg;
    protected PackageInfo mPkgInfo;
    protected PackageManager mPm;
    protected List<String> mPreferenceFilter;
    protected RoleManager mRm;
    protected boolean mShowLegacyChannelConfig = false;
    protected RestrictedLockUtils.EnforcedAdmin mSuspendedAppsAdmin;
    protected int mUid;
    protected int mUserId;

    public void onAttach(Context context) {
        String str;
        int i;
        Class cls = ConversationHeaderPreferenceController.class;
        Class cls2 = HeaderPreferenceController.class;
        super.onAttach(context);
        this.mContext = getActivity();
        this.mIntent = getActivity().getIntent();
        this.mArgs = getArguments();
        this.mPm = getPackageManager();
        this.mNm = NotificationManager.from(this.mContext);
        this.mRm = (RoleManager) this.mContext.getSystemService(RoleManager.class);
        Bundle bundle = this.mArgs;
        if (bundle == null || !bundle.containsKey("package")) {
            str = this.mIntent.getStringExtra("android.provider.extra.APP_PACKAGE");
        } else {
            str = this.mArgs.getString("package");
        }
        this.mPkg = str;
        Bundle bundle2 = this.mArgs;
        if (bundle2 == null || !bundle2.containsKey("uid")) {
            i = this.mIntent.getIntExtra("app_uid", -1);
        } else {
            i = this.mArgs.getInt("uid");
        }
        this.mUid = i;
        if (i < 0) {
            try {
                this.mUid = this.mPm.getPackageUid(this.mPkg, 0);
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        PackageInfo findPackageInfo = findPackageInfo(this.mPkg, this.mUid);
        this.mPkgInfo = findPackageInfo;
        if (findPackageInfo != null) {
            int userId = UserHandle.getUserId(this.mUid);
            this.mUserId = userId;
            this.mSuspendedAppsAdmin = RestrictedLockUtilsInternal.checkIfApplicationIsSuspended(this.mContext, this.mPkg, userId);
            loadChannel();
            loadAppRow();
            loadChannelGroup();
            loadPreferencesFilter();
            collectConfigActivities();
            if (use(cls2) != null) {
                getSettingsLifecycle().addObserver((LifecycleObserver) use(cls2));
            }
            if (use(cls) != null) {
                getSettingsLifecycle().addObserver((LifecycleObserver) use(cls));
            }
            for (NotificationPreferenceController onResume : this.mControllers) {
                onResume.onResume(this.mAppRow, this.mChannel, this.mChannelGroup, (Drawable) null, (ShortcutInfo) null, this.mSuspendedAppsAdmin, this.mPreferenceFilter);
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mIntent == null && this.mArgs == null) {
            Log.w("NotifiSettingsBase", "No intent");
            toastAndFinish();
        } else if (this.mUid < 0 || TextUtils.isEmpty(this.mPkg) || this.mPkgInfo == null) {
            Log.w("NotifiSettingsBase", "Missing package or uid or packageinfo");
            toastAndFinish();
        } else {
            startListeningToPackageRemove();
        }
    }

    public void onDestroy() {
        stopListeningToPackageRemove();
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        if (this.mUid < 0 || TextUtils.isEmpty(this.mPkg) || this.mPkgInfo == null || this.mAppRow == null) {
            Log.w("NotifiSettingsBase", "Missing package or uid or packageinfo");
            finish();
            return;
        }
        loadAppRow();
        if (this.mAppRow == null) {
            Log.w("NotifiSettingsBase", "Can't load package");
            finish();
            return;
        }
        loadChannel();
        loadConversation();
        loadChannelGroup();
        loadPreferencesFilter();
        collectConfigActivities();
    }

    /* access modifiers changed from: protected */
    public void animatePanel() {
        if (this.mPreferenceFilter != null) {
            ViewGroup viewGroup = (ViewGroup) getActivity().findViewById(R.id.main_content);
            this.mLayoutView = viewGroup;
            viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        }
    }

    /* access modifiers changed from: private */
    public void animateIn() {
        ViewGroup viewGroup = this.mLayoutView;
        AnimatorSet buildAnimatorSet = buildAnimatorSet(viewGroup, (float) viewGroup.getHeight(), 0.0f, 0.0f, 1.0f, 250);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        buildAnimatorSet.play(valueAnimator);
        buildAnimatorSet.start();
    }

    private static AnimatorSet buildAnimatorSet(View view, float f, float f2, float f3, float f4, int i) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration((long) i);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, new float[]{f, f2}), ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{f3, f4})});
        return animatorSet;
    }

    private void loadPreferencesFilter() {
        Intent intent = getActivity().getIntent();
        this.mPreferenceFilter = intent != null ? intent.getStringArrayListExtra("android.provider.extra.CHANNEL_FILTER_LIST") : null;
    }

    private void loadChannel() {
        Intent intent = getActivity().getIntent();
        String str = null;
        String stringExtra = intent != null ? intent.getStringExtra("android.provider.extra.CHANNEL_ID") : null;
        if (stringExtra == null && intent != null) {
            Bundle bundleExtra = intent.getBundleExtra(":settings:show_fragment_args");
            stringExtra = bundleExtra != null ? bundleExtra.getString("android.provider.extra.CHANNEL_ID") : null;
        }
        if (intent != null) {
            str = intent.getStringExtra("android.provider.extra.CONVERSATION_ID");
        }
        this.mChannel = this.mBackend.getChannel(this.mPkg, this.mUid, stringExtra, str);
    }

    private void loadConversation() {
        NotificationChannel notificationChannel = this.mChannel;
        if (notificationChannel != null && !TextUtils.isEmpty(notificationChannel.getConversationId()) && !this.mChannel.isDemoted()) {
            ShortcutInfo conversationInfo = this.mBackend.getConversationInfo(this.mContext, this.mPkg, this.mUid, this.mChannel.getConversationId());
            this.mConversationInfo = conversationInfo;
            if (conversationInfo != null) {
                NotificationBackend notificationBackend = this.mBackend;
                Context context = this.mContext;
                NotificationBackend.AppRow appRow = this.mAppRow;
                this.mConversationDrawable = notificationBackend.getConversationDrawable(context, conversationInfo, appRow.pkg, appRow.uid, this.mChannel.isImportantConversation());
            }
        }
    }

    private void loadAppRow() {
        this.mAppRow = this.mBackend.loadAppRow(this.mContext, this.mPm, this.mRm, this.mPkgInfo);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0010, code lost:
        r0 = r5.mChannel;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadChannelGroup() {
        /*
            r5 = this;
            com.android.settings.notification.NotificationBackend r0 = r5.mBackend
            com.android.settings.notification.NotificationBackend$AppRow r1 = r5.mAppRow
            java.lang.String r2 = r1.pkg
            int r1 = r1.uid
            boolean r0 = r0.onlyHasDefaultChannel(r2, r1)
            java.lang.String r1 = "miscellaneous"
            if (r0 != 0) goto L_0x0021
            android.app.NotificationChannel r0 = r5.mChannel
            if (r0 == 0) goto L_0x001f
            java.lang.String r0 = r0.getId()
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x001f
            goto L_0x0021
        L_0x001f:
            r0 = 0
            goto L_0x0022
        L_0x0021:
            r0 = 1
        L_0x0022:
            r5.mShowLegacyChannelConfig = r0
            if (r0 == 0) goto L_0x0035
            com.android.settings.notification.NotificationBackend r0 = r5.mBackend
            com.android.settings.notification.NotificationBackend$AppRow r2 = r5.mAppRow
            java.lang.String r3 = r2.pkg
            int r2 = r2.uid
            r4 = 0
            android.app.NotificationChannel r0 = r0.getChannel(r3, r2, r1, r4)
            r5.mChannel = r0
        L_0x0035:
            android.app.NotificationChannel r0 = r5.mChannel
            if (r0 == 0) goto L_0x0057
            java.lang.String r0 = r0.getGroup()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0057
            com.android.settings.notification.NotificationBackend r0 = r5.mBackend
            java.lang.String r1 = r5.mPkg
            int r2 = r5.mUid
            android.app.NotificationChannel r3 = r5.mChannel
            java.lang.String r3 = r3.getGroup()
            android.app.NotificationChannelGroup r0 = r0.getGroup(r1, r2, r3)
            if (r0 == 0) goto L_0x0057
            r5.mChannelGroup = r0
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.notification.app.NotificationSettings.loadChannelGroup():void");
    }

    /* access modifiers changed from: protected */
    public void toastAndFinish() {
        Toast.makeText(this.mContext, R.string.app_not_found_dlg_text, 0).show();
        getActivity().finish();
    }

    /* access modifiers changed from: protected */
    public void collectConfigActivities() {
        Intent intent = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.NOTIFICATION_PREFERENCES").setPackage(this.mAppRow.pkg);
        List<ResolveInfo> queryIntentActivities = this.mPm.queryIntentActivities(intent, 0);
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Found ");
            sb.append(queryIntentActivities.size());
            sb.append(" preference activities");
            sb.append(queryIntentActivities.size() == 0 ? " ;_;" : "");
            Log.d("NotifiSettingsBase", sb.toString());
        }
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            NotificationBackend.AppRow appRow = this.mAppRow;
            if (appRow.settingsIntent == null) {
                appRow.settingsIntent = intent.setPackage((String) null).setClassName(activityInfo.packageName, activityInfo.name);
                NotificationChannel notificationChannel = this.mChannel;
                if (notificationChannel != null) {
                    this.mAppRow.settingsIntent.putExtra("android.intent.extra.CHANNEL_ID", notificationChannel.getId());
                }
                NotificationChannelGroup notificationChannelGroup = this.mChannelGroup;
                if (notificationChannelGroup != null) {
                    this.mAppRow.settingsIntent.putExtra("android.intent.extra.CHANNEL_GROUP_ID", notificationChannelGroup.getId());
                }
            } else if (DEBUG) {
                Log.d("NotifiSettingsBase", "Ignoring duplicate notification preference activity (" + activityInfo.name + ") for package " + activityInfo.packageName);
            }
        }
    }

    private PackageInfo findPackageInfo(String str, int i) {
        String[] packagesForUid;
        if (!(str == null || i < 0 || (packagesForUid = this.mPm.getPackagesForUid(i)) == null)) {
            int length = packagesForUid.length;
            int i2 = 0;
            while (i2 < length) {
                if (str.equals(packagesForUid[i2])) {
                    try {
                        return this.mPm.getPackageInfo(str, 64);
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.w("NotifiSettingsBase", "Failed to load package " + str, e);
                    }
                } else {
                    i2++;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void startListeningToPackageRemove() {
        if (!this.mListeningToPackageRemove) {
            this.mListeningToPackageRemove = true;
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            getContext().registerReceiver(this.mPackageRemovedReceiver, intentFilter);
        }
    }

    /* access modifiers changed from: protected */
    public void stopListeningToPackageRemove() {
        if (this.mListeningToPackageRemove) {
            this.mListeningToPackageRemove = false;
            getContext().unregisterReceiver(this.mPackageRemovedReceiver);
        }
    }

    /* access modifiers changed from: protected */
    public void onPackageRemoved() {
        getActivity().finishAndRemoveTask();
    }

    protected class DependentFieldListener {
        protected DependentFieldListener() {
        }

        /* access modifiers changed from: protected */
        public void onFieldValueChanged() {
            NotificationSettings notificationSettings = NotificationSettings.this;
            Drawable drawable = notificationSettings.mConversationDrawable;
            if (drawable != null && (drawable instanceof ConversationIconFactory.ConversationIconDrawable)) {
                ((ConversationIconFactory.ConversationIconDrawable) drawable).setImportant(notificationSettings.mChannel.isImportantConversation());
            }
            PreferenceScreen preferenceScreen = NotificationSettings.this.getPreferenceScreen();
            for (NotificationPreferenceController displayPreference : NotificationSettings.this.mControllers) {
                displayPreference.displayPreference(preferenceScreen);
            }
            NotificationSettings.this.updatePreferenceStates();
        }
    }
}
