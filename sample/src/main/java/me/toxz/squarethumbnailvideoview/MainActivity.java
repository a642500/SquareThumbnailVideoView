package me.toxz.squarethumbnailvideoview;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import me.toxz.squarethumbnailvideoview.library.BaseVideoAdapter;
import me.toxz.squarethumbnailvideoview.library.SquareThumbnailVideoView;

import java.io.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SquareThumbnailVideoView squareThumbnailVideoView = (SquareThumbnailVideoView) findViewById(R.id.squareThumbnailVideoView);

        File dir = getExternalFilesDir("video");
        if (!dir.exists()) {
            Log.i("mkdir: ", String.valueOf(dir.mkdirs()));
        }
        if (dir.listFiles() == null)
            copyAssets(dir);
        final File files[] = dir.listFiles();

        squareThumbnailVideoView.setVideoAdapter(new BaseVideoAdapter() {
            @Override
            public int getCount() {
                return files.length;
            }

            @Override
            public Object getItem(int position) {
                return files[position];
            }


            @Override
            public String getVideoPath(int position) {
                return files[position].getPath();
            }

            @Override
            public boolean isEmpty() {
                return files.length > 0;
            }

            @Override
            public boolean setThumbnailImage(@NonNull ImageView thumbnailImageView, @Nullable Bitmap bitmap) {
                Log.d("adapter", "set bitmap by adapter");
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                thumbnailImageView.setImageBitmap(bitmap);
                thumbnailImageView.setVisibility(View.VISIBLE);

                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void copyAssets(File dir) {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(dir, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
