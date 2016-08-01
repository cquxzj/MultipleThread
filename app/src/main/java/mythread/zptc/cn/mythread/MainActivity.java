package mythread.zptc.cn.mythread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mLoadImageButton;
    private Button mHandlerButton;
    private static final int mSleepTime = 500;
    private static final String TAG = "MainActivity";
    private ProgressBar mProgressbar;
    private Handler mHanlder =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: ");
            switch (msg.what){
                case 0:
                    mProgressbar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    int progress=(int)msg.obj;
                    mProgressbar.setProgress(progress);
                    break;
                case 2:
                    mImageView.setImageBitmap((Bitmap)msg.obj);
                    mProgressbar.setVisibility(View.INVISIBLE);
                default:break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.activity_main_image_view);
        mLoadImageButton = (Button) findViewById(R.id.activity_main_load_image_button);
        mHandlerButton= (Button) findViewById( R.id.activity_main_handler_button);
        mProgressbar= (ProgressBar) findViewById(R.id.progressBar);

        mLoadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadImageClass().execute();
            }
        });

        mHandlerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: ");
                        Message msg= mHanlder.obtainMessage();
                        msg.what=0;
                        mHanlder.sendMessage(msg);

                        for(int i=1;i<11;i++){
                            Message msg2= mHanlder.obtainMessage();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            msg2.what=1;
                            msg2.obj=i*10;
                            mHanlder.sendMessage(msg2);
                        }

                        Message msg3= mHanlder.obtainMessage();
                        Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                        msg3.what=2;
                        msg3.obj=bmp;
                        mHanlder.sendMessage(msg3);

                    }
                }).start();
            }
        });
    }

    private void sleep() {
        try {
            Thread.sleep(mSleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class LoadImageClass extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            for(int i=1;i<11;i++){
                sleep();
                publishProgress(i*10);
            }
            Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressbar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
            mProgressbar.setVisibility(View.INVISIBLE);
        }
    }
}
