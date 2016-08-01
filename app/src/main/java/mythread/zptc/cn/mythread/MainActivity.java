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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mLoadImageButton;
    private Button mShowToastButton;
    private Button mHandlerButton;
    private static final int mSleepTime = 500;
    private static final String TAG = "MainActivity";
    private ProgressBar mProgressbar;
    private Handler hanlder=new Handler(){
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
        mShowToastButton = (Button) findViewById(R.id.activity_main_show_toast_button);
        mHandlerButton= (Button) findViewById( R.id.activity_main_handler_button);
        mProgressbar= (ProgressBar) findViewById(R.id.progressBar);

        mLoadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadImage();
              
                new LoadImageClass().execute("aaa","bbb");
            }
        });

        mShowToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "hello image", Toast.LENGTH_SHORT).show();
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
                        Message msg=new Message();
                        msg.what=0;
                        hanlder.sendMessage(msg);

                        for(int i=1;i<11;i++){
                            Message msg2=new Message();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            msg2.what=1;
                            msg2.obj=i*10;
                            hanlder.sendMessage(msg2);
                        }
                    }
                }).start();
            }
        });
    }

    private void loadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                for (int i = 1; i < 11; i++) {
                    sleep();
                }
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();

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
            Log.d(TAG, "doInBackground: "+params[0]+","+params[1]);
            for(int i=1;i<11;i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
