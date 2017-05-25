package edu.iup.flashcard;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Following the Data Access Object design pattern, this class provides access to a question database.
 * 4/30/14 Class needs adapted to account for rolling DB updates from a server.
 *
 * @since 2014
 * @author Ryan C. Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class ContentDAO extends SQLiteOpenHelper {
    // The Android's default system path to your application database
    private static String DB_PATH = "/data/data/edu.iup.flashcard/databases/";
    private static String DB_NAME = "Question_Database";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public static final String LESSON_QUERY = "SELECT LessonNumber AS _id, LessonTitle, LessonDescription FROM LESSON " +
            "ORDER BY LessonNumber";
    public static final String QUESTIONS_QUERY2 = "SELECT QuestionNumber, Image, Audio, " +
            "AnswerTwo, AnswerThree, AnswerFour, CorrectAnswer FROM QUESTION WHERE LessonNumber = ";
    public static final String DECKS_QUERY = "SELECT DeckTitle FROM DECK ORDER BY DeckID";
    public static final String CARDS_QUERY = "SELECT CardNumber, Image, Audio, AnswerPrimary, " +
            "AnswerSecondary FROM CARD WHERE DECKID = ";

    public static final String QUESTIONS_QUERY3 = "SELECT QuestionNumber, Image, Audio, CorrectAnswer FROM QUESTION WHERE LessonNumber = ";
    public static final String QUESTIONS_QUERY4 = "SELECT QuestionNumber, Image, Audio, " +
            "AnswerTwo, AnswerThree, AnswerFour, CorrectAnswer, _rowid_ FROM QUESTION WHERE LessonNumber = ";



    public ContentDAO(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public boolean updateDB(byte[] dbBytes){

        return false;
    }

    /**
     * Creates a copy of the database used by the app in the host system's memory
     * at the location /data/data/edu.iup.JapaneseFlashCard/databases/Question_Database
     * @throws IOException
     */
    public void createDatabase() throws IOException {

		/*
		 * Set dbExist = checkDataBase(); to just false to fix the
		 * "QUESTION TABLE does not exist error.
		 */
        boolean dbExist = false;


        if (dbExist) {
            // do nothing - database already exists
        } else {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Attempts to open and close the database, just to see if it exists
     * @return whether or not the database was found
     */
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // Database doesn't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Creates a copy of the database used by the app in the host system's memory
     * at the location /data/data/edu.iup.JapaneseFlashCard/databases/Question_Database
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public Cursor manageQuestionsQuery(int num) throws SQLException {
        Cursor c = myDataBase.rawQuery(QUESTIONS_QUERY4 + num, null);
        return c;
    }

    /**
     * Opens the database so that it can be used
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Returns a list of questions and all associated data for the given lesson number
     * @param num The lesson number currently selected
     * @return list of questions and all associated data
     */
    public Cursor questionsQuery(int num){
        Cursor c = myDataBase.rawQuery(QUESTIONS_QUERY2 + "" + num , null);
        return c;

    }

    /**
     * Returns the results of a database query
     * @param query
     * @return results of query
     */
    public Cursor query(String query) {
        Cursor c = myDataBase.rawQuery(query, null);

        //log to dump cursor just to see if select statement is right
        Log.d("TAGS", DatabaseUtils.dumpCursorToString(c));

        return c;
    }

    public Cursor practiceQuery(int num) {
        Cursor c = myDataBase.rawQuery(QUESTIONS_QUERY3 + " " + num, null);
        return c;
    }

    public void updateAnswers(String correct, String ans2, String ans3, String ans4, int rowid){
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        ContentValues args = new ContentValues();
        args.put("AnswerTwo", ans2);
        args.put("AnswerThree", ans3);
        args.put("AnswerFour", ans4);
        args.put("CorrectAnswer", correct);
        myDataBase.update("QUESTION", args, "_ROWID_= " + rowid, null);

    }



    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        return;
        // should never be called. DB already exists on disk

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return;
        // handled in the future

    }

}

