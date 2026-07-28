//
//  Api+Services.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 28/04/2024.
//

import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    
    //getAllServices
    
    class func getAllServices(completion : @escaping (_ error:Error? , _ getservice: [General]?, _ success : Bool , _ message:String?)->Void) {
        
        
        let url = URLs.AllService
        
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
                
                
                var getallService = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let service = General()
                    service.id = data["id"]!.int ?? 0
                    print(service.id)
                    Helper.saveApiToken(value:"\(service.id )", key:"serviceid")
                    service.price = data["price"]!.int ?? 0
                    service.service_duration_in_days = data["service_duration_in_days"]!.int ?? 0
                    service.title = data["title"]!.string ?? ""
                    service.describeData = data["description"]!.string ?? ""
                    service.image = data["images"]?.string ?? ""
                    getallService.append(service)
                    
                }
                completion(nil , getallService,true,nil)
            }
        }
    }
    
    
    //offer details
    class func GetServiceDetails(serviceid:Int,completion : @escaping (_ error : Error? , _ getservicedetails:General?, _ success : Bool , _ message:String?)->Void ) {
   let url = "https://staging.agence.sa/api/v1/service/\(serviceid)"
      //  URLs.ServiceDetails + "\(serviceid)"
        print(url)
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
                
                
                let get_service = General()
            get_service.id = json["data"]["id"].intValue
                print(get_service.id )
get_service.service_duration_in_days = json["data"]["service_duration_in_days"].intValue
                
    get_service.price = json["data"]["price"].intValue
                get_service.title = json["data"]["title"].stringValue
                get_service.describeData = json["data"]["description"].stringValue
                get_service.serviceimage = json["data"]["images"].stringValue
                  print( get_service.serviceimage)
                completion(nil , get_service,true,nil)
                
                 
                
            }
            
            
        }
    }
    
    
    //DeleteService
    class func DeleteService(serviceid:Int, completion :  @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.DeleteService + "\(serviceid)"
       
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
    
    
    //EditService
    class func EditService(serviceid:Int,title:String,description:String,price:Int,service_duration_in_days:Int,_method:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.EditService + "\(serviceid)"
        
        print(url)
        
        let parameters = [
            "title":title,
            "description":description,
            "price":price,
            "service_duration_in_days":service_duration_in_days,
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
    
    class func AddServiceToCompany(name:String,descriptiondata:String,price:Int,service_duration_in_days:Int,company_logo:UIImage,completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.AddServiceToCompany
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
                
                form.append("\(service_duration_in_days)".data(using: String.Encoding.utf8)!, withName:"service_duration_in_days")
                
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
