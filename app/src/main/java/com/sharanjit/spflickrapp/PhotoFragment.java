package com.sharanjit.spflickrapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PhotoFragment extends Fragment {
    private String photoURL;
    private ImageView mImageView;
    private String mImageString;
    private Bitmap mBitmap;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        progress = (ProgressBar)view.findViewById(R.id.progressBar2);

        Bundle bundle = getArguments();
        photoURL = bundle.getString("URL", "");
        Log.i(Constants.TAG, "url in fragment: " + photoURL);

        if ( MyCache.imgcache.get(photoURL) == null ) {
Log.d("sharanjit", "Getting Image from Flickr: " + photoURL);
            AsyncTask<String, Void, Long> task = new GetPhoto();
            task.execute();
        } else {
Log.d("sharanjit", "Getting Image from Cache: " + photoURL);
            mBitmap = Bitmap.createBitmap(MyCache.imgcache.get(photoURL));
Log.d("sharanjit", "Cache Bitmap Size" + mBitmap.getByteCount());
            mImageView.setImageBitmap(mBitmap);
        }
        return view;
    }

    private class GetPhoto extends AsyncTask<String, Void, Long> {
        InputStream is = null;
        HttpURLConnection connection = null;

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(String... strings) {

            try{
/*
                URL imageUrl = new URL(photoURL);
                connection = (HttpURLConnection)imageUrl.openConnection();
                connection.connect();
                is = connection.getInputStream();
                mBitmap = BitmapFactory.decodeStream(is);
                return(0L);
*/
/*
Using Cache
 */
                URL imageUrl = new URL(photoURL);

//Log.d("sharanjit", "Getting Image from Flickr: " + photoURL);
                connection = (HttpURLConnection) imageUrl.openConnection();
                connection.connect();
                is = connection.getInputStream();
                mBitmap = BitmapFactory.decodeStream(is);
Log.d("sharanjit", "Flickr Bitmap Size" + mBitmap.getByteCount());

                MyCache.imgcache.put(photoURL, mBitmap);        //Add image in Cache.

                return(0L);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return(1L);
            } catch (IOException e) {
                e.printStackTrace();
                return(1L);
            }finally{
                connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(Long aLong) {
Log.d("sharanjit", "Post Execute Called");
            mImageView.setImageBitmap(mBitmap);
            progress.setVisibility(View.GONE);
            super.onPostExecute(aLong);
        }
    }
}
