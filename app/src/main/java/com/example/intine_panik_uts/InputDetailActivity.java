package com.example.intine_panik_uts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;

public class InputDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button btnSubmit;
    EditText textTanggalLahir;
    String textSpinnerPreferensi;
    Spinner spinnerPreferensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_detail);

        spinnerPreferensi = findViewById(R.id.spinnerPreferensi);
        btnSubmit = findViewById(R.id.btnSubmit);
        textTanggalLahir = findViewById(R.id.teksTanggalLahir);

        //set array to an adapter to the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.labels_array, android.R.layout.simple_spinner_item);
        spinnerPreferensi.setAdapter(adapter);

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
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pilihanKategori = String.valueOf(spinnerPreferensi.getSelectedItem());
                String tanggalLahir = String.valueOf(textTanggalLahir.getText());
                Intent intent = new Intent(InputDetailActivity.this,MainActivity.class);
                intent.putExtra("pref_Kategori",pilihanKategori);
                intent.putExtra("pref_TanggalLahir",tanggalLahir);
                startActivity(intent);
            }
        });
    }

    //    Date Fragment Rule
    public void processDatePickerResult(int day, int month, int year) {
        String day_string = Integer.toString(day);
        String month_string = Integer.toString(month + 1);
        String year_string = Integer.toString(year);

        String dateMessage = day_string + "/" + month_string + "/" + year_string;
        textTanggalLahir.setText(dateMessage);
    }

    //Spinner Rules
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        textSpinnerPreferensi = adapterView.getItemAtPosition(i).toString();
        showSpinnerText();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showSpinnerText(){
        if (!Objects.equals(textSpinnerPreferensi, "Hadir Tepat Waktu")){
            //
        }
        else{
            //
        }
    }
}