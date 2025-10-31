package com.sp.letspace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.letspace.R;
import com.sp.letspace.models.Property;

import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.PropertyViewHolder> {

    private final List<Property> propertyList;

    public PropertiesAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.tvName.setText(property.getName());
        holder.tvUnits.setText("Units: " + property.getUnitCount());

        // Format numbers with commas
        holder.tvRent.setText(String.format("Total Rent: KSh %, .0f", property.getTotalRent()));
        holder.tvCollectable.setText(String.format("Collectable: KSh %, .0f", property.getCollectableRent()));
        holder.tvVacant.setText("Vacant Units: " + property.getVacantUnits());

        // Format occupancy / vacancy percentages
        holder.tvOccupancy.setText(String.format("Occupancy: %.0f%%", property.getOccupancyRate()));
        holder.tvVacancy.setText(String.format("Vacancy: %.0f%%", property.getVacancyRate()));
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUnits, tvRent, tvCollectable, tvVacant, tvOccupancy, tvVacancy;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPropName);
            tvUnits = itemView.findViewById(R.id.tvPropUnits);
            tvRent = itemView.findViewById(R.id.tvPropTotalRent);
            tvCollectable = itemView.findViewById(R.id.tvPropCollectable);
            tvVacant = itemView.findViewById(R.id.tvPropVacant);
            tvOccupancy = itemView.findViewById(R.id.tvPropOccupancy);

            // Add vacancy TextView (if not already in layout)
            tvVacancy = itemView.findViewById(R.id.tvPropVacancy);
        }
    }
}



