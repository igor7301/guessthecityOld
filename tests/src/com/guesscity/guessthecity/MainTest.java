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
public class MainTest extends ActivityInstrumentationTestCase2<Main> {

    private Activity mainAct;
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public MainTest() {
        super("com.guesscity.guessthecity", Main.class);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainAct = this.getActivity();

    }


    public void testMy( ){
        Map<String, String> city_pic = (HashMap < String, String >) ResourceUtils
                .getHashMapResource(mainAct.getApplicationContext(), R.xml.city_pictures);
        Map<String, String> city_names = (HashMap < String, String >) ResourceUtils
                .getHashMapResource(mainAct.getApplicationContext(), R.xml.city_names);

        for (String s : city_pic.keySet()) {

            assertTrue("Picture with key: '" + s +
                    "' doesn't exist in pictures_name map - " + city_names.toString(), city_names.containsKey(s));
        }
    }

    public void testNoPictureWithSameNames() {

    }

}
