package stacklibrary;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.happiness.androidunity2.R;
import com.happiness.androidunity2.UnityPlayerFragment;
import com.unity3d.player.UnityPlayer;

/**
 * extends  this Activity to facilitate the management of multiple fragment instances
 * User: chengwangyong(cwy545177162@163.com)
 * Date: 2016-01-19
 * Time: 18:32
 */
public abstract class RootActivity extends AppCompatActivity {

  public StackManager manager;
  public KeyCallBack callBack;
  protected UnityPlayer mUnityPlayer;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FrameLayout frameLayout = new FrameLayout(this);
    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
    frameLayout.setId(R.id.framLayoutId);

    mUnityPlayer = new UnityPlayer(this);
    setContentView(frameLayout);
    //RootFragment fragment = getRootFragment();
    manager = new StackManager(this);
    setAnim(R.anim.next_in, R.anim.next_out, R.anim.quit_in, R.anim.quit_out);
    //manager.setFragment(fragment);



    onCreateNow(savedInstanceState);
  }

  /**
   * Set the bottom of the fragment
   *
   * @return fragment
   */
  protected abstract @NonNull RootFragment getRootFragment();

  /**
   * Set page switch animation
   *
   * @param nextIn The next page to enter the animation
   * @param nextOut The next page out of the animation
   * @param quitIn The current page into the animation
   * @param quitOut Exit animation for the current page
   */
  public void setAnim(@AnimRes int nextIn, @AnimRes int nextOut, @AnimRes int quitIn,
      @AnimRes int quitOut) {
    manager.setAnim(nextIn, nextOut, quitIn, quitOut);
  }

  /**
   * Rewriting onCreate method
   *
   * @param savedInstanceState savedInstanceState
   */
  public void onCreateNow(Bundle savedInstanceState) {

  }

  @Override public final boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      case KeyEvent.KEYCODE_BACK:
        final Fragment from = this.getSupportFragmentManager().findFragmentByTag(
            UnityPlayerFragment.class.getName());
        if (from != null && from.isVisible()) {
        Fragment[] last = manager.getStack().getLast();
        Fragment to = last[0];

          if (to != null) {
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.show(to).hide(from).setCustomAnimations(R.anim.next_in, R.anim.next_out).commit();
            return true;
          }
        }

        manager.onBackPressed();
        return true;
      default:
        if (callBack != null) {
          return callBack.onKeyDown(keyCode, event) && mUnityPlayer.injectEvent(event);
        }
        break;
    }
    return super.onKeyDown(keyCode, event);
  }

  /**
   * Set button to click callback
   *
   * @param callBack callback
   */
  public void setKeyCallBack(KeyCallBack callBack) {
    this.callBack = callBack;
  }

  public UnityPlayer GetUnityPlayer() {
    return mUnityPlayer;
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
    return mUnityPlayer.injectEvent(event);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return mUnityPlayer.injectEvent(event);
  }

  /*API12*/
  public boolean onGenericMotionEvent(MotionEvent event) {
    return mUnityPlayer.injectEvent(event);
  }

  public abstract void initUnityFinish(String str);
}
