package com.example.rupali.jsonplaceholder;

import android.os.AsyncTask;

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
 * Created by RUPALI on 11-03-2018.
 */

public class UserAsynClass extends AsyncTask<String,Void,ArrayList<User>> {
    public  interface DownloadListener{
        void onDownloadComplete(ArrayList<User> users);
    }
    DownloadListener listener;

    public UserAsynClass(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<User> doInBackground(String... strings) {
        String jsonUrl=strings[0];
        try {
            URL url=new URL(jsonUrl);
            HttpsURLConnection httpsURLConnection=(HttpsURLConnection)url.openConnection();
            httpsURLConnection.connect();
            InputStream inputStream= httpsURLConnection.getInputStream();
            String result="";
            Scanner scanner=new Scanner(inputStream);
            while(scanner.hasNext()){
                result =result.concat(scanner.next());
            }
            ArrayList<User> arrayList=parseResult(result);
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

    @Override
    protected void onPostExecute(ArrayList<User> users) {
        super.onPostExecute(users);
        listener.onDownloadComplete(users);

    }

    private ArrayList<User> parseResult(String result) throws JSONException {
        ArrayList<User> arrayList=new ArrayList<>();
        JSONArray rootArray=new JSONArray(result);
        for(int i=0;i<rootArray.length();i++){
            JSONObject user=rootArray.getJSONObject(i);
            int id=user.getInt("id");
            String name=user.getString("name");
            String username=user.getString("username");
            String email=user.getString("email");
            User user1=new User(id,name,username,email);
            arrayList.add(user1);
        }

        return arrayList;
    }
}
