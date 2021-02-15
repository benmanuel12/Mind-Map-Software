package mindmapsoftware;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

class EditableLabel extends Label {
    TextField tf = new TextField();
    /***
     * backup is used to cancel when press ESC...
     */
    String backup = "";
    public EditableLabel(){
        this("");
    }
    public EditableLabel(String str){
        super(str);
        this.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                tf.setText(backup = this.getText());
                this.setGraphic(tf);
                this.setText("");
                tf.requestFocus();
            }
        });
        tf.focusedProperty().addListener((prop, o, n) -> {
            if(!n){
                toLabel();
            }
        });
        tf.setOnKeyReleased(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                toLabel();
            }else if(e.getCode().equals(KeyCode.ESCAPE)){
                tf.setText(backup);
                toLabel();
            }
        });
    }

    void toLabel(){
        this.setGraphic(null);
        this.setText(tf.getText());
        // Reflect back to array
    }

}

/*
This piece of code creates a subclass of Label that also contains a TextField attribute
Upon a double (left) click, the value of the Label is inserted into the TextField and the TextField is given focus.
If the user then changes the text in the TextField and pressed Enter, the TextField is vanished and the Label's text
is set to the contents of the TextField.
If the user instead presses Escape, the TextField is vanished and the Label's text is set to an empty string.
*/
