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

  private Integer getInt(ReadableMap options, String key) {
    if(options.hasKey(key)) {
      return options.getInt(key);
    }

    return null;
  }

  private Page[] parsePages(ReadableArray pagesArray) {
    if (pagesArray == null) {
      throw new IllegalArgumentException("Invalid 'pages' argument. 'pages' cannot be null.");
    }

    Page[] parsedPages = new Page[pagesArray.size()];
    for (int i = 0; i < pagesArray.size(); i++) {
      ReadableMap pageMap = pagesArray.getMap(i);

      Page page = new Page();

      page.imagePath = pageMap.getString("imagePath");
      page.imageFit = parseImageFit(pageMap.getString("imageFit"));
      page.imageMaxWidth = getInt(pageMap, "imageMaxWidth");
      page.imageMaxHeight = getInt(pageMap, "imageMaxHeight");

      page.width = getInt(pageMap, "width");
      page.height = getInt(pageMap, "height");
      page.backgroundColor = getInt(pageMap, "backgroundColor");

      parsedPages[i] = page;
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

  public class Page {
    public String imagePath;
    public ImageFit imageFit;
    public Integer width;
    public Integer height;
    public Integer backgroundColor;
    public Integer imageMaxWidth;
    public Integer imageMaxHeight;
  }
}
