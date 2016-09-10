package com.mediaplayer.services;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SrtParser {

    String subtitle;
    public int parsedArray = 0;


    public StringBuilder loadSrt(String FileLocation) {
        System.out.println("loading from file:::::: ");
        File file = new File(FileLocation);
        BufferedReader br;
        StringBuilder text = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Exception::::: " + ex);
        } catch (IOException ex) {
            System.out.println("Exception::::: " + ex);
        }
        return text;
    }

    public String parse(StringBuilder text, String videoTime) {
//        System.out.println("Video TIME:::: " + videoTime);
        String[] subtitleArray = text.toString().trim().split("\n");
        for (int i = this.parsedArray; i < subtitleArray.length; i++) {
//            CommonArgs.parsedAryNumber = i;
            System.out.println("parsed::: " + this.parsedArray);

            if (subtitleArray[i].contains(videoTime)) {
                String[] splittedTime = subtitleArray[i].split("-->");
                String startTime = splittedTime[0].substring(0, splittedTime[0].indexOf(","));
                String stopTime = splittedTime[1].substring(0, splittedTime[1].indexOf(","));

//                System.out.println("start time: " + startTime);
//                System.out.println("stop time: " + stopTime);

                if (startTime.trim().equals(videoTime)) {
                    int j = 1;
                    StringBuilder sb = new StringBuilder();
                    while (!subtitleArray[i + j].contains("-->")) {
                        if (subtitleArray[i + j].equals("")) {
                            break;
                        } else {
                            if (subtitleArray[(i + j) - 1].contains("\n") && !subtitleArray[(i + j) - 1].contains("-->")) {
                                sb.append(subtitleArray[i + j]);
                            } else {
                                sb.append(subtitleArray[i + j]);
                                sb.append("\n");
                            }
//                            System.out.println("Subtitle::::: " + subtitleArray[i + j]);
                        }
                        if (subtitleArray.length - 2 == i) {
//                            System.out.println("Breaking...." + i + j);
                            break;
                        } else {
                            j++;
                        }
                    }
                    subtitle = sb.toString();
//                    System.out.println("FINAL LINE:::: " + sb.toString());
                    this.parsedArray = i;
                    break;

                } else if (stopTime.trim().equals(videoTime)) {
//                    System.out.println("FOUND END TIME::::: nothing to show");
                    subtitle = null;
                    this.parsedArray = i;
                } else {
                    break;
                }
            }
            System.out.println("Start Loop:::: " + this.parsedArray + " total loop---> " + i);
        }
        return subtitle;
    }

    public int milliSec(String time) {
        String[] SplittedTime = time.split(":");
        int ms = Integer.parseInt(SplittedTime[2]) * 1000;
        ms = ms + (Integer.parseInt(SplittedTime[1]) * 60) * 1000;
        ms = ms + ((Integer.parseInt(SplittedTime[0]) * 60) * 60) * 1000;
        return ms;
    }

    public String srtTimeFormatter(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = (duration / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
