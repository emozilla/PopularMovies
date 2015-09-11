package net.nanodegree.popularmovies.model.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import net.nanodegree.popularmovies.model.Review;

/**
 * Created by antonio on 10/09/15.
 */
public class ParcelableReview extends Review implements Parcelable {


    public ParcelableReview(Review review) {

        this.id = review.id;
        this.author = review.author;
        this.content = review.content;
        this.url = review.url;
    }

    public ParcelableReview(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {

        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(this.id);
        out.writeString(this.author);
        out.writeString(this.content);
        out.writeString(this.url);
    }

    public static final Parcelable.Creator<ParcelableReview> CREATOR
            = new Parcelable.Creator<ParcelableReview>() {
        public ParcelableReview createFromParcel(Parcel in) {
            return new ParcelableReview(in);
        }

        public ParcelableReview[] newArray(int size) {
            return new ParcelableReview[size];
        }
    };
}
