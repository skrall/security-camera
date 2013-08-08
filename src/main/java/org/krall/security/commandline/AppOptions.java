package org.krall.security.commandline;

import ch.qos.logback.classic.Level;
import org.kohsuke.args4j.Option;

import java.io.File;

public class AppOptions {

    private static AppOptions appOptions;

    @Option(name = "--width", aliases = {"-w"}, depends = "--height", usage = "Width of the image captured")
    int imageWidth = 800;

    @Option(name = "--height", aliases = {"-h"}, depends = "--width", usage = "Height of the image captured")
    int imageHeight = 600;

    @Option(name = "--inputDirectory", aliases = {"-i"}, usage = "Directory to read for image input.")
    File inputDirectory = new File("/home/pi/images/input");

    @Option(name = "--outputDirectory", aliases = {"-o"}, usage = "Directory to read for image input.")
    File outputDirectory = new File("/home/pi/images/output");

    @Option(name = "--verticalRegions", aliases = {"-vr"}, usage = "Number of vertical Regions")
    int verticalRegions = 8;

    @Option(name = "--horizontalRegions", aliases = {"-hr"}, usage = "Number of horizontal Regions")
    int horizontalRegions = 6;

    @Option(name = "--sensitivity", aliases = {"-s"}, usage = "Sensitivity level for motion detection")
    int sensitivity = 5;

    @Option(name = "--stabilizer", aliases = {"-st"}, usage = "Stabilizer level for motion detection")
    int stabilizer = 10;

    @Option(name = "--loglevel", aliases = {"-l"}, usage = "Log level", handler = LogLevelHandler.class)
    Level logLevel = Level.INFO;

    @Option(name = "--useTimeLapse", aliases = {"-t"}, usage = "Use time lapse feature of raspistill")
    boolean useTimeLapse = false;

    @Option(name = "--noWriteDifferences", aliases = {"-n"}, usage = "Don't write differences to disk")
    boolean noWriteDifferences = false;

    @Option(name = "--noDrawDifferenceMarks", aliases = {"-a"}, usage = "Don't modify the images, " +
                                                                        "marking what area on the image changed")
    boolean noDrawDifferenceMarks = false;

    @Option(name = "--millisBetweenCapture", aliases = {"-m"}, usage = "Milliseconds between captures when in time " +
                                                                       "lapse mode.")
    int millisBetweenCapture = 1000;


    static {
        appOptions = new AppOptions();
    }

    private AppOptions() {
    }

    public static AppOptions getInstance() {
        return appOptions;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public File getInputDirectory() {
        return inputDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public int getVerticalRegions() {
        return verticalRegions;
    }

    public int getHorizontalRegions() {
        return horizontalRegions;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public int getStabilizer() {
        return stabilizer;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public boolean isUseTimeLapse() {
        return useTimeLapse;
    }

    public boolean isNoWriteDifferences() {
        return noWriteDifferences;
    }

    public boolean isNoDrawDifferenceMarks() {
        return noDrawDifferenceMarks;
    }

    public int getMillisBetweenCapture() {
        return millisBetweenCapture;
    }
}
