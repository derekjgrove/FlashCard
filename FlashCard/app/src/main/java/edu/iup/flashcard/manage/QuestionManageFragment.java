package edu.iup.flashcard.manage;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import edu.iup.flashcard.ContentDAO;
import edu.iup.flashcard.R;
import edu.iup.flashcard.quiz.ChooseLessonActivity;
import edu.iup.flashcard.quiz.Question;
import edu.iup.flashcard.quiz.QuestionSingleton;

/**
 * This Fragment represents a question and its user interface. It is controlled by the hosting QuestionActivity.
 * This Fragment does not have access to any of the Questions or UserAnswers directly. They are passed one at a time
 * by the hosting activity.
 *
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class QuestionManageFragment extends Fragment {
    private ImageView questionImage;
    private ImageView nextButton;
    private ImageView checkAnswerButton;
    private ImageView prevButton;
    private ImageView editButton;

    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private EditText edit4;

    // member variable for selected answer

    // reference to a question for the view to draw
    private Question question;
    private Activity hostingActivity;
    private ImageView audioButton;

    //required for db edit
    private int lessonNum;
    private int curQuestion = 0;
    LinkedList<Question> questions = QuestionSingleton.getInstance().getQuestions();
    @Override
    //when the QuestionFragment is first loaded by QuestionActivity, set question = the Question object designated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostingActivity = activity;
        question = ((QuestionManageActivity) activity).getCurrentQuestion();
        lessonNum = ((QuestionManageActivity) activity).getLessonNum();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_manage, parent, false);

        // set the text as "Lesson #" + the lesson number (obtained from preferences)
        SharedPreferences sp = hostingActivity.getSharedPreferences(ChooseLessonActivity.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        int lessonNum = sp.getInt(ChooseLessonActivity.SELECTED_LESSON_KEY, 0);

        // set image of question here from Question object
        Bitmap bmp = BitmapFactory.decodeByteArray(question.getImage(), 0,
                question.getImage().length);
        questionImage = (ImageView) v.findViewById(R.id.questionImage);
        questionImage.setClickable(true);

        //I think this is where the picture magically resizes based on how much screen is available
        questionImage.setAdjustViewBounds(true);
        questionImage.setImageBitmap(bmp);

        questionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edit1 = (EditText) v.findViewById(R.id.editText1);
        edit2 = (EditText) v.findViewById(R.id.editText2);
        edit3 = (EditText) v.findViewById(R.id.editText3);
        edit4 = (EditText) v.findViewById(R.id.editText4);

        edit1.setText(question.getAnswers().get(0));
        edit2.setText(question.getAnswers().get(1));
        edit3.setText(question.getAnswers().get(2));
        edit4.setText(question.getAnswers().get(3));

        editButton = (ImageView) v.findViewById(R.id.editButton);
        editButton.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        prevButton.playSoundEffect(SoundEffectConstants.CLICK);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                editButton.setImageResource(R.drawable.ic_edit);

                                String new1 = edit1.getText().toString();
                                String new2 = edit2.getText().toString();
                                String new3 = edit3.getText().toString();
                                String new4 = edit4.getText().toString();

                                ContentDAO dbThing = new ContentDAO(getActivity());
                                int rowid = question.getRowid();

                                dbThing.updateAnswers(new1, new2, new3, new4, rowid);

                                ArrayList<String> answers = new ArrayList<String>();
                                answers.add(new1);
                                answers.add(new2);
                                answers.add(new3);
                                answers.add(new4);

                                question.setAnswers(answers);
                            }
                        }, 250);
                        break;
                    case MotionEvent.ACTION_UP :
                        editButton.setImageResource(R.drawable.ic_edit_active);
                        break;
                }
                return false;
            }
        });


        prevButton = (ImageView) v.findViewById(R.id.prevButton);
        prevButton.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        prevButton.playSoundEffect(SoundEffectConstants.CLICK);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                prevButton.setImageResource(R.drawable.ic_leftarrow);
                                ((QuestionManageActivity) hostingActivity).previousQuestion();
                                curQuestion--;
                            }
                        }, 250);
                        break;
                    case MotionEvent.ACTION_UP :
                        prevButton.setImageResource(R.drawable.ic_leftarrow_filled);
                        break;
                }
                return false;
            }
        });


        nextButton = (ImageView) v.findViewById(R.id.nextButton);
        nextButton.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        nextButton.playSoundEffect(SoundEffectConstants.CLICK);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                nextButton.setImageResource(R.drawable.ic_rightarrow);
                                ((QuestionManageActivity) hostingActivity).nextQuestion();
                                curQuestion++;
                            }
                        }, 250);
                        break;
                    case MotionEvent.ACTION_UP :
                        nextButton.setImageResource(R.drawable.ic_rightarrow_filled);
                        break;
                }
                return false;
            }
        });


        checkAnswerButton = (ImageView) v.findViewById(R.id.checkAnswerButton);
        checkAnswerButton.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        checkAnswerButton.playSoundEffect(SoundEffectConstants.CLICK);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                checkAnswerButton.setImageResource(R.drawable.ic_checkanswer);
                                edit1.setBackgroundColor(getResources().getColor(R.color.greenblue));
                            }
                        }, 250);
                        break;
                    case MotionEvent.ACTION_UP :
                        checkAnswerButton.setImageResource(R.drawable.ic_checkanswer_filled);
                        break;
                }
                return false;
            }
        });


        audioButton = (ImageView) v.findViewById(R.id.soundButton);
        audioButton.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        audioButton.playSoundEffect(SoundEffectConstants.CLICK);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                audioButton.setImageResource(R.drawable.ic_playaudio);
                                try {
                                    File tempFile = File.createTempFile("questionAudio", "WAV");
                                    tempFile.deleteOnExit();
                                    FileOutputStream fos = new FileOutputStream(tempFile);
                                    fos.write(question.getAudio());
                                    fos.close();
                                    MediaPlayer player = new MediaPlayer();
                                    FileInputStream fis = new FileInputStream(tempFile);
                                    player.setDataSource(fis.getFD());
                                    player.prepare();
                                    player.start();
                                 } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }, 250);
                        break;
                    case MotionEvent.ACTION_UP :
                        audioButton.setImageResource(R.drawable.ic_playaudio_filled);
                        break;
                }
                return false;
            }
        });

        return v;
    }

    public int getLessonNum(){
        return lessonNum;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}

