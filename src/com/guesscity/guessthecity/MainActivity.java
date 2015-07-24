package com.guesscity.guessthecity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener {
    private SharedPreferences mSettings;
    private RadioButton radioCity, radioCountry;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayoutTopBar;
    private LinearLayout linearLayoutEndGame;
    private LinearLayout linearLayoutAnswerButtons;
    private TextView textViewEndGameMessage;
    private RatingBar livesWidget;
    private ProgressBar progressBar;
    private Button btnRestartGame, btnQuitGame;
    private Button button1, button2, button3, button4;
    private HashMap pictures;
    private HashMap pictures_name;
    private Integer keyOfActivePicture;
    private List<Integer> remainderPictures = new ArrayList<Integer>();
    private final Handler handler = new Handler();
    private Integer currentLives;
    private final static Integer LIVES = 3;
    private Bundle savedInstanceState;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.main_activity);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        livesWidget = (RatingBar) findViewById(R.id.ratingBar);
        relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        linearLayoutTopBar = (LinearLayout) findViewById(R.id.linearLayoutTopBar);
        linearLayoutAnswerButtons = (LinearLayout) findViewById(R.id.linearLayoutAnswerButtons);
        linearLayoutEndGame = (LinearLayout) findViewById(R.id.linearLayoutEndGame);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        btnRestartGame = (Button) findViewById(R.id.buttonRestartGame);
        btnQuitGame = (Button) findViewById(R.id.buttonQuitGame);
        textViewEndGameMessage = (TextView) findViewById(R.id.textViewEndGameMessage);
        radioCity = (RadioButton) findViewById(R.id.radioBtnCity);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        btnRestartGame.setOnClickListener(this);
        btnQuitGame.setOnClickListener(this);


        pictures = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.pictures);

        mSettings = getSharedPreferences(getResources().getString(R.string.APP_PREFERENCES).toString(), Context.MODE_PRIVATE);
        Boolean city = true;
        if (mSettings.contains(getResources().getString(R.string.APP_PREFERENCES_CITY_GAME).toString())) {
            // Получаем число из настроек
            city = mSettings.getBoolean(getResources().getString(R.string.APP_PREFERENCES_CITY_GAME).toString(), true);

        }

        if (city) {
            pictures_name = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.city_names);
        } else {
            pictures_name = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.country_names);

        }


        updateLives(LIVES);
        init(remainderPictures);
        keyOfActivePicture = getRandomValue(remainderPictures);

        loadMainPicture(keyOfActivePicture);
        buttonsInitialization();

        progressBar.setMax(getAmountOfAllQuestions());
        progressBar.setProgress(0);


    }

    private Integer getAmountOfAllQuestions() {
        return pictures.size();
    }

    private Integer getAmountOfRemainderQuestions() {
        return remainderPictures.size();
    }

    private void updateLives(Integer lives) {
        currentLives = lives;
        livesWidget.setRating(lives);
        livesWidget.setIsIndicator(true);
    }

    private void updateProgress() {
        progressBar.setProgress(getNumberOfActiveQuestion());
    }

    private void processEndGame() {

        String message;
        linearLayoutAnswerButtons.setVisibility(View.INVISIBLE);
        linearLayoutTopBar.setAlpha((float) 0.5);

        if (currentLives > 0) {
            message = "YOU WIN! CONGRATULATIONS!";

        } else {
            message = "YOU LOSE! TRY AGAIN";
        }
        textViewEndGameMessage.setText(message);
        linearLayoutEndGame.setVisibility(View.VISIBLE);

    }

    private Integer getNumberOfActiveQuestion() {
        return (getAmountOfAllQuestions() - getAmountOfRemainderQuestions());
    }


    private void goToNextQuestion() {
        updateProgress();
        keyOfActivePicture = getRandomValue(remainderPictures);
        loadMainPicture(keyOfActivePicture);
        buttonsInitialization();

    }

    private Boolean lastQuestion() {
        return remainderPictures.size() > 0 ? false : true;
    }


    private void setAllButtonsClickable(Boolean condition) {

        button1.setClickable(condition);
        button2.setClickable(condition);
        button3.setClickable(condition);
        button4.setClickable(condition);


    }

    private void processRightAnswer(Button button) {

        processRightButton(button);
        setAllButtonsClickable(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!lastQuestion()) {


                    goToNextQuestion();

                } else {

                    processEndGame();
                }
            }
        }, 1000);


    }


    private void processWrongAnswer(Button button) {

        currentLives--;
        updateLives(currentLives);
        processWrongButton(button);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentLives == 0) {
                    processEndGame();
                } else {

                    processRightAnswer(getCorrectAnswerButton());

                }
            }
        }, 1000);




    }

    private void processWrongButton(Button button) {
        button.setClickable(false);
        button.setBackgroundResource(R.anim.incorrect_button);
        AnimationDrawable anim = (AnimationDrawable) button.getBackground();
        anim.start();

    }


    private void processRightButton(Button button) {
        button.setClickable(false);
        button.setBackgroundResource(R.anim.correct_button);
        AnimationDrawable anim = (AnimationDrawable) button.getBackground();
        anim.start();
    }

    private Button getCorrectAnswerButton() {
        if ((Integer) button1.getTag() == (Integer) relativeLayout.getTag()) {

            return button1;
        } else if ((Integer) button2.getTag() == (Integer) relativeLayout.getTag())

        {
            return button2;
        } else if ((Integer) button3.getTag() == (Integer) relativeLayout.getTag())

        {
            return button3;
        } else if ((Integer) button4.getTag() == (Integer) relativeLayout.getTag())

        {
            return button4;
        }
        return null;


    }

    private void processTheAnswer(Button button, RelativeLayout layout) {


        if ((Integer) button.getTag() == (Integer) layout.getTag()) {
            processRightAnswer(button);


        } else {
            processWrongAnswer(button);
        }


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.button1:
                processTheAnswer(button1, relativeLayout);
                break;
            case R.id.button2:
                processTheAnswer(button2, relativeLayout);
                break;
            case R.id.button3:
                processTheAnswer(button3, relativeLayout);
                break;
            case R.id.button4:
                processTheAnswer(button4, relativeLayout);
                break;
            case R.id.buttonRestartGame:
                onCreate(savedInstanceState);
                break;
            case R.id.buttonQuitGame:
                onBackPressed();
                break;

            default:
                break;

        }
    }


    private void init(List<Integer> list) {
        if (list.size() > 0) {
            list.clear();
        }
        for (int i = 0; i < pictures.size(); i++) {
            list.add(i, i + 1);
        }
    }

    private Integer getRandomValue(List<Integer> list) {

        Integer randInt = new Random().nextInt(list.size());

        Integer retValue = list.get(randInt);
        list.remove(list.indexOf(retValue));
        return retValue;


    }

    private void loadMainPicture(Integer num) {
        int res = getResources()
                .getIdentifier((String) pictures.get((num).toString()), "drawable", "com.guesscity.guessthecity");

        relativeLayout.setBackgroundResource(res);
        relativeLayout.setTag(num);
    }

    private void buttonsInitialization() {

        Integer val1 = keyOfActivePicture;
        Integer val2 = 1 + new Random().nextInt(pictures_name.size());
        Integer val3 = 1 + new Random().nextInt(pictures_name.size());
        Integer val4 = 1 + new Random().nextInt(pictures_name.size());


        List<Integer> keyListOfPictures = new ArrayList<Integer>();
        keyListOfPictures.add(val1);
        keyListOfPictures.add(val2);
        keyListOfPictures.add(val3);
        keyListOfPictures.add(val4);
        Collections.shuffle(keyListOfPictures);


        button1.setText(pictures_name.get(keyListOfPictures.get(0).toString()).toString());
        button2.setText(pictures_name.get(keyListOfPictures.get(1).toString()).toString());
        button3.setText(pictures_name.get(keyListOfPictures.get(2).toString()).toString());
        button4.setText(pictures_name.get(keyListOfPictures.get(3).toString()).toString());
        button1.setTag(keyListOfPictures.get(0));
        button2.setTag(keyListOfPictures.get(1));
        button3.setTag(keyListOfPictures.get(2));
        button4.setTag(keyListOfPictures.get(3));

        initDefaultButton(button1);
        initDefaultButton(button2);
        initDefaultButton(button3);
        initDefaultButton(button4);

        linearLayoutEndGame.setVisibility(View.INVISIBLE);


    }

    private void initDefaultButton(Button button) {
        button.setClickable(true);
        button.setBackgroundColor(Color.DKGRAY);
    }

}
