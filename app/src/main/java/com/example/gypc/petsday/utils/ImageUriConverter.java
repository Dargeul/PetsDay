package com.example.gypc.petsday.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by XUJIJUN on 2017/12/29.
 */

public class ImageUriConverter {
    private static final String BASE_DOWNLOAD_URL = "http://120.78.169.206:4000/download/";

    public static String getImgRemoteUriFromName(String filename) {
        return BASE_DOWNLOAD_URL + filename.concat(".jpeg");
    }

    public static Uri getCacheFileUriFromName(Context context, String filename) {
        Uri resUri = null;
        try {
            File img = new File(context.getCacheDir(), filename.concat(".jpeg"));
            if (!img.exists()) {
                throw new Exception("Image not exists");
            }
            resUri = Uri.fromFile(img);
        } catch (Exception e) {
            Log.e("ImageUriConverter", "getCacheFileUriFromName", e);
        }
        return resUri;
    }

    public static String getFilenameFromUri(Uri uri) {
        String path = uri.getPath();
        String filename = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".jpeg"));
        return filename;
    }
}
