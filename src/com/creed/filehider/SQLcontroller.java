package com.creed.filehider;


import java.util.HashMap;

import android.util.Log;
import android.widget.Toast;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLcontroller extends SQLiteOpenHelper {

	 private static final String LOGCAT = null;
	 
	    public SQLcontroller(Context applicationcontext) {
	        super(applicationcontext, "filehider2.db", null, 1);
	        Log.d(LOGCAT,"Created");
	    }
	     
	    @Override
	    public void onCreate(SQLiteDatabase database) {
	        String query;
	        query = "CREATE TABLE FileHideitems (Filename TEXT PRIMARY KEY,Filesrcpath TEXT,Filetrtpath TEXT,Filext TEXT,Filetype TEXT)";
	        String query2="CREATE TABLE FileHideUser(FUname TEXT PRIMARY KEY,FUpassword TEXT)";
	        try
	        {
	        database.execSQL(query);
	        database.execSQL(query2);
	        Log.d(LOGCAT,"FileHideItems Created");
	 	   
	        }
	        catch(Exception e)
	        {
	        	 Log.e(LOGCAT,"Problem here!");
	      	   
	        }}
	    @Override
	    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
	        String query;
	        query = "DROP TABLE IF EXISTS Students";
	        database.execSQL(query);
	        onCreate(database);
	    }
	     
	    public long insertStudent(String fname,String fsrcpath,String ftrtpath,String fext,String ftype) {
	        SQLiteDatabase database = this.getWritableDatabase();
	       /* ContentValues values = new ContentValues();
	        values.put("PinID",sid);
	        values.put("PinPath",pname);
	        values.put("PinName",fname);
	        values.put("PinType",tname);
	        values.put("PinExt",exname);
	        values.put("PinLoc",loc);
	        database.insert("PinItems", null, values);*/
	        //LOGIC - solving Assassin's inserting issue!
	        String query="INSERT into FileHideitems(Filename,Filesrcpath,Filetrtpath,Filext,Filetype) values (?,?,?,?,?)";
	        SQLiteStatement stmt=database.compileStatement(query);
	        stmt.bindString(1, fname);
	        stmt.bindString(2, fsrcpath);
	        stmt.bindString(3, ftrtpath);
	        stmt.bindString(4, fext);
	        stmt.bindString(5, ftype);
	        long rows=stmt.executeInsert();
	        database.close();
	        return rows;
	        }
	    public long insertuser(String fname,String fpass) {
	        SQLiteDatabase database = this.getWritableDatabase();
	       /* ContentValues values = new ContentValues();
	        values.put("PinID",sid);
	        values.put("PinPath",pname);
	        values.put("PinName",fname);
	        values.put("PinType",tname);
	        values.put("PinExt",exname);
	        values.put("PinLoc",loc);
	        database.insert("PinItems", null, values);*/
	        //LOGIC - solving Assassin's inserting issue!
	        String query="INSERT into FileHideuser(FUname,FUpassword) values (?,?)";
	        SQLiteStatement stmt=database.compileStatement(query);
	        stmt.bindString(1, fname);
	        stmt.bindString(2, fpass);
	        long rows=stmt.executeInsert();
	        database.close();
	        return rows;
	        }
	    public long updateuser(String fname,String fpass,String puser) {
	        SQLiteDatabase database = this.getWritableDatabase();
	       /* ContentValues values = new ContentValues();
	        values.put("PinID",sid);
	        values.put("PinPath",pname);
	        values.put("PinName",fname);
	        values.put("PinType",tname);
	        values.put("PinExt",exname);
	        values.put("PinLoc",loc);
	        database.insert("PinItems", null, values);*/
	        //LOGIC - solving Assassin's inserting issue!
	        String query="UPDATE FileHideuser set FUname=?,FUpassword=? where FUname=?";
	        SQLiteStatement stmt=database.compileStatement(query);
	        stmt.bindString(1, fname);
	        stmt.bindString(2, fpass);
	        stmt.bindString(3, puser);
	        long rows=stmt.executeInsert();
	        database.close();
	        return rows;
	        }
	    public String getuser() {
	        SQLiteDatabase database = this.getWritableDatabase();
	        String val=null;
	       /* ContentValues values = new ContentValues();
	        values.put("PinID",sid);
	        values.put("PinPath",pname);
	        values.put("PinName",fname);
	        values.put("PinType",tname);
	        values.put("PinExt",exname);
	        values.put("PinLoc",loc);
	        database.insert("PinItems", null, values);*/
	        //LOGIC - solving Assassin's inserting issue!
	        String query="SELECT * from FileHideuser";
	        Cursor cursor = database.rawQuery(query, null);
	        if (cursor.moveToFirst()) {
	            do {
	                /*HashMap<String, String> map = new HashMap<String, String>();
	                map.put("PinId", cursor.getString(0));
	                map.put("PinPath", cursor.getString(1));
	                wordList.add(map);*/
	                //MainActivity.ID=Integer.parseInt(cursor.getString(0));
	                //MainActivity.file=cursor.getString(1).toString();
	                val=cursor.getString(0).toString();
	                
	            } while (cursor.moveToNext());
	        }
	        database.close();
	        return val;
	        }
	    public boolean checkuser(String fname,String fpass)
	    {
	        SQLiteDatabase database = this.getWritableDatabase();
		       /* ContentValues values = new ContentValues();
		        values.put("PinID",sid);
		        values.put("PinPath",pname);
		        values.put("PinName",fname);
		        values.put("PinType",tname);
		        values.put("PinExt",exname);
		        values.put("PinLoc",loc);
		        database.insert("PinItems", null, values);*/
		        //LOGIC - solving Assassin's inserting issue!
		        String query="SELECT * from FileHideuser where FUname='"+fname+"' and FUpassword='"+fpass+"'";
		        Cursor cursor = database.rawQuery(query, null);
			    boolean val=cursor.moveToFirst();  
		        database.close();
		        return val;
		    
	    }
	    public int updateStudent(HashMap<String, String> queryValues) {
	        SQLiteDatabase database = this.getWritableDatabase();   
	        ContentValues values = new ContentValues();
	        values.put("StudentName", queryValues.get("StudentName"));
	        return database.update("Students", values, "StudentId" + " = ?", new String[] { queryValues.get("StudentId") });
	        //String updateQuery = "Update  words set txtWord='"+word+"' where txtWord='"+ oldWord +"'";
	        //Log.d(LOGCAT,updateQuery);
	        //database.rawQuery(updateQuery, null);
	        //return database.update("words", values, "txtWord  = ?", new String[] { word });
	    }
	     
	    public void deletePin(String name,String type) {
	        Log.d(LOGCAT,"delete");
	        SQLiteDatabase database = this.getWritableDatabase();   
	        String deleteQuery = "DELETE FROM FileHideitems where Filename=? and FileType=?";
	        SQLiteStatement stmt=database.compileStatement(deleteQuery);
	    	stmt.bindString(1, name);
	    	stmt.bindString(2, type);
	  	   	stmt.execute(); 	 
	        Log.d("query",deleteQuery);    
	    }
	    public void deleteuser() {
	        Log.d(LOGCAT,"delete");
	        SQLiteDatabase database = this.getWritableDatabase();   
	        String deleteQuery = "DELETE FROM FileHideUser where FUname='creed' and FUpassword='1918'";
	        SQLiteStatement stmt=database.compileStatement(deleteQuery);
	       	stmt.execute(); 	 
	        Log.d("query",deleteQuery);    
	    }
	     public int updatePin(String name,int loc,String type)
	     {
	    	 SQLiteDatabase database = this.getWritableDatabase();
	    	  String updateQuery = "UPDATE PinItems set PinLoc="+loc+" where PinPath=? and PinType=?";
	    	 SQLiteStatement stmt=database.compileStatement(updateQuery);
	    	 //String name2=DatabaseUtils.sqlEscapeString(name);
	    	 stmt.bindString(1, name);
	    	 stmt.bindString(2, type);
	    	 int rows=stmt.executeUpdateDelete();
		     Log.d("query",updateQuery);
		        return rows;
	     }
	    public void getAllimages() {
	        //ArrayList<HashMap<String, String>> wordList;
	        PagesFragment.images_to_show.clear();
	        //wordList = new ArrayList<HashMap<String, String>>();
	        String selectQuery = "SELECT  * FROM FileHideitems where Filetype='Pictures'";
	        SQLiteDatabase database = this.getWritableDatabase();
	        Cursor cursor = database.rawQuery(selectQuery, null);
	        if (cursor.moveToFirst()) {
	            do {
	                /*HashMap<String, String> map = new HashMap<String, String>();
	                map.put("PinId", cursor.getString(0));
	                map.put("PinPath", cursor.getString(1));
	                wordList.add(map);*/
	                //MainActivity.ID=Integer.parseInt(cursor.getString(0));
	                //MainActivity.file=cursor.getString(1).toString();
	                PagesFragment.images_to_show.add(cursor.getString(2));
	                Pagesfragment_list.images_to_show.add(cursor.getString(2));
	            } while (cursor.moveToNext());
	        }
	    }
	        public void getAllvideos() {
		        //ArrayList<HashMap<String, String>> wordList;
		        Videosfragment.images_to_show.clear();
		        //wordList = new ArrayList<HashMap<String, String>>();
		        String selectQuery = "SELECT  * FROM FileHideitems where Filetype='Videos'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {
		                /*HashMap<String, String> map = new HashMap<String, String>();
		                map.put("PinId", cursor.getString(0));
		                map.put("PinPath", cursor.getString(1));
		                wordList.add(map);*/
		                //MainActivity.ID=Integer.parseInt(cursor.getString(0));
		                //MainActivity.file=cursor.getString(1).toString();
		                Videosfragment.images_to_show.add(cursor.getString(2));
		                Videofragment_list.images_to_show.add(cursor.getString(2));
		            } while (cursor.moveToNext());
		        }
	        // return contact list
	        //return wordList;
	    }
	        public void getAllvideos_file() {
		        //ArrayList<HashMap<String, String>> wordList;
		        Videosfragment.fnames.clear();
		        //wordList = new ArrayList<HashMap<String, String>>();
		        String selectQuery = "SELECT  * FROM FileHideitems where Filetype='Videos'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {
		                /*HashMap<String, String> map = new HashMap<String, String>();
		                map.put("PinId", cursor.getString(0));
		                map.put("PinPath", cursor.getString(1));
		                wordList.add(map);*/
		                //MainActivity.ID=Integer.parseInt(cursor.getString(0));
		                //MainActivity.file=cursor.getString(1).toString();
		                Videosfragment.fnames.add(cursor.getString(0));
		              //  Videofragment_list.images_to_show.add(cursor.getString(0));
		            } while (cursor.moveToNext());
		        }
	        // return contact list
	        //return wordList;
	    }
	        public void getAlldocuments() {
		        //ArrayList<HashMap<String, String>> wordList;
		        Documentfragment_list.images_to_show.clear();
		        //wordList = new ArrayList<HashMap<String, String>>();
		        String selectQuery = "SELECT  * FROM FileHideitems where Filetype='Documents'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {
		                /*HashMap<String, String> map = new HashMap<String, String>();
		                map.put("PinId", cursor.getString(0));
		                map.put("PinPath", cursor.getString(1));
		                wordList.add(map);*/
		                //MainActivity.ID=Integer.parseInt(cursor.getString(0));
		                //MainActivity.file=cursor.getString(1).toString();
		                Documentfragment_list.images_to_show.add(cursor.getString(2));
		            } while (cursor.moveToNext());
		        }
	        // return contact list
	        //return wordList;
	    }
	        public void getAllmusic() {
		        //ArrayList<HashMap<String, String>> wordList;
		        Musicfragment_list.images_to_show.clear();
		        //wordList = new ArrayList<HashMap<String, String>>();
		        String selectQuery = "SELECT  * FROM FileHideitems where Filetype='Music'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {
		                /*HashMap<String, String> map = new HashMap<String, String>();
		                map.put("PinId", cursor.getString(0));
		                map.put("PinPath", cursor.getString(1));
		                wordList.add(map);*/
		                //MainActivity.ID=Integer.parseInt(cursor.getString(0));
		                //MainActivity.file=cursor.getString(1).toString();
		                Musicfragment_list.images_to_show.add(cursor.getString(2));
		            } while (cursor.moveToNext());
		        }
	        // return contact list
	        //return wordList;
	    }
	        public void getAllothers() {
		        //ArrayList<HashMap<String, String>> wordList;
		        otherfragment.images_to_show.clear();
		        //wordList = new ArrayList<HashMap<String, String>>();
		        String selectQuery = "SELECT  * FROM FileHideitems where Filetype='Others'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {
		                /*HashMap<String, String> map = new HashMap<String, String>();
		                map.put("PinId", cursor.getString(0));
		                map.put("PinPath", cursor.getString(1));
		                wordList.add(map);*/
		                //MainActivity.ID=Integer.parseInt(cursor.getString(0));
		                //MainActivity.file=cursor.getString(1).toString();
		                otherfragment.images_to_show.add(cursor.getString(2));
		            }while (cursor.moveToNext());
		        }
	        // return contact list
	        //return wordList;
	    }
	  	public String getcount(String type)
	    {
	    	String ID=null;
	    	 String selectQuery = "SELECT COUNT(*) FROM FileHideitems where Filetype='"+type+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {

		            	ID=cursor.getString(0).toString();
		            
		                } while (cursor.moveToNext());
		        }
		            
		        // return ID
		        return ID;
	    }
	  	public String getcountuser()
	    {
	    	String ID=null;
	    	 String selectQuery = "SELECT COUNT(*) FROM FileHideUser";
		        SQLiteDatabase database = this.getWritableDatabase();
		        
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {

		            	ID=cursor.getString(0).toString();
		            
		                } while (cursor.moveToNext());
		        }
		            
		        // return ID
		        return ID;
	    }
	    public String GetFilePath(String fname)
	    {
	    	String pname="null";
	        
	    	 String selectQuery = "SELECT * FROM FileHideitems where Filename='"+fname+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        
		       
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        
		        if (cursor.moveToFirst()) {
		            do {

		            	pname=cursor.getString(2).toString();
		            	
		                } while (cursor.moveToNext());
		        }
		        // return ID*/
		        return pname;
	    }
	    public String GetFilesrcPath(String fname)
	    {
	    	String pname="null";
	        
	    	 String selectQuery = "SELECT * FROM FileHideitems where Filename='"+fname+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        
		       
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        
		        if (cursor.moveToFirst()) {
		            do {

		            	pname=cursor.getString(1).toString();
		            	
		                } while (cursor.moveToNext());
		        }
		        // return ID*/
		        return pname;
	    }
	    public String GetFileType(String fname)
	    {
	    	String tname="null";
	    	 String selectQuery = "SELECT * FROM FileHideitems where Filename='"+fname+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {

		            	tname=cursor.getString(4).toString();
		            
		                } while (cursor.moveToNext());
		        }
		            
		        // return ID
		        return tname;
	    }
	    public String GetFileExt(String fname)
	    {
	    	String ename="null";
	    	 String selectQuery = "SELECT * FROM FileHideitems where Filename='"+fname+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {

		            	ename=cursor.getString(3).toString();
		            
		                } while (cursor.moveToNext());
		        }
		            
		        // return ID
		        return ename;
	    }

	    public int Getloc(String name)
	    {
	    	int loc=-23;
	    	try
	    	{
	    	 String selectQuery = "SELECT MAX(PinLoc) FROM PinItems where PinType='"+name+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {

		            	loc=Integer.parseInt(cursor.getString(0).toString());
		            
		                } while (cursor.moveToNext());
		        }
	    	}
	    	catch(Exception e)
	    	{
	    		loc=-23;
	    	}
		        // return ID
		        return loc;
	    	
	    }
	    public String GetFilePathLOC(int loc,String name)
	    {
	    	String pname="null";
	        
	    	 String selectQuery = "SELECT * FROM PinItems where PinLoc="+loc+" and PinType='"+name+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        
		       
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        
		        if (cursor.moveToFirst()) {
		            do {

		            	pname=cursor.getString(1).toString();
		            	
		                } while (cursor.moveToNext());
		        }
		        // return ID*/
		        return pname;
	    }
	    public String GetFileNameLOC(int loc,String name)
	    {
	    	String pname="null";
	        
	    	 String selectQuery = "SELECT * FROM PinItems where PinLoc="+loc+" and PinType='"+name+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        
		       
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        
		        if (cursor.moveToFirst()) {
		            do {

		            	pname=cursor.getString(2).toString();
		            	
		                } while (cursor.moveToNext());
		        }
		        // return ID*/
		        return pname;
	    }
	    public String GetFiletypeLOC(int loc,String name)
	    {
	    	String pname="null";
	        
	    	 String selectQuery = "SELECT * FROM PinItems where PinLoc="+loc+" and PinType='"+name+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        
		       
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        
		        if (cursor.moveToFirst()) {
		            do {

		            	pname=cursor.getString(3).toString();
		            	
		                } while (cursor.moveToNext());
		        }
		        // return ID*/
		        return pname;
	    }
	    public int Getcount(String name)
	    {
	    	int loc=0;
	    	 String selectQuery = "SELECT COUNT(*) FROM PinItems where PinType='"+name+"'";
		        SQLiteDatabase database = this.getWritableDatabase();
		        Cursor cursor = database.rawQuery(selectQuery, null);
		        if (cursor.moveToFirst()) {
		            do {

		            	loc=Integer.parseInt(cursor.getString(0).toString());
		            
		                } while (cursor.moveToNext());
		        }
		            
		        // return ID
		        return loc;
	    	
	    }
	    }
	    