package com.example.vallebarberia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vallebarberia.cortes.ConexionSqliteHelper;
import com.example.vallebarberia.dialogos.DatePickerFragment;
import com.example.vallebarberia.dialogos.TimePickerFragment;
import com.example.vallebarberia.utilidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddCut extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    Spinner spinner;
    EditText et_date;
    Long date;
    String cut_type;
    TextView name;
    int cost = 0;
    ImageView go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cut);

        et_date = findViewById(R.id.et_date);
        et_date.setOnClickListener(this);
        name = findViewById(R.id.name);
        go_back = findViewById(R.id.back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });
        //====================Spinner===========================
        spinner = findViewById(R.id.spn_cuts);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cuts_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //=======================Fecha===================================
        date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        et_date.setHint(sdf.format(date));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_date:
                showDatePickerDialog();
                break;
        }
    }


    public void showDatePickerDialog(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth, hourOfDay, minute);
                        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date = c.getTimeInMillis();
                        et_date.setHint(sdf3.format(date));
                    }
                });
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        cut_type = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void btn_save_cut(View view){


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //==================Base de datos======================================
        ConexionSqliteHelper conection = new ConexionSqliteHelper(this, "bd_cortes",
                null, 3);

        SQLiteDatabase cortes = conection.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (spinner.getSelectedItem().toString().equals("Normal")){
            cost = 10000;
        }
        else if(spinner.getSelectedItem().toString().equals("Normal + Barba")){
            cost = 12000;
        }


        values.put(Utilidades.FIELD_CUT_TYPE, cut_type);
        values.put(Utilidades.FIELD_NAME, name.getText().toString());
        values.put(Utilidades.FIELD_VALUE, cost);
        values.put(Utilidades.FIELD_DATE, sdf2.format(date));

        Long idResultante = cortes.insert(Utilidades.CUT_TABLE, Utilidades.FIELD_ID, values);

        Toast.makeText(this, "Se ha agregado el corte satisfactoriamente: " + idResultante,  Toast.LENGTH_LONG).show();

        cortes.close();
    }


}