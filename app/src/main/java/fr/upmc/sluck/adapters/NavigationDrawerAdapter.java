package fr.upmc.sluck.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.upmc.sluck.R;
import fr.upmc.sluck.model.DrawerItem;

/**
 * Created by labib on 21/03/2018.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.DrawerViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<DrawerItem> drawerMenuList;

    public NavigationDrawerAdapter(ArrayList<DrawerItem> drawerMenuList) {
        this.drawerMenuList = drawerMenuList;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if(viewType == TYPE_HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channels_group_name, parent, false);
        }else{
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);
        }
        return new DrawerViewHolder(view,viewType);
    }


    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if(position == 0) {
            holder.headerText.setText("Channel group");
        }else{
            holder.title.setText(drawerMenuList.get(position - 1).getTitle());
            holder.icon.setImageResource(drawerMenuList.get(position - 1).getIcon());
        }

    }

    @Override
    public int getItemCount() {
        return drawerMenuList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        }
        return TYPE_ITEM   ;
    }


    public class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        TextView headerText;

        public DrawerViewHolder(View itemView,int viewType) {
            super(itemView);

            if(viewType == 0){
                headerText = itemView.findViewById(R.id.group_title);
            }else {
                title =  itemView.findViewById(R.id.title);
                icon =  itemView.findViewById(R.id.icon);
            }

        }
    }
}

