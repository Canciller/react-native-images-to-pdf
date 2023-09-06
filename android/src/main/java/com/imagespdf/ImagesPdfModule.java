package com.imagespdf;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.google.common.io.BaseEncoding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@ReactModule(name = ImagesPdfModule.NAME)
public class ImagesPdfModule extends ReactContextBaseJavaModule {
  public static final String NAME = "ImagesPdf";

  public ImagesPdfModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  static final String MIME_TYPE_PDF = "application/pdf";

  @ReactMethod
  public void createPdf(ReadableMap optionsMap, Promise promise) {
    try {
      CreatePdfOptions options = new CreatePdfOptions(optionsMap);

      String outputPath = options.outputPath;
      CreatePdfOptions.Page[] pages = options.pages;

      if (pages.length == 0) {
        throw new Exception("No images provided.");
      }

      PdfDocument pdfDocument = new PdfDocument();

      try {
        for (int i = 0; i < pages.length; ++i) {
          CreatePdfOptions.Page config = pages[i];
          String imagePath = config.imagePath;

          Bitmap image = getBitmapFromPathOrUri(imagePath);

          if (image == null) {
            throw new Exception(imagePath + " cannot be decoded into a bitmap.");
          }

          Integer pageWidth = config.width;
          Integer width = pageWidth != null ? pageWidth : image.getWidth();

          Integer pageHeight = config.height;
          Integer height = pageHeight != null ? pageHeight : image.getHeight();

          PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
            .Builder(width, height, i + 1)
            .create();

          PdfDocument.Page page = pdfDocument.startPage(pageInfo);

          Bitmap scaledImage = image;

          if (!width.equals(image.getWidth()) || !height.equals(image.getHeight())) {
            ImageFit imageFit = config.imageFit;
            Point size = new Point(width, height);

            scaledImage = ImageScaling.scale(image, size, imageFit);
          }

          Canvas canvas = page.getCanvas();
          if (config.backgroundColor != null) {
            canvas.drawColor(config.backgroundColor);
          }
          canvas.drawBitmap(scaledImage, 0, 0, null);

          pdfDocument.finishPage(page);
        }
      } catch (Exception e) {
        Log.e("ImagesPdfModule", e.getLocalizedMessage(), e);
        promise.reject("PDF_PAGE_CREATE_ERROR", e.getLocalizedMessage(), e);
        pdfDocument.close();
        return;
      }

      String writtenOutputPath = null;

      try {
        writtenOutputPath = writePdfDocument(pdfDocument, outputPath);
      } catch (Exception e) {
        Log.e("ImagesPdfModule", e.getLocalizedMessage(), e);
        promise.reject("PDF_WRITE_ERROR", e.getLocalizedMessage(), e);
        pdfDocument.close();
        return;
      }

      pdfDocument.close();

      promise.resolve(writtenOutputPath);
    } catch (Exception e) {
      Log.e("ImagesPdfModule", e.getLocalizedMessage(), e);

      promise.reject("PDF_CREATE_ERROR", e.getLocalizedMessage(), e);
    }
  }

  @ReactMethod
  public void getDocumentsDirectory(Promise promise) {
    String docsDir = getReactApplicationContext().getExternalFilesDir(null).getAbsolutePath();
    promise.resolve(docsDir);
  }

  String writePdfDocument(PdfDocument pdfDocument, String outputPath) throws IOException {
    OutputStream outputStream = null;
    Uri outputUri = null;
    String writtenOutputPath = null;

    try {
      outputUri = Uri.parse(outputPath);
      writtenOutputPath = outputUri.getPath();

      String scheme = outputUri.getScheme();

      if (scheme == null || scheme.equals(ContentResolver.SCHEME_FILE)) {
        outputStream = new FileOutputStream(writtenOutputPath);
      } else {
        throw new UnsupportedOperationException("Unsupported scheme: " + scheme);
      }

      pdfDocument.writeTo(outputStream);
    } finally {
      if (outputStream != null) {
        outputStream.close();
      }
    }

    return writtenOutputPath;
  }

  Bitmap getBitmapFromPathOrUri(String pathOrUri) throws IOException {
    Bitmap bitmap = null;
    InputStream inputStream = null;

    try {
      byte[] base64Decoded = null;

      try {
        String base64Str = pathOrUri.replaceFirst("^data:image/[a-z]+;base64,", "");
        base64Decoded = BaseEncoding.base64().decode(base64Str);
      } catch (IllegalArgumentException ignored) {
      }

      if (base64Decoded != null) {
        bitmap = BitmapFactory.decodeByteArray(base64Decoded, 0, base64Decoded.length);
      } else {
        Uri uri = Uri.parse(pathOrUri);

        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals(ContentResolver.SCHEME_CONTENT)) {
          ContentResolver contentResolver = getReactApplicationContext()
            .getContentResolver();

          inputStream = contentResolver
            .openInputStream(uri);
        } else if (scheme == null || scheme.equals(ContentResolver.SCHEME_FILE)) {
          inputStream = new FileInputStream(uri.getPath());
        } else {
          throw new UnsupportedOperationException("Unsupported scheme: " + uri.getScheme());
        }

        if (inputStream != null) {
          bitmap = BitmapFactory
            .decodeStream(inputStream);
        }
      }
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return bitmap;
  }
}
