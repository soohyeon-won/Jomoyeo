package com.example.samsung.jomoyeo1.main;

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
        colorPalette.add("#6799FF");
        colorPalette.add("#FAEBFF");
        colorPalette.add("#BCE55C");
        colorPalette.add("#FFA7A7");
        colorPalette.add("#FFC19E");
        colorPalette.add("#FFE08C");
        colorPalette.add("#DAD9FF");
        colorPalette.add("#FFD9FA");
        colorPalette.add("#F29661");
        colorPalette.add("#F2CB61");
        colorPalette.add("#E5D85C");
        colorPalette.add("#CEF279");
        colorPalette.add("#B7F0B1");
        colorPalette.add("#B2EBF4");
        colorPalette.add("#B2CCFF");
    }

    /* 추가할 때 색을 변화 시키는 메소드. */
    public String randomColor(){
        Log.d("출력", picker+". ");
        Log.d("출력", ""+colorPalette.size());

        picker = (int)(Math.random()*colorPalette.size())+0;
        Log.d("picker : ", picker+"");
        String color = colorPalette.get(picker);
        return color;
    }

}
