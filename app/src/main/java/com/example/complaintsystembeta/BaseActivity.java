package com.example.complaintsystembeta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    Snackbar snackbar;
    ProgressDialog dialog;


    public void showProgressDialogue(String title, String message){
        dialog = new ProgressDialog(BaseActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    public void dissmissProgressDialogue(){
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
    public boolean checkAccountNumber(String account){
        String[] accountPortions = account.split("-");
        if(accountPortions.length != 3){
            return false;
        }if(accountPortions[0].length() == 3 && accountPortions[1].length() == 3 && accountPortions[2].length() == 3){
                Log.d(TAG, "checkCNICFormat: " +  String.valueOf(accountPortions[0] + accountPortions[1] + accountPortions[2]));
            return true;
        }

        return false;
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
