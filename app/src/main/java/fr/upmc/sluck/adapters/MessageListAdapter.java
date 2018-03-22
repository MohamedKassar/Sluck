package fr.upmc.sluck.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.upmc.sluck.R;
import fr.upmc.sluck.model.Message;

/**
 * Created by labib on 20/03/2018.
 */

public class MessageListAdapter  extends RecyclerView.Adapter<MessageListAdapter.Holder> {

    private  List<Message> messages;

    public MessageListAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public static  class Holder extends RecyclerView.ViewHolder{

        private final TextView dateView, nameView, messageView;
        public Holder(View itemView) {
            super(itemView);
            System.out.println("Holder===================");
            dateView = itemView.findViewById(R.id.text_message_time);
            nameView = itemView.findViewById(R.id.text_message_name);
            messageView = itemView.findViewById(R.id.text_message_body);
        }
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("Holder===================");
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_received, parent, false);
        return  new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        System.out.println(messages.toString()+"================");
        holder.dateView.setText(messages.get(position).getDate().toString());
        holder.nameView.setText(messages.get(position).getSender().toString());
        holder.messageView.setText(messages.get(position).getText().toString());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
