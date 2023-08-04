"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.createPdf = createPdf;
exports.getDocumentsDirectory = getDocumentsDirectory;
var _reactNative = require("react-native");
const LINKING_ERROR = `The package 'react-native-images-to-pdf' doesn't seem to be linked. Make sure: \n\n` + _reactNative.Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
const ImagesPdf = _reactNative.NativeModules.ImagesPdf ? _reactNative.NativeModules.ImagesPdf : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
function createPdf(options) {
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
      backgroundColor: (0, _reactNative.processColor)(backgroundColor) ?? undefined,
      ...page
    };
  });
  return ImagesPdf.createPdf({
    ...opts,
    pages: internalPages
  });
}
function getDocumentsDirectory() {
  return ImagesPdf.getDocumentsDirectory();
}
//# sourceMappingURL=index.js.map