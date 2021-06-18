package com.android.settings.wifi.calling;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.R$styleable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.internal.util.CollectionUtils;
import com.android.settings.R;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.ims.WifiCallingQueryImsState;
import com.android.settings.search.actionbar.SearchMenuController;
import com.android.settings.support.actionbar.HelpMenuController;
import com.android.settings.support.actionbar.HelpResourceProvider;
import com.android.settings.widget.RtlCompatibleViewPager;
import com.android.settings.widget.SlidingTabLayout;
import com.android.settingslib.core.lifecycle.ObservableFragment;
import java.util.List;

public class WifiCallingSettings extends InstrumentedFragment implements HelpResourceProvider {
    private WifiCallingViewPagerAdapter mPagerAdapter;
    /* access modifiers changed from: private */
    public List<SubscriptionInfo> mSil;
    private SlidingTabLayout mTabLayout;
    private RtlCompatibleViewPager mViewPager;

    public int getHelpResource() {
        return R.string.help_uri_wifi_calling;
    }

    public int getMetricsCategory() {
        return R$styleable.Constraint_pathMotionArc;
    }

    private final class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        private InternalViewPagerListener() {
        }

        public void onPageSelected(int i) {
            WifiCallingSettings.this.updateTitleForCurrentSub();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.wifi_calling_settings_tabs, viewGroup, false);
        this.mTabLayout = (SlidingTabLayout) inflate.findViewById(R.id.sliding_tabs);
        this.mViewPager = (RtlCompatibleViewPager) inflate.findViewById(R.id.view_pager);
        WifiCallingViewPagerAdapter wifiCallingViewPagerAdapter = new WifiCallingViewPagerAdapter(getChildFragmentManager(), this.mViewPager);
        this.mPagerAdapter = wifiCallingViewPagerAdapter;
        this.mViewPager.setAdapter(wifiCallingViewPagerAdapter);
        this.mViewPager.addOnPageChangeListener(new InternalViewPagerListener());
        maybeSetViewForSubId();
        return inflate;
    }

    private void maybeSetViewForSubId() {
        Intent intent;
        if (this.mSil != null && (intent = getActivity().getIntent()) != null) {
            int intExtra = intent.getIntExtra("android.provider.extra.SUB_ID", -1);
            if (SubscriptionManager.isValidSubscriptionId(intExtra)) {
                for (SubscriptionInfo next : this.mSil) {
                    if (intExtra == next.getSubscriptionId()) {
                        this.mViewPager.setCurrentItem(this.mSil.indexOf(next));
                        return;
                    }
                }
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!FeatureFlagUtils.isEnabled(getContext(), "settings_silky_home")) {
            setHasOptionsMenu(true);
            SearchMenuController.init((InstrumentedFragment) this);
            HelpMenuController.init((ObservableFragment) this);
        }
        updateSubList();
    }

    public void onStart() {
        super.onStart();
        List<SubscriptionInfo> list = this.mSil;
        if (list == null || list.size() <= 1) {
            this.mTabLayout.setVisibility(8);
        } else {
            this.mTabLayout.setViewPager(this.mViewPager);
        }
        updateTitleForCurrentSub();
    }

    final class WifiCallingViewPagerAdapter extends FragmentPagerAdapter {
        private final RtlCompatibleViewPager mViewPager;

        public WifiCallingViewPagerAdapter(FragmentManager fragmentManager, RtlCompatibleViewPager rtlCompatibleViewPager) {
            super(fragmentManager);
            this.mViewPager = rtlCompatibleViewPager;
        }

        public CharSequence getPageTitle(int i) {
            return String.valueOf(SubscriptionUtil.getUniqueSubscriptionDisplayName((SubscriptionInfo) WifiCallingSettings.this.mSil.get(i), WifiCallingSettings.this.getContext()));
        }

        public Fragment getItem(int i) {
            Log.d("WifiCallingSettings", "Adapter getItem " + i);
            Bundle bundle = new Bundle();
            bundle.putBoolean("need_search_icon_in_action_bar", false);
            bundle.putInt("subId", ((SubscriptionInfo) WifiCallingSettings.this.mSil.get(i)).getSubscriptionId());
            WifiCallingSettingsForSub wifiCallingSettingsForSub = new WifiCallingSettingsForSub();
            wifiCallingSettingsForSub.setArguments(bundle);
            return wifiCallingSettingsForSub;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            Log.d("WifiCallingSettings", "Adapter instantiateItem " + i);
            return super.instantiateItem(viewGroup, this.mViewPager.getRtlAwareIndex(i));
        }

        public int getCount() {
            if (WifiCallingSettings.this.mSil == null) {
                Log.d("WifiCallingSettings", "Adapter getCount null mSil ");
                return 0;
            }
            Log.d("WifiCallingSettings", "Adapter getCount " + WifiCallingSettings.this.mSil.size());
            return WifiCallingSettings.this.mSil.size();
        }
    }

    private void updateSubList() {
        List<SubscriptionInfo> activeSubscriptions = SubscriptionUtil.getActiveSubscriptions((SubscriptionManager) getContext().getSystemService(SubscriptionManager.class));
        this.mSil = activeSubscriptions;
        if (activeSubscriptions != null) {
            int i = 0;
            while (i < this.mSil.size()) {
                if (!queryImsState(this.mSil.get(i).getSubscriptionId()).isWifiCallingProvisioned()) {
                    this.mSil.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateTitleForCurrentSub() {
        if (CollectionUtils.size(this.mSil) > 1) {
            getActivity().getActionBar().setTitle(SubscriptionManager.getResourcesForSubId(getContext(), this.mSil.get(this.mViewPager.getCurrentItem()).getSubscriptionId()).getString(R.string.wifi_calling_settings_title));
        }
    }

    /* access modifiers changed from: package-private */
    public WifiCallingQueryImsState queryImsState(int i) {
        return new WifiCallingQueryImsState(getContext(), i);
    }
}
