package com.happiness.androidunity2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.unity3d.player.UnityPlayer;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnityPlayerFragment extends Fragment {

  //@BindView(R.id.btn_next) Button btnNext;
  @BindView(R.id.framLayoutId) FrameLayout framLayoutId;
  @BindView(R.id.img_splash) ImageView imgSplash;
  @BindView(R.id.main_container) LinearLayout mainContainer;

  public UnityPlayerFragment() {
    // Required empty public constructor
  }

  private UnityPlayerActivity mUnityMainActivity;
  private UnityPlayer mUnityPlayer;
  View playerView;

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    FrameLayout frameLayout =
        (FrameLayout) inflater.inflate(R.layout.fragment_unity_player, container, false);

    mUnityMainActivity = (UnityPlayerActivity) getActivity();
    mUnityPlayer = mUnityMainActivity.GetUnityPlayer();
    playerView = mUnityPlayer.getView();
    FrameLayout.LayoutParams lp =
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);
    playerView.setLayoutParams(lp);
    if (playerView.getParent() != null) {
      ((ViewGroup) playerView.getParent()).removeAllViews();
    }
    frameLayout.addView(playerView, 0);

    ButterKnife.bind(this, frameLayout);
    return frameLayout;
  }

  //@OnClick(R.id.btn_next) public void onClick() {
  //
  //  FirstFragment fragment =
  //      (FirstFragment) ((UnityPlayerActivity) this.getActivity()).getSupportFragmentManager()
  //          .findFragmentByTag(FirstFragment.class.getName());
  //
  //  ((UnityPlayerActivity) this.getActivity()).switchContent(this, fragment);
  //}

  public void initUnityFinish(String str) {
    //framLayoutId.setVisibility(View.VISIBLE);



    imgSplash.setVisibility(View.GONE);

    ((UnityPlayerActivity) this.getActivity()).getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.main_container, new ThirdFragment())
        .commit();
    Log.d(TAG, "UnityPlayerFragment ------------------- initUnityFinish: ");
  }
}
