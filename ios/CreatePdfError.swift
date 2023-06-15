//
//  CreatePdfError.swift
//  ImagesPdf
//
//  Created by Gabriel Emilio Lopez Ojeda on 06/03/23.
//  Copyright © 2023 Facebook. All rights reserved.
//

enum CreatePdfError: Error {
  case pdfPageCreateError(error: Error)
  case pdfWriteError(error: Error)
  case outputDirectoryDoesNotExist
  case outputDirectoryIsNotWritable
  case noImagesProvided
}
