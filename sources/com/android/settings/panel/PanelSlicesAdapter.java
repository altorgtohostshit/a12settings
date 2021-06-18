package com.android.settings.panel;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.widget.EventInfo;
import androidx.slice.widget.SliceView;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.CustomSliceRegistry;
import com.google.android.setupdesign.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PanelSlicesAdapter extends RecyclerView.Adapter<SliceRowViewHolder> {
    static final int MAX_NUM_OF_SLICES = 6;
    /* access modifiers changed from: private */
    public final int mMetricsCategory;
    /* access modifiers changed from: private */
    public final PanelFragment mPanelFragment;
    private final List<LiveData<Slice>> mSliceLiveData;

    public PanelSlicesAdapter(PanelFragment panelFragment, Map<Uri, LiveData<Slice>> map, int i) {
        this.mPanelFragment = panelFragment;
        this.mSliceLiveData = new ArrayList(map.values());
        this.mMetricsCategory = i;
    }

    public SliceRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 1) {
            view = from.inflate(R.layout.panel_slice_slider_row, viewGroup, false);
        } else {
            view = from.inflate(R.layout.panel_slice_row, viewGroup, false);
        }
        return new SliceRowViewHolder(view);
    }

    public void onBindViewHolder(SliceRowViewHolder sliceRowViewHolder, int i) {
        sliceRowViewHolder.onBind(this.mSliceLiveData.get(i), i);
    }

    public int getItemCount() {
        return Math.min(this.mSliceLiveData.size(), 6);
    }

    public int getItemViewType(int i) {
        return this.mPanelFragment.getPanelViewType();
    }

    /* access modifiers changed from: package-private */
    public List<LiveData<Slice>> getData() {
        return this.mSliceLiveData.subList(0, getItemCount());
    }

    public class SliceRowViewHolder extends RecyclerView.ViewHolder implements DividerItemDecoration.DividedViewHolder {
        private boolean mDividerAllowedAbove = true;
        final LinearLayout mSliceSliderLayout;
        final SliceView sliceView;

        public boolean isDividerAllowedBelow() {
            return true;
        }

        public SliceRowViewHolder(View view) {
            super(view);
            SliceView sliceView2 = (SliceView) view.findViewById(R.id.slice_view);
            this.sliceView = sliceView2;
            sliceView2.setMode(2);
            sliceView2.setShowTitleItems(true);
            this.mSliceSliderLayout = (LinearLayout) view.findViewById(R.id.slice_slider_layout);
        }

        public void onBind(LiveData<Slice> liveData, int i) {
            liveData.observe(PanelSlicesAdapter.this.mPanelFragment.getViewLifecycleOwner(), this.sliceView);
            Slice value = liveData.getValue();
            if (value == null || value.getUri().equals(CustomSliceRegistry.MEDIA_OUTPUT_INDICATOR_SLICE_URI)) {
                this.mDividerAllowedAbove = false;
            }
            this.sliceView.setOnSliceActionListener(new PanelSlicesAdapter$SliceRowViewHolder$$ExternalSyntheticLambda0(this, liveData));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$0(LiveData liveData, EventInfo eventInfo, SliceItem sliceItem) {
            FeatureFactory.getFactory(this.sliceView.getContext()).getMetricsFeatureProvider().action(0, 1658, PanelSlicesAdapter.this.mMetricsCategory, ((Slice) liveData.getValue()).getUri().getLastPathSegment(), eventInfo.actionType);
        }

        public boolean isDividerAllowedAbove() {
            return this.mDividerAllowedAbove;
        }
    }
}
