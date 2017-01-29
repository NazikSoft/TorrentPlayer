package com.naziksoft.videoplayer.entity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naziksoft.videoplayer.R;
import com.naziksoft.videoplayer.controller.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    private List<File> files = new ArrayList<>();
    private OnRecyclerClickListener onRecyclerClickListener;
    private Controller controller;
    private int itemLayout;

    public RecyclerAdapter(Context context,int resLayout, List<File> files) {
        this.files = files;
        controller = new Controller(context);
        itemLayout = resLayout;
    }

    public void updateList(List<File> newList) {
        files = newList;
        notifyDataSetChanged();
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener listener) {
        onRecyclerClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        File current = files.get(position);

        // if File folder
        if (current.isDirectory())
            holder.img.setImageResource(R.drawable.ic_folder_black_24dp);
        else
            // if File video
            if (controller.isVideoFile(current.getPath()))
                holder.img.setImageResource(R.drawable.ic_movie_black_24dp);
            else
                // if File NOT video
                holder.img.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);

        holder.tv.setText(files.get(position).getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }


    public static class Holder extends RecyclerView.ViewHolder {
        View view;
        ImageView img;
        TextView tv;

        public Holder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.itemImgView);
            tv = (TextView) itemView.findViewById(R.id.itemTextView);
            view = itemView;
        }
    }


}
