package com.guesscity.guessthecity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Igor on 01.08.15.
 */
public class LevelUpActivity extends Activity implements View.OnClickListener {
    private TextView textViewInfo;
    private Button buttonContinue;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_up_activity);

        textViewInfo = (TextView) findViewById(R.id.textViewLevelUpInfo);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);

        mSettings = getSharedPreferences(getResources()
                .getString(R.string.APP_PREFERENCES).toString(), Context.MODE_PRIVATE);
        editor = mSettings.edit();

        textViewInfo.setOnClickListener(this);

        textViewInfo.setText("Вы перешли на новый уровень: " + mSettings.getInt(getResources().getString(R.string.APP_PREFERENCES_CURRENT_LEVEL), 0));

        buttonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      switch (view.getId()) {
          case R.id.buttonContinue:
              finish();
              Intent intent = new Intent(this, MainActivity.class);
              startActivity(intent);
              break;
      }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putInt(getResources().getString(R.string.APP_PREFERENCES_CURRENT_LEVEL), 1);
        editor.apply();
    }
}
