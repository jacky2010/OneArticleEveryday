package com.base.framework.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.base.framework.R;
import com.base.framework.contacts.HttpContacts;
import com.base.framework.entity.App;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by Administrator on 2015/10/23.
 */
public class ImageUtils {
    private static File cacheDir;
    private static Context context;
    private static ImageLoader imageLoader;
    private static ImageLoaderConfiguration config;
    public static void init(Context ctx) {
        context = ctx;
        imageLoader = ImageLoader.getInstance();
        cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getExternalCacheDir().getAbsolutePath());
        config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//			    .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(4)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
////			    .writeDebugLogs() // Remove for release app
                .build();//开始构建
//			    Initialize ImageLoader with configuration.
        imageLoader.init(config);
    }

    //头像的图片的属性
    public static DisplayImageOptions headerOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.mipmap.touxiang)//设置下载失败时显示默认的图片
            .showImageForEmptyUri(R.mipmap.touxiang) //设置请求路径为空的时候显示的图片
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(DensityUtils.dp2px(App.getContext(),2))).build(); //头像的图片的属性
    public static DisplayImageOptions groupOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.mipmap.msg_group)//设置下载失败时显示默认的图片
            .showImageForEmptyUri(R.mipmap.msg_group) //设置请求路径为空的时候显示的图片
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(DensityUtils.dp2px(App.getContext(),10))).build();
    //验证码的图片属性

    public static DisplayImageOptions validateOptions = new DisplayImageOptions.Builder()
//            .showImageOnFail(R.mipmap.dynamic_default_bg)//设置下载失败时显示默认的图片
//            .showImageForEmptyUri(R.mipmap.dynamic_default_bg) //设置请求路径为空的时候显示的图片
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .displayer(new SimpleBitmapDisplayer())
            .considerExifParams(false).build();

//圆形头像
    public static  DisplayImageOptions circleHeaderOptions=new DisplayImageOptions.Builder()
        .showImageOnFail(R.mipmap.touxiang)//设置下载失败时显示默认的图片
        .showImageForEmptyUri(R.mipmap.touxiang) //设置请求路径为空的时候显示的图片
        .imageScaleType(ImageScaleType.EXACTLY)
        .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
        .displayer(new RoundedBitmapDisplayer(200))
        .considerExifParams(false).build();

    public static  DisplayImageOptions splashOptions=new DisplayImageOptions.Builder()
            .showImageOnFail(R.mipmap.splash_one)//设置下载失败时显示默认的图片
            .showImageForEmptyUri(R.mipmap.splash_one) //设置请求路径为空的时候显示的图片
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .considerExifParams(false).build();

    public static  DisplayImageOptions LogoOptions=new DisplayImageOptions.Builder()
            .showImageOnFail(R.mipmap.splash_one)//设置下载失败时显示默认的图片
            .showImageForEmptyUri(R.mipmap.splash_one) //设置请求路径为空的时候显示的图片
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .considerExifParams(false).build();
        /**
         * 获得一个圆角的头像
         *
         * @param uri       图片地址
         * @param imageView 要显示图片的控件
         */
    public static void DisplayHeaderImage(String uri, ImageView imageView) {
        if (uri!=null&&imageView!=null) {
            try {
                imageLoader.displayImage(getImageUrl(uri), imageView, headerOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public static void DisplayLocalHeaderImage(String uri, ImageView imageView) {
        if (uri!=null&&imageView!=null) {
            try {
                imageLoader.displayImage(uri, imageView, headerOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
       /**
         * 获得一个圆角的头像
         *
         * @param uri       图片地址
         * @param imageView 要显示图片的控件
         */
    public static void DisplayHeaderImageComPath(String uri, ImageView imageView) {
        if (uri!=null&&imageView!=null) {
            try {
                imageLoader.displayImage(uri, imageView, headerOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void DisplaySplashImage(String uri,ImageView imageView){
        if (uri!=null&&imageView!=null) {
            try {
                imageLoader.displayImage(getImageUrl(uri), imageView, splashOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void DisplayLogoImage(String uri,ImageView imageView){
        if (uri!=null&&imageView!=null) {
            try {
                imageLoader.displayImage(getImageUrl(uri), imageView, splashOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 获得一个圆角的头像
     *
     * @param uri       图片地址
     * @param imageView 要显示图片的控件
     */
    public static void DisplayGroupImage(String uri, ImageView imageView) {
        if (uri!=null&&imageView!=null) {
            try {
                imageLoader.displayImage(getImageUrl(uri), imageView, groupOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 获得一个圆角的头像
     *
     * @param uri       图片地址
     * @param imageView 要显示图片的控件
     */
    public static void DisplayHeaderComImage(String uri, ImageView imageView) {
        if (uri!=null&&imageView!=null) {
            try {
                imageLoader.displayImage(uri, imageView, circleHeaderOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 方形图片
     *
     * @param uri
     * @param imageView
     */
    public static void DisplaySqImage(String uri, ImageView imageView) {
        try {
            imageLoader.displayImage(getImageUrl(uri), imageView, validateOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } /**
     * 方形图片
     *
     * @param uri
     * @param imageView
     */
    public static void DisplaySqComUrlImage(String uri, ImageView imageView) {
        try {
            imageLoader.displayImage(uri, imageView, validateOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示本地文件图片
     *
     * @param uri
     * @param imageView
     */
    public static void DisplaySqFromDiskImage(String uri, ImageView imageView) {
        try {
            imageLoader.displayImage(uri, imageView, validateOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } /**
     * 显示本地文件图片
     *
     * @param uri
     * @param imageView
     */
    public static void DisplayRoundFromDiskImage(String uri, ImageView imageView) {
        try {
            imageLoader.displayImage(uri, imageView, headerOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清理缓存
     */
    public static void clearCache() {
        imageLoader.clearDiskCache();
        imageLoader.clearDiskCache();
    }

    /**
     * 组合图片的请求地址
     *
     * @param url
     * @return
     */
    public static String getImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.length() >= 2) {
                String substring = url.substring(2, url.length());
                substring = HttpContacts.IMG_BASE_URL + substring;
                return substring;
            }
        }
        return null;
    }
    /**
     * 组合图片的请求地址
     *
     * @param url
     * @return
     */
    public static String getTargetUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.length() >= 2) {
                String substring = url.substring(2, url.length());
                substring = HttpContacts.BASE_HTML_URL + substring;
                return substring;
            }
        }
        return null;
    }
    public static String getAccUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            String substring = url.substring(2, url.length());
            substring = HttpContacts.IMG_BASE_URL + substring;
            LOG.e("downloadpath", substring);
            return substring;
        }
        return null;
    }

    /**
     * 显示本地图片
     *
     * @param url
     * @param photoViewAttacher
     * @param imageView
     */
    public static void loadImgFromDisk(String url, final PhotoViewAttacher photoViewAttacher, final ImageView imageView) {
        imageLoader.loadImage(url, validateOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                imageView.setImageBitmap(loadedImage);
                photoViewAttacher.update();

            }
        });
    }

    public static void loadImgFromService(String url, final PhotoViewAttacher photoViewAttacher, final ImageView imageView) {
        loadImgFromDisk(getImageUrl(url), photoViewAttacher, imageView);
    }

    public static void displayImag(String uri,ImageView imageView){
        if (uri!=null&&imageView!=null) {
            imageLoader.displayImage(uri,imageView,validateOptions);
        }
    }
}
