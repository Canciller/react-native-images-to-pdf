package com.imagespdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

// TODO: ImagePosition

public class ImageScaling {
  public static Bitmap resize(Bitmap image, Integer maxWidth, Integer maxHeight) {
    int width = image.getWidth();
    int height = image.getHeight();

    int mWidth = maxWidth != null ? maxWidth : width;
    int mHeight = maxHeight != null ? maxHeight : height;

    int newWidth, newHeight;

    if(width > mWidth || height > mHeight) {
      float aspectRatio = (float) width / height;

      newWidth = mWidth;
      newHeight = Math.round(mWidth / aspectRatio);

      if (newHeight > mHeight) {
        newHeight = mHeight;
        newWidth = Math.round(mHeight * aspectRatio);
      }

      return Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
    }

    return Bitmap.createBitmap(image);
  }

  public static Bitmap scaleToFit(Bitmap image, Point containerSize, ImageFit fit) throws Exception {
    switch (fit) {
      case NONE:
        return scaleWithNone(image, containerSize);
      case CONTAIN:
        return scaleWithContain(image, containerSize);
      case COVER:
        return scaleWithCover(image, containerSize);
      case FILL:
        return scaleWithFill(image, containerSize);
      default:
        throw new Exception("Unknown scale fit: " + fit);
    }
  }

  private static Bitmap scaleWithNone(Bitmap bitmap, Point containerSize) {
    // Create container bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(containerSize.x, containerSize.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int scaledWidth = bitmap.getWidth();
    int scaledHeight = bitmap.getHeight();

    // Apply transformations.

    Matrix matrix = new Matrix();

    float translateX = (containerSize.x - scaledWidth) / 2f;
    float translateY = (containerSize.y - scaledHeight) / 2f;

    matrix.postTranslate(translateX, translateY);

    // Draw the bitmap.

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bitmap, matrix, paint);

    return newBitmap;
  }

  private static Bitmap scaleWithContain(Bitmap bitmap, Point containerSize) {
    // Create container bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(containerSize.x, containerSize.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int width = bitmap.getWidth();
    int height = bitmap.getHeight();

    float aspectRatio = (float) width / height;
    float targetAspectRatio = (float) containerSize.x / containerSize.y;

    int scaledWidth, scaledHeight;
    if (aspectRatio > targetAspectRatio) {
      scaledWidth = containerSize.x;
      scaledHeight = (int) (containerSize.x / aspectRatio);
    } else {
      scaledWidth = (int) (containerSize.y * aspectRatio);
      scaledHeight = containerSize.y;
    }

    // Apply transformations.

    Matrix matrix = new Matrix();

    float scaleX = (float) scaledWidth / width;
    float scaleY = (float) scaledHeight / height;

    matrix.postScale(scaleX, scaleY);

    float translateX = (containerSize.x - scaledWidth) / 2f;
    float translateY = (containerSize.y - scaledHeight) / 2f;

    matrix.postTranslate(translateX, translateY);

    // Draw the bitmap.

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bitmap, matrix, paint);

    return newBitmap;
  }

  private static Bitmap scaleWithCover(Bitmap bitmap, Point containerSize) {
    // Create container bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(containerSize.x, containerSize.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int width = bitmap.getWidth();
    int height = bitmap.getHeight();

    float aspectRatio = (float) width / height;
    float targetAspectRatio = (float) containerSize.x / containerSize.y;

    float scaleFactor;
    if (aspectRatio > targetAspectRatio) {
      scaleFactor = (float) containerSize.y / height;
    } else {
      scaleFactor = (float) containerSize.x / width;
    }

    int scaledWidth = Math.round(width * scaleFactor);
    int scaledHeight = Math.round(height * scaleFactor);

    // Apply transformations.

    Matrix matrix = new Matrix();

    matrix.postScale(scaleFactor, scaleFactor);

    float translateX = (containerSize.x - scaledWidth) / 2f;
    float translateY = (containerSize.y - scaledHeight) / 2f;

    matrix.postTranslate(translateX, translateY);

    // Draw the bitmap.

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bitmap, matrix, paint);

    return newBitmap;
  }

  private static Bitmap scaleWithFill(Bitmap bitmap, Point containerSize) {
    // Create container bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(containerSize.x, containerSize.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int width = bitmap.getWidth();
    int height = bitmap.getHeight();

    int scaledWidth = containerSize.x;
    int scaledHeight = containerSize.y;

    // Apply transformations.

    Matrix matrix = new Matrix();

    float scaleX = (float) scaledWidth / width;
    float scaleY = (float) scaledHeight / height;

    matrix.postScale(scaleX, scaleY);

    // Draw the bitmap.

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bitmap, matrix, paint);

    return newBitmap;
  }
}



