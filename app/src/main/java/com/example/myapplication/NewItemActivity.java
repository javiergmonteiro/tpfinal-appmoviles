package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

import com.example.myapplication.databases.DatabaseHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class NewItemActivity extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private File output = null;

    DatabaseHelper helper;
    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_NAME = "key_username";
    private SharedPreferences mySharedPreferences;
    private String imageName = "";
    private File mypath;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        helper = new DatabaseHelper(this);
        final SQLiteDatabase db = helper.open();

        mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        final String author = mySharedPreferences.getString(KEY_NAME, null);
        final Date currentTime = Calendar.getInstance().getTime();
        final ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("momentosImages", Context.MODE_PRIVATE);
        imageName = author + "-" + currentTime + ".jpg";
        mypath = new File(directory, imageName);

        final Activity activity = this;

        Button photoButton = (Button) this.findViewById(R.id.button_new_item);
        Button saveMoment = (Button) this.findViewById(R.id.save_moment);
        Button cancelMoment = (Button) this.findViewById(R.id.cancel_moment);
        final EditText description = this.findViewById(R.id.moment_description);
        final EditText tags = this.findViewById(R.id.moment_tags);
        final Location location;

        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);


        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        else{
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        final String lat = Double.toString(location.getLatitude());
        final String lng =  Double.toString(location.getLongitude());
        //Toast.makeText(this,"latitud: "+lat , Toast.LENGTH_LONG).show();



        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        cancelMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String mydescription = description.getText().toString();
                String mytags = tags.getText().toString();
                String myimage = mypath.toString();

                String query = "INSERT INTO moments (user,description,tags,image,timestamp,altitud,latitud) VALUES ('"+author+"','"+mydescription+"','"+mytags+"','"+myimage+"','"+currentTime+"','"+lat+"','"+lng+"')";
                try{
                    db.execSQL(query);
                    Toast.makeText(getApplicationContext(), "Momento guardado!", Toast.LENGTH_LONG).show();
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "No se pudo ejecutar la query", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                finally{
                    db.close();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                //output = new File(dir,"CameraContentDemo.jpeg");
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                photo.compress(Bitmap.CompressFormat.PNG,100,fos);
            }
            catch (Exception e){
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            ImageView myimage = findViewById(R.id.moment_image);
            myimage.setImageBitmap(BitmapFactory.decodeFile(mypath.getAbsolutePath()));
        }
    }
}

