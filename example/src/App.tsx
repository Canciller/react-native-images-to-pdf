import * as React from 'react';

import {
  createPdf,
  ImageFit,
  Page,
  getDocumentsDirectory,
} from 'react-native-images-to-pdf';
import { launchImageLibrary } from 'react-native-image-picker';
import Pdf from 'react-native-pdf';
import {
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';

export default function App() {
  const [uri, setUri] = React.useState('');
  const [isLoading, setIsLoading] = React.useState(false);
  const [width, setWidth] = React.useState<number | undefined>(594);
  const [height, setHeight] = React.useState<number | undefined>(842);
  const [backgroundColor, setBackgroundColor] = React.useState('black');
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
        const pages = result.assets.map<Page>((asset) => ({
          imagePath: asset.uri as string,
          imageFit,
          width,
          height,
          backgroundColor,
        }));

        const documentsDir = await getDocumentsDirectory();
        const outputPath = `${documentsDir}/${outputFilename}`;

        const uri = await createPdf({
          outputPath,
          pages,
        });

        console.log('PDF created successfully:', uri);

        setUri(uri);
      }
    } catch (e) {
      console.error('Failed to create PDF:', e);
    }

    setIsLoading(false);
  };

  if (uri) {
    return (
      <View style={{ flex: 1, backgroundColor: 'white' }}>
        <Pdf
          style={{ flex: 1 }}
          onError={console.error}
          source={{
            uri,
          }}
        />
        <TouchableOpacity
          style={[
            styles.button,
            {
              margin: 20,
            },
          ]}
          disabled={isLoading}
          onPress={() => setUri('')}
        >
          <Text style={styles.buttonText}>Close</Text>
        </TouchableOpacity>
      </View>
    );
  }

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
        autoCapitalize="none"
      />
      <TextInput
        style={styles.input}
        placeholder="Image fit"
        defaultValue={imageFit}
        autoCapitalize="none"
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
        style={[
          styles.button,
          {
            marginBottom: 20,
          },
        ]}
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
