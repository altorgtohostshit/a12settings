package com.android.settings.development.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.settings.R;

class BlobInfoViewHolder {
    TextView blobExpiry;
    TextView blobId;
    TextView blobLabel;
    TextView blobSize;
    View rootView;

    BlobInfoViewHolder() {
    }

    static BlobInfoViewHolder createOrRecycle(LayoutInflater layoutInflater, View view) {
        if (view != null) {
            return (BlobInfoViewHolder) view.getTag();
        }
        View inflate = layoutInflater.inflate(R.layout.blob_list_item_view, (ViewGroup) null);
        BlobInfoViewHolder blobInfoViewHolder = new BlobInfoViewHolder();
        blobInfoViewHolder.rootView = inflate;
        blobInfoViewHolder.blobLabel = (TextView) inflate.findViewById(R.id.blob_label);
        blobInfoViewHolder.blobId = (TextView) inflate.findViewById(R.id.blob_id);
        blobInfoViewHolder.blobExpiry = (TextView) inflate.findViewById(R.id.blob_expiry);
        blobInfoViewHolder.blobSize = (TextView) inflate.findViewById(R.id.blob_size);
        inflate.setTag(blobInfoViewHolder);
        return blobInfoViewHolder;
    }
}
