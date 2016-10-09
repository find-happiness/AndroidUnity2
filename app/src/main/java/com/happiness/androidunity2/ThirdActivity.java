package com.happiness.androidunity2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThirdActivity extends AppCompatActivity {

  @BindView(R.id.btn_next) Button btnNext;

  public static Intent getStartActivity(Context ctx) {
    return new Intent(ctx, ThirdActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_third);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btn_next) public void onClick() {
    startActivity(UnityPlayerActivity.getStartIntent(this, UnityPlayerActivity.FLAG_UNITY));
  }
}
