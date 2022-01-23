package com.example.flight_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginCtrl extends AppCompatActivity {
    public static final String MyPREFERENCES = "my_session" ;

    String email_input;
    String mdp_input;

    static String full_name;
    static String email;

    TextView success_txt;

    static SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        sharedpreferences = getSharedPreferences(LoginCtrl.MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences!=null){
            full_name = sharedpreferences.getString("full name","");
            email = sharedpreferences.getString("email","");
            if(full_name!=null && full_name!="" && email!=null && email!="") {
                this.finish();
                Intent switchActivityIntent = new Intent(this, Main_search_view.class);
                startActivity(switchActivityIntent);
            }
        }
    }



    public void login(View view) {
        set_inputs_values();
        Set_default_colors();
        if (email_input.isEmpty() | mdp_input.isEmpty()) {
            snackbar_exceptions("Champ vide !");
            LinearLayout layout = (LinearLayout) findViewById(R.id.linear_login_layout);
            for (int i = 0; i < layout.getChildCount(); i++) {
                View v = layout.getChildAt(i);
                if (v instanceof EditText) {
                    if (((EditText) v).getText().toString().isEmpty()) {
                        v.setBackgroundTintList(ColorStateList.valueOf(Color.argb(203,235,55,87)));
                    }
                }
            }

        }else{
            User user = new User();
            user.setEmail(email_input);
            user.setMdp(mdp_input);
            if (User.is_email_valid(user.getEmail())) {
                Users_table user_table = new Users_table(this);
                user_table.createDatabase();
                user_table.open();
                if(Users_table.login(user)==null) {
                    snackbar_exceptions("Infos incorrect !");
                }else{
                    create_session(user);
                    user_table.close();
                    main_page();
                }
            } else {
                ((EditText) findViewById(R.id.email_login_input)).setTextColor(Color.argb(203,235,55,87));
                ((EditText) findViewById(R.id.email_login_input)).setBackgroundTintList(ColorStateList.valueOf(Color.argb(203,235,55,87)));
                snackbar_exceptions("email incorrect !");
            }
        }
    }

    public void Set_default_colors() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_login_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof EditText) {
                v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(156, 156, 156)));
            }
        }
    }

    public void set_inputs_values(){
        email_input = ((EditText) findViewById(R.id.email_login_input)).getText().toString();
        mdp_input = ((EditText) findViewById(R.id.mdp_login_input)).getText().toString();
    }

    public void go_signin(View view) {
        startActivity(new Intent(LoginCtrl.this, SigninCtrl.class));
    }
        public void create_session(User user){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("id", user.getId());
        editor.putString("full name", Character.toUpperCase(user.getFname().charAt(0)) + user.getFname().substring(1)+" "+user.getLname().toUpperCase());
        editor.putString("email", user.getEmail());
        editor.commit();
    }

    public void main_page(){
        this.finish();
        startActivity(new Intent(LoginCtrl.this, Main_search_view.class));
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