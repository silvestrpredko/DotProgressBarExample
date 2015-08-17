package com.silvestr.dotprogressbar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
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

/**
 * @author Silvestr Predko.
 */
public class DotProgressBar extends View {

  /**
   * Dots amount
   */
  private static final int DOT_AMOUNT = 5;

  /**
   * Drawing tools
   */
  private Paint paint;
  private Paint secondColorPaint;
  private Paint thirdColorPaint;

  /**
   * Animation tools
   */
  private static final int ANIMATION_TIME = 300;
  private BounceAnimation bounceAnimation;
  private float animatedRadius;
  private boolean isFirstLaunch = true;
  private float mInterpolatedTime;

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

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public DotProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DotProgressBar(Context context) {
    super(context);
    init();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

    if (getMeasuredHeight() > getMeasuredWidth()) {
      dotRadius = getMeasuredWidth() / DOT_AMOUNT / 4;
    } else {
      dotRadius = getMeasuredHeight() / 4;
    }

    bounceDotRadius = dotRadius + (dotRadius / 3);
    float circlesWidth = (DOT_AMOUNT * (dotRadius * 2)) + dotRadius * (DOT_AMOUNT - 1);
    xCoordinate = (getMeasuredWidth() - circlesWidth) / 2 + dotRadius;
  }

  private void init() {
    paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    paint.setColor(Color.parseColor("#0D47A1"));
    paint.setStrokeJoin(Paint.Join.ROUND);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setStrokeWidth(20);

    secondColorPaint = new Paint(paint);
    thirdColorPaint = new Paint(paint);
  }

  private void drawCircles(Canvas canvas, float radius) {
    float step = 0;
    for (int i = 0; i < DOT_AMOUNT; i++) {
      if (dotPosition == i) {
        canvas.drawCircle(xCoordinate + step, getMeasuredHeight() / 2, dotRadius + radius, secondColorPaint);
      } else {
        if ((i == (DOT_AMOUNT - 1) && dotPosition == 0 && !isFirstLaunch) || ((dotPosition - 1) == i)) {
          canvas.drawCircle(xCoordinate + step, getMeasuredHeight() / 2, bounceDotRadius - radius, thirdColorPaint);
        } else {
          canvas.drawCircle(xCoordinate + step, getMeasuredHeight() / 2, dotRadius, paint);
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
    bounceAnimation.setDuration(ANIMATION_TIME);
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
        if (dotPosition == DOT_AMOUNT) {
          dotPosition = 0;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(Color.parseColor("#0D47A1"), Color.parseColor("#1976D2"));
        valueAnimator.setDuration(ANIMATION_TIME);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            secondColorPaint.setColor(((Integer) animation.getAnimatedValue()));
          }
        });
        valueAnimator.start();

        ValueAnimator valueAnimator1 = ValueAnimator.ofInt(Color.parseColor("#1976D2"), Color.parseColor("#0D47A1"));
        valueAnimator1.setDuration(ANIMATION_TIME);
        valueAnimator1.setEvaluator(new ArgbEvaluator());
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            thirdColorPaint.setColor(((Integer) animation.getAnimatedValue()));
          }
        });
        valueAnimator1.start();

        isFirstLaunch = false;
      }
    });
    startAnimation(bounceAnimation);
  }

  public static int darker (int color, float factor) {
    int a = Color.alpha( color );
    int r = Color.red(color);
    int g = Color.green( color );
    int b = Color.blue( color );

    return Color.argb( a,
        Math.max( (int)(r * factor), 0 ),
        Math.max( (int)(g * factor), 0 ),
        Math.max( (int)(b * factor), 0 ) );
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
      mInterpolatedTime = interpolatedTime;
      animatedRadius = (bounceDotRadius - dotRadius) * interpolatedTime;
      invalidate();
    }
  }
}
