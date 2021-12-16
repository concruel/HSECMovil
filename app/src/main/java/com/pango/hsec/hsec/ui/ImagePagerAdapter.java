package com.pango.hsec.hsec.ui;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.model.AlbumEntry;
import com.pango.hsec.hsec.model.ImageEntry;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yazeed44 on 6/20/15.
 */
public class ImagePagerAdapter extends PagerAdapter {


    protected final AlbumEntry mAlbumEntry;
    protected final Fragment mFragment;
    protected final PhotoViewAttacher.OnViewTapListener mTapListener;

    public ImagePagerAdapter(final Fragment fragment, final AlbumEntry albumEntry, final PhotoViewAttacher.OnViewTapListener tapListener) {
        mAlbumEntry = albumEntry;
        mFragment = fragment;
        mTapListener = tapListener;
    }

    @Override
    public int getCount() {
        return mAlbumEntry.imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageEntry imageEntry = mAlbumEntry.imageList.get(position);
        final PhotoView view = new PhotoView(mFragment.getActivity());
        view.setOnViewTapListener(mTapListener);


        Glide.with(mFragment)
                .load(imageEntry.path)
                .asBitmap()
                .into(view);

        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
