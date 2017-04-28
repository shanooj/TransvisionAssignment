package com.karbaros.transvisionassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ViewEmpActivity extends AppCompatActivity {

    ListView listView;
    List<Employee> employeeList;
    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emp);

        listView = (ListView) findViewById(R.id.lvDetails);
        dbAdapter = new DBAdapter(getBaseContext());
        employeeList = dbAdapter.selectData();


    }

    @Override
    protected void onStart() {
        super.onStart();
        EmployeAdapter employeAdapter = new EmployeAdapter(this,employeeList);
        listView.setAdapter(employeAdapter);
    }
}
