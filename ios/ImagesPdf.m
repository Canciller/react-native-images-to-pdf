#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ImagesPdf, NSObject)

RCT_EXTERN_METHOD(
                  createPdf: (NSDictionary)options
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(
                  getDocumentsDirectory: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
