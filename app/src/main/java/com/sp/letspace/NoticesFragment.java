package com.sp.letspace;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sp.letspace.adapters.NoticesAdapter;
import com.sp.letspace.models.ApiResponse;
import com.sp.letspace.models.Notice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticesFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoticesAdapter adapter;
    private List<Notice> noticeList = new ArrayList<>();
    private SessionViewModel sessionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notices, container, false);

        recyclerView = view.findViewById(R.id.recyclerNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NoticesAdapter(noticeList);
        recyclerView.setAdapter(adapter);

        // 🔹 Get the shared ViewModel tied to the Activity
        sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);

        // 🔹 Observe profile data
        sessionViewModel.getTenantProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null && profile.notices != null) {
                noticeList.clear();
                noticeList.addAll(profile.notices);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }


}
