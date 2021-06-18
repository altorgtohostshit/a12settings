package com.android.settings.security;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CredentialManagementAppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /* access modifiers changed from: private */
    public final Map<String, Map<Uri, String>> mAppUriAuthentication;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final String mCredentialManagerPackage;
    /* access modifiers changed from: private */
    public final boolean mIncludeExpander;
    private final boolean mIncludeHeader;
    /* access modifiers changed from: private */
    public final boolean mIsLayoutRtl;
    /* access modifiers changed from: private */
    public final PackageManager mPackageManager;
    /* access modifiers changed from: private */
    public final List<String> mSortedAppNames;
    /* access modifiers changed from: private */
    public final RecyclerView.RecycledViewPool mViewPool = new RecyclerView.RecycledViewPool();

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mAppIconView;
        private final TextView mTitleView;

        public HeaderViewHolder(View view) {
            super(view);
            this.mAppIconView = (ImageView) view.findViewById(R.id.credential_management_app_icon);
            this.mTitleView = (TextView) view.findViewById(R.id.credential_management_app_title);
        }

        public void bindView() {
            try {
                ApplicationInfo applicationInfo = CredentialManagementAppAdapter.this.mPackageManager.getApplicationInfo(CredentialManagementAppAdapter.this.mCredentialManagerPackage, 0);
                this.mAppIconView.setImageDrawable(CredentialManagementAppAdapter.this.mPackageManager.getApplicationIcon(applicationInfo));
                this.mTitleView.setText(CredentialManagementAppAdapter.this.mContext.getString(R.string.request_manage_credentials_title, new Object[]{applicationInfo.loadLabel(CredentialManagementAppAdapter.this.mPackageManager)}));
            } catch (PackageManager.NameNotFoundException unused) {
                this.mAppIconView.setImageDrawable((Drawable) null);
                this.mTitleView.setText(CredentialManagementAppAdapter.this.mContext.getString(R.string.request_manage_credentials_title, new Object[]{CredentialManagementAppAdapter.this.mCredentialManagerPackage}));
            }
        }
    }

    public class AppAuthenticationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mAppIconView;
        private final TextView mAppNameView;
        private final RecyclerView mChildRecyclerView;
        private final List<String> mExpandedApps = new ArrayList();
        private final ImageView mExpanderIconView;
        private final TextView mNumberOfUrisView;

        public AppAuthenticationViewHolder(View view) {
            super(view);
            this.mAppIconView = (ImageView) view.findViewById(R.id.app_icon);
            this.mAppNameView = (TextView) view.findViewById(R.id.app_name);
            this.mNumberOfUrisView = (TextView) view.findViewById(R.id.number_of_uris);
            ImageView imageView = (ImageView) view.findViewById(R.id.expand);
            this.mExpanderIconView = imageView;
            this.mChildRecyclerView = (RecyclerView) view.findViewById(R.id.uris);
            if (CredentialManagementAppAdapter.this.mIsLayoutRtl) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((RelativeLayout) view.findViewById(R.id.app_details)).getLayoutParams();
                layoutParams.addRule(0, R.id.app_icon);
                layoutParams.addRule(1, R.id.expand);
                view.setLayoutParams(layoutParams);
            }
            imageView.setOnClickListener(new C1242xabc7f6bd(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            String str = (String) CredentialManagementAppAdapter.this.mSortedAppNames.get(getBindingAdapterPosition());
            if (this.mExpandedApps.contains(str)) {
                this.mExpandedApps.remove(str);
            } else {
                this.mExpandedApps.add(str);
            }
            bindPolicyView(str);
        }

        public void bindView(int i) {
            String str = (String) CredentialManagementAppAdapter.this.mSortedAppNames.get(i);
            try {
                ApplicationInfo applicationInfo = CredentialManagementAppAdapter.this.mPackageManager.getApplicationInfo(str, 0);
                this.mAppIconView.setImageDrawable(CredentialManagementAppAdapter.this.mPackageManager.getApplicationIcon(applicationInfo));
                this.mAppNameView.setText(String.valueOf(applicationInfo.loadLabel(CredentialManagementAppAdapter.this.mPackageManager)));
            } catch (PackageManager.NameNotFoundException unused) {
                this.mAppIconView.setImageDrawable((Drawable) null);
                this.mAppNameView.setText(str);
            }
            bindPolicyView(str);
        }

        private void bindPolicyView(String str) {
            if (CredentialManagementAppAdapter.this.mIncludeExpander) {
                this.mExpanderIconView.setVisibility(0);
                if (this.mExpandedApps.contains(str)) {
                    this.mNumberOfUrisView.setVisibility(8);
                    this.mExpanderIconView.setImageResource(R.drawable.ic_expand_less);
                    bindChildView((Map) CredentialManagementAppAdapter.this.mAppUriAuthentication.get(str));
                    return;
                }
                this.mChildRecyclerView.setVisibility(8);
                this.mNumberOfUrisView.setVisibility(0);
                this.mNumberOfUrisView.setText(getNumberOfUrlsText((Map) CredentialManagementAppAdapter.this.mAppUriAuthentication.get(str)));
                this.mExpanderIconView.setImageResource(17302432);
                return;
            }
            this.mNumberOfUrisView.setVisibility(8);
            this.mExpanderIconView.setVisibility(8);
            bindChildView((Map) CredentialManagementAppAdapter.this.mAppUriAuthentication.get(str));
        }

        private void bindChildView(Map<Uri, String> map) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mChildRecyclerView.getContext(), 1, false);
            linearLayoutManager.setInitialPrefetchItemCount(map.size());
            UriAuthenticationPolicyAdapter uriAuthenticationPolicyAdapter = new UriAuthenticationPolicyAdapter(new ArrayList(map.keySet()));
            this.mChildRecyclerView.setLayoutManager(linearLayoutManager);
            this.mChildRecyclerView.setVisibility(0);
            this.mChildRecyclerView.setAdapter(uriAuthenticationPolicyAdapter);
            this.mChildRecyclerView.setRecycledViewPool(CredentialManagementAppAdapter.this.mViewPool);
        }

        private String getNumberOfUrlsText(Map<Uri, String> map) {
            return CredentialManagementAppAdapter.this.mContext.getResources().getQuantityString(R.plurals.number_of_urls, map.size(), new Object[]{Integer.valueOf(map.size())});
        }
    }

    public CredentialManagementAppAdapter(Context context, String str, Map<String, Map<Uri, String>> map, boolean z, boolean z2) {
        this.mContext = context;
        this.mCredentialManagerPackage = str;
        this.mPackageManager = context.getPackageManager();
        this.mAppUriAuthentication = map;
        this.mSortedAppNames = sortPackageNames(map);
        this.mIncludeHeader = z;
        this.mIncludeExpander = z2;
        this.mIsLayoutRtl = context.getResources().getConfiguration().getLayoutDirection() != 1 ? false : true;
    }

    private List<String> sortPackageNames(Map<String, Map<Uri, String>> map) {
        ArrayList arrayList = new ArrayList(map.keySet());
        arrayList.sort(new CredentialManagementAppAdapter$$ExternalSyntheticLambda0(this));
        return arrayList;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$sortPackageNames$0(String str, String str2) {
        boolean isPackageInstalled = isPackageInstalled(str);
        if (isPackageInstalled == isPackageInstalled(str2)) {
            return str.compareTo(str2);
        }
        return isPackageInstalled ? -1 : 1;
    }

    private boolean isPackageInstalled(String str) {
        try {
            this.mPackageManager.getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != 1) {
            return new AppAuthenticationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_authentication_item, viewGroup, false));
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_manage_credentials_header, viewGroup, false);
        inflate.setEnabled(false);
        return new HeaderViewHolder(inflate);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) viewHolder).bindView();
        } else if (viewHolder instanceof AppAuthenticationViewHolder) {
            if (this.mIncludeHeader) {
                i--;
            }
            ((AppAuthenticationViewHolder) viewHolder).bindView(i);
        }
    }

    public int getItemCount() {
        boolean z = this.mIncludeHeader;
        int size = this.mAppUriAuthentication.size();
        return z ? size + 1 : size;
    }

    public int getItemViewType(int i) {
        if (!this.mIncludeHeader || i != 0) {
            return super.getItemViewType(i);
        }
        return 1;
    }
}
