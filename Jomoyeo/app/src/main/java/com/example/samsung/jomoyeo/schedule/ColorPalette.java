package com.example.samsung.jomoyeo.schedule;

import android.util.Log;

import java.util.ArrayList;

public class ColorPalette {

    int picker;
    public ArrayList<String> colorPalette;

    ColorPalette(){
        picker=0;
        colorPalette = new ArrayList<String>();
        settingColorPalette();
    }

    public void settingColorPalette(){
        colorPalette.clear();
        colorPalette.add("#FFA7A7");
        colorPalette.add("#FFC19E");
        colorPalette.add("#FFE08C");
        colorPalette.add("#CEF279");
        colorPalette.add("#B7F0B1");
        colorPalette.add("#B2EBF4");
        colorPalette.add("#B2CCFF");
        colorPalette.add("#B5B2FF");
        colorPalette.add("#D1B2FF");
        colorPalette.add("#FFB2F5");
        colorPalette.add("#FFB2D9");
        colorPalette.add("#FFD9EC");
        colorPalette.add("#E8D9FF");
        colorPalette.add("#DAD9FF");
        colorPalette.add("#D9E5FF");
        colorPalette.add("#D4F4FA");
        colorPalette.add("#CEFBC9");
        colorPalette.add("#E4F7BA");
        colorPalette.add("#FAF4C0");
        colorPalette.add("#FAECC5");
        colorPalette.add("#FAE0D4");
        colorPalette.add("#FFD8D8");
//        colorPalette.add("");
//        colorPalette.add("");
    }

    /* 추가할 때 색을 변화 시키는 메소드. */
    public String randomColor(){
        Log.d("확인1", ""+colorPalette.size());

        picker = (int)(Math.random()*colorPalette.size())+0;
        Log.d("확인2", picker+"");
        String color = colorPalette.get(picker);
        return color;
    }

}
