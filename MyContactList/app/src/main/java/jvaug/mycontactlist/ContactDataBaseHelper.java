package jvaug.mycontactlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class ContactDataBaseHelper extends SQLiteOpenHelper {

    public static String CONTACT_DB_TABLE_NAME = "mycontactslist";
    public static String CONTACT_DB_NAME = "mycontactslist.db";
    public static int CONTACT_DB_VERSION = 3;

    public static String CREATE_TABLE_CONTACTLIST_COMMAND =
            "create table "+ CONTACT_DB_TABLE_NAME +" (_id integer primary key autoincrement, " +
                    "contactname text, streetaddress text, city text, state text, zipcode text, homephone text, cellphone text, email text, birthdate text);";

    public ContactDataBaseHelper(@Nullable Context context) {
        super(context, CONTACT_DB_NAME, null, CONTACT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTLIST_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContactDataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_DB_TABLE_NAME);
        onCreate(db);
    }
}
