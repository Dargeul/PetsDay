package com.example.gypc.petsday.helper;

/**
 * Created by gypc on 2017/12/20.
 */

import android.content.Context;
import android.graphics.Point;

import com.alley.van.helper.VanMediaFilter;
import com.alley.van.helper.VanMediaType;
import com.alley.van.model.IncapableCause;
import com.alley.van.model.MediaInfo;
import com.alley.van.util.PhotoMetadataUtils;

import java.util.HashSet;
import java.util.Set;


public class GifSizeFilter extends VanMediaFilter {
    private int mMinWidth;
    private int mMinHeight;
    private int mMaxSize;

    public GifSizeFilter(int minWidth, int minHeight, int maxSizeInBytes) {
        mMinWidth = minWidth;
        mMinHeight = minHeight;
        mMaxSize = maxSizeInBytes;
    }

    @Override
    public Set<VanMediaType> constraintTypes() {
        return new HashSet<VanMediaType>() {{
            add(VanMediaType.GIF);
        }};
    }

    @Override
    public IncapableCause filter(Context context, MediaInfo mediaInfo) {
        if (!needFiltering(context, mediaInfo))
            return null;

        Point size = PhotoMetadataUtils.getBitmapBound(context.getContentResolver(), mediaInfo.getContentUri());
        if (size.x < mMinWidth || size.y < mMinHeight || mediaInfo.size > mMaxSize) {
//            提醒机制
            return null;
        }
        return null;
    }

}
