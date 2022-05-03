package com.example.d04m06y22.classes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refWorkers=FBDB.getReference("Worker");
    public static DatabaseReference refFoodCompany=FBDB.getReference("Food_company");
    public static DatabaseReference refOrders = FBDB.getReference("Orders");

}
