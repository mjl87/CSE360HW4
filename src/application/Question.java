

package application;

/**
 * The Question class represents a question entity in the system.
 * It contains the user's details such as the question and the author of the question.
 */
public class Question {
    private String question;
    private String author;
 
    // Constructor to initialize a new Question object with the question and the author of the question.
    /**
     * Constructor to initialize a new Question object with the question and the author of the question
     * @param question The question a user wants to ask
     * @param author The author of the question
     */
    public Question(String question, String author) {
        this.question = question;
        this.author = author;
    }
    /**
     * returns the question
     * @return question Returns the question
     */
    public String getQuestion() { return question; }
    /**
     * returns the question's author
     * @return author Returns the user who made the question.
     */
    public String getAuthor() { return author; }

}
