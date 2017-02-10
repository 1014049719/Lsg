package com.talenton.lsg.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Random;

/**
 * @author zjh
 * @date 2016/4/29
 */
public class RandomUtils {
    private int lastColorPosition = -1; //让相邻2个颜色不同
    private final int MAX_TRY = 10;
    private int tryCount;

    private static int[] colors = new int[]{
            Color.parseColor("#3983cc"),
            Color.parseColor("#f34e4a"), //红
            Color.parseColor("#3fbb65"),//绿
            Color.parseColor("#f2c400"),//黄
            Color.parseColor("#55acef"),
            Color.parseColor("#ff8434"),
            Color.parseColor("#ff8434"),
    };

    public int randomColor(){
        int color = colors[randomPositon()];
        tryCount = 0;
        return color;
    }

    private int randomPositon(){
        int colorPosition = 0;
        Random random = new Random();
        int number = random.nextInt(100);
        if (number <= 39){
            colorPosition = 0;
        }else if (number <= 52){
            colorPosition = 1;
        }else if(number <= 65){
            colorPosition = 2;
        }else if (number <= 79){
            colorPosition = 3;
        }else if (number <= 87){
            colorPosition = 4;
        }else if(number <= 93){
            colorPosition = 5;
        }else {
            colorPosition = 6;
        }

        if (lastColorPosition == colorPosition && tryCount <= MAX_TRY){
            tryCount ++;
            colorPosition = randomPositon();
        }
        lastColorPosition = colorPosition;

        return colorPosition;
    }

    public GradientDrawable randomGradientDrawable(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int roundRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm); // 圆角半径
        int fillColor = randomColor();//内部填充颜色

        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);

        return gd;
    }
}
