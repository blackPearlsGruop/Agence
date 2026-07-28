//
//  Api+General.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 13/05/2024.
//



import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    //service
    class func AllCategories(completion : @escaping (_ error:Error? , _ getcategory: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = "https://staging.agence.sa/api/v1/service"
        print(url)
        let headers: HTTPHeaders = [
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
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    print(category.id)
                    category.title = data["title"]?.string ?? ""
                    category.describeData = data["description"]?.string ?? ""
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    //city
    class func AllCities(completion : @escaping (_ error:Error? , _ getcity: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetCountry
        print(url)

        let headers: HTTPHeaders = [
           
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
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    category.title = data["title"]?.string ?? ""
                  
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    //Availability
    class func availability(method:String,completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.Availability
        
        print(url)
        
        let parameters = [
            "_method":method
          
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
}
