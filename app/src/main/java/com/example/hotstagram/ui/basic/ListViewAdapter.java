package com.example.hotstagram.ui.basic;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hotstagram.R;
import com.example.hotstagram.util.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    FirebaseStorage storage;
    StorageReference storageRef_post;
    StorageReference storageRef_profile;
    private ImageView iv_profile;
    private ImageView iv_post;
    private TextView tv_name;
    private TextView tv_info;
    Context context;

    private ArrayList<ListViewItem> listViewItems = new ArrayList<>();

    public ListViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }


    public void addItem(String name, String profile, String info, String post){
        ListViewItem item = new ListViewItem();

        item.setName(name);
        item.setProfileUri(profile);
        item.setInfo(info);
        item.setPostUri(post);

        listViewItems.add(item);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.basic_listview_item, parent, false);
        }

        tv_name = view.findViewById(R.id.tv_name);
        tv_info = view.findViewById(R.id.tv_info);
        iv_profile = view.findViewById(R.id.iv_profile);
        iv_post = view.findViewById(R.id.iv_post);

        ListViewItem listViewItem = listViewItems.get(pos);

        storage = FirebaseStorage.getInstance();
        storageRef_profile = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+listViewItems.get(pos).getPostUri());
        storageRef_post = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+listViewItems.get(pos).getProfileUri());

        tv_name.setText(listViewItem.getName());
        tv_info.setText(listViewItem.getInfo());
        GlideApp.with(context).load(storageRef_profile).into(iv_post);
        GlideApp.with(context).load(storageRef_post).into(iv_profile);

        return view;
    }



}
