package com.example.tripplanningmobileapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private ArrayList<Trip> tripsList;
    public TripAdapter(ArrayList<Trip> tripsList) {
        this.tripsList = tripsList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);

        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip currentTrip = tripsList.get(position);
        holder.destinationText.setText(currentTrip.getDestination());
        String dates = currentTrip.getStartDate() + " - " + currentTrip.getEndDate();
        holder.dateText.setText(dates);
        holder.budgetText.setText("Budget: $" + currentTrip.getBudget());
        holder.transportText.setText("Transport: " + currentTrip.getTransport());
        holder.paymentText.setText("Payment: " + currentTrip.getPayment());
        holder.completedCheckBox.setChecked(currentTrip.isCompleted());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditTripActivity.class);

            intent.putExtra("position", position);
            intent.putExtra("destination", currentTrip.getDestination());
            intent.putExtra("startDate", currentTrip.getStartDate());
            intent.putExtra("endDate", currentTrip.getEndDate());
            intent.putExtra("budget", currentTrip.getBudget());
            intent.putExtra("notes", currentTrip.getNotes());
            intent.putExtra("completed", currentTrip.isCompleted());
            intent.putExtra("transport", currentTrip.getTransport());
            intent.putExtra("payment", currentTrip.getPayment());

            ((MainActivity) v.getContext()).editTripLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        TextView destinationText, dateText, budgetText, transportText, paymentText;
        CheckBox completedCheckBox;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);

            destinationText = itemView.findViewById(R.id.destinationText);
            dateText = itemView.findViewById(R.id.dateText);
            budgetText = itemView.findViewById(R.id.budgetText);
            transportText = itemView.findViewById(R.id.transportText);
            paymentText = itemView.findViewById(R.id.paymentText);
            completedCheckBox = itemView.findViewById(R.id.completedCheckBox);
        }
    }
}