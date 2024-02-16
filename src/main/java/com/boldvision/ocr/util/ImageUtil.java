package com.boldvision.ocr.util;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;

import java.io.IOException;

public class ImageUtil {
    public byte[] compressImage(byte[] data){
        return null;
    }

    public static byte[] reduceNoise(byte[] imageBytes) throws IOException {
        Mat originalImage = convertBytesToMat(imageBytes);

        // Convert the image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(originalImage, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Apply GaussianBlur to reduce noise
        Mat blurredImage = new Mat();
        Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 0);

        // Convert back to BGR (if needed)
        Mat outputImage = new Mat();
        Imgproc.cvtColor(blurredImage, outputImage, Imgproc.COLOR_GRAY2BGR);

        return convertMatToBytes(outputImage);
    }
    private static Mat convertBytesToMat(byte[] imageBytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);

        return mat;
    }

    private static byte[] convertMatToBytes(Mat mat) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        return matOfByte.toArray();
    }



}
