package com.github.silvestrpredko.dotprogressbar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.github.silvestrpredko.dotprogressbar.R;

/**
 * @author Silvestr Predko.
 */
public class DotProgressBar extends View {

  /**
   * Dots amount
   */
  private int dotAmount;

  /**
   * Drawing tools
   */
  private Paint primaryPaint;
  private Paint startPaint;
  private Paint endPaint;

  /**
   * Animation tools
   */
  private int animationTime;
  private BounceAnimation bounceAnimation;
  private float animatedRadius;
  private boolean isFirstLaunch = true;
  private ValueAnimator startValueAnimator;
  private ValueAnimator endValueAnimator;

  /**
   * Circle size
   */
  private float dotRadius;
  private float bounceDotRadius;

  /**
   * Circle coordinates
   */
  private float xCoordinate;
  private int dotPosition;

  /**
   * Colors
   */
  private int startColor;
  private int endColor;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initializeAttributes(attrs);
    init();
  }

  public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeAttributes(attrs);
    init();
  }

  public DotProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeAttributes(attrs);
    init();
  }

  public DotProgressBar(Context context) {
    super(context);
    initializeAttributes(null);
    init();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

    if (getMeasuredHeight() > getMeasuredWidth()) {
      dotRadius = getMeasuredWidth() / dotAmount / 4;
    } else {
      dotRadius = getMeasuredHeight() / 4;
    }

    bounceDotRadius = dotRadius + (dotRadius / 3);
    float circlesWidth = (dotAmount * (dotRadius * 2)) + dotRadius * (dotAmount - 1);
    xCoordinate = (getMeasuredWidth() - circlesWidth) / 2 + dotRadius;
  }

  private void initializeAttributes(AttributeSet attrs) {
    if (attrs != null) {
      TypedArray a = getContext().getTheme().obtainStyledAttributes(
          attrs,
          R.styleable.DotProgressBar,
          0, 0);

      try {
        dotAmount = a.getInteger(R.styleable.DotProgressBar_amount, 5);
        animationTime = a.getInteger(
            R.styleable.DotProgressBar_duration,
            getResources().getInteger(android.R.integer.config_mediumAnimTime)
        );
        startColor = a.getInteger(R.styleable.DotProgressBar_startColor, getResources().getColor(R.color.light_blue_A700));
        endColor = a.getInteger(R.styleable.DotProgressBar_endColor, getResources().getColor(R.color.light_blue_A400));
      } finally {
        a.recycle();
      }

    } else {
      dotAmount = 5;
      animationTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
      startColor = getResources().getColor(R.color.light_blue_A700);
      endColor = getResources().getColor(R.color.light_blue_A400);
    }
  }

  private void init() {
    primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    primaryPaint.setColor(startColor);
    primaryPaint.setStrokeJoin(Paint.Join.ROUND);
    primaryPaint.setStrokeCap(Paint.Cap.ROUND);
    primaryPaint.setStrokeWidth(20);

    startPaint = new Paint(primaryPaint);
    endPaint = new Paint(primaryPaint);

    startValueAnimator = ValueAnimator.ofInt(startColor, endColor);
    startValueAnimator.setDuration(animationTime);
    startValueAnimator.setEvaluator(new ArgbEvaluator());
    startValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        startPaint.setColor(((Integer) animation.getAnimatedValue()));
      }
    });

    endValueAnimator = ValueAnimator.ofInt(endColor, startColor);
    endValueAnimator.setDuration(animationTime);
    endValueAnimator.setEvaluator(new ArgbEvaluator());
    endValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        endPaint.setColor(((Integer) animation.getAnimatedValue()));
      }
    });
  }

  private void drawCircles(Canvas canvas, float radius) {
    float step = 0;
    for (int i = 0; i < dotAmount; i++) {
      if (dotPosition == i) {
        canvas.drawCircle(xCoordinate + step, getMeasuredHeight() / 2, dotRadius + radius, startPaint);
      } else {
        if ((i == (dotAmount - 1) && dotPosition == 0 && !isFirstLaunch) || ((dotPosition - 1) == i)) {
          canvas.drawCircle(xCoordinate + step, getMeasuredHeight() / 2, bounceDotRadius - radius, endPaint);
        } else {
          canvas.drawCircle(xCoordinate + step, getMeasuredHeight() / 2, dotRadius, primaryPaint);
        }
      }

      step += dotRadius * 3;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawCircles(canvas, animatedRadius);
  }

  private void stopAnimation() {
    this.clearAnimation();
    postInvalidate();
  }

  private void startAnimation() {
    bounceAnimation = new BounceAnimation();
    bounceAnimation.setDuration(animationTime);
    bounceAnimation.setRepeatCount(Animation.INFINITE);
    bounceAnimation.setInterpolator(new LinearInterpolator());
    bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {

      }

      @Override
      public void onAnimationRepeat(Animation animation) {
        dotPosition++;
        if (dotPosition == dotAmount) {
          dotPosition = 0;
        }

        startValueAnimator.start();

        if (!isFirstLaunch) {
          endValueAnimator.start();
        }

        isFirstLaunch = false;
      }
    });
    startAnimation(bounceAnimation);
  }

  public static int darker(int color, float factor) {
    int a = Color.alpha(color);
    int r = Color.red(color);
    int g = Color.green(color);
    int b = Color.blue(color);

    return Color.argb(a,
        Math.max((int) (r * factor), 0),
        Math.max((int) (g * factor), 0),
        Math.max((int) (b * factor), 0));
  }

  @Override
  protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);

    if (visibility == GONE || visibility == INVISIBLE) {
      stopAnimation();
    } else {
      startAnimation();
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    stopAnimation();
    super.onDetachedFromWindow();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    startAnimation();
  }

  private class BounceAnimation extends Animation {
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      super.applyTransformation(interpolatedTime, t);
      animatedRadius = (bounceDotRadius - dotRadius) * interpolatedTime;
      invalidate();
    }
  }
}
