package com.jenshen.awesomeanimation.callbacks;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionCounterAnimationCallbackDelegator implements Transition.TransitionListener {

    private List<Transition> transitions;
    private List<Transition.TransitionListener> listenerAdapters;
    private int count = 0;

    public TransitionCounterAnimationCallbackDelegator() {
        transitions = new ArrayList<>();
        listenerAdapters = new ArrayList<>();
    }

    public void addTransition(@Nullable Transition transition) {
        if (transition == null) {
            return;
        }
        count++;
        transition.addListener(this);
    }

    @Override
    public void onTransitionStart(Transition transition) {
        if (count == 0)
        for (Transition.TransitionListener listenerAdapter : listenerAdapters) {
            listenerAdapter.onTransitionStart(transition);
        }
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        count--;
        for (Transition.TransitionListener listenerAdapter : listenerAdapters) {
            listenerAdapter.onTransitionEnd(transition);
        }
    }

    @Override
    public void onTransitionCancel(Transition transition) {

        for (Transition.TransitionListener listenerAdapter : listenerAdapters) {
            listenerAdapter.onTransitionCancel(transition);
        }
    }

    @Override
    public void onTransitionPause(Transition transition) {
        for (Transition.TransitionListener listenerAdapter : listenerAdapters) {
            listenerAdapter.onTransitionPause(transition);
        }
    }

    @Override
    public void onTransitionResume(Transition transition) {
        for (Transition.TransitionListener listenerAdapter : listenerAdapters) {
            listenerAdapter.onTransitionResume(transition);
        }
    }
}
