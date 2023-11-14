package com.example.botusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.botusers.Adapter.BotAdapter;
import com.example.botusers.Classes.Bot_users;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerMeds;
    BotAdapter botAdapter;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        Button deactivateButton = findViewById(R.id.deactivate);
        deactivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        recyclerMeds = findViewById(R.id.recyclerBots);

        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerMeds.setLayoutManager(layoutManager);
        // Set up Firebase database reference
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Bot_Users");
        Query query = databaseRef.orderByChild("status").equalTo("today");
        // Set up FirebaseRecyclerOptions for VehiclesAdapter
        FirebaseRecyclerOptions<Bot_users> options =
                new FirebaseRecyclerOptions.Builder<Bot_users>()
                        .setQuery(databaseRef, Bot_users.class)
                        .build();

        // Set up VehiclesAdapter
        botAdapter = new BotAdapter(options);
        recyclerMeds.setAdapter(botAdapter);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Update");
        builder.setMessage("Do you want to update the expiry date for all users?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateExpiryDates();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing or handle the "No" action
            }
        });

        builder.show();
    }

    private void updateExpiryDates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = dateFormat.format(new Date());

        // Reference to the "Bot_Users" node
        DatabaseReference botUsersRef = FirebaseDatabase.getInstance().getReference("Bot_Users");

        botUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Update the expiry_date for each child to the current date
                    userSnapshot.child("expiry_date").getRef().setValue(currentDate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if necessary
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        botAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        botAdapter.stopListening();
    }
}
