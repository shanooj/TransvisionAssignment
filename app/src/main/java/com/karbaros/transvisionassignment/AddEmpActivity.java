package com.karbaros.transvisionassignment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEmpActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutName;
    TextInputLayout textInputLayoutAddress;
    TextInputLayout textInputLayoutAge;
    TextInputLayout textInputLayoutPhone;
    TextInputLayout textInputLayoutGender;
    TextInputLayout textInputLayoutGrade;
    TextInputLayout textInputLayoutPlace;
    TextInputLayout textInputLayoutDob;

    EditText etName;
    EditText etAddress;
    EditText etAge;
    EditText etPhone;
    EditText etGender;
    EditText etGrade;
    EditText etPlace;
    EditText etDob;

    Button btnAdd;


    DBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emp);

        initViews();
    }

    private void initViews() {

        dbAdapter = new DBAdapter(getBaseContext());

        btnAdd = (Button) findViewById(R.id.btnAdd);


        etName = (EditText) findViewById(R.id.etName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etAge = (EditText) findViewById(R.id.etAge);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etGender = (EditText) findViewById(R.id.etGender);
        etGrade = (EditText) findViewById(R.id.etGrade);
        etPlace = (EditText) findViewById(R.id.etPlace);
        etDob = (EditText) findViewById(R.id.etDob);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.inputLayoutName);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.inputLayoutAddress);
        textInputLayoutAge = (TextInputLayout) findViewById(R.id.inputLayoutAge);
        textInputLayoutPhone = (TextInputLayout) findViewById(R.id.inputLayoutPhone);
        textInputLayoutGender = (TextInputLayout) findViewById(R.id.inputLayoutGender);
        textInputLayoutGrade = (TextInputLayout) findViewById(R.id.inputLayoutGrade);
        textInputLayoutPlace = (TextInputLayout) findViewById(R.id.inputLayoutPlace);
        textInputLayoutDob = (TextInputLayout) findViewById(R.id.inputLayoutDob);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Employee employee = new Employee();

                employee.setName(etName.getText().toString());
                employee.setAddress(etAddress.getText().toString());
                employee.setAge(Integer.parseInt(etAge.getText().toString()));
                employee.setPhone(Long.parseLong(etPhone.getText().toString()));
                employee.setGender(etGender.getText().toString());
                employee.setGrade(etGrade.getText().toString());
                employee.setPlace(etPlace.getText().toString());
                employee.setDob(etDob.getText().toString());

                if (dbAdapter.insertData(employee))
                    Log.i("dbResult", "inserted");
                else
                    Log.i("dbResult", "insertion failed");
            }
        });


    }
}
