package com.jenshen.awesomeanimation.callbacks;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jenshen.awesomeanimation.callbacks.AnimationState.CANCEL;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.END;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.PAUSE;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.REPEAT;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.RESUME;
import static com.jenshen.awesomeanimation.callbacks.AnimationState.START;

@IntDef({START, END, CANCEL, REPEAT, PAUSE, RESUME})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationState {
    int START = 1; // 0000 0001
    int END = 2; // 0000 0010
    int CANCEL = 4; // 0000 0100
    int REPEAT = 8; // 0000 1000
    int PAUSE = 16; // 0001 0000
    int RESUME = 32; // 0010 0000;
}