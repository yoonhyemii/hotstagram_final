package com.example.hotstagram.ui.home;

import android.net.Uri;

import java.util.ArrayList;

public class PostInfo {
    public String uid;
    public String num;
    public String name;
    public String letter;
    public String img;
    public int count;
    public int size;
    public Uri profil;
    public String commant;
    public String time;

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getNum() { return num; }
    public void setNum(String num) { this.num = num; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public  String getLetter() {
        return letter;
    }
    public void setLetter(String letter) {
        this.letter = letter;
    }

    public  String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public Uri getProfil() {
        return profil;
    }
    public void setProfil(Uri profil) { this.profil = profil; }

    public String getCommant() { return commant; }
    public void setCommant(String commant) { this.commant = commant; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
