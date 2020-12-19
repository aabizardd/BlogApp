package id.ac.id.telkomuniversity.tass.blogapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import id.ac.id.telkomuniversity.tass.blogapp.R;

public class LoadDialog {

    private Activity activity;
    private AlertDialog dialog;

    LoadDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();




    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
