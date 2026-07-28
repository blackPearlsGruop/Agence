//
//  Api+AppInfo.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//


import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    //terms and conditions
    class func TermsAndConditions(completion : @escaping (_ error : Error? , _ terms:General?)->Void ) {
        
        let url = URLs.TermAndConditions
        print(url)
        
        let headers: HTTPHeaders = [
            
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
        ]
        
        AF.request(url, method:.get, parameters:nil, encoding: JSONEncoding.default, headers: headers).responseJSON { (response) in
            
            switch response.result
            {
            case .failure(let error):
                print(error.localizedDescription)
                completion(error , nil)
                
            case .success(let value):
                let json = JSON(value)
                print(json)
                
                let getinfo = General()
                
                getinfo.describeData = json["data"]["description"].stringValue
                getinfo.title = json["data"]["title"].stringValue
                completion(nil , getinfo)
                
                
            }
        }
        
    }
    
    //contactUs Message
    class func contactUs (title:String,descriptiondata:String, completion : @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.ContactUs
        let parameters = [
            "title": title,
            "description": descriptiondata
        ] as [String : Any]
        
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
       
        
        AF.request(url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headers)
            .responseJSON { (response) in
                switch response.result {
                case .failure(let error):
                    completion(error , false, nil)
                    print("error")
                    
                case .success(let value):
                    
                    let json = JSON(value)
                    
                    if json["code"].intValue == 200{
                        let message = json["message"].stringValue
                        completion(nil, true,message )}
                    
                    else   if json["code"].intValue == 401{
                        print("error")
                        let messageerror = json["message"].stringValue
                        completion(nil, false, messageerror)
                        
                    }
                        
                    else {
                        print("error")
                        let messageerror = json["message"].stringValue
                        completion(nil, false, messageerror)
                        
                    }
                    
                    
                }
        }
        
    }
    
}
