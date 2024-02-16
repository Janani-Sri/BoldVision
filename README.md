# BoldVision
Spring Boot OCR Application to extract content from images.

## Table of Contents
- [Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Libraries](#libraries)
- [Installation](#installation)
- [Demo](#demo)

## Project Overview
The Optical Character Recognition (OCR) Web Application is designed to facilitate users in extracting content from images using Tesseract, an OCR engine. Users can sign up, log in, and upload images for processing. The application processes the images, extracts text content, and allows users to view their processed images along with the extracted content.

## Tech Stack

- Spring Boot
- Mustache JS
- Maven
- AJAX
- CSS
- Html

## Libraries

- Tesseract
- OpenCV & Boofcv (Incomplete code for bold content extraction)

## Installation

```bash
# Clone the repository
git clone https://github.com/Janani-Sri/BoldVision.git

# Navigate to the project directory
cd your-project

#Set Up JDK

# Install dependencies
#install dependencies from pom.xml using maven

#Install Tesseract
brew install tesseract

#Install opencv (for image processing - bold content extraction - incomlete code in this repo)
brew install opencv

#Build and Run project
```

## Demo

[Download Demo](src/main/resources/static/video/demo.mov)





