package com.example.a5013003452.assetmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.example.a5013003452.assetmanagement.R;
import com.example.a5013003452.assetmanagement.RegisterAsset.RegisterAssetActivity;
import com.example.a5013003452.assetmanagement.RegisterPerson.RegisterPersonActivity;
import com.example.a5013003452.assetmanagement.db.DBHandler;
import com.example.a5013003452.assetmanagement.db.table.Assets;
import com.example.a5013003452.assetmanagement.db.table.People;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;



/**
 * Created by 5013003452 on 3/23/2017.
 */

public class UpdateAssetActivity extends AppCompatActivity
{
    private Button Save,Cancel,Register;
    private ImageView cameraButton;
    private ImageView editAstID,editSno,editDesc,AstPic;
    private TextView AstID,Sno,Desc,AstOwnr;
    private static String AssetID,SerialNo,Descrptn,Owner,path,path1,Status;
    private long TAGdata;
    private String TAG="Update Asset: ",asset_name;
    private Assets a;
    DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_asset);
        // get the asset details
        Bundle bundle = getIntent().getExtras();
        asset_name = bundle.getString("Asset name");
        a=db.getAsset(asset_name);


        AstPic=(ImageView)findViewById(R.id.profile_pic);
        cameraButton = (ImageView) findViewById(R.id.button_camera);
        editAstID=(ImageView)findViewById(R.id.button_edit);
        editSno=(ImageView)findViewById(R.id.button_edit2);
        editDesc=(ImageView)findViewById(R.id.button_edit3);
        AstID = (TextView)findViewById(R.id.Asset_ID);
        Sno=(TextView)findViewById(R.id.Serial_no);
        Desc=(TextView)findViewById(R.id.desc);
        AstOwnr=(TextView)findViewById(R.id.asset_person);
        Register=(Button)findViewById(R.id.button_register);
        Save=(Button)findViewById(R.id.button_save);

        //setting values if not updated
        AssetID=a.getAssetID();
        AstID.setText(AssetID);
        SerialNo=a.getSno();
        Sno.setText(SerialNo);
        Descrptn=a.getDesc();
        Desc.setText(Descrptn);
        Owner=a.getOwner();
        AstOwnr.setText(Owner);
        path1=a.getImgPath();
        TAGdata=a.getOwmTagData();
        AstPic.setImageBitmap(BitmapFactory.decodeFile(path1));

        final Spinner dynamicSpinner = (Spinner) findViewById(R.id.asset_status);
        String[] items = new String[] {"On Shelf", "In Use", "damaged","shared"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);


        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Status=(String) parent.getItemAtPosition(position);
                Log.v(TAG, Status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // TODO Auto-generated method stub
            }
        });


        cameraButtonActionListener();
        editTextActionListener();
        saveTextActionListener();
    }

    private void saveTextActionListener()
    {
        Save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg)
            {
                db.UpdateAssets(asset_name,AssetID,SerialNo,Descrptn,path1,Owner,Status,TAGdata);
                // Reading all Assets
                Log.d(TAG, "Reading all assets with updated one");
                List<Assets> assets = db.getAllAssets();
                for (Assets asset : assets)
                {
                    String log = "AssetID: " + asset.getAssetID() + " ,SerialNo: " + asset.getSno() + " ,Description: " + asset.getDesc()+ " ,ImagePath: " +asset.getImgPath() + " ,Owner: " + asset.getOwner() + " ,Status: " + asset.getstatus() + " ,Owner TAGdata: " + asset.getOwmTagData() + " ,Allottee TAGdata: " + asset.getAlloTagData() + " ,Allotee: " + asset.getAllotte()+ " ,Category: " + asset.getCategory() ;
                    // Writing shops to log
                    Log.d("Assets Table:: ",log);
                }
            }
        });

    }



    private void cameraButtonActionListener()
    {
        cameraButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                selectImage();
            }
        });
    }

    private void selectImage()
    {
        final CharSequence[] options = {"Take Photo","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAssetActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    //pic = f;
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                //h=0;
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles())
                {
                    if (temp.getName().equals("temp.jpg"))
                    {
                        f = temp;
                        File photo = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        //pic = photo;
                        break;
                    }
                }
                try
                {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    Matrix mat = new Matrix();

                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientstring != null ? Integer.parseInt(orientstring) : ExifInterface.ORIENTATION_NORMAL;
                    int rotateangle = 0;
                    if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
                        rotateangle = 90;
                    if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
                        rotateangle = 180;
                    if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
                        rotateangle = 270;

                    mat.setRotate(rotateangle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

                    File f1 = new File(f.getAbsolutePath());
                    Bitmap bmpPic = BitmapFactory.decodeStream(new FileInputStream(f1), null, null);
                    Bitmap bmpPic1 = Bitmap.createBitmap(bmpPic, 0, 0, bmpPic.getWidth(), bmpPic.getHeight(), mat, true);

                    //  Bitmap resizedBitmap = bmpPic1.createScaledBitmap(bmpPic1, 293, 395, false);
                    AstPic.setImageBitmap(bmpPic1);
                    path = android.os.Environment.getExternalStorageDirectory() + File.separator + "AssetManager"+File.separator+"Assets"+File.separator;
                    //p = path;
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    path1=path + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    try
                    {
                        outFile = new FileOutputStream(file);
                        bmpPic1.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        //pic=file;
                        outFile.flush();
                        outFile.close();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

           /* if(requestCode==2)
            {
                Long tag=data.getLongExtra("TAG data",0);
                People p=db.getPeople(tag);
                Log.d(TAG,"People details are: "+p.getEmp());
                AstOwnr.setText(p.getEmp());
            }*/

        }
        else
        {
            if(requestCode==2)
            {
                Long tag=data.getLongExtra("TAG data",0);
                People p=db.getPeople(tag);
                Log.d(TAG,"People details are: "+p.getEmp());
                TAGdata=tag;
                Owner=p.getEmp();
                AstOwnr.setText(Owner);
            }
        }

    }

    public void editTextActionListener()
    {
        editAstID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateAssetActivity.this);
                alertDialog.setTitle("Asset ID");
                alertDialog.setMessage("Enter Asset ID");

                final EditText input = new EditText(UpdateAssetActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        AssetID = input.getText().toString();
                        AstID.setText(AssetID);
                    }
                });

                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }

        });

        editSno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateAssetActivity.this);
                alertDialog.setTitle("Asset Serial no");
                alertDialog.setMessage("Enter Serial no");

                final EditText input = new EditText(UpdateAssetActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SerialNo = input.getText().toString();
                        Sno.setText(SerialNo);
                    }
                });

                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }

        });

        editDesc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateAssetActivity.this);
                alertDialog.setTitle("Asset Description");
                alertDialog.setMessage("Give asset description");

                final EditText input = new EditText(UpdateAssetActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Descrptn = input.getText().toString();
                        Desc.setText(Descrptn);
                    }
                });

                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }

        });
    }

    public void getAssetOwner(View view)
    {
        Log.d(TAG, "in getAssetOwner()");
        Intent intent = new Intent(this, RegisterPersonActivity.class);
        startActivityForResult(intent,2);
    }


}
