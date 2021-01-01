package com.wp.cheez.window;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.ref.WeakReference;

class SimpleAnimator {

    private WeakReference<View> mViewRef;
    private int animation;

    public SimpleAnimator(View view, int anim) {
        this.animation = anim;
        this.mViewRef = new WeakReference<>(view);
    }

    public void startAnimation() {
        startAnimation(null);
    }

    public void startAnimation(Animation.AnimationListener listener) {
        mViewRef.get().clearAnimation();
        Animation anim = AnimationUtils.loadAnimation(mViewRef.get().getContext(), animation);
        if (listener != null) {
            anim.setAnimationListener(listener);
        }
        anim.setFillAfter(true);
        mViewRef.get().startAnimation(anim);
    }
}
