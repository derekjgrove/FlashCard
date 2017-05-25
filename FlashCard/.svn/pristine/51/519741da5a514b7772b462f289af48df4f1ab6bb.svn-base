package edu.iup.flashcard.quiz;


/**
 * Singleton design patter to hold all of the UserAnswer objects in an array.
 * This allows them to be accessed by multiple classes
 *
 * @since 2014
 * @author Ryan C. Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class UserAnswerSingleton {

    private static UserAnswerSingleton mInstance = null;
    private UserAnswer[] userAnswers;

    /**
     * Constructor for UserAnswerSingleton.java
     *
     */
    private UserAnswerSingleton() {
        userAnswers = new UserAnswer[QuestionSingleton.getInstance().getQuestions().size()];

    }

    public static UserAnswerSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new UserAnswerSingleton();
        }
        return mInstance;
    }

    public UserAnswer[] getUserAnswers() {
        return userAnswers;
    }

}

