package com.example.runa.filedownloadtest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by runa on 21.09.17.
 */

public class DownloadFileFromURL extends AsyncTask <String, String, String> {

    Context context;
    String filePath;

    /**
     *
     * @param context Application context: has to be handed by MainActivity
     * @param fileName Name of the file. Files are stored @context.getFilesDir().toString()+"/"+fileName
     */
   public DownloadFileFromURL(Context context, String fileName ) {
        try{
            this.context = context;
            this.filePath = context.getFilesDir().toString()
                    + "/" +fileName;
        }
        catch (Exception e) {
            Log.d("exception", e.getMessage());
        }

    }
    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(filePath);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    public String getFilePath() {
        return filePath;
    }

}
