package com.example.samsung.jomoyeo.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.samsung.jomoyeo.R;

import java.util.ArrayList;

/**
 * List_item을 반복적으로 보여주는데 도음을 주는 Base Adapter class
 */

public class MyListAdapter extends BaseAdapter {
    Context context;
    ArrayList<List_item> list_itemArrayList;
    TextView idView;
    TextView roomNameView;

    public MyListAdapter(Context context, ArrayList<List_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    /* listView 가 아이템을 몇개 가지고 있는지 */
    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    /* position에 있는 객체를 반환 */
    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    /* 현재 position을 반환 */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /* 리스트 뷰에서 아이템과 xml을 연결하여 화면에 표시해주는 부분
    * 이 부분에서 반복문이 실행된다.
    * 순차적으로 한칸씩 화면을 구성한다.
    * convertView 는 item.xml을 불러온다. context를 생성자를 통해 받았다.*/
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        }
            idView = (TextView)convertView.findViewById(R.id.idView);
            roomNameView = (TextView)convertView.findViewById(R.id.roomNameView);

            idView.setText(list_itemArrayList.get(position).getId());
            roomNameView.setText(list_itemArrayList.get(position).getRoom_name());
        return convertView;
    }

    class ViewHolder{
        public TextView label;
    }
}