package com.guesscity.guessthecity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageView imageViewHint;
    private ImageView imageViewMainPicture;
    private SharedPreferences mSettings;
    private TextView textViewProgress;
    private RelativeLayout relativeLayout;
    private RelativeLayout relativeLayoutTopBar;
    private LinearLayout linearLayoutEndGame;
    private LinearLayout linearLayoutAnswerButtons;
    private TextView textViewEndGameMessage;
    private RatingBar livesWidget;
    private Button btnRestartGame, btnQuitGame;
    private Button button1, button2, button3, button4;
    private HashMap pictures;
    private HashMap pictures_name;
    private HashMap pictures_hints;
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

        livesWidget = (RatingBar) findViewById(R.id.ratingBar);
        relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        relativeLayoutTopBar = (RelativeLayout) findViewById(R.id.relativeLayoutTopBar);
        linearLayoutAnswerButtons = (LinearLayout) findViewById(R.id.linearLayoutAnswerButtons);
        linearLayoutEndGame = (LinearLayout) findViewById(R.id.linearLayoutEndGame);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        btnRestartGame = (Button) findViewById(R.id.buttonRestartGame);
        btnQuitGame = (Button) findViewById(R.id.buttonQuitGame);
        textViewEndGameMessage = (TextView) findViewById(R.id.textViewEndGameMessage);
        textViewProgress = (TextView) findViewById(R.id.textViewProgress);
        imageViewHint = (ImageView) findViewById(R.id.imageViewHint);
        imageViewMainPicture = (ImageView) findViewById(R.id.imageViewMainPicture);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        btnRestartGame.setOnClickListener(this);
        btnQuitGame.setOnClickListener(this);
        imageViewHint.setOnClickListener(this);


        pictures = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.pictures);

        mSettings = getSharedPreferences(getResources().getString(R.string.APP_PREFERENCES).toString(), Context.MODE_PRIVATE);

        Boolean city = mSettings.getBoolean(getResources().getString(R.string.APP_PREFERENCES_CITY_GAME).toString(), true);;

//        if (mSettings.contains(getResources().getString(R.string.APP_PREFERENCES_CITY_GAME).toString()))

        if (city) {
            pictures_name = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.city_names);
            pictures_hints = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.city_hints);
        } else {
            pictures_name = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.country_names);
            pictures_hints = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.country_hints);
        }



        updateLives(LIVES);
        init(remainderPictures);
        keyOfActivePicture = getUniqueRandomValue(remainderPictures);

        loadMainPicture(keyOfActivePicture);
        buttonsInitialization();

        updateProgress();


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
        textViewProgress.setText(getNumberOfActiveQuestion() + "/" + getAmountOfAllQuestions());
    }

    private void processEndGame() {

        String message;
        linearLayoutAnswerButtons.setVisibility(View.INVISIBLE);
        relativeLayoutTopBar.setAlpha((float) 0.5);




        textViewEndGameMessage.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/LeagueGothicRegular.otf"));
        textViewEndGameMessage.setTextSize(40);
        if (currentLives > 0) {
            message = "CONGRATULATIONS!\nYOU WON";

            textViewEndGameMessage.setTextColor(getResources().getColor(R.color.successEndGame));



        } else {
            message = "YOU LOSE!\nTRY AGAIN";
            textViewEndGameMessage.setTextColor(getResources().getColor(R.color.unsuccessEndGame));

        }
        textViewEndGameMessage.setGravity(Gravity.CENTER);
        textViewEndGameMessage.setText(message);
        linearLayoutEndGame.setVisibility(View.VISIBLE);

    }

    private Integer getNumberOfActiveQuestion() {
        return (getAmountOfAllQuestions() - getAmountOfRemainderQuestions());
    }


    private void goToNextQuestion() {
        keyOfActivePicture = getUniqueRandomValue(remainderPictures);
        loadMainPicture(keyOfActivePicture);
        buttonsInitialization();
        updateProgress();

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

        setAllButtonsClickable(false);
        processRightButton(button);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!lastQuestion() && currentLives != 0) {


                    goToNextQuestion();

                } else {

                    processEndGame();
                }
            }
        }, 1000);


    }


    private void processWrongAnswer(Button button) {

        setAllButtonsClickable(false);
        currentLives--;
        updateLives(currentLives);
        processWrongButton(button);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processRightAnswer(getCorrectAnswerButton());
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

    public ImageView getImageViewMainPicture() {
        return imageViewMainPicture;
    }

    private Button getCorrectAnswerButton() {
        if ((Integer) button1.getTag() == (Integer) getImageViewMainPicture().getTag()) {

            return button1;
        } else if ((Integer) button2.getTag() == (Integer) getImageViewMainPicture().getTag())

        {
            return button2;
        } else if ((Integer) button3.getTag() == (Integer) getImageViewMainPicture().getTag())

        {
            return button3;
        } else if ((Integer) button4.getTag() == (Integer) getImageViewMainPicture().getTag())

        {
            return button4;
        }
        return null;


    }

    private void processTheAnswer(Button button) {


        if ((Integer) button.getTag() == (Integer) getImageViewMainPicture().getTag()) {
            processRightAnswer(button);


        } else {
            processWrongAnswer(button);
        }


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.button1:
                processTheAnswer(button1);
                break;
            case R.id.button2:
                processTheAnswer(button2);
                break;
            case R.id.button3:
                processTheAnswer(button3);
                break;
            case R.id.button4:
                processTheAnswer(button4);
                break;
            case R.id.buttonRestartGame:
                onCreate(savedInstanceState);
                break;
            case R.id.buttonQuitGame:
                onBackPressed();
                break;
            case R.id.imageViewHint:
                processHint();
                break;

            default:
                break;

        }
    }

    private void processHint() {

        Toast toast = Toast.makeText(this,  getHint(keyOfActivePicture), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    private String getHint(Integer key) {
        return (String) pictures_hints.get(key.toString());
    }


    private void init(List<Integer> list) {
        if (list.size() > 0) {
            list.clear();
        }
        for (int i = 0; i < pictures.size(); i++) {
            list.add(i, i + 1);
        }
    }

    private <T> T getUniqueRandomValue(List<T> list) {

        Integer randInt = new Random().nextInt(list.size());

        T retValue = list.get(randInt);
        list.remove(list.indexOf(retValue));
        return retValue;
    }

    private void loadMainPicture(Integer num) {
        int res = getResources()

                .getIdentifier((String) pictures.get((num).toString()), "drawable", "com.guesscity.guessthecity");

        imageViewMainPicture.setImageBitmap(decodeSampledBitmapFromResource(getResources(), res,
                480, 480));

        imageViewMainPicture.setTag(num);

    }


    private void buttonsInitialization() {



        List<Integer> keyOfPicture = new ArrayList<Integer>();
        init(keyOfPicture);

        List<Integer> keyOfPictures = new ArrayList<Integer>();
        keyOfPictures.add(keyOfActivePicture);
        removeValue(keyOfPicture, keyOfActivePicture);
        keyOfPictures.add(getUniqueRandomValue(keyOfPicture));
        keyOfPictures.add(getUniqueRandomValue(keyOfPicture));
        keyOfPictures.add(getUniqueRandomValue(keyOfPicture));
        Collections.shuffle(keyOfPictures);


        button1.setText(pictures_name.get(keyOfPictures.get(0).toString()).toString());
        button2.setText(pictures_name.get(keyOfPictures.get(1).toString()).toString());
        button3.setText(pictures_name.get(keyOfPictures.get(2).toString()).toString());
        button4.setText(pictures_name.get(keyOfPictures.get(3).toString()).toString());

        button1.setTag(keyOfPictures.get(0));
        button2.setTag(keyOfPictures.get(1));
        button3.setTag(keyOfPictures.get(2));
        button4.setTag(keyOfPictures.get(3));

        initDefaultButton(button1);
        initDefaultButton(button2);
        initDefaultButton(button3);
        initDefaultButton(button4);

        linearLayoutEndGame.setVisibility(View.INVISIBLE);


    }

    private <T> void removeValue(List<T> list, T value) {
       list.remove(list.indexOf(value));
    }

    private void initDefaultButton(Button button) {
        button.setClickable(true);
        button.setBackgroundResource(R.drawable.default_button);
    }

}
