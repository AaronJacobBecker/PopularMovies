package com.example.android.popularmovies.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.MovieResult;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieResultsAdapter extends ArrayAdapter<MovieResult> {
    private static final String TAG = MovieResultsAdapter.class.getSimpleName();

    public MovieResultsAdapter(Activity context, ArrayList<MovieResult> moviesData) {
        super(context, 0, moviesData);
    }

    // create a new ImageView for each item referenced by the Adapter
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View itemView;
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            itemView = LayoutInflater.from(
                    getContext()
            ).inflate(
                    R.layout.movie_item,
                    parent,
                    false
            );

            imageView = (ImageView) itemView.findViewById(R.id.movie_iv);
            itemView.setTag(imageView);
        } else {
            itemView = convertView;
            imageView = (ImageView) itemView.getTag();
        }

        MovieResult movieResult = getItem(position);
        assert movieResult != null;
        URL urlPoster = NetworkUtils.buildUrl(getContext(), movieResult.poster_path());
        Picasso.with(getContext())
                .load(urlPoster.toString())
                .error(R.string.image_error)
                .into(imageView);
        imageView.setContentDescription(movieResult.original_title());

        return itemView;
    }

    public List<MovieResult> getMovieData() {
        List<MovieResult> movieData = new ArrayList<>();
        for (int i = 0; i < getCount(); ++i) {
            movieData.add(getItem(i));
        }
        return movieData;
    }

    public void setMovieData(List<MovieResult> listMovieResult) {
        clear();
        addAll(listMovieResult);
    }
}
