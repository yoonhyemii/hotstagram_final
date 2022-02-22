package com.example.hotstagram.ui.basic;

import android.net.Uri;

public class ListViewItem {

    private String posturi;
    private String profileuri;
    private String name;
    private String info;

    public String getPostUri(){
        return posturi;
    }
    public String getProfileUri(){
        return profileuri;
    }
    public String getName(){
        return name;
    }
    public String getInfo(){
        return info;
    }


    public void setPostUri(String posturi){
        this.posturi = posturi;
    }
    public void setProfileUri(String profileuri){
        this.profileuri = profileuri;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setInfo(String info){
        this.info = info;
    }


}
