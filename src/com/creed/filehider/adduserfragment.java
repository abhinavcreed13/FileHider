package com.creed.filehider;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.creed.filehider.Constants.*;
import com.creed.filehider.Documentfragment_list.MultiChoiceModeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Images;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class adduserfragment extends Fragment implements OnClickListener {
	EditText username;
	EditText password;
	Button login;
	public adduserfragment(){}
	SQLcontroller controller;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = new SQLcontroller(activity);
    }
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.adduser, container, false);
        username = (EditText)rootView.findViewById(R.id.editText_add);
        password = (EditText)rootView.findViewById(R.id.editText_add2);
        login = (Button)rootView.findViewById(R.id.button_add);
        login.setOnClickListener((OnClickListener) this);
        return rootView;
}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.button_add:
			String name=username.getText().toString();
			String pass=password.getText().toString();
			try
			{
			String temp=controller.getuser();
			controller.updateuser(name, pass,temp);
			Toast.makeText(getActivity(), "Updated successfully!!", Toast.LENGTH_SHORT).show();
			}
			catch(Exception e)
			{
				Toast.makeText(getActivity(), "Username name already found!!", Toast.LENGTH_SHORT).show();
			}
			break;
			default:
				break;
		}
		
	}
		}

