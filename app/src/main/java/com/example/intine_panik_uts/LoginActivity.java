package com.example.intine_panik_uts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnSubmit;
    private TextView textViewRegister;

    private String getLoggedInUserTanggalLahir;
    private boolean userIsLoggedIn = false;
    public static String getUsername;

    private SharedPreferences mSharedPref;
    private final String sharedPrefFile = "com.example.intine_panik_uts";
    private final String LOGIN_KEY = "loginkey";
    private final String TANGGAL_LAHIR_KEY = "tanggalkey";
    private final String USERNAME_KEY = "usernamekey";

    DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("userDB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hide My App Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        textViewRegister = findViewById(R.id.editTextRegister);

        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        userIsLoggedIn = mSharedPref.getBoolean(LOGIN_KEY, false);
        getLoggedInUserTanggalLahir = mSharedPref.getString(TANGGAL_LAHIR_KEY, "");
        getUsername = mSharedPref.getString(USERNAME_KEY, "");

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if (userIsLoggedIn == true){
            intent.putExtra("pref_Kategori","gaming");
            intent.putExtra("pref_TanggalLahir",getLoggedInUserTanggalLahir);
            intent.putExtra("logged_in",true);
            Toast.makeText(LoginActivity.this, "username: "+getUsername, Toast.LENGTH_SHORT).show();
            Toast.makeText(LoginActivity.this, "tgl: "+getLoggedInUserTanggalLahir, Toast.LENGTH_SHORT).show();
            startActivityForResult(intent,1);
        }

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(editTextUsername.getText());
                String password = String.valueOf(editTextPassword.getText());

                databaseReferenceUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot currentData : snapshot.getChildren()){
                                if (currentData.child("username").getValue().toString().equals(username) && currentData.child("password").getValue().toString().equals(password)){
                                    Toast.makeText(LoginActivity.this, "Berhasil login", Toast.LENGTH_SHORT).show();
                                    userIsLoggedIn = true;
                                    saveLoginStatus();

                                    getLoggedInUserTanggalLahir = currentData.child("date").getValue().toString();
                                    saveTanggalLahir();

                                    getUsername = currentData.child("username").getValue().toString();;
                                    saveUsername();

                                    String pilihanKategori = "gaming";
                                    String tanggalLahir = getLoggedInUserTanggalLahir;

                                    intent.putExtra("pref_Kategori",pilihanKategori);
                                    intent.putExtra("pref_TanggalLahir",tanggalLahir);
                                    intent.putExtra("logged_in",true);
                                    startActivityForResult(intent,1);
                                    break;
                                }else{
                                    userIsLoggedIn = false;
                                }
                            }
                            if (userIsLoggedIn == false){
                                //alert code
                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                alertDialog.setTitle("Error Login");
                                alertDialog.setMessage("Username atau Password salah");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

//                if (userIsLoggedIn){
//                    String pilihanKategori = "gaming";
//                    String tanggalLahir = getLoggedInUserTanggalLahir;
//                    Toast.makeText(LoginActivity.this, "UserExist= "+userIsLoggedIn, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(LoginActivity.this, "tgl Lahir= "+tanggalLahir, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("pref_Kategori",pilihanKategori);
//                    intent.putExtra("pref_TanggalLahir",tanggalLahir);
//                    startActivity(intent);
//                }if(userIsLoggedIn == false){
//                    //alert code
//                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
//                    alertDialog.setTitle("Error Login");
//                    alertDialog.setMessage("Username atau Password salah");
//                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    alertDialog.show();
//                }
            }
        });
    }

    private void saveLoginStatus(){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(LOGIN_KEY, userIsLoggedIn);
        editor.apply();
    }

    private void saveTanggalLahir(){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TANGGAL_LAHIR_KEY, getLoggedInUserTanggalLahir);
        editor.apply();
    }

    private void saveUsername(){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(USERNAME_KEY, getUsername);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                userIsLoggedIn = false;
                saveLoginStatus();
                Toast.makeText(LoginActivity.this, "Berhasil Log Out", Toast.LENGTH_SHORT).show();
            }
        }
    }
}