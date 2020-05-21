package com.example.serviceworkerapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Worker class for network call and store the response into interface's  method
class ServiceWorker implements Task<Bitmap> {
    String intro;
    private OnImageFetched imageFetched;

    public ServiceWorker(String intro, OnImageFetched imageFetched) {
        this.intro = intro;
        this.imageFetched = imageFetched;
    }

    @Override
    public Bitmap onExecuteTask(String imageUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(imageUrl).build();    // requesting OkHttpClient to fetch network image
        Response response = null;
        try {
            response = client.newCall(request).execute();                 // response after execution of client
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());          // decode response into a bitmap
        return bitmap;
    }

    @Override
    public void onTaskComplete(Bitmap result) {
        imageFetched.onImageFetchedThroughNetwork(result);          // store response into onImageFetchedThroughNetwork method of OnImageFetched interface
    }

}