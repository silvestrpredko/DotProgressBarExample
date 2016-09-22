package com.github.silvestrpredko.dotprogressbar;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * @author Silvestr Predko.
 */
public class DotProgressBarBuilder {

    private DotProgressBar dotProgressBar;

    public DotProgressBarBuilder(@NonNull Context context) {
        dotProgressBar = new DotProgressBar(context);
    }

    public DotProgressBarBuilder setDotAmount(int amount) {
        dotProgressBar.setDotAmount(amount);
        return this;
    }

    public DotProgressBarBuilder setStartColor(@ColorInt int color) {
        dotProgressBar.setStartColor(color);
        return this;
    }

    public DotProgressBarBuilder setEndColor(@ColorInt int color) {
        dotProgressBar.setEndColor(color);
        return this;
    }

    public DotProgressBarBuilder setAnimationTime(long animationTime) {
        dotProgressBar.setAnimationTime(animationTime);
        return this;
    }

    public DotProgressBarBuilder setAnimationDirection(
            @DotProgressBar.AnimationDirection int direction) {
        dotProgressBar.setAnimationDirection(direction);
        return this;
    }

    public DotProgressBar build() {
        dotProgressBar.reinitialize();
        return dotProgressBar;
    }
}