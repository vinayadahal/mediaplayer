package com.mediaplayer.components;


public class SeekBarVisibility {

    public static void showVolumeSeekbar() {
        new PopUpDialog().showVolumeDialog();
    }

    public static void showBrightnessSeekBar() {
        new PopUpDialog().showBrightnessDialog();
    }

}
