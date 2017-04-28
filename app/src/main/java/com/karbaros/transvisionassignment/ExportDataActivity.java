package com.karbaros.transvisionassignment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportDataActivity extends AppCompatActivity {

    private static final int REQUEST_ID_WRITE_PERMISSION = 200;
    private final String fileName = "emp_details.txt";
    private List<Employee> employeeList;
    DBAdapter dbAdapter;

    Button btnExport;
    Button buttonCloseDone;
    LinearLayout exportLout;
    LinearLayout exportDoneLout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        dbAdapter = new DBAdapter(getBaseContext());
        btnExport = (Button) findViewById(R.id.btnExport);
        buttonCloseDone = (Button) findViewById(R.id.btnCloseExport);
        exportDoneLout = (LinearLayout) findViewById(R.id.exportDoneLout);
        exportLout = (LinearLayout) findViewById(R.id.exportLout);

        buttonCloseDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissionAndWriteFile();
            }
        });


    }

    private void askPermissionAndWriteFile() {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //
        if (canWrite) {
            this.writeFile();
        }
    }

    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        writeFile();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFile() {

        employeeList = dbAdapter.selectData();
        Log.i("export",employeeList.size()+"");
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < employeeList.size(); i++) {
           // Log.i("export",employeeList.get(i).getName());
            buffer.append(employeeList.get(i).getName() + "," + employeeList.get(i).getAddress() + "," + employeeList.get(i).getAge() + "," +
                    employeeList.get(i).getPhone() + "," + employeeList.get(i).getGender() + "," + employeeList.get(i).getGrade() + "," +
                    employeeList.get(i).getPlace() + "," + employeeList.get(i).getDob()+",");
        }
      //  Log.i("export",buffer.toString());
        String data = buffer.toString();


        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
      //  Log.i("ExternalStorageDemo", "Save to: " + path);



        try {
            File myFile = new File(path);
            //   myFile.mkdir();
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();

           // Toast.makeText(getApplicationContext(), fileName + " saved", Toast.LENGTH_LONG).show();
            exportLout.setVisibility(View.GONE);
            exportDoneLout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
