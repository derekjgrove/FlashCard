package edu.iup.flashcard.quiz;

import edu.iup.flashcard.ContentDAO;
import edu.iup.flashcard.MenuActivity;
import edu.iup.flashcard.R;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 * Main activity of the application. Presents a Listview of results from
 * the list.
 *
 * @since 04/30/2014
 * @author Ryan C Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class ChooseLessonActivity extends ListActivity {
    private static final int HOME = 0;
    private Cursor mCursor;
    private ContentDAO dbHelper;
    public static final String SELECTED_LESSON_KEY = "SELECTED_LESSON";
    public static final String PREVIOUS_LESSON_KEY = "PREVIOUS_LESSON";
    public static final String SHARED_PREFERENCES_KEY = "SP_KEY";


    /**
     * Default initialize method for Android SDK. Displays the opening
     * GUI for the application which is the lesson list. Creates the
     * DB connection in the background.
     *
     * @param savedInstanceState - The cached last saved state of the
     * application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 	//restore all variables to what they were when the app was closed
        setContentView(R.layout.activity_choose_lesson);  //designate layout for output

        Typeface tf= Typeface.createFromAsset(getAssets(), "Capture_it.ttf");

        TextView headerText =(TextView) findViewById(R.id.lessonText);
        headerText.setTypeface(tf);

        // query database asynchronously in background
        dbHelper = new ContentDAO(this);


        try {
            if(!dbHelper.checkDataBase()) {	//if the DB doesn't exist, make one and open it
                dbHelper.createDatabase();
                dbHelper.openDataBase();
            }
            else{
                dbHelper.openDataBase();	//otherwise just open the DB
            }
        } catch (Exception ioe) {
            throw new Error("Unable to create database");
        }

        mCursor = dbHelper.query(ContentDAO.LESSON_QUERY); //stores all lesson tables in mCursor

        ListAdapter curserAdapter = new SimpleCursorAdapter(this,	//context, layout type, where stuff is,
                android.R.layout.simple_list_item_1, mCursor,		//query column to use, display format
                new String[] { "_id" }, new int[] { android.R.id.text1 }, 0);
        setListAdapter(curserAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //manages sending selected lesson to next part of app
        String selection = mCursor.getString(0);	//get selection in column 0 (0-indexed)

        int lessonNum = Integer.parseInt(selection);

        SharedPreferences sp = getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        int prevLesson = sp.getInt(SELECTED_LESSON_KEY, -1);
        editor.putInt(SELECTED_LESSON_KEY, lessonNum); //store selection in preferences
        editor.putInt(PREVIOUS_LESSON_KEY, prevLesson);
        editor.commit();


        Intent intent = new Intent(this, InitializeQuestionsActivity.class);
        intent.putExtra(SELECTED_LESSON_KEY, lessonNum);	//add selected lesson to call
        startActivity(intent);	//calls InitializeQuestionsActivity

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuItem item1 = menu.add(Menu.NONE, HOME, Menu.NONE, "Home");
        item1.setIcon(R.drawable.ic_home);
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case HOME:
                //GOTO Home Screen
                return true;
            default:
                return false;
        }

    }

    /**
     * Event listener if a user was to press the back soft button
     */
    @Override
    public void onBackPressed() {
        // DO NOTHING!!
        Intent i = new Intent(ChooseLessonActivity.this, MenuActivity.class);
        ChooseLessonActivity.this.startActivity(i);
    }


    /**
     * Inner class to get a query of the DB in the background. AsyncTask allows the quick
     * threading of pre-loading the DB so that a thread must not be in place for the entirety
     * of the applications.
     *
     * @Generic Void - params, the type of the parameters sent to the task upon execution.
     * @Generic Void - progress, the type of progress units published during the background comp.
     * @Generic Cursor - result, the type of the result of the background computation.
     */
    private class GetLessonsTask extends AsyncTask<Void, Void, Cursor> {
        //this class is never used or referenced.  not sure what it was supposed to do.
        /**
         * Get the user pick of the lesson and close the DB open helper
         *
         * @param params - the params generic
         */
        @Override
        protected Cursor doInBackground(Void... params) {

            dbHelper.close();
            return mCursor;
        }

        /**
         *
         *
         * @param result - the result generic
         */
        @Override
        protected void onPostExecute(Cursor result) {
            if (result != null) {

            }
        }

    }

}
