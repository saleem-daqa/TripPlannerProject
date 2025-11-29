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

public class EditTripActivity extends AppCompatActivity {
    private EditText destinationEditText, startDateEditText, endDateEditText;
    private EditText budgetEditText, notesEditText;
    private Spinner transportSpinner;
    private RadioGroup paymentRadioGroup;
    private RadioButton radioCash, radioCard;
    private CheckBox completedCheckBox;
    private Button updateButton, deleteButton, backButton;
    private String startDate = "", endDate = "";
    private int tripPosition = -1;
    private final Calendar startCalendar = Calendar.getInstance();
    private final Calendar endCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        destinationEditText = findViewById(R.id.destinationEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        budgetEditText = findViewById(R.id.budgetEditText);
        notesEditText = findViewById(R.id.notesEditText);
        transportSpinner = findViewById(R.id.transportSpinner);
        paymentRadioGroup = findViewById(R.id.paymentRadioGroup);
        radioCash = findViewById(R.id.radioCash);
        radioCard = findViewById(R.id.radioCard);
        completedCheckBox = findViewById(R.id.completedCheckBox);
        updateButton = findViewById(R.id.updateTripButton);
        deleteButton = findViewById(R.id.deleteTripButton);
        backButton = findViewById(R.id.backButton);

        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.transport_types,
                android.R.layout.simple_spinner_item
        );
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportSpinner.setAdapter(transportAdapter);

        Intent intent = getIntent();

        tripPosition = intent.getIntExtra("position", -1);

        String destination = intent.getStringExtra("destination");
        String start = intent.getStringExtra("startDate");
        String end = intent.getStringExtra("endDate");
        String budget = intent.getStringExtra("budget");
        String notes = intent.getStringExtra("notes");
        boolean completed = intent.getBooleanExtra("completed", false);
        String transport = intent.getStringExtra("transport");
        String payment = intent.getStringExtra("payment");

        destinationEditText.setText(destination);
        startDateEditText.setText(start);
        endDateEditText.setText(end);
        budgetEditText.setText(budget);
        notesEditText.setText(notes);
        completedCheckBox.setChecked(completed);

        if (start != null) {
            startDate = start;
        } else {
            startDate = "";
        }

        if (end != null) {
            endDate = end;
        } else {
            endDate = "";
        }

        if (transport != null) {
            for (int i = 0; i < transportSpinner.getCount(); i++) {
                if (transportSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(transport)) {
                    transportSpinner.setSelection(i);
                    break;
                }
            }
        }

        if (payment != null) {
            if (payment.equalsIgnoreCase("Cash")) {
                radioCash.setChecked(true);
            } else if (payment.equalsIgnoreCase("Card") || payment.equalsIgnoreCase("Credit Card")) {
                radioCard.setChecked(true);
            }
        }

        startDateEditText.setOnClickListener(v -> {

            Calendar today = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        startCalendar.set(year, month, day);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        startDate = sdf.format(startCalendar.getTime());
                        startDateEditText.setText(startDate);
                        if (!endDate.isEmpty() && endCalendar.before(startCalendar)) {
                            endDate = "";
                            endDateEditText.setText("");
                            Toast.makeText(this, "End date cleared because it is before the new start date", Toast.LENGTH_SHORT).show();
                        }

                    },

                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        endDateEditText.setOnClickListener(v -> {

            if (startDate.isEmpty()) {
                Toast.makeText(this, "Choose start date first", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar today = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, year, month, day) -> {

                        endCalendar.set(year, month, day);

                        if (endCalendar.before(startCalendar)) {
                            Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        endDate = sdf.format(endCalendar.getTime());

                        endDateEditText.setText(endDate);
                    },

                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });


        updateButton.setOnClickListener(v -> {

            if (destinationEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Destination is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (budgetEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter your budget", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please select start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = "";
            if (radioCash.isChecked()) {
                paymentMethod = "Cash";
            } else if (radioCard.isChecked()) {
                paymentMethod = "Card";
            } else {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedTransport = transportSpinner.getSelectedItem().toString();
            Intent result = new Intent();
            result.putExtra("position", tripPosition);
            result.putExtra("destination", destinationEditText.getText().toString().trim());
            result.putExtra("startDate", startDate);
            result.putExtra("endDate", endDate);
            result.putExtra("budget", budgetEditText.getText().toString().trim());
            result.putExtra("notes", notesEditText.getText().toString().trim());
            result.putExtra("transport", selectedTransport);
            result.putExtra("payment", paymentMethod);
            result.putExtra("completed", completedCheckBox.isChecked());

            setResult(RESULT_OK, result);
            finish();

            setResult(RESULT_OK, result);
            finish();
        });


        deleteButton.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("position", tripPosition);
            result.putExtra("delete", true);

            setResult(RESULT_OK, result);
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }
}