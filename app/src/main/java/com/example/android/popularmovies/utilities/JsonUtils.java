package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.MovieResult;
import com.example.android.popularmovies.models.MoviesResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public final class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     * This method parses JSON from a web response and returns a MoviesResults object
     * describing movies based on their sort order.
     *
     * @param movieJsonStr JSON response from server
     *
     * @return MoviesResults object describing movie data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static MoviesResults getMoviesResultsFromJson(
            Context context,
            String movieJsonStr
    ) throws JSONException {

        // Parse MoviesResults
        JSONObject jsonObjectMoviesResults;

        int page;
        int total_results;
        int total_pages;

        JSONArray jsonArrayMoviesResults;

        try {
            jsonObjectMoviesResults = new JSONObject(movieJsonStr);

            page = jsonObjectMoviesResults.optInt(context.getString(R.string.json_movies_page_number), Integer.MIN_VALUE);
            total_results = jsonObjectMoviesResults.optInt(context.getString(R.string.json_movies_total_results_number), Integer.MIN_VALUE);
            total_pages = jsonObjectMoviesResults.optInt(context.getString(R.string.json_movies_total_pages_number), Integer.MIN_VALUE);

            jsonArrayMoviesResults = jsonObjectMoviesResults.getJSONArray(context.getString(R.string.json_movies_results_array));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parsing MoviesResults from: " + movieJsonStr);
            throw e;
        }

        List<MovieResult> movieResults = new ArrayList<>();

        // Parse MoviesResult
        MovieResult movieResult;
        JSONObject jsonObjectMovieResult;

        int vote_count;
        int id;
        boolean video;
        double vote_average;
        String title;
        double popularity;
        String poster_path;
        String original_language;
        String original_title;
        JSONArray jsonArrayGenreIds;
        List<Integer> genre_ids;
        String backdrop_path;
        boolean adult;
        String overview;
        String release_date;


        for (int i = 0; i < jsonArrayMoviesResults.length(); ++i) {
            try {
                jsonObjectMovieResult = jsonArrayMoviesResults.getJSONObject(i);

                vote_count = jsonObjectMovieResult.optInt(context.getString(R.string.json_movies_result_vote_count_number), Integer.MIN_VALUE);
                id = jsonObjectMovieResult.optInt(context.getString(R.string.json_movies_result_id_number), Integer.MIN_VALUE);
                video = jsonObjectMovieResult.optBoolean(context.getString(R.string.json_movies_result_video_bool));
                vote_average = jsonObjectMovieResult.getDouble(context.getString(R.string.json_movies_result_vote_average_number));
                title = jsonObjectMovieResult.optString(context.getString(R.string.json_movies_result_title_string));
                popularity = jsonObjectMovieResult.optDouble(context.getString(R.string.json_movies_result_popularity_number));
                poster_path = jsonObjectMovieResult.getString(context.getString(R.string.json_movies_result_poster_path_string));
                original_language = jsonObjectMovieResult.optString(context.getString(R.string.json_movies_result_original_language_string));
                original_title = jsonObjectMovieResult.getString(context.getString(R.string.json_movies_result_original_title_string));
                jsonArrayGenreIds = jsonObjectMovieResult.optJSONArray(context.getString(R.string.json_movies_result_genre_ids_array_number));
                genre_ids = new ArrayList<>();
                if (jsonArrayGenreIds != null) {
                    int genre_id;
                    for (int j = 0; j < jsonArrayGenreIds.length(); ++j) {
                        genre_id = jsonArrayGenreIds.optInt(j, Integer.MIN_VALUE);
                        genre_ids.add(genre_id);
                    }
                }
                backdrop_path = jsonObjectMovieResult.optString(context.getString(R.string.json_movies_result_backdrop_path_string));
                adult = jsonObjectMovieResult.optBoolean(context.getString(R.string.json_movies_result_adult_bool));
                overview = jsonObjectMovieResult.getString(context.getString(R.string.json_movies_result_overview_string));
                release_date = jsonObjectMovieResult.getString(context.getString(R.string.json_movies_result_release_date_string));

                movieResult = new MovieResult(
                        vote_count,
                        id,
                        video,
                        vote_average,
                        title,
                        popularity,
                        poster_path,
                        original_language,
                        original_title,
                        genre_ids,
                        backdrop_path,
                        adult,
                        overview,
                        release_date
                );
                movieResults.add(movieResult);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "parsing MoviesResults["+i+"] from: "+ movieJsonStr);
                throw e;
            }
        }

        return new MoviesResults(
                page,
                total_results,
                total_pages,
                movieResults
        );
    }
}
