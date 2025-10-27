package com.sp.letspace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sp.letspace.R;
import com.sp.letspace.models.Notice;

import java.util.List;

public class NoticesAdapter extends RecyclerView.Adapter<NoticesAdapter.NoticeViewHolder> {
    private List<Notice> notices;

    public NoticesAdapter(List<Notice> notices) {
        this.notices = notices;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = notices.get(position);
        holder.title.setText(notice.getTitle());
        holder.message.setText(notice.getMessage());
        holder.date.setText("Posted on: " + notice.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, date;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noticeTitle);
            message = itemView.findViewById(R.id.noticeMessage);
            date = itemView.findViewById(R.id.noticeDate);
        }
    }
}

