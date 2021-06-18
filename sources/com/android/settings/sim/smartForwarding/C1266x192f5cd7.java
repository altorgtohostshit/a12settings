package com.android.settings.sim.smartForwarding;

import com.android.settings.sim.smartForwarding.EnableSmartForwardingTask;
import java.util.function.Consumer;

/* renamed from: com.android.settings.sim.smartForwarding.EnableSmartForwardingTask$QueryCallWaitingCommand$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1266x192f5cd7 implements Consumer {
    public final /* synthetic */ EnableSmartForwardingTask.QueryCallWaitingCommand f$0;

    public /* synthetic */ C1266x192f5cd7(EnableSmartForwardingTask.QueryCallWaitingCommand queryCallWaitingCommand) {
        this.f$0 = queryCallWaitingCommand;
    }

    public final void accept(Object obj) {
        this.f$0.queryStatusCallBack(((Integer) obj).intValue());
    }
}
