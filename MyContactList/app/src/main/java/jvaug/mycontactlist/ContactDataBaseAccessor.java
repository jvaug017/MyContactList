package jvaug.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactDataBaseAccessor {
    private SQLiteDatabase database;
    private ContactDataBaseHelper helper;

    public ContactDataBaseAccessor(Context context) {
        helper = new ContactDataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public ContentValues dump_contact_into_table(Contact c) {
        ContentValues v = new ContentValues();
        v.put("contactname", c.getName());
        v.put("streetaddress", c.getAddress());
        v.put("city", c.getCity());
        v.put("state", c.getState());
        v.put("zipcode", c.getZipCode());
        v.put("homephone", c.getHomePhone());
        v.put("cellphone", c.getCellPhone());
        v.put("email", c.getEmail());
        v.put("birthdate", c.getBirthday());
        return v;
    }

    public Contact construct_contact_from_table(Cursor v) {
        Contact c = new Contact();
        c.setID(v.getInt(0));
        c.setName(v.getString(1));
        c.setAddress(v.getString(2));
        c.setCity(v.getString(3));
        c.setState(v.getString(4));
        c.setZipCode(v.getInt(5));
        c.setCellPhone(v.getString(6));
        c.setHomePhone(v.getString(7));
        c.setEmail(v.getString(8));
        c.setBirthday(v.getString(9));
        return c;
    }

    public boolean insert_contact(Contact c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = dump_contact_into_table(c);
            didSucceed = database.insert(ContactDataBaseHelper.CONTACT_DB_TABLE_NAME, null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean update_contact(Contact c) {
        boolean didSucceed = false;
        try {
            int id = c.getID();
            ContentValues updateValues = dump_contact_into_table(c);
            didSucceed = database.update(ContactDataBaseHelper.CONTACT_DB_TABLE_NAME, updateValues, "_id=" + id, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public List<Contact> get_contacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            String query = "SELECT  * FROM "+ContactDataBaseHelper.CONTACT_DB_TABLE_NAME;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Contact c = construct_contact_from_table(cursor);
                contacts.add(c);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contacts = new ArrayList<>();
        }
        return contacts;
    }

    public Contact get_contact(int id) {
        Contact newContact = new Contact();
        try {
            String query = "SELECT  * FROM "+ContactDataBaseHelper.CONTACT_DB_TABLE_NAME+" WHERE _id = " + id;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newContact = construct_contact_from_table(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception ignored) {
        }
        return newContact;
    }

    public boolean delete_contact(int id) {
        boolean didDelete = false;
        try {
            didDelete = database.delete(ContactDataBaseHelper.CONTACT_DB_TABLE_NAME, "_id=" + id, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }
}
