package com.karbaros.transvisionassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EmployeeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

    }

    public void navigate(View view) {
       // Toast.makeText(getBaseContext(),"clicked",Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.btnadd:
                Intent addIntent = new Intent(getBaseContext(), AddEmpActivity.class);
                startActivity(addIntent);
                break;
            case R.id.btnview:
                Intent viewIntent = new Intent(getBaseContext(), ViewEmpActivity.class);
                startActivity(viewIntent);
                break;
            case R.id.btnexport:
                Intent exportIntent = new Intent(getBaseContext(), ExportDataActivity.class);
                startActivity(exportIntent);
                break;
            case R.id.btnimport:
                Intent importIntent = new Intent(getBaseContext(), ImportDataActivity.class);
                startActivity(importIntent);
                break;
        }
    }
}
