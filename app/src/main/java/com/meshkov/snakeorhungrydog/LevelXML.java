package com.meshkov.snakeorhungrydog;

import android.content.Context;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LevelXML {
    private static Level level;
    private static String text;

    private static XmlPullParser prepareXpp(Context context) {
        return context.getResources().getXml(R.xml.levels);
    }

    public static Level getLevelParamsFromXML(Context context, int level_num) {
        level = new Level();

        try {
            XmlPullParser xpp = prepareXpp(context);
            boolean thisLevel = false;

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                String xppName = xpp.getName();
                int xppEventType = xpp.getEventType();
                int a = 1;
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals("level_" + level_num)) {
                            thisLevel = true;
                        }
                        String tmp = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = "
                                    + xpp.getAttributeValue(i) + ", ";
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("level_" + level_num)) {
                            thisLevel = false;
                        }
                        if (thisLevel && xpp.getName().equals("need_level_score")) {
                            level.setNeed_level_score(Integer.valueOf((text)));
                        }
                        if (thisLevel && xpp.getName().equals("number")) {
                            level.setNumber(Integer.parseInt(text));
                        }
                        if (thisLevel && xpp.getName().equals("speed")) {
                            level.setSpeed(Float.valueOf((text)));
                        }
                        if (thisLevel && xpp.getName().equals("color")) {
                            level.setColor(text);
                        }
                        if (thisLevel && xpp.getName().equals("life_of_bone_in_seconds")) {
                            level.setLife_of_bone_in_seconds(Integer.parseInt(text));
                        }
                        if (thisLevel && xpp.getName().equals("life_of_heart_in_seconds")) {
                            level.setLife_of_heart_in_seconds(Integer.parseInt(text));
                        }
                        if (thisLevel && xpp.getName().equals("life_of_enemy_in_seconds")) {
                            level.setLife_of_enemy_in_seconds(Integer.parseInt(text));
                        }
                        if (thisLevel && xpp.getName().equals("bone_not_active_in_seconds")) {
                            level.setBone_not_active_in_seconds(Integer.parseInt(text));
                        }
                        if (thisLevel && xpp.getName().equals("heart_not_active_in_seconds")) {
                            level.setHeart_not_active_in_seconds(Integer.parseInt(text));
                        }
                        if (thisLevel && xpp.getName().equals("enemy_not_active_in_seconds")) {
                            level.setEnemy_not_active_in_seconds(Integer.parseInt(text));
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    default:
                        break;
                }
                xpp.next();

            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return level;
    }
}
