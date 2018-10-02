package com.example.lambui.nevereatalone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class UserHolder extends RecyclerView.ViewHolder {
    View mView;
    Context mContext;


    public UserHolder(View itemView){
        super(itemView);

        mView = itemView;
        mContext = mView.getContext();
    }



//    public void setEmail(String name) {
//        TextView field = (TextView) mView.findViewById(R.id.email);
//        field.setText(name);
//    }



}
