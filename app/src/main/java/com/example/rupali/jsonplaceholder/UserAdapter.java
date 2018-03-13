package com.example.rupali.jsonplaceholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RUPALI on 12-03-2018.
 */

public class UserAdapter extends BaseAdapter {
    ArrayList<User> users;
    Context context;

    public UserAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view=convertView;
        User user=users.get(i);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.user_list_item,parent,false);
            Holder holder=new Holder();
            holder.userName=view.findViewById(R.id.userName);
            holder.email=view.findViewById(R.id.email);
            holder.name=view.findViewById(R.id.name);
            view.setTag(holder);

        }
        Holder holder=(Holder)view.getTag();
        holder.userName.setText(user.getUserName());
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        return view;
    }
    class Holder{
        TextView userName;
        TextView name;
        TextView email;
    }
}
