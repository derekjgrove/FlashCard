package edu.iup.flashcard;

import android.app.*;
import android.content.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Background Service to handle DB updates from the server.
 * 4/30/14 not finished
 * Needs wrapped in a timed task so the app does not need to be launched for the db to update
 * @author Ryan C. Gray and Team
 *
 */
public class QuestionDatabaseIntentService extends IntentService {
    private static final String DB_PATH = "/data/data/edu.iup.flashcard/databases/";
    private static final String DB_NAME = "Question_Database";

    public QuestionDatabaseIntentService(){
        super("QuestionDBIntentService");
    }

    @Override
    protected void onHandleIntent(Intent fromApp) {
        Thread workerThread = new Thread(new Runnable(){

            @Override
            public void run() {
                URL serverAddress;
                try {
                    serverAddress = new URL("");
                    HttpURLConnection conn = (HttpURLConnection) serverAddress.openConnection();


                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e){

                }


            }

        });
        workerThread.start();
    }

}
