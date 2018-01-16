package com.jenshen.awesomeanimation.util.transition;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.util.Log;

import com.jenshen.awesomeanimation.util.HandlerTimer;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TransitionWrapper {

    private static final String TAG = "AwesomeAnimation: " + TransitionWrapper.class.getSimpleName();

    private final Transition transition;
    @Nullable
    private Transition.TransitionListener[] listeners;
    private HandlerTimer handlerTimer;
    private boolean transitionCanceled;
    private boolean transitionEnded;

    TransitionWrapper(final Transition transition,
                      final Transition.TransitionListener transitionListener,
                      final @Nullable Transition.TransitionListener... listeners) {
        this.transition = transition;
        this.listeners = listeners;
        this.handlerTimer = new HandlerTimer();
        if (transitionListener != null) {
            //sometimes transitions are not started
            long duration = transition.getDuration() > 0 ? transition.getDuration() : 0;
            this.handlerTimer.schedule(new Runnable() {
                @Override
                public void run() {
                    if (!transitionCanceled && !transitionEnded) {
                        Log.d(TAG, "on timer finished");
                        transitionListener.onTransitionEnd(transition);
                    }
                }
            }, (int) duration * 2);
        }
    }

    Transition getTransition() {
        return transition;
    }

    void onStart() {
        clearTimer();
        if (transitionEnded || transitionCanceled) {
            return;
        }
        if (listeners != null) {
            for (Transition.TransitionListener listener : listeners) {
                listener.onTransitionStart(transition);
            }
        }
    }

    void onResume() {
        if (transitionEnded || transitionCanceled) {
            return;
        }
        if (listeners != null) {
            for (Transition.TransitionListener listener : listeners) {
                listener.onTransitionResume(transition);
            }
        }
    }

    void onPause() {
        if (transitionEnded || transitionCanceled) {
            return;
        }
        if (listeners != null) {
            for (Transition.TransitionListener listener : listeners) {
                listener.onTransitionPause(transition);
            }
        }
    }

    void onEnd() {
        clearTimer();
        if (transitionEnded || transitionCanceled) {
            return;
        }
        transitionEnded = true;
        if (listeners != null) {
            for (Transition.TransitionListener listener : listeners) {
                listener.onTransitionEnd(transition);
            }
        }
    }

    void cancel() {
        if (listeners != null) {
            for (Transition.TransitionListener listener : listeners) {
                listener.onTransitionCancel(transition);
            }
        }
        clear();
    }

    void clear() {
        transitionCanceled = true;
        clearTimer();
        if (listeners != null) {
            for (Transition.TransitionListener listener : listeners) {
                transition.removeListener(listener);
            }
        }
        this.listeners = null;
    }

    private void clearTimer() {
        if (handlerTimer != null) {
            handlerTimer.cancel();
            handlerTimer = null;
        }
    }
}