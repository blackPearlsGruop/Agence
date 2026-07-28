//
//  Api+Offers.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/06/2024.
//


import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    
    //AllOffer
    class func AllOffer(completion : @escaping (_ error:Error? , _ getoffer: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetOffers
        print(url)
        print(Helper.getApiToken(key:"token")!)
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        AF.request(url,method:.get, parameters:nil, encoding: JSONEncoding.default, headers: headers).responseJSON { (response) in
            
            switch response.result
            {
            case .failure(let error):
                print(error.localizedDescription)
                completion(error , nil,false,nil)
                
            case .success(let value):
                
                print(value)
                print("success")
                print(response)
                let json = JSON(value)
                if json["code"].intValue == 200
                    
                {
                    
                    let message = json["message"].stringValue
                    
                    print(message)
                    completion(nil,nil, true,message)
                }
                
                else if json["code"].intValue == 401
                            
                {
                    
                    let message = json["message"].stringValue
                    
                    print(message)
                    completion(nil,nil, false,message)
                }
                
                else
                
                {
                    
                    let message = json["message"].stringValue
                    print(message)
                    completion(nil,nil, false,message)
                    
                    
                }
                guard let dataarray = json["data"].array else{
                    
                    completion(nil , nil,true,nil)
                    return
                }
                
                var getallorders = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let order = General()
                    order.id = data["id"]!.int ?? 0
                    order.orderid = data["order"]!["id"].int ?? 0
                    order.order_number = data["order"]!["order_number"].string ?? ""
                    order.price_start_from = data["company"]!["price_start_from"].int ?? 0
                    
                    order.categorytitle = data["company"]!["title"].string ?? ""
                    order.categorydescribe = data["company"]!["description"].string ?? ""
//                    
                    getallorders.append(order)
                    
                }
                
                completion(nil , getallorders,true,nil)
            }
        }
    }
    
    //AcceptOffer
    class func AcceptOffer(orderId:Int,payment_method:String, completion :  @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.AcceptOffer + "\(orderId)"
        
        let parameters = [
            "payment_method" : payment_method
       
            
        ] 
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        AF.request(url,method:.post, parameters: parameters , encoding: JSONEncoding.default, headers: headers)
            .responseJSON (){ (response) in
                switch response.result {
                case .failure(let error):
                    completion(error.localizedDescription , false, nil)
                    print(error.localizedDescription)
                    
                case .success(let value):
                    print(value)
                    print("success")
                    print(response)
                    let json = JSON(value)
                    
                    if json["code"].intValue == 200
                        
                    {
                        let message = json["message"].stringValue
                        
                        print(message)
                        completion(nil, true,message)
                        
                    }
                    
                    else if  json["code"].intValue == 401{
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                        
                    }
                    
                    
                    else {
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                        
                    }
                    
                    
                    
                    
                }
            }
    }
    
    
    //RefuseOffer
    class func RefuseOffer(orderId:Int, completion :  @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.RefuseOffer + "\(orderId)"
    
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        AF.request(url,method:.post, parameters: nil , encoding: JSONEncoding.default, headers: headers)
            .responseJSON (){ (response) in
                switch response.result {
                case .failure(let error):
                    completion(error.localizedDescription , false, nil)
                    print(error.localizedDescription)
                    
                case .success(let value):
                    print(value)
                    print("success")
                    print(response)
                    let json = JSON(value)
                    
                    if json["code"].intValue == 200
                        
                    {
                        let message = json["message"].stringValue
                        
                        print(message)
                        completion(nil, true,message)
                        
                    }
                    
                    else if  json["code"].intValue == 401{
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                        
                    }
                    
                    
                    else {
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                        
                    }
                    
                    
                    
                    
                }
            }
    }
    
    
    //offer details
    class func GetOfferDetails(orderid:Int,completion : @escaping (_ error : Error? , _ getofferdetails:General?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.OfferDetails + "\(orderid)"
        
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        AF.request(url,method:.get, parameters:nil, encoding: JSONEncoding.default, headers: headers).responseJSON { (response) in
            
            switch response.result
            {
            case .failure(let error):
                print(error.localizedDescription)
                completion(error , nil,false,nil)
                
            case.success(let value):
                
                let json = JSON(value)
                print(json)
                
                
                    if json["code"].intValue == 200
                    
                    {
                        
                         let message = json["message"].stringValue
                         
                         print(message)
                         completion(nil,nil, true,message)
                    }
                    
                   else if json["code"].intValue == 401
                    
                    {
                        
                         let message = json["message"].stringValue
                         
                         print(message)
                         completion(nil,nil, false,message)
                    }
              
                    else
                    
                    {
                        
                        
                            let message = json["message"].stringValue
                            print(message)
                            completion(nil,nil, false,message)
                      
                    
                    }
                
                
                let getoffer = General()
                getoffer.title = json["data"]["company"]["title"].stringValue
                getoffer.describeData = json["data"]["company"]["description"].stringValue
                getoffer.price_start_from = json["data"]["company"]["price_start_from"].intValue
                getoffer.address = json["data"]["company"]["address"].stringValue
                getoffer.avg_rate = json["data"]["company"]["avg_rate"].floatValue
                getoffer.company_logo = json["data"]["company"]["company_logo"].stringValue
                completion(nil , getoffer,true,nil)
                
                 
                
            }
            
            
        }
    }
}
