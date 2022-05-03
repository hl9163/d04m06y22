package com.example.d04m06y22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.example.d04m06y22.classes.Worker;

import java.util.ArrayList;

import static com.example.d04m06y22.classes.FBref.refWorkers;

/**
 * @author		Harel Leibovich <hl9163@bs.amalnet.k12.il>
 * @version	2.0
 * @since		02/05/2022
 * add or update worker details
 */

public class activity_input_worker extends AppCompatActivity {
    LinearLayout workModeLL, firstNameLL, lastNameLL, compenyLL, phoneLL;
    TextView titlePage;
    Button saveAndContinue;
    Switch workMode;
    EditText pId,cId, firstNameField,lastNameField,comp,phone_numberField;

    static int mode;
    static boolean fistStep = true;
    static String personal_id,card_id,first_name,last_name, worker_company,phone_number;
    boolean  ok = true;
    boolean finish = false;

    ValueEventListener workerListener;
    ArrayList<Worker> workersList = new ArrayList<Worker>();
    ArrayList<String> idInfo = new ArrayList<String>();
    ArrayList<String> cardInfo = new ArrayList<String>();
    ArrayList<Integer> info = new ArrayList<Integer>();
    Worker Cworker;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_worker);

        fistStep = true;

        workModeLL = (LinearLayout) findViewById(R.id.workingLL);
        firstNameLL = (LinearLayout) findViewById(R.id.firstNameLL);
        lastNameLL = (LinearLayout) findViewById(R.id.lastNameLL);
        compenyLL = (LinearLayout) findViewById(R.id.compenyLL);
        phoneLL = (LinearLayout) findViewById(R.id.phoneLL);

        titlePage = (TextView) findViewById(R.id.titleUpdateWorkers);
        saveAndContinue = (Button) findViewById(R.id.saveAndContinueButton);
        workMode = (Switch) findViewById(R.id.switch1);

        pId = (EditText) findViewById(R.id.Personal_id);
        cId = (EditText) findViewById(R.id.card_id);
        firstNameField = (EditText) findViewById(R.id.first_name);
        lastNameField = (EditText) findViewById(R.id.last_name);
        comp = (EditText) findViewById(R.id.compeny);
        phone_numberField = (EditText) findViewById(R.id.Phone);

        Intent gi = getIntent();
        mode = gi.getIntExtra("mode",-1);
        workModeLL.setVisibility(View.INVISIBLE);
        if (mode == 0){
            titlePage.setText("add a new worker:");
        }else if (mode == 1){
            titlePage.setText("edit worker details:");
            firstNameLL.setVisibility(View.INVISIBLE);
            lastNameLL.setVisibility(View.INVISIBLE);
            compenyLL.setVisibility(View.INVISIBLE);
            phoneLL.setVisibility(View.INVISIBLE);
            saveAndContinue.setText("next");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(workerListener != null){
            refWorkers.removeEventListener(workerListener);
        }
    }

    /**
     * check if the personal id and card id are exist in the database and save a new worker
     * <p>
     *
     * @param	personalId Description	String personal Id
     * @param cardId Description String card Id
     */
    public void save_new_worker(String personalId, String cardId){
        workerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idInfo.clear();
                cardInfo.clear();
                workersList.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    String d = (String) data.getKey();
                    Worker temp = data.getValue(Worker.class);
                    workersList.add(temp);
                }
                for(int i =0;i<workersList.size();i++){
                    idInfo.add(workersList.get(i).getPersonal_id());
                    cardInfo.add(workersList.get(i).getCard_id());
                }
                if ((idInfo.contains(personalId) || cardInfo.contains(cardId)) && !finish){
                    personal_id = "855555";
                    popErrorMassage();
                }
                finish = true;
                if (check_inputs(personal_id, card_id, first_name, last_name,worker_company,phone_number)){
                    Worker temp = new Worker(personal_id,card_id,first_name,last_name,phone_number,worker_company,true);
                    refWorkers.child(personal_id).setValue(temp);
                    finish();
                }else{
                    popErrorMassage();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refWorkers.addValueEventListener(workerListener);
    }
    /**
     * read a line from the database by existing personal id
     * <p>
     *
     * @param	id_key Description	String personal Id
     * @param	card_key Description	String card id
     * @return  String[]result Description include all the details  [KEY_ID, PERSONAL_ID, CARD_ID, FIRST_NAME, LAST_NAME, WORKER_COMPENY, PHONE_NUMBER, IS_WORKING]
     */
    public void update_worker_show(String id_key,String card_key){
        workerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    String d = (String) data.getKey();
                    if(d.equals(id_key)){
                        Cworker = data.getValue(Worker.class);
                        if (Cworker.getCard_id().equals(card_key)){
                            ok = true;
                            break;
                        }
                    }
                }
                if(ok){
                    Worker details = Cworker;
                    firstNameLL.setVisibility(View.VISIBLE);
                    lastNameLL.setVisibility(View.VISIBLE);
                    compenyLL.setVisibility(View.VISIBLE);
                    phoneLL.setVisibility(View.VISIBLE);
                    workModeLL.setVisibility(View.VISIBLE);
                    firstNameField.setText(details.getFirst_name());
                    lastNameField.setText(details.getLast_name());
                    comp.setText(details.getCompany());
                    phone_numberField.setText(details.getPhone_number());
                    boolean wM = details.isIs_working();
                    if (wM){
                        workMode.setChecked(false);
                    }else{
                        workMode.setChecked(true);
                    }
                    pId.setFocusable(false);
                    cId.setFocusable(false);
                    saveAndContinue.setText("save");

                }else{
                    popErrorMassage();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refWorkers.addValueEventListener(workerListener);

    }
    /**
     * check if the program can save data in the database.
     * by checking the length of each parameter is bigger then 0 and the id is possible (by the israeli format)
     * <p>
     *
     * @param	id Description	String personal Id
     * @param	cId Description	String card id
     * @param	fn Description	String fist name
     * @param	ln Description	String last name
     * @param	wc Description	String worker company
     * @param	pN Description	String phone number
     * @return true/false Description false - cannot save, true - can save.
     */
    public boolean check_inputs(String id, String cId, String fn, String ln, String wc,String pN){
        if (id.length() == 0 || !check_id(id) || cId.length() == 0 || fn.length() == 0 || ln.length() == 0 || wc.length() == 0 || pN.length() == 0){
            return false;
        }
        return true;
    }
    /**
     * checking the id by the israeli format
     * <p>
     *
     * @param	id_num Description	String personal Id
     * @return  true/false true - ok id, false - not israeli id
     */
    public boolean check_id(String id_num){
        if (id_num.length()<9){
            String zero ="";
            for (int i=0;i>id_num.length()-9;i--){
                zero+="0";
            }
            id_num = zero+id_num;
        }
        int counter = 0;
        int counter2 = 0;
        int num = 1;
        int last_digit;
        for (int i =0;i<id_num.length();i++){
            int current_num =Character.getNumericValue(id_num.charAt(i));
            current_num *= num;
            if (num == 1){
                num = 2;
            }else{
                num = 1;
            }
            if (current_num >9){
                current_num = (current_num/10)+(current_num%10);
            }
            counter+=current_num;
            if (i != id_num.length()-1){
                counter2+=current_num;
            }
        }
        if (10-(counter2%10) != 10){
            last_digit = 10-(counter2%10);
        }else{
            last_digit = 0;
        }

        if (last_digit == (int) Character.getNumericValue(id_num.charAt(id_num.length()-1))){
            if (counter%10 == 0){
                return true;
            }
            return false;
        }else{
            return false;
        }
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
                finish();
                dialog.dismiss();
            }
        });
        AlertDialog ad = adb.create();
        ad.show();

    }
    public void saveWorker(View view) {
        if (mode == 0){
            personal_id = pId.getText().toString();
            card_id = cId.getText().toString();
            first_name = firstNameField.getText().toString();
            last_name = lastNameField.getText().toString();
            worker_company = comp.getText().toString();
            phone_number = phone_numberField.getText().toString();
            save_new_worker(personal_id,card_id);
        }else if (mode == 1){
            if (fistStep){
                personal_id = pId.getText().toString();
                card_id = cId.getText().toString();
                update_worker_show(personal_id,card_id);
                fistStep = false;
            }else{
                personal_id = pId.getText().toString();
                card_id = cId.getText().toString();
                first_name = firstNameField.getText().toString();
                last_name = lastNameField.getText().toString();
                worker_company = comp.getText().toString();
                phone_number = phone_numberField.getText().toString();
                if (check_inputs(personal_id, card_id, first_name, last_name,worker_company,phone_number)){
                    Worker temp = new Worker(personal_id,card_id,first_name,last_name,phone_number,worker_company,!workMode.isChecked());
                    refWorkers.child(personal_id).setValue(temp);
                    finish();
                }
            }
        }
    }
    /**
     * back to menu
     *
     * <p>
     * @param view button
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