package com.example.tripplanningmobileapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripAdapter adapter;

    private ArrayList<Trip> tripList;
    private ArrayList<Trip> originalTripList;

    private Button addTripButton, logoutButton, searchButton;
    private EditText searchEditText;

    private ActivityResultLauncher<Intent> addTripLauncher;
    public ActivityResultLauncher<Intent> editTripLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIND VIEWS
        recyclerView = findViewById(R.id.tripsRecyclerView);
        addTripButton = findViewById(R.id.addTripButton);
        logoutButton = findViewById(R.id.logoutButton);
        searchButton = findViewById(R.id.searchButton);
        searchEditText = findViewById(R.id.searchEditText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripList = new ArrayList<>();
        originalTripList = new ArrayList<>();

        adapter = new TripAdapter(tripList);
        recyclerView.setAdapter(adapter);

        loadTrips();

        // ---------------- ADD NEW TRIP ----------------
        addTripLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                Intent data = result.getData();

                Trip newTrip = new Trip(
                        data.getStringExtra("destination"),
                        data.getStringExtra("startDate"),
                        data.getStringExtra("endDate"),
                        data.getStringExtra("budget"),
                        data.getStringExtra("notes"),
                        data.getBooleanExtra("completed", false),
                        data.getStringExtra("transport"),
                        data.getStringExtra("payment")
                );

                tripList.add(newTrip);
                originalTripList.add(newTrip);
                adapter.notifyItemInserted(tripList.size() - 1);

                saveTrips();
            }
        });

        addTripButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
            addTripLauncher.launch(intent);
        });

        // ---------------- LOGOUT ----------------
        logoutButton.setOnClickListener(v -> {

            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            prefs.edit().putBoolean("rememberMe", false).apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // ---------------- EDIT / DELETE ----------------
        editTripLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                Intent data = result.getData();
                int position = data.getIntExtra("position", -1);

                if (data.getBooleanExtra("delete", false)) {

                    Trip removed = tripList.get(position);
                    tripList.remove(position);

                    for (int i = 0; i < originalTripList.size(); i++) {
                        if (originalTripList.get(i) == removed) {
                            originalTripList.remove(i);
                            break;
                        }
                    }

                    adapter.notifyItemRemoved(position);

                } else {

                    Trip t = tripList.get(position);

                    t.setDestination(data.getStringExtra("destination"));
                    t.setStartDate(data.getStringExtra("startDate"));
                    t.setEndDate(data.getStringExtra("endDate"));
                    t.setBudget(data.getStringExtra("budget"));
                    t.setNotes(data.getStringExtra("notes"));
                    t.setCompleted(data.getBooleanExtra("completed", false));
                    t.setTransport(data.getStringExtra("transport"));
                    t.setPayment(data.getStringExtra("payment"));

                    adapter.notifyItemChanged(position);
                }

                saveTrips();
            }
        });

        searchButton.setOnClickListener(v -> {
            String text = searchEditText.getText().toString().toLowerCase();
            tripList.clear();
            if (text.isEmpty()) {
                for (int i = 0; i < originalTripList.size(); i++) {
                    tripList.add(originalTripList.get(i));
                }
            } else {
                for (int i = 0; i < originalTripList.size(); i++) {
                    Trip t = originalTripList.get(i);

                    if (t.getDestination().toLowerCase().startsWith(text)) {
                        tripList.add(t);
                    }
                }
            }

            adapter.notifyDataSetChanged();
        });
    }

    private void saveTrips() {
        SharedPreferences prefs = getSharedPreferences("TripPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear();
        editor.putInt("trip_count", originalTripList.size());

        for (int i = 0; i < originalTripList.size(); i++) {
            Trip t = originalTripList.get(i);
            String key = "trip_" + i + "_";

            editor.putString(key + "destination", t.getDestination());
            editor.putString(key + "startDate", t.getStartDate());
            editor.putString(key + "endDate", t.getEndDate());
            editor.putString(key + "budget", t.getBudget());
            editor.putString(key + "notes", t.getNotes());
            editor.putBoolean(key + "completed", t.isCompleted());
            editor.putString(key + "transport", t.getTransport());
            editor.putString(key + "payment", t.getPayment());
        }

        editor.apply();
    }

    private void loadTrips() {
        SharedPreferences prefs = getSharedPreferences("TripPrefs", MODE_PRIVATE);
        int count = prefs.getInt("trip_count", 0);
        tripList.clear();
        originalTripList.clear();
        for (int i = 0; i < count; i++) {
            String key = "trip_" + i + "_";
            Trip t = new Trip(
                    prefs.getString(key + "destination", ""),
                    prefs.getString(key + "startDate", ""),
                    prefs.getString(key + "endDate", ""),
                    prefs.getString(key + "budget", ""),
                    prefs.getString(key + "notes", ""),
                    prefs.getBoolean(key + "completed", false),
                    prefs.getString(key + "transport", ""),
                    prefs.getString(key + "payment", "")
            );
            tripList.add(t);
            originalTripList.add(t);
        }

        adapter.notifyDataSetChanged();
    }
}