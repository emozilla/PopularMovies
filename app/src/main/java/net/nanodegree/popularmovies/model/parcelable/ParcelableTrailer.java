package net.nanodegree.popularmovies.model.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import net.nanodegree.popularmovies.model.Trailer;

/**
 * Created by antonio on 10/09/15.
 */
public class ParcelableTrailer extends Trailer implements Parcelable{


    public ParcelableTrailer(Trailer trailer) {

        this.id = trailer.id;
        this.iso = trailer.iso;
        this.key = trailer.key;
        this.name = trailer.name;
        this.site = trailer.site;
        this.size = trailer.size;
        this.type = trailer.type;
    }

    public ParcelableTrailer(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {

        this.id = in.readString();
        this.iso = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(this.id);
        out.writeString(this.iso);
        out.writeString(this.key);
        out.writeString(this.name);
        out.writeString(this.site);
        out.writeInt(this.size);
        out.writeString(this.type);
    }

    public static final Parcelable.Creator<ParcelableTrailer> CREATOR
            = new Parcelable.Creator<ParcelableTrailer>() {
        public ParcelableTrailer createFromParcel(Parcel in) {
            return new ParcelableTrailer(in);
        }

        public ParcelableTrailer[] newArray(int size) {
            return new ParcelableTrailer[size];
        }
    };
}
