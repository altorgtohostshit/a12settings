package com.android.settings.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.Slice;
import androidx.slice.SliceMetadata;
import androidx.slice.widget.SliceLiveData;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;
import com.google.android.setupdesign.DividerItemDecoration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PanelFragment extends Fragment {
    private PanelSlicesAdapter mAdapter;
    private Button mDoneButton;
    private View mFooterDivider;
    private View mHeaderDivider;
    private LinearLayout mHeaderLayout;
    private TextView mHeaderSubtitle;
    private TextView mHeaderTitle;
    @VisibleForTesting
    View mLayoutView;
    /* access modifiers changed from: private */
    public int mMaxHeight;
    private MetricsFeatureProvider mMetricsProvider;
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            PanelFragment.this.animateIn();
            if (PanelFragment.this.mPanelSlices != null) {
                PanelFragment.this.mPanelSlices.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            boolean unused = PanelFragment.this.mPanelCreating = false;
        }
    };
    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = PanelFragment$$ExternalSyntheticLambda3.INSTANCE;
    /* access modifiers changed from: private */
    public PanelContent mPanel;
    /* access modifiers changed from: private */
    public String mPanelClosedKey;
    /* access modifiers changed from: private */
    public boolean mPanelCreating;
    private LinearLayout mPanelHeader;
    private final ViewTreeObserver.OnGlobalLayoutListener mPanelLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            if (PanelFragment.this.mLayoutView.getHeight() > PanelFragment.this.mMaxHeight) {
                ViewGroup.LayoutParams layoutParams = PanelFragment.this.mLayoutView.getLayoutParams();
                layoutParams.height = PanelFragment.this.mMaxHeight;
                PanelFragment.this.mLayoutView.setLayoutParams(layoutParams);
            }
        }
    };
    /* access modifiers changed from: private */
    public RecyclerView mPanelSlices;
    @VisibleForTesting
    PanelSlicesLoaderCountdownLatch mPanelSlicesLoaderCountdownLatch;
    private ProgressBar mProgressBar;
    private Button mSeeMoreButton;
    private final Map<Uri, LiveData<Slice>> mSliceLiveData = new LinkedHashMap();
    private LinearLayout mTitleGroup;
    private ImageView mTitleIcon;
    private TextView mTitleView;

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$0() {
        return false;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.panel_layout, viewGroup, false);
        this.mLayoutView = inflate;
        inflate.getViewTreeObserver().addOnGlobalLayoutListener(this.mPanelLayoutListener);
        this.mMaxHeight = getResources().getDimensionPixelSize(R.dimen.output_switcher_slice_max_height);
        this.mPanelCreating = true;
        createPanelContent();
        return this.mLayoutView;
    }

    /* access modifiers changed from: package-private */
    public void updatePanelWithAnimation() {
        this.mPanelCreating = true;
        AnimatorSet buildAnimatorSet = buildAnimatorSet(this.mLayoutView, 0.0f, (float) this.mLayoutView.findViewById(R.id.panel_container).getHeight(), 1.0f, 0.0f, 200);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        buildAnimatorSet.play(valueAnimator);
        buildAnimatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                PanelFragment.this.createPanelContent();
            }
        });
        buildAnimatorSet.start();
    }

    /* access modifiers changed from: package-private */
    public boolean isPanelCreating() {
        return this.mPanelCreating;
    }

    /* access modifiers changed from: private */
    public void createPanelContent() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            View view = this.mLayoutView;
            if (view == null) {
                activity.finish();
                return;
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = -2;
            this.mLayoutView.setLayoutParams(layoutParams);
            this.mPanelSlices = (RecyclerView) this.mLayoutView.findViewById(R.id.panel_parent_layout);
            this.mSeeMoreButton = (Button) this.mLayoutView.findViewById(R.id.see_more);
            this.mDoneButton = (Button) this.mLayoutView.findViewById(R.id.done);
            this.mTitleView = (TextView) this.mLayoutView.findViewById(R.id.panel_title);
            this.mPanelHeader = (LinearLayout) this.mLayoutView.findViewById(R.id.panel_header);
            this.mTitleIcon = (ImageView) this.mLayoutView.findViewById(R.id.title_icon);
            this.mTitleGroup = (LinearLayout) this.mLayoutView.findViewById(R.id.title_group);
            this.mHeaderLayout = (LinearLayout) this.mLayoutView.findViewById(R.id.header_layout);
            this.mHeaderTitle = (TextView) this.mLayoutView.findViewById(R.id.header_title);
            this.mHeaderSubtitle = (TextView) this.mLayoutView.findViewById(R.id.header_subtitle);
            this.mFooterDivider = this.mLayoutView.findViewById(R.id.footer_divider);
            this.mProgressBar = (ProgressBar) this.mLayoutView.findViewById(R.id.progress_bar);
            this.mHeaderDivider = this.mLayoutView.findViewById(R.id.header_divider);
            this.mPanelSlices.setVisibility(8);
            Bundle arguments = getArguments();
            String string = arguments.getString("PANEL_CALLING_PACKAGE_NAME");
            PanelContent panel = FeatureFactory.getFactory(activity).getPanelFeatureProvider().getPanel(activity, arguments);
            this.mPanel = panel;
            if (panel == null) {
                activity.finish();
                return;
            }
            panel.registerCallback(new LocalPanelCallback());
            if (this.mPanel instanceof LifecycleObserver) {
                getLifecycle().addObserver((LifecycleObserver) this.mPanel);
            }
            this.mMetricsProvider = FeatureFactory.getFactory(activity).getMetricsFeatureProvider();
            updateProgressBar();
            this.mPanelSlices.setLayoutManager(new LinearLayoutManager(activity));
            this.mLayoutView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
            loadAllSlices();
            IconCompat icon = this.mPanel.getIcon();
            CharSequence title = this.mPanel.getTitle();
            CharSequence subTitle = this.mPanel.getSubTitle();
            if (icon != null || (subTitle != null && subTitle.length() > 0)) {
                enablePanelHeader(icon, title, subTitle);
            } else {
                enableTitle(title);
            }
            this.mFooterDivider.setVisibility(8);
            this.mSeeMoreButton.setOnClickListener(getSeeMoreListener());
            this.mDoneButton.setOnClickListener(getCloseListener());
            if (this.mPanel.isCustomizedButtonUsed()) {
                enableCustomizedButton();
            } else if (this.mPanel.getSeeMoreIntent() == null) {
                this.mSeeMoreButton.setVisibility(8);
            }
            this.mMetricsProvider.action(0, 1, this.mPanel.getMetricsCategory(), string, 0);
        }
    }

    /* access modifiers changed from: private */
    public void enablePanelHeader(IconCompat iconCompat, CharSequence charSequence, CharSequence charSequence2) {
        this.mTitleView.setVisibility(8);
        this.mPanelHeader.setVisibility(0);
        this.mPanelHeader.setAccessibilityPaneTitle(charSequence);
        this.mHeaderTitle.setText(charSequence);
        this.mHeaderSubtitle.setText(charSequence2);
        this.mHeaderSubtitle.setAccessibilityPaneTitle(charSequence2);
        if (iconCompat != null) {
            this.mTitleGroup.setVisibility(0);
            this.mHeaderLayout.setGravity(3);
            this.mTitleIcon.setImageIcon(iconCompat.toIcon(getContext()));
            if (this.mPanel.getHeaderIconIntent() != null) {
                this.mTitleIcon.setOnClickListener(getHeaderIconListener());
                this.mTitleIcon.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                return;
            }
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.output_switcher_panel_icon_size);
            this.mTitleIcon.setLayoutParams(new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize));
            return;
        }
        this.mTitleGroup.setVisibility(8);
        this.mHeaderLayout.setGravity(1);
    }

    /* access modifiers changed from: private */
    public void enableTitle(CharSequence charSequence) {
        this.mPanelHeader.setVisibility(8);
        this.mTitleView.setVisibility(0);
        this.mTitleView.setAccessibilityPaneTitle(charSequence);
        this.mTitleView.setText(charSequence);
    }

    /* access modifiers changed from: private */
    public void enableCustomizedButton() {
        CharSequence customizedButtonTitle = this.mPanel.getCustomizedButtonTitle();
        if (TextUtils.isEmpty(customizedButtonTitle)) {
            this.mSeeMoreButton.setVisibility(8);
            return;
        }
        this.mSeeMoreButton.setVisibility(0);
        this.mSeeMoreButton.setText(customizedButtonTitle);
    }

    /* access modifiers changed from: private */
    public void updateProgressBar() {
        if (this.mPanel.isProgressBarVisible()) {
            this.mProgressBar.setVisibility(0);
            this.mHeaderDivider.setVisibility(8);
            return;
        }
        this.mProgressBar.setVisibility(8);
        this.mHeaderDivider.setVisibility(0);
    }

    private void loadAllSlices() {
        this.mSliceLiveData.clear();
        List<Uri> slices = this.mPanel.getSlices();
        this.mPanelSlicesLoaderCountdownLatch = new PanelSlicesLoaderCountdownLatch(slices.size());
        for (Uri next : slices) {
            LiveData<Slice> fromUri = SliceLiveData.fromUri(getActivity(), next, new PanelFragment$$ExternalSyntheticLambda5(this, next));
            this.mSliceLiveData.put(next, fromUri);
            fromUri.observe(getViewLifecycleOwner(), new PanelFragment$$ExternalSyntheticLambda4(this, next));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAllSlices$1(Uri uri, int i, Throwable th) {
        removeSliceLiveData(uri);
        this.mPanelSlicesLoaderCountdownLatch.markSliceLoaded(uri);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAllSlices$3(Uri uri, Slice slice) {
        if (!this.mPanelSlicesLoaderCountdownLatch.isSliceLoaded(uri)) {
            SliceMetadata from = SliceMetadata.from(getActivity(), slice);
            if (slice == null || from.isErrorSlice()) {
                removeSliceLiveData(uri);
                this.mPanelSlicesLoaderCountdownLatch.markSliceLoaded(uri);
            } else if (from.getLoadingState() == 2) {
                this.mPanelSlicesLoaderCountdownLatch.markSliceLoaded(uri);
            } else {
                new Handler().postDelayed(new PanelFragment$$ExternalSyntheticLambda6(this, uri), 250);
            }
            loadPanelWhenReady();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAllSlices$2(Uri uri) {
        this.mPanelSlicesLoaderCountdownLatch.markSliceLoaded(uri);
        loadPanelWhenReady();
    }

    private void removeSliceLiveData(Uri uri) {
        if (!Arrays.asList(getResources().getStringArray(R.array.config_panel_keep_observe_uri)).contains(uri.toString())) {
            this.mSliceLiveData.remove(uri);
        }
    }

    private void loadPanelWhenReady() {
        if (this.mPanelSlicesLoaderCountdownLatch.isPanelReadyToLoad()) {
            PanelSlicesAdapter panelSlicesAdapter = new PanelSlicesAdapter(this, this.mSliceLiveData, this.mPanel.getMetricsCategory());
            this.mAdapter = panelSlicesAdapter;
            this.mPanelSlices.setAdapter(panelSlicesAdapter);
            this.mPanelSlices.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
            this.mPanelSlices.setVisibility(0);
            FragmentActivity activity = getActivity();
            if (activity != null) {
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activity);
                dividerItemDecoration.setDividerCondition(1);
                if (this.mPanelSlices.getItemDecorationCount() == 0) {
                    this.mPanelSlices.addItemDecoration(dividerItemDecoration);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void animateIn() {
        AnimatorSet buildAnimatorSet = buildAnimatorSet(this.mLayoutView, (float) this.mLayoutView.findViewById(R.id.panel_container).getHeight(), 0.0f, 0.0f, 1.0f, 250);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        buildAnimatorSet.play(valueAnimator);
        buildAnimatorSet.start();
        this.mLayoutView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
    }

    private static AnimatorSet buildAnimatorSet(View view, float f, float f2, float f3, float f4, int i) {
        View findViewById = view.findViewById(R.id.panel_container);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration((long) i);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(findViewById, View.TRANSLATION_Y, new float[]{f, f2}), ObjectAnimator.ofFloat(findViewById, View.ALPHA, new float[]{f3, f4})});
        return animatorSet;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (TextUtils.isEmpty(this.mPanelClosedKey)) {
            this.mPanelClosedKey = "others";
        }
        View view = this.mLayoutView;
        if (view != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this.mPanelLayoutListener);
        }
        PanelContent panelContent = this.mPanel;
        if (panelContent != null) {
            this.mMetricsProvider.action(0, 2, panelContent.getMetricsCategory(), this.mPanelClosedKey, 0);
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public View.OnClickListener getSeeMoreListener() {
        return new PanelFragment$$ExternalSyntheticLambda1(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSeeMoreListener$4(View view) {
        this.mPanelClosedKey = "see_more";
        FragmentActivity activity = getActivity();
        if (this.mPanel.isCustomizedButtonUsed()) {
            this.mPanel.onClickCustomizedButton();
        } else {
            activity.startActivityForResult(this.mPanel.getSeeMoreIntent(), 0);
        }
        activity.finish();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public View.OnClickListener getCloseListener() {
        return new PanelFragment$$ExternalSyntheticLambda0(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getCloseListener$5(View view) {
        this.mPanelClosedKey = "done";
        getActivity().finish();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public View.OnClickListener getHeaderIconListener() {
        return new PanelFragment$$ExternalSyntheticLambda2(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getHeaderIconListener$6(View view) {
        getActivity().startActivity(this.mPanel.getHeaderIconIntent());
    }

    /* access modifiers changed from: package-private */
    public int getPanelViewType() {
        return this.mPanel.getViewType();
    }

    class LocalPanelCallback implements PanelContentCallback {
        LocalPanelCallback() {
        }

        public void onCustomizedButtonStateChanged() {
            ThreadUtils.postOnMainThread(new PanelFragment$LocalPanelCallback$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onCustomizedButtonStateChanged$0() {
            PanelFragment.this.enableCustomizedButton();
        }

        public void onHeaderChanged() {
            ThreadUtils.postOnMainThread(new PanelFragment$LocalPanelCallback$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onHeaderChanged$1() {
            PanelFragment panelFragment = PanelFragment.this;
            panelFragment.enablePanelHeader(panelFragment.mPanel.getIcon(), PanelFragment.this.mPanel.getTitle(), PanelFragment.this.mPanel.getSubTitle());
        }

        public void forceClose() {
            String unused = PanelFragment.this.mPanelClosedKey = "others";
            getFragmentActivity().finish();
        }

        public void onTitleChanged() {
            ThreadUtils.postOnMainThread(new PanelFragment$LocalPanelCallback$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onTitleChanged$2() {
            PanelFragment panelFragment = PanelFragment.this;
            panelFragment.enableTitle(panelFragment.mPanel.getTitle());
        }

        public void onProgressBarVisibleChanged() {
            ThreadUtils.postOnMainThread(new PanelFragment$LocalPanelCallback$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onProgressBarVisibleChanged$3() {
            PanelFragment.this.updateProgressBar();
        }

        /* access modifiers changed from: package-private */
        @VisibleForTesting
        public FragmentActivity getFragmentActivity() {
            return PanelFragment.this.getActivity();
        }
    }
}
