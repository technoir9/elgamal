import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class Main extends Application {
    private static final String SIGNATURE_PATH = "results/signature";
    private static final String KEY_PATH = "results/key";

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        new File("results").mkdirs();

        primaryStage.setTitle("El Gamal - podpis cyfrowy");

        //Ustawianie parametrów Grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(5);

        Label firstLabel = new Label("Podaj ścieżkę do pliku do podpisania:");
        GridPane.setConstraints(firstLabel, 0, 0);

        TextField firstFilePath = new TextField();
        GridPane.setConstraints(firstFilePath, 1, 0);

        Label secondLabel = new Label("Podaj sciezke do pliku do sprawdzenia:");
        GridPane.setConstraints(secondLabel, 0, 1);

        TextField secondFilePath = new TextField();
        GridPane.setConstraints(secondFilePath, 1, 1);

        Label inputLabel = new Label("Podaj tekst do podpisania:");
        GridPane.setConstraints(inputLabel, 0, 2);

        TextField firstInput = new TextField();
        GridPane.setConstraints(firstInput, 1, 2);

        Label secondInputLabel = new Label("Podaj tekst do sprawdzenia:");
        GridPane.setConstraints(secondInputLabel, 0, 3);

        TextField secondInput = new TextField();
        GridPane.setConstraints(secondInput, 1, 3);

        //Przyciski
        Button generateSignButton = new Button("Podpisz");
        GridPane.setConstraints(generateSignButton, 3, 0);

        generateSignButton.setOnAction(e -> {
            String filePath = firstFilePath.getText();
            FileService fileService =new FileService();
            Signature signature =new Signature(fileService.readFile(filePath));
            signature.generateSignature();
            try {
                fileService.signatureToFile(SIGNATURE_PATH, signature.getS1(), signature.getS2());
                fileService.keyToFile(KEY_PATH, signature.getP(), signature.getG(), signature.getH());
            }   catch (IOException el) {
                el.printStackTrace();
            }
        });

        Button verifySignButton = new Button("Sprawdz plik");
        GridPane.setConstraints(verifySignButton, 3, 1);

        verifySignButton.setOnAction(e -> {
            FileService fileService = new FileService();
            String filePath = secondFilePath.getText();
            Signature signature = new Signature(fileService.readFile(filePath));
            String signatureFile = SIGNATURE_PATH;
            String keyFile = KEY_PATH;

            BigInteger key[] = fileService.readFile(keyFile,3);
            BigInteger signatureArray[] = fileService.readFile(signatureFile,2);
            BigInteger p = key[0];
            //System.out.println("P:" + p);
            BigInteger g = key[1];
            //System.out.println("g:" + g);
            BigInteger h = key[2];
            //System.out.println("h:" + h);
            BigInteger s1= signatureArray[0];
            //System.out.println("s1:" + s1);
            BigInteger s2 = signatureArray[1];
            //System.out.println("s2:" + s2);
            BigInteger H = signature.getHash();
            //System.out.println("H:" + H);
            SignatureVerification signatureVerification = new SignatureVerification(p,h,g,H,s1,s2);
            if (signatureVerification.verify()) FileService.checkBox("Pozytywny wynik weryfikacjy");
            else FileService.checkBox("Negatywny wynik weryfikacjy");
        });

        //Przyciski
        Button generateInputSignButton = new Button("Podpisz");
        GridPane.setConstraints(generateInputSignButton, 3, 2);

        generateInputSignButton.setOnAction(e -> {
            String input = firstInput.getText();
            FileService fileService =new FileService();
            Signature signature =new Signature(fileService.readString(input));
            signature.generateSignature();
            try {
                fileService.signatureToFile(SIGNATURE_PATH, signature.getS1(), signature.getS2());
                fileService.keyToFile(KEY_PATH, signature.getP(), signature.getG(), signature.getH());
            }   catch (IOException el) {
                el.printStackTrace();
            }
        });

        Button verifyInputSignButton = new Button("Sprawdz tekst");
        GridPane.setConstraints(verifyInputSignButton, 3, 3);

        verifyInputSignButton.setOnAction(e -> {
            FileService fileService = new FileService();
            String input = secondInput.getText();
            Signature signature = new Signature(fileService.readString(input));
            String signatureFile = SIGNATURE_PATH;
            String keyFile = KEY_PATH;

            BigInteger key[] = fileService.readFile(keyFile,3);
            BigInteger signatureArray[] = fileService.readFile(signatureFile,2);
            BigInteger p = key[0];
            //System.out.println("P:" + p);
            BigInteger g = key[1];
            //System.out.println("g:" + g);
            BigInteger h = key[2];
            //System.out.println("h:" + h);
            BigInteger s1= signatureArray[0];
            //System.out.println("s1:" + s1);
            BigInteger s2 = signatureArray[1];
            //System.out.println("s2:" + s2);
            BigInteger H = signature.getHash();
            //System.out.println("H:" + H);
            SignatureVerification signatureVerification = new SignatureVerification(p,h,g,H,s1,s2);
            if (signatureVerification.verify()) FileService.checkBox("Pozytywny wynik weryfikacjy");
            else FileService.checkBox("Negatywny wynik weryfikacjy");
        });

        //Dodawanie do Grid
        grid.getChildren().addAll(firstLabel, firstFilePath, secondLabel, secondFilePath, generateSignButton, verifySignButton,
                inputLabel, firstInput, secondInputLabel, secondInput, generateInputSignButton, verifyInputSignButton);


        Scene scene = new Scene(grid, 700, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
