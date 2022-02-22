package com.example.hotstagram.ui.home;

import android.content.Context;
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

public class CommantListViewAdapter extends BaseAdapter {

    FirebaseStorage storage;
    StorageReference storageRef_post;
    StorageReference storageRef_profile;
    private ImageView iv_profile;
    private TextView tv_name;
    private TextView tv_commant;
    private TextView tv_commantcount;
    Context context;

    private ArrayList<CommantListViewItem> listViewItems = new ArrayList<>();

    public CommantListViewAdapter(Context context){
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


    public void addItem(String name, String profile, String commant, String time){
        CommantListViewItem item = new CommantListViewItem();

        item.setName(name);
        item.setProfileuri(profile);
        item.setCommant(commant);
        item.setTime(time);

        listViewItems.add(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.home_commant_listview_item, parent, false);
        }

        tv_name = view.findViewById(R.id.tv_postname);
        tv_commant = view.findViewById(R.id.tv_postcommant);
        iv_profile = view.findViewById(R.id.iv_commantprofile);
        tv_commantcount = view.findViewById(R.id.tv_commanttime);

        CommantListViewItem listViewItem = listViewItems.get(pos);

        storage = FirebaseStorage.getInstance();
        storageRef_post = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/"+listViewItems.get(pos).getProfileuri());

        tv_name.setText(listViewItem.getName());
        tv_commant.setText(listViewItem.getCommant());
        tv_commantcount.setText(listViewItem.getTime());
        GlideApp.with(context).load(storageRef_post).into(iv_profile);



        return view;
    }



}
