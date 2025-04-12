package application;

import java.sql.SQLException;
import application.Review;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class lists all the answers to a question as well as giving the allowing for other users to
 * answer the question or view other answers to that question.
 */
public class QuestionPage {
	private Label answerLabel;
	private Button AskButton;
	private Button deleteButton;
	private final DatabaseHelper databaseHelper;
	
    /**
     * QuestionPage constructor allowing for use of databaseHelper database operations.
     * @param databaseHelper The databaseHelper where interaction with database is allowed
     */
	public QuestionPage(DatabaseHelper databaseHelper2) {
		// TODO Auto-generated constructor stub
		this.databaseHelper = databaseHelper2;
	}

	public boolean contains(String toSearch, String toSearchFor) {
		Pattern pattern = Pattern.compile(toSearchFor, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(toSearch);
	    return matcher.find();
	}

	/**
	 * Displays all the answers to a question in the provided primary stage, and the author can delete their answer, update, or the author of the question can highlight an answer.
	 * @param primaryStage The primary stage where the scene will be displayed.
	 * @param user The user where user name, password, and role is stored for user.
	 * @param id The User's id  to keep track of author of question
	 */
	public void show(Stage primaryStage, User user, int id) throws SQLException {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    //allows scrolling functionality
    	ScrollPane scrolly = new ScrollPane();
	    scrolly.setPrefSize(69, 69);
	    scrolly.setContent(layout);
	    
	    //Creates a back button so the user can navigate back to the list of questions
	    Button BackButton = new Button("<- Back");
	    BackButton.setOnAction(a -> {
	    	new QuestionsPage(databaseHelper).show(primaryStage, user);
	    });
	    //Displays the question that the user clicked on to view
	    Label questionLabel = new Label(databaseHelper.getQuestion(id) + "\n\n\n");
	    layout.getChildren().add(questionLabel);
	    //If the user is the author of the question, they can delete the question
	    if (user.getUserName().equals(databaseHelper.getQuestionAuthor(id))) {
	    	deleteButton = new Button("Delete Question");
	    	deleteButton.setOnAction(a ->{
	    		try {
					databaseHelper.deleteQuestion(id);
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    		new QuestionsPage(databaseHelper).show(primaryStage, user);
	    	});
	    	layout.getChildren().add(deleteButton);
	    }
	    //Displays all of the answers if there are any, if not it will tell the user that there are no answers
	    String answers = databaseHelper.getAnswers(id);
	    
	   
	    //System.out.print(answers);
	    //answerLabel = new Label(databaseHelper.getAnswers(id) + "\n\n\n");
        //Creates a list of the answers
	    while(!answers.equals("No answers yet")) {
	    	answers = answers.trim();
		    if(answers == "" || answers.length() < 10) {
		    	break;
		    }
		    //substring shenanigans separating answers and answer id
		    String grab = answers.substring(0, answers.indexOf("."));
		    int grabid = Integer.parseInt(grab);
		    
		    
		    int nummer = answers.indexOf("Posted on:");
		    int nummier = answers.indexOf("+=+=+=+=+=+=");
		    
		    if(answers == "") {
		    	break;
		    }
		    answerLabel = new Label(answers.substring(0,nummier+1));
		    //System.out.println(answerLabel);

		    String tempanswers = answers.substring(nummier+12, answers.length());
		    //System.out.println(tempanswers);
		    answers = tempanswers;
		    
		    //beginning of update and highlight buttons which update and highlight answers
		    Button UpdateAnswerButton = new Button("Update");
		    Button HighlightButton = new Button("Highlight");
		    
		    HighlightButton.setOnAction(e -> {
				//highlightbutton stuff
		    	
		    	String oneAnswer = databaseHelper.findAnswer(grabid);
		    	databaseHelper.highlightAnswer(oneAnswer, grabid);
		    	new QuestionsPage(databaseHelper).show(primaryStage, user);

			});
		    UpdateAnswerButton.setOnAction(e -> {
				//update answer stuff
		    	
		    	String oneAnswer = databaseHelper.findAnswer(grabid);
		    	try {
					new UpdateAnswerPage(databaseHelper).show(primaryStage,user, grabid, oneAnswer, databaseHelper.getQuestion(id));
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
		    	
		    	

			});
		    layout.getChildren().add(answerLabel);
		    
		    //checks to see if user is the author of button
		    if(user.getUserName().equals(databaseHelper.getQuestionAuthor(id))) {
		    	layout.getChildren().add(HighlightButton);
		    }
		    
		    //checks to see if user is the author of answer
		    System.out.print((databaseHelper.getAnswerAuthor(grabid)));
		    if(user.getUserName().equals(databaseHelper.getAnswerAuthor(grabid))) {
		    	layout.getChildren().add(UpdateAnswerButton);
		    }
		    
		    //if reviewer, create review button
		    if (contains(user.getRole(), "Reviewer")) {
		        Button reviewButton = new Button("Add Review Comment");
		        reviewButton.setOnAction(e -> {
		            new AddReviewPage(databaseHelper).show(primaryStage, user, grabid);
		        });
		        layout.getChildren().add(reviewButton);
		    	}
		    
		    //display under answer
		    List<Review> reviews = databaseHelper.getReview(grabid);

		    if (reviews.isEmpty()) {
		        layout.getChildren().add(new Label("Reviews: No reviews yet."));
		    } else {
		        layout.getChildren().add(new Label("Reviews:"));
		        for (Review r : reviews) {
		            Label reviewLabel = new Label("- " + r.getReviewer() + ":\n" + r.getReview() + "\n(" + r.getTime() + ")");
		            layout.getChildren().add(reviewLabel);

		            if (user.getUserName().equals(r.getReviewer())) {
		                Button editBtn = new Button("Edit");
		                editBtn.setOnAction(e -> {
		                    new UpdateReviewPage(databaseHelper).show(primaryStage, user, r.getId(), r.getReview(), id);
		                });

		                Button deleteBtn = new Button("Delete");
		                deleteBtn.setOnAction(e -> {
		                    new DeleteReviewPage(databaseHelper).show(primaryStage, user, r.getId(), r.getReview(), id);
		                });

		                layout.getChildren().addAll(editBtn, deleteBtn);
		            } else {
		            	Button trustButton = new Button("Trust Reviewer");
		            	trustButton.setOnAction(e -> {
		            		new AssignTrustPage().show(databaseHelper, primaryStage, user, r.getReviewer());
		            	});
		            	layout.getChildren().add(trustButton);
		            }
		            
		        }
		    }
	    }
	    //If the user is an instructor or a reviewer they will have a button that they can use to create an answer for this question
	    if (user.getRole().contains("Instructor") || 
	    	    user.getRole().contains("Reviewer") || user.getRole().contains("Student")) {//CAN DELETE LATER added student can answer questions
            AskButton = new Button("Answer this question");
            AskButton.setOnAction(a -> {
                new AnswerAQuestionPage(databaseHelper).show( primaryStage, user, id);
            });
            layout.getChildren().add(AskButton);
        }
	    
	 // MessageList Intro
        Button messagesButton = new Button("Messages");
        messagesButton.setOnAction(e -> {
            new MessagesListPage(databaseHelper).show(primaryStage, user);
        });
        layout.getChildren().add(messagesButton);
        
//        // User can go back to their home page
//        Button BackButton = new Button("<- Back");
//        BackButton.setOnAction(a -> {
//            new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
//        });
//        layout.getChildren().add(BackButton);


	    layout.getChildren().add(BackButton);
	    
	    Scene userScene = new Scene(scrolly, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Question Page");
    	
	    }
	}

