//
//  Api.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 04/10/2023.
//



import Foundation
import UIKit
import Alamofire
import SwiftyJSON
class Api: NSObject {
    //MARK:- login

    class func login(phone:String,device_token:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.login
        print(url)
        
        let parameters = [
            "phone": phone,
          //  "password": password,
            "device_token": device_token
        ]
        
        
        let headers: HTTPHeaders = [
         
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
        ]
        
        
        AF.request(url, method:.post, parameters: parameters, encoding:JSONEncoding.default, headers: headers).responseJSON { (response) in
            
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
              
                else
                
                {
                    
                    
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                  
                
                }

            }
        }
        
    }

    //MARK:- check otp
    class func checkedotp(phone:String,otp_code:String,device_token:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.CheckedOtp
        
        print(url)
        
        let parameters = [
            "phone":phone,
            "otp_code":otp_code,
            "device_token":device_token
        ] as [String : Any]
        
        let headers: HTTPHeaders = [
          //  "Authorization":("Bearer " + Helper.getApiToken(key:"token")!),
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
                if let userid = json["data"]["company"]["id"].int{
                    print(value)
                    print("userid \(userid)")
                    Helper.saveApiToken(value: "\(userid)", key: "userid")
                   
                     let userphone = json["data"]["company"]["phone"].string
                        print(value)
                        print("userphone \(userphone)")
                        Helper.saveApiToken(value: "\(userphone)", key: "userphone")
                   
                    let token = json["data"]["access_token"].string
                    print(value)
                    print("token \(token)")
        Helper.saveApiToken(value:token ?? "", key:"token")
                    
                }
                if json["code"].intValue == 200
                
                {
                    let message = json["message"].stringValue
                    
                    print(message)
                    completion(nil, true,message)
                    
                }
                else if json["code"].intValue == 401
                 
                 {
                     
                      let message = json["message"].stringValue
                      
                      print(message)
                      completion(nil, false,message)
                 }
                else
                
                {
                    
                    
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                  
                
                }
                
                
                
            }
        }
        
    }
 
    //MARK:- register
    class func register(account_type:String,name:String,phone:String,country_id:Int ,city_id:Int,accept_terms_and_conditions:Int,device_token:String,commercial_licence:UIImage,categyid:Int,nationality_id:Int, completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.Register
   
        
        let headers: HTTPHeaders = [
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
        ]
        
            
            AF.upload(multipartFormData:{(form: MultipartFormData) in
                if let data = commercial_licence.jpegData(compressionQuality:0.5)
                {    form.append(data,withName:"commercial_licence", fileName:"commercial_licence.jpeg", mimeType:"commercial_licence.jpeg(.lowest)")
                   
                    
                    form.append(name.data(using: String.Encoding.utf8)!, withName: "name")
                    
                    form.append(phone.data(using: String.Encoding.utf8)!, withName:"phone")
                    
                    form.append(account_type.data(using: String.Encoding.utf8)!, withName:"account_type")
                    
                    form.append(device_token.data(using: String.Encoding.utf8)!, withName:"device_token")
                    
                    form.append("\(accept_terms_and_conditions)".data(using: String.Encoding.utf8)!, withName:"accept_terms_and_conditions")
                    
                    
                    form.append("\(nationality_id)".data(using: String.Encoding.utf8)!, withName:"nationality_id")
                    
                    
                    form.append("\(country_id)".data(using: String.Encoding.utf8)!, withName:"country_id")
                    
                    form.append("\(city_id)".data(using: String.Encoding.utf8)!, withName:"city_id")
//
                    form.append("\(categyid)".data(using: String.Encoding.utf8)!, withName:"categories[]")
//                    
                    
//                    for (index,category) in categyid.enumerated() {
//                        
//                form.append("\(category)".data(using: String.Encoding.utf8)!, withName: "categories[\(index)]")
                        
                   // }
                    
                    
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
                        if let userid = json["data"]["company"]["id"].int{
                            print(value)
                            print("userid \(userid)")
                            Helper.saveApiToken(value: "\(userid)", key: "userid")
                            
                            let userphone = json["data"]["company"]["phone"].string
                            print(value)
                            print("userphone \(userphone)")
                            Helper.saveApiToken(value: userphone ?? "", key: "userphonereg")
                            
                            let token = json["data"]["access_token"].string
                            print(value)
                            print("token \(token)")
                            Helper.saveApiToken(value:token ?? "", key:"token")
                        }
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
    
    //MARK:- resend codee
    class func resendCode(phone:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.ResendCode
        
        print(url)
        
        let parameters = [
            "phone": phone
        ]
        
        let headers: HTTPHeaders = [
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
                
                else if json["code"].intValue == 401
                 
                 {
                     
                      let message = json["message"].stringValue
                      
                      print(message)
                      completion(nil, false,message)
                 }
                
                else
                
                {
                    
                    
                        let message = json["message"].stringValue
                        print(message)
                        completion(nil, false,message)
                  
                
                }
                
                
                
            }
        }
        
    }
}
