package edu.iup.flashcard.quiz;

import java.io.Serializable;
/**
 * Representation of a user's answer to a question. Stores all information including
 * the position in the ListView which they touched, the actual answer text, and the corresponding
 * question number.
 *
 * @since 2014
 * @author Ryan C. Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class UserAnswer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int questionNumber;
    private int answerNumber;
    private String chosenAnswer;

    public String getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(String chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }

    /**
     * Constructor of UserAnswer.java.
     * @param questionNumber // integer that holds the question number that the user is answering
     * @param answerNumber // integer that holds the number that corresponds with correct answer of the question
     * @param chosenAnswer // String that holds the answer the user chooses.
     */
    public UserAnswer(int questionNumber, int answerNumber, String chosenAnswer) {
        super();
        this.questionNumber = questionNumber;
        this.answerNumber = answerNumber;
        this.chosenAnswer = chosenAnswer;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

}
