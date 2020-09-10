package com.example.myaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicField> mFiles;

    MusicAdapter(Context mContext, ArrayList<MusicField> mFiles){
        this.mContext = mContext;
        this.mFiles = mFiles;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        ImageView album_art;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try{
            holder.file_name.setText(mFiles.get(position).getTitle());
            byte[] image = getAlbumArt(mFiles.get(position).getPath());
            if(image != null){
                Glide.with(mContext).asBitmap().load(image).into(holder.album_art);
            }else{
                Glide.with(mContext).asBitmap().load(R.drawable.default_img).into(holder.album_art);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,PlayerActivity.class);
                    intent.putExtra("position",position);
                    mContext.startActivity(intent);
                }
            });
        }catch(Exception e){
            Log.e("TAG", String.valueOf(e));
        }


    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    private byte [] getAlbumArt(String uri){
        byte[] art = null;
        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
            retriever.release();
        }catch(Exception e){
            Log.e("TAG", String.valueOf(e));
        }


        return art;
    }

}
