package com.android.settings.development.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settings.R;

class LeaseInfoViewHolder {
    ImageView appIcon;
    TextView leaseDescription;
    TextView leaseExpiry;
    TextView leasePackageName;
    View rootView;

    LeaseInfoViewHolder() {
    }

    static LeaseInfoViewHolder createOrRecycle(LayoutInflater layoutInflater, View view) {
        if (view != null) {
            return (LeaseInfoViewHolder) view.getTag();
        }
        View inflate = layoutInflater.inflate(R.layout.lease_list_item_view, (ViewGroup) null);
        LeaseInfoViewHolder leaseInfoViewHolder = new LeaseInfoViewHolder();
        leaseInfoViewHolder.rootView = inflate;
        leaseInfoViewHolder.appIcon = (ImageView) inflate.findViewById(R.id.app_icon);
        leaseInfoViewHolder.leasePackageName = (TextView) inflate.findViewById(R.id.lease_package);
        leaseInfoViewHolder.leaseDescription = (TextView) inflate.findViewById(R.id.lease_desc);
        leaseInfoViewHolder.leaseExpiry = (TextView) inflate.findViewById(R.id.lease_expiry);
        inflate.setTag(leaseInfoViewHolder);
        return leaseInfoViewHolder;
    }
}
