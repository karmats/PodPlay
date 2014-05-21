package se.roshauw.podplay.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Open helper class for database I/O
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "podcasts";

    // Column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_REF = "img_ref";
    public static final String COLUMN_FEED_URL = "feed_url";
    public static final String COLUMN_CATEGORY_IDS = "category_ids";

    public static final String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE_REF,
            COLUMN_FEED_URL, COLUMN_CATEGORY_IDS};

    private final static String CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TITLE + " TEXT NOT NULL, "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_IMAGE_REF + " TEXT, "
                    + COLUMN_FEED_URL + " TEXT NOT NULL, "
                    + COLUMN_CATEGORY_IDS + " TEXT)";

    private final static String NAME = "podcast_db";
    private final static Integer VERSION = 1;
    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // N/A
    }

    // For test purposes
    public void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }
}
