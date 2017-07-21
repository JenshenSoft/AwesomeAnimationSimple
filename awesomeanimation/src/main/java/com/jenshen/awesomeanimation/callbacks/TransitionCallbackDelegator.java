package com.jenshen.awesomeanimation.callbacks;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jenshen.awesomeanimation.callbacks.AnimationState.CANCEL;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.END;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.PAUSE;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.RESUME;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.START;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionCallbackDelegator implements Transition.TransitionListener {

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

    @Override
    public void onTransitionStart(Transition transition) {
        addState(START);
        if (adapters != null && !adapters.isEmpty()) {
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionStart(transition);
            }
        }
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        addState(END);
        if (adapters != null && !adapters.isEmpty()) {
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionEnd(transition);
            }
        }
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        addState(CANCEL);
        if (adapters != null && !adapters.isEmpty()) {
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionCancel(transition);
            }
        }
    }

    @Override
    public void onTransitionPause(Transition transition) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && adapters != null && !adapters.isEmpty()) {
            addState(PAUSE);
            for (Transition.TransitionListener adapter : adapters) {
                adapter.onTransitionPause(transition);
            }
        }
    }

    @Override
    public void onTransitionResume(Transition transition) {
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
