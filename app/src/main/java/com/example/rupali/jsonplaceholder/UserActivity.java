package com.example.rupali.jsonplaceholder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<User> userArrayList;
    UserAdapter userAdapter;
    ProgressBar progressBar;
    OpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        listView=findViewById(R.id.listView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar=findViewById(R.id.progressBar);
        openHelper=OpenHelper.getOpenHelper(this);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        userArrayList=new ArrayList<>();
        userAdapter=new UserAdapter(userArrayList,this);
        listView.setAdapter(userAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromDb();
                if(userArrayList.size()==0) {
                    fetchDataForUser();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startPostActivity(i);
            }
        });
    }

    private void fetchDataFromDb() {
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.ContractUser.TABLE_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(Contract.ContractUser.ID));
            String name=cursor.getString(cursor.getColumnIndex(Contract.ContractUser.NAME));
            String username=cursor.getString(cursor.getColumnIndex(Contract.ContractUser.USERNAME));
            String email=cursor.getString(cursor.getColumnIndex(Contract.ContractUser.EMAIL));
            User user=new User(id,name,username,email);
            userArrayList.add(user);
            userAdapter.notifyDataSetChanged();
        }
    }

    private void startPostActivity(int position) {
        int userid=userArrayList.get(position).getId();
        Intent intent=new Intent(this,PostActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt(Constants.userId,userid);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void fetchDataForUser() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        String jsonUrl="https://jsonplaceholder.typicode.com/users";
        final SQLiteDatabase database=openHelper.getWritableDatabase();
        UserAsynClass userAsynClassObject =new UserAsynClass(new UserAsynClass.DownloadListener() {
            @Override
            public void onDownloadComplete(ArrayList<User> users) {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                for (int i=0;i<users.size();i++){
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.ContractUser.ID,users.get(i).getId());
                    contentValues.put(Contract.ContractUser.NAME,users.get(i).getName());
                    contentValues.put(Contract.ContractUser.USERNAME,users.get(i).getUserName());
                    contentValues.put(Contract.ContractUser.EMAIL,users.get(i).getEmail());
                    int id=(int)database.insert(Contract.ContractUser.TABLE_NAME,null,contentValues);
                    userArrayList.add(users.get(i));
                    userAdapter.notifyDataSetChanged();
                }
            }
        });
        userAsynClassObject.execute(jsonUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
