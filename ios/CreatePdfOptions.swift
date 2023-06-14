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
}

struct CreatePdfOptions: Decodable {
  let imagePaths: [String]
  let outputDirectory: String
  let outputFilename: String
  let pages: [Page]
}
