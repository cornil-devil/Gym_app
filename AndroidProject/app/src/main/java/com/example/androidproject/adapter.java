package com.example.androidproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class adapter extends ArrayAdapter<members_bean> {
    ImageView imageView;
    public adapter(@NonNull Context context, ArrayList<members_bean> members) {
        super(context,0,(List<members_bean>)  members);
    }

    @NonNull
    @Override
    public View getView(int position,View row,ViewGroup parent) {
        members_bean current_member =getItem(position);

        if(row == null){
            row = LayoutInflater.from(getContext()).inflate(R.layout.row, parent,false);
        }

        imageView =row.findViewById(R.id.imageView);
        TextView txt_name1 = row.findViewById(R.id.txt_name1);
        TextView txt_visit1 = row.findViewById(R.id.txt_visit1);
        TextView txt_code1 = row.findViewById(R.id.txt_code1);
        TextView txt_address1 = row.findViewById((R.id.txt_address1));
        int id;


        String testFilePath = current_member.getPhoto();
        try{
            setImageFromURI(testFilePath, 500, imageView);
        }catch (Exception e){
            //in case image doesn't exist

        }


        /*imageView.setImageResource(current_member.getPhoto());*/
        txt_name1.setText(current_member.getName()+"");
        txt_visit1.setText("Visits: "+current_member.getVisits()+"");
        txt_code1.setText(current_member.getCode()+"");
        txt_address1.setText(current_member.getAddress()+"");
        id = current_member.getId();



        return row;

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
