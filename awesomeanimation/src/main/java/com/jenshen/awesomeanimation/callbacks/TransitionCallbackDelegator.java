package com.jenshen.awesomeanimation.callbacks;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jenshen.awesomeanimation.callbacks.AnimationState.CANCEL;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.END;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.PAUSE;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.RESUME;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.START;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionCallbackDelegator implements Transition.TransitionListener {

    private static final String TAG = "AwesomeAnimation: " + TransitionCallbackDelegator.class.getSimpleName();

    @Nullable
    private List<Transition.TransitionListener> adapters;
    private List<Integer> animationState;

    public void addListener(@NonNull Transition.TransitionListener adapter) {
        if (adapters == null) {
            adapters = new CopyOnWriteArrayList<>();
        }
        adapters.add(adapter);
        if (animationState != null) {
            for (Integer state : animationState) {
                switch (state) {
                    case START:
                        adapter.onTransitionStart(null);
                        break;
                    case END:
                        adapter.onTransitionEnd(null);
                        break;
                    case CANCEL:
                        adapter.onTransitionStart(null);
                        break;
                    case PAUSE:
                        adapter.onTransitionPause(null);
                        break;
                    case RESUME:
                        adapter.onTransitionResume(null);
                        break;
                }
            }
        }
    }

    public void removeListener(@NonNull Transition.TransitionListener adapter) {
        if (adapters == null) {
            return;
        }
        adapters.remove(adapter);
    }

    public void clear() {
        if (adapters != null) {
            adapters.clear();
        }
    }

    @Override
    public void onTransitionStart(Transition transition) {
        Log.d(TAG, "onTransitionStart");
        addState(START);
        if (adapters != null && !adapters.isEmpty()) {
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionStart(transition);
            }
        }
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        Log.d(TAG, "onTransitionEnd");
        addState(END);
        if (adapters != null && !adapters.isEmpty()) {
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionEnd(transition);
            }
            clear();
        }
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        Log.d(TAG, "onTransitionCancel");
        addState(CANCEL);
        if (adapters != null && !adapters.isEmpty()) {
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionCancel(transition);
            }
            clear();
        }
    }

    @Override
    public void onTransitionPause(Transition transition) {
        Log.d(TAG, "onTransitionPause");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            addState(PAUSE);
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionPause(transition);
            }
        }
    }

    @Override
    public void onTransitionResume(Transition transition) {
        Log.d(TAG, "onTransitionResume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            addState(RESUME);
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionResume(transition);
            }
        }
    }

    private void addState(@AnimationState int state) {
        if (animationState == null) {
            animationState = new CopyOnWriteArrayList<>();
        }
        animationState.add(state);
    }
}
