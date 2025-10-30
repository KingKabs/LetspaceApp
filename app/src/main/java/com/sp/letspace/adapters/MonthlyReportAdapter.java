package com.sp.letspace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.letspace.R;
import com.sp.letspace.models.MonthlyReport;

import java.util.ArrayList;
import java.util.List;

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ReportViewHolder> {

    private List<MonthlyReport> reports;

    public MonthlyReportAdapter(List<MonthlyReport> reports) {
        this.reports = reports != null ? reports : new ArrayList<>();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_monthly_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        MonthlyReport report = reports.get(position);
        holder.tvMonth.setText("Current");
        holder.tvUnits.setText("Units: " + report.getUnitCount());
        holder.tvRent.setText("Total Rent: $" + report.getTotalRent());
        holder.tvOccupancy.setText("Occupancy: " + report.getOccupancyRate() + "%");
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvUnits, tvRent, tvOccupancy;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvReportMonth);
            tvUnits = itemView.findViewById(R.id.tvReportUnits);
            tvRent = itemView.findViewById(R.id.tvReportRent);
            tvOccupancy = itemView.findViewById(R.id.tvReportOccupancy);
        }
    }
}

