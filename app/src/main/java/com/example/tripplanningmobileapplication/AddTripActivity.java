package com.example.tripplanningmobileapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity {

    private EditText destinationEditText, budgetEditText, notesEditText;
    private Button startDateButton, endDateButton, saveTripButton, backButton;
    private Spinner transportSpinner;
    private CheckBox completedCheckBox;

    private RadioGroup paymentRadioGroup;
    private RadioButton radioCash, radioCard;

    private String startDate = "";
    private String endDate = "";

    private final Calendar startCalendar = Calendar.getInstance();
    private final Calendar endCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        destinationEditText = findViewById(R.id.destinationEditText);
        budgetEditText = findViewById(R.id.budgetEditText);
        notesEditText = findViewById(R.id.notesEditText);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        saveTripButton = findViewById(R.id.saveTripButton);
        backButton = findViewById(R.id.backButton);
        transportSpinner = findViewById(R.id.transportSpinner);
        completedCheckBox = findViewById(R.id.completedCheckBox);
        paymentRadioGroup = findViewById(R.id.paymentRadioGroup);
        radioCash = findViewById(R.id.radioCash);
        radioCard = findViewById(R.id.radioCard);

        radioCash.setChecked(true);

        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.transport_types,
                android.R.layout.simple_spinner_item
        );
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportSpinner.setAdapter(transportAdapter);

        startDateButton.setOnClickListener(v -> {

            Calendar today = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    AddTripActivity.this,
                    (view, year, month, dayOfMonth) -> {

                        startCalendar.set(year, month, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        startDate = sdf.format(startCalendar.getTime());

                        startDateButton.setText(startDate);
                    },
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        endDateButton.setOnClickListener(v -> {

            if (startDate.isEmpty()) {
                Toast.makeText(this, "Please select start date first", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar today = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    AddTripActivity.this,
                    (view, year, month, dayOfMonth) -> {

                        endCalendar.set(year, month, dayOfMonth);

                        if (endCalendar.before(startCalendar)) {
                            Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        endDate = sdf.format(endCalendar.getTime());

                        endDateButton.setText(endDate);
                    },
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        saveTripButton.setOnClickListener(v -> {
            String destination = destinationEditText.getText().toString().trim();
            String budget = budgetEditText.getText().toString().trim();
            String notes = notesEditText.getText().toString().trim();
            String transport = transportSpinner.getSelectedItem().toString();
            boolean completed = completedCheckBox.isChecked();

            if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (budget.isEmpty()) {
                Toast.makeText(this, "Please enter your budget", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod;
            if (radioCash.isChecked()) {
                paymentMethod = "Cash";
            } else {
                paymentMethod = "Credit Card";
            }

            Intent intent = new Intent();
            intent.putExtra("destination", destination);
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
            intent.putExtra("budget", budget);
            intent.putExtra("notes", notes);
            intent.putExtra("transport", transport);
            intent.putExtra("payment", paymentMethod);
            intent.putExtra("completed", completed);

            setResult(RESULT_OK, intent);
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }
}