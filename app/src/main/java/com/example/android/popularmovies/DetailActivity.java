package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.models.MovieResult;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView mImageViewPoster = (ImageView) findViewById(R.id.value_poster_iv);
        TextView mTextViewOriginalTitle = (TextView) findViewById(R.id.value_original_title_tv);
        TextView mTextViewOverview = (TextView) findViewById(R.id.value_overview_tv);
        TextView mTextViewVoteAverage = (TextView) findViewById(R.id.value_vote_average_tv);
        TextView mTextViewReleaseDate = (TextView) findViewById(R.id.value_release_date_tv);

        Intent intentOriginating = getIntent();

        if (
                intentOriginating != null &&
                intentOriginating.hasExtra(getString(R.string.key_intent_extra_position)) &&
                intentOriginating.hasExtra(getString(R.string.key_intent_extra_movies_results))
        ){
            ArrayList<MovieResult> listMovieResult = intentOriginating.getParcelableArrayListExtra(getString(R.string.key_intent_extra_movies_results));
            int mPosition = intentOriginating.getIntExtra(getString(R.string.key_intent_extra_position), Integer.MIN_VALUE);

            if (0 <= mPosition && mPosition < listMovieResult.size()) {
                MovieResult movieResult = listMovieResult.get(mPosition);

                String urlPoster = NetworkUtils.buildUrl(getBaseContext(), movieResult.poster_path()).toString();
                Picasso.with(getBaseContext())
                        .load(urlPoster)
                        .error(R.string.image_error)
                        .into(mImageViewPoster);
                mImageViewPoster.setContentDescription(movieResult.original_title());
                mTextViewOriginalTitle.setText(movieResult.original_title());
                mTextViewOverview.setText(movieResult.overview());
                mTextViewVoteAverage.setText(Double.toString(movieResult.vote_average()));
                mTextViewReleaseDate.setText(movieResult.release_date());
            }
        }
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}
