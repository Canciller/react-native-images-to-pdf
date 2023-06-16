export type ImageFit = 'none' | 'fill' | 'contain' | 'cover';
export type Page = {
    imagePath: string;
    imageFit?: ImageFit;
    width?: number;
    height?: number;
};
export type CreatePdfOptions = {
    outputDirectory: string;
    outputFilename: string;
    pages: Array<Page | string>;
    imagePaths?: undefined;
} | {
    outputDirectory: string;
    outputFilename: string;
    pages?: undefined;
    /**
     * @deprecated Use the `pages` property instead.
     */
    imagePaths: string[];
};
export declare function createPdf(options: CreatePdfOptions): Promise<string>;
export declare function getDocumentsDirectory(): Promise<string>;
//# sourceMappingURL=index.d.ts.map