//
//  Api+Profile.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//
import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    
    //profile
    class  func GetProfileData (completion : @escaping (_ error : Error? , _ getprofile:General?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.GetProfileData
     
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
                
                let getprofile = General()
                getprofile.name = json["data"]["name"].stringValue
                
                
                getprofile.phone = json["data"]["phone"].stringValue
                
              
        getprofile.image = json["data"]["profile_image"].stringValue
                Helper.saveApiToken(value: getprofile.image,key:"userimage")
                
                
                getprofile.id = json["data"]["id"].intValue
                Helper.saveApiToken(value:"\(getprofile.id)", key:"user_id")
                
                
                completion(nil , getprofile,true,nil)
                
                
            }
            
            
        }
    }
    
    //update profile
    
    
    class func EditProfile(name:String,phone:String,profile_image:UIImage,_method:String,completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.EditProfile
        
        let headers: HTTPHeaders = [

            "Authorization":("Bearer " + Helper.getApiToken(key:"token")!),
             "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        
        AF.upload(multipartFormData:{(form: MultipartFormData) in
            if let data = profile_image.jpegData(compressionQuality:0.5)
            {    form.append(data,withName:"profile_image", fileName:"profile_image.jpeg", mimeType:"profile_image.jpeg(.lowest)")
                
                
                form.append(name.data(using: String.Encoding.utf8)!, withName: "name")
        
                form.append(phone.data(using: String.Encoding.utf8)!, withName:"phone")
                form.append(_method.data(using: String.Encoding.utf8)!, withName:"_method")
                
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
    
    //remove account
    
    class func RemoveAccount(completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        let url = URLs.RemoveAccount
      
        
        let headers: HTTPHeaders = [
            "Authorization":("Bearer " + Helper.getApiToken(key:"token")!),
            // "Accept-Language":"Lang".localized()
            "Accept":"application/json",
            "Content-Type":"application/json"
        ]
        
        
        AF.request(url,method:.post, parameters: nil , encoding: JSONEncoding.default, headers: headers)
            .responseJSON (){ (response) in
                switch response.result {
                case .failure(let error):
                    completion(error , false,nil)
                    print(error)
                case .success(let value):
                    let json = JSON(value)
                    print(response)
                    print(json)
                  
                  
                      
                        if json["code"].intValue == 200
                        
                        {
                            let message = json["message"].stringValue
                            
                            print(message)
                            completion(nil, true,message)
                            
                        }
                    
                    else if json["code"].intValue == 401{
                        let message = json["message"].stringValue
                        
                        completion(nil, false, message)
                        
                    }
                       
                        else {
                            let message = json["message"].stringValue
                            
                            completion(nil, false, message)
                            
                        }
                    
                }
            }
    }
}
