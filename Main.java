////////////////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION ////////////////////////////////
//
// Title: Quiz Generator
// Files: Main.java, AddQuestionForm.java, NodeWrapperADT.java, Question.java, QuestionDB.java
// Choice.java, QuestionDBADT.json, application.css
// Course: CS400 Spring 2019
// Author: Neel Burman, Tamar Dexheimer, Tejas Rangole, Vedaant Tambi
// Email: nburman@wisc.edu, tdexheimer@wisc.edu, rangole@wisc.edu,tambi@wisc.edu
// Lecturer's Name: Deb Deppeler
//
/////////////////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION /////////////////////////////
// Not Applicable
//////////////////////////////////////// CREDIT OUTSIDE HELP ///////////////////////////////////////
// TA Office Hours
////////////////////////////////////////////////////////////////////////////////////////////////////
package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.simple.parser.ParseException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * This class represents the primary Graphic User Interface for the Quiz Generator and is
 * responsible for defining all methods that provide functionality to the GUI
 * 
 * @authors Neel Burman, Tamar Dexheimer, Tejas Rangole, Vedaant Tambi
 */
public class Main extends Application {
  Stage primaryStage = new Stage();
  BorderPane root = new BorderPane();
  Scene primary = new Scene(root, 1000, 650);
  QuestionDB db = new QuestionDB();// Database to store questions
  Label numQuestionsInDB = new Label("Available questions: " + getNumQuestions());
  ObservableList<String> selectedTopics; // List of topics chosen by the user
  ListView<String> topics = new ListView<String>(); // List of all topics that is shown to user
  HBox bottom; // HBox to storing various nodes related to the quiz

  List<String> allTopics = new ArrayList<String>(); // List of all topics
  List<Question> questions = new ArrayList<>(); // List for storing questions that will be asked in
                                                // one generation of the
  Question currQuestion; // the question being displayed by the quiz
  int currQuestionNum = 0; // Question number in the quiz
  int totalNumQuestions = 0; // total number of questions to be asked for a generation of the quiz
  int numincorrect = 0; // number of incoorect questions answered

  @Override
  /**
   * This method is responible for setting up the primary GUI for the quiz, i.e. the starting GUI
   * 
   * @param primaryStage the stage to which the various scenes for the program are added
   */
  public void start(Stage primaryStage) {
    try {

      // Label for displaying the number of questions in the database
      numQuestionsInDB.setText("Number of available questions: " + getNumQuestions());
      // TextField where number of questions in quiz will be entered by user
      TextField numQuestionsInQuiz = new TextField();
      numQuestionsInQuiz.setDisable(true); // Disabled until user selects topics

      // TextFiled and its description added to Hbox
      HBox box =
          new HBox(new Label("Enter number of questions for the quiz: "), numQuestionsInQuiz);
      box.setAlignment(Pos.BOTTOM_CENTER);

      /*
       * This block of code will ensure that user is only able to enter numbers in the
       * numQuestionsInQuiz Text field
       */
      numQuestionsInQuiz.textProperty().addListener(new ChangeListener<String>() { // changed
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
            String newValue) {
          if (!newValue.matches("\\d*")) {
            numQuestionsInQuiz.setText(newValue.replaceAll("[^\\d]", ""));
          }
        }
      });

      // load button for loading questions from a JSON file
      Button load = new Button("Load Data");
      load.setMaxWidth(Double.MAX_VALUE);
      load.setOnAction(e -> {
        displayLoadFromFile();
      });

      // Button to add a question using AddQuestionForm
      Button addQuestion = new Button("Add a Question");
      addQuestion.setMaxWidth(Double.MAX_VALUE);
      addQuestion.setOnAction(e -> {
        displayAddQuestionForm(primary);
      });

      // Button to save all questions in the question database to a .json file
      Button save = new Button("Save to File");
      save.setMaxWidth(Double.MAX_VALUE);
      save.setOnAction(e -> {
        displaySaveToFile();
      });

      // Button to generate a quiz
      Button generateQuiz = new Button("Generate Quiz");
      // Button is disabled and will only be enabled when user has filled nummber of Questions field
      generateQuiz.setDisable(true);
      generateQuiz.setOnAction(e -> {
        displayQuiz();
        generateQuiz.setDisable(true); // once a quiz has been generated, button is disabled again
      });

      // Button for selecting topics present in the Listview
      Button selectTopics = new Button("Select Topics");
      selectTopics.setOnAction(e -> {
        topics.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedTopics = topics.getSelectionModel().getSelectedItems();
        // the numQuestionsInQuiz textfiled is enabled once topics have been selected
        numQuestionsInQuiz.setDisable(false);
      });

      numQuestionsInQuiz.setOnAction(e -> { // changed (just paste this whole event handler)
        // once the number of questions for quiz has been entered, generate quiz button is enabled
        generateQuiz.setDisable(false);
        // String of textfield parsed to an Integer
        int userQuestionNumChoice = Integer.parseInt(numQuestionsInQuiz.getText());
        /*
         * if user enters number which is more than the number of questions present for that topic
         * in that database, then all the questions for that topic are generated for that quiz
         */
        if (userQuestionNumChoice >= getNumberOfQuestionsForSelectedTopics())
          totalNumQuestions = getNumberOfQuestionsForSelectedTopics();
        else
          totalNumQuestions = userQuestionNumChoice;
        getQuestionsForQuiz(); // function that gets questions from the database
      });

      // Code for adding instructions to generate quiz at the center of the stage as a vBox
      Label instructionHeader = new Label("Instructions to Generate quiz: ");
      Label step1 = new Label("1. Click on your topic and click the 'Select Topics Button'");
      Label step2 = new Label(
          "2. Enter the number of questions for the quiz in the text field and press ENTER");
      Label step3 = new Label("3. Click the 'Generate Quiz' Button");
      VBox quizInstructions = new VBox(instructionHeader, step1, step2, step3);
      quizInstructions.setPadding(new Insets(10, 0, 0, 10));
      quizInstructions.setSpacing(5);
      quizInstructions.setAlignment(Pos.CENTER);

      // Code for placing load, addQuestion, and save button in a Vbox
      VBox right = new VBox(load, addQuestion, save);
      right.setPadding(new Insets(10, 10, 0, 0));
      right.setSpacing(4);
      right.setAlignment(Pos.TOP_RIGHT);

      // layout the hbox at the bottom of the stage
      bottom = new HBox(selectTopics, numQuestionsInDB, box, generateQuiz);
      bottom.setAlignment(Pos.BOTTOM_CENTER);
      bottom.setSpacing(50);

      // placing nodes at appropriate places on the stage
      root.setRight(right);
      root.setLeft(topics);
      root.setBottom(bottom);
      root.setCenter(quizInstructions);

      primary.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
      primaryStage.setTitle("Quiz Generator"); // title for the quiz
      primaryStage.setScene(primary); // set the scene
      primaryStage.show(); // the stage is shown
      this.primaryStage = primaryStage;

    } // if exception is thrown in setting up the stage then message is displayed to console
    catch (Exception e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  public void displayAddQuestionForm(Scene primary) {
    AddQuestionFormNode formPage = new AddQuestionFormNode();
    VBox form = (VBox) formPage.getNode();
    Button submitButton = new Button("Submit");
    // submitButton.setId("question");
    form.getChildren().add(submitButton);
    Scene newScene = new Scene(form, 600, 200);
    final Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    // dialog.initOwner(primaryStage);
    dialog.setHeight(500.0);
    dialog.setScene(newScene);
    dialog.show();
    submitButton.setOnAction(e -> {
      Question q = createQuestion(formPage);
      db.addQ(q.getTopic(), q);
      if (!allTopics.contains(q.getTopic())) {
        allTopics.add(q.getTopic());
        java.util.Collections.sort(allTopics);
      }
      topics.getItems().clear();
      for (int i = 0; i < allTopics.size(); i++) {
        topics.getItems().add(allTopics.get(i));
      }

      bottom.getChildren().remove(1);
      numQuestionsInDB.setText("Available questions: " + getNumQuestions());
      bottom.getChildren().add(1, numQuestionsInDB);
      dialog.close(); // changed

    });
  }

  public Question createQuestion(AddQuestionFormNode form) {
    Question q = new Question();
    q.setQuestion(form.getQuestion().getText());
    q.setTopic(form.getTopic().getText());
    q.setImage(form.getImage().getText());
    List<Choice> questionChoices = form.getChoices();
    q.setChoices(questionChoices);
    for (Choice c : questionChoices) {
      if (c.isCorrect) {
        q.setAnswer(c.choice);
      }
    }
    return q;
  }

  /**
   * Method to display the quiz and call the recursive method displayQuestion
   * 
   */
  public void displayQuiz() { // changed
    currQuestion = questions.get(++currQuestionNum - 1);
    displayQuestion();
  }

  /**
   * Recursive method to display question to the scene
   */
  public void displayQuestion() {

    // VBox which conatins the question to be displayed
    VBox questionGraphic = new VBox();
    questionGraphic.setPadding(new Insets(10));
    questionGraphic.setSpacing(8);

    /*
     * Label that shows the number of the question and the total number of questions for that
     * generation of the quiz
     */
    Label questionNumberByTotalQuestions =
        new Label("Question " + currQuestionNum + " of " + totalNumQuestions);
    questionGraphic.getChildren().add(questionNumberByTotalQuestions);

    // Label that displays the question
    Label questionText = new Label(currQuestion.getQuestion());
    questionGraphic.getChildren().add(questionText);

    // if image is present then image is displayed
    if (!currQuestion.getImage().isEmpty() && !currQuestion.getImage().equals("none")) {
      Image image = new Image(currQuestion.getImage());
      ImageView iv1 = new ImageView(); // image view for displaying the image
      iv1.setImage(image);
      iv1.setFitWidth(500);
      iv1.setPreserveRatio(true);
      iv1.setSmooth(true);
      iv1.setCache(true);
      questionGraphic.getChildren().add(iv1); // imageview is added to the vbox
    }
    ToggleGroup ChoiceTG = new ToggleGroup(); // Toggle for questiion choices
    RadioButton radioButtonArray[] = new RadioButton[5]; // radio buttons for the choices
    // to initialize the radio buttons and add them to the vbox
    for (int i = 0; i < 5; i++) {
      radioButtonArray[i] = new RadioButton(currQuestion.getChoices().get(i).getChoice());
      radioButtonArray[i].setToggleGroup(ChoiceTG);
      questionGraphic.getChildren().add(radioButtonArray[i]);
    }

    // submit button is added to the vbox
    Button submitAnswerButton = new Button("Submit Answer");
    submitAnswerButton.setDisable(true); // submit button disabled until option has been clicked
    questionGraphic.getChildren().add(submitAnswerButton);

    // if any of the radio buttons are selected then the submit button is enabled
    for (int i = 0; i < 5; i++) {
      radioButtonArray[i].setOnAction(e -> submitAnswerButton.setDisable(false));
    }

    // label that tells whether the answer to question was correct or incorrect
    Label labelResponse = new Label();
    // Button that takes the user to the next question
    Button nextButton = new Button("Click Here to Proceed to Next Question");
    // Button that displays the result
    Button finishQuizButton = new Button("Finish Quiz and get results");
    submitAnswerButton.setOnAction(e -> {
      RadioButton selectedRadioButton = (RadioButton) ChoiceTG.getSelectedToggle();
      /*
       * if-else statement to check whether users answer choice was correct or incorrect by matching
       * it to the answer
       */
      if (currQuestion.getAnswer().equals(selectedRadioButton.getText())) {
        
        labelResponse.setText("The choice you had selected was correct");
        // once the answer has been submitted then the submit button is disabled
        submitAnswerButton.setDisable(true);
      } else {
        labelResponse
            .setText("The answer was incorrect. The correct answer is " + currQuestion.getAnswer());
        submitAnswerButton.setDisable(true);
        numincorrect++; // if answer is incorrect, the counter for the number of incorrect answers
                        // goes up
      }
      questionGraphic.getChildren().remove(submitAnswerButton); // submit button is removed
      if (currQuestionNum != totalNumQuestions)
        // next button shows up when answer has been submitted
        questionGraphic.getChildren().add(nextButton);
      else // on last question, finish quiz button is displayed
        questionGraphic.getChildren().add(finishQuizButton);
    });

    questionGraphic.getChildren().add(labelResponse);

    /*
     * recursive case: next button changes the current question and the display question method is
     * called again
     */
    nextButton.setOnAction(e -> {
      if (currQuestionNum < totalNumQuestions) {
        currQuestion = questions.get(++currQuestionNum - 1);
        displayQuestion();
      }
    });

    // base case: when finish quiz button is pressed
    finishQuizButton.setOnAction(e -> displayResults());
    root.setCenter(questionGraphic); // Question is displayed at the center of the stage
    primary.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    primaryStage.setScene(primary);
    primaryStage.show();
  }

  /**
   * Helper method that clears the details of the previous results before another generation of the
   * quiz
   */
  private void clearQuizResultDetails() {
    currQuestionNum = 0;
    totalNumQuestions = 0;
    numincorrect = 0;
  }

  /**
   * Method that imports quiz questions from the Question database depending on the number of
   * questions and topic entered by the user.
   */
  public void getQuestionsForQuiz() { // changed added
    Random rand = new Random();
    int topicIndex;
    int questionIndex;
    for (int j = 0; j < totalNumQuestions; j++) {
      // random number generators that randomly select topics and questions from the database
      topicIndex = rand.nextInt(selectedTopics.size());
      questionIndex = rand.nextInt(db.getQuestionList(selectedTopics.get(topicIndex)).size());
      questions.add(db.getQuestionList(selectedTopics.get(topicIndex)).get(questionIndex));

    }
  }

  /**
   * Method to return the total number questions present in the question database
   * 
   * @return the number of questions present in the database
   */
  private int getNumQuestions() {
    return db.getNumQuestions();
  }

  /**
   * Dialog box to save the current database to a JSON file
   */
  protected void displaySaveToFile() {
    BorderPane form = new BorderPane();
    Scene sv = new Scene(form, 600, 200);
    TextField sf = new TextField();
    HBox hb = new HBox(new Label("Save File As: "), sf);
    form.setCenter(hb);
    hb.setAlignment(Pos.CENTER);
    hb.setSpacing(20);
    final Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(primaryStage);
    dialog.setScene(sv);
    dialog.show();
    sf.setOnAction(e -> {
      File file = new File(sf.getText());
      try {
        db.saveToFile(file);
        dialog.close();
      } catch (IOException e1) {
        e1.getLocalizedMessage();
        e1.printStackTrace();
      }
    });
  }

  /**
   * Dialog box to add questions to the database from an existing JSON file
   */
  protected void displayLoadFromFile() {
    BorderPane form = new BorderPane();
    Scene ld = new Scene(form, 600, 200);
    TextField lf = new TextField();
    HBox hb = new HBox(new Label("Enter File Name: "), lf);
    form.setCenter(hb);
    hb.setAlignment(Pos.CENTER);
    hb.setSpacing(20);
    final Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(primaryStage);
    dialog.setScene(ld);
    dialog.show();
    lf.setOnAction(e -> {
      File file = new File(lf.getText());
      try {
        db.loadFromFile(file);
        topics.getItems().clear();// Tamar
        topics.getItems().addAll(db.getTopics());// Tamar
        numQuestionsInDB.setText("Number of avaiable questions: " + getNumQuestions());// Tamar
        dialog.close();
      } catch (IOException | ParseException e1) {
        e1.getLocalizedMessage();
        e1.printStackTrace();
      }
    });
  }


  /**
   * Method to display the results of a quiz
   */
  public void displayResults() { // changed

    // Button for exiting the quiz without saving questions to the database
    Button exitWithoutSaving = new Button("Exit Quiz Without Saving");
    exitWithoutSaving.setMaxWidth(Double.MAX_VALUE);

    // Button that saves the questions to the database before exiting
    Button exitAndSave = new Button("Exit Quiz and Save");
    exitAndSave.setMaxWidth(Double.MAX_VALUE);

    // Label to display the number of correct answer
    Label correctAns =
        new Label("Number of Correct Answers: " + (totalNumQuestions - numincorrect));
    // Label to display the number of questions answered by the user
    Label totalAns = new Label("Total Number of Questions Answered: " + totalNumQuestions);
    // Label to display score as a percentage
    Label score = new Label("Score "
        + (double) (totalNumQuestions - numincorrect) * 100 / (double) totalNumQuestions + "%");

    // VBox to display the results
    VBox results = new VBox(correctAns, totalAns, score);
    // VBox to display the exiting options for the quiz
    VBox options = new VBox(exitWithoutSaving, exitAndSave);

    options.setPadding(new Insets(15, 12, 15, 12));
    options.setSpacing(10);


    results.setPadding(new Insets(15, 12, 15, 12));
    results.setSpacing(10);

    exitAndSave.setOnAction(e -> {
      displaySaveToFile(); // option for saving questions to the database
      clearQuizResultDetails();
      start(primaryStage); // calls the start method to exit from the quiz
    });


    exitWithoutSaving.setOnAction(e -> {
      clearQuizResultDetails();
      start(primaryStage);
    });

    BorderPane root = new BorderPane();

    root.setRight(options);
    root.setCenter(results);

    Scene scene = new Scene(root, 1000, 650);
    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setTitle("Quiz Generator");
    primaryStage.show();
  }

  /**
   * Method that gets the number of questions that are present in the database for a specific topic
   * 
   * @return the number of questions present for that topic in the database
   */
  private int getNumberOfQuestionsForSelectedTopics() { // changed (added)
    int numberOfQuestionsForSelectedTopics = 0;

    for (int i = 0; i < selectedTopics.size(); i++) {
      numberOfQuestionsForSelectedTopics += db.getQuestionList(selectedTopics.get(i)).size();
    }
    return numberOfQuestionsForSelectedTopics;
  }

  /**
   * This method is responsible for launching the GUI for the quiz by call
   * 
   * @param args is a string argument that is provided at the command line
   */
  public static void main(String[] args) {
    launch(args); // launches the GUI
  }
}
