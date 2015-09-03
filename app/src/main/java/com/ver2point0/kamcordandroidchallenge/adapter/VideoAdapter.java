package com.ver2point0.kamcordandroidchallenge.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ver2point0.kamcordandroidchallenge.R;
import com.ver2point0.kamcordandroidchallenge.information.VideoInformation;
import com.ver2point0.kamcordandroidchallenge.network.VolleyNetwork;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private LayoutInflater mInflater;
    private List<VideoInformation> mVideoInformationList;
    private  OnThumbnailClickListener mOnThumbnailClickListener;

    public VideoAdapter(Context context, List<VideoInformation> videoInformation) {
        mInflater = LayoutInflater.from(context);
        mVideoInformationList = videoInformation;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.video_item, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final VideoInformation videoInformation = mVideoInformationList.get(position);
        holder.videoTitle.setText(videoInformation.getTitle());
        holder.videoThumbnail.setImageUrl(videoInformation.getThumbnail(), VolleyNetwork.getInstance().getImageLoader());

        holder.videoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnThumbnailClickListener.onThumbnailClick(videoInformation.getVideoUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoInformationList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView videoTitle;
        NetworkImageView videoThumbnail;

        public VideoViewHolder(View itemView) {
            super(itemView);

            videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            videoThumbnail = (NetworkImageView) itemView.findViewById(R.id.video_thumbnail);
        }
    }

    public interface OnThumbnailClickListener {
        public void onThumbnailClick(String videoUrl);
    }

    public void setOnThumbnailClickListener(OnThumbnailClickListener onThumbnailClickListener) {
        mOnThumbnailClickListener = onThumbnailClickListener;
    }
}
