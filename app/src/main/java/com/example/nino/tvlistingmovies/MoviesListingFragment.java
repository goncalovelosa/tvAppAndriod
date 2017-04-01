package com.example.nino.tvlistingmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nino.tvlistingmovies.utils.MovieContent;
import com.example.nino.tvlistingmovies.utils.MovieContent.MovieItem;
import com.example.nino.tvlistingmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MoviesListingFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    int mRequestedPage;
    boolean mRequesting = false;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoviesListingFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MoviesListingFragment newInstance(int columnCount) {
        MoviesListingFragment fragment = new MoviesListingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        mRequestedPage = 1;
        executeQuery(mRequestedPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mLayoutManager = new LinearLayoutManager(context);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(new MovieRecyclerViewAdapter(MovieContent.ITEMS, mListener));

            setRecyclerViewScrollListener();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MovieItem item);
    }

    private void executeQuery(int page) {
        URL url = NetworkUtils.buildUrl(String.valueOf(page));
        new MovieDbTask().execute(url);
    }

    private int getLastVisibleItemPosition() {
        return mLayoutManager.findLastVisibleItemPosition();
    }

    private void setRecyclerViewScrollListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mLayoutManager.getItemCount();
                if (!mRequesting && totalItemCount == getLastVisibleItemPosition()+1){
                    requestMovies();
                }
            }
        });
    }

    private void requestMovies() {
        if(mRequestedPage < 1000){
            executeQuery(++mRequestedPage);
        }
    }


    public class MovieDbTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRequesting = true;
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String moviesData = null;
            try {
                moviesData = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return moviesData;
        }

        @Override
        protected void onPostExecute(String res) {
            if (res != null && !res.equals("")) {
                try {
                    JSONObject object = new JSONObject(res);
                    JSONArray jsonArray = object.getJSONArray("results");
                    if (jsonArray != null){
                        for (int i = 0; i < jsonArray.length();i++){
                            JSONObject obj = jsonArray.optJSONObject(i);
                            MovieContent.ITEMS.add(new MovieItem(obj));
                            mRecyclerView.getAdapter().notifyItemInserted(i);
                        }
                    }
                    mRequesting = false;
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
