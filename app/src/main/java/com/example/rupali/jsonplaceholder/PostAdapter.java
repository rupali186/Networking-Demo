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

public class PostAdapter extends BaseAdapter {
    Context context;
    ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view=convertView;
        Post post=posts.get(i);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.post_list_item,parent,false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.title=view.findViewById(R.id.postTitle);
            viewHolder.body=view.findViewById(R.id.postBody);
            view.setTag(viewHolder);
        }
        ViewHolder viewHolder= (ViewHolder) view.getTag();
        viewHolder.title.setText(post.getTitle());
        viewHolder.body.setText(post.getBody());
        return view;
    }
    class ViewHolder{
        TextView title;
        TextView body;

    }

}
