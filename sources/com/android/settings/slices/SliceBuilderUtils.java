package com.android.settings.slices;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SliceBuilderUtils {
    public static Slice buildSlice(Context context, SliceData sliceData) {
        Log.d("SliceBuilder", "Creating slice for: " + sliceData.getPreferenceController());
        BasePreferenceController preferenceController = getPreferenceController(context, sliceData);
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(0, 1371, 0, sliceData.getKey(), 0);
        if (!preferenceController.isAvailable()) {
            return null;
        }
        if (preferenceController.getAvailabilityStatus() == 5) {
            return buildUnavailableSlice(context, sliceData);
        }
        if (preferenceController.isCopyableSlice()) {
            return buildCopyableSlice(context, sliceData, preferenceController);
        }
        int sliceType = sliceData.getSliceType();
        if (sliceType == 0) {
            return buildIntentSlice(context, sliceData, preferenceController);
        }
        if (sliceType == 1) {
            return buildToggleSlice(context, sliceData, preferenceController);
        }
        if (sliceType == 2) {
            return buildSliderSlice(context, sliceData, preferenceController);
        }
        throw new IllegalArgumentException("Slice type passed was invalid: " + sliceData.getSliceType());
    }

    public static Pair<Boolean, String> getPathData(Uri uri) {
        String[] split = uri.getPath().split("/", 3);
        if (split.length != 3) {
            return null;
        }
        return new Pair<>(Boolean.valueOf(TextUtils.equals("intent", split[1])), split[2]);
    }

    public static BasePreferenceController getPreferenceController(Context context, SliceData sliceData) {
        return getPreferenceController(context, sliceData.getPreferenceController(), sliceData.getKey());
    }

    public static PendingIntent getActionIntent(Context context, String str, SliceData sliceData) {
        return PendingIntent.getBroadcast(context, 0, new Intent(str).setData(sliceData.getUri()).setClass(context, SliceBroadcastReceiver.class).putExtra("com.android.settings.slice.extra.key", sliceData.getKey()), 167772160);
    }

    public static PendingIntent getContentPendingIntent(Context context, SliceData sliceData) {
        return PendingIntent.getActivity(context, 0, getContentIntent(context, sliceData), 67108864);
    }

    public static CharSequence getSubtitleText(Context context, BasePreferenceController basePreferenceController, SliceData sliceData) {
        if (basePreferenceController.useDynamicSliceSummary()) {
            return basePreferenceController.getSummary();
        }
        String summary = sliceData.getSummary();
        if (isValidSummary(context, summary)) {
            return summary;
        }
        CharSequence screenTitle = sliceData.getScreenTitle();
        return (!isValidSummary(context, screenTitle) || TextUtils.equals(screenTitle, sliceData.getTitle())) ? "" : screenTitle;
    }

    public static Intent buildSearchResultPageIntent(Context context, String str, String str2, String str3, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(":settings:fragment_args_key", str2);
        Intent intent = new SubSettingLauncher(context).setDestination(str).setArguments(bundle).setTitleText(str3).setSourceMetricsCategory(i).toIntent();
        intent.putExtra(":settings:fragment_args_key", str2).setAction("com.android.settings.SEARCH_RESULT_TRAMPOLINE").setComponent((ComponentName) null);
        intent.addFlags(335544320);
        return intent;
    }

    public static Intent getContentIntent(Context context, SliceData sliceData) {
        String str;
        Uri build = new Uri.Builder().appendPath(sliceData.getKey()).build();
        if (TextUtils.isEmpty(sliceData.getScreenTitle())) {
            str = null;
        } else {
            str = sliceData.getScreenTitle().toString();
        }
        Intent buildSearchResultPageIntent = buildSearchResultPageIntent(context, sliceData.getFragmentClassName(), sliceData.getKey(), str, 0);
        buildSearchResultPageIntent.setClassName(context.getPackageName(), SubSettings.class.getName());
        buildSearchResultPageIntent.setData(build);
        return buildSearchResultPageIntent;
    }

    private static Slice buildToggleSlice(Context context, SliceData sliceData, BasePreferenceController basePreferenceController) {
        PendingIntent contentPendingIntent = getContentPendingIntent(context, sliceData);
        IconCompat safeIcon = getSafeIcon(context, sliceData);
        CharSequence subtitleText = getSubtitleText(context, basePreferenceController, sliceData);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        SliceAction toggleAction = getToggleAction(context, sliceData, ((TogglePreferenceController) basePreferenceController).isChecked());
        Set<String> buildSliceKeywords = buildSliceKeywords(sliceData);
        ListBuilder.RowBuilder addEndItem = new ListBuilder.RowBuilder().setTitle(sliceData.getTitle()).setPrimaryAction(SliceAction.createDeeplink(contentPendingIntent, safeIcon, 0, sliceData.getTitle())).addEndItem(toggleAction);
        if (!com.android.settings.Utils.isSettingsIntelligence(context)) {
            addEndItem.setSubtitle(subtitleText);
        }
        return new ListBuilder(context, sliceData.getUri(), -1).setAccentColor(colorAccentDefaultColor).addRow(addEndItem).setKeywords(buildSliceKeywords).build();
    }

    private static Slice buildIntentSlice(Context context, SliceData sliceData, BasePreferenceController basePreferenceController) {
        PendingIntent contentPendingIntent = getContentPendingIntent(context, sliceData);
        IconCompat safeIcon = getSafeIcon(context, sliceData);
        CharSequence subtitleText = getSubtitleText(context, basePreferenceController, sliceData);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        Set<String> buildSliceKeywords = buildSliceKeywords(sliceData);
        ListBuilder.RowBuilder primaryAction = new ListBuilder.RowBuilder().setTitle(sliceData.getTitle()).setPrimaryAction(SliceAction.createDeeplink(contentPendingIntent, safeIcon, 0, sliceData.getTitle()));
        if (!com.android.settings.Utils.isSettingsIntelligence(context)) {
            primaryAction.setSubtitle(subtitleText);
        }
        return new ListBuilder(context, sliceData.getUri(), -1).setAccentColor(colorAccentDefaultColor).addRow(primaryAction).setKeywords(buildSliceKeywords).build();
    }

    private static Slice buildSliderSlice(Context context, SliceData sliceData, BasePreferenceController basePreferenceController) {
        SliderPreferenceController sliderPreferenceController = (SliderPreferenceController) basePreferenceController;
        if (sliderPreferenceController.getMax() <= sliderPreferenceController.getMin()) {
            Log.e("SliceBuilder", "Invalid sliderController: " + sliderPreferenceController.getPreferenceKey());
            return null;
        }
        PendingIntent sliderAction = getSliderAction(context, sliceData);
        PendingIntent contentPendingIntent = getContentPendingIntent(context, sliceData);
        IconCompat safeIcon = getSafeIcon(context, sliceData);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        CharSequence subtitleText = getSubtitleText(context, basePreferenceController, sliceData);
        SliceAction createDeeplink = SliceAction.createDeeplink(contentPendingIntent, safeIcon, 0, sliceData.getTitle());
        Set<String> buildSliceKeywords = buildSliceKeywords(sliceData);
        int sliderPosition = sliderPreferenceController.getSliderPosition();
        if (sliderPosition < sliderPreferenceController.getMin()) {
            sliderPosition = sliderPreferenceController.getMin();
        }
        if (sliderPosition > sliderPreferenceController.getMax()) {
            sliderPosition = sliderPreferenceController.getMax();
        }
        ListBuilder.InputRangeBuilder inputAction = new ListBuilder.InputRangeBuilder().setTitle(sliceData.getTitle()).setPrimaryAction(createDeeplink).setMax(sliderPreferenceController.getMax()).setMin(sliderPreferenceController.getMin()).setValue(sliderPosition).setInputAction(sliderAction);
        if (sliceData.getIconResource() != 0) {
            inputAction.setTitleItem(safeIcon, 0);
            colorAccentDefaultColor = -1;
        }
        if (!com.android.settings.Utils.isSettingsIntelligence(context)) {
            inputAction.setSubtitle(subtitleText);
        }
        return new ListBuilder(context, sliceData.getUri(), -1).setAccentColor(colorAccentDefaultColor).addInputRange(inputAction).setKeywords(buildSliceKeywords).build();
    }

    private static Slice buildCopyableSlice(Context context, SliceData sliceData, BasePreferenceController basePreferenceController) {
        SliceAction copyableAction = getCopyableAction(context, sliceData);
        SliceAction createDeeplink = SliceAction.createDeeplink(getContentPendingIntent(context, sliceData), getSafeIcon(context, sliceData), 0, sliceData.getTitle());
        CharSequence subtitleText = getSubtitleText(context, basePreferenceController, sliceData);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        Set<String> buildSliceKeywords = buildSliceKeywords(sliceData);
        ListBuilder.RowBuilder addEndItem = new ListBuilder.RowBuilder().setTitle(sliceData.getTitle()).setPrimaryAction(createDeeplink).addEndItem(copyableAction);
        if (!com.android.settings.Utils.isSettingsIntelligence(context)) {
            addEndItem.setSubtitle(subtitleText);
        }
        return new ListBuilder(context, sliceData.getUri(), -1).setAccentColor(colorAccentDefaultColor).addRow(addEndItem).setKeywords(buildSliceKeywords).build();
    }

    static BasePreferenceController getPreferenceController(Context context, String str, String str2) {
        try {
            return BasePreferenceController.createInstance(context, str);
        } catch (IllegalStateException unused) {
            return BasePreferenceController.createInstance(context, str, str2);
        }
    }

    private static SliceAction getToggleAction(Context context, SliceData sliceData, boolean z) {
        return SliceAction.createToggle(getActionIntent(context, "com.android.settings.slice.action.TOGGLE_CHANGED", sliceData), (CharSequence) null, z);
    }

    private static PendingIntent getSliderAction(Context context, SliceData sliceData) {
        return getActionIntent(context, "com.android.settings.slice.action.SLIDER_CHANGED", sliceData);
    }

    private static SliceAction getCopyableAction(Context context, SliceData sliceData) {
        return SliceAction.create(getActionIntent(context, "com.android.settings.slice.action.COPY", sliceData), IconCompat.createWithResource(context, R.drawable.ic_content_copy_grey600_24dp), 0, sliceData.getTitle());
    }

    private static boolean isValidSummary(Context context, CharSequence charSequence) {
        if (charSequence == null || TextUtils.isEmpty(charSequence.toString().trim())) {
            return false;
        }
        CharSequence text = context.getText(R.string.summary_placeholder);
        CharSequence text2 = context.getText(R.string.summary_two_lines_placeholder);
        if (TextUtils.equals(charSequence, text) || TextUtils.equals(charSequence, text2)) {
            return false;
        }
        return true;
    }

    private static Set<String> buildSliceKeywords(SliceData sliceData) {
        ArraySet arraySet = new ArraySet();
        arraySet.add(sliceData.getTitle());
        if (!TextUtils.isEmpty(sliceData.getScreenTitle()) && !TextUtils.equals(sliceData.getTitle(), sliceData.getScreenTitle())) {
            arraySet.add(sliceData.getScreenTitle().toString());
        }
        String keywords = sliceData.getKeywords();
        if (keywords != null) {
            arraySet.addAll((List) Arrays.stream(keywords.split(",")).map(SliceBuilderUtils$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList()));
        }
        return arraySet;
    }

    private static Slice buildUnavailableSlice(Context context, SliceData sliceData) {
        String title = sliceData.getTitle();
        Set<String> buildSliceKeywords = buildSliceKeywords(sliceData);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        CharSequence unavailableSliceSubtitle = sliceData.getUnavailableSliceSubtitle();
        if (TextUtils.isEmpty(unavailableSliceSubtitle)) {
            unavailableSliceSubtitle = context.getText(R.string.disabled_dependent_setting_summary);
        }
        IconCompat safeIcon = getSafeIcon(context, sliceData);
        ListBuilder.RowBuilder primaryAction = new ListBuilder.RowBuilder().setTitle(title).setTitleItem(safeIcon, 0).setPrimaryAction(SliceAction.createDeeplink(getContentPendingIntent(context, sliceData), safeIcon, 0, title));
        if (!com.android.settings.Utils.isSettingsIntelligence(context)) {
            primaryAction.setSubtitle(unavailableSliceSubtitle);
        }
        return new ListBuilder(context, sliceData.getUri(), -1).setAccentColor(colorAccentDefaultColor).addRow(primaryAction).setKeywords(buildSliceKeywords).build();
    }

    static IconCompat getSafeIcon(Context context, SliceData sliceData) {
        int iconResource = sliceData.getIconResource();
        if (iconResource == 0) {
            iconResource = R.drawable.ic_settings_accent;
        }
        try {
            return IconCompat.createWithResource(context, iconResource);
        } catch (Exception e) {
            Log.w("SliceBuilder", "Falling back to settings icon because there is an error getting slice icon " + sliceData.getUri(), e);
            return IconCompat.createWithResource(context, R.drawable.ic_settings_accent);
        }
    }
}
