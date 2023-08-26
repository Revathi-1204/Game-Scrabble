package com.boardui;

 

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;

import javafx.event.EventHandler;

import javafx.geometry.Insets;

 

import javafx.scene.Scene;

 

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

 

 

import javafx.stage.Stage;

 

 

 

public class Main extends Application {

 

    private final int BOARD_SIZE = 15;

 

    private final int TILE_SIZE = 30;

 

    private final Color BOARD_COLOR = Color.LIGHTGRAY;

 

    private final int RACK_SIZE = 7;

 

    private final Color TILE_COLOR = Color.WHITE;

 

    Board board;

    Bag bag;

    Player player;

    VerifyAndScore verifyAndScore;

 

    private Button[][] boardTiles;

    private Button[] rackTiles;

 

 

    public static void main(String[] args) {

 

        launch(args);

 

    }

    public void newGame()
	{
		board = new Board();
		player = new Player();
		engine = new Engine(player, board);
		bag = new Bag();
	}
	

 

 

    @Override

 

    public void start(Stage primaryStage) {

 

        primaryStage.setTitle("Scrabble Game");

 

        board = new Board();

        bag = new Bag();

        player = new Player();

        verifyAndScore = new VerifyAndScore(player, board);

 

        GridPane gameBoard = createBoard(board.cellMatrix);

 

        gameBoard = createCellButtons(board.cellMatrix);

       

        givePlayerStartingTiles();

 

        GridPane tilesRack = createTilesRack();

 

        tilesRack = createButtonForRack();

 

        GridPane controlPanel = createRackPanel();

        GridPane childPane = new GridPane();

        childPane.add(tilesRack,0,0);
        childPane.add(controlPanel,1,0);

 

        GridPane root = new GridPane();

 

        root.setPadding(new Insets(10));

 

        root.setHgap(10);

 

        root.setVgap(10);

 

 

 

        root.add(gameBoard, 0, 0);

 

        // root.add(tilesRack, 0, 1);

 

        root.add(childPane, 0, 1);

 

 

 

        Scene scene = new Scene(root);

 

        primaryStage.setScene(scene);

 

        primaryStage.show();
 

    }

 

    private GridPane createBoard(Cell cellMatrix[][]) {

 

        GridPane gameboard = new GridPane();

 

        gameboard.setPadding(new Insets(10));

 

        gameboard.setHgap(2);

 

        gameboard.setVgap(2);

 

        gameboard.setStyle("-fx-background-color: " + BOARD_COLOR.toString().replace("0x", "#") + ";");

 

 

 

        boardTiles = new Button[BOARD_SIZE][BOARD_SIZE];

 

        for (int row = 0; row < BOARD_SIZE; row++) {

            for (int col = 0; col < BOARD_SIZE; col++) {

                Button tile = new Button();

                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                tile.setStyle("-fx-background-color: " + TILE_COLOR.toString().replace("0x", "#") + ";"); // Set the background color

                tile.setBorder(null);

                tile.setText(cellMatrix[row][col].getBonus());

                boardTiles[row][col] = tile;

                gameboard.add(tile, col, row);

 

            }

        }

 

        return gameboard;

 

    }

 

 

    private GridPane createTilesRack() {

 

        GridPane tilesRack = new GridPane();

 

        tilesRack.setPadding(new Insets(10));

 

        tilesRack.setHgap(5);

 

        tilesRack.setVgap(5);

 

        tilesRack.setStyle("-fx-background-color: " + BOARD_COLOR.toString().replace("0x", "#") + ";");

 

 

        rackTiles = new Button[RACK_SIZE];

 

        // TODO: Add tiles to the rack

            for (int i = 0; i < RACK_SIZE; i++) {

                    Button tile = new Button();

                    tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                    tile.setStyle("-fx-background-color: " + TILE_COLOR.toString().replace("0x", "#") + ";");

                    tile.setBorder(null);

                    rackTiles[i] = tile;

                    tilesRack.add(tile, i, 0);

                }

 

        return tilesRack;

 

    }

 

    public GridPane createCellButtons(Cell[][] cellMatrix){

        GridPane gameboard = new GridPane();

        for(int i = 0; i < BOARD_SIZE;i++){

            for(int j = 0;j < BOARD_SIZE;j++){

                Button cellButton = new Button();

                cellButton.setStyle("-fx-background-Color:"+Color.LIGHTGRAY.toString().replace("0x","#")+";");

                cellButton.setPrefSize(TILE_SIZE, TILE_SIZE);

                cellButton.setBorder(null);
                
                cellButton.setText(cellMatrix[i][j].getBonus());

                Cell currentCell = cellMatrix[i][j];

                cellButton.setOnMouseClicked(new EventHandler<Event>() {

 

                    @Override

                    public void handle(Event event) {

                        // TODO Auto-generated method stub

                        if(verifyAndScore.rackTileSelected != null && currentCell.getTile() == null)

                        {

                            currentCell.setTile(verifyAndScore.rackTileSelected);

                            cellButton.setText(verifyAndScore.rackTileSelected.getLetter());                            

                            cellButton.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

                            verifyAndScore.rackTileSelected = null;

                            verifyAndScore.recentlyPlayedCellStack.push(currentCell);

                            verifyAndScore.recentlyPlayedCellButtonStack.push(cellButton);

                        }

                        // cellButton.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

                        // cellButton.setText("H");

                    }

                   

                });

                boardTiles[i][j]=cellButton;

                gameboard.add(cellButton,i,j);

            }

        }

        return gameboard;

    }

 

    public GridPane createButtonForRack(){

        GridPane rack = new GridPane();

        rackTiles = new Button[7];

       

        Button rackButton1 = new Button();

        rackButton1.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

        rackButton1.setText(player.getRack()[0].getLetter());

       

        rackButton1.setOnMouseClicked(new EventHandler<Event>() {

 

            @Override

            public void handle(Event event) {

                // TODO Auto-generated method stub

                if(verifyAndScore.rackTileSelected == null && rackButton1.getText() != "")

                {

                    verifyAndScore.rackTileSelected = player.getAndRemoveFromRackAt(0);

                    verifyAndScore.recentlyPlayedTileStack.push(verifyAndScore.rackTileSelected);

 

                    rackButton1.setStyle("-fx-background-Color:"+Color.LIGHTGREY.toString().replace("0x","#")+";");

                    rackButton1.setText("");

               

                }

            }

           

        });

       

        rackTiles[0] = rackButton1;

        rack.add(rackButton1,0,0);

       

        Button rackButton2 = new Button();

        rackButton2.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

        rackButton2.setText(player.getRack()[1].getLetter());

       

        rackButton2.setOnMouseClicked(new EventHandler<Event>() {

 

            @Override

            public void handle(Event event) {

                // TODO Auto-generated method stub

                if(verifyAndScore.rackTileSelected == null && rackButton2.getText() != "")

                {

                    verifyAndScore.rackTileSelected = player.getAndRemoveFromRackAt(1);

                    verifyAndScore.recentlyPlayedTileStack.push(verifyAndScore.rackTileSelected);

 

                    rackButton2.setStyle("-fx-background-Color:"+Color.LIGHTGREY.toString().replace("0x","#")+";");

                    rackButton2.setText("");

               

                }

            }

           

        });

       

        rackTiles[1] = rackButton2;

        rack.add(rackButton2,1,0);

       

       

        Button rackButton3 = new Button();

        rackButton3.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

        rackButton3.setText(player.getRack()[2].getLetter());

       

        rackButton3.setOnMouseClicked(new EventHandler<Event>() {

 

            @Override

            public void handle(Event event) {

                // TODO Auto-generated method stub

                if(verifyAndScore.rackTileSelected == null && rackButton3.getText() != "")

                {

                    verifyAndScore.rackTileSelected = player.getAndRemoveFromRackAt(2);

                    verifyAndScore.recentlyPlayedTileStack.push(verifyAndScore.rackTileSelected);

 

                    rackButton3.setStyle("-fx-background-Color:"+Color.LIGHTGREY.toString().replace("0x","#")+";");

                    rackButton3.setText("");

               

                }

            }

           

        });

       

        rackTiles[2] = rackButton3;

        rack.add(rackButton3,2,0);

       

        Button rackButton4 = new Button();

        rackButton4.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

        rackButton4.setText(player.getRack()[3].getLetter());

       

        rackButton4.setOnMouseClicked(new EventHandler<Event>() {

 

            @Override

            public void handle(Event event) {

                // TODO Auto-generated method stub

                if(verifyAndScore.rackTileSelected == null && rackButton4.getText() != "")

                {

                    verifyAndScore.rackTileSelected = player.getAndRemoveFromRackAt(3);

                    verifyAndScore.recentlyPlayedTileStack.push(verifyAndScore.rackTileSelected);

 

                    rackButton4.setStyle("-fx-background-Color:"+Color.LIGHTGREY.toString().replace("0x","#")+";");

                    rackButton4.setText("");

               

                }

            }

           

        });

       

        rackTiles[3] = rackButton4;

        rack.add(rackButton4,3,0);

       

        Button rackButton5 = new Button();

        rackButton5.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

        rackButton5.setText(player.getRack()[4].getLetter());

       

        rackButton5.setOnMouseClicked(new EventHandler<Event>() {

 

            @Override

            public void handle(Event event) {

                // TODO Auto-generated method stub

                if(verifyAndScore.rackTileSelected == null && rackButton5.getText() != "")

                {

                    verifyAndScore.rackTileSelected = player.getAndRemoveFromRackAt(4);

                    verifyAndScore.recentlyPlayedTileStack.push(verifyAndScore.rackTileSelected);

 

                    rackButton5.setStyle("-fx-background-Color:"+Color.LIGHTGREY.toString().replace("0x","#")+";");

                    rackButton5.setText("");

               

                }

            }

           

        });

       

        rackTiles[4] = rackButton5;

        rack.add(rackButton5,4,0);

       

        Button rackButton6 = new Button();

        rackButton6.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

        rackButton6.setText(player.getRack()[5].getLetter());

       

        rackButton6.setOnMouseClicked(new EventHandler<Event>() {

 

            @Override

            public void handle(Event event) {

                // TODO Auto-generated method stub

                if(verifyAndScore.rackTileSelected == null && rackButton6.getText() != "")

                {

                    verifyAndScore.rackTileSelected = player.getAndRemoveFromRackAt(5);

                    verifyAndScore.recentlyPlayedTileStack.push(verifyAndScore.rackTileSelected);

 

                    rackButton6.setStyle("-fx-background-Color:"+Color.LIGHTGREY.toString().replace("0x","#")+";");

                    rackButton6.setText("");

               

                }

            }

           

        });

       

        rackTiles[5] = rackButton6;

        rack.add(rackButton6,5,0);

       

        Button rackButton7 = new Button();

        rackButton7.setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x","#")+";");

        rackButton7.setText(player.getRack()[6].getLetter());

       

        rackButton7.setOnMouseClicked(new EventHandler<Event>() {

 

            @Override

            public void handle(Event event) {

                // TODO Auto-generated method stub

                if(verifyAndScore.rackTileSelected == null && rackButton7.getText() != "")

                {

                    verifyAndScore.rackTileSelected = player.getAndRemoveFromRackAt(6);

                    verifyAndScore.recentlyPlayedTileStack.push(verifyAndScore.rackTileSelected);

 

                    rackButton7.setStyle("-fx-background-Color:"+Color.LIGHTGREY.toString().replace("0x","#")+";");

                    rackButton7.setText("");

               

                }

            }

           

        });

       

        rackTiles[6] = rackButton7;

        rack.add(rackButton7,6,0);

        return rack;

    }

    private GridPane createRackPanel(){
        GridPane playerPanel = new GridPane();
        GridPane rackPanel = new GridPane();
        GridPane actionPanel = new GridPane();
        Label scorLabel = new Label("0");

        Button shuffleButton = new Button("Shuffle");
        shuffleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                player.shuffleRack();
                player.organizeRack();
                updatePlayerRackGUI();
            }
        });

        Button undoButton = new Button("Undo");
        undoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                if(verifyAndScore.recentlyPlayedTileStack.size() > 0 && verifyAndScore.recentlyPlayedCellStack.size() > 0 && verifyAndScore.recentlyPlayedCellButtonStack.size() > 0 && verifyAndScore.rackTileSelected == null)
                {
                    Tile recenTilePlayed = verifyAndScore.recentlyPlayedTileStack.pop();
                    player.addTileToRack(recenTilePlayed);
                    Cell recentCell = verifyAndScore.recentlyPlayedCellStack.pop();
                    recentCell.setTile(null);

                    Button recentCellButton = verifyAndScore.recentlyPlayedCellButtonStack.pop();
                    recentCellButton.setText(recentCell.getBonus());
                    recentCellButton.setStyle("-fx-background-Color:"+Color.LIGHTGRAY.toString().replace("0x", "#")+";");

                    player.organizeRack();
                    updatePlayerRackGUI();
                } 
            }
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                if(verifyAndScore.checkBoard() == true)
                {
                    System.out.println("Move Passed");
                    scorLabel.setText(player.getScore());

                    while(true){
                        if(givePlayerANewTile() == false){
                            break;
                        }
                    }
                    updatePlayerRackGUI();
                    bag.populateBag();
                }
                else{
                    System.out.println("Move denied");
                }
            }
        });
        actionPanel.add(shuffleButton,0,0);
        actionPanel.add(undoButton,1,0);
        actionPanel.add(submitButton,2,0);
        Label pointsLabel = new Label("pts.");
        playerPanel.add(actionPanel,2,0);
        playerPanel.add(scorLabel,0,0);
        playerPanel.add(pointsLabel,1,0);
        rackPanel.add(playerPanel,0,0);
        return rackPanel;
    }

    private boolean givePlayerANewTile()

    {

        //cannot add tiles to player's rack if bag is empty or rack is full (rack has more than 7 elements)

        if(bag.bagIsEmpty() || (player.getRackSize() == 7))

        {

            return false;

        }

       

        Tile newTile = bag.getNextTile();

        player.addTileToRack(newTile);

        return true;

    }

 


    private void givePlayerStartingTiles()

        {

            for(int i = 0; i < 7; i++)

            {

                givePlayerANewTile();

            }

        }
    
    private void updatePlayerRackGUI() //places tiles pieces found directly in player's rack onto rack button GUI
    {
        for(int i = 0; i < 7; i++)
        {
            if(player.getRack()[i] != null)
            {
                rackTiles[i].setText(player.getRack()[i].getLetter());
                rackTiles[i].setStyle("-fx-background-Color:"+Color.ORANGE.toString().replace("0x", "#")+";");
            }
            else if(player.getRack()[i] == null)
            {
                rackTiles[i].setText("");
                rackTiles[i].setStyle("-fx-background-Color:"+Color.LIGHTGRAY.toString().replace("0x", "#")+";");
            }
            
        }
    }
}
 