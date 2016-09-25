package com.topper.in.assignment;

/**
 * Created by rajiv on 9/25/2016.
 */

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private String urlJsonObj = "https://hackerearth.0x10.info/api/toppr_events?type=json&query=list_events";
    private List<Event> eventList;
    public static Context context;
    String id = "";
    public static JSONObject data;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category;
        public NetworkImageView image;
        public ImageView navigate;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            category = (TextView) view.findViewById(R.id.category);
            image = (NetworkImageView) view.findViewById(R.id.image);
            navigate = (ImageView) view.findViewById(R.id.navigate);
            navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Eventdata ed = new Eventdata();
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                JSONArray json = readJsonFromUrl(urlJsonObj).getJSONArray("websites");
                                data = json.getJSONObject(getAdapterPosition());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t.start();
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ed.setName(data.optString("name"));
                    ed.setCategory(data.optString("category"));
                    ed.setImage(data.optString("image"));
                    ed.setCtc(data.optString("ctc"));
                    ed.setDescription(data.optString("description"));
                    ed.setExperience(data.optString("experience"));
                    ((MainActivity)context).change(ed);

                }
            });
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
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
        id = event.getId();
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(holder.image,
                R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        holder.image.setImageUrl(url, imageLoader);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}