package com.example.complaintsystembeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    Snackbar snackbar;
    ProgressDialog dialog;

    public void showProgressDialogue(String title, String message){
        dialog = new  ProgressDialog(BaseActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    public void dissmissProgressDialogue(String title, String message){
       dialog.dismiss();
    }
    public  void showSnackBar(String dialogue, String color){
        snackbar =Snackbar.make((findViewById(android.R.id.content)), dialogue, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        view.setBackgroundColor(Color.RED);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
    public boolean checkCNICFormat(String userName) {
        String[] cnicPortions = userName.split("-");
        if(cnicPortions.length != 3){
            return false;
        }
        if(cnicPortions[0].length() == 5 && cnicPortions[1].length() == 7 && cnicPortions[2].length() == 1){
            Log.d(TAG, "checkCNICFormat: " +  String.valueOf(cnicPortions[0] + cnicPortions[1] + cnicPortions[2]));
            return true;
        }
        return false;
    }
    public boolean checkPasswordType(String passwordS) {
        return passwordS.length() > 7;
    }

}
