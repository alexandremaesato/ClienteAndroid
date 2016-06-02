package clientefeedback.aplicacaocliente.Services;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Alexandre on 13/05/2016.
 */
public class ImageLoaderCustom {

    private static ImageLoader imageLoader;

    public static ImageLoader getImageloader(Context mContext){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPoolSize(5) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(3 * 1024 * 1024))
                .memoryCacheSize(3 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .writeDebugLogs()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        return imageLoader;
    }
}
