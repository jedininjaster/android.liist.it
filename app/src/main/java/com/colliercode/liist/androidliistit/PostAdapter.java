package com.colliercode.liist.androidliistit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Steven on 11/29/2014.
 */
public class PostAdapter extends ArrayAdapter<Post> {


    public PostAdapter(Context context, int resource, ArrayList<Post> mPosts) {
        super(context, resource, mPosts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_list_item, parent, false);
        }

        Post post = getItem(position);

        TextView postTitleTextView = (TextView) convertView.findViewById(R.id.postListItemTitleTextView);

        postTitleTextView.setText(post.mTitle);

        //image call

        //need to make a local variable, all the images are being painted on the last post
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        //need to null check
        URL url = post.mImageUrls.get(0);
        new getImage(url, imageView).execute();

        return convertView;

    }

    //wierd issue with using anythong other than object for input
    private class getImage extends AsyncTask<Object, Void, Bitmap> {

        URL mUrl;
        ImageView mImageView;

        getImage(URL url, ImageView imageView){
            this.mUrl = url;
            this.mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Object... urls) {
            try {

                //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
                HttpGet httpRequest;
                URI uri = mUrl.toURI();
                httpRequest = new HttpGet(uri);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                return BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                Log.e("image fetch", "error");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
