package com.jenshen.awesomeanimation.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimatorHandler {

    private List<AnimatorWrapper> animatorList;
    private boolean onPause;

    public void addAnimator(Animator animator) {
        final List<AnimatorWrapper> animators = createListInNeeded();
        animators.add(new AnimatorWrapper(animator));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for (AnimatorWrapper animatorWrapper : animators) {
                    if (animatorWrapper.getAnimator().equals(animation)) {
                        animatorWrapper.clear();
                        animators.remove(animatorWrapper);
                        return;
                    }
                }
            }
        });
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

    public void clear() {
        if (this.animatorList != null) {
            for (AnimatorWrapper animator : animatorList) {
                animator.cancel();
            }
            this.animatorList.clear();
            this.animatorList = null;
        }
    }

    private List<AnimatorWrapper> createListInNeeded() {
        if (this.animatorList == null) {
            this.animatorList = new CopyOnWriteArrayList<>();
        }
        return this.animatorList;
    }

    private static class AnimatorWrapper {
        private final Animator animator;
        private long currentPlayTime;

        private AnimatorWrapper(Animator animator) {
            this.animator = animator;
        }

        void onResume() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.resume();
            } else {
                if (animator instanceof ValueAnimator) {
                    ValueAnimator valueAnimator = (ValueAnimator) animator;
                    currentPlayTime = valueAnimator.getCurrentPlayTime();
                }
                animator.cancel();
            }
        }

        void onPause() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.pause();
            } else {
                if (animator instanceof ValueAnimator) {
                    ValueAnimator valueAnimator = (ValueAnimator) animator;
                    valueAnimator.setCurrentPlayTime(currentPlayTime);
                    currentPlayTime = 0;
                }
                animator.start();
            }
        }

        void cancel() {
            animator.cancel();
            clear();
        }

        void clear() {
            animator.removeAllListeners();
        }

        Animator getAnimator() {
            return animator;
        }
    }
}
