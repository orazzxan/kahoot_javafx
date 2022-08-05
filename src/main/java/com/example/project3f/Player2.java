package com.example.project3f;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player2 extends Application {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Stage stage;
    private int seconds = 30;
    private int afterRound = 10;
    private Timeline timeline = new Timeline();
    private int i = 0;
    private BorderPane design = new BorderPane();
    private String quest;
    private String proverka;
    private String proverkaTest;
    private int allPoint;
    private int pointRound;
    private Timeline timeline2 = new Timeline();
    private Text questionNum = new Text();
    private Text textPoint = new Text();

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public String changeAfterRound() throws IOException {
        String text = String.valueOf(afterRound);
        afterRound--;

        if (afterRound == -1) {
            seconds = 30;
            i++;

            String quest = in.readUTF();
            System.out.println(quest);

            if (quest.equals("test")) {
                stage.setScene(new Scene(gameTest(), 500, 600));
            } else if (quest.equals("fill")) {
                stage.setScene(new Scene(gameFill(), 500, 600));
            }

            if (quest.equals("end")) {
                seconds = -100;
                afterRound = -100;
                timeline.stop();
                timeline2.stop();

                Text t = new Text("YOUR TOTAL POINTS COUNT : \n" + allPoint);
                t.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 30));
                t.setFill(Color.WHITE);

                StackPane endPane = new StackPane();
                endPane.setStyle("-fx-background-color: #3e147f");

                endPane.getChildren().addAll(t);

                stage.setScene(new Scene(endPane, 500, 600));
            }


        }
        return text;
    }

    public void timerAfterRound() throws IOException {
        Text aha = new Text();
        aha.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 30));
        aha.setText(changeAfterRound());
        timeline2.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            try {
                aha.setText(changeAfterRound());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        timeline2.setCycleCount(-1);
        timeline2.play();
    }

    public String change() throws IOException {
        String text = String.valueOf(seconds);
        ;
        seconds--;
        if (seconds == -1) {
            if (i == 0) {
                timerAfterRound();
            }
            afterRound = 10;

            allPoint = allPoint + pointRound;
            System.out.println("Zarabotano za round:" + pointRound + " v obshem: " + allPoint);

            Text points = new Text("+" + pointRound);
            points.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 30));
            points.setFill(Color.WHITE);
            StackPane pointPane = new StackPane();
            pointPane.setStyle("-fx-background-color: #3e147f");
            pointPane.getChildren().addAll(points);

            stage.setScene(new Scene(pointPane, 500, 600));
            pointRound = 0;

        }
        return text;
    }

    public void timer() throws IOException {
        Text aha = new Text();
        aha.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 30));
        aha.setText(change());
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            try {
                aha.setText(change());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();

    }

    public StackPane loadingAnimation() throws IOException {
        StackPane stackPane = new StackPane();

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.WHITE);
        rectangle.setWidth(15);
        rectangle.setHeight(15);

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(rectangle);
        rotateTransition.setDuration(Duration.seconds(1));
        rotateTransition.setCycleCount(-1);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setByAngle(360);
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.play();

        vBox.getChildren().addAll(new StackPane(rectangle));

        stackPane.getChildren().addAll(vBox);

        stackPane.setStyle("-fx-background-color: #3e147f");

        return stackPane;
    }

    public Button button(String color) {
        String thiscolor = color;

        Button butt = new Button();
        butt.setMinHeight(270);
        butt.setMinWidth(245);
        butt.setStyle("-fx-background-color: " + thiscolor);

        return butt;
    }

    public StackPane gameTest() throws IOException {
        StackPane stackPane = new StackPane();

        VBox v1 = new VBox(5);
        VBox v2 = new VBox(5);
        HBox buttons = new HBox(5);

        Button red = button("red");
        Button orange = button("orange");
        Button blue = button("blue");
        Button green = button("green");

        v1.getChildren().addAll(red, orange);
        v2.getChildren().addAll(blue, green);
        buttons.getChildren().addAll(v1, v2);

        questionNum.setText(String.valueOf(i + 1));
        questionNum.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 20));
        questionNum.setFill(Color.BLACK);

        textPoint.setText(String.valueOf(allPoint));
        textPoint.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 20));

        textPoint.setTextAlignment(TextAlignment.CENTER);

        red.setOnAction(r -> {
            try {
                out.writeUTF("Red");
                proverkaTest = in.readUTF();
                if (proverkaTest.equals("Correct")) {
                    System.out.println("Correct Finished in:" + seconds);
                    pointRound = 1000 - ((30 - seconds) * 33);

                } else System.out.println("Incorrect");

                stage.setScene(new Scene(loadingAnimation(), 500, 600));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        orange.setOnAction(o -> {
            try {
                out.writeUTF("Orange");
                proverkaTest = in.readUTF();
                if (proverkaTest.equals("Correct")) {
                    System.out.println("Correct Finished in:" + seconds);
                    pointRound = 1000 - ((30 - seconds) * 33);

                } else System.out.println("Incorrect");

                stage.setScene(new Scene(loadingAnimation(), 500, 600));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        blue.setOnAction(b -> {
            try {
                out.writeUTF("Blue");
                proverkaTest = in.readUTF();
                if (proverkaTest.equals("Correct")) {
                    System.out.println("Correct Finished in:" + seconds);
                    pointRound = 1000 - ((30 - seconds) * 33);

                } else System.out.println("Incorrect");

                stage.setScene(new Scene(loadingAnimation(), 500, 600));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        green.setOnAction(g -> {
            try {
                out.writeUTF("Green");
                proverkaTest = in.readUTF();
                if (proverkaTest.equals("Correct")) {
                    System.out.println("Correct Finished in:" + seconds);
                    pointRound = 1000 - ((30 - seconds) * 33);

                } else System.out.println("Incorrect");

                stage.setScene(new Scene(loadingAnimation(), 500, 600));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        design.setTop(new StackPane(questionNum));
        design.setCenter(new StackPane(buttons));
        design.setBottom(new StackPane(textPoint));
        stackPane.setStyle("-fx-background-color: white");
        stackPane.requestFocus();

        stackPane.getChildren().addAll(design);
        return stackPane;
    }

    public StackPane gameFill() throws IOException {
        StackPane stackPane = new StackPane();

        BorderPane borderPane = new BorderPane();
        TextField textField = new TextField();
        textField.setMaxWidth(300);
        textField.setAlignment(Pos.CENTER);
        textField.setPromptText("Answer");
        borderPane.setCenter(textField);
        Button button = new Button("Enter");
        button.setMaxWidth(150);
        button.setMaxHeight(50);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: black");
        borderPane.setBottom(new StackPane(button));

        questionNum.setText(String.valueOf(i + 1));
        questionNum.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 20));
        questionNum.setFill(Color.BLACK);


        textPoint.setText(String.valueOf(allPoint));
        textPoint.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 20));
        textPoint.setTextAlignment(TextAlignment.CENTER);

        design.setBottom(new StackPane(textPoint));
        design.setTop(new StackPane(questionNum));
        design.setCenter(new StackPane(borderPane));

        stackPane.setStyle("-fx-background-color: white");
        stackPane.requestFocus();

        button.setOnAction(ee -> {
            try {
                out.writeUTF(textField.getText());

                proverka = in.readUTF();
                if (proverka.equals("Correct")) {
                    System.out.println("Correct Finished in " + seconds);
                    pointRound = 1000 - ((30 - seconds) * 33);

                } else System.out.println("Incorrect");

                stage.setScene(new Scene(loadingAnimation(), 500, 600));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stackPane.getChildren().addAll(design);
        return stackPane;
    }

    public StackPane nicknamePlayer() throws IOException {
        StackPane stackPane = new StackPane();
        VBox vBox = new VBox(10);

        Text setNick = new Text("ENTER NICKNAME:");
        setNick.setFill(Color.WHITE);
        setNick.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));

        TextField textField = new TextField();
        textField.setMaxWidth(150);
        textField.setMinHeight(30);
        textField.setPromptText("NICKNAME");
        textField.setAlignment(Pos.CENTER);

        Button enterNick = new Button();
        enterNick.setText("ENTER");
        enterNick.setStyle("-fx-background-color: black");
        enterNick.setMaxWidth(150);
        enterNick.setMinHeight(40);
        enterNick.setTextAlignment(TextAlignment.CENTER);
        enterNick.setTextFill(Color.WHITE);

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(setNick, textField, enterNick);

        enterNick.setOnAction(en -> {
            try {
                out.writeUTF(textField.getText());
                // stage.setScene(new Scene(loadingAnimation(), 500, 600));

                String check = in.readUTF();

                if (check.equals("start")) {
                    timer();
                    String checkQuestion = in.readUTF();
                    System.out.println(checkQuestion);
                    if (checkQuestion.equals("test")) {
                        stage.setScene(new Scene(gameTest(), 500, 600));
                    } else if (checkQuestion.equals("fill")) {
                        stage.setScene(new Scene(gameFill(), 500, 600));
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #3e147f");
        stackPane.requestFocus();
        return stackPane;
    }

    public StackPane pinPlayer() {
        StackPane stackPane = new StackPane();
        VBox vBox = new VBox(10);

        Text setPin = new Text("ENTER PIN:");
        setPin.setFill(Color.WHITE);
        setPin.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));

        TextField textField = new TextField();
        textField.setMaxWidth(150);
        textField.setMinHeight(30);
        textField.setPromptText("GAME PIN");
        textField.setAlignment(Pos.CENTER);

        Text errorText = new Text("SUCH GAME DOESN'T EXIST. TRY AGAIN");
        errorText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        errorText.setFill(Color.WHITE);

        Button enterPin = new Button();
        enterPin.setText("ENTER");
        enterPin.setStyle("-fx-background-color: black");
        enterPin.setMaxWidth(150);
        enterPin.setMinHeight(40);
        enterPin.setTextAlignment(TextAlignment.CENTER);
        enterPin.setTextFill(Color.WHITE);

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(setPin, textField, enterPin);

        stackPane.getChildren().addAll(vBox);


        enterPin.setOnAction(se -> {
            try {
                out.writeInt(Integer.parseInt(textField.getText()));
                String check = in.readUTF();

                if (check.equals("1")) {
                    vBox.getChildren().removeAll(errorText);
                    stage.setScene(new Scene(nicknamePlayer(), 500, 600));
                } else vBox.getChildren().addAll(errorText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stackPane.setStyle("-fx-background-color: #3e147f");
        return stackPane;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        socket = new Socket("localhost", 8089);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        StackPane stackPane = pinPlayer();

        stackPane.getChildren().add(pinPlayer());

        stackPane.requestFocus();
        Scene scene = new Scene(stackPane, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
