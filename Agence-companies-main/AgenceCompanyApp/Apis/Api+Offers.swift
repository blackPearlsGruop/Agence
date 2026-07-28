//
//  Api+Offers.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/06/2024.
//


import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    
    
    //getAllOffers
    
    class func getAllOffers(completion : @escaping (_ error:Error? , _ getoffer: [General]?, _ success : Bool , _ message:String?)->Void) {
        
        
        let url = URLs.AllOffer
        
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
                
                
                var getallOffers = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let offer = General()
                    offer.id = data["id"]!.int ?? 0
                    print(offer.id)
                    offer.price = data["price"]!.int ?? 0
                    offer.title = data["title"]!.string ?? ""
                    offer.describeData = data["description"]!.string ?? ""
                    
                    getallOffers.append(offer)
                    
                }
                completion(nil , getallOffers,true,nil)
            }
        }
    }
    
    
    //offer details
    class func GetOfferDetails(offerid:Int,completion : @escaping (_ error : Error? , _ getofferdetails:General?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.OfferDetails + "\(offerid)"
        
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
                getoffer.id = json["data"]["id"].intValue
                getoffer.offer_duration_in_days = json["data"]["offer_duration_in_days"].intValue
                getoffer.price = json["data"]["price"].intValue
                getoffer.title = json["data"]["title"].stringValue
                getoffer.describeData = json["data"]["description"].stringValue
                getoffer.image = json["data"]["images"].stringValue
                completion(nil , getoffer,true,nil)
                
                 
                
            }
            
            
        }
    }
    
    
    
    //DeleteOffer
    class func DeleteOffer(offerid:Int, completion :  @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.DeleteOffer + "\(offerid)"
       print(url)
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        AF.request(url,method:.delete, parameters: nil , encoding: JSONEncoding.default, headers: headers)
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
    
    
    //EditOffer
    class func EditOffer(offerid:Int,title:String,description:String,price:Int,offer_duration_in_days:Int,_method:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.EditOffer + "\(offerid)"
        
        print(url)
        
        let parameters = [
            "title":title,
            "description":description,
            "price":price,
            "offer_duration_in_days":offer_duration_in_days,
            "_method":"_method"
        ] as [String : Any]
        
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        AF.request(url, method:.post, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON { (response) in
            
            switch response.result
            {
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
    
    //AddOffer
    class func AddOffer(order_id:Int,price:Int,completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.AddOffer
        
        print(url)
        
        let parameters = [
            "order_id":order_id,
            "price":price
        ]
        
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        AF.request(url, method:.post, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON { (response) in
            
            switch response.result
            {
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
    
    
    class func AddOfferToCompany(name:String,descriptiondata:String,price:Int,offer_duration_in_days:Int,company_logo:UIImage,completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.AddOfferToCompany
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        
        AF.upload(multipartFormData:{(form: MultipartFormData) in
            if let data = company_logo.jpegData(compressionQuality:0.5)
            {    form.append(data,withName:"images[]", fileName:"images[].jpeg", mimeType:"images[].jpeg(.lowest)")
              
                
                
                form.append(name.data(using: String.Encoding.utf8)!, withName: "title")
        
                form.append(descriptiondata.data(using: String.Encoding.utf8)!, withName:"description")
                form.append("\(price)".data(using: String.Encoding.utf8)!, withName:"price")
                
                form.append("\(offer_duration_in_days)".data(using: String.Encoding.utf8)!, withName:"offer_duration_in_days")
                
            }
            
            
            
            
        },to: URL.init(string: url)!, usingThreshold: UInt64.init(),
                  method: .post,
                  headers: headers).response{ response in
            do {
                switch response.result {
                case .failure(let error):
                    print(error)
                    completion(error.localizedDescription as! Error , false, nil)
                    print(error.localizedDescription)
                    
                    
                case.success(let value):
                    let json = JSON(value)
                    print(json)
                    
                    if json["code"].int == 200{
                        //success
                        print("upload succeed")
                        let message = json["message"].stringValue
                        
                        print(message)
                        completion(nil, true,message)
                        
                    } else{
                        
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                    }
                }
                
                
            }
            catch{
                print("JSONSerialization error:", error)
            }
        }
        
    }
}
