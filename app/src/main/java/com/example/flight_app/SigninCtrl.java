package com.example.flight_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.controlers.Users_table;
import com.example.moduls.User;
import com.google.android.material.snackbar.Snackbar;

public class SigninCtrl extends AppCompatActivity {

    String nom_input;
    String prenom_input;
    String email_input;
    String mdp_input;
    String Cfmdp_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_page);
    }



    public void submit(View view) {
        set_inputs_values();
        Set_default_colors();
        if (nom_input.isEmpty() | prenom_input.isEmpty() | email_input.isEmpty() | mdp_input.isEmpty() | Cfmdp_input.isEmpty()) {
            snackbar_exceptions("Champ vide !");

            LinearLayout layout = (LinearLayout) findViewById(R.id.inputs_signin_layout);
            for (int i = 0; i < layout.getChildCount(); i++) {
                View v = layout.getChildAt(i);
                if (v instanceof EditText) {
                    if (((EditText) v).getText().toString().isEmpty()) {
                        v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(203,235,55,87)));
                    }
                }
            }

        } else {
            if (mdp_input.matches(Cfmdp_input)) {
                User user = new User();
                user.setEmail(email_input);
                user.setFname(prenom_input);
                user.setLname(nom_input);
                user.setMdp(mdp_input);
                if (User.is_email_valid(user.getEmail())) {
                    Users_table user_table = new Users_table(this);
                    user_table.createDatabase();
                    user_table.open();
                        if(user_table.signin(user) == -2){
                            ((EditText) findViewById(R.id.email_login_input)).setTextColor(Color.argb(203,235,55,87));
                            ((EditText) findViewById(R.id.email_login_input)).setBackgroundTintList(ColorStateList.valueOf(Color.argb(203,235,55,87)));
                            snackbar_exceptions("email already exist !");
                        }else{
                            this.finish();
                            startActivity(new Intent(SigninCtrl.this, LoginCtrl.class));
                        }
                    user_table.close();
                } else {
                    ((EditText) findViewById(R.id.email_login_input)).setTextColor(Color.argb(203,235,55,87));
                    ((EditText) findViewById(R.id.email_login_input)).setBackgroundTintList(ColorStateList.valueOf(Color.argb(203,235,55,87)));
                    snackbar_exceptions("email incorrect !");
                }
            } else {
                ((EditText) findViewById(R.id.Cfmdp_input)).setTextColor(Color.argb(203,235,55,87));
                ((EditText) findViewById(R.id.Cfmdp_input)).setBackgroundTintList(ColorStateList.valueOf(Color.argb(203,235,55,87)));
                snackbar_exceptions("mdp de conf incorrect !" );
            }
        }
    }

    public void Set_default_colors() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.inputs_signin_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof EditText) {
                ((EditText) v).setTextColor(Color.rgb(255, 255, 255));
                v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(156, 156, 156)));
            }
        }
    }

    public void set_inputs_values(){
        nom_input = ((EditText) findViewById(R.id.nom_input)).getText().toString();
        prenom_input = ((EditText) findViewById(R.id.prenom_input)).getText().toString();
        email_input = ((EditText) findViewById(R.id.email_input)).getText().toString();
        mdp_input = ((EditText) findViewById(R.id.mdp_input)).getText().toString();
        Cfmdp_input = ((EditText) findViewById(R.id.Cfmdp_input)).getText().toString();
    }


    public void snackbar_exceptions(String msg){
        final Snackbar snackbar = Snackbar.make((ConstraintLayout) findViewById(R.id.insert_layout),"", Snackbar.LENGTH_INDEFINITE)
                .setAnimationMode(Snackbar.ANIMATION_MODE_FADE);

        View customSnackView = getLayoutInflater().inflate(R.layout.custom_err_snackbar, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        TextView error_msg = (TextView) customSnackView.findViewById(R.id.textView1);
        error_msg.setText(msg);

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);

        Button bGotoWebsite = customSnackView.findViewById(R.id.gotoWebsiteButton);
        bGotoWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbarLayout.addView(customSnackView, 0);
        snackbar.show();
    }

}