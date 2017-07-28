package com.jenshen.awesomeanimation.util.animator;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;

import java.util.ArrayList;

public class AnimatorUtil {

    public static boolean equalsAnimators(Animator animation1, Animator animation2) {
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
        if (isObjectAnimator1 && isObjectAnimator2) {
            ObjectAnimator objectAnimator1 = (ObjectAnimator) animation1;
            ObjectAnimator objectAnimator2 = (ObjectAnimator) animation2;
            String propertyName1 = objectAnimator1.getPropertyName();
            String propertyName2 = objectAnimator2.getPropertyName();
            if (propertyName1 != null && propertyName2 != null) {
                if (!propertyName1.equals(propertyName2)) {
                    return false;
                }
            } else if (propertyName1 != null || propertyName2 != null) {
                return false;
            }
        } else if (isObjectAnimator1 || isObjectAnimator2) {
            return false;
        }

        boolean isValueAnimator1 = animation1 instanceof ValueAnimator;
        boolean isValueAnimator2 = animation2 instanceof ValueAnimator;
        if (isValueAnimator1 && isValueAnimator2) {
            ValueAnimator valueAnimator1 = (ValueAnimator) animation1;
            ValueAnimator valueAnimator2 = (ValueAnimator) animation2;
            Object animatedValue1 = valueAnimator1.getAnimatedValue();
            Object animatedValue2 = valueAnimator2.getAnimatedValue();
            if (animatedValue1 != null && animatedValue2 != null) {
                if (!animatedValue1.equals(animatedValue2)) {
                    return false;
                }
            } else if (animatedValue1 != null || animatedValue2 != null) {
                return false;
            }
            PropertyValuesHolder[] values1 = valueAnimator1.getValues();
            PropertyValuesHolder[] values2 = valueAnimator2.getValues();
            if (values1 != null && values2 != null) {
                if (values1.length != values2.length) {
                    return false;
                }
                for (int i = 0; i < values1.length; i++) {
                    PropertyValuesHolder propertyValuesHolder1 = values1[i];
                    PropertyValuesHolder propertyValuesHolder2 = values2[i];
                    String propertyName1 = propertyValuesHolder1.getPropertyName();
                    String propertyName2 = propertyValuesHolder2.getPropertyName();
                    if (propertyName1 != null && propertyName2 != null) {
                        if (!propertyName1.equals(propertyName2)) {
                            return false;
                        }
                    } else if (propertyName1 != null || propertyName2 != null) {
                        return false;
                    }
                }
            } else if (values1 != null || values2 != null) {
                return false;
            }
        } else if (isValueAnimator1 || isValueAnimator2) {
            return false;
        }

        boolean isAnimatorSet1 = animation1 instanceof AnimatorSet;
        boolean isAnimatorSet2 = animation2 instanceof AnimatorSet;
        if (isAnimatorSet1 && isAnimatorSet2) {
            AnimatorSet animatorSet1 = (AnimatorSet) animation1;
            AnimatorSet animatorSet2 = (AnimatorSet) animation2;
            ArrayList<Animator> childAnimations1 = animatorSet1.getChildAnimations();
            ArrayList<Animator> childAnimations2 = animatorSet2.getChildAnimations();
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
        } else if (isAnimatorSet1 || isAnimatorSet2) {
            return false;
        }
        return true;
    }
}
