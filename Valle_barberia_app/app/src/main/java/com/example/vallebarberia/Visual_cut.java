package com.example.vallebarberia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vallebarberia.cortes.ConexionSqliteHelper;
import com.example.vallebarberia.dialogos.DatePickerFragment;
import com.example.vallebarberia.dialogos.TimePickerFragment;
import com.example.vallebarberia.myAdapter.MyAdapter;
import com.example.vallebarberia.utilidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Visual_cut extends AppCompatActivity implements
        View.OnClickListener{

    Long date;
    TextView tv_total, tv_title_action_bar;
    ImageView go_back;
    EditText et_date;
    ListView list_all_cuts;
    ArrayList<String> list_ids;
    ArrayList<String> list_names;
    ArrayList<String> list_type_cuts;
    ArrayList<String> list_value_cuts;
    ArrayList<String> list_times;
    MyAdapter adapter;
    SimpleDateFormat sdf;

    //Edit text para eliminar el item
    private TextView id_cut, name_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_cut);
        tv_total = findViewById(R.id.tv_total);
        tv_title_action_bar = findViewById(R.id.tv_title_action_bar);
        tv_title_action_bar.setText("Visualizar Cortes");
        go_back = findViewById(R.id.back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

        et_date = findViewById(R.id.et_date);
        list_all_cuts = findViewById(R.id.list_view_cuts);
        et_date.setOnClickListener(this);
        date = System.currentTimeMillis();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        et_date.setHint(sdf.format(date));


        list_all_cuts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                id_cut = (TextView) view.findViewById(R.id.et_id);
                name_client = (TextView) view.findViewById(R.id.et_name);
                int id_cut_register = Integer.parseInt(id_cut.getText().toString());
                String aux_name_client = name_client.getText().toString();

                showAlertDialog(position, id_cut_register, aux_name_client);

            }
        });
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
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = c.getTimeInMillis();
                et_date.setHint(sdf.format(date));

            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void btn_show_cuts(View v){

        addCut();

    }

    public void showAlertDialog(final int position, final int id, String name){
        AlertDialog.Builder delete = new AlertDialog.Builder(Visual_cut.this);

        delete.setTitle("Eliminar corte");
        delete.setMessage("¿Desea eliminar el corte?");
        delete.setIcon(R.drawable.delete);

        delete.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list_ids.remove(position);
                list_names.remove(position);
                list_type_cuts.remove(position);
                list_value_cuts.remove(position);
                list_times.remove(position);
                adapter.notifyDataSetChanged();
                tv_total.setText("Total: " + totalForDay(list_value_cuts));

                deleteCut(id);
            }
        });

        delete.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        delete.show();
    }

    public void addCut(){

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        list_ids = new ArrayList<String>();
        list_names = new ArrayList<String>();
        list_type_cuts = new ArrayList<String>();
        list_value_cuts = new ArrayList<String>();
        list_times = new ArrayList<String>();
        ConexionSqliteHelper connection = new ConexionSqliteHelper(this, "bd_cortes", null, 3);
        SQLiteDatabase db = connection.getReadableDatabase();

        //tener mucho cuidado con las expresiones
        //Al momento de preguntar por fechas y horas, estas deben ir en comilla sencilla
        Cursor cursor = db.rawQuery("SELECT " + Utilidades.FIELD_ID +
                ", time(" +Utilidades.FIELD_DATE +"), " +
                Utilidades.FIELD_NAME + ", " +
                Utilidades.FIELD_CUT_TYPE + ", " +
                Utilidades.FIELD_VALUE + " from " +
                Utilidades.CUT_TABLE + " WHERE date(" + Utilidades.FIELD_DATE +") == "+
                "'" + et_date.getHint() + "' ORDER BY " +
                "time(" +Utilidades.FIELD_DATE +") ASC", null);

        if (cursor.getCount() == 0){
            Toast.makeText(this, "No hay cortes para mostrar", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                list_ids.add(cursor.getString(0));
                list_times.add(cursor.getString(1));
                list_names.add(cursor.getString(2));
                list_type_cuts.add(cursor.getString(3));
                list_value_cuts.add(cursor.getString(4));

            }

            adapter = new MyAdapter(this, list_ids, list_names, list_type_cuts, list_value_cuts, list_times);
            list_all_cuts.setAdapter(adapter);

            tv_total.setText("Total: " + totalForDay(list_value_cuts));
            tv_total.setVisibility(View.VISIBLE);

        }

        db.close();
    }

    //Here we will delete the item if the button "Si" is pressed on the dialogfragment
    public void deleteCut(int id){
        ConexionSqliteHelper connection = new ConexionSqliteHelper(this, "bd_cortes", null, 3);
        SQLiteDatabase db = connection.getWritableDatabase();
        db.delete(Utilidades.CUT_TABLE, Utilidades.FIELD_ID + " = " + id, null);
        Toast.makeText(this, "Se ha eliminado el corte satisfactoriamente", Toast.LENGTH_SHORT).show();
        db.close();
    }

    //Here we will to calculate the total for each day
    public double totalForDay(ArrayList<String> values){

        double totalDay = 0;
        int[] int_values = new int[values.size()];

        for (int i = 0; i < values.size(); i++) {
            int_values[i] = Integer.parseInt(values.get(i));
            totalDay += int_values[i];
        }

        return totalDay;
    }

}