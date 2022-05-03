package com.example.d04m06y22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.d04m06y22.classes.Food_company;
import com.example.d04m06y22.classes.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.example.d04m06y22.classes.Food_company;

import static com.example.d04m06y22.classes.FBref.refFoodCompany;
import static com.example.d04m06y22.classes.FBref.refWorkers;

public class activity_input_company extends AppCompatActivity {
    int mode;
    String company_name, company_id, phone_number, second_phone_number;
    static boolean firstStep = true;;
    boolean finish = false;
    boolean ok = false;
    Food_company current;

    LinearLayout serviceMode,phoneLL, secondPhoneLL;
    TextView title;
    EditText company_name_field, company_id_field, phone_number_field, second_phone_number_field;
    Button saveAndContinue;
    Switch workMode;

    AlertDialog.Builder adb;
    ValueEventListener FCListener;
    ArrayList<String> nameInfo = new ArrayList<String>();
    ArrayList<String> idInfo  = new ArrayList<String>();
    ArrayList<Food_company> companyList = new ArrayList<Food_company>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_company);

        firstStep = true;

        serviceMode = (LinearLayout) findViewById(R.id.serviceLL);
        phoneLL = (LinearLayout) findViewById(R.id.phone_numberLL);
        secondPhoneLL = (LinearLayout) findViewById(R.id.second_phoneLL);

        company_id_field = (EditText) findViewById(R.id.Company_id);
        company_name_field = (EditText) findViewById(R.id.Company_name);
        phone_number_field = (EditText) findViewById(R.id.phone_num);
        second_phone_number_field = (EditText) findViewById(R.id.second_num);

        workMode = (Switch) findViewById(R.id.switch2);
        saveAndContinue = (Button) findViewById(R.id.saveAndContinueButtonFC);

        title = (TextView) findViewById(R.id.titleUpdateCompany);

        Intent gi = getIntent();
        mode = gi.getIntExtra("mode",-1);
        serviceMode.setVisibility(View.INVISIBLE);
        if (mode == 0){
            title.setText("add a new food company:");
        }else if (mode == 1){
            title.setText("edit food company details:");
            phoneLL.setVisibility(View.INVISIBLE);
            secondPhoneLL.setVisibility(View.INVISIBLE);
            saveAndContinue.setText("next");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(FCListener != null){
            refFoodCompany.removeEventListener(FCListener);
        }
    }
    /**
     * check if the program can save data in the database.
     * by checking the length of each parameter is bigger then 0 and the id  and/ or company name is possible
     * <p>
     *
     * @return true/false Description false - cannot save, true - can save.
     */
    public boolean check_inputs(){
        if (company_id.length() == 0 || company_name.length() == 0 || phone_number.length() == 0 || phone_number.equals(second_phone_number)){
            return false;
        }
        return true;
    }
    /**
     * save the data of a new food company on the database if possible
     * <p>
     *
     */
    public void save_new_company() {
            FCListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nameInfo.clear();
                    idInfo.clear();
                    companyList.clear();
                    for (DataSnapshot data : snapshot.getChildren()){
                        String d = (String) data.getKey();
                        Food_company temp = data.getValue(Food_company.class);
                        companyList.add(temp);
                    }
                    for(int i =0;i<companyList.size();i++){
                        idInfo.add(companyList.get(i).getCompany_id());
                        nameInfo.add(companyList.get(i).getCompany_name());
                    }
                    if ((idInfo.contains(company_id) || nameInfo.contains(company_name)) && !finish){
                        finish = true;
                        return;
                    }
                    Food_company temp = new Food_company(company_id,company_name,phone_number,second_phone_number,true);
                    refFoodCompany.child(company_id).setValue(temp);
                    finish = true;
                    finish();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            };
            refFoodCompany.addValueEventListener(FCListener);
            finish();
    }

    /**
     * pop error alert dialog massage to the user
     * <p>
     *
     */
    public void popErrorMassage(){
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("wrong input!");
        adb.setMessage("you entered wrong input to one or more of the input fields");
        adb.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = adb.create();
        ad.show();
    }
    /**
     * show to change and save all the updated info of a food company
     * <p>
     *
     */
    public void update_food_company_details_show(){
        FCListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    current = data.getValue(Food_company.class);
                    if(current.getCompany_id().equals(company_id) && current.getCompany_name().equals(company_name)){
                        ok = true;
                        break;
                    }
                }
                if (ok){
                    phoneLL.setVisibility(View.VISIBLE);
                    secondPhoneLL.setVisibility(View.VISIBLE);
                    serviceMode.setVisibility(View.VISIBLE);
                    phone_number_field.setText(current.getPhone_number());
                    second_phone_number_field.setText(current.getSecond_phone_number());
                    boolean wM = current.isProvide_service();
                    if (wM){
                        workMode.setChecked(false);
                    }else{
                        workMode.setChecked(true);
                    }
                    company_name_field.setFocusable(false);
                    company_id_field.setFocusable(false);
                    saveAndContinue.setText("save");
                }else{
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        refFoodCompany.addValueEventListener(FCListener);

    }
    /**
     * for each mode it is changing - mode 0 - add food company -
     * on click it will begin the saving posses and jump to save_data() function
     * if the code is run on mode 1 - update food company details - the process dividing to two parts:
     * part one is enter only the id and the name.
     * second part is show the user all the current food company data in the right place and save when he click save.
     * <p>
     *
     */
    public void saveWorker(View view) {
        company_id = company_id_field.getText().toString();
        company_name = company_name_field.getText().toString();
        phone_number = phone_number_field.getText().toString();
        second_phone_number = second_phone_number_field.getText().toString();
        if (mode == 0){
            if(check_inputs()){
                save_new_company();
                finish();
            }
        }else if (mode == 1){
            if (firstStep){
                firstStep = false;
                update_food_company_details_show();
            }else{
                if(check_inputs()){
                    Food_company temp = new Food_company(company_id,company_name,phone_number,second_phone_number,!workMode.isChecked());
                    refFoodCompany.child(company_id).setValue(temp);
                    finish();
                }
            }
        }
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
    /**
     * back to menu
     * <p>
     * @param view button
     */
    public void back_to_main_menu(View view) {
        finish();
    }
}