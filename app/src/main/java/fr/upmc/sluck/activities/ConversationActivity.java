package fr.upmc.sluck.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import static android.view.Gravity.LEFT;


import fr.upmc.sluck.R;
import fr.upmc.sluck.adapters.MessageListAdapter;
import fr.upmc.sluck.adapters.NavigationDrawerAdapter;
import fr.upmc.sluck.model.DrawerItem;
import fr.upmc.sluck.model.Message;

/**
 * Created by labib on 21/03/2018.
 */

public class ConversationActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewMenu;
    private RecyclerView mRecyclerViewMenu1;
    private RecyclerView mRecyclerViewMenu2;
    private MessageListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;
    private ArrayList<DrawerItem> mDrawerItemList;
    private ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        mRecyclerView = findViewById(R.id.recyclerViewMessage);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager( new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        //TODO
        messages =new ArrayList<>();
        messages.add(new Message("aa","hello","ayyoub", new Date(1)));
        System.err.println(messages.toString());
        mAdapter = new MessageListAdapter(messages);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager( new LinearLayoutManager(this));

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mRecyclerViewMenu = findViewById(R.id.drawerRecyclerView);
        mRecyclerViewMenu1 = findViewById(R.id.drawerRecyclerView1);
        mRecyclerViewMenu2 = findViewById(R.id.drawerRecyclerView2);
       /* navigationView.addOnItemTouchListener(
                new RecyclerView.OnItemTouchListener() {
                });*/
        mDrawerItemList = new ArrayList<>();

        DrawerItem item = new DrawerItem("channel A",R.drawable.inbox);
        mDrawerItemList.add(item);

        DrawerItem item2 = new DrawerItem("channel B",R.drawable.send);
        mDrawerItemList.add(item2);


        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(mDrawerItemList);
        mRecyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewMenu.setAdapter(adapter);
        mRecyclerViewMenu1.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewMenu1.setAdapter(adapter);
        mRecyclerViewMenu2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewMenu2.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(LEFT))
            mDrawerLayout.closeDrawer(LEFT);
        else
            super.onBackPressed();
    }
}
