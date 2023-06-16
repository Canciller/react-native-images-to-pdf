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
      return scaleWithNone(to: size)
    case .contain:
      return scaleWithContain(to: size)
    case .cover:
      return scaleWithCover(to: size)
    case .fill:
      return scaleWithFill(to: size)
    }
  }
  
  private func scaleWithNone(to size: CGSize) -> UIImage? {
    let scaledWidth = self.size.width
    
    let origin = CGPoint(
      x: (size.width - self.size.width) / 2.0,
      y: (size.height - self.size.height) / 2.0
    )
    
    UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
    defer { UIGraphicsEndImageContext() }
    
    self.draw(in: CGRect(origin: origin, size: self.size))
    
    return UIGraphicsGetImageFromCurrentImageContext()
  }
  
  private func scaleWithContain(to size: CGSize) -> UIImage? {
    let aspectRatio = self.size.width / self.size.height
    let targetAspectRatio = size.width / size.height
    
    var scaledSize = CGSize(width: size.width, height: size.height)
    if aspectRatio > targetAspectRatio {
      scaledSize.height = size.width / aspectRatio
    } else {
      scaledSize.width = size.height * aspectRatio
    }
    
    let origin = CGPoint(
      x: (size.width - scaledSize.width) / 2.0,
      y: (size.height - scaledSize.height) / 2.0
    )
    
    UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
    defer { UIGraphicsEndImageContext() }
    
    self.draw(in: CGRect(origin: origin, size: scaledSize))
    
    return UIGraphicsGetImageFromCurrentImageContext()
  }
  
  private func scaleWithCover(to size: CGSize) -> UIImage? {
    let aspectRatio = self.size.width / self.size.height
    let targetAspectRatio = size.width / size.height
    
    var scaleFactor = size.width / self.size.width
    if aspectRatio > targetAspectRatio {
      scaleFactor = size.height / self.size.height
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
    
    return  UIGraphicsGetImageFromCurrentImageContext()
  }
  
  private func scaleWithFill(to size: CGSize) -> UIImage? {
    UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
    defer { UIGraphicsEndImageContext() }
    
    self.draw(in: CGRect(origin: .zero, size: size))
    
    return UIGraphicsGetImageFromCurrentImageContext()
  }
}
