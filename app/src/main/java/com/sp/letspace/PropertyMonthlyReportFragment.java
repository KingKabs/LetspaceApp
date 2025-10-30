package com.sp.letspace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.letspace.adapters.MonthlyReportAdapter;
import com.sp.letspace.models.MonthlyReport;

import java.util.ArrayList;
import java.util.List;

public class PropertyMonthlyReportFragment extends Fragment {

    private static final String ARG_REPORTS = "arg_reports";
    private List<MonthlyReport> reports;

    public static PropertyMonthlyReportFragment newInstance(ArrayList<MonthlyReport> reports) {
        PropertyMonthlyReportFragment fragment = new PropertyMonthlyReportFragment();
        Bundle args = new Bundle();
        //args.putParcelableArrayList(ARG_REPORTS, reports);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //reports = getArguments().getParcelableArrayList(ARG_REPORTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_monthly_report, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvMonthlyReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MonthlyReportAdapter(reports));

        return view;
    }
}


