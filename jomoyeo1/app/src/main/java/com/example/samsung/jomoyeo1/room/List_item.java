package com.example.samsung.jomoyeo1.room;

/**
 * 게시판의 내용을 구성하는 부분.
 */

public class List_item {

    //추가한 변수
    private String number;
    private String id;
    private String room_name;
    private String room_pwd;

    public List_item(String number, String id, String room_name, String room_pwd) {
        this.number = number;
        this.id=id;
        this.room_name = room_name;
        this.room_pwd = room_pwd;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_pwd() {
        return room_pwd;
    }

    public void setRoom_pwd(String room_pwd) {
        this.room_pwd = room_pwd;
    }

    @Override
    public String toString() {
        return "List_item{" +
                "number='" + number + '\'' +
                ", id='" + id + '\'' +
                ", room_name='" + room_name + '\'' +
                ", room_pwd='" + room_pwd + '\'' +
                '}';
    }
}
