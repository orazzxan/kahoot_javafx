package com.example.project3f;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.GatheringByteChannel;
import java.util.*;

public class QuizMaker extends Application {
    private Quiz quiz = new Quiz();
    private ArrayList<Question> questions = new ArrayList<>();
    private int i = 0;
    private int correct = 0;
    private Timeline timeline = new Timeline();
    private int seconds = 30;
    private BorderPane design = new BorderPane();
    private Random rnd = new Random();
    private int pin = rnd.nextInt(100000, 999999);
    private DataInputStream in;
    private DataOutputStream out;
    private ServerSocket serverSocket;
    private Socket socket;
    private String nickname;
    private int afterRound = 10;
    private Timeline timeline2 = new Timeline();
    private VBox top = new VBox(5);
    private int allPoint;
    private int pointRound;
    private Rectangle rec = new Rectangle();
    private Text t = new Text();
    private ArrayList<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public String changeAfterRound() throws IOException {
        String text = String.valueOf(afterRound);
        afterRound--;

        if (afterRound == -1) {
            seconds = 30;
            i++;

            if (i == questions.size()) {
                seconds = -100;
                afterRound = -100;

                timeline.stop();
                timeline2.stop();
                out.writeUTF("end");

                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(1);
                rectangle.setHeight(1);
                rectangle.setFill(Color.PURPLE);

                Text end = new Text();
                end.setText("THANK YOU FOR TAKING A QUIZ! \nTHERE IS A RESULT: ");
                end.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 30));
                end.setFill(Color.WHITE);

                Text endText = new Text(nickname + " - " + allPoint);
                endText.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 30));
                endText.setFill(Color.WHITE);

                top.getChildren().addAll(endText);
                top.setAlignment(Pos.CENTER);

                design.getChildren().removeAll(info());
                design.setTop(new StackPane(end));
                design.setCenter(new StackPane(top));
                design.setBottom(rectangle);
                design.setStyle("-fx-background-color: #3e147f");

            }

            if (questions.get(i) instanceof Test) {
                out.writeUTF("test");
            } else if (questions.get(i) instanceof FillIn) {
                out.writeUTF("fill");
            } else System.out.println("ladno");

            design.setStyle("-fx-background-color: white");
            design.setCenter(page(i));

            System.out.println(i);

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

            Text points = new Text(nickname + " - " + allPoint);
            points.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 30));
            points.setFill(Color.WHITE);
            StackPane pointPane = new StackPane();
            pointPane.setStyle("-fx-background-color: black");
            pointPane.getChildren().addAll(points);

            design.setStyle("-fx-background-color: black");
            design.setCenter(pointPane);
            pointRound = 0;

        }
        return text;
    }

    public Text timer() throws IOException {
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
        return aha;
    }

    public ImageView imageView(String jpg) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("C:\\Users\\user\\IdeaProjects\\project3f\\src\\" + jpg));
        ImageView imageView = new ImageView(image);
        return imageView;
    }

    public HBox info() {
        HBox hBox = new HBox(10);
        Text t = new Text("Game pin: ");
        t.setFont(Font.font("Gill Sans Nova", FontWeight.LIGHT, 21));
        Text pinText = new Text(String.valueOf(pin));
        pinText.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 21));

        hBox.getChildren().addAll(t, pinText);

        return hBox;
    }

    public BorderPane currentQuestion(int index) {

        BorderPane questDesc = new BorderPane();
        ImageView k = null;
        try {
            k = imageView("k.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        k.setFitHeight(30);
        k.setFitWidth(50);

        Text description = new Text(index + 1 + ") " + questions.get(index).getDescription());
        description.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 22));

        if (description.getText().contains("{blank}")) {
            description.setText(description.getText().replace("{blank}", "______"));
            HBox filn = new HBox();
            filn.getChildren().addAll(k, description);
            filn.setAlignment(Pos.BOTTOM_CENTER);
            questDesc.setCenter(filn);

        } else {
            questDesc.setCenter(description);
        }

        return questDesc;
    }

    public StackPane page(int index) throws IOException {
        StackPane stackPane = new StackPane();
        StackPane[] stackPanes = new StackPane[questions.size()];

        ImageView es = null;
        try {
            es = imageView("result.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        es.setFitHeight(675);
        es.setFitWidth(1200);
        try {
            ImageView k = imageView("k.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ImageView chose = imageView("background.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView test = null;
        try {
            test = imageView("logo.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        test.setFitWidth(500);
        test.setFitHeight(250);
        ;
        ImageView fillinjpg = null;
        try {
            fillinjpg = imageView("fillin.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fillinjpg.setFitWidth(500);
        fillinjpg.setFitHeight(250);

        BorderPane design = new BorderPane();

        HBox[] hBoxes = new HBox[questions.size()];

        Rectangle red = new Rectangle();
        Rectangle orange = new Rectangle();
        Rectangle blue = new Rectangle();
        Rectangle green = new Rectangle();

        red.setWidth(595);
        red.setHeight(143);
        red.setFill(Color.RED);
        orange.setWidth(595);
        orange.setHeight(143);
        orange.setFill(Color.ORANGE);
        blue.setWidth(595);
        blue.setHeight(143);
        blue.setFill(Color.BLUE);
        green.setWidth(595);
        green.setHeight(143);
        green.setFill(Color.GREEN);

        HBox hBox = new HBox(5);
        VBox v2 = new VBox(5);
        VBox v1 = new VBox(5);

        Text filln = new Text("Please write a word in your field");
        filln.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 15));

        Rectangle nevidimka = new Rectangle();
        nevidimka.setWidth(595);
        nevidimka.setHeight(262);
        nevidimka.setFill(Color.WHITE);

        if (questions.get(i) instanceof Test) {
            String ans = "";
            String line = questions.get(i).toString();
            String[] lines = line.split("\n");

            if (questions.get(i).getAnswer().equals("A")) {
                ans = lines[1];
            }
            if (questions.get(i).getAnswer().equals("B")) {
                ans = lines[2];
            }
            if (questions.get(i).getAnswer().equals("C")) {
                ans = lines[3];
            }
            if (questions.get(i).getAnswer().equals("D")) {
                ans = lines[4];
            }

            Text redText = new Text(lines[1]);
            redText.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 21));
            redText.setFill(Color.WHITE);
            Text orangeText = new Text(lines[2]);
            orangeText.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 21));
            orangeText.setFill(Color.WHITE);
            Text blueText = new Text(lines[3]);
            blueText.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 21));
            blueText.setFill(Color.WHITE);
            Text greenText = new Text(lines[4]);
            greenText.setFont(Font.font("Gill Sans Nova", FontWeight.BOLD, 21));
            greenText.setFill(Color.WHITE);

            StackPane redStack = new StackPane();
            redStack.getChildren().addAll(red, redText);
            StackPane orangeStack = new StackPane();
            orangeStack.getChildren().addAll(orange, orangeText);
            StackPane blueStack = new StackPane();
            blueStack.getChildren().addAll(blue, blueText);
            StackPane greenStack = new StackPane();
            greenStack.getChildren().addAll(green, greenText);

            v1.getChildren().addAll(redStack, orangeStack);
            v2.getChildren().addAll(blueStack, greenStack);
            hBox.getChildren().addAll(v1, v2);

            hBox.setAlignment(Pos.BOTTOM_CENTER);
            hBoxes[i] = hBox;

            design.setTop(new StackPane(currentQuestion(i)));
            design.setCenter(new StackPane(test));
            design.setBottom(new StackPane(hBoxes[i]));
        } else if (questions.get(i) instanceof FillIn) {
            VBox v = new VBox();
            v.getChildren().addAll(filln, nevidimka);
            v.setAlignment(Pos.BOTTOM_CENTER);

            design.setBottom(new StackPane(v));
            design.setTop(new StackPane(currentQuestion(i)));
            design.setCenter(new StackPane(fillinjpg));
        }

        stackPane.getChildren().addAll(design);
        stackPanes[i] = stackPane;
        return stackPanes[i];
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ImageView chose = imageView("background.jpg");

        FileChooser fileChooser = new FileChooser();

        Text pinText = new Text("PIN: " + pin);
        pinText.setFill(Color.WHITE);
        pinText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 50));

        StackPane stackPane = new StackPane();

        Button gameStart = new Button("START");
        gameStart.setStyle("-fx-background-color: red");
        gameStart.setMaxWidth(150);
        gameStart.setMinHeight(40);
        gameStart.setTextAlignment(TextAlignment.CENTER);
        gameStart.setTextFill(Color.WHITE);
        gameStart.setAlignment(Pos.BOTTOM_CENTER);

        VBox paneForNicks = new VBox();
        paneForNicks.setAlignment(Pos.CENTER);
        paneForNicks.setMaxHeight(300);
        paneForNicks.setLayoutY(300);
        paneForNicks.setMaxWidth(1000);

        BorderPane waitPlayer = new BorderPane();
        waitPlayer.setTop(new StackPane(pinText));
        waitPlayer.setCenter(new StackPane(paneForNicks));
        waitPlayer.setBottom(new StackPane(gameStart));

        Button choose = new Button("Choose a file");
        stackPane.getChildren().addAll(chose, choose);
        choose.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                questions = quiz.loadFromFile(file.getPath());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            stackPane.getChildren().removeAll(chose, choose);
            stackPane.setStyle("-fx-background-color: #3e147f");
            stackPane.getChildren().addAll(waitPlayer);
        });

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8089);
                while (true) {
                    socket = serverSocket.accept();
                    sockets.add(socket);
                    new Thread(() -> {
                        try {
                            in = new DataInputStream(socket.getInputStream());
                            out = new DataOutputStream(socket.getOutputStream());

                            while (true) {
                                int getPlayerPin = in.readInt();
                                System.out.println(getPlayerPin);
                                if (getPlayerPin == pin) {
                                    out.writeUTF("1");
                                    break;
                                } else out.writeUTF("0");
                            }

                            nickname = in.readUTF();
                            Text textNick = new Text(nickname);
                            textNick.setFill(Color.WHITE);
                            textNick.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
                            Platform.runLater(() -> {
                                        paneForNicks.getChildren().addAll(textNick);
                                    }
                            );


                            gameStart.setOnAction(ema -> {
                                try {
                                    out.writeUTF("start");
                                    if (questions.get(i) instanceof Test) {
                                        out.writeUTF("test");
                                    } else out.writeUTF("fill");

                                    stackPane.getChildren().removeAll(waitPlayer);
                                    stackPane.setStyle("-fx-background-color: white");
                                    design.setCenter(page(i));
                                    design.setBottom(info());
                                    stackPane.getChildren().addAll(design);
                                    design.setTop(new StackPane(timer()));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            });

                            for (int j = 0; i < questions.size(); j++) {
                                for (Socket socket : sockets) {
                                    DataOutputStream out1 = new DataOutputStream(socket.getOutputStream());
                                    String check = in.readUTF();
                                    if (check.equals(questions.get(i).getAnswer()) && !check.equals("Red") && !check.equals("Orange") && !check.equals("Blue") && !check.equals("Green")) {
                                        out1.writeUTF("Correct");
                                        pointRound = 1000 - ((30 - seconds) * 33);
                                    } else if (check.equals("Red")) {
                                        if (questions.get(i).getAnswer().equals("A")) {
                                            out1.writeUTF("Correct");
                                            pointRound = 1000 - ((30 - seconds) * 33);
                                        }
                                    } else if (check.equals("Orange")) {
                                        if (questions.get(i).getAnswer().equals("B")) {
                                            out1.writeUTF("Correct");
                                            pointRound = 1000 - ((30 - seconds) * 33);
                                        }
                                    } else if (check.equals("Blue")) {
                                        if (questions.get(i).getAnswer().equals("C")) {
                                            out1.writeUTF("Correct");
                                            pointRound = 1000 - ((30 - seconds) * 33);
                                        }
                                    } else if (check.equals("Green")) {
                                        if (questions.get(i).getAnswer().equals("D")) {
                                            out1.writeUTF("Correct");
                                            pointRound = 1000 - ((30 - seconds) * 33);
                                        }
                                    } else out1.writeUTF("Incorrect");
                                }
                            }
//                            nickname = in.readUTF();
//                            Text textNick = new Text(nickname);
//                            textNick.setFill(Color.WHITE);
//                            textNick.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
//                            Platform.runLater(() -> {
//                                        paneForNicks.getChildren().addAll(textNick);
//                                    }
//                            );

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }).start();

        Scene scene = new Scene(stackPane, 1200, 675);
        primaryStage.setScene(scene);
        primaryStage.setTitle("kahootcom.exe");
        primaryStage.show();
    }
}
