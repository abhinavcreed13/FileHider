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
import com.creed.filehider.ImageGridActivity.ImageAdapter;
import com.creed.filehider.ImageGridActivity.ImageAdapter.ViewHolder;
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
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class about extends Fragment {
	String[] imageUrls;
	protected AbsListView listView;
	DisplayImageOptions options;
	String path=null;
	File dir4=null;
	GridView gridview1;
	ImageAdapter imageadapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	Object[] real_images;
	public static ArrayList<String> images_to_show=new ArrayList<String>();
	public about(){}
	SQLcontroller controller;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = new SQLcontroller(activity);
    }
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.about, container, false);
	return rootView;
}
	
/*private void startImagePagerActivity(int position) {
	Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
	intent.putExtra(Extra.IMAGES, imageUrls);
	intent.putExtra(Extra.IMAGE_POSITION, position);
	startActivity(intent);
}*/

}
