//
//  UIImage+Scaling.swift
//  ImagesPdf
//
//  Created by Gabriel Emilio Lopez Ojeda on 14/06/23.
//  Copyright © 2023 Facebook. All rights reserved.
//

import UIKit

// TODO: ImagePosition

extension UIImage {
  func scale(to size: CGSize, with fit: ImageFit?) -> UIImage? {
    let fit = fit ?? .none
    
    switch fit {
    case .none:
      let origin = CGPoint(
        x: (size.width - self.size.width) / 2.0,
        y: (size.height - self.size.height) / 2.0
      )
      
      UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
      defer { UIGraphicsEndImageContext() }
      
      self.draw(in: CGRect(origin: origin, size: self.size))
      let newImage = UIGraphicsGetImageFromCurrentImageContext()
      
      return newImage
      
    case .contain, .cover:
      let widthRatio = size.width / self.size.width
      let heightRatio = size.height / self.size.height
      let scaleFactor: CGFloat
      
      if fit == .contain {
        scaleFactor = min(widthRatio, heightRatio)
      } else {
        scaleFactor = max(widthRatio, heightRatio)
      }
      
      let scaledSize = CGSize(
        width: self.size.width * scaleFactor,
        height: self.size.height * scaleFactor
      )
      
      let origin = CGPoint(
        x: (size.width - scaledSize.width) / 2.0,
        y: (size.height - scaledSize.height) / 2.0
      )
      
      UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
      defer { UIGraphicsEndImageContext() }
      
      self.draw(in: CGRect(origin: origin, size: scaledSize))
      let newImage = UIGraphicsGetImageFromCurrentImageContext()
      
      return newImage
      
    case .fill:
      UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
      defer { UIGraphicsEndImageContext() }
      
      self.draw(in: CGRect(origin: .zero, size: size))
      let newImage = UIGraphicsGetImageFromCurrentImageContext()
      
      return newImage
    }
  }
}
