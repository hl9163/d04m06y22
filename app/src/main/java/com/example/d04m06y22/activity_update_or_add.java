package com.example.d04m06y22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class activity_update_or_add extends AppCompatActivity {
    /**
     * @author		Harel Leibovich <hl9163@bs.amalnet.k12.il>
     * @version	2.0
     * @since		02/05/2022
     * side menu that the user need to choose what he want to add or update
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_or_add);
    }
    /**
     * click the button will take the user to add a new worker
     * mode 0 = add worker
     * mode 1 = update worker details
     * same screen to those two goals.
     * <p>
     *
     * @param	view Description	button add worker
     */
    public void add_worker(View view) {
        Intent si = new Intent(this,activity_input_worker.class);
        si.putExtra("mode",0);
        startActivity(si);
    }
    /**
     * click the button will take the user to update a worker
     * mode 0 = add worker
     * mode 1 = update worker details
     * same screen to those two goals.
     * <p>
     *
     * @param	view Description	button update worker
     */
    public void update_worker(View view) {
        Intent si = new Intent(this,activity_input_worker.class);
        si.putExtra("mode",1);
        startActivity(si);
    }
    /**
     * click the button will take the user to make a new order screen (input meal)
     * two screens to this goal.
     * <p>
     *
     * @param	view Description	button make a new order
     */
    public void make_new_order(View view) {
        Intent si = new Intent(this,activity_input_meal.class);
        startActivity(si);
    }
    /**
     * click the button will take the user to add a new food company
     * mode 0 = add food company
     * mode 1 = update food company details
     * same screen to those two goals.
     * <p>
     *
     * @param	view Description	button add food company
     */
    public void add_food_company(View view) {
        Intent si = new Intent(this,activity_input_company.class);
        si.putExtra("mode",0);
        startActivity(si);
    }
    /**
     * click the button will take the user to update food company details
     * mode 0 = add food company
     * mode 1 = update food company details
     * same screen to those two goals.
     * <p>
     *
     * @param	view Description	button update food company
     */
    public void update_fc_details(View view) {
        Intent si = new Intent(this,activity_input_company.class);
        si.putExtra("mode",1);
        startActivity(si);
    }
    /**
     * click the button will take the user to the main menu
     * <p>
     *
     * @param	view Description	button update food company
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