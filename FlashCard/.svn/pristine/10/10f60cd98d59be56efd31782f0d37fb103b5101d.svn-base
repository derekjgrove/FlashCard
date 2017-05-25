package edu.iup.flashcard.quiz;

import java.util.Collections;
import java.util.LinkedList;
import edu.iup.flashcard.ContentDAO;
import edu.iup.flashcard.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Intermediary Activity which provides only 1 UI. A circular progress dialog
 * while the app queries the DB in the background in a seperate thread
 *
 * @since 04/30/2014
 * @author Ryan C Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class InitializeQuestionsActivity extends Activity {
    private Cursor mCursor;
    private ContentDAO dbHelper;
    private ProgressBar progBar;


    /**
     * Default initialize method for Android SDK. Displays progress bar
     * and goes to load up the questions view for that given lesson.
     *
     * @param savedInstanceState - The cached last saved state of the
     * application.
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);	//retrieve saved state
        setContentView(R.layout.activity_initialization);	//designate content view to be used
        progBar = (ProgressBar) findViewById(R.id.progressBar1);
        progBar.setIndeterminate(true);

        int lessonNum = getIntent().getExtras().getInt(ChooseLessonActivity.SELECTED_LESSON_KEY);  //retrieve lesson num
        //from lesson key
        int prevLesson = getIntent().getExtras().getInt(ChooseLessonActivity.PREVIOUS_LESSON_KEY);
        dbHelper = new ContentDAO(this);

        try {
            dbHelper.openDataBase();	//create readable DB view
        } catch (SQLException sqle) {
            throw sqle;
        }



        new GetQuestionsTask().execute(lessonNum, prevLesson);
        //start GetQuestionsTask, passing in lessonNum
    }

    /**
     * Inner class to get a query of the question table of the DB in the background.
     * AsyncTask allows the quick threading of pre-loading the DB so that a thread must
     * not be in place for the entirety of the applications.
     *
     * @Generic Integer - params, the type of the parameters sent to the task upon execution.
     * @Generic Integer - progress, the type of progress units published during the background comp.
     * @Generic Void - result, the type of the result of the background computation.
     */
    private class GetQuestionsTask extends AsyncTask<Integer, Integer, Void> {

        /**
         *
         *
         * @param params - the params Generic
         *
         */
        @Override
        protected Void doInBackground(Integer... params) {

            mCursor = dbHelper.questionsQuery(params[0]);	//grab all rows where LessonNumber = lessonNum
            Log.d("debug", DatabaseUtils.dumpCursorToString(mCursor));	//must be a debug thing
            dbHelper.close();


            //retrieve the current question list, or create an empty one if one does not exist
            LinkedList<Question> questions = QuestionSingleton.getInstance().getQuestions();

            if (params[0] != params[1]) {

                questions.clear();
                Question question;
                byte[] image;
                byte[] audio;
                int questionNum;
                String correctAnswer;
                String answer2;
                String answer3;
                String answer4;

                mCursor.moveToNext();
                while (!mCursor.isAfterLast()) { //for each row, grabs data, instantiates new Question, stores in list
                    questionNum = mCursor.getInt(0);
                    image = mCursor.getBlob(1);
                    audio = mCursor.getBlob(2);
                    answer2 = mCursor.getString(3);
                    answer3 = mCursor.getString(4);
                    answer4 = mCursor.getString(5);
                    correctAnswer = mCursor.getString(6);
                    //guys, I found the "QuestionText" column from the DB \/ \/ \/
                    question = new Question("Choose the answer which best describes the image", correctAnswer, answer2, answer3, answer4, questionNum, image, audio);
                    questions.add(question);
                    mCursor.moveToNext();

                }
                Collections.shuffle(questions);    //lets questions appear in random order
            }
            return null;
        }

        /**
         *
         *
         * @param progress - the progress Generic
         */
        @Override
        protected void onProgressUpdate(Integer... progress){
            setProgress(progress[0]); //explicitly do nothing
        }

        /**
         *
         *
         * @param result - the result Generic
         */
        @Override
        protected void onPostExecute(Void result) {
            //after it's done loading, go to QuestionActivity
            Intent intent = new Intent(InitializeQuestionsActivity.this,QuestionActivity.class);
            startActivity(intent);
        }

    }

}
