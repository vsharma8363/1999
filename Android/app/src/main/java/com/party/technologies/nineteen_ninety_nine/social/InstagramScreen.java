package com.party.technologies.nineteen_ninety_nine.social;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InstagramScreen extends AppCompatActivity {

    private WebView myWebView;
    private RequestQueue queue;
    private String instagramUID;
    private String instagramToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(InstagramScreen.this);
        ArrayList<String> res = new ArrayList<String>();
        setContentView(R.layout.activity_instagram_screen);
        myWebView = (WebView)findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("https://api.instagram.com/oauth/authorize" +
                "?client_id=1285596391901986" +
                "&redirect_uri=https://github.com/vsharma8363/1999/" +
                "&scope=user_profile,user_media" +
                "&response_type=code");
        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                String redirected_url = view.getUrl();
                if (redirected_url.contains("https://github.com/vsharma8363/1999/?code=")) {
                    String[] split = redirected_url.split("code=");
                    String[] split_2 = split[1].split("#_");
                    String code = split_2[0];
                    String postUrl = "https://api.instagram.com/oauth/access_token";
//                    RequestQueue queue = Volley.newRequestQueue(InstagramScreen.this);

                    StringRequest request = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                instagramUID = json.get("user_id").toString();
                                instagramToken = json.get("access_token").toString();
                                // Get profile username.
                                setInstagramHandle();
                                // Get all photos.
                                getInstagramPhotos();
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> postData = new HashMap<String, String>();
                            postData.put("client_id", "1285596391901986");
                            postData.put("client_secret", "5d3d52728a7ab07549fd604c70bdb523");
                            postData.put("grant_type", "authorization_code");
                            postData.put("redirect_uri", "https://github.com/vsharma8363/1999/");
                            postData.put("code", code);
                            return postData;
                        }
                    };

                    queue.add(request);
                }
                return false;
            }
        });
    }

    private String setInstagramHandle() {
        String url = "https://graph.instagram.com/" + instagramUID + "?fields=id,username&access_token=" + instagramToken;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject obj = new JSONObject(response);
                    UserInterface.getCurrentUser().setInstagramUserName(obj.get("username").toString());
                    finish();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
        return null;
    }

    private ArrayList<String> getInstagramPhotos() {
        // null if no photos exist.
        return null;
    }
}