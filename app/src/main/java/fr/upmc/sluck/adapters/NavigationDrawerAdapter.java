package fr.upmc.sluck.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.upmc.sluck.R;
import fr.upmc.sluck.controllers.OnItemSelecteListener;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.model.DrawerItem;

/**
 * Created by labib on 21/03/2018.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.DrawerViewHolder> {
    private List<Channel> drawerMenuList;
    private OnItemSelecteListener mListener;


    public NavigationDrawerAdapter(List<Channel> drawerMenuList) {
        this.drawerMenuList = drawerMenuList;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);

        return new DrawerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
            holder.title.setText(drawerMenuList.get(position ).getName());

    }

    @Override
    public int getItemCount() {
        return drawerMenuList.size();
    }

    public void setOnItemClickLister(OnItemSelecteListener mListener) {
        this.mListener = mListener;
    }



    public class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public DrawerViewHolder(View itemView) {
            super(itemView);

                title =  itemView.findViewById(R.id.title);
            itemView.setOnClickListener(view -> mListener.onItemSelected(view, getAdapterPosition()));



        }
    }
}

