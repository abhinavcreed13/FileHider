package com.creed.filehider;

import android.graphics.drawable.Drawable;

public class Item 
{String title;
Drawable image;

public Item(String string, Drawable drawable) {
	// TODO Auto-generated constructor stub
	super();
	this.title = string;
	this.image = drawable;
}

// Empty Constructor
public void Item()
{

}

// Constructor

// Getter and Setter Method
public String getTitle()
{
    return title;
}

public void setTitle(String title)
{
    this.title = title;
}

public Drawable getImage()
{
    return image;
}

public void setImage(Drawable image)
{
    this.image = image;
}


}