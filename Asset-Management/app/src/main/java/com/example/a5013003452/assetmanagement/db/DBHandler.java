package com.example.a5013003452.assetmanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.a5013003452.assetmanagement.db.table.Assets;
import com.example.a5013003452.assetmanagement.db.table.People;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by 5013003452 on 2/8/2017.
 */
public class DBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AssetsList";
    // For table People
    private static final String TABLE_PEOPLE = "People";
    private static final String KEY_ID = "id";
    private static final String KEY_GID = "gid";
    private static final String KEY_CODE = "tag_code";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_PATH= "imgurl";
    // for table Assets
    private static final String TABLE_ASSETS="Assets";
    private static final String KEY_IDNO = "id";
    private static final String KEY_ASSET_ID="asset_id";
    private static final String KEY_SNO="serial_no";
    private static final String KEY_DESC="desc";
    private static final String KEY_IMG_PTH="assets_img";
    private static final String KEY_OWNER="owner";
    private static final String KEY_STATUS="status";
    private static final String KEY_OWN_TAG="own_tag_data";
    private static final String KEY_ALLO_TAG="allo_tag_data";
    private static final String KEY_ALLOTEE="allotee";
    private static final String KEY_CAT="category";


    private String TAG="Database Handler: ";

    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_PEOPLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GID + " LONG,"+ KEY_CODE + " LONG,"
        + KEY_NAME + " TEXT," + KEY_IMAGE_PATH + " TEXT" + ")";

        String CREATE_ASSETS_TABLE=  "CREATE TABLE " + TABLE_ASSETS + "(" + KEY_IDNO + " INTEGER PRIMARY KEY," + KEY_ASSET_ID + " TEXT," + KEY_SNO + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_IMG_PTH + " TEXT," + KEY_OWNER + " TEXT," + KEY_STATUS + " TEXT," + KEY_OWN_TAG + " LONG," + KEY_ALLO_TAG + " LONG," + KEY_ALLOTEE + " TEXT," + KEY_CAT + " TEXT" + ")";

        db.execSQL(CREATE_PEOPLE_TABLE);
        db.execSQL(CREATE_ASSETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSETS);
        onCreate(db);
    }
    public void addPerson(People people)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GID, people.getGID());
        values.put(KEY_CODE, people.getTagId());
        values.put(KEY_NAME, people.getEmp());
        values.put(KEY_IMAGE_PATH, people.getImgPath());
        // Inserting Row
        db.insert(TABLE_PEOPLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting one person from TAG data
    public People getPeople(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PEOPLE, new String[]{KEY_ID,KEY_GID, KEY_CODE, KEY_NAME,KEY_IMAGE_PATH}, KEY_CODE + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        People contact = new People(Long.parseLong(cursor.getString(1)),Long.parseLong(cursor.getString(2)),cursor.getString(3), cursor.getString(4));
// return single person
        cursor.close();
        return contact;
    }

    // Getting one person from GID
    public People getPeopleGID(long gid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PEOPLE, new String[]{KEY_ID,KEY_GID, KEY_CODE, KEY_NAME,KEY_IMAGE_PATH}, KEY_GID + "=?",
                new String[]{String.valueOf(gid)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        People contact = new People(Long.parseLong(cursor.getString(1)),Long.parseLong(cursor.getString(2)),cursor.getString(3), cursor.getString(4));
// return single person
        cursor.close();
        return contact;
    }

    // Getting list of all People
    public List<People> getAllPeople()
    {
        List<People> PeopleList = new ArrayList<People>();
        String selectQuery = "SELECT * FROM " + TABLE_PEOPLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                People people = new People();
                people.setGID(Long.parseLong(cursor.getString(1)));
                people.setTagId(Long.parseLong(cursor.getString(2)));
                people.setEmp(cursor.getString(3));
                people.setImgPath(cursor.getString(4));
                PeopleList.add(people);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return PeopleList;
    }

    public boolean hasObject(Long TagID)
    {
        String tag=Long.toString(TagID);
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_PEOPLE + " WHERE " + KEY_CODE + " =?";
        Cursor cursor = db.rawQuery(selectString, new String[] {tag});
        boolean hasObject = false;
        if(cursor.moveToFirst())
        {
            hasObject = true;
            int count = 0;
            while(cursor.moveToNext())
            {
                count++;
            }
            Log.d(TAG, String.format("%d records found", count));
        }
        cursor.close();
        db.close();
        return hasObject;
    }



    // Getting count of all the people
    public int getPeopleCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_PEOPLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }


    // Updating the People's table
   /* public int updatePeople(People people)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GID, people.getGID());
        values.put(KEY_CODE, people.getTagId());
        values.put(KEY_NAME, people.getEmp());
        values.put(KEY_IMAGE_PATH, people.getImgPath());
        return db.update(TABLE_PEOPLE, values, KEY_GID + " = ?", new String[]{String.valueOf(people.getGID())});
    }*/

    public void UpdatePeople(long tag,long GID,String profile,String path)
    {
       // Log.d(TAG,"Value of tag is: "+tag);
        String selectQuery = "UPDATE "+TABLE_PEOPLE+" SET "+KEY_GID+"="+GID+","+KEY_NAME+"="+"'"+profile+"'"+","+KEY_IMAGE_PATH+"="+"'"+path+"'"+" WHERE "+KEY_CODE+"="+"'"+tag+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void RemoveAPerson(long GID)
    {
        String selectQuery = "DELETE FROM "+TABLE_PEOPLE+" WHERE "+KEY_GID+"="+GID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    // Deleting the People's table
    public void deletePeople(People people)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEOPLE, KEY_GID + " = ?", new String[]{ String.valueOf(people.getGID()) });
        db.close();
    }

    public void addAsset(Assets asset)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ASSET_ID, asset.getAssetID());
        values.put(KEY_SNO, asset.getSno());
        values.put(KEY_DESC, asset.getDesc());
        values.put(KEY_IMG_PTH, asset.getImgPath());
        values.put(KEY_OWNER, asset.getOwner());
        values.put(KEY_STATUS, asset.getstatus());
        values.put(KEY_OWN_TAG, asset.getOwmTagData());
        values.put(KEY_ALLO_TAG, asset.getAlloTagData());
        values.put(KEY_ALLOTEE,"");
        values.put(KEY_CAT,"");
        // Inserting Row
        db.insert(TABLE_ASSETS, null, values);
        db.close(); // Closing database connection
    }

    // Getting list of all Assets
    public List<Assets> getAllAssets()
    {
        List<Assets> AssetList = new ArrayList<Assets>();
        String selectQuery = "SELECT * FROM " + TABLE_ASSETS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Assets assets = new Assets();
                assets.setAssetID(cursor.getString(1));
                assets.setSno(cursor.getString(2));
                assets.setDesc(cursor.getString(3));
                assets.setImgPath(cursor.getString(4));
                assets.setOwner(cursor.getString(5));
                assets.setstatus(cursor.getString(6));
                assets.setOwnTagData(Long.parseLong(cursor.getString(7)));
                assets.setAlloTagData(Long.parseLong(cursor.getString(8)));
                assets.setAllotte(cursor.getString(9));
                assets.setCategory(cursor.getString(10));
                AssetList.add(assets);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return AssetList;
    }

    public List<String> getAssetNames()
    {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT "+ KEY_DESC +" FROM " + TABLE_ASSETS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                labels.add(cursor.getString(cursor.getColumnIndex(KEY_DESC) ));
            }
            while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return labels;
    }

    // Getting one asset details
    public Assets getAsset(String des)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ASSETS, new String[]{KEY_IDNO,KEY_ASSET_ID, KEY_SNO, KEY_DESC,KEY_IMG_PTH,KEY_OWNER,KEY_STATUS,KEY_OWN_TAG,KEY_ALLO_TAG,KEY_ALLOTEE,KEY_CAT}, KEY_DESC + "=?",
                new String[]{String.valueOf(des)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Assets asset = new Assets(cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getString(6),Long.parseLong(cursor.getString(7)),Long.parseLong(cursor.getString(8)),cursor.getString(9),cursor.getString(10));
// return single person
        cursor.close();
        return asset;
    }

    public void AddfullAssets(String desc,Long Tagdata, String allotee,String category)
    {
        String selectQuery = "UPDATE "+TABLE_ASSETS+" SET "+KEY_ALLO_TAG+"="+Tagdata+","+KEY_ALLOTEE+"="+"'"+allotee+"'"+","+KEY_CAT+"="+"'"+category+"'"+" WHERE "+KEY_DESC+"="+"'"+desc+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void RemoveAnAsset(String asset)
    {
        String selectQuery = "DELETE FROM "+TABLE_ASSETS+" WHERE "+KEY_DESC+"="+"'"+asset+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void UpdateAssets(String oldasset,String AssetID,String Sno,String desc,String path,String owner,String status,long tag)
    {
        Log.d(TAG,"Value of tag is: "+tag);
        String selectQuery = "UPDATE "+TABLE_ASSETS+" SET "+KEY_ASSET_ID+"="+"'"+AssetID+"'"+","+KEY_SNO+"="+"'"+Sno+"'"+","+KEY_DESC+"="+"'"+desc+"'"+","+KEY_IMG_PTH+"="+"'"+path+"'"+","+KEY_OWNER+"="+"'"+owner+"'"+","+KEY_STATUS+"="+"'"+status+"'"+","+KEY_OWN_TAG+"="+tag+","+KEY_ALLO_TAG+"="+"0"+","+KEY_ALLOTEE+"="+"''"+","+KEY_CAT+"="+"''"+" WHERE "+KEY_DESC+"="+"'"+oldasset+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    // count the number of assets which are registered to a particular user
    public int CountAssets(long tag)
    {
        String countQuery = "SELECT * FROM " + TABLE_ASSETS+" WHERE "+KEY_OWN_TAG+"="+tag;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public void UpdateAssetsOwnerName(String owner,Long TagData)
    {
        String selectQuery = "UPDATE "+TABLE_ASSETS+" SET "+KEY_OWNER+"="+"'"+owner+"'"+" WHERE "+KEY_OWN_TAG+"="+TagData;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void UpdateAssetsAlloteeName(String allotee,Long TagData)
    {
        String selectQuery = "UPDATE "+TABLE_ASSETS+" SET "+KEY_ALLOTEE+"="+"'"+allotee+"'"+" WHERE "+KEY_ALLO_TAG+"="+TagData;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


}
