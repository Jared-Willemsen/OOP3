package project.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Webcam{
    private String place;
    private final ZoneId timeZone;
    private ZonedDateTime time;
    private Location location;
    private String webcamURL;


    public Webcam(String place, ZoneId timeZone, double longitude, double latitude, String webcamURL) {
        this.place = place;
        this.timeZone = timeZone;
        this.location = new Location(longitude, latitude);
        this.webcamURL = webcamURL;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    this.time = ZonedDateTime.now(this.timeZone);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeline.play(); // Start the timeline
    }

    public String getPlace() {return place;}
    public void setPlace(String place) {this.place = place;}

    public String getTime() {return String.format("%d:%d", time.getHour(), time.getMinute());}
    public void setTime(ZonedDateTime time) {this.time = time;}

    public Location getLocation() {return location;}
    public void setLocation(Location location) {this.location = location;}

    public String getWebcamURL() {return webcamURL;}
    public void setWebcamURL(String webcamURL) {this.webcamURL = webcamURL;}

    @Override
    public String toString() {
        return this.place;
    }
}
