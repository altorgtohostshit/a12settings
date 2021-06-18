package com.android.settings.notification.zen;

import android.app.Application;
import android.app.AutomaticZenRule;
import android.app.NotificationManager;
import android.content.Context;
import android.icu.text.MessageFormat;
import android.service.notification.ZenModeConfig;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

public class ZenModeSettings extends ZenModeSettingsBase {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.zen_mode_settings) {
        public List<String> getNonIndexableKeys(Context context) {
            List<String> nonIndexableKeys = super.getNonIndexableKeys(context);
            nonIndexableKeys.add("zen_mode_duration_settings");
            return nonIndexableKeys;
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ZenModeSettings.buildPreferenceControllers(context, (Lifecycle) null, (FragmentManager) null, (Application) null, (Fragment) null);
        }
    };

    public int getHelpResource() {
        return R.string.help_uri_interruptions;
    }

    public int getMetricsCategory() {
        return 76;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.zen_mode_settings;
    }

    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        FragmentActivity activity = getActivity();
        return buildPreferenceControllers(context, getSettingsLifecycle(), getFragmentManager(), activity != null ? activity.getApplication() : null, this);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle, FragmentManager fragmentManager, Application application, Fragment fragment) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZenModeButtonPreferenceController(context, lifecycle, fragmentManager));
        arrayList.add(new ZenModePeoplePreferenceController(context, lifecycle, "zen_mode_behavior_people"));
        arrayList.add(new ZenModeBypassingAppsPreferenceController(context, application, fragment, lifecycle));
        arrayList.add(new ZenModeSoundVibrationPreferenceController(context, lifecycle, "zen_sound_vibration_settings"));
        arrayList.add(new ZenModeAutomationPreferenceController(context));
        arrayList.add(new ZenModeDurationPreferenceController(context, lifecycle));
        arrayList.add(new ZenModeBlockedEffectsPreferenceController(context, lifecycle));
        arrayList.add(new ZenModeSettingsFooterPreferenceController(context, lifecycle, fragmentManager));
        return arrayList;
    }

    public static class SummaryBuilder {
        private static final int[] ALL_PRIORITY_CATEGORIES = {32, 64, 128, 4, 2, 1, 8, 16};
        private Context mContext;

        public SummaryBuilder(Context context) {
            this.mContext = context;
        }

        /* access modifiers changed from: package-private */
        public String getOtherSoundCategoriesSummary(NotificationManager.Policy policy) {
            List<String> enabledCategories = getEnabledCategories(policy, ZenModeSettings$SummaryBuilder$$ExternalSyntheticLambda2.INSTANCE, true);
            int size = enabledCategories.size();
            MessageFormat messageFormat = new MessageFormat(this.mContext.getString(R.string.zen_mode_other_sounds_summary), Locale.getDefault());
            HashMap hashMap = new HashMap();
            hashMap.put("count", Integer.valueOf(size));
            if (size >= 1) {
                hashMap.put("sound_category_1", enabledCategories.get(0));
                if (size >= 2) {
                    hashMap.put("sound_category_2", enabledCategories.get(1));
                    if (size == 3) {
                        hashMap.put("sound_category_3", enabledCategories.get(2));
                    }
                }
            }
            return messageFormat.format(hashMap);
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getOtherSoundCategoriesSummary$0(Integer num) {
            if (32 == num.intValue() || 64 == num.intValue() || 128 == num.intValue() || 1 == num.intValue() || 2 == num.intValue()) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public String getCallsSettingSummary(NotificationManager.Policy policy) {
            List<String> enabledCategories = getEnabledCategories(policy, ZenModeSettings$SummaryBuilder$$ExternalSyntheticLambda0.INSTANCE, true);
            int size = enabledCategories.size();
            if (size == 0) {
                return this.mContext.getString(R.string.zen_mode_none_calls);
            }
            if (size == 1) {
                return this.mContext.getString(R.string.zen_mode_calls_summary_one, new Object[]{enabledCategories.get(0)});
            }
            return this.mContext.getString(R.string.zen_mode_calls_summary_two, new Object[]{enabledCategories.get(0), enabledCategories.get(1)});
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getCallsSettingSummary$1(Integer num) {
            return 8 == num.intValue() || 16 == num.intValue();
        }

        /* access modifiers changed from: package-private */
        public String getMessagesSettingSummary(NotificationManager.Policy policy) {
            List<String> enabledCategories = getEnabledCategories(policy, ZenModeSettings$SummaryBuilder$$ExternalSyntheticLambda1.INSTANCE, false);
            if (enabledCategories.size() == 0) {
                return this.mContext.getString(R.string.zen_mode_none_messages);
            }
            return enabledCategories.get(0);
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getMessagesSettingSummary$2(Integer num) {
            return 4 == num.intValue();
        }

        /* access modifiers changed from: package-private */
        public String getSoundSummary() {
            if (NotificationManager.from(this.mContext).getZenMode() != 0) {
                String description = ZenModeConfig.getDescription(this.mContext, true, NotificationManager.from(this.mContext).getZenModeConfig(), false);
                if (description == null) {
                    return this.mContext.getString(R.string.zen_mode_sound_summary_on);
                }
                return this.mContext.getString(R.string.zen_mode_sound_summary_on_with_info, new Object[]{description});
            }
            MessageFormat messageFormat = new MessageFormat(this.mContext.getString(R.string.zen_mode_sound_summary_off), Locale.getDefault());
            HashMap hashMap = new HashMap();
            hashMap.put("count", Integer.valueOf(getEnabledAutomaticRulesCount()));
            return messageFormat.format(hashMap);
        }

        /* access modifiers changed from: package-private */
        public String getBlockedEffectsSummary(NotificationManager.Policy policy) {
            int i = policy.suppressedVisualEffects;
            if (i == 0) {
                return this.mContext.getResources().getString(R.string.zen_mode_restrict_notifications_summary_muted);
            }
            if (NotificationManager.Policy.areAllVisualEffectsSuppressed(i)) {
                return this.mContext.getResources().getString(R.string.zen_mode_restrict_notifications_summary_hidden);
            }
            return this.mContext.getResources().getString(R.string.zen_mode_restrict_notifications_summary_custom);
        }

        /* access modifiers changed from: package-private */
        public String getAutomaticRulesSummary() {
            MessageFormat messageFormat = new MessageFormat(this.mContext.getString(R.string.zen_mode_settings_schedules_summary), Locale.getDefault());
            HashMap hashMap = new HashMap();
            hashMap.put("count", Integer.valueOf(getEnabledAutomaticRulesCount()));
            return messageFormat.format(hashMap);
        }

        /* access modifiers changed from: package-private */
        public int getEnabledAutomaticRulesCount() {
            Map<String, AutomaticZenRule> automaticZenRules = NotificationManager.from(this.mContext).getAutomaticZenRules();
            int i = 0;
            if (automaticZenRules != null) {
                for (Map.Entry<String, AutomaticZenRule> value : automaticZenRules.entrySet()) {
                    AutomaticZenRule automaticZenRule = (AutomaticZenRule) value.getValue();
                    if (automaticZenRule != null && automaticZenRule.isEnabled()) {
                        i++;
                    }
                }
            }
            return i;
        }

        private List<String> getEnabledCategories(NotificationManager.Policy policy, Predicate<Integer> predicate, boolean z) {
            ArrayList arrayList = new ArrayList();
            for (int i : ALL_PRIORITY_CATEGORIES) {
                boolean z2 = z && arrayList.isEmpty();
                if (predicate.test(Integer.valueOf(i)) && isCategoryEnabled(policy, i) && !(i == 16 && isCategoryEnabled(policy, 8) && policy.priorityCallSenders == 0)) {
                    arrayList.add(getCategory(i, policy, z2));
                }
            }
            return arrayList;
        }

        private boolean isCategoryEnabled(NotificationManager.Policy policy, int i) {
            return (policy.priorityCategories & i) != 0;
        }

        private String getCategory(int i, NotificationManager.Policy policy, boolean z) {
            if (i == 32) {
                if (z) {
                    return this.mContext.getString(R.string.zen_mode_alarms_list_first);
                }
                return this.mContext.getString(R.string.zen_mode_alarms_list);
            } else if (i == 64) {
                if (z) {
                    return this.mContext.getString(R.string.zen_mode_media_list_first);
                }
                return this.mContext.getString(R.string.zen_mode_media_list);
            } else if (i == 128) {
                if (z) {
                    return this.mContext.getString(R.string.zen_mode_system_list_first);
                }
                return this.mContext.getString(R.string.zen_mode_system_list);
            } else if (i == 4) {
                int i2 = policy.priorityMessageSenders;
                if (i2 == 0) {
                    return this.mContext.getString(R.string.zen_mode_from_anyone);
                }
                if (i2 == 1) {
                    return this.mContext.getString(R.string.zen_mode_from_contacts);
                }
                return this.mContext.getString(R.string.zen_mode_from_starred);
            } else if (i == 2) {
                if (z) {
                    return this.mContext.getString(R.string.zen_mode_events_list_first);
                }
                return this.mContext.getString(R.string.zen_mode_events_list);
            } else if (i == 1) {
                if (z) {
                    return this.mContext.getString(R.string.zen_mode_reminders_list_first);
                }
                return this.mContext.getString(R.string.zen_mode_reminders_list);
            } else if (i == 8) {
                int i3 = policy.priorityCallSenders;
                if (i3 == 0) {
                    if (z) {
                        return this.mContext.getString(R.string.zen_mode_from_anyone);
                    }
                    return this.mContext.getString(R.string.zen_mode_all_callers);
                } else if (i3 == 1) {
                    if (z) {
                        return this.mContext.getString(R.string.zen_mode_from_contacts);
                    }
                    return this.mContext.getString(R.string.zen_mode_contacts_callers);
                } else if (z) {
                    return this.mContext.getString(R.string.zen_mode_from_starred);
                } else {
                    return this.mContext.getString(R.string.zen_mode_starred_callers);
                }
            } else if (i != 16) {
                return "";
            } else {
                if (z) {
                    return this.mContext.getString(R.string.zen_mode_repeat_callers);
                }
                return this.mContext.getString(R.string.zen_mode_repeat_callers_list);
            }
        }
    }
}
