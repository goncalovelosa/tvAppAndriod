package com.example.nino.tvlistingmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "original_title";
    private static final String ARG_AVG_VOTES = "vote_average";
    private static final String ARG_VOTE_COUNT = "total_votes";
    private static final String ARG_RELEASE_DATE = "release_date";
    private static final String ARG_OVERVIEW = "overview";
    private static final String ARG_BACKDROP = "backdrop_path";


    // TODO: Rename and change types of parameters
    private ImageView ivBackDrop;
    private TextView tvTitle;
    private RatingBar rbAvgVotes;
    private TextView tvReleaseDate;
    private TextView tvOverview;
    private TextView tvTotalVotes;


    private OnFragmentInteractionListener mListener;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id
     * @param title
     * @param voteAvg
     * @param releaseDate
     * @param overView
     * @param backDrop
     * @return A new instance of fragment MovieDetailsFragment
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFragment newInstance(String id, String title, float voteAvg, String totalVotes, String releaseDate, String overView, String backDrop) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        args.putString(ARG_TITLE, title);
        args.putFloat(ARG_AVG_VOTES, voteAvg);
        args.putString(ARG_VOTE_COUNT,totalVotes);
        args.putString(ARG_RELEASE_DATE, releaseDate);
        args.putString(ARG_OVERVIEW, overView);
        args.putString(ARG_BACKDROP, backDrop);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Created View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        if (getArguments() != null) {
            ivBackDrop = (ImageView) view.findViewById(R.id.im_backdrop);
            tvTitle = (TextView) view.findViewById(R.id.tv_movie_title);
            rbAvgVotes = (RatingBar) view.findViewById(R.id.rb_vote_average);
            tvTotalVotes = (TextView) view.findViewById(R.id.tv_vote_count);
            tvReleaseDate = (TextView) view.findViewById(R.id.tv_release_date);
            tvOverview = (TextView) view.findViewById(R.id.tv_overview);

            Picasso.with(getContext()).load(getArguments()
                    .getString(ARG_BACKDROP))
                    .placeholder(R.drawable.ic_image_photo)
                    .error(R.drawable.ic_image_error)
                    .into(ivBackDrop);
            tvTitle.setText(getArguments().getString(ARG_TITLE));
            tvOverview.setText(getArguments().getString(ARG_OVERVIEW));
            rbAvgVotes.setRating(getArguments().getFloat(ARG_AVG_VOTES));
            tvReleaseDate.append(" "+getArguments().getString(ARG_RELEASE_DATE));
            tvTotalVotes.setText(getArguments().getString(ARG_VOTE_COUNT));

            tvOverview.setMovementMethod(new ScrollingMovementMethod());

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_share);
            fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Release Date: "+getArguments().getString(ARG_RELEASE_DATE);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getArguments().getString(ARG_TITLE));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        }
        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
