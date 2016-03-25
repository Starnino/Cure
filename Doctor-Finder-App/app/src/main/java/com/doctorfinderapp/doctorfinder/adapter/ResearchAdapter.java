package com.doctorfinderapp.doctorfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doctorfinderapp.doctorfinder.R;

import java.util.ArrayList;

/**
 * Created by francesco on 04/03/16.
 */
public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ResearchViewHolder>{

    public static class ResearchViewHolder extends RecyclerView.ViewHolder {
        TextView special;
        TextView city;
        ImageButton search;

        ResearchViewHolder(View itemView) {
            super(itemView);
            special = (TextView) itemView.findViewById(R.id.categ);
            city = (TextView) itemView.findViewById(R.id.city);
            search = (ImageButton) itemView.findViewById(R.id.research_button);
        }
    }

    ArrayList<String[]> linearLayouts;

    public ResearchAdapter(ArrayList<String[]> research){
        this.linearLayouts = research;
    }

    @Override
    public ResearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_item, parent, false);
        return new ResearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ResearchViewHolder holder, final int position) {
        holder.special.setText(linearLayouts.get(position)[0]);
        holder.city.setText(linearLayouts.get(position)[1]);
        /*TODO REMOVE holder.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ON CLICK SEARCH --> ", GlobalVariable.research_city_parameters.get(position) + " " + position);
                MainActivity.research(GlobalVariable.research_special_parameters.get(position),
                        GlobalVariable.research_city_parameters.get(position));

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return linearLayouts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}