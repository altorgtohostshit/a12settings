package androidx.mediarouter.media;

import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.RegisteredMediaRouteProvider;

public final /* synthetic */ class RegisteredMediaRouteProviderWatcher$$ExternalSyntheticLambda0 implements RegisteredMediaRouteProvider.ControllerCallback {
    public final /* synthetic */ RegisteredMediaRouteProviderWatcher f$0;
    public final /* synthetic */ RegisteredMediaRouteProvider f$1;

    public /* synthetic */ RegisteredMediaRouteProviderWatcher$$ExternalSyntheticLambda0(RegisteredMediaRouteProviderWatcher registeredMediaRouteProviderWatcher, RegisteredMediaRouteProvider registeredMediaRouteProvider) {
        this.f$0 = registeredMediaRouteProviderWatcher;
        this.f$1 = registeredMediaRouteProvider;
    }

    public final void onControllerReleasedByProvider(MediaRouteProvider.RouteController routeController) {
        this.f$0.lambda$scanPackages$0(this.f$1, routeController);
    }
}
