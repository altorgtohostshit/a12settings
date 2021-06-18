package com.android.settings.print;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.print.PrintManager;
import android.print.PrinterDiscoverySession;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrintServiceInfo;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrintServiceSettingsFragment extends SettingsPreferenceFragment implements OnMainSwitchChangeListener, LoaderManager.LoaderCallbacks<List<PrintServiceInfo>> {
    private Intent mAddPrintersIntent;
    /* access modifiers changed from: private */
    public ComponentName mComponentName;
    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        public void onChanged() {
            invalidateOptionsMenuIfNeeded();
            PrintServiceSettingsFragment.this.updateEmptyView();
        }

        private void invalidateOptionsMenuIfNeeded() {
            int unfilteredCount = PrintServiceSettingsFragment.this.mPrintersAdapter.getUnfilteredCount();
            if ((PrintServiceSettingsFragment.this.mLastUnfilteredItemCount <= 0 && unfilteredCount > 0) || (PrintServiceSettingsFragment.this.mLastUnfilteredItemCount > 0 && unfilteredCount <= 0)) {
                PrintServiceSettingsFragment.this.getActivity().invalidateOptionsMenu();
            }
            int unused = PrintServiceSettingsFragment.this.mLastUnfilteredItemCount = unfilteredCount;
        }
    };
    /* access modifiers changed from: private */
    public int mLastUnfilteredItemCount;
    private String mPreferenceKey;
    /* access modifiers changed from: private */
    public PrintersAdapter mPrintersAdapter;
    private SearchView mSearchView;
    private boolean mServiceEnabled;
    private Intent mSettingsIntent;
    private SettingsMainSwitchBar mSwitchBar;

    public int getMetricsCategory() {
        return 79;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String string = getArguments().getString("EXTRA_TITLE");
        if (!TextUtils.isEmpty(string)) {
            getActivity().setTitle(string);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.mServiceEnabled = getArguments().getBoolean("EXTRA_CHECKED");
        return onCreateView;
    }

    public void onStart() {
        super.onStart();
        initComponents();
        updateUiForArguments();
        updateEmptyView();
        updateUiForServiceState();
    }

    public void onPause() {
        SearchView searchView = this.mSearchView;
        if (searchView != null) {
            searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) null);
        }
        super.onPause();
    }

    public void onStop() {
        super.onStop();
        this.mSwitchBar.removeOnSwitchChangeListener(this);
        this.mSwitchBar.hide();
        this.mPrintersAdapter.unregisterAdapterDataObserver(this.mDataObserver);
    }

    private void onPreferenceToggled(String str, boolean z) {
        ((PrintManager) getContext().getSystemService("print")).setPrintServiceEnabled(this.mComponentName, z);
    }

    /* access modifiers changed from: private */
    public void updateEmptyView() {
        ViewGroup viewGroup = (ViewGroup) getListView().getParent();
        View emptyView = getEmptyView();
        if (!this.mSwitchBar.isChecked()) {
            if (emptyView != null) {
                viewGroup.removeView(emptyView);
                emptyView = null;
            }
            if (emptyView == null) {
                View inflate = getActivity().getLayoutInflater().inflate(R.layout.empty_print_state, viewGroup, false);
                ((TextView) inflate.findViewById(R.id.message)).setText(R.string.print_service_disabled);
                viewGroup.addView(inflate);
                setEmptyView(inflate);
            }
        } else if (this.mPrintersAdapter.getUnfilteredCount() <= 0) {
            if (emptyView != null) {
                viewGroup.removeView(emptyView);
                emptyView = null;
            }
            if (emptyView == null) {
                View inflate2 = getActivity().getLayoutInflater().inflate(R.layout.empty_printers_list_service_enabled, viewGroup, false);
                viewGroup.addView(inflate2);
                setEmptyView(inflate2);
            }
        } else if (this.mPrintersAdapter.getItemCount() <= 0) {
            if (emptyView != null) {
                viewGroup.removeView(emptyView);
                emptyView = null;
            }
            if (emptyView == null) {
                View inflate3 = getActivity().getLayoutInflater().inflate(R.layout.empty_print_state, viewGroup, false);
                ((TextView) inflate3.findViewById(R.id.message)).setText(R.string.print_no_printers_found);
                viewGroup.addView(inflate3);
                setEmptyView(inflate3);
            }
        } else if (this.mPrintersAdapter.getItemCount() > 0 && emptyView != null) {
            viewGroup.removeView(emptyView);
        }
    }

    private void updateUiForServiceState() {
        if (this.mServiceEnabled) {
            this.mSwitchBar.setCheckedInternal(true);
            this.mPrintersAdapter.enable();
        } else {
            this.mSwitchBar.setCheckedInternal(false);
            this.mPrintersAdapter.disable();
        }
        getActivity().invalidateOptionsMenu();
    }

    private void initComponents() {
        PrintersAdapter printersAdapter = new PrintersAdapter();
        this.mPrintersAdapter = printersAdapter;
        printersAdapter.registerAdapterDataObserver(this.mDataObserver);
        SettingsMainSwitchBar switchBar = ((SettingsActivity) getActivity()).getSwitchBar();
        this.mSwitchBar = switchBar;
        switchBar.setTitle(getContext().getString(R.string.default_print_service_main_switch_title));
        this.mSwitchBar.addOnSwitchChangeListener(this);
        this.mSwitchBar.show();
        this.mSwitchBar.setOnBeforeCheckedChangeListener(new PrintServiceSettingsFragment$$ExternalSyntheticLambda0(this));
        getListView().setAdapter(this.mPrintersAdapter);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initComponents$0(Switch switchR, boolean z) {
        onPreferenceToggled(this.mPreferenceKey, z);
        return false;
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        updateEmptyView();
    }

    private void updateUiForArguments() {
        Bundle arguments = getArguments();
        ComponentName unflattenFromString = ComponentName.unflattenFromString(arguments.getString("EXTRA_SERVICE_COMPONENT_NAME"));
        this.mComponentName = unflattenFromString;
        this.mPreferenceKey = unflattenFromString.flattenToString();
        this.mSwitchBar.setCheckedInternal(arguments.getBoolean("EXTRA_CHECKED"));
        getLoaderManager().initLoader(2, (Bundle) null, this);
        setHasOptionsMenu(true);
    }

    public Loader<List<PrintServiceInfo>> onCreateLoader(int i, Bundle bundle) {
        return new SettingsPrintServicesLoader((PrintManager) getContext().getSystemService("print"), getContext(), 3);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00af  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLoadFinished(androidx.loader.content.Loader<java.util.List<android.printservice.PrintServiceInfo>> r7, java.util.List<android.printservice.PrintServiceInfo> r8) {
        /*
            r6 = this;
            r7 = 0
            r0 = 0
            if (r8 == 0) goto L_0x0027
            int r1 = r8.size()
            r2 = r0
        L_0x0009:
            if (r2 >= r1) goto L_0x0027
            java.lang.Object r3 = r8.get(r2)
            android.printservice.PrintServiceInfo r3 = (android.printservice.PrintServiceInfo) r3
            android.content.ComponentName r3 = r3.getComponentName()
            android.content.ComponentName r4 = r6.mComponentName
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x0024
            java.lang.Object r8 = r8.get(r2)
            android.printservice.PrintServiceInfo r8 = (android.printservice.PrintServiceInfo) r8
            goto L_0x0028
        L_0x0024:
            int r2 = r2 + 1
            goto L_0x0009
        L_0x0027:
            r8 = r7
        L_0x0028:
            if (r8 != 0) goto L_0x002d
            r6.finishFragment()
        L_0x002d:
            boolean r1 = r8.isEnabled()
            r6.mServiceEnabled = r1
            java.lang.String r1 = r8.getSettingsActivityName()
            java.lang.String r2 = "android.intent.action.MAIN"
            if (r1 == 0) goto L_0x0071
            android.content.Intent r1 = new android.content.Intent
            r1.<init>(r2)
            android.content.ComponentName r3 = new android.content.ComponentName
            android.content.ComponentName r4 = r8.getComponentName()
            java.lang.String r4 = r4.getPackageName()
            java.lang.String r5 = r8.getSettingsActivityName()
            r3.<init>(r4, r5)
            r1.setComponent(r3)
            android.content.pm.PackageManager r3 = r6.getPackageManager()
            java.util.List r3 = r3.queryIntentActivities(r1, r0)
            boolean r4 = r3.isEmpty()
            if (r4 != 0) goto L_0x0073
            java.lang.Object r3 = r3.get(r0)
            android.content.pm.ResolveInfo r3 = (android.content.pm.ResolveInfo) r3
            android.content.pm.ActivityInfo r3 = r3.activityInfo
            boolean r3 = r3.exported
            if (r3 == 0) goto L_0x0073
            r6.mSettingsIntent = r1
            goto L_0x0073
        L_0x0071:
            r6.mSettingsIntent = r7
        L_0x0073:
            java.lang.String r1 = r8.getAddPrintersActivityName()
            if (r1 == 0) goto L_0x00af
            android.content.Intent r7 = new android.content.Intent
            r7.<init>(r2)
            android.content.ComponentName r1 = new android.content.ComponentName
            android.content.ComponentName r2 = r8.getComponentName()
            java.lang.String r2 = r2.getPackageName()
            java.lang.String r8 = r8.getAddPrintersActivityName()
            r1.<init>(r2, r8)
            r7.setComponent(r1)
            android.content.pm.PackageManager r8 = r6.getPackageManager()
            java.util.List r8 = r8.queryIntentActivities(r7, r0)
            boolean r1 = r8.isEmpty()
            if (r1 != 0) goto L_0x00b1
            java.lang.Object r8 = r8.get(r0)
            android.content.pm.ResolveInfo r8 = (android.content.pm.ResolveInfo) r8
            android.content.pm.ActivityInfo r8 = r8.activityInfo
            boolean r8 = r8.exported
            if (r8 == 0) goto L_0x00b1
            r6.mAddPrintersIntent = r7
            goto L_0x00b1
        L_0x00af:
            r6.mAddPrintersIntent = r7
        L_0x00b1:
            r6.updateUiForServiceState()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.print.PrintServiceSettingsFragment.onLoadFinished(androidx.loader.content.Loader, java.util.List):void");
    }

    public void onLoaderReset(Loader<List<PrintServiceInfo>> loader) {
        updateUiForServiceState();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        Intent intent;
        Intent intent2;
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.print_service_settings, menu);
        MenuItem findItem = menu.findItem(R.id.print_menu_item_add_printer);
        if (!this.mServiceEnabled || (intent2 = this.mAddPrintersIntent) == null) {
            menu.removeItem(R.id.print_menu_item_add_printer);
        } else {
            findItem.setIntent(intent2);
        }
        MenuItem findItem2 = menu.findItem(R.id.print_menu_item_settings);
        if (!this.mServiceEnabled || (intent = this.mSettingsIntent) == null) {
            menu.removeItem(R.id.print_menu_item_settings);
        } else {
            findItem2.setIntent(intent);
        }
        MenuItem findItem3 = menu.findItem(R.id.print_menu_item_search);
        if (!this.mServiceEnabled || this.mPrintersAdapter.getUnfilteredCount() <= 0) {
            menu.removeItem(R.id.print_menu_item_search);
            return;
        }
        SearchView searchView = (SearchView) findItem3.getActionView();
        this.mSearchView = searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return true;
            }

            public boolean onQueryTextChange(String str) {
                PrintServiceSettingsFragment.this.mPrintersAdapter.getFilter().filter(str);
                return true;
            }
        });
        this.mSearchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
                if (AccessibilityManager.getInstance(PrintServiceSettingsFragment.this.getActivity()).isEnabled()) {
                    view.announceForAccessibility(PrintServiceSettingsFragment.this.getString(R.string.print_search_box_shown_utterance));
                }
            }

            public void onViewDetachedFromWindow(View view) {
                FragmentActivity activity = PrintServiceSettingsFragment.this.getActivity();
                if (activity != null && !activity.isFinishing() && AccessibilityManager.getInstance(activity).isEnabled()) {
                    view.announceForAccessibility(PrintServiceSettingsFragment.this.getString(R.string.print_search_box_hidden_utterance));
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    private final class PrintersAdapter extends RecyclerView.Adapter<ViewHolder> implements LoaderManager.LoaderCallbacks<List<PrinterInfo>>, Filterable {
        /* access modifiers changed from: private */
        public final List<PrinterInfo> mFilteredPrinters;
        /* access modifiers changed from: private */
        public CharSequence mLastSearchString;
        /* access modifiers changed from: private */
        public final Object mLock;
        /* access modifiers changed from: private */
        public final List<PrinterInfo> mPrinters;

        public long getItemId(int i) {
            return (long) i;
        }

        private PrintersAdapter() {
            this.mLock = new Object();
            this.mPrinters = new ArrayList();
            this.mFilteredPrinters = new ArrayList();
        }

        public void enable() {
            PrintServiceSettingsFragment.this.getLoaderManager().initLoader(1, (Bundle) null, this);
        }

        public void disable() {
            PrintServiceSettingsFragment.this.getLoaderManager().destroyLoader(1);
            this.mPrinters.clear();
        }

        public int getUnfilteredCount() {
            return this.mPrinters.size();
        }

        public Filter getFilter() {
            return new Filter() {
                /* access modifiers changed from: protected */
                public Filter.FilterResults performFiltering(CharSequence charSequence) {
                    synchronized (PrintersAdapter.this.mLock) {
                        if (TextUtils.isEmpty(charSequence)) {
                            return null;
                        }
                        Filter.FilterResults filterResults = new Filter.FilterResults();
                        ArrayList arrayList = new ArrayList();
                        String lowerCase = charSequence.toString().toLowerCase();
                        int size = PrintersAdapter.this.mPrinters.size();
                        for (int i = 0; i < size; i++) {
                            PrinterInfo printerInfo = (PrinterInfo) PrintersAdapter.this.mPrinters.get(i);
                            String name = printerInfo.getName();
                            if (name != null && name.toLowerCase().contains(lowerCase)) {
                                arrayList.add(printerInfo);
                            }
                        }
                        filterResults.values = arrayList;
                        filterResults.count = arrayList.size();
                        return filterResults;
                    }
                }

                /* access modifiers changed from: protected */
                public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                    synchronized (PrintersAdapter.this.mLock) {
                        CharSequence unused = PrintersAdapter.this.mLastSearchString = charSequence;
                        PrintersAdapter.this.mFilteredPrinters.clear();
                        if (filterResults == null) {
                            PrintersAdapter.this.mFilteredPrinters.addAll(PrintersAdapter.this.mPrinters);
                        } else {
                            PrintersAdapter.this.mFilteredPrinters.addAll((List) filterResults.values);
                        }
                    }
                    PrintersAdapter.this.notifyDataSetChanged();
                }
            };
        }

        public int getItemCount() {
            int size;
            synchronized (this.mLock) {
                size = this.mFilteredPrinters.size();
            }
            return size;
        }

        private Object getItem(int i) {
            PrinterInfo printerInfo;
            synchronized (this.mLock) {
                printerInfo = this.mFilteredPrinters.get(i);
            }
            return printerInfo;
        }

        public boolean isActionable(int i) {
            return ((PrinterInfo) getItem(i)).getStatus() != 3;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.printer_dropdown_item, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.itemView.setEnabled(isActionable(i));
            final PrinterInfo printerInfo = (PrinterInfo) getItem(i);
            String name = printerInfo.getName();
            String description = printerInfo.getDescription();
            Drawable loadIcon = printerInfo.loadIcon(PrintServiceSettingsFragment.this.getActivity());
            ((TextView) viewHolder.itemView.findViewById(R.id.title)).setText(name);
            TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.subtitle);
            if (!TextUtils.isEmpty(description)) {
                textView.setText(description);
                textView.setVisibility(0);
            } else {
                textView.setText((CharSequence) null);
                textView.setVisibility(8);
            }
            LinearLayout linearLayout = (LinearLayout) viewHolder.itemView.findViewById(R.id.more_info);
            if (printerInfo.getInfoIntent() != null) {
                linearLayout.setVisibility(0);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            PrintServiceSettingsFragment.this.getActivity().startIntentSender(printerInfo.getInfoIntent().getIntentSender(), (Intent) null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("PrintServiceSettings", "Could not execute pending info intent: %s", e);
                        }
                    }
                });
            } else {
                linearLayout.setVisibility(8);
            }
            ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.icon);
            if (loadIcon != null) {
                imageView.setVisibility(0);
                if (!isActionable(i)) {
                    loadIcon.mutate();
                    TypedValue typedValue = new TypedValue();
                    PrintServiceSettingsFragment.this.getActivity().getTheme().resolveAttribute(16842803, typedValue, true);
                    loadIcon.setAlpha((int) (typedValue.getFloat() * 255.0f));
                }
                imageView.setImageDrawable(loadIcon);
            } else {
                imageView.setVisibility(8);
            }
            viewHolder.itemView.setOnClickListener(new C1233xab42105c(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(int i, View view) {
            PrinterInfo printerInfo = (PrinterInfo) getItem(i);
            if (printerInfo.getInfoIntent() != null) {
                try {
                    PrintServiceSettingsFragment.this.getActivity().startIntentSender(printerInfo.getInfoIntent().getIntentSender(), (Intent) null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("PrintServiceSettings", "Could not execute info intent: %s", e);
                }
            }
        }

        public Loader<List<PrinterInfo>> onCreateLoader(int i, Bundle bundle) {
            if (i == 1) {
                return new PrintersLoader(PrintServiceSettingsFragment.this.getContext());
            }
            return null;
        }

        public void onLoadFinished(Loader<List<PrinterInfo>> loader, List<PrinterInfo> list) {
            synchronized (this.mLock) {
                this.mPrinters.clear();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    PrinterInfo printerInfo = list.get(i);
                    if (printerInfo.getId().getServiceName().equals(PrintServiceSettingsFragment.this.mComponentName)) {
                        this.mPrinters.add(printerInfo);
                    }
                }
                this.mFilteredPrinters.clear();
                this.mFilteredPrinters.addAll(this.mPrinters);
                if (!TextUtils.isEmpty(this.mLastSearchString)) {
                    getFilter().filter(this.mLastSearchString);
                }
            }
            notifyDataSetChanged();
        }

        public void onLoaderReset(Loader<List<PrinterInfo>> loader) {
            synchronized (this.mLock) {
                this.mPrinters.clear();
                this.mFilteredPrinters.clear();
                this.mLastSearchString = null;
            }
            notifyDataSetChanged();
        }
    }

    private static class PrintersLoader extends Loader<List<PrinterInfo>> {
        /* access modifiers changed from: private */
        public PrinterDiscoverySession mDiscoverySession;
        private final Map<PrinterId, PrinterInfo> mPrinters = new LinkedHashMap();

        public PrintersLoader(Context context) {
            super(context);
        }

        public void deliverResult(List<PrinterInfo> list) {
            if (isStarted()) {
                super.deliverResult(list);
            }
        }

        /* access modifiers changed from: protected */
        public void onStartLoading() {
            if (!this.mPrinters.isEmpty()) {
                deliverResult(new ArrayList(this.mPrinters.values()));
            }
            onForceLoad();
        }

        /* access modifiers changed from: protected */
        public void onStopLoading() {
            onCancelLoad();
        }

        /* access modifiers changed from: protected */
        public void onForceLoad() {
            loadInternal();
        }

        /* access modifiers changed from: protected */
        public boolean onCancelLoad() {
            return cancelInternal();
        }

        /* access modifiers changed from: protected */
        public void onReset() {
            onStopLoading();
            this.mPrinters.clear();
            PrinterDiscoverySession printerDiscoverySession = this.mDiscoverySession;
            if (printerDiscoverySession != null) {
                printerDiscoverySession.destroy();
                this.mDiscoverySession = null;
            }
        }

        /* access modifiers changed from: protected */
        public void onAbandon() {
            onStopLoading();
        }

        private boolean cancelInternal() {
            PrinterDiscoverySession printerDiscoverySession = this.mDiscoverySession;
            if (printerDiscoverySession == null || !printerDiscoverySession.isPrinterDiscoveryStarted()) {
                return false;
            }
            this.mDiscoverySession.stopPrinterDiscovery();
            return true;
        }

        private void loadInternal() {
            if (this.mDiscoverySession == null) {
                PrinterDiscoverySession createPrinterDiscoverySession = ((PrintManager) getContext().getSystemService("print")).createPrinterDiscoverySession();
                this.mDiscoverySession = createPrinterDiscoverySession;
                createPrinterDiscoverySession.setOnPrintersChangeListener(new PrinterDiscoverySession.OnPrintersChangeListener() {
                    public void onPrintersChanged() {
                        PrintersLoader.this.deliverResult(new ArrayList(PrintersLoader.this.mDiscoverySession.getPrinters()));
                    }
                });
            }
            this.mDiscoverySession.startPrinterDiscovery((List) null);
        }
    }
}
