package com.boldvision.ocr;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoldTextOpenCV {
    public static void main(String[] args) {
        // Load the image
        String inputImagePath = "path/to/your/image.jpg";
        Mat originalImage = Imgcodecs.imread(inputImagePath);

        // Convert the image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(originalImage, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Apply adaptive thresholding to create a binary image
        Mat binaryImage = new Mat();
        Imgproc.adaptiveThreshold(
                grayImage, binaryImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 4);

        // Find contours in the binary image
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Create a mask for bold text regions
        Mat mask = Mat.zeros(binaryImage.size(), CvType.CV_8UC1);
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);

            // Adjust the threshold for bounding box width to consider as bold text
            if (rect.width > 30) {
                Imgproc.drawContours(mask, Collections.singletonList(contour), -1, new Scalar(255), -1);
            }
        }

        // Apply the mask to the original image
        Mat resultImage = new Mat();
        originalImage.copyTo(resultImage, mask);

        // Save the result image
        String outputImagePath = "path/to/your/bold_text_image.jpg";
        Imgcodecs.imwrite(outputImagePath, resultImage);
    }
}
