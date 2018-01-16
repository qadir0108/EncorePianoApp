package com.encore.piano.cardview.start;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.cardview.CardClickListener;

import java.util.ArrayList;

public class StartViewAdapter extends RecyclerView
        .Adapter<StartViewAdapter.StartViewHolder> {

    private static String LOG_TAG = "StartViewAdapter";
    public static boolean isClickable = true;
    private ArrayList<StartDataModel> mDataset;
    private static CardClickListener myClickListener;

    public static class StartViewHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView backdrop;
        TextView label;
        TextView dateTime;

        public StartViewHolder(View itemView) {
            super(itemView);
            backdrop = (ImageView) itemView.findViewById(R.id.backdrop);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(!isClickable)
                return;

            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(CardClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public StartViewAdapter(ArrayList<StartDataModel> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public StartViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.startscreen_row, parent, false);

        StartViewHolder dataObjectHolder = new StartViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(StartViewHolder holder, int position) {
        holder.backdrop.setImageResource(mDataset.get(position).getmBackdrop());
        holder.label.setText(mDataset.get(position).getmText1());
        holder.dateTime.setText(mDataset.get(position).getmText2());
    }

    public void addItem(StartDataModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
