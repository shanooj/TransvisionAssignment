package com.karbaros.transvisionassignment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shanu on 27-Apr-17.
 */

public class EmployeAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Employee> employeeList;

    public EmployeAdapter(Activity activity, List<Employee> employeeList) {
        this.activity = activity;
        this.employeeList = employeeList;
        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Employee employee = employeeList.get(i);

        view = layoutInflater.inflate(R.layout.custom_list_view, viewGroup, false);

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvGenderAge = (TextView) view.findViewById(R.id.tvGenderAge);
        TextView tvPlace = (TextView) view.findViewById(R.id.tvPlace);
        TextView tvGrade = (TextView) view.findViewById(R.id.tvGrade);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        TextView tvDob = (TextView) view.findViewById(R.id.tvDob);


        tvName.setText(employee.getName());
        tvAddress.setText(employee.getAddress());
        tvDob.setText(employee.getDob());
        tvGenderAge.setText(employee.getGender() + " " + employee.getAge());
        tvGrade.setText(employee.getGrade());
        tvPlace.setText(employee.getPlace());
        tvPhone.setText("" + employee.getPhone());

        return view;
    }
}
