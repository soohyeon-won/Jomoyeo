package com.example.samsung.jomoyeo1.dbconnection;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.samsung.jomoyeo1.login.LoginActivity;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    //main으로 넘어가는 delay time
    public static final int SPLASH_TIME = 2000;
    //임의로 permission을 구분하는 번호 (return 값) 크게 상관은 없음
    private final int MY_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        checkPermissionAndStartMain();
        startMain();
    }


    //안드로이드 M 이상 퍼미션 체크 M이하에선 안돔
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionAndStartMain() {
        boolean startFlag = true;
        ArrayList<String> permissionList = new ArrayList<String>();

        //권한을 사용할 수 없을 때.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //사용할 수 없는걸 넣음
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            startFlag = false;
        }

        if (startFlag == true) startMain();

        if (permissionList.size() > 0) {
            String permissionArray[] = permissionList.toArray(new String[permissionList.size()]);
            requestPermissions(permissionArray, MY_PERMISSION_REQUEST);
        }
    }

    //메인진입관련 메소드
    private void startMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME);
    }
}