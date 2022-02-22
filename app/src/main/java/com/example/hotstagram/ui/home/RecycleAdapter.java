package com.example.hotstagram.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.hotstagram.ui.home.CommantActivity;
import com.example.hotstagram.GetPostDataBase;
import com.example.hotstagram.R;
import com.example.hotstagram.UpdateUploadActivity;
import com.example.hotstagram.util.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostInfo> postInfoArrayList = new ArrayList<>();
    private MyViewHolder myViewHolder;
    FirebaseStorage storage;
    StorageReference storageRefpr;
    Context context;

    Intent intent;

    FirebaseUser firebaseUser;
    GetPostDataBase getPostDataBase;

    String[] commantcount;


    RecycleAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_recyclerview, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        myViewHolder = (MyViewHolder) holder;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();


        //이미지 뷰페이저
        String[] splitimg =  postInfoArrayList.get(position).getImg().split(",");
        ArrayList<String> resimg = new ArrayList<>();
        for(int i=0; i<splitimg.length; i++) {
            resimg.add(splitimg[i].substring(1, 57));
        }
        VPGalleryAdapter vpGalleryAdapter = new VPGalleryAdapter(context, resimg);
        myViewHolder.viewPager.setAdapter(vpGalleryAdapter);
        myViewHolder.circleIndicator.setViewPager(myViewHolder.viewPager);

        if(postInfoArrayList.get(position).getCommant() != null) {
            commantcount = postInfoArrayList.get(position).getCommant().split(",");
            myViewHolder.tvcommantcount.setText("댓글 " + commantcount.length/3 + "개");
        }

        myViewHolder.tvname.setText(postInfoArrayList.get(position).getName());
        myViewHolder.tvletter.setText(postInfoArrayList.get(position).getLetter());

        //메뉴 다이얼로그
        myViewHolder.ivmenu.setTag(holder.getAdapterPosition());
        myViewHolder.ivmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(postInfoArrayList.get(position).getUid().equals(firebaseUser.getUid())) {
                    show(view);
                }else{
                    Toast.makeText(view.getContext(), "같은 사용자가아닙니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //프로필사진
        if(postInfoArrayList.get(position).getProfil() != null) {
            storageRefpr = storage.getReferenceFromUrl("gs://hotstagram-cd509.appspot.com/" + postInfoArrayList.get(position).getProfil().toString());
            Log.e("proimg",""+postInfoArrayList.get(position).getProfil().toString());
            GlideApp.with(context).load(storageRefpr).error(R.drawable.user_profile2).into(myViewHolder.ivprofile);

        }

        //게시물 시간
        myViewHolder.tvpostcount.setText(postInfoArrayList.get(position).getTime());

        //좋아요
        if(postInfoArrayList.get(position).getCount() != -1){
            myViewHolder.ivlike.setImageResource(R.drawable.basic_fill);
        }else{
            myViewHolder.ivlike.setImageResource(R.drawable.basic);
        }
        if (postInfoArrayList.get(position).getSize() > 0) {
            myViewHolder.tvlike.setText("좋아요 " + postInfoArrayList.get(position).getSize() + "개");
        } else {
            myViewHolder.tvlike.setText("좋아요 " + postInfoArrayList.get(position).getSize() + "개");
        }

    }



    //다이얼로그
    public void show(final View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final int pos = (int) view.getTag();

        builder.setItems(R.array.home_post, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("which",which +" ");
                switch (which) {
                    case 0:
                        notifyItemRemoved(pos);
                        GetPostDataBase getPostDataBase = new GetPostDataBase(view.getContext());
                        getPostDataBase.RemovePostDataBase(pos,postInfoArrayList);
                        Toast.makeText(view.getContext(), "삭제" + postInfoArrayList.get(pos).getLetter(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(view.getContext(), "수정", Toast.LENGTH_SHORT).show();
                        Log.e("postInfoArrayList.ge",""+ postInfoArrayList.get(pos).getImg());

                        intent = new Intent(context, UpdateUploadActivity.class);
                        intent.putExtra("setimg",postInfoArrayList.get(pos).getImg());
                        intent.putExtra("setname",postInfoArrayList.get(pos).getName());
                        intent.putExtra("settime",postInfoArrayList.get(pos).getTime());
                        intent.putExtra("setpos",pos);
                        intent.putExtra("setUid",postInfoArrayList.get(pos).getUid());
                        intent.putExtra("setNum",postInfoArrayList.get(pos).getNum());
                        context.startActivity(intent);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return postInfoArrayList.size();
    }

    /*  public void additem(PostInfo postInfo){

          postInfoArrayList.add(postInfo);

      }
  */
    public void updateAllData(ArrayList<PostInfo> postInfoArrayList) {
        if (this.postInfoArrayList != null || this.postInfoArrayList.size() > 0) {
            this.postInfoArrayList.clear();
            this.postInfoArrayList = null;
            this.postInfoArrayList = new ArrayList<>();
        }
        this.postInfoArrayList = postInfoArrayList;
        notifyDataSetChanged();
    }

    /*public void updateData(PostInfo postInfo) {

        Log.e("updateData",postInfo.getImg()+" ");
        this.postInfoArrayList.add(0, postInfo);



        notifyDataSetChanged();
    }*/

    public void updateData(PostInfo postInfo) {

        this.postInfoArrayList.add(postInfo);

        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivlike;
        TextView tvlike;
        ImageView ivPicture;
        ImageView ivmenu;
        TextView tvname;
        TextView tvletter;
        ImageView ivprofile;
        ImageView ivbubble;
        CircleIndicator circleIndicator;
        TextView tvcommantcount;
        TextView tvpostcount;


        ViewPager viewPager;
        Intent intent;


        MyViewHolder(View view) {
            super(view);
            circleIndicator = view.findViewById(R.id.indicator);
            tvname = view.findViewById(R.id.tv_name);
            tvletter = view.findViewById(R.id.tv_letter);
            ivmenu = view.findViewById(R.id.iv_menu);
            ivlike = view.findViewById(R.id.img_like);
            tvlike = view.findViewById(R.id.tv_like);
            ivprofile = view.findViewById(R.id.iv_profile);
            ivbubble = view.findViewById(R.id.img_bubble);
            viewPager = view.findViewById(R.id.recycler_viewpager);
            tvcommantcount = view.findViewById(R.id.tv_commantcount);
            tvpostcount = view.findViewById(R.id.tv_postcount);


            //좋아요
            ivlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String user = firebaseUser.getUid();
                    Log.e("getCount",postInfoArrayList.get(pos).getCount()+" ");
                    if (postInfoArrayList.get(pos).getCount() != -1) {
                        ivlike.setImageResource(R.drawable.basic);
                        getPostDataBase = new GetPostDataBase(view.getContext());
                        getPostDataBase.unlikeclick(pos, user);
                        postInfoArrayList.get(pos).setCount(-1);
                        postInfoArrayList.get(pos).setSize(postInfoArrayList.get(pos).getSize()-1);
                    } else {
                        ivlike.setImageResource(R.drawable.basic_fill);
                        getPostDataBase = new GetPostDataBase(view.getContext());
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        getPostDataBase.likeclick(pos, user);
                        postInfoArrayList.get(pos).setCount(0);
                        postInfoArrayList.get(pos).setSize(postInfoArrayList.get(pos).getSize()+1);
                    }
                    String likeText = postInfoArrayList.get(pos).getSize() + "좋아요";
                    tvlike.setText(likeText);
                }
            });

            //댓글클릭
            ivbubble.setOnClickListener(ClickListener);
            tvcommantcount.setOnClickListener(ClickListener);
        }

        View.OnClickListener ClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                intent = new Intent(context, CommantActivity.class);
                intent.putExtra("postletter",postInfoArrayList.get(pos).getLetter());
                intent.putExtra("proimg",postInfoArrayList.get(pos).getProfil().toString());
                intent.putExtra("postname",postInfoArrayList.get(pos).getName());
                intent.putExtra("pos",pos);
                intent.putExtra("document",postInfoArrayList.get(pos).getUid()+"_" + postInfoArrayList.get(pos).getNum());
                Log.e("Uid",postInfoArrayList.get(pos).getUid()+"_"+postInfoArrayList.get(pos).getNum());
                context.startActivity(intent);
            }
        };

    }
}
