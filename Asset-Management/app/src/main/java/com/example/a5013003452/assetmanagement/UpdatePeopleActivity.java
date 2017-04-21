package com.example.a5013003452.assetmanagement;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a5013003452.assetmanagement.R;
import com.example.a5013003452.assetmanagement.RegisterPerson.RegisterPersonView;
import com.example.a5013003452.assetmanagement.db.table.People;
import com.example.a5013003452.assetmanagement.db.DBHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by 5013003452 on 3/24/2017.
 */

public class UpdatePeopleActivity extends AppCompatActivity
{
    private Button Save;
    private ImageView cameraButton;
    private ImageView editGID,editName;
    private ImageView profilepic;
    private TextView GID,ProfileName;
    private RelativeLayout layout;
    private static String profile,GlobalID,path,path1;
    private Long EmpID,TAGdata;
    private People p;
    private String TAG="Update Person View";
    DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_user);
        // get the people details
        Bundle bundle = getIntent().getExtras();
        TAGdata = bundle.getLong("tag data");
        p=db.getPeople(TAGdata);


        layout = (RelativeLayout) findViewById(R.id.GID_container);
        profilepic = (ImageView) findViewById(R.id.profile_pic);
        cameraButton = (ImageView) findViewById(R.id.button_camera);
        editGID = (ImageView) findViewById(R.id.button_edit);
        editName = (ImageView) findViewById(R.id.button_edit2);
        GID = (TextView) findViewById(R.id.GID_text);
        ProfileName = (TextView) findViewById(R.id.profile_name);
        Save = (Button) findViewById(R.id.button_save);
        /*Bundle extras = getIntent().getExtras();
        TAGdata = extras.getLong("TAG data");
        Log.d(TAG, "TAGdata is: " + TAGdata);*/

        GlobalID=Long.toString(p.getGID());
        GID.setText(GlobalID);
        profile=p.getEmp();
        ProfileName.setText(profile);
        path1=p.getImgPath();
        profilepic.setImageBitmap(BitmapFactory.decodeFile(path1));
       // TAGdata=p.getTagId();

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
                db.UpdatePeople(TAGdata,Long.parseLong(GlobalID),profile,path1);
                db.UpdateAssetsOwnerName(profile,TAGdata);
                db.UpdateAssetsAlloteeName(profile,TAGdata);

                // Reading all People
                Log.d("Reading: ", "Reading all people");
                List<People> peoples = db.getAllPeople();
                for (People people : peoples)
                {
                    String log = "GID: " + people.getGID() + " ,TAGID: " + people.getTagId() + " ,Name: " + people.getEmp()+ " ,ImagePath: " +people.getImgPath();
                    // Writing shops to log
                    Log.d("People Table:: ", log);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePeopleActivity.this);
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
                    profilepic.setImageBitmap(bmpPic1);
                    path = android.os.Environment.getExternalStorageDirectory() + File.separator + "AssetManager"+File.separator+"People"+File.separator;
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

        }
    }

    private void editTextActionListener()
    {
        editGID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                // tv.setText("Welcome to android");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdatePeopleActivity.this);
                alertDialog.setTitle("Global Employee ID");
                alertDialog.setMessage("Enter GID");

                final EditText input = new EditText(UpdatePeopleActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        GlobalID = input.getText().toString();
                        GID.setText(GlobalID);
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

        editName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdatePeopleActivity.this);
                alertDialog.setTitle("Employee Name");
                alertDialog.setMessage("Enter Name");

                final EditText input = new EditText(UpdatePeopleActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        profile = input.getText().toString();
                        ProfileName.setText(profile);
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

}
