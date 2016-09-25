package com.topper.in.assignment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventDetail.OnFragmentInteractionListener {
    public static JSONObject json = null;
    private String urlJsonObj = "https://hackerearth.0x10.info/api/toppr_events?type=json&query=list_events";
    private static String TAG = MainActivity.class.getSimpleName();
    public static ArrayList<Event> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    public static EventAdapter mAdapter;
    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new EventAdapter(movieList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    json = readJsonFromUrl(urlJsonObj);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("tl-1", json.toString());
            }
        };

        thread.start();
        try {
            thread.join();
            getdata(json);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public static void getdata(JSONObject objectdata) {
        try {
            JSONArray arraydata = objectdata.getJSONArray("websites");
            for (int i = 0; i < arraydata.length(); i++) {
                JSONObject data = arraydata.getJSONObject(i);
                Event eventObject = new Event();
                eventObject.setName(data.getString("name"));
                eventObject.setCategory(data.getString("category"));
                eventObject.setImage(data.getString("image"));
                eventObject.setId(i + "");
                movieList.add(eventObject);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.d("tl-2", "error" + e.toString());
        }
    }

    public void change(Eventdata ed) {
        EventDetail eventdetail = EventDetail.newInstance(ed);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, eventdetail);
        recyclerView.setVisibility(View.GONE);
        transaction.commit();
    }

    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}