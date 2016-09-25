package com.topper.in.assignment;

/**
 * Created by rajiv on 9/25/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;
    public static Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category;
        public NetworkImageView image;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            category = (TextView) view.findViewById(R.id.category);
            image = (NetworkImageView) view.findViewById(R.id.image);
        }
    }


    public EventAdapter(List<Event> eventList, Context context) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getName());
        holder.category.setText(event.getCategory());
        String url = event.getImage().replace("\\", "");
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(holder.image,
                R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        holder.image.setImageUrl(url, imageLoader);
        //holder.image.setImageDrawable();
        //setText(event.getYear());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}