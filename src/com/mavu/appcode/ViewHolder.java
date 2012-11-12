package com.mavu.appcode;

import com.mavu.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {    
	    private View mRow;
	    private TextView description = null;
	    private TextView title = null;
	    private TextView date = null;
	    private ImageView image = null;

		public ViewHolder(View row) {
	    	mRow = row;
		}

		public TextView getDescription() {
			if(null == description){
				description = (TextView) mRow.findViewById(R.id.postDesc);
			}
			return description;
		}

		public TextView getTitle() {
			if(null == title){
				title = (TextView) mRow.findViewById(R.id.postTitle);
			}
			return title;
		}    
		
		public TextView getDate() {
			if(null == date){
				date = (TextView) mRow.findViewById(R.id.postDate);
			}
			return date;
		} 
		
		public ImageView getImage() {
			if(null == image){
				image = (ImageView) mRow.findViewById(R.id.postImage);
			}
			return image;
		} 
	}

