package com.imagespdf;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

public class CreatePdfOptions {
  public String outputDirectory;
  public String outputFilename;
  public Page[] pages;

  public CreatePdfOptions(ReadableMap options) {
    outputDirectory = getStringOrThrow(options, "outputDirectory");
    outputFilename = getStringOrThrow(options, "outputFilename");
    pages = parsePages(getArrayOrThrow(options, "pages"));
  }

  private String getStringOrThrow(ReadableMap options, String key) {
    if (!options.hasKey(key)) {
      throw new IllegalArgumentException("Required option '" + key + "' not found.");
    }
    return options.getString(key);
  }

  private ReadableArray getArrayOrThrow(ReadableMap options, String key) {
    if (!options.hasKey(key)) {
      throw new IllegalArgumentException("Required option '" + key + "' not found.");
    }
    return options.getArray(key);
  }

  private Page[] parsePages(ReadableArray pagesArray) {
    if (pagesArray == null) {
      throw new IllegalArgumentException("Invalid 'pages' argument. 'pages' cannot be null.");
    }

    Page[] parsedPages = new Page[pagesArray.size()];
    for (int i = 0; i < pagesArray.size(); i++) {
      ReadableMap pageMap = pagesArray.getMap(i);
      String imagePath = pageMap.getString("imagePath");
      double width = pageMap.getDouble("width");
      double height = pageMap.getDouble("height");
      int backgroundColor = pageMap.getInt("backgroundColor");

      ImageFit imageFit = parseImageFit(pageMap.getString("imageFit"));

      parsedPages[i] = new Page(imagePath, imageFit, width, height, backgroundColor);
    }

    return parsedPages;
  }

  private ImageFit parseImageFit(String imageFitValue) {
    try {
      return imageFitValue == null ? ImageFit.NONE : ImageFit.valueOf(imageFitValue.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid 'imageFit' value: " + imageFitValue);
    }
  }

  public static class Page {
    public String imagePath;
    public ImageFit imageFit;
    public Double width;
    public Double height;
    public Integer backgroundColor;

    public Page(String imagePath,
                ImageFit imageFit,
                double width,
                double height,
                int backgroundColor) {
      this.imagePath = imagePath;
      this.imageFit = imageFit;
      this.width = width;
      this.height = height;
      this.backgroundColor = backgroundColor;
    }
  }
}
