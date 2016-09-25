package com.topper.in.assignment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class EventDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String name;
    private String image;
    private String ctc;
    private String description;
    private String experience;

    private OnFragmentInteractionListener mListener;

    public EventDetail() {

    }

    public static EventDetail newInstance(Eventdata eventdata) {
        EventDetail fragment = new EventDetail();
        Bundle args = new Bundle();
        args.putString("name", eventdata.getName());
        args.putString("image", eventdata.getImage());
        args.putString("experience", eventdata.getExperience());
        args.putString("ctc", eventdata.getCtc());
        args.putString("description", eventdata.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
            image = getArguments().getString("image");
            ctc = getArguments().getString("ctc");
            description = getArguments().getString("description");
            experience = getArguments().getString("experience");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_detail, container, false);
        TextView name = (TextView)v.findViewById(R.id.d_name);
        TextView ctc = (TextView)v.findViewById(R.id.d_ctc);
        TextView description = (TextView)v.findViewById(R.id.d_description);
        TextView experience = (TextView)v.findViewById(R.id.d_experience);
        Log.d("data-1",image);
        NetworkImageView image = (NetworkImageView) v.findViewById(R.id.d_image);
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(this.getActivity())
                .getImageLoader();
        imageLoader.get(this.image.replace("\\",""), ImageLoader.getImageListener(image,
                R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        image.setImageUrl(this.image, imageLoader);
        name.setText(this.name);
        ctc.setText(this.ctc);
        description.setText(this.description);
        experience.setText(this.experience);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
