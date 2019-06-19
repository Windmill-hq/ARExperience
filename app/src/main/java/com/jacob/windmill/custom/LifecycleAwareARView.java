package com.jacob.windmill.custom;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.viro.core.RendererConfiguration;
import com.viro.core.ViroViewARCore;

public class LifecycleAwareARView extends ViroViewARCore implements LifecycleObserver {

    public LifecycleAwareARView(@NonNull Context context, @Nullable StartupListener startupListener) {
        super(context, startupListener);
    }

    public LifecycleAwareARView(@NonNull Context context, @Nullable StartupListener startupListener, @Nullable RendererConfiguration config) {
        super(context, startupListener, config);
    }

    public LifecycleAwareARView(@NonNull Context context) {
        super(context);
    }

    public LifecycleAwareARView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LifecycleAwareARView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        onActivityStarted(mWeakActivity.get());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        onActivityResumed(mWeakActivity.get());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        onActivityPaused(mWeakActivity.get());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        onActivityStopped(mWeakActivity.get());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        onActivityDestroyed(mWeakActivity.get());
    }
}
