package com.example.a5013003452.assetmanagement;

import android.app.AlertDialog;
import android.app.ListActivity;

import com.example.a5013003452.assetmanagement.RegisterPerson.RegisterPersonActivity;
import com.example.a5013003452.assetmanagement.db.table.Assets;
import com.example.a5013003452.assetmanagement.db.table.People;
import com.example.a5013003452.assetmanagement.db.DBHandler;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by 5013003452 on 3/20/2017.
 */

public class ViewPeopleList extends ListActivity
{
    private ArrayList<String> results = new ArrayList<String>();
    private String tableName = "People",TAG="View People: ";
    private SQLiteDatabase newDB;
    private AlertDialog.Builder build;
    private ListView PeopleList;
    private String Person,GID;
    private int argument2;
    private View argument1;
    DBHandler dbHelper = new DBHandler(this);
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        openAndQueryDatabase();
        displayResultList();
        editList();
    }

    private void editList()
    {
        PeopleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int arg2, long arg3)
            {
                argument1=arg1;
                argument2=arg2;
                build = new AlertDialog.Builder(ViewPeopleList.this);
                build.setTitle("Update/Delete People's list");
                build.setMessage("Do you want to update/delete the record?(Hit back to cancel)");

                build.setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int which)
                    {
                        String value = (String)arg0.getItemAtPosition(arg2);
                        Log.d(TAG,"value is: "+value);
                        String pp=value.substring(0,value.indexOf(" "));
                        Person=pp;
                        String gd = value.substring(value.lastIndexOf(' ') + 1).trim();
                        GID=gd;
                        Log.d(TAG,"Person is: "+pp);
                        Log.d(TAG,"GID is: "+gd);
                        Log.d(TAG,"using delete button now");
                        Intent intent = new Intent(ViewPeopleList.this, RegisterPersonActivity.class);
                        startActivityForResult(intent,1);
                        //  AssetList.removeViewInLayout(argument1);
                        //  AssetList.deferNotifyDataSetChanged();
                        //   dataBase.delete(DBHelper.TABLE_NAME, DBHelper.STAFID + "=" + stafid.get(arg2), null);
                        //   Toast.makeText(getApplicationContext(), results.get(arg2)+ " is deleted.", Toast.LENGTH_SHORT).show();
                        //   displayData();
                        //    dialog.cancel();

                    }
                });//end DELETE

                build.setNegativeButton("UPDATE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int which)
                    {
                        String value = (String)arg0.getItemAtPosition(arg2);
                        Log.d(TAG,"value is: "+value);
                        String pp=value.substring(0,value.indexOf(" "));
                        Person=pp;
                        String gd = value.substring(value.lastIndexOf(' ') + 1).trim();
                        GID=gd;
                        Log.d(TAG,"Person is: "+pp);
                        Log.d(TAG,"GID is: "+gd);
                        Log.d(TAG,"using update button now");
                        Intent intent = new Intent(ViewPeopleList.this, RegisterPersonActivity.class);
                        startActivityForResult(intent,2);
                        //  AssetList.removeViewInLayout(argument1);
                        //  AssetList.deferNotifyDataSetChanged();
                        //   dataBase.delete(DBHelper.TABLE_NAME, DBHelper.STAFID + "=" + stafid.get(arg2), null);
                        //   Toast.makeText(getApplicationContext(), results.get(arg2)+ " is deleted.", Toast.LENGTH_SHORT).show();
                        //   displayData();
                        //    dialog.cancel();

                    }
                });//end DELETE

                AlertDialog alert = build.create();
                alert.show();

                //  String value = (String)adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "in OnActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            long tag = data.getLongExtra("TAG data", 0);
            People p=dbHelper.getPeopleGID(Long.parseLong(GID));
            Log.d(TAG, "Tag data from card: " + tag);
            Log.d(TAG, "Tag data from person: " + p.getTagId());
            if (p.getTagId() == tag)
            {
                Log.d(TAG,"Name to be deleted is: "+p.getEmp());
                Log.d(TAG,"GID to be deleted is: "+p.getGID());
                Long empid=p.getGID();
                int count=dbHelper.CountAssets(tag);
                if(count==0)
                {
                    Log.d(TAG,"Deleting the person");
                    dbHelper.RemoveAPerson(empid);
                }
                else if(count>0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Assets are assigned in the name of "+p.getEmp()+"\n")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
               /* Intent in=new Intent(ViewAssetList.this,ViewAssetList.class);
                startActivity(in);*/
            }
            else if(p.getTagId() != tag)
            {
               // Log.d(TAG, "Asset obtained is: " + a.getDesc());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You are not authorized to delete this person")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
               /* Intent in=new Intent(ViewAssetList.this,ViewAssetList.class);
                startActivity(in);*/

            }
        }
        else if(requestCode == 2)
        {
            long tag = data.getLongExtra("TAG data", 0);
            People p=dbHelper.getPeopleGID(Long.parseLong(GID));
            Log.d(TAG, "Tag data from card: " + tag);
            Log.d(TAG, "Tag data from person: " + p.getTagId());
            if (p.getTagId() == tag) {
                Log.d(TAG, "Name to be updated is: " + p.getEmp());
                Log.d(TAG, "GID to be updated is: " + p.getGID());

                Long tagdata = p.getTagId();
                Intent in = new Intent(ViewPeopleList.this, UpdatePeopleActivity.class);
                in.putExtra("tag data", tagdata);
                Log.d(TAG, "Going to start update people activity");
                startActivity(in);
            }

            else if(p.getTagId() != tag)
            {
              //  Log.d(TAG, "Asset obtained is: " + a.getDesc());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You are not authorized to update "+p.getEmp()+"'s details")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
               /* Intent in=new Intent(ViewAssetList.this,ViewAssetList.class);
                startActivity(in);*/

            }
        }
    }


    private void displayResultList()
    {
        PeopleList=getListView();
        TextView tView = new TextView(this);
        tView.setTextSize(20);
        tView.setText("      Name              GID");
        PeopleList.addHeaderView(tView);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
        PeopleList.setTextFilterEnabled(true);

    }
    private void openAndQueryDatabase()
    {
        try {
            DBHandler dbHelper = new DBHandler(this.getApplicationContext());
            newDB = dbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT name,gid FROM "+tableName, null);
            if (c != null )
            {
                if (c.moveToFirst())
                {
                    do
                    {
                        String name = c.getString(c.getColumnIndex("name"));
                        Long gid = c.getLong(c.getColumnIndex("gid"));
                        results.add(name+"      "+gid);
                    }
                    while (c.moveToNext());
                }
            }
        }
        catch (SQLiteException se )
        {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
        finally
        {
          //  if (newDB != null)
           //     newDB.execSQL("DELETE FROM " + tableName);
            newDB.close();
        }

    }
}
