package com.android.settings.sim.smartForwarding;

import android.content.Context;
import android.telephony.CallForwardingInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.google.common.util.concurrent.SettableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class EnableSmartForwardingTask implements Callable<FeatureResult> {
    SettableFuture<FeatureResult> client = SettableFuture.create();
    private final String[] mCallForwardingNumber;
    FeatureResult mResult = new FeatureResult(false, (SlotUTData[]) null);
    /* access modifiers changed from: private */

    /* renamed from: sm */
    public final SubscriptionManager f100sm;
    /* access modifiers changed from: private */

    /* renamed from: tm */
    public final TelephonyManager f101tm;

    interface Command {
        boolean process() throws Exception;
    }

    public EnableSmartForwardingTask(Context context, String[] strArr) {
        this.f101tm = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.f100sm = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mCallForwardingNumber = strArr;
    }

    public FeatureResult call() throws TimeoutException, InterruptedException, ExecutionException {
        FlowController flowController = new FlowController();
        if (flowController.init(this.mCallForwardingNumber)) {
            flowController.startProcess();
        } else {
            this.client.set(this.mResult);
        }
        return this.client.get(20, TimeUnit.SECONDS);
    }

    class FlowController {
        private SlotUTData[] mSlotUTData;
        private final ArrayList<Command> mSteps = new ArrayList<>();

        FlowController() {
        }

        public boolean init(String[] strArr) {
            if (!initObject(strArr)) {
                return false;
            }
            initSteps();
            return true;
        }

        private boolean initObject(String[] strArr) {
            String[] strArr2 = strArr;
            ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
            if (EnableSmartForwardingTask.this.f101tm == null || EnableSmartForwardingTask.this.f100sm == null) {
                Log.e("SmartForwarding", "TelephonyManager or SubscriptionManager is null");
                return false;
            } else if (strArr2.length != EnableSmartForwardingTask.this.f101tm.getActiveModemCount()) {
                Log.e("SmartForwarding", "The length of PhoneNum array should same as phone count.");
                return false;
            } else {
                this.mSlotUTData = new SlotUTData[EnableSmartForwardingTask.this.f101tm.getActiveModemCount()];
                for (int i = 0; i < this.mSlotUTData.length; i++) {
                    int[] subscriptionIds = EnableSmartForwardingTask.this.f100sm.getSubscriptionIds(i);
                    if (subscriptionIds.length < 1) {
                        Log.e("SmartForwarding", "getSubscriptionIds() return empty sub id list.");
                        return false;
                    }
                    int i2 = subscriptionIds[0];
                    if (!EnableSmartForwardingTask.this.f100sm.isActiveSubId(i2)) {
                        EnableSmartForwardingTask.this.mResult.setReason(FeatureResult.FailedReason.SIM_NOT_ACTIVE);
                        return false;
                    }
                    QueryCallWaitingCommand queryCallWaitingCommand = new QueryCallWaitingCommand(EnableSmartForwardingTask.this.f101tm, newSingleThreadExecutor, i2);
                    QueryCallForwardingCommand queryCallForwardingCommand = new QueryCallForwardingCommand(EnableSmartForwardingTask.this.f101tm, newSingleThreadExecutor, i2);
                    QueryCallForwardingCommand queryCallForwardingCommand2 = queryCallForwardingCommand;
                    this.mSlotUTData[i] = new SlotUTData(i2, strArr2[i], queryCallWaitingCommand, queryCallForwardingCommand2, new UpdateCallWaitingCommand(EnableSmartForwardingTask.this.f101tm, newSingleThreadExecutor, queryCallWaitingCommand, i2), new UpdateCallForwardingCommand(EnableSmartForwardingTask.this.f101tm, newSingleThreadExecutor, queryCallForwardingCommand, i2, strArr2[i]));
                }
                return true;
            }
        }

        private void initSteps() {
            for (SlotUTData queryCallWaitingCommand : this.mSlotUTData) {
                this.mSteps.add(queryCallWaitingCommand.getQueryCallWaitingCommand());
            }
            for (SlotUTData queryCallForwardingCommand : this.mSlotUTData) {
                this.mSteps.add(queryCallForwardingCommand.getQueryCallForwardingCommand());
            }
            for (SlotUTData updateCallWaitingCommand : this.mSlotUTData) {
                this.mSteps.add(updateCallWaitingCommand.getUpdateCallWaitingCommand());
            }
            for (SlotUTData updateCallForwardingCommand : this.mSlotUTData) {
                this.mSteps.add(updateCallForwardingCommand.getUpdateCallForwardingCommand());
            }
        }

        public void startProcess() {
            boolean z;
            int i = 0;
            boolean z2 = true;
            while (i < this.mSteps.size() && z2) {
                Command command = this.mSteps.get(i);
                Log.d("SmartForwarding", "processing : " + command);
                try {
                    z = command.process();
                } catch (Exception e) {
                    Log.d("SmartForwarding", "Failed on : " + command, e);
                    z = false;
                }
                if (z) {
                    i++;
                } else {
                    Log.d("SmartForwarding", "Failed on : " + command);
                }
                z2 = z;
            }
            if (z2) {
                boolean unused = EnableSmartForwardingTask.this.mResult.result = true;
                SlotUTData[] unused2 = EnableSmartForwardingTask.this.mResult.slotUTData = this.mSlotUTData;
                Log.d("SmartForwarding", "Smart forwarding successful");
                EnableSmartForwardingTask enableSmartForwardingTask = EnableSmartForwardingTask.this;
                enableSmartForwardingTask.client.set(enableSmartForwardingTask.mResult);
                return;
            }
            restoreAllSteps(i);
            EnableSmartForwardingTask enableSmartForwardingTask2 = EnableSmartForwardingTask.this;
            enableSmartForwardingTask2.client.set(enableSmartForwardingTask2.mResult);
        }

        private void restoreAllSteps(int i) {
            List<Command> subList = this.mSteps.subList(0, i);
            Collections.reverse(subList);
            for (Command next : subList) {
                Log.d("SmartForwarding", "restoreStep: " + next);
                if (next instanceof UpdateCommand) {
                    ((UpdateCommand) next).onRestore();
                }
            }
        }
    }

    final class SlotUTData {
        String mCallForwardingNumber;
        QueryCallForwardingCommand mQueryCallForwarding;
        QueryCallWaitingCommand mQueryCallWaiting;
        UpdateCallForwardingCommand mUpdateCallForwarding;
        UpdateCallWaitingCommand mUpdateCallWaiting;
        int subId;

        public SlotUTData(int i, String str, QueryCallWaitingCommand queryCallWaitingCommand, QueryCallForwardingCommand queryCallForwardingCommand, UpdateCallWaitingCommand updateCallWaitingCommand, UpdateCallForwardingCommand updateCallForwardingCommand) {
            this.subId = i;
            this.mCallForwardingNumber = str;
            this.mQueryCallWaiting = queryCallWaitingCommand;
            this.mQueryCallForwarding = queryCallForwardingCommand;
            this.mUpdateCallWaiting = updateCallWaitingCommand;
            this.mUpdateCallForwarding = updateCallForwardingCommand;
        }

        public QueryCallWaitingCommand getQueryCallWaitingCommand() {
            return this.mQueryCallWaiting;
        }

        public QueryCallForwardingCommand getQueryCallForwardingCommand() {
            return this.mQueryCallForwarding;
        }

        public UpdateCallWaitingCommand getUpdateCallWaitingCommand() {
            return this.mUpdateCallWaiting;
        }

        public UpdateCallForwardingCommand getUpdateCallForwardingCommand() {
            return this.mUpdateCallForwarding;
        }
    }

    static abstract class QueryCommand<T> implements Command {
        Executor executor;
        int subId;

        /* renamed from: tm */
        TelephonyManager f102tm;

        public QueryCommand(TelephonyManager telephonyManager, Executor executor2, int i) {
            this.subId = i;
            this.f102tm = telephonyManager;
            this.executor = executor2;
        }

        public String toString() {
            return getClass().getSimpleName() + "[SubId " + this.subId + "]";
        }
    }

    static abstract class UpdateCommand<T> implements Command {
        Executor executor;
        int subId;

        /* renamed from: tm */
        TelephonyManager f103tm;

        /* access modifiers changed from: package-private */
        public abstract void onRestore();

        public UpdateCommand(TelephonyManager telephonyManager, Executor executor2, int i) {
            this.subId = i;
            this.f103tm = telephonyManager;
            this.executor = executor2;
        }

        public String toString() {
            return getClass().getSimpleName() + "[SubId " + this.subId + "] ";
        }
    }

    static class QueryCallWaitingCommand extends QueryCommand<Integer> {
        int result;
        SettableFuture<Boolean> resultFuture = SettableFuture.create();

        public QueryCallWaitingCommand(TelephonyManager telephonyManager, Executor executor, int i) {
            super(telephonyManager, executor, i);
        }

        public boolean process() throws Exception {
            this.f102tm.createForSubscriptionId(this.subId).getCallWaitingStatus(this.executor, new C1266x192f5cd7(this));
            return this.resultFuture.get().booleanValue();
        }

        /* access modifiers changed from: package-private */
        public Integer getResult() {
            return Integer.valueOf(this.result);
        }

        public void queryStatusCallBack(int i) {
            this.result = i;
            if (i == 1 || i == 2) {
                Log.d("SmartForwarding", "Call Waiting result: " + i);
                this.resultFuture.set(Boolean.TRUE);
                return;
            }
            this.resultFuture.set(Boolean.FALSE);
        }
    }

    static class QueryCallForwardingCommand extends QueryCommand<CallForwardingInfo> {
        CallForwardingInfo result;
        SettableFuture<Boolean> resultFuture = SettableFuture.create();

        public QueryCallForwardingCommand(TelephonyManager telephonyManager, Executor executor, int i) {
            super(telephonyManager, executor, i);
        }

        public boolean process() throws Exception {
            this.f102tm.createForSubscriptionId(this.subId).getCallForwarding(3, this.executor, new TelephonyManager.CallForwardingInfoCallback() {
                public void onCallForwardingInfoAvailable(CallForwardingInfo callForwardingInfo) {
                    Log.d("SmartForwarding", "Call Forwarding result: " + callForwardingInfo);
                    QueryCallForwardingCommand queryCallForwardingCommand = QueryCallForwardingCommand.this;
                    queryCallForwardingCommand.result = callForwardingInfo;
                    queryCallForwardingCommand.resultFuture.set(Boolean.TRUE);
                }

                public void onError(int i) {
                    Log.d("SmartForwarding", "Query Call Forwarding failed.");
                    QueryCallForwardingCommand.this.resultFuture.set(Boolean.FALSE);
                }
            });
            return this.resultFuture.get().booleanValue();
        }

        /* access modifiers changed from: package-private */
        public CallForwardingInfo getResult() {
            return this.result;
        }
    }

    static class UpdateCallWaitingCommand extends UpdateCommand<Integer> {
        QueryCallWaitingCommand queryResult;
        SettableFuture<Boolean> resultFuture = SettableFuture.create();

        public UpdateCallWaitingCommand(TelephonyManager telephonyManager, Executor executor, QueryCallWaitingCommand queryCallWaitingCommand, int i) {
            super(telephonyManager, executor, i);
            this.queryResult = queryCallWaitingCommand;
        }

        public boolean process() throws Exception {
            this.f103tm.createForSubscriptionId(this.subId).setCallWaitingEnabled(true, this.executor, new C1268xf4424410(this));
            return this.resultFuture.get().booleanValue();
        }

        public void updateStatusCallBack(int i) {
            Log.d("SmartForwarding", "UpdateCallWaitingCommand updateStatusCallBack result: " + i);
            if (i == 1 || i == 2) {
                this.resultFuture.set(Boolean.TRUE);
            } else {
                this.resultFuture.set(Boolean.FALSE);
            }
        }

        /* access modifiers changed from: package-private */
        public void onRestore() {
            Log.d("SmartForwarding", "onRestore: " + this);
            if (this.queryResult.getResult().intValue() != 1) {
                this.f103tm.createForSubscriptionId(this.subId).setCallWaitingEnabled(false, (Executor) null, (Consumer) null);
            }
        }
    }

    static class UpdateCallForwardingCommand extends UpdateCommand<Integer> {
        String phoneNum;
        QueryCallForwardingCommand queryResult;
        SettableFuture<Boolean> resultFuture = SettableFuture.create();

        public UpdateCallForwardingCommand(TelephonyManager telephonyManager, Executor executor, QueryCallForwardingCommand queryCallForwardingCommand, int i, String str) {
            super(telephonyManager, executor, i);
            this.phoneNum = str;
            this.queryResult = queryCallForwardingCommand;
        }

        public boolean process() throws Exception {
            this.f103tm.createForSubscriptionId(this.subId).setCallForwarding(new CallForwardingInfo(true, 3, this.phoneNum, 3), this.executor, new C1267x438b917a(this));
            return this.resultFuture.get().booleanValue();
        }

        public void updateStatusCallBack(int i) {
            Log.d("SmartForwarding", "UpdateCallForwardingCommand updateStatusCallBack : " + i);
            if (i == 0) {
                this.resultFuture.set(Boolean.TRUE);
            } else {
                this.resultFuture.set(Boolean.FALSE);
            }
        }

        /* access modifiers changed from: package-private */
        public void onRestore() {
            Log.d("SmartForwarding", "onRestore: " + this);
            this.f103tm.createForSubscriptionId(this.subId).setCallForwarding(this.queryResult.getResult(), (Executor) null, (Consumer) null);
        }
    }

    public static class FeatureResult {
        private FailedReason reason;
        /* access modifiers changed from: private */
        public boolean result;
        /* access modifiers changed from: private */
        public SlotUTData[] slotUTData;

        enum FailedReason {
            NETWORK_ERROR,
            SIM_NOT_ACTIVE
        }

        public FeatureResult(boolean z, SlotUTData[] slotUTDataArr) {
            this.result = z;
            this.slotUTData = slotUTDataArr;
        }

        public boolean getResult() {
            return this.result;
        }

        public SlotUTData[] getSlotUTData() {
            return this.slotUTData;
        }

        public void setReason(FailedReason failedReason) {
            this.reason = failedReason;
        }

        public FailedReason getReason() {
            return this.reason;
        }
    }
}
