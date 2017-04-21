package com.example.a5013003452.assetmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a5013003452.assetmanagement.RegisterAsset.RegisterAssetActivity;
import com.example.a5013003452.assetmanagement.RegisterPerson.RegisterPersonActivity;
import com.example.a5013003452.assetmanagement.db.DBHandler;
import com.example.a5013003452.assetmanagement.db.table.Assets;
import com.example.a5013003452.assetmanagement.db.table.People;

import java.util.List;

/**
 * Created by 5013003452 on 3/9/2017.
 */

public class AllocateAssetActivity extends AppCompatActivity
{
    private static String Asset,Category,Allotee;
    private long TAGdata;
    private TextView AlloteeName;
    private Button Approve,Allot,Save;
    private ImageView Approve_view;
    private String TAG="Allocate Asset: ";
    DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_asset);

        Approve=(Button)findViewById(R.id.button_approve);
        Allot=(Button)findViewById(R.id.button_allot);
        Save=(Button)findViewById(R.id.button_save);
        AlloteeName=(TextView)findViewById(R.id.allotee_name);
        Approve_view=(ImageView)findViewById(R.id.allot_view);

        List<String>list=db.getAssetNames();
        Spinner dynamicSpinner = (Spinner) findViewById(R.id.asset_choose);
      //  String[] items= new String[list.size()];
      //  String[] items = new String[] {"On Shelf", "In Use", "damaged","shared"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Asset=(String) parent.getItemAtPosition(position);
                Log.v(TAG, Asset);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // TODO Auto-generated method stub
            }
        });


        Spinner dynamicSpinner1 = (Spinner) findViewById(R.id.asset_category);
        String[] items = new String[] {"Internal", "External"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dynamicSpinner1.setAdapter(adapter1);
        dynamicSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Category=(String) parent.getItemAtPosition(position);
                Log.v(TAG, Category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // TODO Auto-generated method stub
            }
        });

       // approveButtonActionListener();
       // allotButtonActionListener();
        saveButtonActionListener();
    }

    public void approveButtonActionListener(View view)
    {
        Log.d(TAG,"using approve button now");
        Intent intent = new Intent(this, RegisterPersonActivity.class);
        startActivityForResult(intent,1);
    }

    public void allotButtonActionListener(View view)
    {
                Log.d(TAG,"using Allot button now");
                Intent intent = new Intent(this, RegisterPersonActivity.class);
                startActivityForResult(intent,2);
    }

    public void saveButtonActionListener()
    {
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0)
            {
                  db.AddfullAssets(Asset,TAGdata,Allotee,Category);
                Log.d(TAG, "Reading the full asset table now");
                List<Assets> assets = db.getAllAssets();
                for (Assets asset : assets)
                {
                    String log = "AssetID: " + asset.getAssetID() + " ,SerialNo: " + asset.getSno() + " ,Description: " + asset.getDesc()+ " ,ImagePath: " +asset.getImgPath() + " ,Owner: " + asset.getOwner() + " ,Status: " + asset.getstatus() + " ,Owner TAGdata: " + asset.getOwmTagData() + " ,Allotee TAGdata: " + asset.getAlloTagData() + " ,Allotee: " + asset.getAllotte()+ " ,Category: " + asset.getCategory() ;
                    // Writing shops to log
                    Log.d("Assets Table:: ",log);
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            long tag=data.getLongExtra("TAG data",0);
            Assets a=db.getAsset(Asset);
            Log.d(TAG,"Tag data from card: "+tag);
            Log.d(TAG,"Tag data from asset: "+a.getOwmTagData());
            if(a.getOwmTagData()==tag)
            {
                Log.d(TAG,"Asset obtained is: "+a.getDesc());
                Approve_view.setImageResource(R.drawable.tick);
            }
            else
            {
                Log.d(TAG,"Asset obtained is: "+a.getDesc());
                Approve_view.setImageResource(R.drawable.cross);
            }

        }
        if(requestCode==2)
        {
            long tag=data.getLongExtra("TAG data",0);
            People p=db.getPeople(tag);
            Log.d(TAG,"People details are: "+p.getEmp());
            TAGdata=tag;
            Allotee=p.getEmp();
            AlloteeName.setText(Allotee);
        }
    }








}
