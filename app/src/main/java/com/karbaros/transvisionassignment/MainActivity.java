package com.karbaros.transvisionassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {


    ImageButton imageButton;
    ImageButton imageButton2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent empIntent = new Intent(getBaseContext(),EmployeeActivity.class);
                startActivity(empIntent);

            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getBaseContext(),MapActivity.class);
                startActivity(mapIntent);

            }
        });
    }
}
