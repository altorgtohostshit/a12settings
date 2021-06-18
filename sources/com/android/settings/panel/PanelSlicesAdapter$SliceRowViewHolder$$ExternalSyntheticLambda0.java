package com.android.settings.panel;

import androidx.lifecycle.LiveData;
import androidx.slice.SliceItem;
import androidx.slice.widget.EventInfo;
import androidx.slice.widget.SliceView;
import com.android.settings.panel.PanelSlicesAdapter;

public final /* synthetic */ class PanelSlicesAdapter$SliceRowViewHolder$$ExternalSyntheticLambda0 implements SliceView.OnSliceActionListener {
    public final /* synthetic */ PanelSlicesAdapter.SliceRowViewHolder f$0;
    public final /* synthetic */ LiveData f$1;

    public /* synthetic */ PanelSlicesAdapter$SliceRowViewHolder$$ExternalSyntheticLambda0(PanelSlicesAdapter.SliceRowViewHolder sliceRowViewHolder, LiveData liveData) {
        this.f$0 = sliceRowViewHolder;
        this.f$1 = liveData;
    }

    public final void onSliceAction(EventInfo eventInfo, SliceItem sliceItem) {
        this.f$0.lambda$onBind$0(this.f$1, eventInfo, sliceItem);
    }
}
