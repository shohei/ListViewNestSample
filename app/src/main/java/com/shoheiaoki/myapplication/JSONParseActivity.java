package com.shoheiaoki.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JSONParseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doJSONSerialize();
    }

    protected void doJSONSerialize() {
        try {
            JSONObject rootJSONObject = new JSONObject(getJSONString());
            JSONArray rootJSONArray = rootJSONObject.getJSONArray("categories");
            for (int i = 0; i < rootJSONArray.length(); i++) {
                JSONObject categoryJSONObject = rootJSONArray.getJSONObject(i);
                Log.e("hoge",categoryJSONObject.getString("name"));
                try {
                    JSONArray productJSONArray = new JSONArray(categoryJSONObject.getString("products"));
                    for(int j=0;j<productJSONArray.length();j++){
                        JSONObject productJSONObject = productJSONArray.getJSONObject(j);
                        Log.e("hoge",productJSONObject.getString("name"));
                        Log.e("hoge",productJSONObject.getString("price"));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJSONString() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.product_categories);
            byte[] buffer = new byte[is.available()];
            while((is.read(buffer)) != -1) {}
            json = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
