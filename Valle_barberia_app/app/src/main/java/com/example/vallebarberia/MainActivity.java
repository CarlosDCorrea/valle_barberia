package com.example.vallebarberia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn_add_cut(View view){

        Intent i = new Intent(this, AddCut.class);
        startActivity(i);

    }

    public void btn_visualice_cut(View view){

        Intent i = new Intent(this, Visual_cut.class);
        startActivity(i);

    }

}
