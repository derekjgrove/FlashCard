package edu.iup.flashcard.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.LinkedList;

import edu.iup.flashcard.R;
import edu.iup.flashcard.quiz.Question;
import edu.iup.flashcard.quiz.QuestionSingleton;

/**
 *
 * Hosting activity for a Fragment. This Activity provides 1 UI, a fragment container
 * in which we fill with a QuestionFragment. As the hosting Activity, this class controls
 * instantiation of all Fragments and maintains all of the Questions and UserAnswer data structures.
 * The Fragment is initialized and calls protected methods of this class to get the proper data
 * to display a question.
 *
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class QuestionManageActivity extends FragmentActivity {
    // maybe have references to questions and userAnswers
    private LinkedList<Question> questions;
    public int currentQuestion;
    public static final String CURRENT_QUESTION_KEY = "CURRENT_QUESTION_KEY";
    public static final String QUESTION_LIST_FILE = "QUESTION_lIST_FILE";
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private EditText edit4;
    private int lessonNum;
    /**
     * Default initialize method for Android SDK. Goes to display
     * the question.
     *
     * @param savedInstanceState - The cached last saved state of the
     * application.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        questions = QuestionSingleton.getInstance().getQuestions(); //retrieve list of questions

        if (questions == null) {
            //if the lists are empty, try to fill them
            FileInputStream fis1;
            ObjectInputStream in1;
            FileInputStream fis2;
            ObjectInputStream in2;

            try {
                fis1 = openFileInput(QUESTION_LIST_FILE);
                in1 = new ObjectInputStream(fis1);
                questions = (LinkedList<Question>) in1.readObject(); //fill empty list with contents of QUESTION_LIST_FILE
                in1.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        //if user is returning to the lesson, have them start on the question they were on
        if (savedInstanceState != null) {
            currentQuestion = savedInstanceState.getInt(CURRENT_QUESTION_KEY);
        } else {
            currentQuestion = 0;
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.questionFragmentContainer); //check if a fragment is currently loaded
        //if not, create one and load it
        if (fragment == null) {
            fragment = new QuestionManageFragment();
            fm.beginTransaction().add(R.id.questionFragmentContainer, fragment)
                    .commit();
        }

    }

    /**
     * Point to the next question in the list, and replace the frame with
     * the next question frame. If it is the last question in the list,
     * then it calls the report card.
     */
    protected void nextQuestion() {
        if ((currentQuestion == (questions.size() - 1))) { //if you press next at the last question,
            Intent intent = new Intent(this, ChooseLessonManageActivity.class); //activate report card
            startActivity(intent);
        } else {
            currentQuestion++; //otherwise, increment currentQuestion and load a new QuestionFragment
            Fragment fragment = new QuestionManageFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.questionFragmentContainer, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            transaction.commit();

        }

    }

    /**
     * Goes back to the previous question in the list, loads the container
     * with the previous question frame. If it is the first question then
     * it tells the user that it is the first question and you can not go
     * back.
     */
    protected void previousQuestion() {
        if (currentQuestion == 0) { //if it's the first question and they press back, tell them so
            Toast.makeText(this, "this is the first question", Toast.LENGTH_SHORT).show();
        } else {
            if (currentQuestion != 0) { //otherwise, decrement currentQuestion and load a new QuestionFragment
                currentQuestion--;
                Fragment fragment = new QuestionManageFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.questionFragmentContainer, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                transaction.commit();
            }
        }

    }

    /**
     * Gets the Question object for the currentQuestion
     * @return the question object
     */
    public Question getCurrentQuestion() {
        return questions.get(currentQuestion);
    }

    public int getLessonNum() {
        return lessonNum;
    }

    /**
     * Get the index in the list of where the user is
     * @return the index value
     */
    protected int getCurrentListPosition() {
        return currentQuestion;
        // currentQuestion
    }

    /**
     *
     */
    @Override
    public void onPause() {
        super.onPause();

    }

    /**
     * The parameter that is passed in and called when the application
     * is loaded. Saves what question the user is on for next login.
     *
     * @param outState - the value holding the cached location.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_QUESTION_KEY, currentQuestion);
    }

    /**
     * Method to kill any outstanding processes
     *
     * Right now it is saving, should be moved to a onSaveInstance() call or
     * moved to the onPause() method
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        String FILENAME1 = QUESTION_LIST_FILE;
        FileOutputStream fos1;
        FileOutputStream fos2;
        ObjectOutputStream out1;
        ObjectOutputStream out2;
        try {
            fos1 = openFileOutput(FILENAME1, Context.MODE_PRIVATE);
            out1 = new ObjectOutputStream(fos1);
            out1.writeObject(questions);
            out1.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    /**
     * Event listener if a user was to press the back soft button
     */
    @Override
    public void onBackPressed() {
        // DO NOTHING!!
        Intent i = new Intent(QuestionManageActivity.this, ChooseLessonManageActivity.class);
        QuestionManageActivity.this.startActivity(i);
    }

}

