package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class all_members extends AppCompatActivity {
    adapter adapter1;
    Context context;
    ArrayList<members_bean> contacts = new ArrayList<members_bean>();
    DatabaseHelper db = new DatabaseHelper(this);
    ListView lv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members);


        context = this;
        contacts = db.getAllMembers();

        adapter1 = new adapter(this,contacts);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter1);


        for(members_bean m : db.getAllMembers()){
            Log.d("Re"+m.getId(),m.toString());
        }

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){


            @Override

            public boolean onItemLongClick (AdapterView<?> parent, View V, final int position, long id){


                final  int pos = position;
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(all_members.this);
                View view = getLayoutInflater().inflate(R.layout.row, null);
                alertDialogBuilder.setView(view);


                adapter1 = new adapter(getApplicationContext(),contacts);

                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(adapter1.getItem(pos).getStatus() == 0){
                                    String msg = "";
                                    db.hardDeleteMember(adapter1.getItem(pos).getId());
                                    adapter1.remove(adapter1.getItem(pos));
                                    adapter1.notifyDataSetChanged();
                                    arg0.dismiss();
                                    lv.setAdapter(adapter1);
                                }else{
                                   Toast.makeText(getApplicationContext(),"Client is still in the gym !",Toast.LENGTH_LONG).show();;
                                }

                            }
                        });

                //4. Set negative button
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                return true;


            }
        });

    }
}
