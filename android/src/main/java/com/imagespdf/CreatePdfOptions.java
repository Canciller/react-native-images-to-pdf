package com.imagespdf;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

public class CreatePdfOptions {
  public String outputPath;
  public Page[] pages;

  public CreatePdfOptions(ReadableMap options) {
    outputPath = getStringOrThrow(options, "outputPath");
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
      String imagePath = pageMap.getString("imagePath");
      Integer width = getInt(pageMap, "width");
      Integer height = getInt(pageMap, "height");
      Integer backgroundColor = getInt(pageMap, "backgroundColor");

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
    public Integer width;
    public Integer height;
    public Integer backgroundColor;

    public Page(String imagePath,
                ImageFit imageFit,
                Integer width,
                Integer height,
                Integer backgroundColor) {
      this.imagePath = imagePath;
      this.imageFit = imageFit;
      this.width = width;
      this.height = height;
      this.backgroundColor = backgroundColor;
    }
  }
}
