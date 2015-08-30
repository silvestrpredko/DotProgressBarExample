package com.silvestr.dotprogressbarexample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    Button btnDotAmount = (Button) findViewById(R.id.btn_change_dot_amount);
    dotProgressBar = (DotProgressBar) findViewById(R.id.dot_progress_bar);

    btnDotAmount.setOnClickListener(this);

    Uri uri = Uri.parse("http://i1.wp.com/cdn.techreviewpro.com//2015/03/Amazing-WhatsApp-DP-Wonderful-Stylish-Girls-for-fb-Profile-Picture.jpg");
    Glide.with(this).load(uri)
        .transform(new CircleTransform(this))
        .placeholder(getResources().getDrawable(R.mipmap.ic_account_circle_black_48dp))
        .into(profileImageView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View v) {
    if (dotProgressBar.getVisibility() == View.VISIBLE) {
      dotProgressBar.setVisibility(View.GONE);
    } else {
      dotProgressBar.setVisibility(View.VISIBLE);
    }
  }

  static class CircleTransform extends BitmapTransformation {
    public CircleTransform(Context context) {
      super(context);
    }

    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
      return circleCrop(pool, toTransform);
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

    @Override public String getId() {
      return getClass().getName();
    }
  }
}
