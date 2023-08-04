import type { ColorValue } from 'react-native';
export type ImageFit = 'none' | 'fill' | 'contain' | 'cover';
export type Page = {
    imagePath: string;
    imageFit?: ImageFit;
    width?: number;
    height?: number;
    backgroundColor?: ColorValue;
};
export type CreatePdfOptions = {
    outputPath: string;
    pages: Page[];
};
export declare function createPdf(options: CreatePdfOptions): Promise<string>;
export declare function getDocumentsDirectory(): Promise<string>;
//# sourceMappingURL=index.d.ts.map