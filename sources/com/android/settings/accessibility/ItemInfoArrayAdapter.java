package com.android.settings.accessibility;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.accessibility.ItemInfoArrayAdapter.ItemInfo;
import java.util.List;

public class ItemInfoArrayAdapter<T extends ItemInfo> extends ArrayAdapter<T> {
    public ItemInfoArrayAdapter(Context context, List<T> list) {
        super(context, R.layout.dialog_single_radio_choice_list_item, R.id.title, list);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = super.getView(i, view, viewGroup);
        ItemInfo itemInfo = (ItemInfo) getItem(i);
        ((TextView) view2.findViewById(R.id.title)).setText(itemInfo.mTitle);
        TextView textView = (TextView) view2.findViewById(R.id.summary);
        if (!TextUtils.isEmpty(itemInfo.mSummary)) {
            textView.setVisibility(0);
            textView.setText(itemInfo.mSummary);
        } else {
            textView.setVisibility(8);
        }
        ((ImageView) view2.findViewById(R.id.image)).setImageResource(itemInfo.mDrawableId);
        return view2;
    }

    public static class ItemInfo {
        public final int mDrawableId;
        public final CharSequence mSummary;
        public final CharSequence mTitle;

        public ItemInfo(CharSequence charSequence, CharSequence charSequence2, int i) {
            this.mTitle = charSequence;
            this.mSummary = charSequence2;
            this.mDrawableId = i;
        }
    }
}
