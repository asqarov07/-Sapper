/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapper;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ASQAROV
 */
public class ModalWindow {
    public void window(String str){
        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        
        Button btn= new Button("Ok");
        btn.setOnAction(e->window.close());
        btn.setOnKeyPressed(e->window.close());
        Label label0=new Label();
        label0.setText(str);
        
        VBox layout1=new VBox(20);
        layout1.setAlignment(Pos.CENTER);
        
        layout1.getChildren().addAll(label0,btn);
        
        Scene scene1=new Scene(layout1,200,100);
        layout1.setOpacity(0.6);
        layout1.setStyle("-fx-background-color: #ccc143;");
        
        window.setScene(scene1);
        window.setTitle("Error");
        window.initStyle(StageStyle.UNDECORATED);
        window.showAndWait();
        
    }
}
