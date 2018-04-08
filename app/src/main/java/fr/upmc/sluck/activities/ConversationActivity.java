package fr.upmc.sluck.activities;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.upmc.sluck.Application;
import fr.upmc.sluck.R;
import fr.upmc.sluck.adapters.MessageListAdapter;
import fr.upmc.sluck.adapters.NavigationDrawerAdapter;
import fr.upmc.sluck.controllers.GlobalController;
import fr.upmc.sluck.controllers.Update;
import fr.upmc.sluck.model.Channel;
import fr.upmc.sluck.model.Message;
import fr.upmc.sluck.model.MessageRecievedUpdate;
import fr.upmc.sluck.model.MessageSentUpdate;
import fr.upmc.sluck.utils.exceptions.UtilException;

import static android.view.Gravity.LEFT;

/**
 * Created by labib on 21/03/2018.
 */

public class ConversationActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewMenu;
    private RecyclerView mRecyclerViewMenu1;
    private RecyclerView mRecyclerViewMenu2;
    public static MessageListAdapter mAdapter;
    private NavigationDrawerAdapter adaptermc;
    private NavigationDrawerAdapter adapteroc;
    public static NavigationDrawerAdapter adapterac;
    public static String currentChannel;
    @BindView(R.id.button_chatbox_send)
    Button send_btn;
    @BindView(R.id.edittext_chatbox)
    EditText et_chat;

    private DrawerLayout mDrawerLayout;
    private List<Channel> mcItems;
    private List<Channel> acItems;
    private List<Channel> ocItems;
    public static ArrayList<Update> messages;
    private GlobalController gc;
    Application app;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);
        app = (Application) getApplicationContext();
        Toolbar toolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        //  setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("ayyoub");
        /*android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setHomeActionContentDescription(R.drawable.ic_menu);*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gc = app.getController();
        mDrawerLayout = findViewById(R.id.drawer_layout);


        mRecyclerView = findViewById(R.id.recyclerViewMessage);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewMenu = findViewById(R.id.drawerRecyclerView);
        mRecyclerViewMenu1 = findViewById(R.id.drawerRecyclerView1);
        mRecyclerViewMenu2 = findViewById(R.id.drawerRecyclerView2);

        mRecyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewMenu1.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewMenu2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList<>();
        mcItems = new ArrayList<>();
        acItems = gc.getAvailableChannels();
        ocItems = new ArrayList<>();



        // messages.add(new UserStatusUpdate(new User("Tarek","1.1.1.1",1), Update.Type.USER_ENTERED));


        // mDrawerItemList.add(new DrawerItem("channel B"));

        mAdapter = new MessageListAdapter(messages);
        adaptermc = new NavigationDrawerAdapter(mcItems);
        adapteroc = new NavigationDrawerAdapter(ocItems);
        adapterac = new NavigationDrawerAdapter(acItems);

        mRecyclerViewMenu2.setAdapter(adapterac);
        mRecyclerViewMenu.setAdapter(adaptermc);
        mRecyclerViewMenu1.setAdapter(adapteroc);
        mRecyclerView.setAdapter(mAdapter);

        adapterac.setOnItemClickLister((v, position) -> {
            Toast.makeText(ConversationActivity.this, "You clicked at position: " + position, Toast.LENGTH_SHORT).show();
            currentChannel = acItems.get(position).getName();
            messages.clear();
            for (Message m : acItems.get(position).getMessages()
                    ) {
                if (m.getSender().equalsIgnoreCase(app.getUserName()))
                    messages.add(new MessageSentUpdate(m));
                else
                    messages.add(new MessageRecievedUpdate(m));


            }
            mAdapter.notifyDataSetChanged();
            ocItems.add(acItems.remove(position));
            adapterac.notifyItemRemoved(position);
            adapteroc.notifyDataSetChanged();
            getSupportActionBar().setTitle(currentChannel);
            mDrawerLayout.closeDrawer(LEFT);
        });

        adapteroc.setOnItemClickLister((v, position) -> {
            Toast.makeText(ConversationActivity.this, "You clicked at position: " + position, Toast.LENGTH_SHORT).show();
            currentChannel = ocItems.get(position).getName();
            messages.clear();
            for (Message m : ocItems.get(position).getMessages()
                    ) {
                if (m.getSender().equalsIgnoreCase(app.getUserName()))
                    messages.add(new MessageSentUpdate(m));
                else
                    messages.add(new MessageRecievedUpdate(m));

            }
            mAdapter.notifyDataSetChanged();
            getSupportActionBar().setTitle(currentChannel);
            mDrawerLayout.closeDrawer(LEFT);

        });


        adaptermc.setOnItemClickLister((v, position) -> {

            Toast.makeText(ConversationActivity.this, "You clicked at position: " + position, Toast.LENGTH_SHORT).show();
            currentChannel = mcItems.get(position).getName();
            messages.clear();

            for (Message m : mcItems.get(position).getMessages()
                    ) {
                if (m.getSender().equalsIgnoreCase(app.getUserName()))
                    messages.add(new MessageSentUpdate(m));
                else
                    messages.add(new MessageRecievedUpdate(m));

            }
            mAdapter.notifyDataSetChanged();
            getSupportActionBar().setTitle(currentChannel);
            mDrawerLayout.closeDrawer(LEFT);
        });


        et_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(""))
                    send_btn.setEnabled(false);
                else
                    send_btn.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(LEFT);
                return true;

            case R.id.action_favorite:
                findViewById(R.id.add_new_channel).setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(LEFT))
            mDrawerLayout.closeDrawer(LEFT);
        else
            super.onBackPressed();
    }

    public void addChannel(View v) {
        EditText et = findViewById(R.id.input_channel);
        if (et.getText().toString().isEmpty()) {
            et.setError("please write channel's name");
        } else {
            try {
                mcItems.add(gc.addNewLocalChannel(et.getText().toString()));
                adaptermc.notifyDataSetChanged();
            } catch (UtilException e) {
                e.printStackTrace();
            }
            findViewById(R.id.add_new_channel).setVisibility(View.GONE);
        }
    }

    public void sendMessage(View v) {

        if (currentChannel == null || currentChannel.isEmpty())
            et_chat.setError("open the drawer menu and click on a channel to send message");
        if(et_chat.getText().toString().isEmpty())
            et_chat.setError("You can't send an empty message");

        else {
            Message msg = gc.sendMessageOnChannel(et_chat.getText().toString(), currentChannel);
           //messages.add(new MessageSentUpdate(msg));
            //mAdapter.notifyDataSetChanged();
        }
    }

}
