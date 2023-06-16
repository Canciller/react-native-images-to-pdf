import { NativeModules, Platform } from 'react-native';
const LINKING_ERROR = `The package 'react-native-images-to-pdf' doesn't seem to be linked. Make sure: \n\n` + Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
const ImagesPdf = NativeModules.ImagesPdf ? NativeModules.ImagesPdf : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
export function createPdf(options) {
  const {
    pages,
    imagePaths,
    ...opts
  } = options;
  const mappedPages = (imagePaths || pages || []).map(e => {
    if (typeof e === 'string') {
      return {
        imagePath: e
      };
    }
    return e;
  });
  return ImagesPdf.createPdf({
    ...opts,
    pages: mappedPages
  });
}
export function getDocumentsDirectory() {
  return ImagesPdf.getDocumentsDirectory();
}
//# sourceMappingURL=index.js.map