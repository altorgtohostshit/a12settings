package com.android.settings.datausage;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.datausage.TemplatePreference;
import com.android.settingslib.net.DataUsageController;

public class DataUsagePreference extends Preference implements TemplatePreference {
    private int mSubId;
    private NetworkTemplate mTemplate;
    private int mTitleRes;

    public DataUsagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{16843233}, TypedArrayUtils.getAttr(context, R.attr.preferenceStyle, 16842894), 0);
        this.mTitleRes = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
    }

    public void setTemplate(NetworkTemplate networkTemplate, int i, TemplatePreference.NetworkServices networkServices) {
        this.mTemplate = networkTemplate;
        this.mSubId = i;
        DataUsageController dataUsageController = getDataUsageController();
        if (this.mTemplate.isMatchRuleMobile()) {
            setTitle((int) R.string.app_cellular_data_usage);
        } else {
            DataUsageController.DataUsageInfo dataUsageInfo = dataUsageController.getDataUsageInfo(this.mTemplate);
            setTitle(this.mTitleRes);
            setSummary((CharSequence) getContext().getString(R.string.data_usage_template, new Object[]{DataUsageUtils.formatDataUsage(getContext(), dataUsageInfo.usageLevel), dataUsageInfo.period}));
        }
        if (dataUsageController.getHistoricalUsageLevel(networkTemplate) > 0) {
            setIntent(getIntent());
            return;
        }
        setIntent((Intent) null);
        setEnabled(false);
    }

    public Intent getIntent() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("network_template", this.mTemplate);
        bundle.putInt("sub_id", this.mSubId);
        bundle.putInt("network_type", this.mTemplate.isMatchRuleMobile() ^ true ? 1 : 0);
        SubSettingLauncher sourceMetricsCategory = new SubSettingLauncher(getContext()).setArguments(bundle).setDestination(DataUsageList.class.getName()).setSourceMetricsCategory(0);
        if (this.mTemplate.isMatchRuleMobile()) {
            sourceMetricsCategory.setTitleRes(R.string.app_cellular_data_usage);
        } else {
            sourceMetricsCategory.setTitleRes(this.mTitleRes);
        }
        return sourceMetricsCategory.toIntent();
    }

    /* access modifiers changed from: package-private */
    public DataUsageController getDataUsageController() {
        return new DataUsageController(getContext());
    }
}
