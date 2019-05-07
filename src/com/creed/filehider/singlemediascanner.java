package com.creed.filehider;

import java.io.File;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.widget.Toast;

public class singlemediascanner implements MediaScannerConnectionClient {

private MediaScannerConnection mMs;
private File mFile;
Context context;
public singlemediascanner(Context context, File f) {
    this.context=context;
	mFile = f;
    mMs = new MediaScannerConnection(context, this);
    mMs.connect();
    Toast.makeText(context, "CONNECTED!", Toast.LENGTH_SHORT).show();
}

@Override
public void onMediaScannerConnected() {
    mMs.scanFile(mFile.getAbsolutePath(), "*/*");
}

@Override
public void onScanCompleted(String path, Uri uri) {
	  Toast.makeText(context, "DISCONNECTED!", Toast.LENGTH_SHORT).show();
	mMs.disconnect();
    
}

}