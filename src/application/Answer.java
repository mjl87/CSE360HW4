
package application;

/**
 * The Answer class represents a answer entity in the system.
 * It contains the user's details such as the question and the author of the question.
 */
public class Answer {
    private int questionID;
    private String answer;
    private String author;

    // Constructor to initialize a new Answer object with the answer and the author of the answer.
    /**
     * Constructor to initialize a new Answer object with the answer and the author of the answer.
     * @param questionID The question id in database
     * @param answer The answer to the question
     * @param author The author of the answer
     */
    public Answer(int questionID, String answer, String author) {
        this.questionID = questionID;
        this.answer = answer;
        this.author = author;
    }
    /**
     * The question id
     * @return The question id
     */
    public int getQuestionID() { return questionID; }
    /**
     * Answer to the question
     * @return The answer to the question
     */
    public String getAnswer() {return answer;}
    /**
     * user of an answer
     * @return The user of an answer
     */
    public String getAuthor() { return author; }

}