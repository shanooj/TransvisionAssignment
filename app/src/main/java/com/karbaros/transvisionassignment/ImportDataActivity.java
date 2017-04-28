package com.karbaros.transvisionassignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lypeer.fcpermission.FcPermissions;
import com.lypeer.fcpermission.impl.FcPermissionsCallbacks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.karbaros.transvisionassignment.FileChooser.getPath;

public class ImportDataActivity extends AppCompatActivity implements FcPermissionsCallbacks {

    private static final int TEXT_REQUEST = 100;
    Button mBtnSelect;
    Uri textUri;

    LinearLayout linearLayoutDone;
    LinearLayout linearLayoutImport;
    Button buttonDoneClose;

    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);

        mBtnSelect = (Button) findViewById(R.id.btnSelect);
        dbAdapter = new DBAdapter(getApplicationContext());

        linearLayoutDone = (LinearLayout) findViewById(R.id.importDoneLout);
        linearLayoutImport = (LinearLayout) findViewById(R.id.importLout);

        linearLayoutImport.setVisibility(View.VISIBLE);

        buttonDoneClose = ( Button) findViewById(R.id.btnCloseImport);
        buttonDoneClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestStoragePermission();
                getFile();

            }
        });
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }

    public void requestStoragePermission() {
        FcPermissions.requestPermissions(this, "The Storage permission is necessary to Download",
                FcPermissions.REQ_PER_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void getFile() {
        Intent galleryIntnt = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntnt.setType("text/*");
        startActivityForResult(galleryIntnt, TEXT_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST && resultCode == RESULT_OK) {
            textUri = data.getData();
            String filePath = "";


            filePath = getPath(getBaseContext(), textUri);

            if (textUri == null) {
                Toast.makeText(getBaseContext(), "select a file", Toast.LENGTH_SHORT).show();
            } else {
                final ArrayList<Employee> listEmp = getListContactFromTextFile(filePath);

                File tempFile = new File(filePath);
                String fileName = tempFile.getName();
                String fileSize = String.valueOf(tempFile.length()) + " Bytes";
                final int empCount = listEmp.size();

                // mFrstImp.setVisibility(View.GONE);
                // mSecImp.setVisibility(View.VISIBLE);

                ///  mFileName.setText(fileName);
                //  mFileSize.setText(fileSize);
                // mContactCount.setText(String.valueOf(contactCount));

                Log.d("import", fileName);
                Log.d("import", fileSize);
                Log.d("import", String.valueOf(empCount));



                for (int i = 0; i < listEmp.size(); i++) {

                    if (dbAdapter.insertData(new Employee(listEmp.get(i).getName(), listEmp.get(i).getAddress(), listEmp.get(i).getAge(),
                            listEmp.get(i).getPhone(), listEmp.get(i).getGender(), listEmp.get(i).getGrade(), listEmp.get(i).getPlace(),
                            listEmp.get(i).getDob())))

                     Log.i("importdb", "sucess");

                    else
                       Log.i("importdb", "fail");

                }
                linearLayoutImport.setVisibility(View.GONE);
                linearLayoutDone.setVisibility(View.VISIBLE);



/*
                mImport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < listEmp.size(); i++) {
                            dbAdapter.insertData(new Employee(listEmp.get(i).getName(), listEmp.get(i).getAddress(), listEmp.get(i).getAge(),
                                    listEmp.get(i).getPhone(), listEmp.get(i).getGender(), listEmp.get(i).getGrade(), listEmp.get(i).getPlace(),
                                    listEmp.get(i).getDob()));
                            //  mSecImp.setVisibility(View.GONE);
                            //  mThirdImp.setVisibility(View.VISIBLE);
                            //  mImportRes.setText("Successfully added " + contactCount + " contacts!");

                        }
                    }
                });*/

             /*   mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });*/

            }
        }
    }

    public static ArrayList<Employee> getListContactFromTextFile(String filePath) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader bReader = null;
        ArrayList<Employee> listResult = new ArrayList<Employee>();
        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis);
            bReader = new BufferedReader(isr);
            //String save line get from text file
            String line = null;
            //Array save product
            String[] strProduct = null;

            //Loop and get all data in text file
            while (true) {
                //Get 1 line
                line = bReader.readLine();
                Log.i("import", line);
                //Check line get empty, exit loop
                if (line == null) {
                    break;
                } else {


                    Employee employee = new Employee();
                    String[] subArray = new String[8];
                    int subArrayCount = 0;
                    strProduct = line.split(",");
                    for (int j = 0; j < strProduct.length; j++) {

                        subArray[subArrayCount] = strProduct[j];


                        if (subArrayCount == 7) {

                            //  Log.i("importsub", subArray[0]);
                            employee.setName(subArray[0]);
                            employee.setAddress(subArray[1]);
                            employee.setAge(Integer.parseInt(subArray[2]));
                            employee.setPhone(Long.parseLong(subArray[3]));
                            employee.setGender(subArray[4]);
                            employee.setGrade(subArray[5]);
                            employee.setPlace(subArray[6]);
                            employee.setDob(subArray[7]);

                            listResult.add(employee);

                            subArrayCount = 0;


                        } else {
                            subArrayCount++;

                        }


                    }
                    //  Log.i("importlnth", strProduct.length + "");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close file
            try {
                if (bReader != null)
                    bReader.close();
                if (isr != null)
                    isr.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listResult;
    }


}
