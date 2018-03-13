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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> commentArrayList;
    ArrayAdapter arrayAdapter;
    ProgressBar progressBar;
    Bundle bundle;
    OpenHelper openHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        openHelper=OpenHelper.getOpenHelper(this);
        progressBar=findViewById(R.id.commentProgressBar);
        listView=findViewById(R.id.commentListView);
        commentArrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,commentArrayList);
        listView.setAdapter(arrayAdapter);
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
        if(bundle!=null){
            fetchDataFromDb();
            if(commentArrayList.size()==0) {
                fetchDataForComment();
            }
        }
    }
    private void fetchDataFromDb() {
        int postid=bundle.getInt(Constants.postId);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        String []selectionArgs={postid+""};
        Cursor cursor=database.query(Contract.ContractComments.TABLE_NAME,null,Contract.ContractComments.POSTID+" =? ",selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(Contract.ContractComments.ID));
            String name=cursor.getString(cursor.getColumnIndex(Contract.ContractComments.NAME));
            String email=cursor.getString(cursor.getColumnIndex(Contract.ContractComments.EMAIL));
            String body=cursor.getString(cursor.getColumnIndex(Contract.ContractComments.BODY));
            Comments comments=new Comments(postid,id,name,email,body);
            commentArrayList.add(body);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void fetchDataForComment() {
        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        int postid=bundle.getInt(Constants.postId);
        String url="https://jsonplaceholder.typicode.com/comments?postId="+postid;
        CommentAsynClass commentAsynClass=new CommentAsynClass(new CommentAsynClass.OnDownload() {
            @Override
            public void onDownloadComplete(ArrayList<Comments> comments) {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                SQLiteDatabase database=openHelper.getWritableDatabase();
                for (int i=0;i<comments.size();i++){
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.ContractComments.ID,comments.get(i).getId());
                    contentValues.put(Contract.ContractComments.POSTID,comments.get(i).getPostId());
                    contentValues.put(Contract.ContractComments.NAME,comments.get(i).getName());
                    contentValues.put(Contract.ContractComments.BODY,comments.get(i).getBody());
                    contentValues.put(Contract.ContractComments.EMAIL,comments.get(i).getEmail());
                    int id= (int) database.insert(Contract.ContractComments.TABLE_NAME,null,contentValues);
                    commentArrayList.add(comments.get(i).getBody());
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        commentAsynClass.execute(url);
    }

}
