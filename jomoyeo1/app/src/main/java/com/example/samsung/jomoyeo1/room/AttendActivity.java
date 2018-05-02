package com.example.samsung.jomoyeo1.room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.main.MainActivity;

/*
 *
 * 2018-03-27
 * 방 참가 화면
 * 참가, 검색(SearchActivity)으로 이동, 생성(CreationActivity)으로 이동 구현함
 */

public class AttendActivity  extends Activity {

    Context mContext = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);
    }

    /* back 버튼 눌렀을 때 이전화면(메인)으로 돌아가기 */
    @Override public void onBackPressed() {
        Intent backIntent = new Intent(mContext, MainActivity.class);
        startActivity(backIntent);
        finish();
    }
}
