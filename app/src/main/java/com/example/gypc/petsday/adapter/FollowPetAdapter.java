package com.example.gypc.petsday.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.utils.ImageUriConverter;

import java.util.List;

/**
 * Created by StellaSong on 2017/12/20.
 */

public class FollowPetAdapter extends RecyclerView.Adapter<FollowPetAdapter.FollowPetViewHolder> {
    public interface OnItemClickListener {
        void onClickList(int position);
    }

    private OnItemClickListener mOnItemClickListener = null;
    private List<Pet> pets;
    private Context context;

    public FollowPetAdapter(List<Pet> pets, Context context) {
        this.pets = pets;
        this.context = context;
    }

    @Override
    public FollowPetAdapter.FollowPetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FollowPetAdapter.FollowPetViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.mine_followpet, parent, false));
    }

    @Override
    public void onBindViewHolder(final FollowPetAdapter.FollowPetViewHolder holder, int position) {
        holder.pet_nickname.setText(pets.get(position).getPet_nickname());
        holder.pet_type.setText(pets.get(position).getPet_type());
        holder.pet_follow.setText("" + pets.get(position).getCount());
        Uri localImageUri;
        String remoteImagePath;

        localImageUri = ImageUriConverter.getCacheFileUriFromName(context, pets.get(position).getPet_photo());
        remoteImagePath = ImageUriConverter.getImgRemoteUriFromName(pets.get(position).getPet_photo());
        Glide
                .with((context))
                .load(localImageUri != null ? localImageUri : remoteImagePath)
                .placeholder(R.mipmap.pet_default)
                .error(R.drawable.error_img)
                .priority(Priority.HIGH)
                .into(holder.pet_photo);

        holder.contentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClickList(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pets == null ? 0 : pets.size();
    }

    public void setOnItemClickListener(FollowPetAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    class FollowPetViewHolder extends RecyclerView.ViewHolder {
        TextView pet_nickname;
        TextView pet_type;
        TextView pet_follow;
        ImageView pet_photo;
        LinearLayout contentLL;

        public FollowPetViewHolder(View view) {
            super(view);
            pet_nickname = (TextView)view.findViewById(R.id.petnameTV);
            pet_type = (TextView)view.findViewById(R.id.pettypeTV);
            pet_follow = (TextView)view.findViewById(R.id.followTV);
            pet_photo = (ImageView)view.findViewById(R.id.headIV);
            contentLL = (LinearLayout)view.findViewById(R.id.contentLL);
        }
    }


}
