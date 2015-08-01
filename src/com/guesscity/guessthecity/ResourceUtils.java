package com.guesscity.guessthecity;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Igor on 18.07.15.
 */
public class ResourceUtils {
    public static Map<String, String> getHashMapResource(Context c, int hashMapResId) {
        return getHashMapResource(c, hashMapResId, null);
    }

    public static Map<String, String> getHashMapResource(Context c, int hashMapResId, String level) {
        Map<String, String> map = null;
        XmlResourceParser parser = c.getResources().getXml(hashMapResId);

        String key = null;
        String value = null;


        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("utils", "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("map")) {
                        boolean isLinked = parser.getAttributeBooleanValue(null, "linked", false);

                        map = isLinked ? new LinkedHashMap<String, String>() : new HashMap<String, String>();
                    } else if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {

                        if (level != null) {

                            if (value.contains(level)) {
                                map.put(key, value);
                            }
                            else {
//                                throw new RuntimeException("Please check you level names." +
//                                        " The level with " + level + " name wasn't found");
                            }
                        } else {
                            map.put(key, value);
                        }

                        key = null;
                        value = null;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        parser.getText();
                        // getResources().

                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }
}
