package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MoviesResults implements Parcelable {
    // my instance variables
    private int result_position;

    // instance variables from JSON
    private int page; // 1
    private int total_results; // 19845
    private int total_pages; // 993
    private List<MovieResult> movieResults;

    private MoviesResults() {}
    public MoviesResults(
            int _page,
            int _total_results,
            int _total_pages,
            List<MovieResult> _movieResults
    ) {
        result_position = Integer.MIN_VALUE;
        page = _page;
        total_results = _total_results;
        total_pages = _total_pages;
        movieResults = _movieResults;
    }

    private MoviesResults(Parcel in){
        result_position = in.readInt();
        page = in.readInt();
        total_results = in.readInt();
        total_pages = in.readInt();
        in.readList(movieResults, MovieResult.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(result_position);
        dest.writeInt(page);
        dest.writeInt(total_results);
        dest.writeInt(total_pages);
        dest.writeList(movieResults);
    }

    public static final Creator<MoviesResults> CREATOR = new Creator<MoviesResults>() {
        @Override
        public MoviesResults createFromParcel(Parcel in) {
            return new MoviesResults(in);
        }

        @Override
        public MoviesResults[] newArray(int size) {
            return new MoviesResults[size];
        }
    };

    // accessor properties
    // MoviesResults
    public ArrayList<MovieResult> movieResults() { return (ArrayList<MovieResult>)movieResults; }

    @Override
    public int describeContents() { return 0; }
}
