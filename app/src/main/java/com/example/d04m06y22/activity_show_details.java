package com.example.d04m06y22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.d04m06y22.classes.Food_company;
import com.example.d04m06y22.classes.Order;
import com.example.d04m06y22.classes.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.d04m06y22.classes.FBref.refFoodCompany;
import static com.example.d04m06y22.classes.FBref.refOrders;
import static com.example.d04m06y22.classes.FBref.refWorkers;

public class activity_show_details extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    Spinner optionList;
    LinearLayout moreDetails;
    TextView title, sub_info_field;
    ListView screen_list;

    ValueEventListener workerListener;
    ValueEventListener ordersListener;
    ValueEventListener fcListener;

    static ArrayList<String> tbl = new ArrayList<>();
    static ArrayList<String> sub_tbl = new ArrayList<>();

    static int mode;
    static ArrayList<String> userOptions =new ArrayList<>();
    static ArrayList<String> workersList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        optionList = (Spinner) findViewById(R.id.sort_list_options);
        moreDetails = (LinearLayout) findViewById(R.id.moreDetails);
        title = (TextView) findViewById(R.id.titleShowDetails);
        sub_info_field = (TextView) findViewById(R.id.f1);
        screen_list = (ListView) findViewById(R.id.show_filed);


        Intent gi = getIntent();
        mode = gi.getIntExtra("mode",-1);
        connect_spinner_values();
        moreDetails.setVisibility(View.INVISIBLE);

        if (mode == 0){
            title.setText("show details of: worker");
        }else if (mode == 1){
            title.setText("show details of:orders");
        }else if (mode == 2){
            title.setText("show details of: food companies");
        }
        optionList.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp_spinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, userOptions);
        optionList.setAdapter(adp_spinner);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(workerListener != null){
            refWorkers.removeEventListener(workerListener);
        }
        if(ordersListener != null){
            refOrders.removeEventListener(ordersListener);
        }
        if(fcListener != null){
            refFoodCompany.removeEventListener(fcListener);
        }

    }

    /**
     * make to the spinner the list of options to the user.
     * <p>
     *
     */
    public static void connect_spinner_values(){
        userOptions.clear();

        if (mode == 0){
            userOptions.add("all:");
            userOptions.add("Show only active employees");
            userOptions.add("Show only inactive employees");
            userOptions.add("sort by name");
        }else if (mode == 1){
            userOptions.add("all: details");
            userOptions.add("all: meal");
            userOptions.add("by time frame");
            userOptions.add("company");
        }else if (mode == 2){
            userOptions.add("all:");
            userOptions.add("Show only active food companies");
            userOptions.add("Show only inactive food companies");
            userOptions.add("sort by name");
        }

    }
    /**
     * return to the main menu
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
    /**
     * sort an array by popularity of String.
     * <p>
     * @param arr array of all the elements.
     * @return ArrayList<String> array of all the elements by popularity of their appearance
     * in the array
     */
    public static ArrayList<String> sortList2(ArrayList<String>arr){
        ArrayList<String> arrElements = new ArrayList<String>();
        ArrayList<String> arr2 = new ArrayList<String>();
        ArrayList<Integer> arr3 = new ArrayList<Integer>();

        for (int i =0;i<arr.size();i++){
            if (arrElements.isEmpty()){
                arrElements.add(arr.get(i));
            }else if (!arrElements.contains(arr.get(i))){
                arrElements.add(arr.get(i));
            }
        }
        System.out.println(arrElements);
        for (int i =0;i<arrElements.size();i++){
            String current = arrElements.get(i);
            int occurrences = Collections.frequency(arr, current);
            arr3.add(occurrences);
        }
        System.out.println(arr3);
        while (!arrElements.isEmpty()){
            int biggest = arr3.get(0);
            int index = 0;
            for (int i =0;i<arr3.size();i++){
                if (arr3.get(i) > biggest){
                    biggest = arr3.get(i);
                    index = i;
                }
            }
            arr2.add(arrElements.get(index));
            arr3.remove(index);
            arrElements.remove(index);
        }

        return arr2;
    }
    /**
     * organize the time frames to show the frames ond not the hour.
     * <p>
     * @param arr not organized array
     * @return organized array
     *
     */
    public static ArrayList<String> return_time_frame(ArrayList<String>arr){
        ArrayList<String> arr2 = new ArrayList<String>();
        arr = sortList2(arr);
        for (int i=0;i<arr.size();i++){
            if (is_number(arr.get(i))){
                arr2.add(arr.get(i)+"<t<"+String.valueOf(Integer.parseInt(arr.get(i))+1));
            }
        }
        return arr2;
    }
    /**
     * check if the input is number.
     * <p>
     * @param num random string
     * @return if the string is a number
     */
    public static boolean is_number(String num){
        try {
            int value = Integer.parseInt(num);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(mode == 0){
            if (position == 0){
                workerListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        sub_tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            String d = (String) data.getKey();
                            Worker temp = data.getValue(Worker.class);

                            if (temp.isIs_working()){
                                details = ""+temp.getPersonal_id()+", "+temp.getFirst_name()+", "+temp.getLast_name()+", "+temp.getCompany()+""+temp.getCard_id()+", "+temp.getPhone_number()+", working";
                            }else{
                                details = ""+temp.getPersonal_id()+", "+temp.getFirst_name()+", "+temp.getLast_name()+", "+temp.getCompany()+""+temp.getCard_id()+", "+temp.getPhone_number()+", working";
                            }
                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                refWorkers.addValueEventListener(workerListener);
            }else if (position == 1){
                workerListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            Worker temp = data.getValue(Worker.class);
                            details = ""+temp.getPersonal_id()+", "+temp.getFirst_name()+", "+temp.getLast_name()+", "+temp.getCompany()+""+temp.getCard_id()+", "+temp.getPhone_number()+", working";

                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                Query q = refWorkers.orderByChild("is_working").equalTo(true);
                q.addValueEventListener(workerListener);
            }else if (position == 2){
                workerListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            Worker temp = data.getValue(Worker.class);
                            details = ""+temp.getPersonal_id()+", "+temp.getFirst_name()+", "+temp.getLast_name()+", "+temp.getCompany()+""+temp.getCard_id()+", "+temp.getPhone_number()+", working";

                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                Query q = refWorkers.orderByChild("is_working").equalTo(false);
                q.addValueEventListener(workerListener);
            }else if (position == 3){
                workerListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            Worker temp = data.getValue(Worker.class);
                            details = ""+temp.getPersonal_id()+", "+temp.getFirst_name()+", "+temp.getLast_name()+", "+temp.getCompany()+""+temp.getCard_id()+", "+temp.getPhone_number()+", working";

                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                Query q = refWorkers.orderByChild("first_name");
                q.addValueEventListener(workerListener);


            }
        }else if(mode == 1){
            if (position == 0) {
                ordersListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String d = (String) data.getKey();
                            Order temp = data.getValue(Order.class);
                            details = ""+temp.getDate()+", "+temp.getTime()+", "+temp.getWorker_id()+", "+temp.getFood_company_name();
                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                refOrders.addValueEventListener(ordersListener);
            }else if (position == 1) {
                ordersListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String d = (String) data.getKey();
                            Order temp = data.getValue(Order.class);
                            details = ""+temp.getAppetizer()+", "+temp.getMain_course()+", "+temp.getExtra()+", "+temp.getDrink()+", "+temp.getDessert();
                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                refOrders.addValueEventListener(ordersListener);
            }else if (position == 2) {
                ordersListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String d = (String) data.getKey();
                            Order temp = data.getValue(Order.class);
                            tbl.add(temp.getTime().substring(0,2));
                        }
                        tbl = return_time_frame(tbl);
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                refOrders.addValueEventListener(ordersListener);
            }else if (position == 3) {
                ordersListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String d = (String) data.getKey();
                            Order temp = data.getValue(Order.class);
                            tbl.add(temp.getFood_company_name());
                        }
                        tbl = sortList2(tbl);
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                refOrders.addValueEventListener(ordersListener);
            }
        }else if (mode == 2){
            if (position == 0){
                fcListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            String d = (String) data.getKey();
                            Food_company temp = data.getValue(Food_company.class);
                            details = ""+temp.getCompany_name()+", "+temp.getCompany_id()+", "+temp.getPhone_number()+", "+temp.getSecond_phone_number();

                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                refFoodCompany.addValueEventListener(fcListener);
            }else if (position == 1){
                fcListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            Food_company temp = data.getValue(Food_company.class);
                            details = ""+temp.getCompany_name()+", "+temp.getCompany_id()+", "+temp.getPhone_number()+", "+temp.getSecond_phone_number();

                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                Query q = refFoodCompany.orderByChild("provide_service").equalTo(true);
                q.addValueEventListener(fcListener);
            }else if (position == 2){
                fcListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            Food_company temp = data.getValue(Food_company.class);
                            details = ""+temp.getCompany_name()+", "+temp.getCompany_id()+", "+temp.getPhone_number()+", "+temp.getSecond_phone_number();

                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                Query q = refFoodCompany.orderByChild("provide_service").equalTo(false);
                q.addValueEventListener(fcListener);
            }else if (position == 3){
                fcListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String details;
                        tbl.clear();
                        for (DataSnapshot data : snapshot.getChildren()){
                            Food_company temp = data.getValue(Food_company.class);
                            details = ""+temp.getCompany_name()+", "+temp.getCompany_id()+", "+temp.getPhone_number()+", "+temp.getSecond_phone_number();


                            tbl.add(details);
                        }
                        screen_list.setOnItemClickListener(activity_show_details.this);
                        screen_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter<String> adp_list = new ArrayAdapter<String>(activity_show_details.this, R.layout.support_simple_spinner_dropdown_item, tbl);
                        screen_list.setAdapter(adp_list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                };
                Query q = refFoodCompany.orderByChild("company_name");
                q.addValueEventListener(fcListener);


            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
}