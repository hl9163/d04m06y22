package com.example.d04m06y22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.d04m06y22.classes.Food_company;
import com.example.d04m06y22.classes.Order;
import com.example.d04m06y22.classes.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.d04m06y22.classes.FBref.refFoodCompany;
import static com.example.d04m06y22.classes.FBref.refOrders;
import static com.example.d04m06y22.classes.FBref.refWorkers;

public class activity_complete_order extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner companyView;
    EditText workerIdF, dateF, timeF;

    String appetizer,main_course,extra,dessert,drink;
    String company, workerId, date, time;

    boolean returnMain = false;
    boolean ok = false;
    String name = "";

    ArrayList<String> companyNames = new ArrayList<>();

    ValueEventListener companiesListener;
    ValueEventListener workerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);

        workerIdF = (EditText) findViewById(R.id.worker_idF);
        dateF = (EditText) findViewById(R.id.dateF);
        timeF = (EditText) findViewById(R.id.timeF);


        companyView = (Spinner) findViewById(R.id.company_list);

        companyView.setOnItemSelectedListener(this);
        Intent gi = getIntent();
        appetizer = gi.getStringExtra("appetizer");
        main_course = gi.getStringExtra("main_course");
        extra = gi.getStringExtra("extra");
        dessert = gi.getStringExtra("dessert");
        drink = gi.getStringExtra("drink");

        companyNames.add("chose a food company:");
        showCompanies();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(companiesListener != null){
            refFoodCompany.removeEventListener(companiesListener);
        }
        if(workerListener != null){
            refWorkers.removeEventListener(workerListener);
        }
    }
    /**
     * make to the spinner the list of companies.
     * <p>
     *
     */
    public void showCompanies(){
        companiesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    Food_company current = data.getValue(Food_company.class);
                    if (current.isProvide_service()){
                        companyNames.add(current.getCompany_name());
                    }
                }
                ArrayAdapter<String> adp = new ArrayAdapter<String>(activity_complete_order.this,
                        R.layout.support_simple_spinner_dropdown_item,companyNames);
                companyView.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refFoodCompany.addValueEventListener(companiesListener);
    }
    /**
     * check if the input that the user entered is legal
     * <p>
     *
     */
    public boolean check_input(){
        if (company.equals("None") || !check_date() || !check_time()){
            return false;
        }
        return true;
    }
    public void save_data(){
        workerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                   Worker current = data.getValue(Worker.class);
                    if (current.getPersonal_id().equals(workerId)){
                        ok = true;
                        break;
                    }
                }
                if (ok){
                    if (check_input()){
                        Order temp = new Order(date,time,company,workerId,appetizer,main_course,extra,dessert,drink);
                         name +=","+time;
                        refOrders.child(name).setValue(temp);
                        finish();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refWorkers.addValueEventListener(workerListener);
    }
    /**
     * check if the date that the user entered is legal
     * <p>
     *
     */
    public boolean check_date(){
        String dd;
        String mm;
        String yy;
        if (date.length() == 8){
            dd = date.substring(0,2);
            name += dd + ":";
            if (!is_number(dd)){
                return false;
            }
            int day = Integer.parseInt(dd);
            if (day < 0 || day>31){
                return false;
            }
            mm = date.substring(3,5);
            if (!is_number(mm)){
                return false;
            }
            int month = Integer.parseInt(mm);
            name += mm + ":";
            if (month < 1 || month>12){
                return false;
            }
            yy = date.substring(6,8);
            name += yy;
            if (!is_number(yy)){
                return false;
            }
        }
        return true;
    }
    /**
     * check if num is number
     * <p>
     * @param num string num to check if it is a number
     *
     */
    public boolean is_number(String num){
        try {
            int value = Integer.parseInt(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    /**
     * check if the time that the user entered is legal (and in the noon(; )
     * <p>
     *
     */
    public boolean check_time(){
        String hh;
        String mm;
        if (time.length() == 5){
            hh = time.substring(0,2);
            if (!is_number(hh)){
                return false;
            }
            int hours = Integer.parseInt(hh);
            if (hours >16 || hours < 12){
                return false;
            }
            mm = time.substring(3,5);
            if (!is_number(mm)){
                return false;
            }
            int minute = Integer.parseInt(mm);
            if (minute<0 || minute>59){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0){
            company = companyNames.get(position);
        }else{
            company = "None";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        company = "None";
    }
    /**
     * save order including checking the input and return to the main menu.
     * <p>
     * @param view save button
     *
     */
    public void save_order(View view) {
        workerId = workerIdF.getText().toString();
        date = dateF.getText().toString();
        time = timeF.getText().toString();
        if (check_input()){
            save_data();
            returnMain =true;

        }
        Intent si = new Intent(this,MainActivity.class);
        startActivity(si);

    }
    /**
     * back without saving
     * <p>
     *
     */
    public void back_to_main_menu(View view) {
        finish();
    }
    /**
     * create the menu.
     * <p>
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    /**
     * move to the credits activity.
     * <p>
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.return_mainMenu){
            Intent si = new Intent(this,MainActivity.class);
            startActivity(si);
            return true;
        }else if(id == R.id.credits){
            Intent si = new Intent(this,creditsActivity.class);
            startActivity(si);
            return true;

        }
        return true;
    }
}