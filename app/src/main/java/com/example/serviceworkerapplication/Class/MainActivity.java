package com.example.serviceworkerapplication.Class;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.serviceworkerapplication.Interface.OnImageFetched;
import com.example.serviceworkerapplication.R;

// root class from where the execution will start
public class MainActivity extends AppCompatActivity implements OnImageFetched {

    Button button1, button2;
    ImageView image1, image2;
    ProgressBar loadProgress;

    // urls to load image
    String image1Url = "https://developer.android.com/images/tools/studio/studio-feature-devices_2x.png";
    String image2Url = "https://api.androidhive.info/images/sample.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        loadProgress = findViewById(R.id.load_progress);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchImage1AndSet(image1Url, image1);              // calling method to do network call and load image1
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchImage2AndSet(image2Url, image2);              // calling method to do network call and load image2
            }
        });
    }

    public void fetchImage1AndSet(final String imageUrl, final ImageView view) {
        final ServiceWorker serviceMethod = new ServiceWorker("service method", this);
        final Bitmap[] res = new Bitmap[1];

        if (view.getVisibility() == View.GONE) {                            // condition to check whether the image is already loaded or not. If not loaded then it will do the network call else not
            loadProgress.setVisibility(View.VISIBLE);                       // progress bar until the image loads

            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();

                    res[0] = serviceMethod.onExecuteTask(imageUrl);           // running onExecuteTask on separate thread to do network call

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadProgress.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            serviceMethod.onTaskComplete(res[0]);             // update the UI on main thread

                            OnImageFetched<Bitmap> taskFetched = new OnImageFetched<Bitmap>() {
                                @Override
                                public void onImageFetchedThroughNetwork(Bitmap response) {
                                    image1.setImageBitmap(response);                              // set the image1 with bitmap response
                                }
                            };

                            taskFetched.onImageFetchedThroughNetwork(res[0]);                     // wrap network response with an interface
                        }
                    });
                }
            };
            thread.start();
        } else {
            loadProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Image already loaded", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchImage2AndSet(final String imageUrl, final ImageView view) {
        final ServiceWorker serviceWorker = new ServiceWorker("service method2", this);
        final Bitmap[] res = new Bitmap[1];

        if (view.getVisibility() == View.GONE) {                                 // condition to check whether the image is already loaded or not. If not loaded then it will do the network call else not
            loadProgress.setVisibility(View.VISIBLE);                            // progress bar until the image loads

            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();

                    res[0] = serviceWorker.onExecuteTask(imageUrl);               // running onExecuteTask on separate thread to do network call

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadProgress.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            serviceWorker.onTaskComplete(res[0]);                  // update the UI on main thread

                            OnImageFetched<Bitmap> taskFetched = new OnImageFetched<Bitmap>() {
                                @Override
                                public void onImageFetchedThroughNetwork(Bitmap response) {
                                    image2.setImageBitmap(response);                           // set the image1 with bitmap response
                                }
                            };

                            taskFetched.onImageFetchedThroughNetwork(res[0]);                     // wrap network response with an interface
                        }
                    });
                }
            };
            thread.start();
        } else {
            loadProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Image already loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageFetchedThroughNetwork(Object response) {
        Toast.makeText(getApplicationContext(), "Image loaded", Toast.LENGTH_SHORT).show();
    }
}



