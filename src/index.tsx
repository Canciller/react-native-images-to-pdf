import { NativeModules, Platform } from 'react-native';

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
  width?: number;
  height?: number;
};

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

  const mappedPages = (imagePaths || pages || []).map<Page>((e) => {
    if (typeof e === 'string') {
      return {
        imagePath: e,
      };
    }

    return e;
  });

  return ImagesPdf.createPdf({
    ...opts,
    pages: mappedPages,
  });
}

export function getDocumentsDirectory(): Promise<string> {
  return ImagesPdf.getDocumentsDirectory();
}
