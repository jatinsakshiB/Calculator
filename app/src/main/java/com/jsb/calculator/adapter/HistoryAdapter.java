package com.jsb.calculator.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jsb.calculator.R;
import com.jsb.calculator.modules.CalculatorHistory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private Date date  = null;
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;


    private List<CalculatorHistory> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnSize0 onSize0;

    public HistoryAdapter(Context context, List<CalculatorHistory> data) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mData = data;

        sharedPreferences = context.getSharedPreferences("com.jsb.calculator", MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.each_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CalculatorHistory calHis = mData.get(position);

        if (calHis != null) {
            holder.textValue.setText(calHis.getValue()+" = "+calHis.getCal());
            holder.textTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(calHis.getTime())));
            holder.textType.setText(calHis.getCal()+"");

            if (calHis.getType() == 0){
                holder.imageType.setBackgroundResource(R.drawable.ic_calculator2);
            }else if (calHis.getType() == 1){
                holder.imageType.setBackgroundResource(R.drawable.ic_keyboard_white);
            }else if (calHis.getType() == 2){
                holder.imageType.setBackgroundResource(R.drawable.ic_keyboard_white);
            }else if (calHis.getType() == 3){
                holder.imageType.setBackgroundResource(R.drawable.ic_floating_cal_white);
            }




            holder.textDate.setVisibility(View.GONE);

            if (position == 0){
                holder.textDate.setVisibility(View.VISIBLE);

                Calendar smsTime = Calendar.getInstance();
                smsTime.setTime(new Date(calHis.getTime()));
                Calendar now = Calendar.getInstance();

                if (DateUtils.isToday(calHis.getTime())) {
                    holder.textDate.setText("Today");
                }else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
                    holder.textDate.setText("Yesterday");
                } else {
                    holder.textDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(new Date(calHis.getTime())));
                }

            }else {
                CalculatorHistory qrCode2 = mData.get(position-1);
                if (!isSameDay( new Date(qrCode2.getTime()), new Date(calHis.getTime()))) {
                    holder.textDate.setVisibility(View.VISIBLE);

                    Calendar smsTime = Calendar.getInstance();
                    smsTime.setTime(new Date(calHis.getTime()));
                    Calendar now = Calendar.getInstance();

                    if (DateUtils.isToday(calHis.getTime())) {
                        holder.textDate.setText("Today");
                    }else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
                        holder.textDate.setText("Yesterday");
                    } else {
                        holder.textDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(new Date(calHis.getTime())));
                    }
                }else {
                    holder.textDate.setVisibility(View.GONE);
                }
            }

            

            holder.deleteBt.setOnClickListener(view -> {
                mData.remove(calHis);
                String json = gson.toJson(mData);
                sharedPreferences.edit().putString("CalHis", json).apply();
                notifyDataSetChanged();
                if (mData.size() == 0){
                    if (onSize0 != null) onSize0.onSize0();
                }
            });


        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textType,textValue, textTime, textDate;
        ImageView imageType, deleteBt;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            textType = (TextView) itemView.findViewById(R.id.typeText);
            textValue = (TextView) itemView.findViewById(R.id.valueText);
            textTime = (TextView) itemView.findViewById(R.id.timeText);
            textDate = (TextView) itemView.findViewById(R.id.date_tv);

            imageType = (ImageView) itemView.findViewById(R.id.image_type);
            deleteBt = (ImageView) itemView.findViewById(R.id.delete_bt);

            cardView = (CardView) itemView.findViewById(R.id.layout_cv);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    CalculatorHistory getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setOnSize0(OnSize0 onSize0) {
        this.onSize0 = onSize0;
        if (mData.size() <= 0){
            onSize0.onSize0();
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnSize0 {
        void onSize0();
    }


    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public void updateList(List<CalculatorHistory> list){
        mData = list;
        notifyDataSetChanged();
    }

}
