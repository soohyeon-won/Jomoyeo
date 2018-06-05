package com.example.samsung.jomoyeo1.room;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.samsung.jomoyeo1.R;
import com.example.samsung.jomoyeo1.dbconnection.HttpRequestResult;
import com.example.samsung.jomoyeo1.dbconnection.ServerService;
import com.example.samsung.jomoyeo1.main.MainActivity;
import com.example.samsung.jomoyeo1.preference.UserPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 방 검색 화면 + 방 목록
 * DB
 */

public class SearchActivity extends Activity implements AbsListView.OnScrollListener{

    Context mContext = this;
    private Dialog mDialog = null;

    ServerService serverService;
    HttpRequestResult requestResult;

    ListView listView;              //xml의 리스트 뷰
    MyListAdapter myListAdapter;    // 생성한 클래스 리스트뷰에 반복해서 item의 내용을 띄운다.
    ArrayList<List_item> list_itemArrayList;
    /* 검색 기능 구현 */
    EditText search;
    private ArrayList<List_item> clone;

    private static int DBsize = 0;
    JSONArray jsonArray;

    /* 페이징 */
    private ProgressBar progressBar;        //data로딩중을 표시할 프로그래스바
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private boolean mLockListView = false;// 데이터 불러올때 중복안되게 하기위한 변수


    /* 비밀번호 입력받을 다이얼로그 */
    private boolean pass;
    private Button popAttendButton;  // 팝업창 추가
    private Button popupBackButton; // 뒤로 가기
    String password;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView)findViewById(R.id.listView);
        list_itemArrayList = new ArrayList<List_item>();

        /* DB에서 방 목록을 받아옴 */
        serverService = new ServerService(mContext);
        requestResult = serverService.selectRoomList();

        /* 검색을 수행하기 위해 복사해 놓음 */
        clone = new ArrayList<List_item>();
        clone.addAll(list_itemArrayList);

        /* MyListAdapter 클래스와 연결 */
        myListAdapter = new MyListAdapter(SearchActivity.this,list_itemArrayList);
        listView.setAdapter(myListAdapter);

        /* 게시물의 인덱스를 재정렬 하는 부분 */
        DBsize = requestResult.getResultJsonArray().length();   // DB에 들어가있는 게시물 수
        try {
            jsonArray = (JSONArray) requestResult.getResultJsonArray();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        /* 페이징 */
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        listView.setOnScrollListener(this);

        search = (EditText)findViewById(R.id.searchEdit);
        clone = new ArrayList<List_item>();
        getItem();
        clone.addAll(list_itemArrayList);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = search.getText().toString();
                search(text);
            }
        });

        /* listView 클릭하면 방으로 들어감 */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HttpRequestResult r;
                ServerService isAttendUserService = new ServerService(mContext);
                /* 이미 존재하는 유저인지 검사. */
                r = isAttendUserService.isAttendUser(list_itemArrayList.get(position).getRoom_name(), UserPreference.getInstance().getId(mContext));
                if(r.getResultCode()==200) {//존재안하면 패스워드 입력 팝업창 띄움.
                    password = list_itemArrayList.get(position).getRoom_pwd();
                    //패스워드 입력받기.
                    popupPass(position);
                }
                else{//존재할 때. 그냥 PASS
                    Intent intent = new Intent(mContext, RoomViewActivity.class);
                    //누른 listView의 position을 put함
                    intent.putExtra("id", list_itemArrayList.get(position).getId());
                    intent.putExtra("room_name", list_itemArrayList.get(position).getRoom_name());
                    intent.putExtra("back", "SearchActivity");
                    startActivity(intent);  //roomViewActivity로 이동.
                    finish();
                }
            }
        });
    }

    /* 참여 하기 */
    private void popupPass(int position) {
        final View innerView = getLayoutInflater().inflate(R.layout.popup_room_pass, null);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(innerView);
        setPopupSize(mDialog);

        // Back키 눌렀을 경우 Dialog Cancle 여부 설정
        mDialog.getWindow().setGravity(Gravity.CENTER);                                     //Dialog 위치 이동
        final int pos = position;
        final EditText popPasswordTextView = (EditText)innerView.findViewById(R.id.popPasswordTextView);
        /* 팝업창에서 참여 버튼을 눌렀을 때 */
        popAttendButton = (Button) innerView.findViewById(R.id.popAttendButton);
        popAttendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                /* 비밀번호 비교 */
                if(password.equals(popPasswordTextView.getText().toString())){
                    pass = true;
                    if(pass==true){
                        //attend list에 추가.
                        ServerService insertAttendList = new ServerService(mContext);
                        requestResult = insertAttendList.insertAttendUser(list_itemArrayList.get(pos).getRoom_name(), UserPreference.getInstance().getId(mContext), "0", list_itemArrayList.get(pos).getId());

                        dismissDialog();
                        Intent intent = new Intent(mContext, RoomViewActivity.class);
                        //누른 listView의 position을 put함
                        intent.putExtra("id", list_itemArrayList.get(pos).getId());
                        intent.putExtra("room_name", list_itemArrayList.get(pos).getRoom_name());
                        intent.putExtra("back", "SearchActivity");
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(mContext, "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
                }
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

    /* 팝업 크기 설정 */
    private void setPopupSize(Dialog mDialog){
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    /* 다이얼로그 종료 */
    private void dismissDialog() {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list_itemArrayList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list_itemArrayList.addAll(clone);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < clone.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (clone.get(i).getRoom_name().contains(charText))
                {
                    Log.d("출력", charText);
                    // 검색된 데이터를 리스트에 추가한다.
                    list_itemArrayList.add(clone.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        myListAdapter.notifyDataSetChanged();
    }


    int number = 0;
    private void getItem(){

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;
        /* 받아온 내용을 list에 add 한다. */
        try {
            for(int i = 0; i < OFFSET; i++, number++) {
                if(number==DBsize) break;
                JSONObject jsonObject = jsonArray.getJSONObject(number);
                Log.d("test2",jsonObject.toString()+ jsonObject.getString("id"));
                Log.d("출력", jsonObject.getString("id"));
                list_itemArrayList.add(list_itemArrayList.size(),new List_item( number+"",jsonObject.getString("id"),jsonObject.getString("room_name"), jsonObject.getString("room_pwd")));
            }
            myListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d("test2",e.toString());
            e.printStackTrace();
        }

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                myListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);
    }

    /* 페이징 */
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);
            // 다음 데이터를 불러온다.
            clone.clear();
            getItem();
            clone.addAll(list_itemArrayList);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    public void createRoomButtonListener(View view){
        Intent createIntent = new Intent(mContext, CreationActivity.class);
        createIntent.putExtra("backIntent", "SearchActivity");
        startActivity(createIntent);
        finish();
    }

    /* back 버튼 눌렀을 때 이전화면(메인)으로 돌아가기 */
    @Override public void onBackPressed() {
        Intent backIntent = new Intent(mContext, MainActivity.class);
        startActivity(backIntent);
        finish();
    }
}
