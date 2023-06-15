@objc(ImagesPdf)
class ImagesPdf: NSObject {
  let E_PDF_CREATE_ERROR = "PDF_CREATE_ERROR"
  let E_PDF_WRITE_ERROR = "PDF_WRITE_ERROR"
  let E_PDF_PAGE_CREATE_ERROR = "PDF_PAGE_CREATE_ERROR"
  let E_OUTPUT_DIRECTORY_DOES_NOT_EXIST = "OUTPUT_DIRECTORY_DOES_NOT_EXIST"
  let E_OUTPUT_DIRECTORY_IS_NOT_WRITABLE = "OUTPUT_DIRECTORY_IS_NOT_WRITABLE"
  let E_PAGES_IS_EMPTY = "PAGES_IS_EMPTY"
  
  @objc
  func createPdf(_ options: NSDictionary, resolver resolve:RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    do {
      let createPdfOptions = try parseOptions(options: options)
      
      let outputDirectory = createPdfOptions.outputDirectory
      let outputFilename = createPdfOptions.outputFilename
      let pages = createPdfOptions.pages
      
      if pages.isEmpty {
        throw CreatePdfError.pagesIsEmpty
      }
      
      let data = try renderPdfData(pages)
      
      let outputUrl = try writePdfFile(data: data,
                                       outputDirectory: outputDirectory,
                                       outputFilename: outputFilename)
      
      resolve(outputUrl.absoluteString)
    } catch CreatePdfError.pagesIsEmpty {
      reject(E_PAGES_IS_EMPTY,
             "pages is empty.",
             nil)
    } catch CreatePdfError.outputDirectoryIsNotWritable {
      reject(E_OUTPUT_DIRECTORY_IS_NOT_WRITABLE,
             "outputDirectory is not writable.",
             nil)
    } catch CreatePdfError.outputDirectoryDoesNotExist {
      reject(E_OUTPUT_DIRECTORY_DOES_NOT_EXIST,
             "outputDirectory does not exist.",
             nil)
    } catch CreatePdfError.pdfPageCreateError(let error) {
      reject(E_PDF_PAGE_CREATE_ERROR,
             error.localizedDescription,
             error)
    } catch CreatePdfError.pdfWriteError(let error) {
      reject(E_PDF_WRITE_ERROR,
             error.localizedDescription,
             error)
    } catch {
      reject(E_PDF_CREATE_ERROR,
             error.localizedDescription,
             error)
    }
  }
  
  func renderPdfData(_ pages: [Page]) throws -> Data {
    let renderer = UIGraphicsPDFRenderer()
    var pageError: Error? = nil
    
    let data = renderer.pdfData {(context) in
      for page in pages {
        let imageUrl = buildUrl(paths: [page.imagePath])!
        var image: UIImage? = nil
        
        do {
          let imageData = try Data(contentsOf: imageUrl)
          image = UIImage(data: imageData)
        } catch {
          pageError = error
          break
        }
        
        if let image = image {
          let width = page.width ?? image.size.width
          let height = page.height ?? image.size.height
          
          let pageBounds = CGRect(x: 0, y: 0, width: width, height: height)
          context.beginPage(withBounds: pageBounds, pageInfo: [:])
          
          var scaledImage: UIImage?
          if width != image.size.width || height != image.size.height {
            let fit = page.imageFit
            let size = CGSize(width: width, height: height)
            
            scaledImage = image.scale(to: size, with: fit)
          } else {
            scaledImage = image
          }
          
          scaledImage?.draw(at: .zero)
        }
      }
    }
    
    if let pageError = pageError {
      throw CreatePdfError.pdfPageCreateError(error: pageError)
    }
    
    return data
  }
  
  func writePdfFile(data: Data, outputDirectory: String, outputFilename: String) throws -> URL {
    let fileManager = FileManager.default
    
    let directoryUrl = buildUrl(paths: [outputDirectory])!
    let directoryPath = directoryUrl.path
    
    if !fileManager.fileExists(atPath: directoryPath) {
      throw CreatePdfError.outputDirectoryDoesNotExist
    }
    
    if !fileManager.isWritableFile(atPath: directoryPath) {
      throw CreatePdfError.outputDirectoryIsNotWritable
    }
    
    let url = buildUrl(paths: [outputDirectory, outputFilename])!
    
    do {
      try data.write(to: url)
    } catch {
      throw CreatePdfError.pdfWriteError(error: error)
    }
    
    return url
  }
  
  func buildUrl(paths: [String]) -> URL? {
    if paths.isEmpty {
      return nil
    }
    
    var url = URL(string: paths[0])!
    
    if url.scheme == nil {
      let fileScheme = URL(fileURLWithPath: "").scheme
      
      var urlComponent = URLComponents(string: paths[0])!
      urlComponent.scheme = fileScheme
      
      url = urlComponent.url!
    }
    
    for p in paths.dropFirst() {
      url.appendPathComponent(p)
    }
    
    return url
  }
  
  @objc
  func getDocumentsDirectory(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    let docsDirUrl = getDocumentsDirectoryURL()
    
    var docsDir = docsDirUrl.absoluteString
    docsDir.removeLast()
    
    resolve(docsDir)
  }
  
  func getDocumentsDirectoryURL() -> URL {
    let docsDir = try! FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
    return docsDir
  }
  
  func parseOptions(options: NSDictionary) throws -> CreatePdfOptions {
    let jsonData = try JSONSerialization.data(withJSONObject: options, options: [])

    let pdfCreateOptions = try JSONDecoder().decode(CreatePdfOptions.self, from: jsonData)
    
    return pdfCreateOptions
  }
}
