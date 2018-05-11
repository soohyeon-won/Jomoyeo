package com.example.samsung.jomoyeo1.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.dbconnection.HttpRequestResult;
import com.example.samsung.jomoyeo1.dbconnection.ServerService;
import com.example.samsung.jomoyeo1.preference.UserPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
* 시간표 textView를 생성하는 클래스
* */
public class ScheduleMatrix {

    private Context mContext;
    private String id;
    final static int x = 12;                //행
    final static int y = 7;                 //열
    public TextView[][] scheduleTextView; //시간표
    private ArrayList<String> listXY;       //스케쥴 list
    private String singleXY;                //스케쥴 String

    private boolean initialFlag;           //스케쥴 초기화 flag
    private boolean colorChangeFlag = true;//색상 정보 flag

    HttpRequestResult requestResult;
    JSONArray jsonArray;                     //저장된 스케쥴 불러올 jsonArray

    ColorPalette colorPalette;
    String color;

    int viewCount=1;
    ArrayList<String> tvUser;

    /* 생성자 */
    public ScheduleMatrix(Context mContext){
        this.mContext = mContext;
        scheduleTextView = new TextView[x][y];
        listXY = new ArrayList<String>();
        initialFlag = false;
        id = UserPreference.getInstance().getId(mContext);
        colorPalette = new ColorPalette();
        tvUser = new ArrayList<String>();
    }

    /* textVIew로 구성된 2차원 배열 null값으로 초기화 */
    public void initialScheduleArray(){
        for(int i=0; i<scheduleTextView.length; i++){
            for(int j=0; j<scheduleTextView[i].length; j++){
                scheduleTextView[i][j] = null;
            }
        }
    }

    /* textVIew로 구성된 2차원 배열 생성 */
    public void makeScheduleArray(){
        for(int i=0; i<scheduleTextView.length; i++){
            for(int j=0; j<scheduleTextView[i].length; j++){
                int tvId = mContext.getResources().getIdentifier("s"+(i)+""+(j),"id",mContext.getPackageName());
                scheduleTextView[i][j] = ((Activity)mContext).findViewById(tvId);
            }
        }
        existentScheduleArray();
    }

    public void makeCombineScheduleArray(){
        for(int i=0; i<scheduleTextView.length; i++){
            for(int j=0; j<scheduleTextView[i].length; j++){
                int tvId = mContext.getResources().getIdentifier("s"+(i)+""+(j),"id",mContext.getPackageName());
                scheduleTextView[i][j] = ((Activity)mContext).findViewById(tvId);
            }
        }
    }

    /* DB에 저장된 스케쥴 목록 불러오기 */
    public void existentScheduleArray(){
        ServerService serverService = new ServerService(mContext);
        //DB에 존재하는지 검사
        requestResult = serverService.selectSchedule(id);
        if(requestResult.getResultCode() == 200){
            try {
                jsonArray = (JSONArray) requestResult.getResultJsonArray();
                for(int i = 0 ; i <jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("schedule_title");
                    int x = Integer.parseInt(jsonObject.getString("x"));
                    int y = Integer.parseInt(jsonObject.getString("y"));
                    String currColor = jsonObject.getString("color");
                    /* setText and colorChange */
                    (scheduleTextView[x][y]).setText(title);
                    (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor(currColor));
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void combineSchedule(String id){
        ServerService combine = new ServerService(mContext);
        //DB에 존재하는지 검사
        requestResult = combine.selectSchedule(id);
        if(requestResult.getResultCode() == 200){
            try {
                jsonArray = (JSONArray) requestResult.getResultJsonArray();
                for(int i = 0 ; i <jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("schedule_title");
                    int x = Integer.parseInt(jsonObject.getString("x"));
                    int y = Integer.parseInt(jsonObject.getString("y"));
                    String currColor = jsonObject.getString("color");
                    tvUser.add(x+"#"+y+"#"+id);
                    if(!scheduleTextView[x][y].getText().equals(""))
                    {
                        viewCount=Integer.parseInt(scheduleTextView[x][y].getText().toString());
                        viewCount++;
                    }
                    /* setText and colorChange */
                    (scheduleTextView[x][y]).setText(viewCount+"");

                    switch (viewCount){
                        case 1 : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#FAEBFF"));break;
                        case 2 : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#E8D9FF"));break;
                        case 3 : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#D1B2FF"));break;
                        case 4 : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#BFA0ED"));break;
                        case 5 : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#AD8EDB"));break;
                        case 6 : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#A566FF"));break;
                        case 7 : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#8041D9"));break;
                        default : (scheduleTextView[x][y]).setBackgroundColor(Color.parseColor("#2A0066"));break;
                    }
                    viewCount = 1;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getTvUser(){
        return tvUser;
    }

    /* 시간표에서 시간을 선택했을때 x,y 좌표를 받아옴 */
    public void scheduleClickHelper(View view , String color){
        this.color = color;
        if(initialFlag)
            for (int i = 0; i < scheduleTextView.length; i++) {
                for (int j = 0; j < scheduleTextView[i].length; j++) {
                    if (view.getId() == scheduleTextView[i][j].getId()) {
                        boolean overlap = overlapSchedule(i, j);
                        if (overlap==true){
                            singleXY = (i) + "," + (j);
                            colorChangeFlag = true;
                            /* list에 이미 존재하면 draw로 바꾸고 리스트에서 없애기. */
                            /* 클릭했을때 색 변화시키는 부분 / 더블 클릭하면 원래대로 */
                            for (String list : listXY) {
                                if (list.equals(singleXY)) {
                                  changeScheduleDraw(view);
                                  listXY.remove(singleXY);
                                  colorChangeFlag = false;
                                  break;
                            }
                        }
                        if (colorChangeFlag) {
                            listXY.add(singleXY);
                            changeScheduleColor(view);
                        }
                        Log.d("i,j : ", i + "," + j);
                    }
                    }
                }
            }
        }

    /* 중복된 시간인지 검사 */
    public boolean overlapSchedule(int i, int j){
            ServerService serverService = new ServerService(mContext);
            //DB에 존재하는지 검사
            requestResult = serverService.selectScheduleTitle(id, i+"", j+"");
            if(requestResult.getResultCode() != 200){   //존재안함.
                return true;
            }
            else {  //존재함.
                Toast.makeText(mContext, "이미 존재하는 스케쥴입니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        public boolean overlapCombineSchedule(int i, int j){
            ServerService serverService = new ServerService(mContext);
            //DB에 존재하는지 검사
            requestResult = serverService.selectScheduleTitle(id, i+"", j+"");
            if(requestResult.getResultCode() != 200){   //스케쥴이 존재안함
                return true;
            }
            else {                                      //스케쥴 존재함.
                return false;
            }
        }


    /* 삭제리스너 헬퍼 */
    public void removeClickHelper(View view){
        if(initialFlag)
            for (int i = 0; i < scheduleTextView.length; i++) {
                for (int j = 0; j < scheduleTextView[i].length; j++) {
                    if (view.getId() == scheduleTextView[i][j].getId()) {
                        ServerService serverService = new ServerService(mContext);
                        //삭제할 시간이 비어있는지 검사
                        requestResult = serverService.selectScheduleTitle(id, i+"", j+"");
                        //빈 공간이 아니면 삭제
                        if(requestResult.getResultCode() == 200){
                            ServerService deleteService = new ServerService(mContext);
                            changeScheduleDraw(view);
                            (scheduleTextView[i][j]).setText("");
                            deleteService.deleteSchedule(id, i+"", j+"");
                        }
                        //빈 공간이면 삭제 불가
                        else{
                            Toast.makeText(mContext, "삭제할 스케쥴이 없습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
    }

    /* 취소시 현재까지 클릭한 textView 원래대로 되돌림 */
    public void scheduleCancelClick(){
        if(!listXY.isEmpty()){
            for(String xy : listXY){
                String[] xyArr = new String[2];
                xyArr = xy.split(",");
                changeScheduleDraw(scheduleTextView[Integer.parseInt(xyArr[0])][Integer.parseInt(xyArr[1])]);
            }
        }
    }

    /* 추가시 현재까지 클릭한 textView의 text를 변경 */
    public void scheduleAdd(String title){
        if(!listXY.isEmpty()){
            for(String xy : listXY){
                String[] xyArr = new String[2];
                xyArr = xy.split(",");
                (scheduleTextView[Integer.parseInt(xyArr[0])][Integer.parseInt(xyArr[1])]).setText(title);
            }
        }
    }

    /* DB에 스케쥴 추가 */
    public void scheduleAddDB(String id, String title){
        String[] xyArr = new String[2];
        for(String xy : listXY) {
            //서버 서비스를 포문 밖에서 돌리면 같은 서비스를 계속 불러내서 에러가 난다.
            ServerService serverService = new ServerService(mContext);
            xyArr = xy.split(",");
            String x = xyArr[0];
            String y = xyArr[1];
            requestResult = serverService.insertSchedule(id, title, x, y, color);
        }
    }

    /* color로 색 변경 */
    public void changeScheduleColor(View view){
        view.setBackgroundColor(Color.parseColor(color));
    }

    /* drawble로 변경 */
    public void changeScheduleDraw(View view){
        view.setBackground(ContextCompat.getDrawable(mContext, R.drawable.schedule_back));
    }

    public boolean getInitialFlag(){
        return getInitialFlag();
    }

    public void setInitialFlag(boolean initialFlag){
        this.initialFlag = initialFlag;
    }

    public String getListXY(){
        return listXY.toString();
    }

    public void addListXY(String xy){
        listXY.add(xy);
    }

    /* listXY 비우기 */
    public void listXYClear(){
        if(listXY.isEmpty()==false) {
            listXY.clear();
        }
    }

    public String getSingleXY(){
        return singleXY;
    }

    public void setSingleXY(String singleXY){
        this.singleXY = singleXY;
    }

    public void setColorChangeFlag(boolean colorChangeFlag){
        this.colorChangeFlag = colorChangeFlag;
    }

    public boolean getColorChangeFlag(){
        return colorChangeFlag;

    }

}
