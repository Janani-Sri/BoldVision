package com.boldvision.ocr;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

@SpringBootApplication
public class BoldVisionApplication {
//	static {
//		// Set java.library.path to the path containing libtesseract.dylib
//		System.setProperty("java.library.path", "/usr/local/lib");
//
//		// Load the Tess4J library
//		System.loadLibrary("tesseract");
//	}


	public static void main(String[] args) throws IOException {
		System.setProperty("jna.library.path", "/opt/local/lib");
//		System.setProperty("java.library.path","/opt/local/libexec/opencv4/java/jni/libopencv_java480.dylib");
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
//			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.setProperty("jna.library.path", "/opt/local/lib");

		SpringApplication.run(BoldVisionApplication.class, args);

		BufferedImage bufferedImage = ImageIO.read(new File("/Users/janani-11495/Janu/per/SDE/Vitraya/Ass_1/BoldVision/src/main/resources/static/images/sampleOCRImage.png"));
		GrayU8 inputImage = ConvertBufferedImage.convertFrom(bufferedImage, (GrayU8) null);

		// Apply Gaussian blur for noise reduction
		GBlurImageOps.gaussian(inputImage, inputImage, -1, 2, null);

		// Apply global thresholding to create a binary image
		GrayU8 binaryImage = new GrayU8(inputImage.width, inputImage.height);
		ThresholdImageOps.threshold(inputImage, binaryImage, (int) GThresholdImageOps.computeOtsu(inputImage, 0, 255), true);

		// Extract bold content (you may need to adjust the parameters)
		// For example, you can use connected component analysis to find and filter regions with bold text

		// Save or process the resulting binary image as needed
		BufferedImage outputBufferedImage = ConvertBufferedImage.convertTo(binaryImage, (BufferedImage) null);
		ImageIO.write(outputBufferedImage, "png", new File("/Users/janani-11495/Janu/per/SDE/Vitraya/Ass_1/BoldVision/src/main/resources/static/images/image_filtered.png"));

		System.out.println("Bold text extracted!");
	}

}
