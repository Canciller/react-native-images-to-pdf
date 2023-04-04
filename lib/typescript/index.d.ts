export interface CreatePdfOptions {
    imagePaths: string[];
    outputDirectory: string;
    outputFilename: string;
}
export declare function createPdf(options: CreatePdfOptions): Promise<string>;
export declare function getDocumentsDirectory(): Promise<string>;
//# sourceMappingURL=index.d.ts.map