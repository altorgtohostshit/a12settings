package com.android.settings.sim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.SidecarFragment;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SwitchToEuiccSubscriptionSidecar;
import com.android.settings.network.SwitchToRemovableSlotSidecar;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.GlifRecyclerLayout;
import com.google.android.setupdesign.items.Dividable;
import com.google.android.setupdesign.items.IItem;
import com.google.android.setupdesign.items.Item;
import com.google.android.setupdesign.items.ItemGroup;
import com.google.android.setupdesign.items.RecyclerItemAdapter;
import com.google.android.setupdesign.view.HeaderRecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChooseSimActivity extends Activity implements RecyclerItemAdapter.OnItemSelectedListener, SidecarFragment.Listener {
    private ArrayList<SubscriptionInfo> mEmbeddedSubscriptions = new ArrayList<>();
    private boolean mHasPsim;
    private boolean mIsSwitching;
    private ItemGroup mItemGroup;
    private boolean mNoPsimContinueToSettings;
    private SubscriptionInfo mRemovableSubscription = null;
    private int mSelectedItemIndex;
    private SwitchToEuiccSubscriptionSidecar mSwitchToEuiccSubscriptionSidecar;
    private SwitchToRemovableSlotSidecar mSwitchToRemovableSlotSidecar;

    public static Intent getIntent(Context context) {
        return new Intent(context, ChooseSimActivity.class);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.choose_sim_activity);
        Intent intent = getIntent();
        this.mHasPsim = intent.getBooleanExtra("has_psim", false);
        this.mNoPsimContinueToSettings = intent.getBooleanExtra("no_psim_continue_to_settings", false);
        updateSubscriptions();
        if (this.mEmbeddedSubscriptions.size() == 0) {
            Log.e("ChooseSimActivity", "Unable to find available eSIM subscriptions.");
            finish();
            return;
        }
        if (bundle != null) {
            this.mSelectedItemIndex = bundle.getInt("selected_index");
            this.mIsSwitching = bundle.getBoolean("is_switching");
        }
        GlifLayout glifLayout = (GlifLayout) findViewById(R.id.glif_layout);
        TextView textView = (TextView) findViewById(R.id.subtitle);
        int size = this.mEmbeddedSubscriptions.size();
        if (this.mHasPsim) {
            size++;
        }
        glifLayout.setHeaderText((CharSequence) getString(R.string.choose_sim_title));
        textView.setText(getString(R.string.choose_sim_text, new Object[]{Integer.valueOf(size)}));
        displaySubscriptions();
        this.mSwitchToRemovableSlotSidecar = SwitchToRemovableSlotSidecar.get(getFragmentManager());
        this.mSwitchToEuiccSubscriptionSidecar = SwitchToEuiccSubscriptionSidecar.get(getFragmentManager());
    }

    public void onResume() {
        super.onResume();
        this.mSwitchToRemovableSlotSidecar.addListener(this);
        this.mSwitchToEuiccSubscriptionSidecar.addListener(this);
    }

    public void onPause() {
        this.mSwitchToEuiccSubscriptionSidecar.removeListener(this);
        this.mSwitchToRemovableSlotSidecar.removeListener(this);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("selected_index", this.mSelectedItemIndex);
        bundle.putBoolean("is_switching", this.mIsSwitching);
        super.onSaveInstanceState(bundle);
    }

    public void onItemSelected(IItem iItem) {
        if (!this.mIsSwitching) {
            this.mIsSwitching = true;
            Item item = (Item) iItem;
            item.setSummary(getString(R.string.choose_sim_activating));
            int id = item.getId();
            this.mSelectedItemIndex = id;
            if (id == -1) {
                Log.i("ChooseSimActivity", "Ready to switch to pSIM slot.");
                this.mSwitchToRemovableSlotSidecar.run(-1);
                return;
            }
            Log.i("ChooseSimActivity", "Ready to switch to eSIM subscription with index: " + this.mSelectedItemIndex);
            this.mSwitchToEuiccSubscriptionSidecar.run(this.mEmbeddedSubscriptions.get(this.mSelectedItemIndex).getSubscriptionId());
        }
    }

    public void onStateChange(SidecarFragment sidecarFragment) {
        SubscriptionInfo firstRemovableSubscription;
        SwitchToRemovableSlotSidecar switchToRemovableSlotSidecar = this.mSwitchToRemovableSlotSidecar;
        if (sidecarFragment == switchToRemovableSlotSidecar) {
            int state = switchToRemovableSlotSidecar.getState();
            if (state == 2) {
                this.mSwitchToRemovableSlotSidecar.reset();
                Log.i("ChooseSimActivity", "Switch slot successfully.");
                SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(SubscriptionManager.class);
                if (subscriptionManager.canDisablePhysicalSubscription() && (firstRemovableSubscription = SubscriptionUtil.getFirstRemovableSubscription(this)) != null) {
                    subscriptionManager.setUiccApplicationsEnabled(firstRemovableSubscription.getSubscriptionId(), true);
                }
                finish();
            } else if (state == 3) {
                this.mSwitchToRemovableSlotSidecar.reset();
                Log.e("ChooseSimActivity", "Failed to switch slot in ChooseSubscriptionsActivity.");
                handleEnableRemovableSimError();
            }
        } else {
            SwitchToEuiccSubscriptionSidecar switchToEuiccSubscriptionSidecar = this.mSwitchToEuiccSubscriptionSidecar;
            if (sidecarFragment == switchToEuiccSubscriptionSidecar) {
                int state2 = switchToEuiccSubscriptionSidecar.getState();
                if (state2 == 2) {
                    this.mSwitchToEuiccSubscriptionSidecar.reset();
                    if (this.mNoPsimContinueToSettings) {
                        Log.e("ChooseSimActivity", "mNoPsimContinueToSettings is true which is not supported for now.");
                        return;
                    }
                    Log.i("ChooseSimActivity", "User finished selecting eSIM profile.");
                    finish();
                } else if (state2 == 3) {
                    this.mSwitchToEuiccSubscriptionSidecar.reset();
                    Log.e("ChooseSimActivity", "Failed to switch subscription in ChooseSubscriptionsActivity.");
                    Item item = (Item) this.mItemGroup.getItemAt(this.mSelectedItemIndex);
                    item.setEnabled(false);
                    item.setSummary(getString(R.string.choose_sim_could_not_activate));
                    this.mIsSwitching = false;
                }
            }
        }
    }

    private void displaySubscriptions() {
        GlifRecyclerLayout glifRecyclerLayout = (GlifRecyclerLayout) findViewById(16908290).findViewById(R.id.recycler_list);
        RecyclerItemAdapter recyclerItemAdapter = (RecyclerItemAdapter) glifRecyclerLayout.getAdapter();
        recyclerItemAdapter.setOnItemSelectedListener(this);
        this.mItemGroup = (ItemGroup) recyclerItemAdapter.getRootItemHierarchy();
        if (this.mHasPsim) {
            DisableableItem disableableItem = new DisableableItem();
            CharSequence charSequence = null;
            SubscriptionInfo subscriptionInfo = this.mRemovableSubscription;
            if (subscriptionInfo != null) {
                charSequence = SubscriptionUtil.getUniqueSubscriptionDisplayName(Integer.valueOf(subscriptionInfo.getSubscriptionId()), (Context) this);
            }
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = getString(R.string.sim_card_label);
            }
            disableableItem.setTitle(charSequence);
            if (!this.mIsSwitching || this.mSelectedItemIndex != -1) {
                String formattedPhoneNumber = SubscriptionUtil.getFormattedPhoneNumber(this, this.mRemovableSubscription);
                if (TextUtils.isEmpty(formattedPhoneNumber)) {
                    formattedPhoneNumber = "";
                }
                disableableItem.setSummary(formattedPhoneNumber);
            } else {
                disableableItem.setSummary(getString(R.string.choose_sim_activating));
            }
            disableableItem.setId(-1);
            this.mItemGroup.addChild(disableableItem);
        }
        int i = 0;
        Iterator<SubscriptionInfo> it = this.mEmbeddedSubscriptions.iterator();
        while (it.hasNext()) {
            SubscriptionInfo next = it.next();
            DisableableItem disableableItem2 = new DisableableItem();
            CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(Integer.valueOf(next.getSubscriptionId()), (Context) this);
            if (TextUtils.isEmpty(uniqueSubscriptionDisplayName)) {
                uniqueSubscriptionDisplayName = next.getDisplayName();
            }
            disableableItem2.setTitle(uniqueSubscriptionDisplayName);
            if (!this.mIsSwitching || this.mSelectedItemIndex != i) {
                String formattedPhoneNumber2 = SubscriptionUtil.getFormattedPhoneNumber(this, next);
                if (TextUtils.isEmpty(formattedPhoneNumber2)) {
                    formattedPhoneNumber2 = "";
                }
                disableableItem2.setSummary(formattedPhoneNumber2);
            } else {
                disableableItem2.setSummary(getString(R.string.choose_sim_activating));
            }
            disableableItem2.setId(i);
            this.mItemGroup.addChild(disableableItem2);
            i++;
        }
        ((HeaderRecyclerView) glifRecyclerLayout.getRecyclerView()).getHeader().setVisibility(8);
    }

    private void updateSubscriptions() {
        List<SubscriptionInfo> selectableSubscriptionInfoList = SubscriptionUtil.getSelectableSubscriptionInfoList(this);
        if (selectableSubscriptionInfoList != null) {
            for (SubscriptionInfo next : selectableSubscriptionInfoList) {
                if (next != null) {
                    if (next.isEmbedded()) {
                        this.mEmbeddedSubscriptions.add(next);
                    } else {
                        this.mRemovableSubscription = next;
                    }
                }
            }
        }
    }

    private void handleEnableRemovableSimError() {
        int i = this.mSelectedItemIndex;
        if (i == -1) {
            i = 0;
        }
        Item item = (Item) this.mItemGroup.getItemAt(i);
        item.setEnabled(false);
        item.setSummary(getString(R.string.choose_sim_could_not_activate));
        this.mIsSwitching = false;
    }

    class DisableableItem extends Item implements Dividable {
        public boolean isDividerAllowedAbove() {
            return true;
        }

        public boolean isDividerAllowedBelow() {
            return true;
        }

        DisableableItem() {
        }

        public void onBindView(View view) {
            super.onBindView(view);
            ((TextView) view.findViewById(R.id.sud_items_title)).setEnabled(isEnabled());
            ((TextView) view.findViewById(R.id.sud_items_summary)).setEnabled(isEnabled());
        }
    }
}
