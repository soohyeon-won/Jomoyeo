package com.example.samsung.jomoyeo.schedule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.example.samsung.jomoyeo.R;

import java.util.ArrayList;
import java.util.Collections;

public class CombineSchedule extends Activity {

    Context mContext = this;
    String AttendUserNameString;
    ArrayList<String> userName;
    ScheduleMatrix scheduleMatrix;

    /* 중복 유저 관리하는 필드 */
    int viewCount = 0;
    ArrayList<String> tvUser;
    private Dialog mDialog = null;
    TextView popupTvUserList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_combine);
        Intent intent = getIntent();
        AttendUserNameString = intent.getStringExtra("AttendUserNameString");

        //list로 하나하나 쪼개기
        userName = new ArrayList<String>();
        String[] userNameArray = AttendUserNameString.split("\n");
        Collections.addAll(userName, userNameArray);

        /* 스케쥴 뷰생성 */
        scheduleMatrix = new ScheduleMatrix(mContext);
        scheduleMatrix.makeCombineScheduleArray();
        addSchedule();

        //textView 눌렀을 때 비어있지 않으면 팝업 띄워서 누구의 스케쥴인지 보여주기.
        tvUser = scheduleMatrix.getTvUser();

//        tvUserPrint();

    }

    boolean isOpenPopup = false;
    public void getTvUserList(View view){
        boolean emptySpace=false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scheduleMatrix.scheduleTextView.length; i++) {
            for (int j = 0; j < scheduleMatrix.scheduleTextView[i].length; j++) {
                if (view.getId() == scheduleMatrix.scheduleTextView[i][j].getId()) {
                    //빈 공간인지 확인

                    if(scheduleMatrix.scheduleTextView[i][j].getText().equals("")) {
                        //빈공간
                        if(isOpenPopup==false) {
                            Toast.makeText(mContext, "빈 공간입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{//팝업이 열려있으면
                            dismissDialog();
                            isOpenPopup = false;
                        }
                    }
                    else{
                        if(isOpenPopup == true){
                            dismissDialog();
                            isOpenPopup = false;
                        }
                        for(int k = 0 ; k < tvUser.size(); k++) {
                            String a = tvUser.get(k);
                            String[] arr = a.split("#");
                            if(arr[0].equals(String.valueOf(i)) && arr[1].equals(String.valueOf(j))){
                                sb.append(arr[2]);
                                sb.append("\n");
                                isOpenPopup = true;
                            }
                        }
                        sb.deleteCharAt(sb.length()-1);
                        popupUserList(sb.toString());
                    }
                    }
                }
            }
        }

    /* user 목록 확인 팝업 */
    private void popupUserList(String a) {
        final View innerView = getLayoutInflater().inflate(R.layout.popup_view_schedule_users, null);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(innerView);
        setPopupSize(mDialog);

        popupTvUserList = (TextView)innerView.findViewById(R.id.popupTvUserList);
        popupTvUserList.setText(a);
        mDialog.setCanceledOnTouchOutside(false);                                            // Dialog 밖을 터치 했을 경우 Dialog 사라지게 하기
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);        // Dialog 생성시 배경화면 어둡게 하지 않기
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL); //뒤 클릭 가능
        mDialog.show();
    }

    /* 다이얼로그 종료 */
    private void dismissDialog() {
            mDialog.dismiss();
    }
    /* 팝업 크기 설정 */
    private void setPopupSize(Dialog mDialog){
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    public void tvUserPrint(){
        for(int i = 0 ; i <tvUser.size(); i++){
            Log.d("체크 tvUser ",tvUser.get(i));
        }
    }

    public void addSchedule(){
        //포문 돌려서 이름 하나당 데이터 받고 스케쥴에 추가하기
        for(int i = 0 ; i < userName.size(); i++)
        {
            Log.d("확인 size ", userName.size()+"");
            Log.d("확인 : " , userName.get(i));
            scheduleMatrix.combineSchedule(userName.get(i));
        }
    }

    public void upViewCount(){
        viewCount++;
    }

    public void downViewCount(){
        viewCount--;
    }
}
