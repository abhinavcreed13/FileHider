package com.creed.filehider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

public class Pagesfragment_list extends Fragment {
	String[] imageUrls;
	protected AbsListView listView;
	DisplayImageOptions options;
	public static ArrayList<String> images_to_show=new ArrayList<String>();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	Object [] images;
	public Pagesfragment_list(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.ac_image_list, container, false);
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

	listView = (ListView) rootView.findViewById(android.R.id.list);
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
		images=images_to_show.toArray();
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
			view = getActivity().getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
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
		imageLoader.displayImage("file:///"+images[position], holder.imageView, options, animateFirstListener);
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
