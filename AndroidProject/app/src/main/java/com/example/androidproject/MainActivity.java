package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    int REQUEST_SCAN_CODE = 1;

    ImageView imageView;

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        VideoView videoview = (VideoView) findViewById(R.id.videoView2);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
        videoview.setVideoURI(uri);
        videoview.start();


        DatabaseHelper db = new DatabaseHelper(this);





    }

    public void OnClick (View V){
        Intent myIntent = new Intent(this, NewMemberClass.class);
        this.startActivity(myIntent);

    }
    public void All_members(View V){
        Intent myIntent = new Intent(this, all_members.class);
        this.startActivity(myIntent);
    }
    public  void check_in (View V){


        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "PRODUCT_CODE");
        startActivityForResult(intent, REQUEST_SCAN_CODE);


//        alertDialog.setCanceledOnTouchOutside(false);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        int check = 0;
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_SCAN_CODE && resultCode == RESULT_OK) {





            String contents = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            // Toast.makeText(this,""+contents,Toast.LENGTH_LONG).show();
            DatabaseHelper db = new DatabaseHelper(this);
            int l_id = db.getStudentCount();
            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.check_in,null);

            final ImageView imageView = view.findViewById(R.id.image);
            final  TextView txt_name1 = view.findViewById(R.id.txt_name1);
            final TextView txt_visit1 = view.findViewById(R.id.txt_visit1);
            final TextView txt_code1 = view.findViewById(R.id.txt_code1);
            final TextView txt_age = view.findViewById((R.id.txt_age));
            final TextView txt_dob = view.findViewById((R.id.txt_dob));
            final Button btn_check_in = view.findViewById(R.id.btn_check_in);
            alert.setView(view);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            for(int i = 1;i<=l_id;i++){
                try{
                if(String.valueOf(db.getMember(Long.valueOf(i)).getCode()).equals(contents)){

                    final members_bean get_member = db.getMember(Long.valueOf(i));
                    check = 1;



                    try{
                        setImageFromURI(get_member.getPhoto(), 500, imageView);
                    }catch (Exception e){
                        //in case image doesn't exist

                    }

                        txt_name1.setText(get_member.getName());
                        txt_dob.setText(get_member.getDob().toString()+"");

                        String dob = get_member.getDob();

                        String[] dob1 = dob.split("-");

                        txt_age.setText(2020-Integer.parseInt(dob1[2])+"");
                        txt_visit1.setText("Visit: "+get_member.getVisits()+"");
                        txt_code1.setText(get_member.getCode()+"");

                        if(get_member.getStatus() == 1){
                            btn_check_in.setText("Check Out");
                        }else{
                            btn_check_in.setText("Check In");
                        }

                    btn_check_in.setOnClickListener(new View.OnClickListener() {
                            @Override



                            public void onClick(View v) {
                                DatabaseHelper db = new DatabaseHelper(context);
                                if(get_member.getStatus() == 0){

                                    get_member.setStatus(1);
                                    db.updateStudent(get_member);

                                    alertDialog.dismiss();
                                }else{
                                    get_member.setStatus(0);


                                    get_member.setVisits(get_member.getVisits()+1);
                                    db.updateStudent(get_member);
                                    alertDialog.dismiss();
                                }

                            }
                        });




                }else{
                 check = 0;
                 alertDialog.dismiss();
                }
                }catch (RuntimeException e){

                }
            }


            alertDialog.show();
            //code.setText(contents);
            if(check ==1 ){
                Toast.makeText(this,"Barcode not found, try again please !",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }

        }  else if (resultCode == RESULT_CANCELED) {


        }


    }


    public void setImageFromURI(String filePath, int scale, ImageView imageView){
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(filePath);
        Matrix mat = new Matrix();
        mat.postRotate(Integer.parseInt(String.valueOf(90)));
        Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), mat, true);
        imageView.setImageBitmap(bMapRotate);
    }
}
