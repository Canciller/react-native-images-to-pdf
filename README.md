# react-native-images-to-pdf

[![badge](https://img.shields.io/npm/v/react-native-images-to-pdf.svg?style=flat-square)](https://www.npmjs.com/package/react-native-images-to-pdf)

Easily generate PDF files from images in React Native.

<table>
  <tr><td><strong>iOS</strong></td><td><strong>Android</strong></td></tr>
  <tr>
    <td><p align="center"><img src="/docs/example-ios.gif" height="500"></p></td>
    <td><p align="center"><img src="/docs/example-android.gif" height="500"></p></td>
  </tr>
</table>

## Installation

```sh
npm install react-native-images-to-pdf
```

or

```sh
yarn add react-native-images-to-pdf
```

### iOS

Run `pod install` in the `ios` directory.

## Usage

### Example using [`react-native-blob-util`](https://github.com/RonRadtke/react-native-blob-util)

```javascript
import { createPdf } from 'react-native-images-to-pdf';
import RNBlobUtil from 'react-native-blob-util';

const options = {
  pages: [
    { imagePath: '/path/to/image1.jpg' },
    { imagePath: '/path/to/image2.jpg' }
  ],
  outputPath: `file://${RNBlobUtil.fs.dirs.DocumentDir}/file.pdf`,
};

createPdf(options)
  .then((path) => console.log(`PDF created successfully: ${path}`))
  .catch((error) => console.log(`Failed to create PDF: ${error}`));
```

This example is using [`react-native-blob-util`](https://github.com/RonRadtke/react-native-blob-util) to get a valid `outputPath`, but you can choose any other library to achieve the same functionality.

### Example using [`react-native-document-scanner-plugin`](https://github.com/websitebeaver/react-native-document-scanner-plugin)

```javascript
import { createPdf } from 'react-native-images-to-pdf';
import DocumentScanner from 'react-native-document-scanner-plugin';

DocumentScanner.scanDocument()
  .then(({scannedImages}) => {
    if (!scannedImages?.length) {
      throw new Error('No images scanned');
    }

    return createPdf({
      pages: scannedImages.map(imagePath => ({ imagePath })),
      outputPath: `file:///path/to/output/file.pdf`,
    });
  })
  .then(path => console.log(`PDF created successfully: ${path}`))
  .catch(error => console.log(`Failed to create PDF: ${error}`));
```

## API

### `createPdf(options: CreatePdfOptions) => Promise<string>`

Returns a Promise that resolves to a `string` representing the output path of the generated PDF file.

#### `CreatePdfOptions`

| Property     | Type                    | Description                               |
| ------------ | ----------------------- | ----------------------------------------- |
| `pages`      | `Page[]`                | Pages that should be included in the PDF. |
| `outputPath` | `string`                | The path to the output PDF file.          |

#### Valid `outputPath`

| Usage                              | Description                    | iOS | Android |
| ---------------------------------- | ------------------------------ | --- | ------- |
| `file:///absolute/path/to/xxx.pdf` | Save PDF to local file system. | ✓   | ✓       |

### `Page`

| Property          | Type       | Required | Default      | Description                                                                        |
| ----------------- | ---------- | -------- | ------------ | ---------------------------------------------------------------------------------- |
| `imagePath`       | `string`   | ✓        |              | Path to the image file.                                                            |
| `imageFit`        | `ImageFit` |          | `'none'`     | Image fitting option. Possible values: `'none'`, `'fill'`, `'contain'`, `'cover'`. |
| `width`           | `number`   |          | Image width  | Width of the page in pixels.                                                       |
| `height`          | `number`   |          | Image height | Height of the page in pixels.                                                      |
| `backgroundColor` | `string`   |          | `'white'`    | Background color of the page.                                                      |

#### Valid `imagePath`

| Usage                                | Description                        | iOS | Android |
| ------------------------------------ | ---------------------------------- | --- | ------- |
| `file:///absolute/path/to/image.xxx` | Load image from local file system. | ✓   | ✓       |
| `data:image/xxx;base64,iVBORw...`    | Load image from base64 string.     | ✓   | ✓       |

## Example

Check the `example` folder for a usage demo.

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
