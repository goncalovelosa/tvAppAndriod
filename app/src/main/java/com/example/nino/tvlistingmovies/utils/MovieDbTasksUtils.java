package com.example.nino.tvlistingmovies.utils;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Nino on 01/04/2017.
 */

public class MovieDbTasksUtils extends AsyncTask<URL, Void, String> {


    public static boolean mRequesting = false;      //Used to check is requesting
    public static int mRequestPage;                 //Page Param to request
    private RecyclerView mRecyclerView;             //

    public MovieDbTasksUtils(RecyclerView recyclerView){
        this.mRecyclerView = recyclerView;
    }

    /**
     * Action to be executed before the Task
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRequesting = true;
    }

    /**
     * Main task
     * @param params
     * @return
     */
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

    /**
     * Executed inidiatly after the doInBackgrouns ends
     * @param res
     */
    @Override
    protected void onPostExecute(String res) {
        if (res != null && !res.equals("")) {
            try {
                JSONObject object = new JSONObject(res);
                JSONArray jsonArray = object.getJSONArray("results");
                if (jsonArray != null){
                    for (int i = 0; i < jsonArray.length();i++){
                        JSONObject obj = jsonArray.optJSONObject(i);
                        MovieContent.ITEMS.add(new MovieContent.MovieItem(obj));
                        mRecyclerView.getAdapter().notifyItemInserted(i);
                    }
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            mRequesting = false;
        }
    }
}
