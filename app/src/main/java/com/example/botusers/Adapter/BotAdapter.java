package com.example.botusers.Adapter;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.botusers.Classes.Bot_users;
import com.example.botusers.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BotAdapter extends FirebaseRecyclerAdapter<Bot_users, BotAdapter.Bot_usersViewHolder> {
    private List<Bot_users> Bot_usersList;
    private Context context;
    FirebaseAuth mAuth;

    public BotAdapter(@NonNull FirebaseRecyclerOptions<Bot_users> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull Bot_usersViewHolder holder, int position, @NonNull Bot_users model) {
        holder.bind(model);
        String key = getRef(position).getKey();
        String expiryDate = model.getExpiry_date();

        // Parse the expiry date string into a Date object
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date expiryDateObj;
        try {
            expiryDateObj = sdf.parse(expiryDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Get the current date
        Calendar currentDateCalendar = Calendar.getInstance();
        Date currentDate = currentDateCalendar.getTime();

        // Check if the expiry date has passed
        if (currentDate.after(expiryDateObj)) {
            // Set the background to rounded_lay_red
            holder.txtexpired.setVisibility(View.VISIBLE);
            holder.linearLayout.setBackgroundResource(R.drawable.rounded_lay_red);
        } else if (!currentDate.after(expiryDateObj)) {
            // Set a different background (if needed)
            holder.txtexpired.setVisibility(View.GONE);
            holder.linearLayout.setBackgroundResource(R.drawable.rounded_lay);
        }

        // Calculate and set the daysRemaining to a TextView (as in the previous code)
        long diffInMillis = expiryDateObj.getTime() - currentDate.getTime();
        long daysRemaining = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        holder.days.setText(String.valueOf(daysRemaining) + " days remaining");
        holder.reset15th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                // Set the day of the month to 15
                calendar.set(Calendar.DAY_OF_MONTH, 20);

                // Format the date to "dd MMMM yyyy" format
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                String expiry_date = sdf.format(calendar.getTime());

                // Update the expiry_date in Firebase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Bot_Users").child(key);
                databaseReference.child("expiry_date").setValue(expiry_date);
                Toast.makeText(holder.itemView.getContext(), "Expiry changed to: " + expiry_date, Toast.LENGTH_SHORT).show();
                // You can also update any UI elements to display the new expiry_date if needed

            }
        });
        holder.reset5th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                // Set the day of the month to 15
                calendar.set(Calendar.DAY_OF_MONTH, 5);

                // Format the date to "dd MMMM yyyy" format
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                String expiry_date = sdf.format(calendar.getTime());

                // Update the expiry_date in Firebase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Bot_Users").child(key);
                databaseReference.child("expiry_date").setValue(expiry_date);
                Toast.makeText(holder.itemView.getContext(), "Expiry changed to: " + expiry_date, Toast.LENGTH_SHORT).show();
                // You can also update any UI elements to display the new expiry_date if needed
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @NonNull
    @Override
    public Bot_usersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_users, parent, false);
        return new Bot_usersViewHolder(view);
    }

    public static class Bot_usersViewHolder extends RecyclerView.ViewHolder {
        private TextView expiryDateTextView;
        private TextView installedDateTextView;
        private TextView macAddressTextView;
        private TextView paymentStatusTextView;
        private TextView userTextView, days, txtexpired;
        Button reset15th, reset5th;
        LinearLayout linearLayout;

        public Bot_usersViewHolder(@NonNull View itemView) {
            super(itemView);
            expiryDateTextView = itemView.findViewById(R.id.textExpiryDate);
            installedDateTextView = itemView.findViewById(R.id.textInstalledDate);
            macAddressTextView = itemView.findViewById(R.id.textMacAddress);
            reset5th = itemView.findViewById(R.id.reset5th);
            txtexpired = itemView.findViewById(R.id.txtexpired);
            days = itemView.findViewById(R.id.days);
            linearLayout = itemView.findViewById(R.id.laymain);
            reset15th = itemView.findViewById(R.id.reset15th);
        }

        public void bind(Bot_users Bot_users) {
            expiryDateTextView.setText(Bot_users.getExpiry_date());
            installedDateTextView.setText(Bot_users.getInstalled_date());
            macAddressTextView.setText(Bot_users.getMac_address());
            String expiryDate = Bot_users.getExpiry_date();

            // Parse the expiry date string into a Date object
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            Date expiryDateObj;
            try {
                expiryDateObj = sdf.parse(expiryDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

            // Get the current date
            Calendar currentDateCalendar = Calendar.getInstance();
            Date currentDate = currentDateCalendar.getTime();

            // Calculate the days remaining
            long diffInMillis = expiryDateObj.getTime() - currentDate.getTime();
            long daysRemaining = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            // Set the daysRemaining to a TextView
            days.setText(String.valueOf(daysRemaining) + " days remaining");

        }
    }
}


