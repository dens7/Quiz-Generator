/**
 * 
 */
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @authors Neel Burman, Tamar Dexheimer, Tejas Rangole, Vedaant Tambi
 *
 */
class QuestionDB implements QuestionDBADT {

  private Map<String, List<Question>> topics = new HashMap<>();
  protected List<Question> list;
  private List<String> topicList;
  private int numQuestionsInDB;

  public QuestionDB() {
    numQuestionsInDB = 0;
    topics = new HashMap<String, List<Question>>();
    list = new ArrayList<Question>();
    topicList = new ArrayList<String>();
  }

  /**
   * This method provides the total number of questions in the database
   * 
   * @return int the total number of questions in the database
   */
  @Override
  public int getNumQuestions() {
    return numQuestionsInDB;
  }

  /**
   * This method is to intended to load one or more questions from a JSON file into the database
   * 
   * @param File JSON file to be loaded
   * @throws FileNotFoundException, IOException, ParseException
   */
  @SuppressWarnings("unchecked")
  @Override
  public void loadFromFile(File file) throws FileNotFoundException, IOException, ParseException {
    try {
    String metadata = "";
    String question = "";
    String topic = "";
    String image = "";
    String choice = "";
    String isCorrectString = "";
    String answer = "";
    List<Question> questions = new ArrayList<>();
    Object obj = new JSONParser().parse(new FileReader(file));
    JSONObject questionsFile = (JSONObject) obj; // creating JSON object for file
    JSONArray questionArray = (JSONArray) questionsFile.get("questionArray");
    for (int i = 0; i < questionArray.size(); i++) {
      JSONObject curr = (JSONObject) questionArray.get(i);
      metadata = (String) curr.get("meta-data");
      question = (String) curr.get("questionText");
      topic = (String) curr.get("topic");
      image = (String) curr.get("image");// Tamar
      JSONArray choiceArray = (JSONArray) curr.get("choiceArray");
      ArrayList<Choice> choiceList = new ArrayList<>();
      for (int j = 0; j < choiceArray.size(); j++) {
        boolean isCorrect;
        JSONObject currChoice = (JSONObject) choiceArray.get(j);
        isCorrectString = (String) currChoice.get("isCorrect");
        choice = (String) currChoice.get("choice");
        if (isCorrectString.equals("T")) {
          isCorrect = true;
          answer = choice;
        }  
        else
          isCorrect = false;
        choiceList.add(new Choice(isCorrect, choice));
      }
      Question q = new Question();
      q.setMetadata(metadata);
      q.setQuestion(question);
      q.setTopic(topic);
      q.setImage(image);
      q.setChoices(choiceList);
      q.setAnswer(answer);
      questions.add(q);
      if (topics.containsKey(topic)) {
        topics.get(topic).add(q);
      } else {
        topics.put(topic, questions);
      }
      addQ(topic, q); // should add the question to the database -Tamar
      getNumQuestions();
      getTopics();
    }
    } catch (FileNotFoundException fe) {
  
    } catch (IOException fe) {
      
    }catch (ParseException fe) {
      
    }

  }

  /**
   * Save the contents of the class to a JSON file
   * 
   * @param file the name of the JSON file the question database should be written to
   * @throws IOException
   */
  public void saveToFile(File file) throws IOException {
    FileWriter fw = new FileWriter(file); // Tamar
    JSONObject questionDatabase = new JSONObject();// Tamar
    JSONArray questionArray = new JSONArray();
    JSONObject item = new JSONObject(); // attempt to fix save format -Tamar
    List<Question> questions = new ArrayList<Question>();
    for (int i = 0; i < getTopics().size(); i++) {// Tamar edited
      questions.addAll(getQuestionList(topicList.get(i)));
      for (int j = 0; j < questions.size(); j++) {
        Question curr = questions.get(i);
        // edited to fix file format -Tamar
        item.put("meta-data", curr.getMetadata());
        item.put("questionText", curr.getQuestion());
        item.put("topic", curr.getTopic());// Tamar
        item.put("choiceArray", curr.getChoices());
        item.put("image", curr.getImage());// Tamar
      }
      questionArray.add(item);// Tamar
      item = new JSONObject();// Tamar
    }
    questionDatabase.put("questionArray", questionArray);// Tamar
    fw.write(questionDatabase.toJSONString());// Tamar
    fw.flush();// Tamar
    fw.close();// Tamar
  }


  /**
   * Displays a list of the topics available in the database
   * 
   * @return ObservableList<String>
   */
  public ObservableList<String> getTopics() {
    ObservableList<String> top = FXCollections.observableArrayList(topicList);
    if (top.isEmpty()) { // ensures ListView will have something to display -Tamar
      top.add("no topics");
    }
    return top;
  }

  /**
   * allows the user to add a question to the database
   * 
   * @param String topic, Question question to be added
   */
  @Override
  public void addQ(String topicString, Question q) {
    // case where topic is not present
    if (!topicList.contains(topicString)) {
      ArrayList<Question> newQuestionList = new ArrayList<>();
      newQuestionList.add(q);
      topicList.add(topicString);
      topics.put(topicString, newQuestionList);
      numQuestionsInDB++;
    } else {
      topics.get(topicString).add(q);
      numQuestionsInDB++;
    } // case where topic is present but question is not

  }

  /**
   * provides a list of all the questions with the given topic
   * 
   * @param String topic
   * @return List<Question>
   */
  @Override
  public List<Question> getQuestionList(String topicString) {
    return topics.get(topicString);
  }

}
