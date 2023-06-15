import * as React from 'react';

import { createPdf, ImageFit, Page } from 'react-native-images-to-pdf';
import { launchImageLibrary } from 'react-native-image-picker';
import { pickDirectory } from 'react-native-document-picker';
import {
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';

export default function App() {
  const [isLoading, setIsLoading] = React.useState(false);
  const [width, setWidth] = React.useState<number | undefined>(594);
  const [height, setHeight] = React.useState<number | undefined>(842);
  const [imageFit, setImageFit] = React.useState<ImageFit | undefined>('cover');

  const parts: string[] = [];
  if (width) {
    parts.push(`${width}w`);
  }
  if (height) {
    parts.push(`${height}h`);
  }
  parts.push(imageFit ?? 'none');

  const outputFilename = parts.join('-') + '.pdf';

  const selectImages = async () => {
    setIsLoading(true);
    try {
      const result = await launchImageLibrary({
        mediaType: 'photo',
        selectionLimit: 0,
      });

      if (result.assets) {
        const resultPickDir = await pickDirectory();

        if (!resultPickDir) {
          return;
        }

        const outputDirectory = resultPickDir.uri;

        const pages = result.assets.map((asset) => {
          const page: Page = {
            imagePath: asset.uri as string,
            imageFit,
            width,
            height,
          };

          return page;
        });

        await createPdf({
          outputDirectory,
          outputFilename,
          pages,
        });
      }
    } catch (e) {
      console.error(e);
    }

    setIsLoading(false);
  };

  return (
    <View style={styles.root}>
      <TextInput
        style={styles.input}
        placeholder="Page width"
        defaultValue={width ? width.toString() : ''}
        onChangeText={(text) => {
          const n = parseFloat(text);
          setWidth(Number.isNaN(n) ? undefined : n);
        }}
      />
      <TextInput
        style={styles.input}
        placeholder="Page height"
        defaultValue={height ? height.toString() : ''}
        onChangeText={(text) => {
          const n = parseFloat(text);
          setHeight(Number.isNaN(n) ? undefined : n);
        }}
      />
      <TextInput
        style={styles.input}
        placeholder="Image fit"
        defaultValue={imageFit}
        onChangeText={(text) => {
          const f = text.toLowerCase();
          switch (f) {
            case 'none':
            case 'cover':
            case 'contain':
            case 'fill':
              setImageFit(f);
              break;
            default:
              setImageFit(undefined);
          }
        }}
      />

      <TouchableOpacity style={styles.button} onPress={selectImages}>
        <Text style={styles.buttonText}>Press to select images</Text>
      </TouchableOpacity>

      <Text>Page width: {width ?? 'undefined'}</Text>
      <Text>Page height: {height ?? 'undefined'}</Text>
      <Text>Image fit: {imageFit ?? 'undefined'}</Text>
      <Text style={styles.text}>Output file name: {outputFilename}</Text>

      {isLoading ? (
        <Text style={styles.loadingText}>Creating PDF document...</Text>
      ) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    marginBottom: 20,
  },
  buttonText: {
    color: 'blue',
  },
  text: {
    marginBottom: 20,
  },
  loadingText: {
    color: 'gray',
  },
  input: {
    marginBottom: 20,
  },
});
