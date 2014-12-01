package com.colliercode.liist.androidliistit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_list_item, parent, false);
        }

        Post post = getItem(position);

        TextView postTitleTextView = (TextView) convertView.findViewById(R.id.postListItemTitleTextView);

        postTitleTextView.setText(post.mTitle);

        return convertView;

    }
}
