package com.example.rupali.jsonplaceholder;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by RUPALI on 12-03-2018.
 */

public class PostAsynClass extends AsyncTask<String,Void,ArrayList<Post>>{
    public interface OnDownloadComplete{
        void onDownloadcomplete(ArrayList<Post> arrayList);
    }
    OnDownloadComplete listener;

    public PostAsynClass(OnDownloadComplete listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Post> doInBackground(String... strings) {
        String postUrl=strings[0];
        try {
            URL url=new URL(postUrl);
            HttpsURLConnection httpsURLConnection= (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
            InputStream inputStream=httpsURLConnection.getInputStream();
            Scanner scanner=new Scanner(inputStream);
            String result="";
            while (scanner.hasNext()){
                result=result.concat(scanner.next());
            }
            Log.d("Post result",result);
            ArrayList<Post> posts=parseResult(result);
            return posts;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private ArrayList<Post> parseResult(String result) throws JSONException {
        ArrayList<Post> arrayList=new ArrayList<>();
        JSONArray rootArray=new JSONArray(result);
        for (int i=0;i<rootArray.length();i++){
            JSONObject post=rootArray.getJSONObject(i);
            int userid=post.getInt("userId");
            int id = post.getInt("id");
            String title = post.getString("title");
            String body = post.getString("body");
            Log.d("Post","userid: "+userid+" title: "+title+" id: "+id+" body: "+body);
            Post post1 = new Post(id, userid, title, body);
            arrayList.add(post1);

        }
        Log.d("size",arrayList.size()+"");
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Post> posts) {
        super.onPostExecute(posts);
        listener.onDownloadcomplete(posts);
    }
}
