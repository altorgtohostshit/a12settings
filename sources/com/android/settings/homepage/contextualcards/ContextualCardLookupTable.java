package com.android.settings.homepage.contextualcards;

import android.util.Log;
import com.android.settings.R;
import com.android.settings.homepage.contextualcards.conditional.ConditionContextualCardController;
import com.android.settings.homepage.contextualcards.conditional.ConditionContextualCardRenderer;
import com.android.settings.homepage.contextualcards.conditional.ConditionFooterContextualCardRenderer;
import com.android.settings.homepage.contextualcards.conditional.ConditionHeaderContextualCardRenderer;
import com.android.settings.homepage.contextualcards.legacysuggestion.LegacySuggestionContextualCardController;
import com.android.settings.homepage.contextualcards.legacysuggestion.LegacySuggestionContextualCardRenderer;
import com.android.settings.homepage.contextualcards.slices.SliceContextualCardController;
import com.android.settings.homepage.contextualcards.slices.SliceContextualCardRenderer;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ContextualCardLookupTable {
    static final Set<ControllerRendererMapping> LOOKUP_TABLE = new TreeSet<ControllerRendererMapping>() {
        {
            Class<ConditionContextualCardRenderer> cls = ConditionContextualCardRenderer.class;
            Class<SliceContextualCardRenderer> cls2 = SliceContextualCardRenderer.class;
            Class<SliceContextualCardController> cls3 = SliceContextualCardController.class;
            Class<ConditionContextualCardController> cls4 = ConditionContextualCardController.class;
            add(new ControllerRendererMapping(3, R.layout.conditional_card_half_tile, cls4, cls));
            add(new ControllerRendererMapping(3, R.layout.conditional_card_full_tile, cls4, cls));
            add(new ControllerRendererMapping(2, R.layout.legacy_suggestion_tile, LegacySuggestionContextualCardController.class, LegacySuggestionContextualCardRenderer.class));
            add(new ControllerRendererMapping(1, R.layout.contextual_slice_full_tile, cls3, cls2));
            add(new ControllerRendererMapping(1, R.layout.contextual_slice_half_tile, cls3, cls2));
            add(new ControllerRendererMapping(1, R.layout.contextual_slice_sticky_tile, cls3, cls2));
            add(new ControllerRendererMapping(5, R.layout.conditional_card_footer, cls4, ConditionFooterContextualCardRenderer.class));
            add(new ControllerRendererMapping(4, R.layout.conditional_card_header, cls4, ConditionHeaderContextualCardRenderer.class));
        }
    };

    static class ControllerRendererMapping implements Comparable<ControllerRendererMapping> {
        /* access modifiers changed from: package-private */
        public final int mCardType;
        final Class<? extends ContextualCardController> mControllerClass;
        final Class<? extends ContextualCardRenderer> mRendererClass;
        /* access modifiers changed from: package-private */
        public final int mViewType;

        ControllerRendererMapping(int i, int i2, Class<? extends ContextualCardController> cls, Class<? extends ContextualCardRenderer> cls2) {
            this.mCardType = i;
            this.mViewType = i2;
            this.mControllerClass = cls;
            this.mRendererClass = cls2;
        }

        public int compareTo(ControllerRendererMapping controllerRendererMapping) {
            return Comparator.comparingInt(C0955xd5f016ad.INSTANCE).thenComparingInt(C0956xd5f016ae.INSTANCE).compare(this, controllerRendererMapping);
        }
    }

    public static Class<? extends ContextualCardController> getCardControllerClass(int i) {
        for (ControllerRendererMapping next : LOOKUP_TABLE) {
            if (next.mCardType == i) {
                return next.mControllerClass;
            }
        }
        return null;
    }

    public static Class<? extends ContextualCardRenderer> getCardRendererClassByViewType(int i) throws IllegalStateException {
        List list = (List) LOOKUP_TABLE.stream().filter(new ContextualCardLookupTable$$ExternalSyntheticLambda0(i)).collect(Collectors.toList());
        if (list == null || list.isEmpty()) {
            Log.w("ContextualCardLookup", "No matching mapping");
            return null;
        } else if (list.size() == 1) {
            return ((ControllerRendererMapping) list.get(0)).mRendererClass;
        } else {
            throw new IllegalStateException("Have duplicate VIEW_TYPE in lookup table.");
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getCardRendererClassByViewType$0(int i, ControllerRendererMapping controllerRendererMapping) {
        return controllerRendererMapping.mViewType == i;
    }
}
