package com.android.settings.dashboard.profileselector;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.google.android.material.tabs.TabLayout;
import java.util.Locale;

public abstract class ProfileSelectFragment extends DashboardFragment {
    /* access modifiers changed from: private */
    public static final int[] LABEL = {R.string.category_personal, R.string.category_work};
    private ViewGroup mContentView;

    public abstract Fragment[] getFragments();

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ProfileSelectFragment";
    }

    public int getMetricsCategory() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.placeholder_preference_screen;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = (ViewGroup) super.onCreateView(layoutInflater, viewGroup, bundle);
        FragmentActivity activity = getActivity();
        int convertPosition = convertPosition(getTabId(activity, getArguments()));
        View findViewById = this.mContentView.findViewById(R.id.tab_container);
        ViewPager viewPager = (ViewPager) findViewById.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout tabLayout = (TabLayout) findViewById.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        findViewById.setVisibility(0);
        tabLayout.getTabAt(convertPosition).select();
        ((FrameLayout) this.mContentView.findViewById(16908351)).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        RecyclerView listView = getListView();
        listView.setOverScrollMode(2);
        Utils.setActionBarShadowAnimation(activity, getSettingsLifecycle(), listView);
        return this.mContentView;
    }

    /* access modifiers changed from: package-private */
    public int getTabId(Activity activity, Bundle bundle) {
        if (bundle != null) {
            int i = bundle.getInt(":settings:show_fragment_tab", -1);
            if (i != -1) {
                return i;
            }
            if (UserManager.get(activity).isManagedProfile(bundle.getInt("android.intent.extra.USER_ID", UserHandle.SYSTEM.getIdentifier()))) {
                return 1;
            }
        }
        if (UserManager.get(activity).isManagedProfile(activity.getIntent().getContentUserHint())) {
            return 1;
        }
        return 0;
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final Fragment[] mChildFragments;
        private final Context mContext;

        ViewPagerAdapter(ProfileSelectFragment profileSelectFragment) {
            super(profileSelectFragment.getChildFragmentManager());
            this.mContext = profileSelectFragment.getContext();
            this.mChildFragments = profileSelectFragment.getFragments();
        }

        public Fragment getItem(int i) {
            return this.mChildFragments[ProfileSelectFragment.convertPosition(i)];
        }

        public int getCount() {
            return this.mChildFragments.length;
        }

        public CharSequence getPageTitle(int i) {
            return this.mContext.getString(ProfileSelectFragment.LABEL[ProfileSelectFragment.convertPosition(i)]);
        }
    }

    /* access modifiers changed from: private */
    public static int convertPosition(int i) {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1 ? (LABEL.length - 1) - i : i;
    }
}
