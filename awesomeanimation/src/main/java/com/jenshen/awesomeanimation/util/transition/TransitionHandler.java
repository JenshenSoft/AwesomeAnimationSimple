package com.jenshen.awesomeanimation.util.transition;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class TransitionHandler {

    private static final String TAG = "AwesomeAnimation: " + TransitionHandler.class.getSimpleName();

    private List<TransitionWrapper> transitionList;
    private boolean onPause;
    private boolean onDestroyed;

    private Transition.TransitionListener transitionListener;

    public void addTransition(Transition transition,
                              boolean withProtection,
                              @Nullable Transition.TransitionListener... transitionListeners) {
        final List<TransitionWrapper> transitions = createListTransitionsInNeeded();
        transition.addListener(this.transitionListener);
        transitions.add(new TransitionWrapper(transition, withProtection ? this.transitionListener : null, transitionListeners));
    }

    public void addTransition(Transition transition, @Nullable Transition.TransitionListener... transitionListeners) {
        addTransition(transition, true, transitionListeners);
    }

    public void removeTransition(Transition transitionToRemove) {
        if (transitionList != null) {
            for (TransitionWrapper transitionWrapper : transitionList) {
                if (TransitionUtil.equalsTransitions(transitionWrapper.getTransition(), transitionToRemove)) {
                    transitionWrapper.clear();
                    transitionList.remove(transitionWrapper);
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

    public boolean isOnDestroyed() {
        return onDestroyed;
    }

    public void onResume() {
        if (!onPause) {
            return;
        }
        onPause = false;
        Log.d(TAG, "onResume");
        if (transitionList != null) {
            for (TransitionWrapper transitionWrapper : transitionList) {
                transitionWrapper.onResume();
            }
        }
    }

    public void onPause() {
        if (onPause) {
            return;
        }
        onPause = true;
        Log.d(TAG, "onPause");
        if (transitionList != null) {
            for (TransitionWrapper transitionWrapper : transitionList) {
                transitionWrapper.onPause();
            }
        }
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (onDestroyed) {
            return;
        }
        onDestroyed = true;
        cancel();
    }

    public void cancel() {
        Log.d(TAG, "cancel");
        if (this.transitionList != null) {
            for (TransitionWrapper transition : transitionList) {
                transition.cancel();
            }
            this.transitionList.clear();
            this.transitionList = null;
        }
        this.transitionListener = null;
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
                            if (TransitionUtil.equalsTransitions(transitionWrapper.getTransition(), transition)) {
                                transitionWrapper.onStart();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (transitionList != null) {
                        for (TransitionWrapper transitionWrapper : transitionList) {
                            if (TransitionUtil.equalsTransitions(transitionWrapper.getTransition(), transition)) {
                                transitionWrapper.onEnd();
                                break;
                            }
                        }
                        removeTransition(transition);
                    }
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
