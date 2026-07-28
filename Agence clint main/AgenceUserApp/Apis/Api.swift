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
    //,password:String
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
                if let userid = json["data"]["user"]["id"].int{
                    print(value)
                    print("userid \(userid)")
                    Helper.saveApiToken(value: "\(userid)", key: "userid")
                   
                     let userphone = json["data"]["user"]["phone"].string
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
    class func register(name:String,phone:String,password:String,password_confirmation:String,accept_terms_and_conditions:Int,device_token:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.Register
        print(url)
        let parameters = [
            "name": name,
            "phone":phone,
            "password":password,
            "password_confirmation":password_confirmation,
            "accept_terms_and_conditions":accept_terms_and_conditions,
            "device_token":device_token
        ] as [String : Any]
        
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
            
                print(response)
                let json = JSON(value)
              
                if let userid = json["data"]["user"]["id"].int{
                    print(value)
                    print("userid \(userid)")
                    Helper.saveApiToken(value: "\(userid)", key: "userid")
                   
                     let userphone = json["data"]["user"]["phone"].string
                        print(value)
                        print("userphone \(userphone)")
                        Helper.saveApiToken(value: userphone ?? "", key: "userphonereg")
                   
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
              
              
                else
                
                {
                    let messageerror = json["message"].stringValue
                    print(messageerror)
                    completion(nil, false,messageerror)
                    
                }

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
