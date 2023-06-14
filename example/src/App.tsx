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
  const [outputFilename, setOutputFilename] = React.useState('Example.pdf');
  const [width, setWidth] = React.useState(594);
  const [height, setHeight] = React.useState(842);
  const [imageFit, setImageFit] = React.useState<ImageFit>('cover');

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
          imagePaths: [],
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
      <TouchableOpacity style={styles.button} onPress={selectImages}>
        <Text style={styles.buttonText}>Press to select images</Text>
      </TouchableOpacity>

      <TextInput
        style={styles.input}
        placeholder="Output file name"
        value={outputFilename}
        onChangeText={setOutputFilename}
      />
      <TextInput
        style={styles.input}
        placeholder="Page width"
        defaultValue={height ? height.toString() : ''}
        onChangeText={(text) => {
          const n = parseFloat(text);
          if (!Number.isNaN(n)) {
            setWidth(n);
          }
        }}
      />
      <TextInput
        style={styles.input}
        placeholder="Page height"
        defaultValue={width ? width.toString() : ''}
        onChangeText={(text) => {
          const n = parseFloat(text);
          if (!Number.isNaN(n)) {
            setHeight(n);
          }
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
          }
        }}
      />

      {isLoading ? (
        <Text style={styles.text}>Creating PDF document...</Text>
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
    color: 'gray',
  },
  input: {
    marginBottom: 20,
  },
});
