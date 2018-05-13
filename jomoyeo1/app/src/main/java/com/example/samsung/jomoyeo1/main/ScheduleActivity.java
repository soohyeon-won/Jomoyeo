package com.example.samsung.jomoyeo1.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.preference.UserPreference;

import java.util.ArrayList;

/*
 * 스케쥴을 등록하는 부분
 * DB
 */

public class ScheduleActivity extends Activity {

    Context mContext = this;
    private Dialog mDialog = null;

    /* 추가 하기를 눌렀을 때 */
    private Button popupAddButton;  // 팝업창 추가
    private Button popAddButtonv2;
    private Button popupBackButton; // 뒤로 가기
    private Button popAddSTTButton; // STT로 스케쥴 추가 버튼
    public String scheduleTitle;    // 제목
    public String scheduleContent;  // 내용
    ScheduleMatrix scheduleMatrix;   // 시간표

    //TSS
    Intent i;
    SpeechRecognizer mRecognizer;
    ArrayList<String> mResult;
    String[] rs;
    EditText title;

    boolean addDelete = true;

    ColorPalette colorPalette;
    String color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        /* ScheduleMatrix 객체 생성 */
        scheduleMatrix = new ScheduleMatrix(mContext);

        //textView 저장 되어 있는 배열 생성
        scheduleMatrix.makeScheduleArray();
        colorPalette = new ColorPalette();

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);

    }

    /* 각 버튼 클릭시 수행 */
    public void onClick(View view){
        switch(view.getId()){

            case R.id.addButton :
                //textView 저장 되어 있는 배열 생성
                addPopupWindow();
                break;

            case R.id.removeButton :
                removePopupWindow();
                break;
        }
    }

    /* textView 눌렀을때 수행 */
    public void scheduleClick(View view){
        if(addDelete == true) {
            scheduleMatrix.scheduleClickHelper(view, color);
        }
        else{
            scheduleMatrix.removeClickHelper(view);
        }
    }

    /* 추가하기 */
    private void addPopupWindow() {
        final View innerView = getLayoutInflater().inflate(R.layout.popup_add_schedule, null);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(innerView);
        setPopupSize(mDialog);
                                                   // Back키 눌렀을 경우 Dialog Cancle 여부 설정
        mDialog.getWindow().setGravity(Gravity.BOTTOM);                                     //Dialog 위치 이동
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);        // Dialog 생성시 배경화면 어둡게 하지 않기
        title = (EditText) innerView.findViewById(R.id.scheduleTitle);

        /* 팝업창에서 추가 버튼을 눌렀을 때 */
        popupAddButton = (Button) innerView.findViewById(R.id.popAddButton);
        popupAddButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                scheduleTitle = String.valueOf(title.getText()).trim();
                if (scheduleTitle.getBytes().length <= 0){           //비어있으면
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_LONG).show();
            }
            else{
                color = colorPalette.randomColor();
                dismissDialog();
                addPopupWindow_v2();
            }
            }
        });

        /* STT로 스케쥴 추가하는 버튼 */
        popAddSTTButton = (Button)innerView.findViewById(R.id.popAddSTTButton);
        popAddSTTButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(i);  //리스너 부르기.
                title.getText();
                Log.d("확인", title.getText().toString());
//                title.setText(rs[0]);
            }
        });

        /* 취소하기 */
        popupBackButton = (Button) innerView.findViewById(R.id.popBackButton);
        popupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        mDialog.show();
    }


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {
            String key="";
            Log.d("확인", "확인");
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            mResult = results.getStringArrayList(key);
            rs = new String[mResult.size()];
            mResult.toArray(rs);
            Log.d("확인", "확인");
            Log.d("확인 rs[0]", rs[0]);
            mRecognizer.startListening(i);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    /* 추가하기_v2 팝업 */
    private void addPopupWindow_v2() {
        final View innerView = getLayoutInflater().inflate(R.layout.popup_add_schedule_v2, null);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(innerView);
        setPopupSize(mDialog);  //팝업 사이즈 설정

        mDialog.setCanceledOnTouchOutside(false);                                             // Dialog 밖을 터치 했을 경우 Dialog 사라지게 하기
        mDialog.getWindow().setGravity(Gravity.BOTTOM);                                       //Dialog 위치 이동
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);          // Dialog 생성시 배경화면 어둡게 하지 않기
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL); //뒤 클릭 가능

        addDelete = true;
        scheduleMatrix.setInitialFlag(true);

        /* 추가하기 (DB 저장) */
        popAddButtonv2 = (Button) innerView.findViewById(R.id.popAddButtonv2);
        popAddButtonv2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                scheduleMatrix.scheduleAdd(scheduleTitle);  //스케줄 제목 셋팅
                String id = UserPreference.getInstance().getId(mContext);
                Log.d("출력1", scheduleTitle);
                scheduleMatrix.scheduleAddDB(id, scheduleTitle);
                dismissDialog();
            }
        });

        /* 취소하기 */
        popupBackButton = (Button) innerView.findViewById(R.id.popBackButton);
        popupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleMatrix.scheduleCancelClick();
                dismissDialog();
            }
        });
        mDialog.show();
    }

    /* 삭제하기 팝업 */
    private void removePopupWindow() {
        final View innerView = getLayoutInflater().inflate(R.layout.popup_remove_schedule, null);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(innerView);
        setPopupSize(mDialog);

        mDialog.setCanceledOnTouchOutside(false);                                            // Dialog 밖을 터치 했을 경우 Dialog 사라지게 하기
        mDialog.getWindow().setGravity(Gravity.BOTTOM);                                     //Dialog 위치 이동
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);        // Dialog 생성시 배경화면 어둡게 하지 않기
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL); //뒤 클릭 가능
        addDelete = false;
        scheduleMatrix.setInitialFlag(true);

        /* 완료 */
        Button popCompliteButton = (Button)innerView.findViewById(R.id.popCompliteButton);
        popCompliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        mDialog.show();
    }

    /* 다이얼로그 종료 */
    private void dismissDialog() {
        if(mDialog != null && mDialog.isShowing()) {
            Log.d("listXY", scheduleMatrix.getListXY());
            scheduleMatrix.listXYClear();
            scheduleMatrix.setInitialFlag(false);
            mDialog.dismiss();
        }
    }

    /* 뒷 배경 터치 가능하게 하는 메소드 */
    public boolean dispatchTouchEvent(MotionEvent ev){
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);
        if(!dialogBounds.contains((int)ev.getX(), (int)ev.getY())){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }


    /* 팝업 크기 설정 */
    private void setPopupSize(Dialog mDialog){
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    /* back 버튼 눌렀을 때 이전화면(메인)으로 돌아가기 */
    @Override public void onBackPressed() {
        Intent backIntent = new Intent(mContext, MainActivity.class);
        startActivity(backIntent);
        finish();
    }

}
