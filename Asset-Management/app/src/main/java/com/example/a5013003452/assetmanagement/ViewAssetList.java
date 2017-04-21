package com.example.a5013003452.assetmanagement;

import android.app.Activity;
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
import android.widget.Toast;


/**
 * Created by 5013003452 on 3/20/2017.
 */

public class ViewAssetList extends ListActivity
{

   private ArrayList<String> results = new ArrayList<String>();
    private String tableName = "Assets";
    private SQLiteDatabase newDB;
    private ListView AssetList;
    private AlertDialog.Builder build;
    private String TAG="ViewAsset: ";
    private String Asset;
    private int argument2;
    private View argument1;
    DBHandler dbHelper = new DBHandler(this);
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG,"in OnCreate()");
        super.onCreate(savedInstanceState);
        AssetList=getListView();
        TextView tView = new TextView(this);
        tView.setTextSize(20);
        tView.setText("      Name              S.No          Owner        Allotee      Category ");
        AssetList.addHeaderView(tView);
        openAndQueryDatabase();
      displayResultList();
        editList();
    }

    private void editList()
    {
        AssetList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int arg2, long arg3)
            {
                argument1=arg1;
                argument2=arg2;
                build = new AlertDialog.Builder(ViewAssetList.this);
                build.setTitle("Update/Delete Assets");
                build.setMessage("Do you want to update/delete the record?(Hit back to cancel)");

                build.setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int which)
                    {
                        String value = (String)arg0.getItemAtPosition(arg2);
                        Log.d(TAG,"value is: "+value);
                        String as=value.substring(0,value.indexOf(" "));
                        Asset=as;
                        Log.d(TAG,"Asset is: "+as);
                        Log.d(TAG,"using delete button now");
                        Intent intent = new Intent(ViewAssetList.this, RegisterPersonActivity.class);
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
                        String as=value.substring(0,value.indexOf(" "));
                        Asset=as;
                        Log.d(TAG,"Asset is: "+as);
                        Log.d(TAG,"using update button now");
                        Intent intent = new Intent(ViewAssetList.this, RegisterPersonActivity.class);
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


    private void displayResultList()
    {
        Log.d(TAG,"in displayresultlist()");
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
        AssetList.setTextFilterEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG,"in OnActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            long tag = data.getLongExtra("TAG data", 0);
            Assets a = dbHelper.getAsset(Asset);
            Log.d(TAG, "Tag data from card: " + tag);
            Log.d(TAG, "Tag data from asset: " + a.getOwmTagData());
            if (a.getOwmTagData() == tag)
            {
                Log.d(TAG, "Asset obtained is: " + a.getDesc());
                dbHelper.RemoveAnAsset(Asset);
               /* Intent in=new Intent(ViewAssetList.this,ViewAssetList.class);
                startActivity(in);*/
            }
            else if(a.getOwmTagData() != tag)
            {
                Log.d(TAG, "Asset obtained is: " + a.getDesc());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You are not authorized to delete this asset")
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

        else if(requestCode==2)
        {
            long tag = data.getLongExtra("TAG data", 0);
            Assets a = dbHelper.getAsset(Asset);
            Log.d(TAG, "Tag data from card: " + tag);
            Log.d(TAG, "Tag data from asset: " + a.getOwmTagData());
            if (a.getOwmTagData() == tag)
            {
                String ass=a.getDesc();
                Intent in=new Intent(ViewAssetList.this,UpdateAssetActivity.class);
                in.putExtra("Asset name",ass);
                Log.d(TAG,"Going to start update assets activity");
                startActivity(in);
            }
            else if(a.getOwmTagData() != tag)
            {
                Log.d(TAG, "Asset obtained is: " + a.getDesc());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You are not authorized to update this asset")
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


    private void openAndQueryDatabase()
    {
        Log.d(TAG,"in openAndQueryDatabase()");
        try {

            newDB = dbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT desc,serial_no,owner,allotee,category FROM "+tableName, null);
            if (c != null )
            {
                if (c.moveToFirst())
                {
                    do
                    {
                        String name = c.getString(c.getColumnIndex("desc"));
                        String sno = c.getString(c.getColumnIndex("serial_no"));
                        String owner = c.getString(c.getColumnIndex("owner"));
                        String category = c.getString(c.getColumnIndex("category"));
                        String allotee = c.getString(c.getColumnIndex("allotee"));
                        results.add(name+"      "+sno+"     "+owner+"       "+allotee+"        "+category);
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

    @Override
    protected void onResume()
    {
        //refresh data for screen is invoked/displayed
        Log.d(TAG,"In onResume()");
       // displayResultList();
        super.onResume();
    }
}
