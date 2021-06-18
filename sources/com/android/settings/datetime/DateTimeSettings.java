package com.android.settings.datetime;

import android.app.Dialog;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.datetime.DatePreferenceController;
import com.android.settings.datetime.TimePreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class DateTimeSettings extends DashboardFragment implements TimePreferenceController.TimePreferenceHost, DatePreferenceController.DatePreferenceHost {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.date_time_prefs);

    public int getDialogMetricsCategory(int i) {
        if (i != 0) {
            return i != 1 ? 0 : 608;
        }
        return 607;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DateTimeSettings";
    }

    public int getMetricsCategory() {
        return 38;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.date_time_prefs;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        getSettingsLifecycle().addObserver(new TimeChangeListenerMixin(context, this));
        ((LocationTimeZoneDetectionPreferenceController) use(LocationTimeZoneDetectionPreferenceController.class)).setFragment(this);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        FragmentActivity activity = getActivity();
        boolean booleanExtra = activity.getIntent().getBooleanExtra("firstRun", false);
        AutoTimeZonePreferenceController autoTimeZonePreferenceController = new AutoTimeZonePreferenceController(activity, this, booleanExtra);
        AutoTimePreferenceController autoTimePreferenceController = new AutoTimePreferenceController(activity, this);
        AutoTimeFormatPreferenceController autoTimeFormatPreferenceController = new AutoTimeFormatPreferenceController(activity, this);
        arrayList.add(autoTimeZonePreferenceController);
        arrayList.add(autoTimePreferenceController);
        arrayList.add(autoTimeFormatPreferenceController);
        arrayList.add(new TimeFormatPreferenceController(activity, this, booleanExtra));
        arrayList.add(new TimeZonePreferenceController(activity, autoTimeZonePreferenceController));
        arrayList.add(new TimePreferenceController(activity, this, autoTimePreferenceController));
        arrayList.add(new DatePreferenceController(activity, this, autoTimePreferenceController));
        return arrayList;
    }

    public void updateTimeAndDateDisplay(Context context) {
        updatePreferenceStates();
    }

    public Dialog onCreateDialog(int i) {
        if (i == 0) {
            return ((DatePreferenceController) use(DatePreferenceController.class)).buildDatePicker(getActivity());
        }
        if (i == 1) {
            return ((TimePreferenceController) use(TimePreferenceController.class)).buildTimePicker(getActivity());
        }
        throw new IllegalArgumentException();
    }

    public void showTimePicker() {
        removeDialog(1);
        showDialog(1);
    }

    public void showDatePicker() {
        showDialog(0);
    }
}
