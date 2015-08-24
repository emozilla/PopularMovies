package net.nanodegree.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by antonio on 24/08/15.
 */
public class ParcelableMovie extends Movie implements Parcelable {

    public ParcelableMovie(Movie movie) {
        this.id = movie.id;
        this.adult = movie.adult;
        this.language = movie.language;
        this.title = movie.title;
        this.originalTitle = movie.originalTitle;
        this.overview = movie.overview;
        this.release = movie.release;
        this.backdrop = movie.backdrop;
        this.poster = movie.poster;
        this.popularity = movie.popularity;
        this.video = movie.video;
        this.vote_average = movie.vote_average;
        this.vote_count = movie.vote_count;
        this.genre_ids = movie.genre_ids;
    }

    public ParcelableMovie(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {

        this.id = in.readInt();

        boolean[] adultBooleanArr = new boolean[1];
        in.readBooleanArray(adultBooleanArr);
        this.adult = adultBooleanArr[0];

        this.language = in.readString();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();

        this.release = (java.util.Date) in.readSerializable();

        this.backdrop = in.readString();
        this.poster = in.readString();
        this.popularity = in.readFloat();

        boolean[] videoBooleanArr = new boolean[1];
        in.readBooleanArray(videoBooleanArr);
        this.video = videoBooleanArr[0];

        this.vote_average = in.readFloat();
        this.vote_count = in.readInt();
        this.genre_ids = in.readArrayList(Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(this.id);

        boolean[] adultBooleanArr = new boolean[1];
        adultBooleanArr[0] = this.adult.booleanValue();
        out.writeBooleanArray(adultBooleanArr);

        out.writeString(this.language);
        out.writeString(this.title);
        out.writeString(this.originalTitle);
        out.writeString(this.overview);
        out.writeSerializable(this.release);
        out.writeString(this.backdrop);
        out.writeString(this.poster);
        out.writeFloat(this.popularity);

        boolean[] videoBooleanArr = new boolean[1];
        videoBooleanArr[0] = this.video.booleanValue();
        out.writeBooleanArray(videoBooleanArr);

        out.writeFloat(this.vote_average);
        out.writeInt(this.vote_count);
        out.writeList(this.genre_ids);
    }

    public static final Parcelable.Creator<ParcelableMovie> CREATOR
            = new Parcelable.Creator<ParcelableMovie>() {
        public ParcelableMovie createFromParcel(Parcel in) {
            return new ParcelableMovie(in);
        }

        public ParcelableMovie[] newArray(int size) {
            return new ParcelableMovie[size];
        }
    };
}
