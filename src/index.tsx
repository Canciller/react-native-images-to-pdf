import type { ColorValue, ProcessedColorValue } from 'react-native';
import { NativeModules, Platform, processColor } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-images-to-pdf' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ImagesPdf = NativeModules.ImagesPdf
  ? NativeModules.ImagesPdf
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export type ImageFit = 'none' | 'fill' | 'contain' | 'cover';

export type Page = {
  imagePath: string;
  imageFit?: ImageFit;
  imageMaxWidth?: number;
  imageMaxHeight?: number;
  width?: number;
  height?: number;
  backgroundColor?: ColorValue;
};

interface InternalPage extends Omit<Page, 'backgroundColor'> {
  backgroundColor?: ProcessedColorValue;
}

export type CreatePdfOptions =
  | {
      outputDirectory: string;
      outputFilename: string;
      pages: Array<Page | string>;
      imagePaths?: undefined;
    }
  | {
      outputDirectory: string;
      outputFilename: string;
      pages?: undefined;
      /**
       * @deprecated Use the `pages` property instead.
       */
      imagePaths: string[];
    };

export function createPdf(options: CreatePdfOptions): Promise<string> {
  const { pages, imagePaths, ...opts } = options;

  const internalPages = (imagePaths || pages || []).map<InternalPage>((e) => {
    if (typeof e === 'string') {
      return {
        imagePath: e,
      };
    }

    const { backgroundColor, ...page } = e;

    return {
      backgroundColor: processColor(backgroundColor) ?? undefined,
      ...page,
    };
  });

  return ImagesPdf.createPdf({
    ...opts,
    pages: internalPages,
  });
}

export function getDocumentsDirectory(): Promise<string> {
  return ImagesPdf.getDocumentsDirectory();
}
