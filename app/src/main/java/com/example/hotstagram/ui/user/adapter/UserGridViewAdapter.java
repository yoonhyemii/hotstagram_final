package com.example.hotstagram.ui.user.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hotstagram.R;
import com.example.hotstagram.ui.user.UserGridItemView;
import com.example.hotstagram.util.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserGridViewAdapter extends BaseAdapter {

    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView iv_photo;
    private ArrayList<UserGridItemView> userArrayList = new ArrayList<>();

    public UserGridViewAdapter(){

    }

    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return userArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.user_profile_gridview,viewGroup,false);
        }

        UserGridItemView userGridItemView = userArrayList.get(i);
        iv_photo = view.findViewById(R.id.iv_postphoto);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+userGridItemView.getUri().toString());
        GlideApp.with(context).load(storageRef).into(iv_photo);

        return view;
    }

    public void addItem(String uri){
        UserGridItemView userGridItemView = new UserGridItemView();
        userGridItemView.setUri(Uri.parse(uri));

        userArrayList.add(userGridItemView);
    }


}
