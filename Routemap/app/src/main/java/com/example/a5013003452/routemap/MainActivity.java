package com.example.a5013003452.routemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

/**
 * Created by 5013003452 on 6/2/2017.
 */

public class MainActivity extends AppCompatActivity
{
    private String TAG="Search Activity";
    private TextView source,dest;
    private Place origin,end;
    @Override
    public void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main);
        source=(TextView)findViewById(R.id.source_address);
        dest=(TextView)findViewById(R.id.dest_address);
    }

    public void findSource(View view)
    {
        try
        {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent,1);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            Log.e(TAG,e.getLocalizedMessage());
            // TODO: Handle the error.
        }
        catch (GooglePlayServicesNotAvailableException e)
        {
            Log.e(TAG,e.getLocalizedMessage());
            // TODO: Handle the error.
        }
    }

    public void findDest(View view)
    {
        try
        {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent,2);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            Log.e(TAG,e.getLocalizedMessage());
            // TODO: Handle the error.
        }
        catch (GooglePlayServicesNotAvailableException e)
        {
            Log.e(TAG,e.getLocalizedMessage());
            // TODO: Handle the error.
        }
    }

    public void findroute(View view)
    {
        if((origin!=null)&&(end!=null))
        {
           Intent intent=new Intent(this,MapsActivity.class);
            Bundle args=new Bundle();
            args.putParcelable("origin",origin.getLatLng());
            args.putParcelable("destination",end.getLatLng());
            intent.putExtra("geocodes",args);
            startActivity(intent);
        }
        else if((origin==null)||(end==null))
        {
         Toast.makeText(MainActivity.this,"Both origin and destination should be entered",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                origin=PlaceAutocomplete.getPlace(this,data);
                Log.d(TAG,"Origin address: "+origin.getAddress()+" Origin co-ordinates: "+origin.getLatLng().toString());
                source.setText(origin.getAddress());
            }
            else if(resultCode==PlaceAutocomplete.RESULT_ERROR)
            {
                Status status=PlaceAutocomplete.getStatus(this,data);
                Log.e(TAG,status.getStatusMessage());
            }
            else if(resultCode==RESULT_CANCELED)
            {

            }
        }
         else if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {
                end=PlaceAutocomplete.getPlace(this,data);
                Log.d(TAG,"End address: "+end.getAddress()+" End co-ordinates: "+end.getLatLng().toString());
                dest.setText(end.getAddress());
            }
            else if(resultCode==PlaceAutocomplete.RESULT_ERROR)
            {
                Status status=PlaceAutocomplete.getStatus(this,data);
                Log.e(TAG,status.getStatusMessage());
            }
            else if(resultCode==RESULT_CANCELED)
            {

            }
        }
    }


}
