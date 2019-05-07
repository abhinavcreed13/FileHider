package com.creed.filehider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.creed.filehider.Constants.*;
import com.creed.filehider.ImageGridActivity.ImageAdapter;
import com.creed.filehider.ImageGridActivity.ImageAdapter.ViewHolder;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Videofragment_list extends Fragment {
	String[] imageUrls;
	protected AbsListView listView;
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	String [] images;
			Object[] real_images;
			SQLcontroller controller;
	public static ArrayList<String> images_to_show=new ArrayList<String>();
	public Videofragment_list(){}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = new SQLcontroller(activity);
    }
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.ac_video_list, container, false);
        /*Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        intent.putExtra(Extra.IMAGES,Constants.IMAGES);
        getActivity().startActivity(intent);*/
       

        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

	/*Bundle bundle = getActivity().getIntent().getExtras();
	imageUrls = bundle.getStringArray(Extra.IMAGES);
*/
	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	//getvideothumbnail();
    setvideothumbnail();
    create_array();
	listView = (ListView) rootView.findViewById(R.id.listview_video);
	((ListView) listView).setAdapter(new ImageAdapter());
	listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//startImagePagerActivity(position);
		}
	});
	return rootView;
}
	private void create_array() {
		// TODO Auto-generated method stub
		real_images=images_to_show.toArray();
	}

private void setvideothumbnail() {
		// TODO Auto-generated method stub
	images_to_show.clear();
	File dsc = Environment.getExternalStorageDirectory();
	String dsc2 = dsc + "/Android/data/com.creed.pinme/";
	File fil=new File(dsc2);
	for (File f : fil.listFiles()) {
	    if (f.isFile())
	    {
	        images_to_show.add(dsc2+f.getName());
	    }   
	}
	}

private void getvideothumbnail() {
	
	File dsc = Environment.getExternalStorageDirectory();
	String dsc2 = dsc + "/Android/data/com.creed.pinme/";
	File fil=new File(dsc2);
	if(!fil.isDirectory())
	{
	fil.mkdir();
	}
	for(int i=0;i<images.length;i++)
	{
	Bitmap bit=ThumbnailUtils.createVideoThumbnail(images[i], Images.Thumbnails.MINI_KIND);
	Random generator = new Random();
	int n = 10000;
	n = generator.nextInt(n);
	String fname = "Image-" + n + ".jpg";
	File file = new File(fil, fname);
	try {
	FileOutputStream out = new FileOutputStream(file);
	bit.compress(Bitmap.CompressFormat.JPEG, 90, out);
	out.flush();
	out.close();
	}
	catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	}
/*private void startImagePagerActivity(int position) {
	Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
	intent.putExtra(Extra.IMAGES, imageUrls);
	intent.putExtra(Extra.IMAGE_POSITION, position);
	startActivity(intent);
}*/

public class ImageAdapter extends BaseAdapter {
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View view = convertView;
		if (view == null) {
			view = getActivity().getLayoutInflater().inflate(R.layout.item_list_video, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.imageView = (ImageView) view.findViewById(R.id.image);
			holder.text = (TextView) view.findViewById(R.id.text);
			//holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.text.setText("Item " + (position + 1));
		imageLoader.displayImage("file:///"+real_images[position], holder.imageView, options, animateFirstListener);
		 return view;
	}

	class ViewHolder {
		ImageView imageView;
		
		public TextView text;
	}
	
}
private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

	static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (loadedImage != null) {
			ImageView imageView = (ImageView) view;
			boolean firstDisplay = !displayedImages.contains(imageUri);
			if (firstDisplay) {
				FadeInBitmapDisplayer.animate(imageView, 500);
				displayedImages.add(imageUri);
			}
		}
	}
}
}
