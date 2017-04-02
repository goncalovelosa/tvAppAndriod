package com.example.nino.tvlistingmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nino.tvlistingmovies.utils.MovieContent;
import com.example.nino.tvlistingmovies.utils.MovieContent.MovieItem;
import com.example.nino.tvlistingmovies.utils.MovieDbTasksUtils;
import com.example.nino.tvlistingmovies.utils.NetworkUtils;

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
        requestMovies();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = (MenuItem) menu.findItem(R.id.action_search);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
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
        new MovieDbTasksUtils(mRecyclerView).execute(url);
    }

    /**
     * Used to determine the last visible element for the Scroll view, used to determine when to request more movies details.
     *
     * @return int Value of the last Visible entry on the Scroll View.
     */
    private int getLastVisibleItemPosition() {
        return mLayoutManager.findLastVisibleItemPosition();
    }

    /**
     * Set an Event listener on the scroll view, used to pull new page from Movie DB.
     */
    private void setRecyclerViewScrollListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mLayoutManager.getItemCount();
                if (!MovieDbTasksUtils.mRequesting && totalItemCount == getLastVisibleItemPosition()+1){
                    requestMovies();
                }
            }
        });
    }

    /**
     * Execute async task for fetching more results form Movies.
     */
    private void requestMovies() {
        if(MovieDbTasksUtils.mRequestPage < 1000){
            executeQuery(++MovieDbTasksUtils.mRequestPage);
        }
    }

}
