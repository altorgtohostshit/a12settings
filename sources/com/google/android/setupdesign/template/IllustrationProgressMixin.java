package com.google.android.setupdesign.template;

import android.annotation.TargetApi;
import android.content.Context;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.GlifLayout;

@TargetApi(14)
@Deprecated
public class IllustrationProgressMixin implements Mixin {
    private final Context context;
    private final GlifLayout glifLayout;
    private ProgressConfig progressConfig = ProgressConfig.CONFIG_DEFAULT;

    public IllustrationProgressMixin(GlifLayout glifLayout2) {
        this.glifLayout = glifLayout2;
        this.context = glifLayout2.getContext();
    }

    public enum ProgressConfig {
        CONFIG_DEFAULT(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_DEFAULT),
        CONFIG_ACCOUNT(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_ACCOUNT),
        CONFIG_CONNECTION(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_CONNECTION),
        CONFIG_UPDATE(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_UPDATE);
        
        private final PartnerConfig config;

        private ProgressConfig(PartnerConfig partnerConfig) {
            if (partnerConfig.getResourceType() == PartnerConfig.ResourceType.ILLUSTRATION) {
                this.config = partnerConfig;
                return;
            }
            throw new IllegalArgumentException("Illustration progress only allow illustration resource");
        }
    }
}
