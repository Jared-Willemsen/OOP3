package project.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import project.model.Webcam;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Controller {
    @FXML private Label webcamLabelLeft;
    @FXML private ComboBox<Webcam> webcamDropdownLeft;
    @FXML private MediaView mediaViewLeft;
    @FXML private TextField textFieldLeft;

    @FXML private Label webcamLabelRight;
    @FXML private ComboBox<Webcam> webcamDropdownRight;
    @FXML private MediaView mediaViewRight;
    @FXML private TextField textFieldRight;

    @FXML private Label distanceLabel;

    private MediaPlayer mediaPlayerLeft;
    private MediaPlayer mediaPlayerRight;

    private Thread mediaPlayerLeftThread;
    private Thread mediaPlayerRightThread;

    private ObservableList<Webcam> webcams;

    @FXML
    public void initialize() {
        webcams = FXCollections.observableList(new ArrayList<>());
        webcamDropdownLeft.setItems(webcams);
        webcamDropdownRight.setItems(webcams);
        addWebcam("Amsterdam", ZoneId.of("GMT+01:00"),52.22060, 4.53336,"https://cdn-004.whatsupcams.com/hls/hr_pula01.m3u8");
        addWebcam("New York",ZoneId.of("GMT-05:00"),40.43231 , -73.59571,"https://cdn-004.whatsupcams.com/hls/hr_pula01.m3u8");
        addWebcam("Canberra",ZoneId.of("GMT+11:00"),-35.18221, 149.07474,"https://cdn-004.whatsupcams.com/hls/hr_pula01.m3u8");
    }

    public void addWebcam(String place, ZoneId timeZone, double longitude, double latitude, String webcamURL){
        Webcam newWebcam = new Webcam(place, timeZone, longitude, latitude, webcamURL);
        webcams.add(newWebcam);
    }

    public void filterWebcamsLeft(){
        webcamDropdownLeft.setItems(webcams.stream()
                                            .filter(webcam -> webcam.getPlace().contains(textFieldLeft.getText()))
                                            .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }
    public void filterWebcamsRight(){
        webcamDropdownRight.setItems(webcams.stream()
                .filter(webcam -> webcam.getPlace().contains(textFieldRight.getText()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    public void startSelectedWebcamLeft() {
        Webcam selectedWebcam = webcamDropdownLeft.getSelectionModel().getSelectedItem();
        if (selectedWebcam == null) return; // Prevent null pointer exceptions
        if (mediaPlayerLeftThread != null) {mediaPlayerLeftThread.interrupt();}
        // Run media loading in a separate thread
        mediaPlayerLeftThread = new Thread(() -> {
            try {
                Media media = new Media(selectedWebcam.getWebcamURL());
                MediaPlayer newMediaPlayer = new MediaPlayer(media);

                // Update UI on the JavaFX thread
                Platform.runLater(() -> {
                    if (mediaPlayerLeft != null) {
                        mediaPlayerLeft.stop(); // Stop the previous video
                    }
                    mediaPlayerLeft = newMediaPlayer;
                    mediaViewLeft.setMediaPlayer(mediaPlayerLeft);
                    mediaPlayerLeft.setAutoPlay(true);
                    webcamLabelLeft.setText(selectedWebcam.getPlace() + " - " + selectedWebcam.getTime());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mediaPlayerLeftThread.start();
        updateWebcamDistance();
    }
    public void playSelectedWebcamLeft() {
        mediaPlayerLeft.play();
    }
    public void pauseSelectedWebcamLeft() {
        mediaPlayerLeft.pause();
    }

    public void startSelectedWebcamRight() {
        Webcam selectedWebcam = webcamDropdownRight.getSelectionModel().getSelectedItem();
        if (selectedWebcam == null) return; // Prevent null pointer exceptions

        if (mediaPlayerRightThread != null) {mediaPlayerRightThread.interrupt();}
        // Run media loading in a separate thread
        mediaPlayerRightThread = new Thread(() -> {
            try {
                Media media = new Media(selectedWebcam.getWebcamURL());
                MediaPlayer newMediaPlayer = new MediaPlayer(media);

                // Update UI on the JavaFX thread
                Platform.runLater(() -> {
                    if (mediaPlayerRight != null) {
                        mediaPlayerRight.stop(); // Stop the previous video
                    }
                    mediaPlayerRight = newMediaPlayer;
                    mediaViewRight.setMediaPlayer(mediaPlayerRight);
                    mediaPlayerRight.setAutoPlay(true);
                    webcamLabelRight.setText(selectedWebcam.getPlace() + " - " + selectedWebcam.getTime());
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mediaPlayerRightThread.start();
        updateWebcamDistance();
    }
    public void playSelectedWebcamRight() {
        mediaPlayerRight.play();
    }
    public void pauseSelectedWebcamRight() {
        mediaPlayerRight.pause();
    }

    private void updateWebcamDistance() {
        Webcam webcamLeft = webcamDropdownLeft.getSelectionModel().getSelectedItem();
        Webcam webcamRight = webcamDropdownRight.getSelectionModel().getSelectedItem();
        if (webcamLeft == null || webcamRight == null)return;

        double distance = Math.round(calculateDistance(webcamLeft.getLocation().getLatitude(), webcamLeft.getLocation().getLongitude(), webcamRight.getLocation().getLatitude(), webcamRight.getLocation().getLongitude()));
        distanceLabel.setText(Double.toString(distance) + " Km");
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);

        return Math.sqrt(x * x + y * y) * 6371;
    }
}
