package edu.iup.flashcard.quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Representation of a Question.
 * @since 2014
 * @author Ryan C. Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    private String qText;
    private String correctAnswer;
    private String answerText2;
    private String answerText3;
    private String answerText4;
    private int cAnswer;
    private int qNumber;
    private byte[] image;
    private byte[] audio;
    private int rowid;
    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    private ArrayList<String> answers;

    /**
     * Constructor of Question.java class.
     * Sets private variables of Question.java equal to parameters of constructor.
     * Adds correct answer and the three "false" answers to an ArrayList called "answers". The answers ArrayList is shuffled.
     * Last, int Canswer is set. This integer holds the index of the correct answer in the shuffled answer ArrayList.
     *
     * @param qText // String that holds questions
     * @param correctAnswer // String that stores the correct answer to the question
     * @param answerText2 // String that hold a "false" answer for the question
     * @param answerText3 // String that hold a "false" answer for the question
     * @param answerText4 // String that hold a "false" answer for the question
     * @param qNumber // integer that holds the question number of the question
     * @param image // byte[] that stores image associated with question
     * @param audio // byte[] that stores audio associated with question
     *
     */
    public Question(String qText, String correctAnswer, String answerText2,
                    String answerText3, String answerText4, int qNumber,
                    byte[] image, byte[] audio) {
        super();
        this.qText = qText;
        this.correctAnswer = correctAnswer;
        this.answerText2 = answerText2;
        this.answerText3 = answerText3;
        this.answerText4 = answerText4;
        this.qNumber = qNumber;
        this.image = image;
        this.audio = audio;
        answers = new ArrayList<String>();
        answers.add(correctAnswer);
        answers.add(answerText2);
        answers.add(answerText3);
        answers.add(answerText4);
        Collections.shuffle(answers);
        for(int i = 0; i < answers.size(); i++){
            if(answers.get(i).equals(correctAnswer)){
                cAnswer = i;
            }
        }
    }

    //For manage
    public Question(String qText, String correctAnswer, String answerText2,
                    String answerText3, String answerText4, int qNumber,
                    byte[] image, byte[] audio, int isManage, int rowid) {
        super();
        this.qText = qText;
        this.correctAnswer = correctAnswer;
        this.answerText2 = answerText2;
        this.answerText3 = answerText3;
        this.answerText4 = answerText4;
        this.qNumber = qNumber;
        this.image = image;
        this.audio = audio;
        answers = new ArrayList<String>();
        answers.add(correctAnswer);
        answers.add(answerText2);
        answers.add(answerText3);
        answers.add(answerText4);
        for(int i = 0; i < answers.size(); i++){
            if(answers.get(i).equals(correctAnswer)){
                cAnswer = i;
            }
        }
        this.rowid = rowid;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getqText() {
        return qText;
    }

    public void setqText(String qText) {
        this.qText = qText;
    }

    public int getcAnswer() {
        return cAnswer;
    }

    public void setcAnswer(int cAnswer) {
        this.cAnswer = cAnswer;
    }

    public int getqNumber() {
        return qNumber;
    }

    public void setqNumber(int qNumber) {
        this.qNumber = qNumber;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getRowid(){
        return rowid;
    }
}

