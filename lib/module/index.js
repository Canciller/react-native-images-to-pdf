import { NativeModules, Platform, processColor } from 'react-native';
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
    ...opts
  } = options;
  const internalPages = pages.map(e => {
    const {
      backgroundColor,
      ...page
    } = e;
    return {
      backgroundColor: processColor(backgroundColor) ?? undefined,
      ...page
    };
  });
  return ImagesPdf.createPdf({
    ...opts,
    pages: internalPages
  });
}
export function getDocumentsDirectory() {
  return ImagesPdf.getDocumentsDirectory();
}
//# sourceMappingURL=index.js.map