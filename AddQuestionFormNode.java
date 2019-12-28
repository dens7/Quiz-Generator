package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AddQuestionFormNode extends Main {

  private List<TextField> choiceTexts;
  private List<ToggleGroup> choiceGroups;
  private VBox form;
  private TextField question = new TextField();
  private TextField metadata = new TextField();
  private TextField topic = new TextField();
  private TextField image = new TextField();
  private List<RadioButton> radioButtons;
  ToggleGroup chooseOne = new ToggleGroup();
  Map<TextField, RadioButton> choiceToButton = new HashMap<TextField, RadioButton>();

  public AddQuestionFormNode() {

  }

  public TextField getMetadata() {
    metadata.setPromptText("meta-data");
    return metadata;
  }

  public TextField getQuestion() {
    question.setPromptText("question");
    return question;
  }

  public TextField getTopic() {
    topic.setPromptText("topic");
    return topic;
  }

  public TextField getImage() {
    image.setPromptText("image");
    return image;
  }

  public List<TextField> getChoiceTexts() {
    choiceTexts = new ArrayList<TextField>();
    choiceTexts.add(new TextField("choice: "));
    choiceTexts.add(new TextField("choice: "));
    choiceTexts.add(new TextField("choice: "));
    choiceTexts.add(new TextField("choice: "));
    choiceTexts.add(new TextField("choice: "));
    return choiceTexts;
  }

  public VBox getNode() {
    TextField metadata = this.getMetadata();
    form = new VBox();
    form.getChildren().add(metadata);
    form.getChildren().add(this.getQuestion());
    List<TextField> choices = this.getChoiceTexts();
    radioButtons = setUpRadioButtons(choiceTexts);
    for (int i = 0; i < choices.size(); i++) {
      choiceToButton.put(choices.get(i), radioButtons.get(i));
      form.getChildren().add(choices.get(i));
      form.getChildren().add(radioButtons.get(i));
    }

    /*
     * for(int i = 0; i < radioButtons.size(); i++) { form.getChildren().add(radioButtons.get(i)); }
     */
    form.getChildren().add(this.getTopic());
    form.getChildren().add(this.getImage());

    return form;
  }

  public List<ToggleGroup> getChoiceGroups() {
    List<TextField> choices = this.getChoiceTexts();
    for (int i = 0; i < choices.size(); i++) {
      ToggleGroup tg = new ToggleGroup();
      tg.setUserData(choices.get(i).getUserData());
      choiceGroups.add(tg);
    }
    return choiceGroups;
  }

  private List<RadioButton> setUpRadioButtons(List<TextField> choiceTexts) {
    List<RadioButton> buttons = new ArrayList<RadioButton>();
    for (int i = 0; i < choiceTexts.size(); i++) {
      RadioButton button = new RadioButton(choiceTexts.get(i).getPromptText());
      button.setToggleGroup(chooseOne);
      buttons.add(button);
    }
    return buttons;
  }

  public List<Choice> getChoices(){
        List<Choice> choices = new ArrayList<>();
        List<TextField> keys = new ArrayList<>(choiceToButton.keySet());
        for(int i = 0; i < keys.size(); i++) {
          Choice a = new Choice();
          TextField entry = keys.get(i);
          a.choice = keys.get(i).getText();
          a.isCorrect = choiceToButton.get(entry).isSelected();
          choices.add(a);
        }
        return choices;
  }

}


