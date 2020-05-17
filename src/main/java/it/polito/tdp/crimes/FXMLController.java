/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.CrimesCouple;
import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.MonthYear;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;
	private ObservableList<String> categoriesList = FXCollections.observableArrayList();
	private ObservableList<MonthYear> monthsList = FXCollections.observableArrayList();
	private ObservableList<CrimesCouple> crimesList = FXCollections.observableArrayList();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<MonthYear> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<CrimesCouple> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {

    	this.txtResult.clear();
    	
    	CrimesCouple cc = this.boxArco.getValue();
    	if(cc==null) {
    		this.txtResult.setText("Error! Please select an edge!!\n");
    		return;
    	}
    	
    	this.txtResult.setText("The longest path between "+cc.getCrime1()+" and "+cc.getCrime2()+" is:\n"+this.model.getLongestPath(cc.getCrime1(), cc.getCrime2()));
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	this.crimesList.clear();
    	this.boxArco.setItems(crimesList);
    	
    	String category = this.boxCategoria.getValue();
    	if(category==null) {
    		this.txtResult.setText("Error! Please select a category!!\n");
    		return;
    	}
    	
    	MonthYear my = this.boxMese.getValue();
    	if(my==null) {
    		this.txtResult.setText("Error! Please select a specific month!!\n");
    		return;
    	}
    	int month = my.getMonth();
    	int year = my.getYear();
    	
    	this.model.setGraph(category, month, year);
    	this.txtResult.setText("Graph created with "+model.verticesNumber()+" vertices and "+model.edgesNumber()+" edges\n"+model.printOverWeightedEdgesList());
   
    	
    	this.crimesList.addAll(this.model.getOverWeightedEdgesList());
    	this.boxArco.setItems(crimesList);
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.categoriesList.addAll(model.getCategories());
    	this.boxCategoria.setItems(categoriesList);
    	
    	this.monthsList.addAll(model.getMonths());
    	this.boxMese.setItems(monthsList);
    }
}
