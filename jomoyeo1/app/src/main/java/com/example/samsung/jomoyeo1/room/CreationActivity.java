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
 * 방 생성 화면
 */

public class CreationActivity extends Activity {

    Context mContext = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

    }

    /* back 버튼 눌렀을 때 이전화면(메인)으로 돌아가기 */
    @Override public void onBackPressed() {
        Intent backIntent = new Intent(mContext, MainActivity.class);
        startActivity(backIntent);
        finish();
    }
}
