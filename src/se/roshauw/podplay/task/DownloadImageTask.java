package se.roshauw.podplay.task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import se.roshauw.podplay.util.PodPlayUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Task to download image. When completed sets the image to the
 * {@link ImageView} defined in the constructor
 * 
 * @author mats
 * 
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private HttpURLConnection connection;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap internetImg = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            InputStream is = connection.getInputStream();
            internetImg = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            PodPlayUtil.logException(e);
        } finally {
            connection.disconnect();
        }
        return internetImg;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

    @Override
    protected void onCancelled() {
        // Disconnect if a connection is active
        if (null != connection) {
            connection.disconnect();
        }
        super.onCancelled();
    }

}
