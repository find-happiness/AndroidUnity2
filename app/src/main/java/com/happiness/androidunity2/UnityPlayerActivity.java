package com.happiness.androidunity2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import com.unity3d.player.UnityPlayer;

public class UnityPlayerActivity extends AppCompatActivity {

  public static final int FLAG_UNITY = 1;
  public static final int FLAG_MENU = 2;

  private static final String FLAG = "flag";

  protected UnityPlayer mUnityPlayer;
  // don't change the name of this variable; referenced from native code
  private Fragment isFragment;
  private MainFragment mainFragment;
  private static final String TAG = "UnityPlayerActivity";

  public static Intent getStartIntent(Context ctx, int flag) {
    Intent intent = new Intent(ctx, UnityPlayerActivity.class);
    intent.putExtra(FLAG, flag);
    return intent;
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
    switch (intent.getIntExtra(FLAG, FLAG_MENU)) {
      case FLAG_MENU:
        transaction.hide(isFragment).show(mainFragment).commit();
        break;
      case FLAG_UNITY:
        transaction.hide(mainFragment).show(isFragment).commit();
        break;
    }
  }

  // Setup activity layout
  @Override protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);

    getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy
    //FrameLayout frameLayout = new FrameLayout(this);
    //frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
    //    ViewGroup.LayoutParams.MATCH_PARENT));
    //frameLayout.setId(R.id.framLayoutId);

    setContentView(R.layout.activity_unity_player);
    mUnityPlayer = new UnityPlayer(this);
    //为UnityPlayer设置一些参数
    if (mUnityPlayer.getSettings().getBoolean("hide_status_bar", false)) {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    isFragment = new UnityPlayerFragment();
    mainFragment = new MainFragment();
    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
    //transaction.addToBackStack("Unity");
    transaction.replace(R.id.framLayoutId, isFragment, UnityPlayerFragment.class.getName())
        .replace(R.id.content, mainFragment, MainFragment.class.getName())
        .hide(mainFragment)
        .commit();

    //mUnityPlayer = new UnityPlayer(this);
    //setContentView(mUnityPlayer);
    //mUnityPlayer.requestFocus();
  }

  public UnityPlayer GetUnityPlayer() {
    return mUnityPlayer;
  }

  @Override public void onBackPressed() {

    if (!mainFragment.isVisible()) {
      FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
      transaction.hide(isFragment).show(mainFragment).commit();
      return;
    }

    super.onBackPressed();
  }

  // Quit Unity
  @Override protected void onDestroy() {
    mUnityPlayer.quit();
    super.onDestroy();
  }

  // Pause Unity
  @Override protected void onPause() {
    super.onPause();
    mUnityPlayer.pause();
  }

  // Resume Unity
  @Override protected void onResume() {
    super.onResume();
    mUnityPlayer.resume();
  }

  // This ensures the layout will be correct.
  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mUnityPlayer.configurationChanged(newConfig);
  }

  // Notify Unity of the focus change.
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    mUnityPlayer.windowFocusChanged(hasFocus);
  }

  // For some reason the multiple keyevent type is not supported by the ndk.
  // Force event injection by overriding dispatchKeyEvent().
  @Override public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_MULTIPLE) return mUnityPlayer.injectEvent(event);
    return super.dispatchKeyEvent(event);
  }

  // Pass any events not handled by (unfocused) views straight to UnityPlayer
  @Override public boolean onKeyUp(int keyCode, KeyEvent event) {

    switch (keyCode) {
      case KeyEvent.KEYCODE_BACK:
        return super.onKeyUp(keyCode, event);
    }
    return mUnityPlayer.injectEvent(event);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {

    switch (keyCode) {
      case KeyEvent.KEYCODE_BACK:
        return super.onKeyDown(keyCode, event);
    }

    return mUnityPlayer.injectEvent(event);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return mUnityPlayer.injectEvent(event);
  }

  /*API12*/
  public boolean onGenericMotionEvent(MotionEvent event) {
    return mUnityPlayer.injectEvent(event);
  }

  public void initUnityFinish(final String str) {

    //switchContent(isFragment, new MainFragment());

    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
    transaction.hide(isFragment).show(mainFragment).commit();

    mUnityPlayer.post(new Runnable() {
      @Override public void run() {
        ((UnityPlayerFragment) getSupportFragmentManager().findFragmentByTag(
            UnityPlayerFragment.class.getName())).initUnityFinish(str);
      }
    });
  }

  public void openDrawerLayout(String str) {
    startActivity(SecondActivity.getStartActivity(this));
  }

  /**
   * 当fragment进行切换时，采用隐藏与显示的方法加载fragment以防止数据的重复加载
   */
  public void switchContent(Fragment from, Fragment to) {
    if (isFragment != to && to != null) {
      isFragment = to;
      FragmentManager fm = getSupportFragmentManager();
      //添加渐隐渐现的动画
      FragmentTransaction ft = fm.beginTransaction();
      //ft.addToBackStack("Android");
      if (!to.isAdded()) {    // 先判断是否被add过
        ft.hide(from)
            .add(R.id.framLayoutId, to, to.getClass().getName())
            .commit(); // 隐藏当前的fragment，add下一个到Activity中
      } else {
        ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
      }
    }
  }
}
