package com.android.settings.homepage.contextualcards;

import android.content.Context;
import android.util.Log;
import androidx.collection.ArraySet;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.homepage.contextualcards.conditional.ConditionContextualCardController;
import com.android.settings.homepage.contextualcards.conditional.ConditionContextualCardRenderer;
import com.android.settings.homepage.contextualcards.conditional.ConditionFooterContextualCardRenderer;
import com.android.settings.homepage.contextualcards.conditional.ConditionHeaderContextualCardRenderer;
import com.android.settings.homepage.contextualcards.legacysuggestion.LegacySuggestionContextualCardController;
import com.android.settings.homepage.contextualcards.legacysuggestion.LegacySuggestionContextualCardRenderer;
import com.android.settings.homepage.contextualcards.slices.SliceContextualCardController;
import com.android.settings.homepage.contextualcards.slices.SliceContextualCardRenderer;
import java.util.Iterator;
import java.util.Set;

public class ControllerRendererPool {
    private final Set<ContextualCardController> mControllers = new ArraySet();
    private final Set<ContextualCardRenderer> mRenderers = new ArraySet();

    public <T extends ContextualCardController> T getController(Context context, int i) {
        Class<? extends ContextualCardController> cardControllerClass = ContextualCardLookupTable.getCardControllerClass(i);
        Iterator<ContextualCardController> it = this.mControllers.iterator();
        while (it.hasNext()) {
            T t = (ContextualCardController) it.next();
            if (t.getClass().getName().equals(cardControllerClass.getName())) {
                Log.d("ControllerRendererPool", "Controller is already there.");
                return t;
            }
        }
        T createCardController = createCardController(context, cardControllerClass);
        if (createCardController != null) {
            this.mControllers.add(createCardController);
        }
        return createCardController;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Set<ContextualCardController> getControllers() {
        return this.mControllers;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Set<ContextualCardRenderer> getRenderers() {
        return this.mRenderers;
    }

    public ContextualCardRenderer getRendererByViewType(Context context, LifecycleOwner lifecycleOwner, int i) {
        return getRenderer(context, lifecycleOwner, ContextualCardLookupTable.getCardRendererClassByViewType(i));
    }

    private ContextualCardRenderer getRenderer(Context context, LifecycleOwner lifecycleOwner, Class<? extends ContextualCardRenderer> cls) {
        for (ContextualCardRenderer next : this.mRenderers) {
            if (next.getClass() == cls) {
                Log.d("ControllerRendererPool", "Renderer is already there.");
                return next;
            }
        }
        ContextualCardRenderer createCardRenderer = createCardRenderer(context, lifecycleOwner, cls);
        if (createCardRenderer != null) {
            this.mRenderers.add(createCardRenderer);
        }
        return createCardRenderer;
    }

    private ContextualCardController createCardController(Context context, Class<? extends ContextualCardController> cls) {
        if (ConditionContextualCardController.class == cls) {
            return new ConditionContextualCardController(context);
        }
        if (SliceContextualCardController.class == cls) {
            return new SliceContextualCardController(context);
        }
        if (LegacySuggestionContextualCardController.class == cls) {
            return new LegacySuggestionContextualCardController(context);
        }
        return null;
    }

    private ContextualCardRenderer createCardRenderer(Context context, LifecycleOwner lifecycleOwner, Class<?> cls) {
        if (ConditionContextualCardRenderer.class == cls) {
            return new ConditionContextualCardRenderer(context, this);
        }
        if (SliceContextualCardRenderer.class == cls) {
            return new SliceContextualCardRenderer(context, lifecycleOwner, this);
        }
        if (LegacySuggestionContextualCardRenderer.class == cls) {
            return new LegacySuggestionContextualCardRenderer(context, this);
        }
        if (ConditionFooterContextualCardRenderer.class == cls) {
            return new ConditionFooterContextualCardRenderer(context, this);
        }
        if (ConditionHeaderContextualCardRenderer.class == cls) {
            return new ConditionHeaderContextualCardRenderer(context, this);
        }
        return null;
    }
}
