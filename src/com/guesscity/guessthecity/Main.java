package com.guesscity.guessthecity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.*;

import static java.lang.Thread.sleep;

public class Main extends Activity implements View.OnClickListener {
    private ImageView imageView;
    private Button button1, button2, button3, button4;
    private HashMap pictures;
    private HashMap pictures_name;
    private Integer keyOfActivePicture;
    private List<Integer> remainderPictures = new ArrayList<Integer>();


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imageView = (ImageView) findViewById(R.id.imageView);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);


        pictures = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.city_pictures);
        pictures_name = (HashMap<String, String>) ResourceUtils.getHashMapResource(this, R.xml.city_names);

        init(remainderPictures);
        keyOfActivePicture = getRandomValue(remainderPictures);

        loadMainPicture(keyOfActivePicture);
        buttonsInitialization();
        processRightAnswer(button1);


    }

    private void processEndGame() {
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
            Toast.makeText(this, "End game", Toast.LENGTH_SHORT).show();

    }

    private void processRightAnswer(Button button) {
            Toast.makeText(this, "RIGHT!!!!!", Toast.LENGTH_SHORT).show();
              button.setBackgroundColor(Color.GREEN);
             freeze((int) Integer.MAX_VALUE);
              button.setBackgroundColor(Color.RED);




    }

    private void freeze(int delay){
        for (int i = 0; i<delay; i++){

        }

    }

    private void goToNextQuestion() {
            keyOfActivePicture = getRandomValue(remainderPictures);
            loadMainPicture(keyOfActivePicture);
            buttonsInitialization();

    }

    private Boolean lastQuestion() {
       return remainderPictures.size() > 0 ? false : true;
    }

    private void processWrongAnswer() {
            Toast.makeText(this, "WRONG!!!!!", Toast.LENGTH_SHORT).show();

    }

    private void processTheAnswer(Button button, ImageView imageView) {


            if ((Integer) button.getTag() == (Integer) imageView.getTag()) {
                processRightAnswer(button);

                if (!lastQuestion()) {
                    goToNextQuestion();
                }
                else {
                    processEndGame();
                }
            }
            else {
                processWrongAnswer();
            }



    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.button1:
                processTheAnswer(button1, imageView);
                break;
            case R.id.button2:
                processTheAnswer(button2, imageView);
                break;
            case R.id.button3:
                processTheAnswer(button3, imageView);
                break;
            case R.id.button4:
                processTheAnswer(button4, imageView);
                break;
            default:
                break;

        }
    }


    private void init(List<Integer> list) {
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
        imageView.setImageResource(res);
        imageView.setTag(num);
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

    }

}
