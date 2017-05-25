package edu.iup.flashcard.quiz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.iup.flashcard.R;

/**
 * This Fragment represents a question and its user interface. It is controlled by the hosting QuestionActivity.
 * This Fragment does not have access to any of the Questions or UserAnswers directly. They are passed one at a time
 * by the hosting activity.
 *
 * @since 2014
 * @author Ryan C. Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class QuestionFragment extends Fragment {
    private ListView answerList;
    private TextView questionText;
    private ImageView questionImage;
    private ImageView nextButton;
    private ImageView checkAnswerButton;
    private ImageView prevButton;
    private ImageView expImg;
    private Dialog dialog;
    // member variable for selected answer

    // reference to a question for the view to draw
    private Question question;
    private Activity hostingActivity;
    private ImageView audioButton;

    private Bitmap bmp;

    @Override
    //when the QuestionFragment is first loaded by QuestionActivity, set question = the Question object designated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostingActivity = activity;
        question = ((QuestionActivity) activity).getCurrentQuestion();
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


    public void showImage() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.WHITE));

        expImg = new ImageView(getActivity());
        expImg.setImageBitmap(bmp);
        dialog.addContentView(expImg, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, parent, false);

        // set the text as "Lesson #" + the lesson number (obtained from preferences)
        SharedPreferences sp = hostingActivity.getSharedPreferences(ChooseLessonActivity.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        int lessonNum = sp.getInt(ChooseLessonActivity.SELECTED_LESSON_KEY, 0);

        // set image of question here from Question object
        bmp = BitmapFactory.decodeByteArray(question.getImage(), 0,
                question.getImage().length);
        questionImage = (ImageView) v.findViewById(R.id.questionImage);
        questionImage.setClickable(true);

        //I think this is where the picture magically resizes based on how much screen is available
        questionImage.setAdjustViewBounds(true);
        questionImage.setImageBitmap(bmp);


        questionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage();
            }
        });

        //set answers to question
        answerList = (ListView) v.findViewById(R.id.answerList);
        answerList.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, question.getAnswers()));
        answerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        answerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) { //when they choose an answer, turn it grey
                for (int i = 0; i < arg0.getChildCount(); i++) {
                    arg0.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                //answer onclick
                arg1.setBackgroundColor(getResources().getColor(R.color.answerblue));

            }

        });

        //re-display a previously selected answer to a question if one exists
        UserAnswer ua = ((QuestionActivity) hostingActivity).getCurrentQuestionAnswer();
        if(ua != null && ua.getChosenAnswer() != null){
           Toast.makeText(getActivity(), "Last Selected Answer " + ua.getChosenAnswer(), Toast.LENGTH_SHORT).show();

        }

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
                                int i = answerList.getCheckedItemPosition();
                                String choice = (String) answerList.getItemAtPosition(i);
                                if (choice != null) {
                                    UserAnswer ua = new UserAnswer(question.getqNumber(), i, choice);
                                    ((QuestionActivity) hostingActivity).putAnswer(ua);
                                }
                                ((QuestionActivity) hostingActivity).previousQuestion();
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
                                int i = answerList.getCheckedItemPosition();
                                String choice = (String) answerList.getItemAtPosition(i);
                                if (choice != null) {
                                    UserAnswer ua = new UserAnswer(question.getqNumber(), i, choice);
                                    ((QuestionActivity) hostingActivity).putAnswer(ua);
                                }
                                ((QuestionActivity) hostingActivity).nextQuestion();
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
                                int i = answerList.getCheckedItemPosition();
                                if (i == -1) { //if no answer selected, tell them so
                                    Toast.makeText(hostingActivity, "Please select an answer.",
                                    Toast.LENGTH_SHORT).show();
                                    //return;
                                }
                                if (i != question.getcAnswer()) { //if they picked it wrong, set theirs red and the right one green
                                    answerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.redblue));
                                    answerList.getChildAt(question.getcAnswer())
                                        .setBackgroundColor(getResources().getColor(R.color.greenblue));

                                } else { //if they picked right, set their answer green
                                    answerList.getChildAt(question.getcAnswer())
                                        .setBackgroundColor(getResources().getColor(R.color.greenblue));
                                }
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

