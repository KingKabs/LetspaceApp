package com.sp.letspace.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.letspace.MaintenanceRequestDetailFragment;
import com.sp.letspace.R;
import com.sp.letspace.models.MaintenanceRequest;

import java.util.List;

public class MaintenanceRequestAdapter extends RecyclerView.Adapter<MaintenanceRequestAdapter.ViewHolder> {

    private List<MaintenanceRequest> requestList;

    public MaintenanceRequestAdapter(List<MaintenanceRequest> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaintenanceRequest request = requestList.get(position);
        holder.tvTitle.setText(request.getTitle());
        holder.tvCategory.setText(request.getCategory());
        holder.tvDescription.setText(request.getDescription());
        holder.tvStatus.setText(request.getStatus());

        holder.itemView.setOnClickListener(v -> {
            // Create fragment instance and pass data
            MaintenanceRequestDetailFragment fragment = new MaintenanceRequestDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("request", request);
            fragment.setArguments(bundle);

            // Perform fragment transaction
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });



        // Change status color
        switch (request.getStatus().toLowerCase()) {
            case "new":
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB300"))); // Amber
                break;
            case "in progress":
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1976D2"))); // Blue
                break;
            case "resolved":
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#388E3C"))); // Green
                break;
            case "closed":
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D32F2F"))); // Red
                break;
            default:
                holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#757575"))); // Grey
                break;
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDescription, tvStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
