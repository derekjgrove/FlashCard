package edu.iup.flashcard.quiz;

import java.util.LinkedList;

/**
 * @since 2014
 * @author Ryan C. Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 */
public class QuestionSingleton {
    private static QuestionSingleton mInstance = null;
    private LinkedList<Question> questions;

    private String mString;

    /**
     * Constructor for QuestionSingleton.java
     */
    private QuestionSingleton() {
        questions = new LinkedList<Question>();

    }

    public static QuestionSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new QuestionSingleton();
        }
        return mInstance;
    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }

}
