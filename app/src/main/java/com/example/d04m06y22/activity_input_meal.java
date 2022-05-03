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
import android.widget.EditText;

public class activity_input_meal extends AppCompatActivity {
    String appetizer,main_course,extra,dessert,drink;
    EditText app,mC,ex,des,dri;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        app = (EditText) findViewById(R.id.appetizerF);
        mC = (EditText) findViewById(R.id.main_courseF);
        ex = (EditText) findViewById(R.id.extraF);
        des = (EditText) findViewById(R.id.dessertF);
        dri = (EditText) findViewById(R.id.drinkF);
    }

    /**
     * back to last screen
     * <p>
     * @param view back button
     *
     */
    public void back_to_main_menu(View view) {
        finish();
    }

    /**
     * check if the user entered
     * <p>
     *
     */
    public boolean check_input(){
        if (main_course.length() == 0){
            return false;
        }
        return true;

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
    /**
     * continue to the next input screen.
     * in addition it move the last information to the next screen to continue the
     * order (input more details).
     * <p>
     *
     */
    public void continueToNextPart(View view) {
        appetizer = app.getText().toString();
        main_course = mC.getText().toString();
        extra = ex.getText().toString();
        dessert = des.getText().toString();
        drink = dri.getText().toString();
        if (check_input()){
            Intent si = new Intent(this, activity_complete_order.class);
            si.putExtra("appetizer",appetizer);
            si.putExtra("main_course",main_course);
            si.putExtra("extra",extra);
            si.putExtra("dessert",dessert);
            si.putExtra("drink",drink);
            startActivity(si);
        }else{
            popErrorMassage();
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
}