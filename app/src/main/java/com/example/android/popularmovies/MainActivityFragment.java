package com.example.android.popularmovies;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.android.popularmovies.adapters.MovieResultsAdapter;
import com.example.android.popularmovies.models.MovieResult;
import com.example.android.popularmovies.models.MoviesResults;
import com.example.android.popularmovies.utilities.JsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();


    private GridView mGridView;
    private MovieResultsAdapter mMovieResultsAdapter;
    private ProgressBar mLoadingIndicator;

    private Activity mActivity;

//    private int mMenuID;

    public MainActivityFragment() {  }

    @Override
    public void onCreate(Bundle savedInstanceState){

        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


        if (
                savedInstanceState != null &&
                savedInstanceState.containsKey(getString(R.string.key_parcelable_movies_results)) &&
                (
                    mMovieResultsAdapter == null ||
                    mMovieResultsAdapter.getCount() <= 0
                )
        ){
            ArrayList<MovieResult> listMovieResult = savedInstanceState.getParcelableArrayList(getString(R.string.key_parcelable_movies_results));
            mMovieResultsAdapter = new MovieResultsAdapter(getActivity(), listMovieResult);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        if (mMovieResultsAdapter != null) {
            outState.putParcelableArrayList(getString(R.string.key_parcelable_movies_results), new ArrayList<Parcelable>(mMovieResultsAdapter.getMovieData()));
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        mActivity = getActivity();
        View mRootView = layoutInflater.inflate(
                R.layout.activity_main_fragment,
                container,
                false
        );

        mGridView = (GridView) mRootView.findViewById(R.id.movies_gv);
        mLoadingIndicator = (ProgressBar) mRootView.findViewById(R.id.pb_loading_indicator);

        mGridView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        new FetchMovieTask().execute(getString(R.string.api_popular_movie_endpoint));

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuID = item.getItemId();
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        switch (menuID) {
            default:
            case R.id.action_popular_movies_sort:
                fetchMovieTask.execute(getString(R.string.api_popular_movie_endpoint));
                break;
            case R.id.action_top_rated_movies_sort:
                fetchMovieTask.execute(getString(R.string.api_top_rated_movie_endpoint));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class FetchMovieTask extends AsyncTask<String, Void, MoviesResults> {

        FetchMovieTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.INVISIBLE);
        }

        /*
        * isOnline() is derived from two separate popular answer on StackOverflow (link below):
        * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
        *
        * First we check for network connection as suggested in the accepted answer by gar.
        *
        * Then we check for network access as suggested in the most popular answer (that wasn't accepted) by Levit.
        * I chose to use the second suggestion (to open a socket connection) as opposed to the first option (ping).
        * I was already not on the UI thread (a possible negative of the second method is that it cannot be run on the UI thread) and
        * according to Levit this method has the advantages of being fast, portable, and reliable.
        * */
        private boolean isOnline() {
            // check network connection
            ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
                return false;
            }

            // check network access
            try {
                String ipGoogleDNS = "8.8.8.8";
                int portDNS = 53;
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress sockaddr = new InetSocketAddress(ipGoogleDNS, portDNS);

                sock.connect(sockaddr, timeoutMs);
                sock.close();

                return true;
            } catch (IOException e) { return false; }
        }

        @Override
        protected MoviesResults doInBackground(String... params) {

            /* If not online then we can't look anything up (and we don't was't to crash trying). */
            if (!isOnline()) {
                return null;
            }

            /* If there's no selected filter, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            URL url;
            String endpoint = params[0];
            if (
                    !endpoint.equals(mActivity.getString(R.string.api_popular_movie_endpoint)) &&
                    !endpoint.equals(mActivity.getString(R.string.api_top_rated_movie_endpoint))
            ) {
                url = NetworkUtils.buildUrl(mActivity, endpoint);
            } else {
                url = NetworkUtils.buildUrl(mActivity, endpoint.equals(mActivity.getString(R.string.api_popular_movie_endpoint)));
            }

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(url);
                return JsonUtils.getMoviesResultsFromJson(mActivity, jsonMoviesResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MoviesResults moviesData) {
            if (moviesData != null) {
                if (mMovieResultsAdapter == null /*|| mMovieResultsAdapter.getCount() == 0*/) {
                    mMovieResultsAdapter = new MovieResultsAdapter(mActivity, moviesData.movieResults());
                } else {
                    mMovieResultsAdapter.setMovieData(moviesData.movieResults());
                }

                // Get a reference to the GridView, and attach this adapter to it.
                mGridView.setAdapter(mMovieResultsAdapter);
                mMovieResultsAdapter.notifyDataSetChanged();


                mGridView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            public void onItemClick(
                                    AdapterView parent,
                                    View v,
                                    int position,
                                    long id
                            ) {
                                Context context = getActivity();
                                Class classDestination = DetailActivity.class;
                                Intent intentDetailActivity = new Intent(context, classDestination);
                                ArrayList<MovieResult> movieResults = new ArrayList<>(mMovieResultsAdapter.getMovieData());
                                intentDetailActivity.putExtra(getString(R.string.key_intent_extra_position), position);
                                intentDetailActivity.putParcelableArrayListExtra(getString(R.string.key_intent_extra_movies_results), movieResults);
                                startActivity(intentDetailActivity);
                            }
                        }
                );
            }
            mGridView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
