package com.example.harjoitustyo;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Luokka, joka toteuttaa mp3-soittimen käyttöliittymän ja toiminnan. Sisältää mp3-tiedostojen soittoon
 * vaadittavat metodit.
 * @author Aliisa Pikkarainen
 * @version 1.0
 */
public class Mp3Soitin extends Application {
    /**
     * Label-kenttä, johon asetetaan soitettavan mp3-tiedoston nimi
     */
    private Label nimiLabel;
    /**
     * Edistymispalkki, joka osoittaa paljon tiedostoa on soitettu
     */
    private ProgressBar edistymisPalkki;
    /**
     * Slider, jolla säädetään toiston äänenvoimakkuutta
     */
    private Slider volumeSaadin;
    /**
     * Nappi, jolla keskeytetään toisto tai jatketaan sitä
     */
    private Button playPauseNappi;
    /**
     * Mediasoitin-olio, joka soittaa valittua mp3-tiedostoa
     */
    private MediaPlayer mediaPlayer;
    /**
     * Merkkijono, joka kuvaa mp3-tiedoston kokonaiskestoa
     */
    private String kesto;

    /**
     * Asettaa mediasoittimen äänenvoimakkuuden volumesäätimen arvon mukaiseksi jos mediasoitin ei ole null
     * @param volumeSaadin Slider
     * @param mediaPlayer MediaPlayer
     */
    public void saadaVolume(Slider volumeSaadin, MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            double aanenvoimakkuus = volumeSaadin.getValue();
            mediaPlayer.setVolume(aanenvoimakkuus);
        }
    }

    /**
     * Katkaisee mediasoittimen toiston tai jatkaa sitä riippuen siitä soiko soitin vai ei
     */
    public void playPauseMetodi() {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            playPauseNappi.setText("Play");
        } else {
            mediaPlayer.play();
            playPauseNappi.setText("Pause");

        }
    }

    /**
     * Pysäyttää mediasoittimen jos se ei ole null ja on käynnissä
     */
    public void tarkistaSoikoSoitin() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }
    }

    /**
     * Avaa tiedostonvalitsimen ja palauttaa käyttäjän valitseman mp3-tiedoston
     * @return valittuTiedosto File
     */
    public File valitseTiedosto() {
        FileChooser tiedostonValitsin = new FileChooser();
        tiedostonValitsin.setTitle("Valitse mp3-tiedosto");
        tiedostonValitsin.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3"));
        return tiedostonValitsin.showOpenDialog(null);
    }

    /**
     * Asettaa valitun mp3-tiedoston nimen nimi-labeliin
     * @param nimiLabel Label
     * @param valittuTiedosto File
     */
    public void asetaLabel(Label nimiLabel, File valittuTiedosto) {
        nimiLabel.setText(valittuTiedosto.getName());
    }

    /**
     * Soittaa valitun mp3-tiedoston
     * @param valittuTiedosto File
     */
    public void soitaTiedosto(File valittuTiedosto) {
        String tiedosto = valittuTiedosto.toURI().toString();
        Media media = new Media(tiedosto);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    /**
     * Asettaa edistysmispalkkiin mp3-tiedoston toiston edistymisen
     * @param edistymisPalkki ProgressBar
     */
    public void asetaEdistyminen(ProgressBar edistymisPalkki) {
        // Edellä oleva koodinpätkä on suoraan stackoverflowsta käyttäjältä Slaw
        edistymisPalkki.progressProperty().bind(
                Bindings.createDoubleBinding(() -> {
                            Duration current = mediaPlayer.getCurrentTime();
                            Duration total = mediaPlayer.getCycleDuration();
                            return current.toMillis() / total.toMillis();
                        },
                        mediaPlayer.currentTimeProperty(),
                        mediaPlayer.cycleDurationProperty())
        );
    }

    /**
     * Palauttaa mp3-tiedoston keston minuutteina ja sekunteina formatoituna merkkijonona
     * @return kesto String
     */
    public String getKesto() {
        Duration pituus = mediaPlayer.getTotalDuration();
        double sekunnitYhteensa = pituus.toSeconds();
        double minuutit = sekunnitYhteensa / 60;
        double sekunnit = sekunnitYhteensa % 60;
        int minuutitInt = (int) Math.floor(minuutit);
        int sekunnitInt = (int) Math.floor(sekunnit);
        return kesto = String.format("%02d:%02d", minuutitInt, sekunnitInt);
    }

    /**
     * Leikkaa .mp3 päätteen mp3-tiedoston nimestä pois
     * @param valittuTiedosto FIle
     * @return tiedostoNimi String
     */
    public String formatTiedostoNimi(File valittuTiedosto) {
        String muokattavaNimi = valittuTiedosto.getName();
        String tiedostoNimi = muokattavaNimi.substring(0, muokattavaNimi.length() - 4);
        return tiedostoNimi;
    }

    /**
     * Kirjoittaa kappale-olion tiedot tiedostoon
     * @param tiedosto          tiedosto, johon olion tiedot tallennetaan
     * @param biisi             olio, jonka tiedot kirjoitetaan tiedostoon
     * @param jarjestysNumero   kappaleen järjestysnumero
     * @param tyyppi            kappaleen tiedostotyyppi
     * @param nimi              kapppaleen nimi
     * @param kesto             kappaleen kesto
     */
    public void kirjoitaBiisiTiedostoon(File tiedosto, Kappale biisi, AtomicInteger jarjestysNumero, String tyyppi, String nimi, String kesto) {
        try{
            PrintWriter kirjoitusTiedosto = new PrintWriter(tiedosto);
            kirjoitusTiedosto.println(biisi.getJarjestysNumero() + " " + biisi.getTiedostoTyyppi() + " " + biisi.getNimi() + " " + getKesto());
            kirjoitusTiedosto.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lukee kappaleen tiedot tiedostosta ja lisää ne ListView:iin
     * @param tiedosto          tiedosto, josta luetaan tiedot
     * @param historiaLista     ListView, johon tiedostosta luetut tiedot asetetaan
     */
    public void lueTiedosto (File tiedosto, ListView historiaLista) {
        try (Scanner lukija = new Scanner(tiedosto)) {
            while (lukija.hasNextLine()) {
                String rivi = lukija.nextLine();
                historiaLista.getItems().add(rivi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ohjelmaikkunan käynnistyksen ja toiminnallisuuden määrittely
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Color taustaVari = Color.rgb(2, 81, 97);
        Color tekstiVari = Color.rgb(0, 72, 87);
        Color ruutuVari = Color.rgb(120, 209, 227);

        Pane paneeli = new Pane();
        paneeli.setBackground(new Background(new BackgroundFill(taustaVari, CornerRadii.EMPTY, Insets.EMPTY)));
        Image icon = new Image(getClass().getResourceAsStream("/com/example/harjoitustyo/icon.png")); // Image by Freeimages.com
        stage.getIcons().add(icon);

        Rectangle nimiRuutu = new Rectangle(20, 20, 490, 40);
        nimiRuutu.setStroke(tekstiVari);
        nimiRuutu.setFill(ruutuVari);
        nimiRuutu.setArcHeight(30);
        nimiRuutu.setArcWidth(30);

        nimiLabel = new Label("Tervetuloa mp3-soittimeen!");
        nimiLabel.setLayoutX(30);
        nimiLabel.setLayoutY(27);
        nimiLabel.setFont(new Font("Trebuchet MS", 20));
        nimiLabel.setTextFill(tekstiVari);
        nimiLabel.setMaxWidth(480);
        nimiLabel.setTextOverrun(OverrunStyle.ELLIPSIS);

        Rectangle queRuutu = new Rectangle(20, 140,490, 230);
        queRuutu.setStroke(tekstiVari);
        queRuutu.setFill(ruutuVari);
        queRuutu.setArcHeight(30);
        queRuutu.setArcWidth(30);

        ListView historiaLista = new ListView<>();
        historiaLista.setPrefSize(480, 180);
        historiaLista.setLayoutX(25);
        historiaLista.setLayoutY(173);

        Label historiaLabel = new Label("Soittohistoria:");
        historiaLabel.setLayoutX(30);
        historiaLabel.setLayoutY(145);
        historiaLabel.setFont(new Font("Trebuchet MS", 20));
        historiaLabel.setTextFill(tekstiVari);

        edistymisPalkki = new ProgressBar();
        edistymisPalkki.setLayoutX(20);
        edistymisPalkki.setLayoutY(65);
        edistymisPalkki.setMinSize(490, 10);

        volumeSaadin = new Slider(0, 1, 0.5);
        volumeSaadin.setLayoutX(372);
        volumeSaadin.setLayoutY(87);

        volumeSaadin.valueProperty().addListener(e -> {
            saadaVolume(volumeSaadin, mediaPlayer);
        });

        Text volTeksti = new Text("vol");
        volTeksti.setLayoutX(355);
        volTeksti.setLayoutY(97);
        volTeksti.setFill(Color.WHITE);

        playPauseNappi = new Button("Pause");
        playPauseNappi.setLayoutX(215);
        playPauseNappi.setLayoutY(88);
        playPauseNappi.setMinSize(100, 15);
        playPauseNappi.setOnAction(event -> playPauseMetodi());


        File tiedosto = new File("kuunteluhistoria.txt");
        AtomicInteger jarjestysNumero = new AtomicInteger(1);

        Button avaaTiedosto = new Button("Valitse mp3-tiedosto");
        avaaTiedosto.setLayoutX(20);
        avaaTiedosto.setLayoutY(88);
        avaaTiedosto.setOnAction( event -> {
            File valittuTiedosto = valitseTiedosto();
            if (valittuTiedosto != null) {
                asetaLabel(nimiLabel, valittuTiedosto);
                tarkistaSoikoSoitin();
                soitaTiedosto(valittuTiedosto);
                saadaVolume(volumeSaadin, mediaPlayer);

                mediaPlayer.setOnReady(() -> {
                    asetaEdistyminen(edistymisPalkki);
                    String kesto = getKesto();
                    Kappale biisi = new Kappale(jarjestysNumero, formatTiedostoNimi(valittuTiedosto), kesto, "MP3");
                    kirjoitaBiisiTiedostoon(tiedosto, biisi, jarjestysNumero, biisi.getTiedostoTyyppi(), biisi.getNimi(), biisi.getKesto());
                    lueTiedosto(tiedosto, historiaLista);
                    jarjestysNumero.addAndGet(1);
                });
            } else {
            }
        });

        paneeli.getChildren().addAll(nimiRuutu, nimiLabel, queRuutu, edistymisPalkki, volumeSaadin, playPauseNappi, avaaTiedosto, volTeksti, historiaLabel, historiaLista);

        Scene ikkuna = new Scene(paneeli, 530, 400);
        stage.setTitle("MP3-Soitin");
        stage.setResizable(false);
        stage.setX(50);
        stage.setY(50);
        stage.setScene(ikkuna);
        stage.show();
    }

    /**
     * ohjelman käynnistävä metodi
     * @param args kytsuparametrejä ei käytetä
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

}




