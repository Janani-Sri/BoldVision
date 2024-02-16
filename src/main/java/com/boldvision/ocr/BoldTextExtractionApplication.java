package com.boldvision.ocr;


import boofcv.core.image.ConvertImage;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.GrayU8;
import georegression.struct.shapes.Rectangle2D_I32;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import georegression.struct.point.Point2D_I32;
import boofcv.io.image.UtilImageIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static boofcv.alg.filter.binary.BinaryImageOps.erode8;
import static org.bytedeco.opencv.global.opencv_imgproc.findContours;

@SpringBootApplication
public class BoldTextExtractionApplication {

    public static void main(String[] args) throws IOException {
        System.setProperty("jna.library.path", "/opt/local/lib");
        SpringApplication.run(BoldVisionApplication.class, args);


        BufferedImage bufferedImage = ImageIO.read(new File("/Users/janani-11495/Janu/per/SDE/Vitraya/Ass_1/BoldVision/src/main/resources/static/images/sampleOCRImage.png"));

        GrayU8 gray = ConvertBufferedImage.convertFrom(bufferedImage, (GrayU8) null);
        GrayU8 binary = new GrayU8(gray.width, gray.height);
        ThresholdImageOps.threshold(gray, binary, 128, false);

        // Erode the binary image
        GrayU8 erodedImage = new GrayU8(binary.width, binary.height);
        BinaryImageOps.erode8(binary, 1, erodedImage);

        List<Contour> contours = BinaryImageOps.contour(binary, ConnectRule.EIGHT, null);
        List<Contour> boldContours = new ArrayList<>();
        for (Contour contour : contours) {
            // Calculate the area of the contour
            double area = calculateContourArea(contour.external);
            // Filter contours based on area (adjust the threshold as needed)
            if (area > 100) {
                boldContours.add(contour);
            }
        }

        List<Object> boldCharacters = new ArrayList<>();
        for (Contour contour : boldContours) {
            // Get the bounding box of the contour
            Rectangle2D_I32 boundingBox = calculateBoundingBox(contour.external);
            Object character = bufferedImage.getSubimage(
                    boundingBox.x0, boundingBox.y0, boundingBox.x1, boundingBox.y1);
            boldCharacters.add(character);
        }
        for (int i = 0; i < boldCharacters.size(); i++) {
            ImageIO.write(bufferedImage, "png", new File("/Users/janani-11495/Janu/per/SDE/Vitraya/Ass_1/BoldVision/src/main/resources/static/images/"+"bold_character_" + i + ".png"));
        }

// Save the result image
        ImageIO.write(ConvertBufferedImage.convertTo(erodedImage, (BufferedImage)null), "png",
                new File("/Users/janani-11495/Janu/per/SDE/Vitraya/Ass_1/BoldVision/src/main/resources/static/images/eroded_image.png"));

        System.out.println("Image output updated");
    }
    private static double calculateContourArea(List<Point2D_I32> contour) {
        // Calculate the area of the contour using the shoelace formula
        double area = 0;
        int n = contour.size();

        for (int i = 0; i < n; i++) {
            Point2D_I32 p1 = contour.get(i);
            Point2D_I32 p2 = contour.get((i + 1) % n);
            area += (p1.x * p2.y - p2.x * p1.y);
        }

        return Math.abs(area) / 2.0;
    }

    private static Rectangle2D_I32 calculateBoundingBox(List<Point2D_I32> contour) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Point2D_I32 point : contour) {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        return new Rectangle2D_I32(minX, minY, maxX - minX, maxY - minY);
    }
}
