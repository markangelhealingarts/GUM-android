package com.gum.a499_android.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gum.a499_android.LandingPage;
import com.gum.a499_android.R;
import com.gum.a499_android.SelectWorkout;

public class PopupRecyclerViewAdapter extends RecyclerView.Adapter<PopupRecyclerViewAdapter.ItemHolder>{

    private Context mContext;
    private String data[];
    private String choice;
    private PopupWindow popUp;

    public PopupRecyclerViewAdapter(Context mContext, String data[], PopupWindow popUp) {
        this.mContext = mContext;
        this.data = data;
        this.popUp = popUp;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new  ItemHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        holder.bind(data[position]);
    }

    @Override
    public int getItemCount() { return data.length; }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item, parent, false));
        }

        public void bind(String obj) {
            final TextView item = itemView.findViewById(R.id.item_id);
            item.setText(obj);
            //make item clickable
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save selected object
                    choice = data[getAdapterPosition()];
                    Intent intent = new Intent("level");
                    intent.putExtra("choice",choice);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
//                    Log.d("PopUpRecycler",data[getAdapterPosition()]);
                    if(SelectWorkout.difficulty.equals("easy")){
                        choice = choice + "_gentle_" + LandingPage.avatarName;
                    }else if(SelectWorkout.difficulty.equals("medium")){
                        choice = choice + "_moderate_" + LandingPage.avatarName;
                    }else {
                        choice = choice + "_vigorous_" + LandingPage.avatarName;
                    }
                    SelectWorkout.selectedWorkout = choice;
                    popUp.dismiss();
                    Toast.makeText(mContext, choice, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}