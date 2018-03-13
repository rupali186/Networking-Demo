package com.example.rupali.jsonplaceholder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    PostAdapter adapter;
    ArrayList<Post> posts;
    Bundle bundle;
    OpenHelper openHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=findViewById(R.id.postListView);
        openHelper=OpenHelper.getOpenHelper(this);
        progressBar=findViewById(R.id.postProgressBar);
        posts=new ArrayList<>();
        adapter=new PostAdapter(this,posts);
        listView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent intent=getIntent();
        bundle=intent.getExtras();
        //adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,posts);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startCommentActivity(i);
            }
        });
        if(bundle!=null){
            fetchDataFromDb();
            if(posts.size()==0) {
                fetchDataforPost();
            }
        }
    }

    private void fetchDataFromDb() {
        int userid=bundle.getInt(Constants.userId);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        String []selectionArgs={userid+""};
        Cursor cursor=database.query(Contract.ContractPost.TABLE_NAME,null,Contract.ContractPost.USERID+" =? ",selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(Contract.ContractPost.ID));
            String title=cursor.getString(cursor.getColumnIndex(Contract.ContractPost.TITLE));
            String body=cursor.getString(cursor.getColumnIndex(Contract.ContractPost.BODY));
            Post post=new Post(id,userid,title,body);
            posts.add(post);
            adapter.notifyDataSetChanged();
        }
    }

    private void startCommentActivity(int position) {
        int postid=posts.get(position).getId();
        Intent intent=new Intent(this,CommentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt(Constants.postId,postid);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void fetchDataforPost() {
        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final SQLiteDatabase database=openHelper.getWritableDatabase();
        int userid=bundle.getInt(Constants.userId);
        String url="https://jsonplaceholder.typicode.com/posts?userId="+userid;
        PostAsynClass postAsynClass=new PostAsynClass(new PostAsynClass.OnDownloadComplete() {
            @Override
            public void onDownloadcomplete(ArrayList<Post> arrayList) {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                for (int i=0;i<arrayList.size();i++){
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.ContractPost.ID,arrayList.get(i).getId());
                    contentValues.put(Contract.ContractPost.USERID,arrayList.get(i).getUserId());
                    contentValues.put(Contract.ContractPost.TITLE,arrayList.get(i).getTitle());
                    contentValues.put(Contract.ContractPost.BODY,arrayList.get(i).getBody());
                    int id= (int) database.insert(Contract.ContractPost.TABLE_NAME,null,contentValues);
                    posts.add(arrayList.get(i));
                    adapter.notifyDataSetChanged();
                }
            }
        });
        postAsynClass.execute(url);
    }

}
