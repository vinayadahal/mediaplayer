package com.mediaplayer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

public class FXMLController implements Initializable {

//    String filename = "f:\\kf.avi";
    String filename = "E:\\Zaroorat.mp4";
    FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(new File(filename).getAbsolutePath());
    List<BufferedImage> frameList = new ArrayList();

    CanvasFrame canvas;

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        canvas = new CanvasFrame("JavaCV player");
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        Thread th = new Thread() {
            @Override
            public void run() {
                loadFrame();
            }
        };
        th.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        showLoadedFrame();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void loadFrame() {
        Frame captured_frame;
        try {
            frameGrabber.start();
            for (;;) {
                try {
                    captured_frame = frameGrabber.grab();
                    if (captured_frame.image == null) {
                        continue;
                    }
                    frameList.add(new Java2DFrameConverter().convert(captured_frame));
                    if (frameGrabber.getLengthInFrames() == frameList.size()) {
                        break;
                    }
                    if ((frameList.size() % 125) == 0) {
                        try {
                            Thread.sleep(4000);
                            showLoadedFrame();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (FrameGrabber.Exception e) {
                    System.out.println("Exception::: " + e);
                }
            }
            showLoadedFrame();
        } catch (FrameGrabber.Exception e) {
            System.out.println("Exception:::::: " + e);
        }
    }

    public void showLoadedFrame() {
        Thread th = new Thread() {
            @Override
            public void run() {
                int i = 0;
                for (;;) {
                    canvas.showImage(frameList.get(0)); // getting first element and showing it.
                    try {
                        frameList.remove(0);
                        clearRam();
                        this.sleep(40);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        th.start();
    }

    public void clearRam() {
        Thread th = new Thread() {
            @Override
            public void run() {
                System.gc();
            }
        };
        th.start();
    }
}
