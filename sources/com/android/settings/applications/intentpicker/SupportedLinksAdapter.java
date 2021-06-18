package com.android.settings.applications.intentpicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import com.android.settings.R;
import java.util.List;

public class SupportedLinksAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<SupportedLinkWrapper> mWrapperList;

    public long getItemId(int i) {
        return (long) i;
    }

    public SupportedLinksAdapter(Context context, List<SupportedLinkWrapper> list) {
        this.mContext = context;
        this.mWrapperList = list;
    }

    public int getCount() {
        return this.mWrapperList.size();
    }

    public Object getItem(int i) {
        if (i < this.mWrapperList.size()) {
            return this.mWrapperList.get(i);
        }
        return null;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.supported_links_dialog_item, (ViewGroup) null);
        }
        CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(16908308);
        checkedTextView.setText(this.mWrapperList.get(i).getDisplayTitle(this.mContext));
        checkedTextView.setEnabled(this.mWrapperList.get(i).isEnabled());
        checkedTextView.setChecked(this.mWrapperList.get(i).isChecked());
        checkedTextView.setOnClickListener(new SupportedLinksAdapter$$ExternalSyntheticLambda0(this, checkedTextView, i));
        return view;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getView$0(CheckedTextView checkedTextView, int i, View view) {
        checkedTextView.toggle();
        this.mWrapperList.get(i).setChecked(checkedTextView.isChecked());
    }
}
