/**
 * Question Database Interface
 */
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import javafx.collections.ObservableList;

/**
 * @authors Neel Burman, Tamar Dexheimer, Tejas Rangole, Vedaant Tambi
 *
 */
interface QuestionDBADT {
	
	/**
	 * add question from "Add Question" button display
	 */
	void addQ(String word, Question q);

	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	void loadFromFile(File file) throws FileNotFoundException, IOException, ParseException;
	
	/**
	 * the number of questions in the database
	 */
	int getNumQuestions();
	
	/**
	 * retrieves a list of questions for use in the generated quiz
	 */
	List<Question> getQuestionList(String word);
	
	
	/**
	 * 
	 */
	ObservableList<String> getTopics();

}

