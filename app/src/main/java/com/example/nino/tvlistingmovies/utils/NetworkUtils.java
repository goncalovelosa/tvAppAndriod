package com.example.nino.tvlistingmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Nino on 31/03/2017.
 */

public class NetworkUtils {

    final static String MOVIES_BASIC_URL = "https://api.themoviedb.org/3/";

    final static String DISCOVER = "discover/movie";
    final static String SEARCH = "search/movie";

    final static String PARAM_SORT = "sort_by";
    final static String PARAM_API = "api_key";
    final static String PARAM_PAGE = "page";
    final static String PARAM_SEARCH = "query";

    final static String sortBy = "popularity.desc";
    final static String apiKey = "9c8a3badf8660ed8e756140ea8afda51";

    /**
     * Builds the URL used to query The Movie Db.
     *
     * @param page Page Number to fetch Results
     * @return The URL to use to query the The Movie Db.
     */
    public static URL buildUrl(String page){
        Uri builtUri = Uri.parse(MOVIES_BASIC_URL+DISCOVER).buildUpon()
                .appendQueryParameter(PARAM_API, apiKey)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_PAGE, page)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to search The Movie Db.
     *
     * @param page Page Number to fetch Results
     * @param search Search Query
     * @return The URL to use to query the The Movie Db
     */
    public static URL buildUrl(String page, String search){
        Uri builtUri = Uri.parse(MOVIES_BASIC_URL+SEARCH).buildUpon()
                .appendQueryParameter(PARAM_SEARCH,search)
                .appendQueryParameter(PARAM_API, apiKey)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_PAGE, page)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
