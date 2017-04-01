package com.example.nino.tvlistingmovies.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class MovieContent {

    public static final List<MovieItem> ITEMS = new ArrayList<MovieItem>();


    private static void addItem(MovieItem item) {
        ITEMS.add(item);
    }

    /**
     * A Movie item representing a piece of content.
     */
    public static class MovieItem {

        public final static String MOVIES_IMAGES_URL = "http://image.tmdb.org/t/p/w500";

        public String mId;             //Movie Id
        public String mTitle;          //Title of the movie
        public float mAvgVotes;        //Average Votes
        public String mTotalVotes;     //Total Votes
        public String mPoster;         //Poster URL
        public String mReleaseDate;    //Release Date
        public String mOverview;       //Movie Description
        public String mBackdrop;       //Movie Backdrop URL

        public MovieItem(JSONObject movieJSON) {
            try {
                mId = movieJSON.getString("id");
                mTitle = movieJSON.getString("original_title");
                mAvgVotes = Float.parseFloat(movieJSON.getString("vote_average"));
                mTotalVotes = "Total Votes: " + movieJSON.getString("vote_count");
                mReleaseDate = movieJSON.getString("release_date");
                mOverview = movieJSON.getString("overview");
                mBackdrop = MOVIES_IMAGES_URL + movieJSON.getString("backdrop_path");
                mPoster = MOVIES_IMAGES_URL + movieJSON.getString("poster_path");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
