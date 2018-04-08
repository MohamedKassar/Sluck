package fr.upmc.sluck.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.upmc.sluck.R;
import fr.upmc.sluck.model.MessageRecievedUpdate;
import fr.upmc.sluck.model.MessageSentUpdate;
import fr.upmc.sluck.controllers.Update;
import fr.upmc.sluck.model.UserStatusUpdate;

/**
 * Created by labib on 20/03/2018.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.Holder> {

    private List<Update> messages;


    public MessageListAdapter(List<Update> messages) {
        this.messages = messages;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView dateView, nameView, messageView, userupdate;

        public Holder(View itemView, Update.Type viewType) {
            super(itemView);
            switch (viewType) {
                case MSG_SENT:
                    dateView = itemView.findViewById(R.id.text_message_time);
                    messageView = itemView.findViewById(R.id.text_message_body);
                    break;
                case USER_LEFT:
                    userupdate = itemView.findViewById(R.id.user_status);
                    break;
                case MSG_RECEIVED:
                    dateView = itemView.findViewById(R.id.text_message_time);
                    nameView = itemView.findViewById(R.id.text_message_name);
                    messageView = itemView.findViewById(R.id.text_message_body);
                    break;
                case USER_ENTERED:
                    userupdate = itemView.findViewById(R.id.user_status);
                    break;
            }
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v=null ;
        Update.Type t=null;
        switch (viewType) {
            case 1:
                t= Update.Type.valueOf(Update.Type.MSG_RECEIVED.name());
                v =LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                break;
            case 2:
                t= Update.Type.valueOf(Update.Type.MSG_SENT.name());
                v =LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                break;
            case 3:
                t= Update.Type.valueOf(Update.Type.USER_ENTERED.name());
                v =LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_status, parent, false);
                break;
            case 4:
                t= Update.Type.valueOf(Update.Type.USER_LEFT.name());
                v =LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_status, parent, false);
                break;

        }
        return new Holder(v,t);
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType().getValue();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        switch (messages.get(position).getType()){

            case MSG_SENT:
                holder.dateView.setText(((MessageSentUpdate)messages.get(position)).getMsg().getDate().toString());
                holder.messageView.setText(((MessageSentUpdate)messages.get(position)).getMsg().getText().toString());
                break;
            case USER_LEFT:
                holder.userupdate.setText(((UserStatusUpdate)messages.get(position)).getUser().getName()+" has left the channel");
                break;
            case MSG_RECEIVED:
                holder.dateView.setText(((MessageRecievedUpdate)messages.get(position)).getMsg().getDate().toString());
                holder.messageView.setText(((MessageRecievedUpdate)messages.get(position)).getMsg().getText().toString());
                holder.nameView.setText(((MessageRecievedUpdate)messages.get(position)).getMsg().getSender().toString());
                break;
            case USER_ENTERED:
                holder.userupdate.setText(((UserStatusUpdate)messages.get(position)).getUser().getName()+" has entered the channel");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
