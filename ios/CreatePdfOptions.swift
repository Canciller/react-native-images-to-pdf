//
//  CreatePdfOptions.swift
//  ImagesPdf
//
//  Created by Gabriel Emilio Lopez Ojeda on 06/03/23.
//  Copyright © 2023 Facebook. All rights reserved.
//

enum ImageFit: String, Decodable {
  case none
  case fill
  case contain
  case cover
}

struct Page: Decodable {
  let imagePath: String
  let imageFit: ImageFit?
  let width: Double?
  let height: Double?
  let backgroundColor: Int?
}

class CreatePdfOptions: Decodable {
  let outputPath: String
  let pages: [Page]
  
  init(_ options: NSDictionary) throws {
    let jsonData = try JSONSerialization.data(withJSONObject: options, options: [])
    let pdfCreateOptions = try JSONDecoder().decode(CreatePdfOptions.self, from: jsonData)
    
    self.outputPath = pdfCreateOptions.outputPath
    self.pages = pdfCreateOptions.pages
  }
}
