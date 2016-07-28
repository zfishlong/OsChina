package com.ilmare.oschina.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ilmare.oschina.Adapter.RecyclingPagerAdapter;
import com.ilmare.oschina.Base.BaseActivity;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Widget.HackyViewPager;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/1/2016 10:28 AM
 * 版本号： 1.0
 * 版权所有(C) 7/1/2016
 * 描述：
 * ===============================
 */
public class ImagePreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    @InjectView(R.id.view_pager)
    HackyViewPager viewPager;
    @InjectView(R.id.tv_img_index)
    TextView tvImgIndex;
    @InjectView(R.id.iv_more)
    ImageView ivMore;


    public static final String BUNDLE_KEY_IMAGES = "bundle_key_images";
    private static final String BUNDLE_KEY_INDEX = "bundle_key_index";
    private String[] mImageUrls;
    private SamplePagerAdapter samplePagerAdapter;
    private int mCurrentPostion;

    public static void showImagePrivew(Context context, int index,
                                       String[] images) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(BUNDLE_KEY_IMAGES, images);
        intent.putExtra(BUNDLE_KEY_INDEX, index);
        context.startActivity(intent);
    }

    @Override
    protected boolean hasActionBar() {
        getSupportActionBar().hide();  //隐藏actionbar
        return false;
    }

    @Override
    protected int getActionBarTitle() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.inject(this);
        mImageUrls = getIntent().getStringArrayExtra(BUNDLE_KEY_IMAGES);
        int index = getIntent().getIntExtra(BUNDLE_KEY_INDEX, 0);
        samplePagerAdapter = new SamplePagerAdapter(mImageUrls);
        viewPager.setAdapter(samplePagerAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(index);
        onPageSelected(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPostion = position;
        if (mImageUrls != null && mImageUrls.length > 1) {
            if (tvImgIndex != null) {
                tvImgIndex.setText((mCurrentPostion + 1) + "/"
                        + mImageUrls.length);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class SamplePagerAdapter extends RecyclingPagerAdapter{

        private String[] images = new String[] {};

        SamplePagerAdapter(String[] images) {
            this.images = images;
        }

        public String getItem(int position) {
            return images[position];
        }

        @Override
        public int getCount() {
            return images.length;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.image_preview_item, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.image.setOnFinishListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    ImagePreviewActivity.this.finish();
                }
            });

            final ProgressBar bar = vh.progress;
            KJBitmap kjbitmap = new KJBitmap();
            kjbitmap.displayWithDefWH(vh.image, images[position],
                    new ColorDrawable(0x000000), new ColorDrawable(0x000000),
                    new BitmapCallBack() {
                        @Override
                        public void onPreLoad() {
                            super.onPreLoad();
                            bar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Exception arg0) {
                            AppContext.showToast(R.string.tip_load_image_faile);
                        }
                    });
            return convertView;
        }


    }

    static class ViewHolder {
        PhotoView image;
        ProgressBar progress;

        ViewHolder(View view) {
            image = (PhotoView) view.findViewById(R.id.photoview);
            progress = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

}
