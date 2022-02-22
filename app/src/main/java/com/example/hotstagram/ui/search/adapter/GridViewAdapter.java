package com.example.hotstagram.ui.search.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hotstagram.R;
import com.example.hotstagram.ui.search.GridViewItem;
import com.example.hotstagram.util.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private ArrayList<GridViewItem> arrayList = new ArrayList<>();
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference islandRef;
    ImageView imageView;


    public GridViewAdapter(){

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
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
            view = inflater.inflate(R.layout.fragment_search_gridview,viewGroup,false);
        }

        GridViewItem gridViewItem = arrayList.get(i);
        imageView = (ImageView)view.findViewById(R.id.iv_img);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+gridViewItem.getUri().toString());
        GlideApp.with(context /* context */).load(storageRef).into(imageView);

        return view;
    }
    public void addItem(String uri){
        GridViewItem gridViewItem = new GridViewItem();
        gridViewItem.setUri(Uri.parse(uri));

        arrayList.add(gridViewItem);
    }
}