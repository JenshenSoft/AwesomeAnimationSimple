package com.jenshen.awesomeanimation.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimatorHandler {

    private List<AnimatorWrapper> animatorList;
    private List<TransitionWrapper> transitionList;
    private boolean onPause;

    private Transition.TransitionListener transitionListener;
    private AnimatorListenerAdapter animatorListenerAdapter ;

    public void addAnimator(final Animator animator) {
        final List<AnimatorWrapper> animators = createListAnimatorsInNeeded();
        animators.add(new AnimatorWrapper(animator));
        animator.addListener(animatorListenerAdapter);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void addTransition(Transition transition) {
        final List<TransitionWrapper> transitions = createListTransitionsInNeeded();
        transitions.add(new TransitionWrapper(transition));
        transition.addListener(transitionListener);
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
        if (this.animatorList != null) {
            for (AnimatorWrapper animator : this.animatorList) {
                animator.onResume();
            }
        }
    }

    public void onPause() {
        if (onPause) {
            return;
        }
        onPause = true;
        if (this.animatorList != null) {
            for (AnimatorWrapper animator : this.animatorList) {
                animator.onPause();
            }
        }
    }

    public void cancel() {
        cancel(null);
    }

    public void cancel(@Nullable ViewGroup viewGroup) {
        if (this.animatorList != null) {
            for (AnimatorWrapper animator : animatorList) {
                animator.cancel();
            }
            this.animatorList.clear();
            this.animatorList = null;
        }

        if (this.transitionList != null) {
            for (TransitionWrapper transition : transitionList) {
                transition.cancel(viewGroup, transitionListener);
            }
            this.transitionList.clear();
            this.transitionList = null;
        }
    }

    private List<AnimatorWrapper> createListAnimatorsInNeeded() {
        if (this.animatorList == null) {
            this.animatorList = new CopyOnWriteArrayList<>();
            this.animatorListenerAdapter = new AnimatorListenerAdapter() {

                @Override
                public void onAnimationCancel(Animator animation) {
                    removeAnimator(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    removeAnimator(animation);
                }
            };
        }
        return this.animatorList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private List<TransitionWrapper> createListTransitionsInNeeded() {
        if (this.transitionList == null) {
            this.transitionList = new CopyOnWriteArrayList<>();
            this.transitionListener = new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
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

    private void removeAnimator(Animator animator) {
        if (animatorList != null) {
            for (AnimatorWrapper animatorWrapper : animatorList) {
                if (animatorWrapper.getAnimator().equals(animator)) {
                    animatorWrapper.clear();
                    animatorList.remove(animatorWrapper);
                    return;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void removeTransition(Transition transitionToRemove) {
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
}
