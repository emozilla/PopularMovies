package net.nanodegree.popularmovies.provider;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import net.nanodegree.popularmovies.model.Movie;

/**
 * Created by antonio on 12/09/15.
 */
public class MovieDbProvider extends OrmLiteSimpleContentProvider<MovieDbHelper> {

    @Override
    protected Class<MovieDbHelper> getHelperClass() {
        return MovieDbHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()
                .add(Movie.class, SubType.DIRECTORY, "", MovieContract.CONTENT_URI_PATTERN_MANY)
                .add(Movie.class, SubType.ITEM, "#", MovieContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}