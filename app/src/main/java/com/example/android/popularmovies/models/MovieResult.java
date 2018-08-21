package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MovieResult implements Parcelable {
    private int vote_count; // 1732
    private int id; // 337167
    private boolean video; // false
    private double vote_average; // 6
    private String title; // "Fifty Shades Freed"
    private double popularity; // 538.579632
    private String poster_path; // "\/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg"
    private String original_language; // "en"
    private String original_title; // "Fifty Shades Freed"
    private List genre_ids; // [number]
    private String backdrop_path; // "\/9ywA15OAiwjSTvg3cBs9B7kOCBF.jpg"
    private boolean adult; // false
    private String overview; // "Believing they have left behind shadowy figures from their past, newlyweds Christian and Ana fully embrace an inextricable connection and shared life of luxury. But just as she steps into her role as Mrs. Grey and he relaxes into an unfamiliar stability, new threats could jeopardize their happy ending before it even begins."
    private String release_date; // "2018-02-07"

    private MovieResult() {}
    public MovieResult(
            int _vote_count,
            int _id,
            boolean _video,
            double _vote_average,
            String _title,
            double _popularity,
            String _poster_path,
            String _original_language,
            String _original_title,
            List _genre_ids,
            String _backdrop_path,
            boolean _adult,
            String _overview,
            String _release_date
    ) {
        vote_count = _vote_count;
        id = _id;
        video = _video;
        vote_average = _vote_average;
        title = _title;
        popularity = _popularity;
        poster_path = _poster_path;
        original_language = _original_language;
        original_title = _original_title;
        genre_ids = _genre_ids;
        backdrop_path = _backdrop_path;
        adult = _adult;
        overview = _overview;
        release_date = _release_date;
    }

    private MovieResult(Parcel in) {

        vote_count = in.readInt();
        id = in.readInt();
        video = in.readByte() != 0;
        vote_average = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        genre_ids = in.readArrayList(int.class.getClassLoader());
        backdrop_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vote_count);
        dest.writeInt(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(vote_average);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(poster_path);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeList(genre_ids);
        dest.writeString(backdrop_path);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    public static final Creator<MovieResult> CREATOR = new Creator<MovieResult>() {
        @Override
        public MovieResult createFromParcel(Parcel in) {
            return new MovieResult(in);
        }

        @Override
        public MovieResult[] newArray(int size) {
            return new MovieResult[size];
        }
    };

    // accessor properties
    public double vote_average() { return vote_average; }
    public String poster_path() { return poster_path; }
    public String original_title() { return original_title; }
    public String overview() { return overview; }
    public String release_date() { return release_date; }

    @Override
    public int describeContents() {
        return 0;
    }
}
