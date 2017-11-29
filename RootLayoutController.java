package org.tm.util.pdf.view;

import java.io.File;
import java.io.IOException;

import org.tm.util.pdf.MainApp;
import org.tm.util.pdf.utils.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;



public class RootLayoutController {

	@FXML
	private TextField pageFromField;
	@FXML
	private TextField pageToField;

	@FXML
	private Label hintLabel;
	
	public static final String EXTENSION = ".pdf";
	private MainApp mainApp;
	private boolean isFileOpen = false;
	private File file;
	private int pageFrom;
	private int pageTo;
	
	
	@FXML
   private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Resource File");
        
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PDF files (*.pdf)", "*" + EXTENSION);
        fileChooser.getExtensionFilters().add(extFilter);

        
        // Show open file dialog
        file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            isFileOpen = true;
            hintLabel.setText("File Selected");
        }
    }
	
	@FXML
	private void handleSplit() {
		if(isInputValid()) {		
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Splitted File");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
					"PDF files (*.pdf)", EXTENSION);
			fileChooser.getExtensionFilters().add(extFilter);	
			File splitFile = fileChooser.showSaveDialog(mainApp.getPrimaryStage());			
			if (splitFile != null) {
	            // Make sure it has the correct extension
	            if (!splitFile.getPath().endsWith(EXTENSION)) {
	            	splitFile = new File(splitFile.getPath() + EXTENSION);
	            }
	        }
			
			try {
				PDFProcessor.split(file.getPath(), pageFrom, pageTo, splitFile.getPath());
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText("Split Failed");
		        alert.setContentText("File not found.");
		        alert.showAndWait();
			}
            hintLabel.setText("Splitting Succeed");
            
		} else {
            hintLabel.setText("Enter valid page number");
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	
	private boolean isInputValid() {
        String errorMessage = "";
        int pageFrom = -1;
        int pageTo = -1;
        if (!isFileOpen) {
        	errorMessage += "No File Selected";
        }
        if (pageFromField.getText() == null || pageFromField.getText().length() == 0) {
            errorMessage += "No valid begin page!\n"; 
        } 
        if (pageToField.getText() == null || pageToField.getText().length() == 0) {
            errorMessage += "No valid end page!\n"; 
        }
        
    	try {
    		pageFrom = Integer.parseInt(pageFromField.getText());
    		pageTo = Integer.parseInt(pageToField.getText());
    	} catch (NumberFormatException e) {
    		Alert alert = new Alert(AlertType.ERROR);
            //alert.initOwner(dialogStage);
            alert.setTitle("Invalid Page Number");
            alert.setHeaderText("Please correct page number");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
    	}
       
        if (errorMessage.length() == 0 && isPageNumberValid(pageFrom, pageTo)) {
        	this.pageFrom = pageFrom;
        	this.pageTo = pageTo;
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            //alert.initOwner(dialogStage);
            alert.setTitle("Invalid Page Number");
            alert.setHeaderText("Please input valid page number");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
	
	private boolean isPageNumberValid(int pageFrom, int pageTo) {
		return PDFProcessor.pageValidation(file.getPath(), pageFrom, pageTo);
	}
	
}
