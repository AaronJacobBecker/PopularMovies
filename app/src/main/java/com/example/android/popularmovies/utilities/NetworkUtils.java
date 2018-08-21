package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This utility class design and implementation were heavily influenced and drew directly from
 * the Sunshine Project.
 */
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * Builds the URL used to talk to the movie server.
     *
     * @param context invoking Activity
     * @param endPointPopular boolean value representing which type of Url to build (if true, then popular endpoint, else top rated endpoint)
     * @return The appropriate URL (defined by the EndPoint) to use to query the Movie server.
     */
    public static URL buildUrl(Context context, boolean endPointPopular) {
        Uri builtUri;
        if (!endPointPopular){
            builtUri = Uri.parse(context.getString(R.string.api_movie_base_url)).buildUpon().
                    appendPath(context.getString(R.string.api_top_rated_movie_endpoint)).
                    appendQueryParameter(
                            context.getString(R.string.api_key),
                            context.getString(R.string.api_key_value)
                    ).
                    build();
        } else {
            builtUri = Uri.parse(context.getString(R.string.api_movie_base_url)).buildUpon().
                    appendPath(context.getString(R.string.api_popular_movie_endpoint)).
                    appendQueryParameter(
                            context.getString(R.string.api_key),
                            context.getString(R.string.api_key_value)
                    ).
                    build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL " + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the movie server.
     *
     * @param context invoking Activity
     * @param relativePathMoviePoster (only valid for endPoint == MoviePoster) which movie poster to get
     * @return The appropriate URL (defined by the EndPoint) to use to query the Movie server.
     */
    public static URL buildUrl(Context context, String relativePathMoviePoster) {
        relativePathMoviePoster = relativePathMoviePoster.substring(1);
        Uri builtUri = Uri.parse(context.getString(R.string.image_base_url)).buildUpon().
                appendPath(context.getString(R.string.image_size_w185_path)).
                appendPath(relativePathMoviePoster).
                build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL " + url);

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
