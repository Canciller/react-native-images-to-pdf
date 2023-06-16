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
  const [backgroundColor, setBackgroundColor] = React.useState('#fff');
  const [imageFit, setImageFit] = React.useState<ImageFit | undefined>(
    'contain'
  );

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

        const pages = result.assets.map<Page>((asset) => ({
          imagePath: asset.uri as string,
          imageFit,
          width,
          height,
          backgroundColor,
        }));

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
        keyboardType="numeric"
        defaultValue={width ? width.toString() : ''}
        onChangeText={(text) => {
          const n = parseFloat(text);
          setWidth(Number.isNaN(n) ? undefined : n);
        }}
      />
      <TextInput
        style={styles.input}
        placeholder="Page height"
        keyboardType="numeric"
        defaultValue={height ? height.toString() : ''}
        onChangeText={(text) => {
          const n = parseFloat(text);
          setHeight(Number.isNaN(n) ? undefined : n);
        }}
      />
      <TextInput
        style={styles.input}
        placeholder="Page background color"
        value={backgroundColor}
        onChangeText={setBackgroundColor}
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

      <TouchableOpacity
        style={styles.button}
        onPress={selectImages}
        disabled={isLoading}
      >
        <Text style={styles.buttonText}>
          {isLoading ? 'Loading...' : 'Press to select images'}
        </Text>
      </TouchableOpacity>

      <Text>
        <Text style={styles.bold}>Page width:</Text> {width ?? 'image width'}
      </Text>
      <Text>
        <Text style={styles.bold}>Page height:</Text> {height ?? 'image height'}
      </Text>
      <Text>
        <Text style={styles.bold}>Image fit:</Text> {imageFit ?? 'none'}
      </Text>
      <Text>
        <Text style={styles.bold}>Output file name:</Text> {outputFilename}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    justifyContent: 'center',
    padding: 20,
  },
  input: {
    marginBottom: 20,
    borderWidth: 1,
    borderColor: '#32a9d9',
    borderRadius: 10,
    paddingHorizontal: 10,
    minHeight: 40,
  },
  button: {
    marginBottom: 20,
    backgroundColor: '#32a9d9',
    padding: 10,
    borderRadius: 10,
  },
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
    textTransform: 'uppercase',
    textAlign: 'center',
  },
  bold: {
    fontWeight: 'bold',
  },
});
