package com.happiness.androidunity2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import stacklibrary.RootFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

  @BindView(R.id.btn_next) Button btnNext;

  public MainFragment() {
    // Required empty public constructor
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.btn_next) public void onClick() {
    startActivity(SecondActivity.getStartActivity(this.getActivity()));
  }
}
