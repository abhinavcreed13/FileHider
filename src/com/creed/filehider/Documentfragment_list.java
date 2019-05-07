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
import com.creed.filehider.ImageGridActivity.ImageAdapter;
import com.creed.filehider.ImageGridActivity.ImageAdapter.ViewHolder;
import com.creed.filehider.Musicfragment_list.MultiChoiceModeListener;
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

public class Documentfragment_list extends Fragment {
	String[] imageUrls;
	protected AbsListView listView;
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	Object[] real_images;
	String path=null;
	File dir4=null;
	GridView gridview1;
	ImageAdapter imageadapter;
	SQLcontroller controller;
	public static ArrayList<String> images_to_show=new ArrayList<String>();
	public Documentfragment_list(){}
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
       
        try
        {
        change();
        }
        catch(Exception e)
        {
        }
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
	
	controller.getAlldocuments();
    create_array();
	listView = (ListView) rootView.findViewById(R.id.listview_video);
	listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	listView.setMultiChoiceModeListener(new MultiChoiceModeListener());
	imageadapter=new ImageAdapter();
	((ListView) listView).setAdapter(imageadapter);
	listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String pname=(String)real_images[position];
			String f_name=FilenameUtils.getName(pname);
			String fname=FilenameUtils.removeExtension(f_name);
			//Toast.makeText(getActivity(), fname, Toast.LENGTH_SHORT).show();
			path=controller.GetFilePath(fname);
			String type=controller.GetFileType(fname);
			String ext=controller.GetFileExt(fname);
			//path=path.replace(".fhd", "."+ext);
			File dir=Environment.getExternalStorageDirectory();
			String dir2=dir+"/Android/data/com.creed.FileHider/Cache/";
			File dir3=new File(dir2);
			if(!dir3.isDirectory())
			{
			dir3.mkdir();
			}
			try
			{
			File ur=new File(path);
			String dir5=dir+"/Android/data/com.creed.filehider/"+fname+"."+ext;
			dir4=new File(dir5);
			ur.renameTo(dir4);
			}
			catch(Exception e)
			{
				Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
		        
			}
		   // String type=controller.GetFiletypeLOC(position,TYPEall);
	        //Toast.makeText(getActivity(),path,Toast.LENGTH_SHORT).show();
	        
             	File file = new File(dir4.getAbsolutePath());
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                if(type.compareTo("Pictures")==0)
                {
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                }
                else if(type.compareTo("Videos")==0)
                {
               	intent.setDataAndType(Uri.fromFile(file), "video/*");  	 
                }
                else if(type.compareTo("Documents")==0)
                {
                intent.setDataAndType(Uri.fromFile(file), "text/plain");  
                }
                else if(type.compareTo("Music")==0)
                {
                intent.setDataAndType(Uri.fromFile(file), "audio/*");  
                }
                else
                {
                intent.setDataAndType(Uri.fromFile(file), "*/*");
               }
               startActivityForResult(intent, 10);
	}
	
	
	
});
	return rootView;
}
	public void change() {
		try{
	File dir=Environment.getExternalStorageDirectory();
	String path=dir+"/Android/data/com.creed.filehider/";
	File fil=new File(path);
	String[] arr=fil.list();
	for(int i=0;i<arr.length;i++)
	{
		String ext=FilenameUtils.getExtension(arr[i]);
		if(!ext.equals("fhd"))
		{
     String f=FilenameUtils.removeExtension(arr[i]);
     String path2=path+f+".fhd";
     String prev=path+arr[i];
     File p1=new File(prev);
     File p2=new File(path2);
     p1.renameTo(p2);
  	}
}
	}
        catch(Exception e)
        {
        	Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
	}
private void create_array() {
		// TODO Auto-generated method stub
		real_images=images_to_show.toArray();
		
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
		return real_images.length;
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
		String pname=(String)real_images[position];
		String name=FilenameUtils.getName(pname);
		String f_name=FilenameUtils.removeExtension(name);
		holder.text.setText(f_name);
		File dir=Environment.getExternalStorageDirectory();
		String dir2=dir+"/Android/data/com.creed.filehider/internal.fhd/word7.png";
		imageLoader.displayImage("file:///"+dir2, holder.imageView, options, animateFirstListener);
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
public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
	ArrayList<String> file_name=new ArrayList<String>();
	ArrayList<String> file_share=new ArrayList<String>();
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		mode.setTitle("Choose Options!");
		mode.setSubtitle("One item selected");
		 mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
		 file_name.clear();
		 file_share.clear();
		return true;
	}
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return true;
	}

	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
        case R.id.item_delete:
        	for(String s:file_name)
        	{
        	//Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                
        	String srcpath=controller.GetFilePath(s);
        	String trtpath=controller.GetFilesrcPath(s);
          //	Toast.makeText(getActivity(), srcpath, Toast.LENGTH_SHORT).show();
          //	Toast.makeText(getActivity(), trtpath, Toast.LENGTH_SHORT).show();
            
        	File p1=new File(srcpath);
        	File p2=new File(trtpath);
        	try
        	{
        	FileUtils.moveFile(p1, p2);
        	controller.deletePin(s,"Documents");
        	controller.getAlldocuments();
        	create_array();
        	imageadapter.notifyDataSetChanged();
        	Toast.makeText(getActivity(), "Your file is visible now!", Toast.LENGTH_SHORT).show();
            
        	}
        	catch(Exception e)
        	{
        		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                
        	}
        	mode.finish(); // Action picked, so close the CAB
        	}
        	return true;
           
        case R.id.menu_item_share:
        	  	//Toast.makeText(getActivity(), "Value"+siz, Toast.LENGTH_SHORT).show();
            	try {
            		String url=controller.GetFilePath(file_share.get(0));
        			String ext2=controller.GetFileExt(file_share.get(0));
        			File dir=Environment.getExternalStorageDirectory();
        			File ur=new File(url);
        			String dir5=dir+"/Android/data/com.creed.filehider/"+file_share.get(0)+"."+ext2;
        			dir4=new File(dir5);
        			ur.renameTo(dir4);
            		String url2=dir5;
                    File myFile = new File(url2);
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String ext=myFile.getName().substring(myFile.getName().lastIndexOf(".")+1);
                    String type = mime.getMimeTypeFromExtension(ext);
                    Intent sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType(type);
                        sharingIntent.putExtra("android.intent.extra.STREAM",Uri.fromFile(myFile));
                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
            		 }
                catch(Exception e){
                    Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        	return true;
       }
	
	public void onDestroyActionMode(ActionMode mode) {
	}

	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		String pname=(String)real_images[position];
		String f_name=FilenameUtils.getName(pname);
		String fname=FilenameUtils.removeExtension(f_name);
		file_name.add(fname);
		file_share.add(fname);
		int selectCount = listView.getCheckedItemCount();
		switch (selectCount) {
		case 1:
			mode.setSubtitle("One item selected");
			break;
		default:
			mode.setSubtitle("" + selectCount + " items selected");
			break;
		}
	}
}
}
