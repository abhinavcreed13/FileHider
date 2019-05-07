/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.creed.filehider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.creed.filehider.adapter.NavDrawerListAdapter;
import com.creed.filehider.model.NavDrawerItem;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class HomeActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public int pos_il;
	String name, name2, filename, pathname, ptype, pext;
	 public ProgressDialog progress3;
	  public static final int progress_bar_type = 0; 
	  SQLcontroller controller=new SQLcontroller(this);
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_home);
		/*Intent intent = new Intent(this, ImageGridActivity.class);
		intent.putExtra(Extra.IMAGES, IMAGES);
		startActivity(intent);*/
		File dsc = Environment.getExternalStorageDirectory();
		String dsc2 = dsc + "/Android/data/com.creed.filehider/";
		String path="/Android/data/com.creed.filehider/";
		//String dsc3 = dsc + "/PicsArt/";
	   
		File fil2 = new File(dsc2);
		if(!fil2.isDirectory())
		{
		fil2.mkdir();
		}
		File fil3=new File(dsc2+"internal.fhd");
		if(!fil3.isDirectory())
		{
		fil3.mkdir();
		}
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String action = intent.getAction();

		

			mTitle = mDrawerTitle = getTitle();

			// load slide menu items
			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

			// nav drawer icons from resources
			navMenuIcons = getResources()
					.obtainTypedArray(R.array.nav_drawer_icons);

			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

			navDrawerItems = new ArrayList<NavDrawerItem>();

			// adding nav drawer items to array
			// Home
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
			// Find People
			
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1),true,controller.getcount("Pictures")));
			// Photos
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1),true,controller.getcount("Videos")));
			// Communities, Will add a counter here
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true,controller.getcount("Music")));
			// Pages
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1),true,controller.getcount("Documents")));
			// What's hot, We  will add a counter here
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(2, -1), true,controller.getcount("Others")));
			
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(1, -1), true,controller.getcount("Others")));
			
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(3, -1), true,controller.getcount("Others")));
			
			// Recycle the typed array
			navMenuIcons.recycle();

			mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

			// setting the nav drawer list adapter
			adapter = new NavDrawerListAdapter(getApplicationContext(),
					navDrawerItems);
			mDrawerList.setAdapter(adapter);

			// enabling action bar app icon and behaving it as toggle button
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer, //nav menu toggle icon
					R.string.app_name, // nav drawer open - description for accessibility
					R.string.app_name // nav drawer close - description for accessibility
			) {
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(mTitle);
					// calling onPrepareOptionsMenu() to show action bar icons
					invalidateOptionsMenu();
				}

				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(mDrawerTitle);
					// calling onPrepareOptionsMenu() to hide action bar icons
					invalidateOptionsMenu();
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			if (savedInstanceState == null) {
				// on first time display view for first nav item
				displayView(0);
			}
			if (Intent.ACTION_SEND.equals(action)) {
				if (extras.containsKey(Intent.EXTRA_STREAM)) {
					// Get resource path
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
					pathname = parseUriToFilepath(uri);
					filename = new File(pathname).getName();
					String filename_noext = FilenameUtils.removeExtension(filename);
					String file_ext=FilenameUtils.getExtension(filename);
					//Toast.makeText(HomeActivity.this,file_ext, Toast.LENGTH_SHORT).show();
					File src = new File(pathname);
					//Toast.makeText(HomeActivity.this,"Path"+src,Toast.LENGTH_SHORT).show();
					dsc2 = dsc2 + "/" + filename_noext+".fhd";
					//dsc2=dsc2+filename;
					File fil = new File(dsc2);
					//Toast.makeText(HomeActivity.this, "Path" + fil,Toast.LENGTH_SHORT).show();
					String filetype;
					try {
						//copyFile(src, fil);
						//boolean val=copyfile("/"+filename,path+filename);
						//Toast.makeText(MainActivity.this, "del path:" + src,	Toast.LENGTH_SHORT).show();
						moving_file(src,fil);
						
						if ((file_ext.compareTo("jpg") == 0) || (file_ext.compareTo("png") == 0)
								|| (file_ext.compareTo("jpeg") == 0)
								|| (file_ext.compareTo("bmp") == 0)) {
							filetype="Pictures";

						} else if ((file_ext.compareTo("mp4") == 0)
								|| (file_ext.compareTo("avi") == 0)
								|| (file_ext.compareTo("mpeg") == 0)
								|| (file_ext.compareTo("mkv") == 0)) {
							filetype="Videos";
							
						} else if ((file_ext.compareTo("doc") == 0)
								|| (file_ext.compareTo("docx") == 0)
								|| (file_ext.compareTo("pdf") == 0)
								|| (file_ext.compareTo("xlsx") == 0)) {
							filetype="Documents";
							
						} else if ((file_ext.compareTo("mp3") == 0)
								|| (file_ext.compareTo("ffc") == 0)
								|| (file_ext.compareTo("flac") == 0)
								|| (file_ext.compareTo("m4a") == 0)) {
							filetype="Music";
						} else 
						{
							filetype="Others";
						}
						//Toast.makeText(HomeActivity.this,filetype, Toast.LENGTH_SHORT).show();
						insert_into_database(filename_noext,src.getAbsolutePath(),fil.getAbsolutePath(),file_ext,filetype);
						Boolean val=FileUtils.deleteQuietly(src);
						//Toast.makeText(HomeActivity.this, "del:"+val,Toast.LENGTH_LONG).show();
						//boolean del = src.delete();
						//FileUtils.moveFile(src, fil);
						//Toast.makeText(MainActivity.this,"value="+val,Toast.LENGTH_SHORT).show();
						// LOGIC - TO REFRESH GALLERY
						//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
						File dir=Environment.getExternalStorageDirectory();
						String dir2=dir.getAbsolutePath()+"/DCIM";
						File imageFile=new File(dir2);
						MediaScannerConnection.scanFile(this, new String[] { imageFile.getPath() }, null,
					              new MediaScannerConnection.OnScanCompletedListener() {
					                @Override
					                public void onScanCompleted(String path, Uri uri) {
					                  Log.i("Scanned ",path);
					                }
					              });
					} catch (Exception e) {
						Toast.makeText(HomeActivity.this, e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}
			}
			
		}
	 private void insert_into_database(String filename,String filesrc,String filetrt,String filext,String filetype) {
	 long rows=controller.insertStudent(filename, filesrc, filetrt, filext, filetype);
	 Toast.makeText(HomeActivity.this, "ROWS"+rows,
				Toast.LENGTH_LONG).show();
	}
	@Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case progress_bar_type: // we set this to 0
	            progress3 = new ProgressDialog(this);
	            progress3.setMessage("Hiding your file! Please Wait. . .");
	            progress3.setIndeterminate(false);
	            progress3.setMax(100);
	            progress3.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	            progress3.setCancelable(true);
	            progress3.show();
	            return progress3;
	        default:
	            return null;
	        }
	    }
	public String parseUriToFilepath(Uri uri) {
		String selectedImagePath = null;
		String filemanagerPath = uri.getPath();
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {

			// Here you will get a null pointer if cursor is null

			// This can be if you used OI file manager for picking the media

			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			selectedImagePath = cursor.getString(column_index);
		}

		if (selectedImagePath != null) {
			return selectedImagePath;

		} else if (filemanagerPath != null) {
			return filemanagerPath;
		}
		return null;
	}

	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {
	// display view for selected nav drawer item
	displayView(position);
	pos_il=position;
}
}
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new PagesFragment();
			break;
		case 2:
			fragment = new Videosfragment();
			break;
		case 3:
			fragment = new Musicfragment_list();
			break;
		case 4:
			fragment = new Documentfragment_list();
			break;
		case 5:
			fragment = new otherfragment();
			break;
		case 6:
			fragment=new adduserfragment();
			break;
		case 7:
			fragment=new about();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	
	private void moving_file(File src, File fil) {
		String url=src.getAbsolutePath();
		String dest=fil.getAbsolutePath();
		  new DownloadFileFromURL().execute(url,dest);
		
	}
	/* public void open(View view){
	      progress3.setMessage("Downloading Music :) ");
	      progress3.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	      progress3.setIndeterminate(false);
	      progress3.setMax(100);
	      progress3.show();
	    
	 }*/
	
public boolean copyfile(String src,String trgt)
{
	 boolean success=false;
	 File sd=Environment.getExternalStorageDirectory();
	
	File file = new File(sd,src);
	// Destination directory
	success = file.renameTo(new File(sd, trgt));
	return success;
}
	public String parseUritoFileName(Uri uri) {
		String pinfilename = "unknown";
		String selectedImagePath = null;
		// String filemanagerPath = uri.getPath();
		if (uri.getScheme().toString().compareTo("content") == 0) {

			// Here you will get a null pointer if cursor is null

			// This can be if you used OI file manager for picking the media

			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(uri, projection, null,
					null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				selectedImagePath = cursor.getString(column_index);
				pinfilename = uri.getLastPathSegment().toString();
			}
		}
		/*
		 * else if (uri.getScheme().compareTo("file")==0) { pinfilename =
		 * uri.getLastPathSegment().toString(); } else { pinfilename =
		 * pinfilename+"_"+uri.getLastPathSegment(); }
		 */
		if (pinfilename != null) {
			return pinfilename;

		}
		return null;
	}

	
public static void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}
	
	//[WORKING] - FUCK!! FUCK !! FUCK!! !!! !!
	public class DownloadFileFromURL extends AsyncTask<String, String, String> {
		 @SuppressWarnings("deprecation")
			@Override
	     protected void onPreExecute() {
	         super.onPreExecute();
	        showDialog(progress_bar_type);
	     }

	     /**
	      * Downloading file in background thread
	      * */
	     @Override
	     protected String doInBackground(String... f_url) {
	         int count;
	         try {
	             //URL url = new URL(f_url[0]);
	             //URLConnection conection = url.openConnection();
	             //conection.connect();
	             // this will be useful so that you can show a tipical 0-100% progress bar
	             File src1=new File(f_url[0]);
	             File desc=new File(f_url[1]);
	        	 long lenghtOfFile = src1.length();

	             // download the file
	             InputStream input = new FileInputStream(src1);

	             // Output stream
	             OutputStream output = new FileOutputStream(desc);

	             byte data[] = new byte[1024];

	             long total = 0;

	             while ((count = input.read(data)) != -1) {
	                 total += count;
	                 // publishing the progress....
	                 // After this onProgressUpdate will be called
	                 publishProgress(""+(int)((total*100)/lenghtOfFile));

	                 // writing data to file
	                 output.write(data, 0, count);
	             }

	             // flushing output
	             output.flush();

	             // closing streams
	             output.close();
	             input.close();

	         } catch (Exception e) {
	             Log.e("Error: ", e.getMessage());
	         }

	         return null;
	     }

	     /**
	      * Updating progress bar
	      * */
	     protected void onProgressUpdate(String... progress) {
	         // setting progress percentage
	         progress3.setProgress(Integer.parseInt(progress[0]));
	    }

	     /**
	      * After completing background task
	      * Dismiss the progress dialog
	      * **/
	     @SuppressWarnings("deprecation")
			@Override
	     protected void onPostExecute(String file_url) {
	         // dismiss the dialog after the file was downloaded
	         dismissDialog(progress_bar_type);

	         // Displaying downloaded image into image view
	         // Reading image path from sdcard
	         //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
	         // setting downloaded into image view
	         //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
	     }

	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	public void onBackPressed() {
		//imageLoader.stop();
		super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		
				case R.id.menu_item_view1:
					fragment = new Videofragment_list();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment).commit();

						// update selected item and title, then close the drawer
						mDrawerList.setItemChecked(pos_il, true);
						mDrawerList.setSelection(pos_il);
						setTitle(navMenuTitles[pos_il]);
						mDrawerLayout.closeDrawer(mDrawerList);
					} else 
					{
						// error in creating fragment
						Log.e("MainActivity", "Error in creating fragment");
					}
					return true;
				case R.id.menu_item_view2:
					fragment = new Videosfragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment).commit();

						// update selected item and title, then close the drawer
						mDrawerList.setItemChecked(pos_il, true);
						mDrawerList.setSelection(pos_il);
						setTitle(navMenuTitles[pos_il]);
						mDrawerLayout.closeDrawer(mDrawerList);
					} else {
						// error in creating fragment
						Log.e("MainActivity", "Error in creating fragment");
					}
					return true;
				
				default:
					return super.onOptionsItemSelected(item);
			
		}
		
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings2).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
}