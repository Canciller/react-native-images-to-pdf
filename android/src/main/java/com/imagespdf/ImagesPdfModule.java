package com.imagespdf;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

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

  @ReactMethod
  public void createPdf(ReadableMap options, Promise promise) {
    try {
      ReadableArray imagePaths = options.getArray("imagePaths");
      String outputDirectory = options.getString("outputDirectory");
      String outputFilename = options.getString("outputFilename");

      if (imagePaths.size() == 0) {
        throw new Exception("imagePaths is empty.");
      }

      PdfDocument pdfDocument = new PdfDocument();

      try {
        for (int i = 0; i < imagePaths.size(); ++i) {
          String imagePath = imagePaths.getString(i);
          Bitmap bitmap = getBitmapFromPathOrUri(imagePath);

          if (bitmap == null) {
            throw new Exception(imagePath + " cannot be decoded into a bitmap.");
          }

          PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
            .Builder(bitmap.getWidth(), bitmap.getHeight(), i + 1)
            .create();

          PdfDocument.Page page = pdfDocument.startPage(pageInfo);

          Canvas canvas = page.getCanvas();
          canvas.drawBitmap(bitmap, 0, 0, null);

          pdfDocument.finishPage(page);
        }
      } catch (Exception e) {
        Log.e("ImagesPdfModule", e.getLocalizedMessage(), e);
        promise.reject("PDF_PAGE_CREATE_ERROR", e.getLocalizedMessage(), e);
        pdfDocument.close();
        return;
      }

      Uri outputUri = null;

      try {
        outputUri = writePdfDocument(pdfDocument, outputDirectory, outputFilename);
      } catch (Exception e) {
        Log.e("ImagesPdfModule", e.getLocalizedMessage(), e);
        promise.reject("PDF_WRITE_ERROR", e.getLocalizedMessage(), e);
        pdfDocument.close();
        return;
      }

      pdfDocument.close();

      promise.resolve(outputUri.getPath());
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

  public Uri writePdfDocument(PdfDocument pdfDocument, String outputDirectory, String outputFilename) throws IOException {
    OutputStream outputStream = null;
    Uri outputUri = null;

    try {
      Uri uri = Uri.parse(outputDirectory);
      String mimeTypePdf = "application/pdf";

      String scheme = uri.getScheme();

      if (scheme != null && scheme.equals(ContentResolver.SCHEME_CONTENT)) {
        // TODO: Fix warnings: Method invocation may produce 'NullPointerException'.

        DocumentFile dirFile = DocumentFile
          .fromTreeUri(getReactApplicationContext().getApplicationContext(), uri);

        DocumentFile pdfFile = dirFile
          .createFile(mimeTypePdf, outputFilename);

        outputUri = pdfFile.getUri();

        outputStream = getReactApplicationContext()
          .getContentResolver()
          .openOutputStream(outputUri);
      } else if (scheme == null || scheme.equals(ContentResolver.SCHEME_FILE)) {
        outputUri = uri.buildUpon()
          .appendPath(outputFilename)
          .build();

        outputStream = new FileOutputStream(outputUri.getPath());
      } else {
        throw new UnsupportedOperationException("Unsupported scheme: " + uri.getScheme());
      }

      pdfDocument.writeTo(outputStream);
    } finally {
      if (outputStream != null) {
        outputStream.close();
      }
    }

    return outputUri;
  }

  public Bitmap getBitmapFromPathOrUri(String pathOrUri) throws IOException {
    Bitmap bitmap = null;
    InputStream inputStream = null;

    try {
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
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return bitmap;
  }
}
