package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class NewMemberClass extends AppCompatActivity {
    int REQUEST_SCAN_CODE = 1;
    int REQUEST_GRAB_PICTURE_CODE = 2;
    int REQUEST_TAKE_PHOTO = 3;
    ImageView photu;
    EditText fname,sname,dob,address,city,province,barcode;
    TextView txt_barcode;
    String currentPhotoPath;
    String testFilePath = "";
    Context context = this;
    String photo = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member_class);

        fname = (EditText) findViewById(R.id.txt_fname);
        sname = (EditText) findViewById(R.id.txt_sname);
        dob = (EditText) findViewById(R.id.txt_dob);
        address = (EditText) findViewById(R.id.txt_address1);
        city = (EditText) findViewById(R.id.txt_city);
        province = (EditText) findViewById(R.id.txt_province);
        barcode = (EditText) findViewById(R.id.txt_barcode);
        photu = (ImageView) findViewById(R.id.photu);
        txt_barcode = (TextView) findViewById(R.id.txt_barcode);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        //QED: create the image from the camera and save it and in order to be use later on
        try{
            setImageFromURI(testFilePath, 1000, photu);
        }catch (Exception e){
            //in case image doesn't exist
            Toast.makeText(this, "Sorry, this path doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick2(View v) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();


    }

    public void scan(View v){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
        startActivityForResult(intent, REQUEST_SCAN_CODE);

    }

    public void register(View V){

        if(fname.getText().toString().equals("") || sname.getText().toString().equals("") || address.getText().toString().equals("") || city.getText().toString().equals("") || dob.getText().toString().equals("") || barcode.getText().toString().equals("") || photo.contains(" ")){
            Toast.makeText(this,"Validate all the fields !!",Toast.LENGTH_SHORT).show();
        }else {

            DatabaseHelper db = new DatabaseHelper(this);

            int id = (int) db.insertMember(new members_bean(fname.getText().toString() + " " + sname.getText().toString(), address.getText().toString(), city.getText().toString(), dob.getText().toString(), barcode.getText().toString(), 0, photo, 0));

            for (members_bean m : db.getAllMembers()) {
                Log.d("Dekh lo", m.toString());
            }

            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_SCAN_CODE && resultCode == RESULT_OK) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            EditText barcode = findViewById(R.id.txt_barcode);
            barcode.setText(contents);
        } else if (requestCode == REQUEST_GRAB_PICTURE_CODE && resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photu.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setImageFromURI(currentPhotoPath, 500, photu); //neat function to set an image from a file path and also crop and scale
            photo = currentPhotoPath;
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled Successfully", Toast.LENGTH_SHORT).show();
        }
    }



    public void takepicture(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("error ", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //send an URI to the camera in order to save the image in the given path
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.androidproject",
                        photoFile);

                //debug purpose: to view the litteral path
                Log.d("PHOTOURI", photoFile.toString());
                Log.d("PHOTOFILEPATH", currentPhotoPath);
                DatabaseHelper db = new DatabaseHelper(context);
                //important we need to put the photouri when startin the activity
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    public void grabPicture(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, REQUEST_GRAB_PICTURE_CODE);
    }


    public static  Bitmap cropAndScale (Bitmap source, int scale){
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }

    public void setImageFromURI(String filePath, int scale, ImageView imageView){
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(filePath);
        Matrix mat = new Matrix();
        mat.postRotate(Integer.parseInt(String.valueOf(90)));
        Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), mat, true);
       imageView.setImageBitmap(bMapRotate);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
