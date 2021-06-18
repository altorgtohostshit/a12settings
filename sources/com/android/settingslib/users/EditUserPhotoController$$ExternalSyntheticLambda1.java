package com.android.settingslib.users;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;

public final /* synthetic */ class EditUserPhotoController$$ExternalSyntheticLambda1 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ ListPopupWindow f$0;

    public /* synthetic */ EditUserPhotoController$$ExternalSyntheticLambda1(ListPopupWindow listPopupWindow) {
        this.f$0 = listPopupWindow;
    }

    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        EditUserPhotoController.lambda$showUpdatePhotoPopup$1(this.f$0, adapterView, view, i, j);
    }
}
