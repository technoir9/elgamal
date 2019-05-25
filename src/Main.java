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

        Label nazwaPlik = new Label("Podaj ścieżkę do pliku do podpisania:");
        GridPane.setConstraints(nazwaPlik, 0, 0);

        TextField tekstPlik = new TextField();
        GridPane.setConstraints(tekstPlik, 1, 0);

        Label nazwaZaszyfr = new Label("Podaj sciezke do pliku do sprawdzenia:");
        GridPane.setConstraints(nazwaZaszyfr, 0, 1);

        TextField tekstZaszyfr = new TextField();
        GridPane.setConstraints(tekstZaszyfr, 1, 1);

        //Przyciski
        Button button_szyfr = new Button("Podpisz");
        GridPane.setConstraints(button_szyfr, 3, 0);

        button_szyfr.setOnAction(e -> {
            String filePath = tekstPlik.getText();
            FileService fileService =new FileService();
            Signature signature =new Signature(fileService.readFile(filePath));
            signature.generateSignature();
            try {
//                fileService.podpisDoPliku(FileService.pathBox("Sciezka do zapisu podpisu"), signature.getS1(), signature.getS2());
//                fileService.kluczykDoPliku(FileService.pathBox("Sciezka do zapisu klucza"), signature.getP(), signature.getG(), signature.getH());
                fileService.podpisDoPliku(SIGNATURE_PATH, signature.getS1(), signature.getS2());
                fileService.kluczykDoPliku(KEY_PATH, signature.getP(), signature.getG(), signature.getH());
            }   catch (IOException el) {
                el.printStackTrace();
            }
        });

        Button button_szyfrplik = new Button("Sprawdz plik");
        GridPane.setConstraints(button_szyfrplik, 3, 1);

        button_szyfrplik.setOnAction(e -> {
            FileService fileService = new FileService();
            String fileTekst = tekstZaszyfr.getText();
            Signature signature = new Signature(fileService.readFile(fileTekst));
//            String filePodpis = FileService.pathBox("Sciezka do odczytu pliku z podpisem");
//            String fileKlucz = FileService.pathBox("Sciezka do odczytu pliku z kluczem");
            String filePodpis = SIGNATURE_PATH;
            String fileKlucz = KEY_PATH;

            BigInteger klucz[] = fileService.odczytaj(fileKlucz,3);
            BigInteger podpisWartosc[] = fileService.odczytaj(filePodpis,2);
            BigInteger p = klucz[0];
            //System.out.println("P:" + p);
            BigInteger g = klucz[1];
            //System.out.println("g:" + g);
            BigInteger h = klucz[2];
            //System.out.println("h:" + h);
            BigInteger s1= podpisWartosc[0];
            //System.out.println("s1:" + s1);
            BigInteger s2 = podpisWartosc[1];
            //System.out.println("s2:" + s2);
            BigInteger H = signature.getHash();
            //System.out.println("H:" + H);
            SignatureVerification signatureVerification = new SignatureVerification(p,h,g,H,s1,s2);
            if (signatureVerification.verify()) FileService.checkBox("Signature sie zgadza");
            else FileService.checkBox("Signature błędny");
        });

        //Dodawanie do Grid
        grid.getChildren().addAll(nazwaPlik, tekstPlik, nazwaZaszyfr, tekstZaszyfr, button_szyfr, button_szyfrplik);


        Scene scene = new Scene(grid, 500, 90);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
