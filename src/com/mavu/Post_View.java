package com.mavu;

import com.mavu.appcode.Post;

import android.app.Activity;
import android.os.Bundle;

public class Post_View extends Activity {
	
	private Post selectedPost;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view_layout);
        
        
        //Todo: We will have to serialize the object i guess to pass it...Rhode? You had fun with this right?
        //selectedPost = new Post(getIntent().getStringExtra("SelectedPost"));
    }

	

}
