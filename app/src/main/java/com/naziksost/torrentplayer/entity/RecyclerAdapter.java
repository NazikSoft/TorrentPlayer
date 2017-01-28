package com.naziksost.torrentplayer.entity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.naziksost.torrentplayer.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private List<File> files = new ArrayList<>();
    private OnRecyclerClickListener onRecyclerClickListener;

    public RecyclerAdapter(List<File> files) {
        this.files = files;
    }

    public void updateList(ArrayList<File> newList){
        files = newList;
        notifyDataSetChanged();
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener listener){
        onRecyclerClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        holder.img.setImageResource(android.R.drawable.ic_menu_slideshow);
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
