package com.colliercode.liist.androidliistit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {

    ListView mListView;
    ArrayList<Post> mPosts = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: make a custom ListView that accepts click actions
        mListView = (ListView) findViewById(R.id.postsListView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_fetch) {

            String url = "http://10.0.2.2:3001/posts/";
            System.out.println(url);
            new DownloadTask().execute(url);

            return true;
        } else if (id == R.id.action_posts) {

            Intent intent = new Intent(MainActivity.this, postListActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setupAdapter() {
        mListView.setAdapter(new PostAdapter(this, R.layout.post_list_item, mPosts));
//        for(int i = 0; i < mPosts.size(); i++ ){
//
//        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //TODO: consider using an external http lib for calls, okhttp seems to have a bug in code below
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //todo: tidy up
            JSONArray mJsonArray;
            JSONObject obj;

            Log.i("Network", result);

            try {
                obj = new JSONObject(result);
                Log.d("My App", obj.toString());
            } catch (Throwable t) {
                //was hitting this error for a while, consider reducing the scope of theis catch block
                Log.e("My App", "Could not parse malformed JSON: " + result);
                return;
            }

            try {
                mJsonArray = obj.getJSONArray("posts");
                for (int i = 0; i < mJsonArray.length(); i++) {

                    //do some null checks, u fu, maybe

                    JSONObject jsonObject = mJsonArray.getJSONObject(i);
                    String postTitle = jsonObject.getString("title");
                    String postContent = jsonObject.getString("content");
                    JSONArray imageUrls = jsonObject.getJSONArray("images");

                    Post post = new Post();

                    //post images
                    post.mImageUrls = new ArrayList<URL>();
                    for (int j = 0; j < imageUrls.length(); j++) {
                        JSONObject imageJSON = imageUrls.getJSONObject(j);
                        URL url = new URL(imageJSON.getString("source"));
                        post.mImageUrls.add(url);
                    }

                    //post other stuff
                    post.mTitle = postTitle;
                    post.mContent = postContent;
                    mPosts.add(post);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            setupAdapter();
        }

        private String loadFromNetwork(String urlString) throws IOException {
            InputStream stream = null;
            String str = "";
            try {
                stream = downloadUrl(urlString);
                str = readIt(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return str;
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            return stream;
        }

        private String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
            return convertStreamToString(stream);
        }

        private String convertStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}

//
//DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
//HttpGet httpGet = new HttpGet(url);
//// Depends on your web service
////        httpGet.setHeader("Content-type", "application/json");
//
//InputStream inputStream = null;
//String result = null;
//try {
//        HttpResponse response = httpclient.execute(httpGet);
//        HttpEntity entity = response.getEntity();
//
//        inputStream = entity.getContent();
//        // json is UTF-8 by default
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        while ((line = reader.readLine()) != null)
//        {
//        sb.append(line + "\n");
//        System.out.println(line);
//        }
//        result = sb.toString();
//        } catch (Exception e) {
//        // Oops
//        }
//        finally {
//        try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
//        System.out.println(result);
//        }


//class HttpGuy extends AsyncTask {
//
//    String url = "http://10.0.2.2:3001/api/posts/";
//
//    OkHttpClient client = new OkHttpClient();
//
//    public void run() throws IOException {
//
//        System.out.println(url);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
//    }
//
//    @Override
//    protected Object doInBackground(Object[] params) {
//        try {
//            run();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}

