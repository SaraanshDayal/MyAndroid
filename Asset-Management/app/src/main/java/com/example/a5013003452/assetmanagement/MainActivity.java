package com.example.a5013003452.assetmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a5013003452.assetmanagement.RegisterAsset.RegisterAssetActivity;
import com.example.a5013003452.assetmanagement.RegisterPerson.RegisterPersonActivity;
import com.example.a5013003452.assetmanagement.db.table.People;
import com.example.a5013003452.assetmanagement.db.DBHandler;

public class MainActivity extends AppCompatActivity
{
    private String TAG="Main Activity: ";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG,"in OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void RegisterPerson(View view)
    {
        Log.d(TAG,"in RegisterPerson()");
        Intent intent = new Intent(this, RegisterPersonActivity.class);
       startActivity(intent);
    }
    public void RegisterAsset(View view)
    {
        Log.d(TAG,"in RegisterAsset()");
        Intent intent = new Intent(this, RegisterAssetActivity.class);
        startActivity(intent);
    }
    public void ViewAssets(View view)
    {
        Log.d(TAG,"in ViewAssets()");
        Intent intent = new Intent(this, ViewAssetList.class);
        startActivity(intent);
    }

    public void AllocateAssets(View view)
    {
        Log.d(TAG,"in AllocateAssets()");
          Intent intent = new Intent(this, AllocateAssetActivity.class);
          startActivity(intent);
    }

    public void ViewPeople(View view)
    {
        Log.d(TAG,"in AllocateAssets()");
        Intent intent = new Intent(this, ViewPeopleList.class);
        startActivity(intent);
    }

}
