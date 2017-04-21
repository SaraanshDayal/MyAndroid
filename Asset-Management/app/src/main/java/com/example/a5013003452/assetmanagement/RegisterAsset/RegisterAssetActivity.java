package com.example.a5013003452.assetmanagement.RegisterAsset;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.a5013003452.assetmanagement.RegisterPerson.RegisterPersonActivity;
import com.example.a5013003452.assetmanagement.db.DBHandler;
import com.example.a5013003452.assetmanagement.db.table.Assets;
import com.example.a5013003452.assetmanagement.db.table.People;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by 5013003452 on 2/16/2017.
 */
public class RegisterAssetActivity extends AppCompatActivity
{
    private Button Save,Cancel,Register;
    private ImageView cameraButton;
    private ImageView editAstID,editSno,editDesc,AstPic;
    private TextView AstID,Sno,Desc,AstOwnr;
    private static String AssetID,SerialNo,Descrptn,Owner,path,path1,Status;
    private long TAGdata;
    private String TAG="Register Asset: ";
    private Socket socket;
    private SaveAssetTask saveassettask;
    String json="";

    private static final int SERVERPORT = 7000;
    private static final String SERVER_IP = "43.88.78.69";

    DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_asset);
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



        Spinner dynamicSpinner = (Spinner) findViewById(R.id.asset_status);
        String[] items = new String[] {"On Shelf", "In Use", "damaged","shared"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
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
       // getAssetOwner();
    }


    private void saveTextActionListener()
    {
        Save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg)
            {
                /*db.addAsset(new Assets(AssetID,SerialNo,Descrptn,path1,Owner,Status,TAGdata,0L,"",""));
                // Reading all Assets
                Log.d("Reading: ", "Reading all assets");
                List<Assets> assets = db.getAllAssets();
                for (Assets asset : assets)
                {
                    String log = "AssetID: " + asset.getAssetID() + " ,SerialNo: " + asset.getSno() + " ,Description: " + asset.getDesc()+ " ,ImagePath: " +asset.getImgPath() + " ,Owner: " + asset.getOwner() + " ,Status: " + asset.getstatus() + " ,Owner TAGdata: " + asset.getOwmTagData() + " ,Allottee TAGdata: " + asset.getAlloTagData() + " ,Allotee: " + asset.getAllotte()+ " ,Category: " + asset.getCategory() ;
                    // Writing shops to log
                    Log.d("Assets Table:: ",log);
                }*/
                saveassettask=new SaveAssetTask();
                saveassettask.execute();

            }
        });

    }

    public class SaveAssetTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                final JSONObject jsonobj=new JSONObject(); // declared locally so that it destroys after serving its purpose
                json="";
                jsonobj.put("Action","Register Asset");
                jsonobj.put("AssetID",AssetID);
                jsonobj.put("SerialNo",SerialNo);
                jsonobj.put("Description",Descrptn);
                // jsonobj.put("image path",path1);
                jsonobj.put("Owner",Owner);
                jsonobj.put("Status",Status);
                jsonobj.put("Owner TAG",TAGdata);
                json=jsonobj.toString();
                Log.d(TAG,"JSON string is: "+json);
          /*  PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(str);*/
                socket = new Socket(SERVER_IP, SERVERPORT);
                // Exmp : Socket socket = new Socket("192.168.0.101", 80);
                OutputStream outToServer = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(json);
                // Exmp : out.writeUTF("TEST");

                socket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAssetActivity.this);
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
            {   // fetch data from the server regarding
                String name="";
              //  try {
                   FetchOwner own=new FetchOwner();
                    Long tag = data.getLongExtra("TAG data",0);
                    try {
                        name = own.execute(tag).get();
                        Log.d(TAG,"Boolean data to be fed is: "+name);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    //  People p=db.getPeople(tag);
                    //   Log.d(TAG,"People details are: "+p.getEmp());
                    TAGdata = tag;
                    //   Owner=p.getEmp();
                    AstOwnr.setText(name);
              //  }

                /*catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        }

    }

    public class FetchOwner extends AsyncTask<Long, Void, String>
    {
        String name;
        @Override
        protected String doInBackground(Long... params)
        {
            try
            {
                Long tag2=params[0];
                final JSONObject jsonobj=new JSONObject(); // declared locally so that it destroys after serving its purpose
                jsonobj.put("Action","Get Person");
                jsonobj.put("Tag Data",tag2);
                json=jsonobj.toString();


                // socket opens
                socket = new Socket(SERVER_IP, SERVERPORT);
                // sending data to server initially
                OutputStream outToServer = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(json);
                // Receive data from server
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while((name = in.readLine()) != null)
                {
                    Log.d(TAG,"Owner name obtained is: "+name);
                }
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return name;
        }
    }



    public void editTextActionListener()
    {
        editAstID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterAssetActivity.this);
                alertDialog.setTitle("Asset ID");
                alertDialog.setMessage("Enter Asset ID");

                final EditText input = new EditText(RegisterAssetActivity.this);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterAssetActivity.this);
                alertDialog.setTitle("Asset Serial no");
                alertDialog.setMessage("Enter Serial no");

                final EditText input = new EditText(RegisterAssetActivity.this);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterAssetActivity.this);
                alertDialog.setTitle("Asset Description");
                alertDialog.setMessage("Give asset description");

                final EditText input = new EditText(RegisterAssetActivity.this);
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
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            String message=data.getStringExtra("TAG ID");
            AstOwnr.setText(message);
        }
    } */

}




