/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sapper;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Random;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author ASQAROV
 */
//-2 opened
//0 not open
//-1 bombs
//if number >0 then it is bombs number around
public class Sapper extends Application{
        
    Scene sceneMain;
    Scene sceneStart;
    ObservableList<String> nxm;
    ObservableList<String> levels;
    
    int n5=5, n10=10, n15=15, n20=20;
    int l1=1,l2=2,l3=3,l4=4,l5=5,l6=6,l7=7;
    
    int bombs_count,percent;
    int mine_number=20;
    
    Label lbLevel;
    Label lbBombs;
    Button rePlay;
    
    int width=600,height=600;
    int t_count=0;
    int number=0;
    
    boolean first_step, canPlay;
    
    Button[][] btn;
    int n=0,m=0;
    Random rand=new Random();
    
    int[][] bombs;
    int[][] flags;
    
    public void random_bombs(int y,int x){
        bombs=new int[n][m];
        flags=new int[n][m];
        for (int k=0; k<mine_number; k++){
            int rand_number_i;
            int rand_number_j;
            do{
                rand_number_i=rand.nextInt(n);
                rand_number_j=rand.nextInt(m);
            }while(bombs[rand_number_i][rand_number_j]==-1 || (rand_number_i==y && rand_number_j==x));
            bombs[rand_number_i][rand_number_j]=-1;
        }
    }
   
    public void step(int y, int x){
        if(y<0 || x<0 || x>n-1 ||y>m-1 || bombs[y][x]!=0) return;
        int bomb=0;
        //System.out.println("sapper.Sapper.step() start");
        for(int i=y-1; i<=y+1; i++){
            for(int j=x-1; j<=x+1; j++){
                if(!(j<0 || i<0 || j>n-1 || i>m-1) && bombs[i][j]==-1){
                    bomb++;
                }
            }
        }
        //System.out.println("sapper.Sapper.step() middle");
        if(bomb==0){
            bombs[y][x]=-2;
            for(int i=y-1; i<=y+1; i++){
                for(int j=x-1; j<=x+1; j++){
                    if(!( j<0 || i<0 || j>n-1 || i>m-1))
                    if(i!=y || j!=x) step(i,j);
                }
            }
        }else bombs[y][x]=bomb;
        //System.out.println("sapper.Sapper.step() end");
    }
    
    public void open(){
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                if(bombs[i][j]==-2){
                    btn[i][j].setText("");
                    btn[i][j].setDisable(true);
                    btn[i][j].setStyle("-fx-background-color:red;");
                }
                if(bombs[i][j]>0){
                    btn[i][j].setText(""+bombs[i][j]);
                    btn[i][j].setDisable(true);
                    btn[i][j].setStyle("-fx-background-color:red;");
                }
                if(bombs[i][j]==-1 && !canPlay){
                    btn[i][j].setDisable(true);
                    btn[i][j].setStyle("-fx-background-color:blue;");
                }
            }
        }
        System.out.println("sapper.Sapper.open()");
        System.out.println();
        for(int c=0; c<bombs.length; c++){
            for(int s=0; s<bombs[c].length; s++){
                System.out.print(bombs[c][s]+" ");
            }
            System.out.println();
        }
    }
    
    public void lose(){
        canPlay=false;
        boolean los=false;
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                if(bombs[i][j]==-1){
                    los=true;
                    btn[i][j].setDisable(true);
                    btn[i][j].setText("B");
                }
            }
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Lose");
        alert.setHeaderText("Unfortunately!");
        alert.setContentText("You are loser!");
        alert.showAndWait();
    }
    
    public void checkWin(){
        boolean win=true;
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                if(bombs[i][j]==0){
                    win=false;
                    break;
                //System.out.println("sapper.Sapper.checkWin()");
                }
            }
            if(!win) break;
        }
        if(win){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Win");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You are winner!");
            alert.showAndWait();
            canPlay=false;
        }
    }
    
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            boolean found=false;
            int j=0,i=0;
            int k=0,l=0;
            int v1=0,v2=0;
            for(i=0; i<m; i++){
                for(j=0; j<n; j++){
                    if(mouseEvent.getSource()==btn[i][j]){
                        
                        if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                            flags[i][j]=-9;
                            btn[i][j].setText("F");
                            btn[i][j].setDisable(true);
                            btn[i][j].setStyle("-fx-background-color:blue;");
                            mine_number-=1;
                            lbBombs.setText(mine_number+"");
                            
                            for(int q=0; q<flags.length; q++){
                                for(int w=0; w<flags[q].length; w++){
                                    System.out.print(flags[q][w]);
                                }
                                System.out.println();
                            }
                            break;
                        }
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            found=true;
                            k=i; l=j;
                            break;
                        }
                        break;
                    }
                }
            }

            if(canPlay){
                if(!first_step){
                    random_bombs(k, l);
                    first_step=true;
                }
                if(bombs[k][l]!=-1){
                    step(k, l);
                    open();
                    checkWin();
                }else lose();
                checkWin();
            }else open();
        } 
    };
    
    @Override
    public void start(Stage stage) {
        levels = FXCollections.observableArrayList(
            "Level "+l1,
            "Level "+l2,
            "Level "+l3,
            "Level "+l4,
            "Level "+l5,
            "Level "+l6,
            "Level "+l7
        );
        nxm = FXCollections.observableArrayList(
            n5+" x "+n5,
            n10+" x "+n10,
            n10+" x "+n15,
            n10+" x "+n20
        );
           
        final ComboBox comboBoxLevels = new ComboBox(levels);
        comboBoxLevels.setPromptText("Select level");
        comboBoxLevels.setDisable(true);
        final ComboBox comboBoxNxM = new ComboBox(nxm);
        comboBoxNxM.setPromptText("Select size");
        Button btnPlay=new Button("Play");
        
        VBox rootV = new VBox(10);
        rootV.getChildren().addAll(comboBoxNxM,comboBoxLevels,btnPlay);
        rootV.setAlignment(Pos.CENTER);
        
        sceneStart=new Scene(rootV,300,200);
        
        GridPane root = new GridPane();
        root.setGridLinesVisible(true);
        root.setAlignment(Pos.CENTER);
        
        BorderPane rootB=new BorderPane();
        rootB.setCenter(root);
        
        lbLevel=new Label("Hello world!");
        lbLevel.setMaxWidth(100);
        lbBombs=new Label(mine_number+"");
        lbBombs.setMaxWidth(100);
        rePlay=new Button("RePlay");
        
        /*rePlay.setOnAction(e->{
            stage.setScene(sceneStart);
        });*/
        
        HBox rootH = new HBox(10);
        rootH.setPadding(new Insets(20.0,0,0,0));
        rootH.getChildren().addAll(lbLevel,lbBombs,rePlay);
        rootH.setAlignment(Pos.CENTER);
        rootB.setTop(rootH);
        
        
        comboBoxNxM.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                if(t1.equals(n5+" x "+n5)){
                    width=width*n5;
                    height=height*n5;
                    n=n5;m=n5;
                }else if(t1.equals(n10+" x "+n10)){ 
                    width=width*n10;
                    height=height*n10;
                    n=n10; m=n10;
                }else if(t1.equals(n10+" x "+n15)){
                    width=width*n15;
                    height=height*n15;
                    n=n10; m=n15;
                }else if(t1.equals(n10+" x "+n20)){
                    width=width*n20;
                    height=height*n20;
                    n=n10; m=n20;
                }
                btn=new Button[n][m];
                comboBoxLevels.setDisable(false);
            }            
        });
        
        btnPlay.setOnAction(e->{
            for(int i=0; i<n; i++){
                for(int y=0; y<m; y++){
                    btn[i][y]=new Button(String.valueOf(" "));
                    btn[i][y].setPrefSize(30.0, 30.0);
                    btn[i][y].setOnMouseClicked(mouseHandler);
                    root.add(btn[i][y], y, i);
                }
            }
            first_step=false;
            canPlay=true;
            stage.setScene(sceneMain);
        });
        
        comboBoxLevels.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                if(t1.equals("Level "+l1)){
                    percent=((10+l1*5)/100)*(n*m);
                }
                if(t1.equals("Level "+l2)){
                    percent=((10+l2*5)/100)*(n*m);
                }
                if(t1.equals("Level "+l3)){
                    percent=((10+l3*5)/100)*(n*m);
                }
                if(t1.equals("Level "+l4)){
                    percent=((10+l4*5)/100)*(n*m);
                }
                if(t1.equals("Level "+l5)){
                    percent=((10+l5*5)/100)*(n*m);
                }
                if(t1.equals("Level "+l6)){
                    percent=((10+l6*5)/100)*(n*m);
                }
                if(t1.equals("Level "+l7)){
                    percent=((10+l7*5)/100)*(n*m);
                }
                System.out.println(percent);
                //System.out.println(n);
                //System.out.println(m);
            }    
        });
        
        
        sceneMain = new Scene(rootB,width, height);
        
        stage.setTitle("!");
        stage.setScene(sceneStart);
        stage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("HELLO");
        System.out.println(15.0/100);
        launch(args);
    } 
}
