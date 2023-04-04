# react-native-images-to-pdf

[![badge](https://img.shields.io/npm/v/react-native-images-to-pdf.svg?style=flat-square)](https://www.npmjs.com/package/react-native-images-to-pdf)

Easily generate PDF files from images in React Native.

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
  imagePaths: ['/path/to/image1.jpg', '/path/to/image2.jpg'],
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

| Option            | Type       | Description                                                                                                                 |
| ----------------- | ---------- | --------------------------------------------------------------------------------------------------------------------------- |
| `imagePaths`      | `string[]` | An array of paths to the images that should be included in the PDF. Images will be added to the PDF in the order specified. |
| `outputDirectory` | `string`   | The path to the directory where the output PDF file should be saved.                                                        |
| `outputFilename`  | `string`   | The name of the output PDF file.                                                                                            |

## Example

Check the `example` folder for a usage demo.

<table>
  <tr><td><strong>iOS</strong></td><td><strong>Android</strong></td></tr>
  <tr>
    <td><p align="center"><img src="/docs/example-ios.gif" height="500"></p></td>
    <td><p align="center"><img src="/docs/example-android.gif" height="500"></p></td>
  </tr>
</table>

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
