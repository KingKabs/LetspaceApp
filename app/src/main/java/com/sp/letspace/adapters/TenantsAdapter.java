package com.sp.letspace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.letspace.R;
import com.sp.letspace.models.Tenant;

import java.util.List;


public class TenantsAdapter extends RecyclerView.Adapter<TenantsAdapter.TenantViewHolder> {

    private final List<Tenant> tenantList;

    public TenantsAdapter(List<Tenant> tenantList) {
        this.tenantList = tenantList;
    }

    @NonNull
    @Override
    public TenantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tenant, parent, false);
        return new TenantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenantViewHolder holder, int position) {
        Tenant tenant = tenantList.get(position);

        // Full name with fallback
        String fullName = tenant.getFullName();
        holder.tvTenantName.setText(fullName);
        holder.tvTenantUnit.setText("Unit: " + tenant.getDisplayUnit());
        holder.tvTenantProperty.setText("Property: " + tenant.getDisplayProperty());


        // Contact info (fallback to ID if phone not available)
        String contactInfo = (tenant.phone != null && !tenant.phone.isEmpty())
                ? "Phone: " + tenant.phone
                : "Tenant ID: " + tenant.id;
        holder.tvTenantContact.setText(contactInfo);
    }

    @Override
    public int getItemCount() {
        return tenantList != null ? tenantList.size() : 0;
    }

    public static class TenantViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenantName, tvTenantUnit, tvTenantProperty, tvTenantContact;

        public TenantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenantName = itemView.findViewById(R.id.tvTenantName);
            tvTenantUnit = itemView.findViewById(R.id.tvTenantUnit);
            tvTenantProperty = itemView.findViewById(R.id.tvTenantProperty);
            tvTenantContact = itemView.findViewById(R.id.tvTenantContact);
        }
    }
}


