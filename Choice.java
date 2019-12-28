///////////////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION ////////////////////////////////
//
// Title: Quiz Generator
// Files: Main.java, AddQuestionForm.java, NodeWrapperADT.java, Question.java, QuestionDB.java
//        Choice.java, QuestionDBADT.json, application.css
// Course: CS400 Spring 2019
// Author: Neel Burman, Tamar Dexheimer, Tejas Rangole, Vedaant Tambi
// Email: nburman@wisc.edu, tdexheimer@wisc.edu, rangole@wisc.edu,tambi@wisc.edu
// Lecturer's Name: Deb Deppeler
//
/////////////////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION /////////////////////////////
//                                        Not Applicable
//////////////////////////////////////// CREDIT OUTSIDE HELP ///////////////////////////////////////
//                                       TA Office Hours
////////////////////////////////////////////////////////////////////////////////////////////////////
package application;

/**
 * This class represents an instance of choice to a question in the quiz generator program
 * 
 * @authors Neel Burman, Tamar Dexheimer, Tejas Rangole, Vedaant Tambi
 */
public class Choice {
  
  boolean isCorrect; // data field that determines whether this choice is the answer to the question 
  String choice; // the choice is stored as a string
  
  /**
   * This constructor instantiates an object of class Choice and initiates the data fields
   * 
   * @param isCorrect whether the choice is the answer 
   * @param choice in the form of a string
   */
  public Choice(boolean isCorrect, String choice) {
    this.isCorrect = isCorrect;
    this.choice = choice;
  }
  
  /**
   * No argument constructor 
   */
  public Choice() { 
    
  }
  
  /**
   * Getter for the choice data filed
   * @return the choice in the form of a string
   */
  public String getChoice() {
    return this.choice;
  }
  
  
  /**
   * Getter for the is Correct data field
   * 
   * @returns whether the choice is the answer to the question or not
   */
  public boolean getIsCorrect() {
    return this.isCorrect;
  }

}
