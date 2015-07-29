package com.guesscity.guessthecity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.guesscity.guessthecity.MainTest \
 * com.guesscity.guessthecity.tests/android.test.InstrumentationTestRunner
 */
public class MainTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity mainAct;
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public MainTest() {
        super("com.guesscity.guessthecity", MainActivity.class);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainAct = this.getActivity();

    }


    public void testMy( ){
        Map<String, String> city_pic = (HashMap < String, String >) ResourceUtils
                .getHashMapResource(mainAct.getApplicationContext(), R.xml.pictures);
        Map<String, String> city_names = (HashMap < String, String >) ResourceUtils
                .getHashMapResource(mainAct.getApplicationContext(), R.xml.city_names);

        for (String s : city_pic.keySet()) {

            assertTrue("Picture with key: '" + s +
                    "' doesn't exist in pictures map - " + city_names.toString(), city_names.containsKey(s));
        }
    }

    public void testMy2( ){
        Map<String, String> city_pic = (HashMap < String, String >) ResourceUtils
                .getHashMapResource(mainAct.getApplicationContext(), R.xml.pictures);
        Map<String, String> country_name = (HashMap < String, String >) ResourceUtils
                .getHashMapResource(mainAct.getApplicationContext(), R.xml.country_names);

        for (String s : city_pic.keySet()) {

            assertTrue("Picture with key: '" + s +
                    "' doesn't exist in pictures map - " + country_name.toString(), country_name.containsKey(s));
        }
    }

    public void testNoPicturesWithSameNumbers( ){
        Map<String, String> city_pic = (HashMap < String, String >) ResourceUtils
                .getHashMapResource(mainAct.getApplicationContext(), R.xml.pictures);


//        for (String s : city_pic.keySet()) {
//
//            assertTrue("Picture with key: '" + s +
//                    "' doesn't exist in pictures map - " + country_name.toString(), country_name.containsKey(s));
//        }
    }


    public void testNoPictureWithSameNames() {

    }

}
