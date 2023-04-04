import * as React from 'react';

import { createPdf } from 'react-native-images-to-pdf';
import { launchImageLibrary } from 'react-native-image-picker';
import { pickDirectory } from 'react-native-document-picker';
import { Button, StyleSheet, View } from 'react-native';

export default function App() {
  const selectImages = async () => {
    try {
      const result = await launchImageLibrary({
        mediaType: 'photo',
        selectionLimit: 0,
      });

      if (result.assets) {
        const assets = result.assets;
        const imagePaths = assets.map((asset) => asset.uri as string);

        const resultPickDir = await pickDirectory();

        if (!resultPickDir) {
          return;
        }

        const outputDirectory = resultPickDir.uri;
        const outputFilename = 'Example.pdf';

        await createPdf({
          imagePaths,
          outputDirectory,
          outputFilename,
        });
      }
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <View style={styles.root}>
      <Button title="Select images" onPress={selectImages} />
    </View>
  );
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
