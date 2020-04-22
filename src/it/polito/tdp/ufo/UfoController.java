/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.Annata;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Annata> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;

    @FXML
    void handleAnalizza(ActionEvent event) {
    	   try {
    	   	txtResult.appendText(model.analizza(boxStato.getValue())+"\n");
    	    }catch (Exception e ) {
    	    txtResult.appendText("errore selezionare uno stato");
    	    }
    	    
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	txtResult.clear();
    try {
    	
    	model.creaGrafo(boxAnno.getValue().getAnno());
        boxStato.getItems().addAll(model.getStati());
        txtResult.appendText(model.parametriGrafo());
    	
    }catch (Exception e ) {
    txtResult.appendText("errore selezionare un anno");
    }
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	
   	   try {
	   txtResult.appendText(model.trovaSequenza(boxStato.getValue()).toString()+"\n");
	    }catch (Exception e ) {
	    txtResult.appendText("errore selezionare uno stato");
	    }
	    
}
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";
        
    }

	public void setModel(Model model) {
		this.model= model;
		boxAnno.getItems().addAll(this.model.getAnni());
	}
}
