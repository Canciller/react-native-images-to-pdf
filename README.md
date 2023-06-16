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
yarn add react-native-images-to-pdf
```

### iOS

Run `pod install` in the `ios` directory.

## Usage

```javascript
import { createPdf } from 'react-native-images-to-pdf';

const options = {
  pages: ['/path/to/image1.jpg', '/path/to/image2.jpg'],
  outputDirectory: '/path/to/output',
  outputFilename: 'output.pdf',
};

createPdf(options)
  .then((path) => console.log(`PDF created successfully: ${path}`))
  .catch((error) => console.log(`Failed to create PDF: ${error}`));
```

## API

### `createPdf(options: CreatePdfOptions) => Promise<string>`

Returns a Promise that resolves to a `string` representing the output path of the generated PDF file.

#### `CreatePdfOptions`

| Property            | Type       | Description                                                                                                                 |
| ----------------- | ---------- | --------------------------------------------------------------------------------------------------------------------------- |
| `pages`      | `Array<Page \| string>` | Pages that should be included in the PDF. |
| `outputDirectory` | `string`   | The path to the directory where the output PDF file should be saved.                                                        |
| `outputFilename`  | `string`   | The name of the output PDF file.                                                                                            |

### `Page`

| Property          | Type                      | Optional | Default       | Description                                                            |
|-------------------|---------------------------|----------|---------------|------------------------------------------------------------------------|
| `imagePath`       | `string`                  |          |               | Path to the image file to be included in the PDF.                       |
| `imageFit`        | `ImageFit \| undefined`    | ✓        | `'none'`      | Image fitting option. Possible values: `'none'`, `'fill'`, `'contain'`, `'cover'`. |
| `width`           | `number \| undefined`      | ✓        | Image width   | Width of the page in pixels. If not specified, it defaults to the width of the image. |
| `height`          | `number \| undefined`      | ✓        | Image height  | Height of the page in pixels. If not specified, it defaults to the height of the image. |

## Example

Check the `example` folder for a usage demo.

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
