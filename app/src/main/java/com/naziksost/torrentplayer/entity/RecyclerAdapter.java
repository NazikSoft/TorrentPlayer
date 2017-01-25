package com.naziksost.torrentplayer.entity;

import android.support.v7.widget.CardView;
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

/**
 * Created by Nazar on 25.01.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FilesHolder> {

    private List<File> files = new ArrayList<>();

    public RecyclerAdapter(List<File> files) {
        this.files = files;
    }

    @Override
    public FilesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        FilesHolder fh = new FilesHolder(v);
        return fh;
    }

    @Override
    public void onBindViewHolder(FilesHolder holder, int position) {
        holder.img.setImageResource(android.R.drawable.ic_menu_slideshow);
        holder.tv.setText(files.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class FilesHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView img;
        TextView tv;

        public FilesHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.itemCardView);
            img = (ImageView) itemView.findViewById(R.id.itemImgView);
            tv = (TextView) itemView.findViewById(R.id.itemTextView);
        }
    }
}
