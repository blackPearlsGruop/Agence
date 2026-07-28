//
//  Api+Profile.swift
//  AgenceCompanyApp
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
                getprofile.companyname = json["data"]["title"].stringValue
                getprofile.companyaddress = json["data"]["address"].stringValue
                  getprofile.companydescribe = json["data"]["description"].stringValue
                getprofile.companyservice = json["data"]["categories"][0]["title"].stringValue
            getprofile.companybackground = json["data"]["company_background_image"].stringValue
                getprofile.companyimage = json["data"]["company_logo"].stringValue
                Helper.saveApiToken(value: getprofile.companyimage,key:"userimage")


                getprofile.id = json["data"]["id"].intValue
                Helper.saveApiToken(value:"\(getprofile.id)", key:"user_id")


                completion(nil , getprofile,true,nil)

                
                
            }
            
            
        }
    }
    
    //update profile
    
    
    class func EditProfile(name:String,descriptiondata:String,address:String,serviceid:[Int],company_logo:UIImage,completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.EditProfile
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        
        AF.upload(multipartFormData:{(form: MultipartFormData) in
            if let data = company_logo.jpegData(compressionQuality:0.5)
            {    form.append(data,withName:"company_logo", fileName:"company_logo.jpeg", mimeType:"company_logo.jpeg(.lowest)")
              
                
                
                form.append(name.data(using: String.Encoding.utf8)!, withName: "name")
        
                form.append(descriptiondata.data(using: String.Encoding.utf8)!, withName:"description")
                form.append(address.data(using: String.Encoding.utf8)!, withName:"address")
                
                for (index,category) in serviceid.enumerated() {
                    
            form.append("\(category)".data(using: String.Encoding.utf8)!, withName: "categories[\(index)]")
                    
                }
                
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
             "Accept-Language":"Lang".localized(),
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
    
    
    class func EditBackground(company_background_image:UIImage,completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.EditBackground
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        
        AF.upload(multipartFormData:{(form: MultipartFormData) in
            if let data = company_background_image.jpegData(compressionQuality:0.5)
            {    form.append(data,withName:"company_background_image", fileName:"company_background_image.jpeg", mimeType:"company_background_image.jpeg(.lowest)")
              
                
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

