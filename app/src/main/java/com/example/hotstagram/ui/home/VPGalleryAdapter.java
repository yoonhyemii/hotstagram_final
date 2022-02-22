package com.example.hotstagram.ui.home;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.hotstagram.R;
import com.example.hotstagram.util.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VPGalleryAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> imglist;

    FirebaseStorage storage;
    StorageReference storageRef;

    public VPGalleryAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imglist = imageList;
    }

    @Override
    public int getCount() {
        return imglist.size();

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_home_recycler_viewpager, null);

        ImageView imageView = view.findViewById(R.id.vp_img);
        storage = FirebaseStorage.getInstance();
        Log.d("imglist.size()", "" + imglist.size());

        storageRef = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/" + imglist.get(position));
        Log.d("storageRef", "" + storageRef);

        GlideApp.with(context).load(storageRef).into(imageView);

        container.addView(view);

        return view;
    }

}
