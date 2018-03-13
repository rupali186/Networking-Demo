package com.example.rupali.jsonplaceholder;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

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

public class CommentAsynClass extends AsyncTask<String,Void,ArrayList<Comments>> {
    public interface OnDownload{
        void onDownloadComplete(ArrayList<Comments> comments);
    }
    OnDownload listener;

    public CommentAsynClass(OnDownload listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Comments> doInBackground(String... strings) {
        String commentUrl=strings[0];
        try {
            URL url=new URL(commentUrl);
            HttpsURLConnection httpsURLConnection= (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
            InputStream inputStream=httpsURLConnection.getInputStream();
            Scanner scanner=new Scanner(inputStream);
            String result="";
            while (scanner.hasNext()){
               result=result.concat(scanner.next());
            }
            Log.d("result: ",result);
            ArrayList<Comments> arrayList=parseResult(result);
            return arrayList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private ArrayList<Comments> parseResult(String result) throws JSONException {
        ArrayList<Comments> arrayList=new ArrayList<>();
        JSONArray rootArray=new JSONArray(result);
        for (int i=0;i<rootArray.length();i++){
            JSONObject object=rootArray.getJSONObject(i);
            int postId=object.getInt("postId");
            int id=object.getInt("id");
            String email=object.getString("email");
            String body=object.getString("body");
            String name=object.getString("name");
            Comments comments=new Comments(postId,id,name,email,body);
            arrayList.add(comments);
            Log.d(" body i ",""+body);
        }
        Log.d("size: ",arrayList.size()+"'");
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Comments> comments) {
        super.onPostExecute(comments);
        listener.onDownloadComplete(comments);
    }
}
