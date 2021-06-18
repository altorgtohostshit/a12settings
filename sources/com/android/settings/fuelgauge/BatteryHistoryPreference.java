package com.android.settings.fuelgauge;

import android.content.Context;
import android.os.BatteryUsageStats;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.fuelgauge.BatteryInfo;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.UsageView;

public class BatteryHistoryPreference extends Preference {
    private BatteryChartView mBatteryChartView;
    BatteryInfo mBatteryInfo;
    private BatteryChartPreferenceController mChartPreferenceController;
    boolean mHideSummary;
    private boolean mIsChartGraphEnabled;
    private CharSequence mSummaryContent;
    private TextView mSummaryView;

    public BatteryHistoryPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsChartGraphEnabled = FeatureFactory.getFactory(context).getPowerUsageFeatureProvider(context).isChartGraphEnabled(context);
        Log.i("BatteryHistoryPreference", "isChartGraphEnabled: " + this.mIsChartGraphEnabled);
        setLayoutResource(this.mIsChartGraphEnabled ? R.layout.battery_chart_graph : R.layout.battery_usage_graph);
        setSelectable(false);
    }

    public void setBottomSummary(CharSequence charSequence) {
        this.mSummaryContent = charSequence;
        TextView textView = this.mSummaryView;
        if (textView != null) {
            textView.setVisibility(0);
            this.mSummaryView.setText(this.mSummaryContent);
        }
        this.mHideSummary = false;
    }

    public void hideBottomSummary() {
        TextView textView = this.mSummaryView;
        if (textView != null) {
            textView.setVisibility(8);
        }
        this.mHideSummary = true;
    }

    /* access modifiers changed from: package-private */
    public void setBatteryUsageStats(BatteryUsageStats batteryUsageStats) {
        BatteryInfo.getBatteryInfo(getContext(), new BatteryHistoryPreference$$ExternalSyntheticLambda0(this), batteryUsageStats, false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setBatteryUsageStats$0(BatteryInfo batteryInfo) {
        this.mBatteryInfo = batteryInfo;
        notifyChanged();
    }

    /* access modifiers changed from: package-private */
    public void setChartPreferenceController(BatteryChartPreferenceController batteryChartPreferenceController) {
        this.mChartPreferenceController = batteryChartPreferenceController;
        BatteryChartView batteryChartView = this.mBatteryChartView;
        if (batteryChartView != null) {
            batteryChartPreferenceController.setBatteryChartView(batteryChartView);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mBatteryInfo != null) {
            if (this.mIsChartGraphEnabled) {
                BatteryChartView batteryChartView = (BatteryChartView) preferenceViewHolder.findViewById(R.id.battery_chart);
                this.mBatteryChartView = batteryChartView;
                batteryChartView.setCompanionTextView((TextView) preferenceViewHolder.findViewById(R.id.companion_text));
                BatteryChartPreferenceController batteryChartPreferenceController = this.mChartPreferenceController;
                if (batteryChartPreferenceController != null) {
                    batteryChartPreferenceController.setBatteryChartView(this.mBatteryChartView);
                }
            } else {
                ((TextView) preferenceViewHolder.findViewById(R.id.charge)).setText(this.mBatteryInfo.batteryPercentString);
                TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.bottom_summary);
                this.mSummaryView = textView;
                CharSequence charSequence = this.mSummaryContent;
                if (charSequence != null) {
                    textView.setText(charSequence);
                }
                if (this.mHideSummary) {
                    this.mSummaryView.setVisibility(8);
                }
                UsageView usageView = (UsageView) preferenceViewHolder.findViewById(R.id.battery_usage);
                usageView.findViewById(R.id.label_group).setAlpha(0.7f);
                this.mBatteryInfo.bindHistory(usageView, new BatteryInfo.BatteryDataParser[0]);
            }
            BatteryUtils.logRuntime("BatteryHistoryPreference", "onBindViewHolder", currentTimeMillis);
        }
    }
}
