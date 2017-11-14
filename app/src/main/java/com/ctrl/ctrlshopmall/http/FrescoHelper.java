package com.ctrl.ctrlshopmall.http;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by ctrlc on 2017/11/13.
 */

public class FrescoHelper {
    public static FrescoHelper helper;


    private FrescoHelper(){

    }
    public static FrescoHelper getInstance(){
        if(helper==null){
            return new FrescoHelper();
        }else{
            return helper;
        }
    }

    /**
     * 渐进式加载图片
     */
    public void setImageUrlByGradual(SimpleDraweeView view,String imageUrl){
        Uri uri = Uri.parse(imageUrl);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.getController())
                .build();
        view.setController(controller);
    }

}
