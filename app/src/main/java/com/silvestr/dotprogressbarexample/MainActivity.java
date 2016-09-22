package com.silvestr.dotprogressbarexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;

public class MainActivity extends AppCompatActivity {

  DotProgressBar dotProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setStatusBarColor(getResources().getColor(R.color.indigo_700));
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitleTextColor(getResources().getColor(R.color.indigo_50));
    setSupportActionBar(toolbar);

    getSupportActionBar().setTitle(getString(R.string.app_name));
    ImageView profileImageView = (ImageView) findViewById(R.id.imageView);
    final Button btnChangeVisibility = (Button) findViewById(R.id.btn_visibility);
    final Button btnChangeAnimationDirection = (Button) findViewById(R.id.btn_direction);
    dotProgressBar = (DotProgressBar) findViewById(R.id.dot_progress_bar);

    btnChangeVisibility.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (dotProgressBar.getVisibility() == View.VISIBLE) {
          dotProgressBar.setVisibility(View.INVISIBLE);
        } else {
          dotProgressBar.setVisibility(View.VISIBLE);
        }
      }
    });

    btnChangeAnimationDirection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (dotProgressBar.getAnimationDirection() < 0) {
          dotProgressBar.changeAnimationDirection(DotProgressBar.RIGHT_DIRECTION);
        } else {
          dotProgressBar.changeAnimationDirection(DotProgressBar.LEFT_DIRECTION);
        }
      }
    });

    final FrameLayout progressBarContainer =
            (FrameLayout) findViewById(R.id.container);

    DotProgressBarBuilder builder = new DotProgressBarBuilder(this);
    builder.setStartColor(ContextCompat.getColor(this, R.color.deep_purple_800))
            .setEndColor(ContextCompat.getColor(this, R.color.deep_purple_400))
            .setAnimationDirection(DotProgressBar.LEFT_DIRECTION)
            .setDotAmount(7);

    progressBarContainer.addView(
            builder.build(),
            new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    150,
                    Gravity.CENTER
            )
    );

    Uri uri = Uri.parse("http://i1.wp.com/cdn.techreviewpro.com//2015/03/Amazing-WhatsApp-DP-Wonderful-Stylish-Girls-for-fb-Profile-Picture.jpg");
    Glide.with(this).load(uri)
        .transform(new CircleTransform(this))
        .placeholder(getResources().getDrawable(R.mipmap.ic_account_circle_black_48dp))
        .into(profileImageView);
  }

  static class CircleTransform extends BitmapTransformation {
    public CircleTransform(Context context) {
      super(context);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
      if (source == null) return null;

      int size = Math.min(source.getWidth(), source.getHeight());
      int x = (source.getWidth() - size) / 2;
      int y = (source.getHeight() - size) / 2;

      Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

      Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
      if (result == null) {
        result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
      }

      Canvas canvas = new Canvas(result);
      Paint paint = new Paint();
      paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
      paint.setAntiAlias(true);
      float r = size / 2f;
      canvas.drawCircle(r, r, r, paint);
      return result;
    }

    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
      return circleCrop(pool, toTransform);
    }

    @Override public String getId() {
      return getClass().getName();
    }
  }
}
