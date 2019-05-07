package com.creed.filehider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.creed.filehider.Constants.*;
import com.creed.filehider.ImageGridActivity.ImageAdapter;
import com.creed.filehider.ImageGridActivity.ImageAdapter.ViewHolder;
import com.creed.filehider.PagesFragment.MultiChoiceModeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Videosfragment extends Fragment {
	String[] imageUrls;
	protected AbsListView listView;
	DisplayImageOptions options;
	Dialog dialog;
	String path=null;
	File dir4=null;
	GridView gridview1;
	ImageAdapter imageadapter;
	private Handler handler;
	SQLcontroller controller;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
public static ArrayList<String> fnames=new ArrayList<String>();
	String [] images;
	Object[] f_name;
			Object[] real_images;
	Object[] images_thumbs;
	public static ArrayList<String> images_to_show=new ArrayList<String>();
	ArrayList<String> images_thumb=new ArrayList<String>();
	public Videosfragment(){}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = new SQLcontroller(activity);
    }
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		

		dialog = new Dialog(getActivity());
		
        View rootView = inflater.inflate(R.layout.ac_video_grid, container, false);
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
	
	 controller.getAllvideos();
	 create_array();
	 controller.getAllvideos_file();
	 create_arr_names();
	 getvideothumbnail();
    setvideothumbnail();
   create_array_thumb();
   // hide_dialog();
	
	listView = (GridView) rootView.findViewById(R.id.gridview_video);
	
	gridview1=(GridView) rootView.findViewById(R.id.gridview_video);
	gridview1.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
	gridview1.setMultiChoiceModeListener(new MultiChoiceModeListener());
	imageadapter=new ImageAdapter();
	((GridView) listView).setAdapter(imageadapter);
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
	
private void change() {
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
private void create_arr_names() {
		// TODO Auto-generated method stub
		f_name=fnames.toArray();
	}
private void create_array_thumb() {
	images_thumbs=images_thumb.toArray();	
	}
private void hide_dialog() {
		// TODO Auto-generated method stub
	dialog.hide();	
	}

private void create_dialog() {
		// TODO Auto-generated method stub

	dialog.setContentView(R.layout.progressbar);
	dialog.setTitle("Loading..Please wait!");

		dialog.show();
  
	}

private void create_array() {
		// TODO Auto-generated method stub
		real_images=images_to_show.toArray();
	}

private void setvideothumbnail() {
		// TODO Auto-generated method stub
	images_thumb.clear();
	File dsc = Environment.getExternalStorageDirectory();
	String dsc2 = dsc + "/Android/data/com.creed.filehider/thumbs.fhd/";
	File fil=new File(dsc2);
	for (File f : fil.listFiles()) {
	    if (f.isFile())
	    {
	        images_thumb.add(dsc2+f.getName());
	    }   
	}
	}

private void getvideothumbnail() {
	
	File dsc = Environment.getExternalStorageDirectory();
	String dsc2 = dsc + "/Android/data/com.creed.filehider/thumbs.fhd/";
	File fil=new File(dsc2);
	if(!fil.isDirectory())
	{
	fil.mkdir();
	}
	for(int i=0;i<real_images.length;i++)
	{
	Bitmap bit=ThumbnailUtils.createVideoThumbnail(real_images[i].toString(), Images.Thumbnails.MINI_KIND);
	String fname=f_name[i].toString();
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
	@Override
	public int getCount() {
		return images_thumbs.length;
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
			view = getActivity().getLayoutInflater().inflate(R.layout.item_grid_video, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.imageView = (ImageView) view.findViewById(R.id.image_video);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress_video);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		imageLoader.displayImage("file:///"+images_thumbs[position], holder.imageView, options, new SimpleImageLoadingListener() {
									 @Override
									 public void onLoadingStarted(String imageUri, View view) {
										 holder.progressBar.setProgress(0);
										 holder.progressBar.setVisibility(View.VISIBLE);
									 }

									 @Override
									 public void onLoadingFailed(String imageUri, View view,
											 FailReason failReason) {
										 holder.progressBar.setVisibility(View.GONE);
									 }

									 @Override
									 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										 holder.progressBar.setVisibility(View.GONE);
									 }
								 }, new ImageLoadingProgressListener() {
									 @Override
									 public void onProgressUpdate(String imageUri, View view, int current,
											 int total) {
										 holder.progressBar.setProgress(Math.round(100.0f * current / total));
									 }
								 }
		);

		 return view;
	}

	class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
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
        	controller.deletePin(s,"Videos");
        	remove_thumb(s);
        	controller.getAllvideos();
        	create_array();
        	controller.getAllvideos_file();
        	create_arr_names();
        	getvideothumbnail();
           setvideothumbnail();
           	create_array_thumb();
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
	
	private void remove_thumb(String s) {
	File dir=Environment.getExternalStorageDirectory();
	String dir2=dir+"/Android/data/com.creed.filehider/thumbs.fhd/"+s;
	File dir3=new File(dir2);
		FileUtils.deleteQuietly(dir3);
		
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
		int selectCount = gridview1.getCheckedItemCount();
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


