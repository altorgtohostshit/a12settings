package com.android.settings.applications.specialaccess.notificationaccess;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.NotificationBackend;
import com.android.settingslib.applications.ApplicationsState;

public class BridgedAppsSettings extends DashboardFragment {
    private ComponentName mComponentName;
    private ApplicationsState.AppFilter mFilter;
    private boolean mShowSystem;
    private int mUserId;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BridgedAppsSettings";
    }

    public int getMetricsCategory() {
        return 1865;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.notification_access_bridged_apps_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mShowSystem = bundle != null && bundle.getBoolean("show_system");
        ((BridgedAppsPreferenceController) use(BridgedAppsPreferenceController.class)).setNm(new NotificationBackend());
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.add(0, 43, 0, this.mShowSystem ? R.string.menu_hide_system : R.string.menu_show_system);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        ApplicationsState.AppFilter appFilter;
        if (menuItem.getItemId() == 43) {
            boolean z = !this.mShowSystem;
            this.mShowSystem = z;
            menuItem.setTitle(z ? R.string.menu_hide_system : R.string.menu_show_system);
            if (this.mShowSystem) {
                appFilter = ApplicationsState.FILTER_NOT_HIDE;
            } else {
                appFilter = ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER;
            }
            this.mFilter = appFilter;
            ((BridgedAppsPreferenceController) use(BridgedAppsPreferenceController.class)).setFilter(this.mFilter).rebuild();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("show_system", this.mShowSystem);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onAttach(Context context) {
        ApplicationsState.AppFilter appFilter;
        super.onAttach(context);
        if (this.mShowSystem) {
            appFilter = ApplicationsState.FILTER_ALL_ENABLED;
        } else {
            appFilter = ApplicationsState.FILTER_DOWNLOADED_AND_LAUNCHER;
        }
        this.mFilter = appFilter;
        Bundle arguments = getArguments();
        Intent intent = arguments == null ? getIntent() : (Intent) arguments.getParcelable("intent");
        String string = arguments.getString("android.provider.extra.NOTIFICATION_LISTENER_COMPONENT_NAME");
        if (string != null) {
            this.mComponentName = ComponentName.unflattenFromString(string);
        }
        if (intent == null || !intent.hasExtra("android.intent.extra.user_handle")) {
            this.mUserId = UserHandle.myUserId();
        } else {
            this.mUserId = ((UserHandle) intent.getParcelableExtra("android.intent.extra.user_handle")).getIdentifier();
        }
        ((BridgedAppsPreferenceController) use(BridgedAppsPreferenceController.class)).setAppState(ApplicationsState.getInstance((Application) context.getApplicationContext())).setCn(this.mComponentName).setUserId(this.mUserId).setSession(getSettingsLifecycle()).setFilter(this.mFilter).rebuild();
    }
}
