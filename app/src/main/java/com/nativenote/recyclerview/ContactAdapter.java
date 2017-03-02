package com.nativenote.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by imtiaz on 3/2/17.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context context;
    private List<Contact> contactList;

    public ContactAdapter(Context context, List<Contact> contactList){
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.phone_number.setText(contact.getPhone_number());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, phone_number;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            phone_number = (TextView) view.findViewById(R.id.phone_number);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Clicked: " + contactList.get(getAdapterPosition()).getName(), Toast.LENGTH_LONG).show();
        }
    }
}
