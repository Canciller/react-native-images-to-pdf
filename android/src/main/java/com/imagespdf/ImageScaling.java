package com.imagespdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

// TODO: ImagePosition

public class ImageScaling {
  public static Bitmap scale(Bitmap image, Point size, String fit) throws Exception {
    String imageFit = fit == null ? "none" : fit;

    switch (imageFit) {
      case "none":
        return scaleWithNone(image, size);
      case "contain":
        return scaleWithContain(image, size);
      case "cover":
        return scaleWithCover(image, size);
      case "fill":
        return scaleWithFill(image, size);
      default:
        throw new Exception("Unknown scale fit: " + fit);
    }
  }

  private static Bitmap scaleWithNone(Bitmap bitmap, Point size) {
    // Create background bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int scaledWidth = bitmap.getWidth();
    int scaledHeight = bitmap.getHeight();

    // Apply transformations.

    Matrix matrix = new Matrix();

    float translateX = (size.x - scaledWidth) / 2f;
    float translateY = (size.y - scaledHeight) / 2f;

    matrix.postTranslate(translateX, translateY);

    // Draw the bitmap.

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bitmap, matrix, paint);

    return newBitmap;
  }

  private static Bitmap scaleWithContain(Bitmap bitmap, Point size) {
    // Create background bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int width = bitmap.getWidth();
    int height = bitmap.getHeight();

    float aspectRatio = (float) width / height;
    float targetAspectRatio = (float) size.x / size.y;

    int scaledWidth, scaledHeight;
    if (aspectRatio > targetAspectRatio) {
      scaledWidth = size.x;
      scaledHeight = (int) (size.x / aspectRatio);
    } else {
      scaledWidth = (int) (size.y * aspectRatio);
      scaledHeight = size.y;
    }

    // Apply transformations.

    Matrix matrix = new Matrix();

    float scaleX = (float) scaledWidth / width;
    float scaleY = (float) scaledHeight / height;

    matrix.postScale(scaleX, scaleY);

    float translateX = (size.x - scaledWidth) / 2f;
    float translateY = (size.y - scaledHeight) / 2f;

    matrix.postTranslate(translateX, translateY);

    // Draw the bitmap.

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bitmap, matrix, paint);

    return newBitmap;
  }

  private static Bitmap scaleWithCover(Bitmap bitmap, Point size) {
    // Create background bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int width = bitmap.getWidth();
    int height = bitmap.getHeight();

    float aspectRatio = (float) width / height;
    float targetAspectRatio = (float) size.x / size.y;

    float scaleFactor;
    if (aspectRatio > targetAspectRatio) {
      scaleFactor = (float) size.y / height;
    } else {
      scaleFactor = (float) size.x / width;
    }

    int scaledWidth = Math.round(width * scaleFactor);
    int scaledHeight = Math.round(height * scaleFactor);

    // Apply transformations.

    Matrix matrix = new Matrix();

    matrix.postScale(scaleFactor, scaleFactor);

    float translateX = (size.x - scaledWidth) / 2f;
    float translateY = (size.y - scaledHeight) / 2f;

    matrix.postTranslate(translateX, translateY);

    // Draw the bitmap.

    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

    canvas.drawBitmap(bitmap, matrix, paint);

    return newBitmap;
  }

  private static Bitmap scaleWithFill(Bitmap bitmap, Point size) {
    // Create background bitmap.

    Bitmap newBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);

    // Calculate new width and height.

    int width = bitmap.getWidth();
    int height = bitmap.getHeight();

    int scaledWidth = size.x;
    int scaledHeight = size.y;

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



