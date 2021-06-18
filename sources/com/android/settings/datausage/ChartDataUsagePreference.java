package com.android.settings.datausage;

import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkPolicy;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.widget.UsageView;
import com.android.settingslib.Utils;
import com.android.settingslib.net.NetworkCycleChartData;
import com.android.settingslib.net.NetworkCycleData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartDataUsagePreference extends Preference {
    private long mEnd;
    private final int mLimitColor;
    private NetworkCycleChartData mNetworkCycleChartData;
    private NetworkPolicy mPolicy;
    private Resources mResources;
    private int mSecondaryColor;
    private int mSeriesColor;
    private long mStart;
    private final int mWarningColor;

    public ChartDataUsagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mResources = context.getResources();
        setSelectable(false);
        this.mLimitColor = Utils.getColorAttrDefaultColor(context, 16844099);
        this.mWarningColor = Utils.getColorAttrDefaultColor(context, 16842808);
        setLayoutResource(R.layout.data_usage_graph);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        UsageView usageView = (UsageView) preferenceViewHolder.findViewById(R.id.data_usage);
        if (this.mNetworkCycleChartData != null) {
            int top = getTop();
            usageView.clearPaths();
            usageView.configureGraph(toInt(this.mEnd - this.mStart), top);
            calcPoints(usageView, this.mNetworkCycleChartData.getUsageBuckets());
            setupContentDescription(usageView, this.mNetworkCycleChartData.getUsageBuckets());
            Context context = getContext();
            long j = this.mStart;
            Context context2 = getContext();
            long j2 = this.mEnd;
            usageView.setBottomLabels(new CharSequence[]{com.android.settings.Utils.formatDateRange(context, j, j), com.android.settings.Utils.formatDateRange(context2, j2, j2)});
            bindNetworkPolicy(usageView, this.mPolicy, top);
        }
    }

    public int getTop() {
        long totalUsage = this.mNetworkCycleChartData.getTotalUsage();
        NetworkPolicy networkPolicy = this.mPolicy;
        return (int) (Math.max(totalUsage, networkPolicy != null ? Math.max(networkPolicy.limitBytes, networkPolicy.warningBytes) : 0) / 524288);
    }

    /* access modifiers changed from: package-private */
    public void calcPoints(UsageView usageView, List<NetworkCycleData> list) {
        if (list != null) {
            SparseIntArray sparseIntArray = new SparseIntArray();
            sparseIntArray.put(0, 0);
            long currentTimeMillis = System.currentTimeMillis();
            long j = 0;
            for (NetworkCycleData next : list) {
                long startTime = next.getStartTime();
                if (startTime > currentTimeMillis) {
                    break;
                }
                long endTime = next.getEndTime();
                j += next.getTotalUsage();
                if (sparseIntArray.size() == 1) {
                    sparseIntArray.put(toInt(startTime - this.mStart) - 1, -1);
                }
                int i = (int) (j / 524288);
                sparseIntArray.put(toInt((startTime - this.mStart) + 1), i);
                sparseIntArray.put(toInt(endTime - this.mStart), i);
            }
            if (sparseIntArray.size() > 1) {
                usageView.addPath(sparseIntArray);
            }
        }
    }

    private void setupContentDescription(UsageView usageView, List<NetworkCycleData> list) {
        String str;
        Context context = getContext();
        StringBuilder sb = new StringBuilder();
        String formatDateTime = DateUtils.formatDateTime(context, this.mStart, 65552);
        String formatDateTime2 = DateUtils.formatDateTime(context, this.mEnd, 65552);
        sb.append(this.mResources.getString(R.string.data_usage_chart_brief_content_description, new Object[]{formatDateTime, formatDateTime2}));
        if (list == null || list.isEmpty()) {
            sb.append(this.mResources.getString(R.string.data_usage_chart_no_data_content_description));
            usageView.setContentDescription(sb);
            return;
        }
        for (DataUsageSummaryNode next : getDensedStatsData(list)) {
            int dataUsagePercentage = next.getDataUsagePercentage();
            if (!next.isFromMultiNode() || dataUsagePercentage == 100) {
                str = DateUtils.formatDateTime(context, next.getStartTime(), 65552);
            } else {
                str = DateUtils.formatDateRange(context, next.getStartTime(), next.getEndTime(), 65552);
            }
            sb.append(String.format(";%s %d%%", new Object[]{str, Integer.valueOf(dataUsagePercentage)}));
        }
        usageView.setContentDescription(sb);
    }

    /* access modifiers changed from: package-private */
    public List<DataUsageSummaryNode> getDensedStatsData(List<NetworkCycleData> list) {
        ArrayList arrayList = new ArrayList();
        long max = Math.max(1, list.stream().mapToLong(ChartDataUsagePreference$$ExternalSyntheticLambda5.INSTANCE).sum());
        long j = 0;
        for (NetworkCycleData next : list) {
            j += next.getTotalUsage();
            arrayList.add(new DataUsageSummaryNode(next.getStartTime(), next.getEndTime(), (int) ((100 * j) / max)));
        }
        ArrayList arrayList2 = new ArrayList();
        ((Map) arrayList.stream().collect(Collectors.groupingBy(ChartDataUsagePreference$$ExternalSyntheticLambda1.INSTANCE))).forEach(new ChartDataUsagePreference$$ExternalSyntheticLambda0(this, arrayList2));
        return (List) arrayList2.stream().sorted(Comparator.comparingInt(ChartDataUsagePreference$$ExternalSyntheticLambda2.INSTANCE)).collect(Collectors.toList());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getDensedStatsData$0(List list, Integer num, List list2) {
        DataUsageSummaryNode dataUsageSummaryNode = new DataUsageSummaryNode(list2.stream().mapToLong(ChartDataUsagePreference$$ExternalSyntheticLambda4.INSTANCE).min().getAsLong(), list2.stream().mapToLong(ChartDataUsagePreference$$ExternalSyntheticLambda3.INSTANCE).max().getAsLong(), num.intValue());
        if (list2.size() > 1) {
            dataUsageSummaryNode.setFromMultiNode(true);
        }
        list.add(dataUsageSummaryNode);
    }

    class DataUsageSummaryNode {
        private int mDataUsagePercentage;
        private long mEndTime;
        private boolean mIsFromMultiNode = false;
        private long mStartTime;

        public DataUsageSummaryNode(long j, long j2, int i) {
            this.mStartTime = j;
            this.mEndTime = j2;
            this.mDataUsagePercentage = i;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public long getEndTime() {
            return this.mEndTime;
        }

        public int getDataUsagePercentage() {
            return this.mDataUsagePercentage;
        }

        public void setFromMultiNode(boolean z) {
            this.mIsFromMultiNode = z;
        }

        public boolean isFromMultiNode() {
            return this.mIsFromMultiNode;
        }
    }

    private int toInt(long j) {
        return (int) (j / 60000);
    }

    private void bindNetworkPolicy(UsageView usageView, NetworkPolicy networkPolicy, int i) {
        int i2;
        CharSequence[] charSequenceArr = new CharSequence[3];
        if (networkPolicy != null) {
            long j = networkPolicy.limitBytes;
            int i3 = 0;
            if (j != -1) {
                i2 = this.mLimitColor;
                charSequenceArr[2] = getLabel(j, R.string.data_usage_sweep_limit, i2);
            } else {
                i2 = 0;
            }
            long j2 = networkPolicy.warningBytes;
            if (j2 != -1) {
                usageView.setDividerLoc((int) (j2 / 524288));
                float f = ((float) (networkPolicy.warningBytes / 524288)) / ((float) i);
                usageView.setSideLabelWeights(1.0f - f, f);
                i3 = this.mWarningColor;
                charSequenceArr[1] = getLabel(networkPolicy.warningBytes, R.string.data_usage_sweep_warning, i3);
            }
            usageView.setSideLabels(charSequenceArr);
            usageView.setDividerColors(i3, i2);
        }
    }

    private CharSequence getLabel(long j, int i, int i2) {
        Formatter.BytesResult formatBytes = Formatter.formatBytes(this.mResources, j, 9);
        return new SpannableStringBuilder().append(TextUtils.expandTemplate(getContext().getText(i), new CharSequence[]{formatBytes.value, formatBytes.units}), new ForegroundColorSpan(i2), 0);
    }

    public void setNetworkPolicy(NetworkPolicy networkPolicy) {
        this.mPolicy = networkPolicy;
        notifyChanged();
    }

    public long getInspectStart() {
        return this.mStart;
    }

    public long getInspectEnd() {
        return this.mEnd;
    }

    public void setNetworkCycleData(NetworkCycleChartData networkCycleChartData) {
        this.mNetworkCycleChartData = networkCycleChartData;
        this.mStart = networkCycleChartData.getStartTime();
        this.mEnd = networkCycleChartData.getEndTime();
        notifyChanged();
    }

    public void setColors(int i, int i2) {
        this.mSeriesColor = i;
        this.mSecondaryColor = i2;
        notifyChanged();
    }
}
