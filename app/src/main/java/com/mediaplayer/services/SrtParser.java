package com.mediaplayer.services;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SrtParser {

    public Map<String, String> startDialog = new HashMap();
    public Map<String, String> endDialog = new HashMap();

    public StringBuilder loadSrt(String FileLocation) {
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

    public void parse(StringBuilder text) {
        String[] subtitleArray = text.toString().trim().split("\n");
        for (int i = 0; i < subtitleArray.length; i++) {
            if (subtitleArray[i].contains("-->")) {
                String[] splittedTime = subtitleArray[i].split("-->");
                String startTime = splittedTime[0].substring(0, splittedTime[0].indexOf(",")).trim();
                String stopTime = splittedTime[1].substring(0, splittedTime[1].indexOf(",")).trim();
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
                    }
                    if (subtitleArray.length - 2 == i) {
                        break;
                    } else {
                        j++;
                    }
                    if (i + j >= subtitleArray.length) {
                        break;
                    }
                }
                startDialog.put(startTime, sb.toString().trim());
                endDialog.put(stopTime, "");
            }
        }
    }

    public String srtTimeFormatter(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = (duration / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public String showMap(String videoTime) {
        String subtitle;
        if (startDialog.get(videoTime) != null) {
            subtitle = startDialog.get(videoTime);
        } else {
            subtitle = endDialog.get(videoTime);
        }
        return subtitle;
    }


}
