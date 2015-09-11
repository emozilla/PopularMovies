package net.nanodegree.popularmovies.model.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import net.nanodegree.popularmovies.model.Cast;

/**
 * Created by antonio on 10/09/15.
 */
public class ParcelableCast extends Cast implements Parcelable {

    public ParcelableCast(Cast cast) {
        this.id = cast.id;
        this.cast_id = cast.cast_id;
        this.character = cast.character;
        this.creditId = cast.creditId;
        this.name = cast.name;
        this.order = cast.order;
        this.profileImage = cast.profileImage;
    }

    public ParcelableCast(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {

        this.id = in.readInt();
        this.cast_id = in.readInt();
        this.character = in.readString();
        this.creditId = in.readString();
        this.name = in.readString();
        this.order = in.readInt();
        this.profileImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(this.id);
        out.writeInt(this.cast_id);
        out.writeString(this.character);
        out.writeString(this.creditId);
        out.writeString(this.name);
        out.writeInt(this.order);
        out.writeString(this.profileImage);
    }

    public static final Parcelable.Creator<ParcelableCast> CREATOR
            = new Parcelable.Creator<ParcelableCast>() {
        public ParcelableCast createFromParcel(Parcel in) {
            return new ParcelableCast(in);
        }

        public ParcelableCast[] newArray(int size) {
            return new ParcelableCast[size];
        }
    };
}
