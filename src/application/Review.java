
package application;

/**
 * The Review class represents a review entity in the system.
 * It contains the user's details such as the review and the author of the review.
 */
public class Review {
    private int id;
    private int answerID;
    private String review;
    private String reviewer;
    private String time;
    
    /**
     * Constructor to initialize a new Review object with the review and the author of the review
     * @param id The id of the user reviewer
     * @param answerID The id of the answer the reviewer is reviewing
     * @param review The review the reviewer created
     * @param reviewer The username of the reviewer
     * @param time Displays the time
     */
    public Review(int id, int answerID, String review, String reviewer, String time) {
        this.id = id;
        this.answerID = answerID;
        this.review = review;
        this.reviewer = reviewer;
        this.time = time;
    }
    
    /**
     * returns user's id
     * @return id returns user's id
     */
    public int getId() { return id; }
    /**
     * returns answer's id
     * @return answerId return answer's id
     */
    public int getAnswerID() { return answerID; }
    /**
     * returns reviewer's review
     * @return review returns a reviewer's review
     */
    public String getReview() { return review; }
    /**
     * returns reviewer's username
     * @return reviewer returns reviewer's username
     */
    public String getReviewer() { return reviewer; }
    /**
     * returns time
     * @return time returns time
     */
    public String getTime() { return time; }
}
