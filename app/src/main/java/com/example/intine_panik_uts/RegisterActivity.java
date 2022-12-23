package com.example.intine_panik_uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
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


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnSubmit;
    private TextView textViewHaveAccount;
    private EditText textTanggalLahir;

    DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("userDB");
    private String currentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hide My App Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        textViewHaveAccount = findViewById(R.id.textViewHaveAccount);
        textTanggalLahir = findViewById(R.id.teksTanggalLahir);

        textTanggalLahir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    textTanggalLahir.setEnabled(false);
                    DialogFragment dateFragment = new DatePickerFragment();
                    dateFragment.show(getSupportFragmentManager(), "date-picker");
                }
                else{
                    textTanggalLahir.setEnabled(true);
                }
            }
        });

        textViewHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(editTextUsername.getText());
                String password = String.valueOf(editTextPassword.getText());
                addUser(username, password, textTanggalLahir.getText().toString());
                finish();
            }
        });
    }

    private void addUser(String username, String password, String date) {
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        currentID = currentData.getKey();
                    }
                }
//                Toast.makeText(RegisterActivity.this, currentID, Toast.LENGTH_SHORT).show();
                //increment function
                String incrementedID = String.valueOf(Integer.parseInt(currentID)+1);

                databaseReferenceUser.child(incrementedID).child("username").setValue(username);
                databaseReferenceUser.child(incrementedID).child("password").setValue(password);
                databaseReferenceUser.child(incrementedID).child("date").setValue(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Error! "+error, Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(this, "Berhasil menambahkan user", Toast.LENGTH_SHORT).show();
    }

    //    Date Fragment Rule
    public void processDatePickerResult(int day, int month, int year) {
        String day_string = Integer.toString(day);
        String month_string = Integer.toString(month + 1);
        String year_string = Integer.toString(year);

        String dateMessage = day_string + "/" + month_string + "/" + year_string;
        textTanggalLahir.setText(dateMessage);
    }
}