//
//  CreatePdfOptions.swift
//  ImagesPdf
//
//  Created by Gabriel Emilio Lopez Ojeda on 06/03/23.
//  Copyright © 2023 Facebook. All rights reserved.
//

struct CreatePdfOptions: Decodable {
  let imagePaths: [String]
  let outputDirectory: String
  let outputFilename: String
}
