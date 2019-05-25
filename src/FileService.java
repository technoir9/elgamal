import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

public class FileService {

    public byte[] readFile(String pathFile) {
        byte[] evr;
        String error;

        File file = new File(pathFile);
        byte[] fileByte = new byte[(int) file.length()];
        FileInputStream inFile;

        try {
            inFile = new FileInputStream(file);
            inFile.read(fileByte);

        } catch (FileNotFoundException var5) {
            error = "Błąd";
            evr = error.getBytes();
            return evr;
        } catch (IOException var6) {
            error = "Zła ścieżka";
            evr = error.getBytes();
            return evr;
        }

        return fileByte;
    }

    public void keyToFile(String pathFile, BigInteger p, BigInteger g, BigInteger h) throws IOException {
        try (PrintWriter keyFile = new PrintWriter(pathFile)) {
            keyFile.println(p);
            keyFile.println(g);
            keyFile.println(h);
            //keyFile.println(H);
        }catch (FileNotFoundException e){ System.out.println("Nie ma takiego pliku");}
    }

    public void signatureToFile(String pathFile, BigInteger s1, BigInteger s2){
        try (PrintWriter signatureFile = new PrintWriter(pathFile)) {
            signatureFile.println(s1);
            signatureFile.println(s2);
        }catch (FileNotFoundException e){ System.out.println("Nie ma takiego pliku");}
    }

    public BigInteger[] odczytaj(String path, int size){
        BigInteger tab[] = new BigInteger[size];
        Scanner in = null;
        try{
            in = new Scanner(new File(path));
        }catch (FileNotFoundException e){ System.out.println("Plik nie istnieje");}
        int i=0;
        while(in.hasNextBigInteger()){
            tab[i]=in.nextBigInteger();
            i++;
        }
        return tab;
    }

    public static String pathBox(String message){
        Stage alert = new Stage();
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Wybierz swoją drogę");
        alert.setMinWidth(250);
        final String[] path = {""};

        Label label = new Label(message);
        TextField field = new TextField();


        Button close = new Button("OK");
        close.setOnAction(e -> {
            path[0] = field.getText();
            alert.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, field, close);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        alert.setScene(scene);
        alert.showAndWait();

        return path[0];

    }
    public static void checkBox(String message){
        Stage alert = new Stage();
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Sprawdzanie");
        alert.setMinWidth(250);

        Label label = new Label(message);

        Button close = new Button("EXIT");
        close.setOnAction(e -> {
            alert.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        alert.setScene(scene);
        alert.showAndWait();
    }
}