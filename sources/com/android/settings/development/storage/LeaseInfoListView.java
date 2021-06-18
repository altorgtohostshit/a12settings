package com.android.settings.development.storage;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.blob.BlobInfo;
import android.app.blob.BlobStoreManager;
import android.app.blob.LeaseInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.internal.util.CollectionUtils;
import com.android.settings.R;
import java.io.IOException;
import java.util.List;

public class LeaseInfoListView extends ListActivity {
    private LeaseListAdapter mAdapter;
    /* access modifiers changed from: private */
    public BlobInfo mBlobInfo;
    private BlobStoreManager mBlobStoreManager;
    private Context mContext;
    /* access modifiers changed from: private */
    public LayoutInflater mInflater;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        this.mBlobStoreManager = (BlobStoreManager) getSystemService(BlobStoreManager.class);
        this.mInflater = (LayoutInflater) getSystemService(LayoutInflater.class);
        this.mBlobInfo = getIntent().getParcelableExtra("BLOB_KEY");
        LeaseListAdapter leaseListAdapter = new LeaseListAdapter(this);
        this.mAdapter = leaseListAdapter;
        if (leaseListAdapter.isEmpty()) {
            Log.e("LeaseInfoListView", "Error fetching leases for shared data: " + this.mBlobInfo.toString());
            finish();
        }
        setListAdapter(this.mAdapter);
        getListView().addHeaderView(getHeaderView());
        getListView().addFooterView(getFooterView());
        getListView().setClickable(false);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onNavigateUp() {
        finish();
        return true;
    }

    private LinearLayout getHeaderView() {
        LinearLayout linearLayout = (LinearLayout) this.mInflater.inflate(R.layout.blob_list_item_view, (ViewGroup) null);
        linearLayout.setEnabled(false);
        TextView textView = (TextView) linearLayout.findViewById(R.id.blob_label);
        textView.setText(this.mBlobInfo.getLabel());
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        ((TextView) linearLayout.findViewById(R.id.blob_id)).setText(getString(R.string.blob_id_text, new Object[]{Long.valueOf(this.mBlobInfo.getId())}));
        ((TextView) linearLayout.findViewById(R.id.blob_expiry)).setVisibility(8);
        ((TextView) linearLayout.findViewById(R.id.blob_size)).setText(SharedDataUtils.formatSize(this.mBlobInfo.getSizeBytes()));
        return linearLayout;
    }

    private Button getFooterView() {
        Button button = new Button(this);
        button.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        button.setText(R.string.delete_blob_text);
        button.setOnClickListener(getButtonOnClickListener());
        return button;
    }

    private View.OnClickListener getButtonOnClickListener() {
        return new LeaseInfoListView$$ExternalSyntheticLambda1(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getButtonOnClickListener$0(View view) {
        new AlertDialog.Builder(this.mContext).setMessage((int) R.string.delete_blob_confirmation_text).setPositiveButton(17039370, getDialogOnClickListener()).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create().show();
    }

    private DialogInterface.OnClickListener getDialogOnClickListener() {
        return new LeaseInfoListView$$ExternalSyntheticLambda0(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getDialogOnClickListener$1(DialogInterface dialogInterface, int i) {
        try {
            this.mBlobStoreManager.deleteBlob(this.mBlobInfo);
            setResult(1);
        } catch (IOException e) {
            Log.e("LeaseInfoListView", "Unable to delete blob: " + e.getMessage());
            setResult(-1);
        }
        finish();
    }

    private class LeaseListAdapter extends ArrayAdapter<LeaseInfo> {
        private Context mContext;

        LeaseListAdapter(Context context) {
            super(context, 0);
            this.mContext = context;
            List leases = LeaseInfoListView.this.mBlobInfo.getLeases();
            if (!CollectionUtils.isEmpty(leases)) {
                addAll(leases);
            }
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            Drawable drawable;
            LeaseInfoViewHolder createOrRecycle = LeaseInfoViewHolder.createOrRecycle(LeaseInfoListView.this.mInflater, view);
            View view2 = createOrRecycle.rootView;
            view2.setEnabled(false);
            LeaseInfo leaseInfo = (LeaseInfo) getItem(i);
            try {
                drawable = this.mContext.getPackageManager().getApplicationIcon(leaseInfo.getPackageName());
            } catch (PackageManager.NameNotFoundException unused) {
                drawable = this.mContext.getDrawable(17301651);
            }
            createOrRecycle.appIcon.setImageDrawable(drawable);
            createOrRecycle.leasePackageName.setText(leaseInfo.getPackageName());
            createOrRecycle.leaseDescription.setText(getDescriptionString(leaseInfo));
            createOrRecycle.leaseExpiry.setText(LeaseInfoListView.this.getString(R.string.accessor_expires_text, new Object[]{SharedDataUtils.formatTime(leaseInfo.getExpiryTimeMillis())}));
            return view2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0023, code lost:
            r1 = r5.getDescription().toString();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
            if (android.text.TextUtils.isEmpty(r1) == false) goto L_0x0032;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0038, code lost:
            if (android.text.TextUtils.isEmpty((java.lang.CharSequence) null) != false) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x003a, code lost:
            r4.this$0.getString(com.android.settings.R.string.accessor_no_description_text);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x003f, code lost:
            throw r5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
            return r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x001b, code lost:
            r5 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0021, code lost:
            if (r5.getDescription() != null) goto L_0x0023;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001d */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.lang.String getDescriptionString(android.app.blob.LeaseInfo r5) {
            /*
                r4 = this;
                r0 = 2130968935(0x7f040167, float:1.7546538E38)
                r1 = 0
                com.android.settings.development.storage.LeaseInfoListView r2 = com.android.settings.development.storage.LeaseInfoListView.this     // Catch:{ NotFoundException -> 0x001d }
                int r3 = r5.getDescriptionResId()     // Catch:{ NotFoundException -> 0x001d }
                java.lang.String r5 = r2.getString(r3)     // Catch:{ NotFoundException -> 0x001d }
                boolean r1 = android.text.TextUtils.isEmpty(r5)
                if (r1 == 0) goto L_0x0033
            L_0x0014:
                com.android.settings.development.storage.LeaseInfoListView r4 = com.android.settings.development.storage.LeaseInfoListView.this
                java.lang.String r5 = r4.getString(r0)
                goto L_0x0033
            L_0x001b:
                r5 = move-exception
                goto L_0x0034
            L_0x001d:
                java.lang.CharSequence r2 = r5.getDescription()     // Catch:{ all -> 0x001b }
                if (r2 == 0) goto L_0x002b
                java.lang.CharSequence r5 = r5.getDescription()     // Catch:{ all -> 0x001b }
                java.lang.String r1 = r5.toString()     // Catch:{ all -> 0x001b }
            L_0x002b:
                boolean r5 = android.text.TextUtils.isEmpty(r1)
                if (r5 == 0) goto L_0x0032
                goto L_0x0014
            L_0x0032:
                r5 = r1
            L_0x0033:
                return r5
            L_0x0034:
                boolean r1 = android.text.TextUtils.isEmpty(r1)
                if (r1 == 0) goto L_0x003f
                com.android.settings.development.storage.LeaseInfoListView r4 = com.android.settings.development.storage.LeaseInfoListView.this
                r4.getString(r0)
            L_0x003f:
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.development.storage.LeaseInfoListView.LeaseListAdapter.getDescriptionString(android.app.blob.LeaseInfo):java.lang.String");
        }
    }
}
