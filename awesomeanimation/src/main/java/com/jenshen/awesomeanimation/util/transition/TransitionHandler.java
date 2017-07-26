package com.jenshen.awesomeanimation.util.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.view.View;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class TransitionHandler {

    private List<TransitionWrapper> transitionList;
    private boolean onPause;

    private Transition.TransitionListener transitionListener;

    public void addTransition(Transition transition, Transition.TransitionListener transitionListener) {
        final List<TransitionWrapper> transitions = createListTransitionsInNeeded();
        transitions.add(new TransitionWrapper(transition, transitionListener));
        transition.addListener(this.transitionListener);
    }

    public void removeTransition(Transition transitionToRemove) {
        if (transitionList != null) {
            for (TransitionWrapper transition : transitionList) {
                if (transition.getTransition().equals(transitionToRemove)) {
                    transition.clear(transitionListener);
                    transitionList.remove(transition);
                    return;
                }
            }
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            onResume();
        } else {
            onPause();
        }
    }

    public void onVisibilityChanged(int visibility) {
        if (visibility == View.VISIBLE) {
            onResume();
        } else {
            onPause();
        }
    }

    public boolean isOnPause() {
        return onPause;
    }

    public void onResume() {
        if (!onPause) {
            return;
        }
        onPause = false;
        for (TransitionWrapper transitionWrapper : transitionList) {
            transitionWrapper.onResume();
        }
    }

    public void onPause() {
        if (onPause) {
            return;
        }
        onPause = true;
        for (TransitionWrapper transitionWrapper : transitionList) {
            transitionWrapper.onPause();
        }
    }

    public void cancel() {
        if (this.transitionList != null) {
            for (TransitionWrapper transition : transitionList) {
                transition.cancel(transitionListener);
            }
            this.transitionList.clear();
            this.transitionList = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private List<TransitionWrapper> createListTransitionsInNeeded() {
        if (this.transitionList == null) {
            this.transitionList = new CopyOnWriteArrayList<>();
            this.transitionListener = new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    if (transitionList != null) {
                        for (TransitionWrapper transitionWrapper : transitionList) {
                            transitionWrapper.onStart();
                        }
                    }
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (transitionList != null) {
                        for (TransitionWrapper transitionWrapper : transitionList) {
                            transitionWrapper.onEnd();
                        }
                    }
                    removeTransition(transition);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    removeTransition(transition);
                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            };
        }
        return this.transitionList;
    }
}
