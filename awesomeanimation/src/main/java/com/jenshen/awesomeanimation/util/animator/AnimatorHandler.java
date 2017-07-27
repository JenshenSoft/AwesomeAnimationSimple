package com.jenshen.awesomeanimation.util.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimatorHandler {

    private static final String TAG = "AwesomeAnimation: " + AnimatorHandler.class.getSimpleName();

    private List<AnimatorWrapper> animatorList;
    private boolean onPause;

    private AnimatorListenerAdapter animatorListenerAdapter;

    public void addAnimator(final Animator animator) {
        final List<AnimatorWrapper> animators = createListAnimatorsInNeeded();
        animators.add(new AnimatorWrapper(animator));
        animator.addListener(animatorListenerAdapter);
    }

    public void removeAnimator(final Animator animator) {
        if (animatorList != null) {
            for (AnimatorWrapper animatorWrapper : animatorList) {
                if (equalsAnimators(animatorWrapper.getAnimator(), animator)) {
                    animatorWrapper.clear();
                    animatorList.remove(animatorWrapper);
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
        Log.d(TAG, "onResume");
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
        Log.d(TAG, "onPause");
        if (this.animatorList != null) {
            for (AnimatorWrapper animator : this.animatorList) {
                animator.onPause();
            }
        }
    }

    public void cancel() {
        Log.d(TAG, "cancel");
        if (this.animatorList != null) {
            for (AnimatorWrapper animator : animatorList) {
                animator.cancel();
            }
            this.animatorList.clear();
            this.animatorList = null;
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

    private boolean equalsAnimators(Animator animation1, Animator animation2) {
        if (animation1.equals(animation2)) {
            return true;
        }
        if (animation1.getDuration() != animation2.getDuration()) {
            return false;
        }
        if (animation1.getStartDelay() != animation2.getStartDelay()) {
            return false;
        }
        boolean isObjectAnimator1 = animation1 instanceof ObjectAnimator;
        boolean isObjectAnimator2 = animation2 instanceof ObjectAnimator;
        if (isObjectAnimator1 || isObjectAnimator2) {
            if (!(isObjectAnimator1) || !(isObjectAnimator2)) {
                return false;
            }
            ObjectAnimator objectAnimator1 = (ObjectAnimator) animation1;
            ObjectAnimator objectAnimator2 = (ObjectAnimator) animation2;
            String propertyName1 = objectAnimator1.getPropertyName();
            String propertyName2 = objectAnimator2.getPropertyName();
            if (propertyName1 != null || propertyName2 != null) {
                if (propertyName1 != null && propertyName2 == null || propertyName1 == null) {
                    return false;
                }
                if (!propertyName1.equals(propertyName2)) {
                    return false;
                }
            }
        }
        boolean isValueAnimator1 = animation1 instanceof ValueAnimator;
        boolean isValueAnimator2 = animation2 instanceof ValueAnimator;
        if (isValueAnimator1 || isValueAnimator2) {
            if (!(isValueAnimator1) || !(isValueAnimator2)) {
                return false;
            }
            ValueAnimator valueAnimator1 = (ValueAnimator) animation1;
            ValueAnimator valueAnimator2 = (ValueAnimator) animation2;
            Object animatedValue1 = valueAnimator1.getAnimatedValue();
            Object animatedValue2 = valueAnimator2.getAnimatedValue();
            if (animatedValue1 != null || animatedValue2 != null) {
                if (animatedValue1 != null && animatedValue2 == null || animatedValue1 == null) {
                    return false;
                }
                if (!animatedValue1.equals(animatedValue2)) {
                    return false;
                }
            }
            PropertyValuesHolder[] values1 = valueAnimator1.getValues();
            PropertyValuesHolder[] values2 = valueAnimator2.getValues();
            if (values1.length != values2.length) {
                return false;
            }
            for (int i = 0; i < values1.length; i++) {
                PropertyValuesHolder propertyValuesHolder1 = values1[i];
                PropertyValuesHolder propertyValuesHolder2 = values2[i];
                if (!propertyValuesHolder1.getPropertyName().equals(propertyValuesHolder2.getPropertyName())) {
                    return false;
                }
            }
        }
        boolean isAnimatorSet1 = animation1 instanceof AnimatorSet;
        boolean isAnimatorSet2 = animation2 instanceof AnimatorSet;
        if (isAnimatorSet1 || isAnimatorSet2) {
            if (!(isAnimatorSet1) || !(isAnimatorSet2)) {
                return false;
            }
            AnimatorSet animatorSet1 = (AnimatorSet) animation1;
            AnimatorSet animatorSet2 = (AnimatorSet) animation2;
            ArrayList<Animator> childAnimations1 = animatorSet1.getChildAnimations();
            ArrayList<Animator> childAnimations2 = animatorSet2.getChildAnimations();
            if (childAnimations1 != null || childAnimations2 != null) {
                if (childAnimations1 != null && childAnimations2 == null || childAnimations1 == null) {
                    return false;
                }
                if (childAnimations1.size() != childAnimations2.size()) {
                    return false;
                }
                for (int i = 0; i < childAnimations1.size(); i++) {
                    Animator animatorChild1 = childAnimations1.get(i);
                    Animator animatorChild2 = childAnimations2.get(i);
                    if (!equalsAnimators(animatorChild1, animatorChild2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
