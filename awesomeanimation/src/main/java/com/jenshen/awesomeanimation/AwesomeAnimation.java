package com.jenshen.awesomeanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.jenshen.awesomeanimation.AwesomeAnimation.CoordinationMode.COORDINATES;
import static com.jenshen.awesomeanimation.AwesomeAnimation.CoordinationMode.TRANSITION;
import static com.jenshen.awesomeanimation.AwesomeAnimation.SizeMode.SCALE;
import static com.jenshen.awesomeanimation.AwesomeAnimation.SizeMode.SIZE;

@SuppressWarnings("unused")
public class AwesomeAnimation {

    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private static final Property<View, Float> PROPERTY_PADDING =
            new Property<View, Float>(Float.class, "padding") {

                @Override
                public void set(View object, Float value) {
                    object.getLayoutParams().width = value.intValue();
                    object.requestLayout();
                }

                @Override
                public Float get(View object) {
                    return (float) object.getLayoutParams().width;
                }
            };

    private static final Property<View, Float> PROPERTY_WIDTH =
            new Property<View, Float>(Float.class, "viewLayoutWidth") {

                @Override
                public void set(View object, Float value) {
                    object.getLayoutParams().width = value.intValue();
                    object.requestLayout();
                }

                @Override
                public Float get(View object) {
                    return (float) object.getLayoutParams().width;
                }
            };

    private static final Property<View, Float> PROPERTY_HEIGHT =
            new Property<View, Float>(Float.class, "viewLayoutHeight") {

                @Override
                public void set(View object, Float value) {
                    object.getLayoutParams().height = value.intValue();
                    object.requestLayout();
                }

                @Override
                public Float get(View object) {
                    return (float) object.getLayoutParams().height;
                }
            };

    private View view;
    @NonNull
    private List<AnimationParams> objectAnimations;
    @Nullable
    private List<Animator> animators;
    //animation params
    private Interpolator interpolator;
    private int duration = DEFAULT_ANIMATION_DURATION;
    private AnimatorSet animatorSet;

    private AwesomeAnimation(Builder builder) {
        view = builder.view;
        objectAnimations = builder.objectAnimations;
        animators = builder.animators;
        interpolator = builder.interpolator;
        duration = builder.duration;
        animatorSet = createAnimationSet();
    }

    public void start() {
        animatorSet.start();
    }

    public AnimatorSet getAnimatorSet() {
        return animatorSet;
    }

    private AnimatorSet createAnimationSet() {
        List<Animator> animators = new ArrayList<>();
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        if (!objectAnimations.isEmpty())
            for (AnimationParams customAnimation : objectAnimations) {
                animators.add(createAnimation(view, customAnimation));
            }

        if (this.animators != null)
            for (Animator animator : this.animators) {
                animators.add(animator);
            }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        if (interpolator != null) {
            animatorSet.setInterpolator(interpolator);
        }
        animatorSet.setDuration(duration);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
        return animatorSet;
    }

    private ObjectAnimator createAnimation(View view, AnimationParams params) {
        ObjectAnimator animator = null;
        if (params.attr != null) {
            if (params.paramsFloat != null) {
                animator = ObjectAnimator.ofFloat(view, params.attr, params.paramsFloat);
            } else if (params.paramsInt != null) {
                animator = ObjectAnimator.ofInt(view, params.attr, params.paramsInt);
            }
        } else {
            if (params.paramsFloat != null && params.propertyFloat != null) {
                animator = ObjectAnimator.ofFloat(view, params.propertyFloat, params.paramsFloat);
            } else if (params.paramsInt != null && params.propertyInt != null) {
                animator = ObjectAnimator.ofInt(view, params.propertyInt, params.paramsInt);
            }
        }
        if (animator == null) {
            throw new RuntimeException("Can't support this animation params");
        }

        if (params.repeatCount != 0) {
            animator.setRepeatCount(params.repeatCount);
        }
        if (params.evaluator != null) {
            animator.setEvaluator(params.evaluator);
        }
        if (params.interpolator != null) {
            animator.setInterpolator(params.interpolator);
        }
        if (params.animatorListenerAdapter != null) {
            animator.addListener(params.animatorListenerAdapter);
        }
        if (params.updateListener != null) {
            animator.addUpdateListener(params.updateListener);
        }
        return animator;
    }

    @StringDef({SizeMode.SCALE, SizeMode.SIZE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SizeMode {
        String SCALE = "scale";
        String SIZE = "size";
    }

    @StringDef({CoordinationMode.TRANSITION, CoordinationMode.COORDINATES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CoordinationMode {
        String TRANSITION = "transition";
        String COORDINATES = "coordinates";
    }

    @IntDef(value = {DirectionMode.LEFT, DirectionMode.RIGHT, DirectionMode.TOP, DirectionMode.BOTTOM}, flag = true)
    @Retention(RetentionPolicy.SOURCE)
    public @interface DirectionMode {
        int LEFT = 1;
        int RIGHT = 2;
        int TOP = 4;
        int BOTTOM = 8;
    }

    public static class Builder {

        @NonNull
        private View view;
        @NonNull
        private List<AnimationParams> objectAnimations;
        @Nullable
        private List<Animator> animators;
        @Nullable
        private Interpolator interpolator;
        private int duration = DEFAULT_ANIMATION_DURATION;
        private int repeatCount;

        public Builder(@NonNull View view) {
            this.view = view;
            this.objectAnimations = new ArrayList<>();
        }

        public Builder setX(@CoordinationMode String mode, float... x) {
            if (mode.equals(COORDINATES)) {
                objectAnimations.add(new AnimationParams.Builder(View.X, x).build());
            } else if (mode.equals(TRANSITION)) {
                objectAnimations.add(new AnimationParams.Builder(View.TRANSLATION_X, x).build());
            } else {
                throw new RuntimeException("Can't support this mode");
            }
            return this;
        }

        public Builder setY(@CoordinationMode String mode, float... y) {
            if (mode.equals(COORDINATES)) {
                objectAnimations.add(new AnimationParams.Builder(View.Y, y).build());
            } else if (mode.equals(TRANSITION)) {
                objectAnimations.add(new AnimationParams.Builder(View.TRANSLATION_Y, y).build());
            } else {
                throw new RuntimeException("Can't support this mode");
            }
            return this;
        }

        public Builder setSizeX(@SizeMode String mode, float... x) {
            if (mode.equals(SCALE)) {
                objectAnimations.add(new AnimationParams.Builder(View.SCALE_X, x).build());
            } else if (mode.equals(SIZE)) {
                objectAnimations.add(new AnimationParams.Builder(PROPERTY_WIDTH, x).build());
            } else {
                throw new RuntimeException("Can't support this mode");
            }
            return this;
        }

        public Builder setSizeY(@SizeMode String mode, float... y) {
            if (mode.equals(SCALE)) {
                objectAnimations.add(new AnimationParams.Builder(View.SCALE_Y, y).build());
            } else if (mode.equals(SIZE)) {
                objectAnimations.add(new AnimationParams.Builder(PROPERTY_HEIGHT, y).build());
            } else {
                throw new RuntimeException("Can't support this mode");
            }
            return this;
        }

        public Builder setPadding(@DirectionMode final int direction, int... padding) {
            int from;
            switch (direction) {
                case DirectionMode.LEFT:
                    from = view.getPaddingLeft();
                    break;
                case DirectionMode.TOP:
                    from = view.getPaddingTop();
                    break;
                case DirectionMode.RIGHT:
                    from = view.getPaddingRight();
                    break;
                case DirectionMode.BOTTOM:
                    from = view.getPaddingBottom();
                    break;
                default:
                    from = 0;
            }
            int[] values = new int[padding.length + 1];
            values[0] = from;
            System.arraycopy(padding, 0, values, 1, padding.length);
            ValueAnimator animator = ValueAnimator.ofInt(values);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @SuppressWarnings("PointlessBitwiseExpression")
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Integer animatedValue = (Integer) valueAnimator.getAnimatedValue();
                    int left;
                    if ((direction | DirectionMode.LEFT) == direction) {
                        left = animatedValue;
                    } else {
                        left = view.getPaddingLeft();
                    }
                    int right;
                    if ((direction | DirectionMode.RIGHT) == direction) {
                        right = animatedValue;
                    } else {
                        right = view.getPaddingRight();
                    }
                    int top;
                    if ((direction | DirectionMode.TOP) == direction) {
                        top = animatedValue;
                    } else {
                        top = view.getPaddingTop();
                    }
                    int bottom;
                    if ((direction | DirectionMode.BOTTOM) == direction) {
                        bottom = animatedValue;
                    } else {
                        bottom = view.getPaddingBottom();
                    }
                    view.setPadding(left, top, right, bottom);
                }
            });
            return addAnimator(animator);
        }

        public Builder setRotation(float... rotation) {
            objectAnimations.add(new AnimationParams.Builder(View.ROTATION, rotation).build());
            return this;
        }

        public Builder setAlpha(float... alpha) {
            objectAnimations.add(new AnimationParams.Builder(View.ALPHA, alpha).build());
            return this;
        }

        public Builder setRepeatCount(int repeatCount) {
            this.repeatCount = repeatCount;
            return this;
        }

        public Builder addObjectAnimation(AnimationParams animationParams) {
            objectAnimations.add(animationParams);
            return this;
        }

        public Builder addObjectAnimation(String attr, float... params) {
            objectAnimations.add(new AnimationParams.Builder(attr, params).build());
            return this;
        }

        public Builder addObjectAnimation(String attr, int... params) {
            objectAnimations.add(new AnimationParams.Builder(attr, params).build());
            return this;
        }

        public Builder addAnimator(@NonNull Animator animator) {
            if (animators == null) {
                animators = new ArrayList<>();
            }
            animators.add(animator);
            return this;
        }

        public Builder setInterpolator(@NonNull Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public AwesomeAnimation build() {
            if (repeatCount != 0) {
                for (AnimationParams objectAnimation : objectAnimations) {
                    objectAnimation.repeatCount = repeatCount;
                }
            }
            return new AwesomeAnimation(this);
        }

        private float[] deleteZeroFromArray(float[] array) {
            if (array == null) {
                return null;
            }
            List<Float> list = new ArrayList<>();
            for (float value : array) {
                list.add(value);
            }
            list.remove(0.0f);
            if (list.isEmpty()) {
                return null;
            } else {
                array = new float[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                return array;
            }
        }

        private void addTranslation(float[] array, float translation) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                array[i] += translation;
            }
        }
    }

    public static class AnimationParams {
        private String attr;
        private int repeatCount;
        @Nullable
        private Property<View, Float> propertyFloat;
        @Nullable
        private Property<View, Integer> propertyInt;
        @Nullable
        private Interpolator interpolator;
        @Nullable
        private TypeEvaluator evaluator;
        @Nullable
        private AnimatorListenerAdapter animatorListenerAdapter;
        @Nullable
        private ValueAnimator.AnimatorUpdateListener updateListener;
        @Nullable
        private float[] paramsFloat;
        @Nullable
        private int[] paramsInt;

        private AnimationParams(Builder builder) {
            this.attr = builder.attr;
            this.repeatCount = builder.repeatCount;
            this.propertyFloat = builder.propertyFloat;
            this.propertyInt = builder.propertyInt;
            this.interpolator = builder.interpolator;
            this.evaluator = builder.evaluator;
            this.paramsFloat = builder.paramsFloat;
            this.paramsInt = builder.paramsInt;
            this.animatorListenerAdapter = builder.animatorListenerAdapter;
            this.updateListener = builder.updateListener;
        }

        public static class Builder {
            private String attr;
            private int repeatCount;
            @Nullable
            private Property<View, Float> propertyFloat;
            @Nullable
            private Property<View, Integer> propertyInt;
            @Nullable
            private float[] paramsFloat;
            @Nullable
            private int[] paramsInt;

            //optional
            @Nullable
            private Interpolator interpolator;
            @Nullable
            private TypeEvaluator evaluator;
            @Nullable
            private AnimatorListenerAdapter animatorListenerAdapter;
            @Nullable
            private ValueAnimator.AnimatorUpdateListener updateListener;

            public Builder(String attr, @NonNull float... params) {
                this.attr = attr;
                this.paramsFloat = params;
            }

            public Builder(String attr, @NonNull int... params) {
                this.attr = attr;
                this.paramsInt = params;
            }

            public Builder(@NonNull Property<View, Float> property, @NonNull float... params) {
                this.propertyFloat = property;
                this.paramsFloat = params;
            }

            public Builder(@NonNull Property<View, Integer> property, @NonNull int... params) {
                this.propertyInt = property;
                this.paramsInt = params;
            }

            public Builder setRepeatCount(int repeatCount) {
                this.repeatCount = repeatCount;
                return this;
            }

            public Builder setInterpolator(@NonNull Interpolator interpolator) {
                this.interpolator = interpolator;
                return this;
            }

            public Builder setEvaluator(@NonNull TypeEvaluator evaluator) {
                this.evaluator = evaluator;
                return this;
            }

            public Builder setAnimatorListenerAdapter(@Nullable AnimatorListenerAdapter animatorListenerAdapter) {
                this.animatorListenerAdapter = animatorListenerAdapter;
                return this;
            }

            public Builder setUpdateListener(@Nullable ValueAnimator.AnimatorUpdateListener updateListener) {
                this.updateListener = updateListener;
                return this;
            }

            public AnimationParams build() {
                return new AnimationParams(this);
            }
        }
    }
}
