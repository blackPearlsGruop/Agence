//
//  Api+Notifications.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//



import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    class func AllNotifications(page:Int,count_paginate:Int,completion : @escaping (_ error:Error? , _ getnotify: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetNotifications
        
        let headers: HTTPHeaders = [
            "Authorization":("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json"
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
                
                var getnotifications = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let notify = General()
                    notify.id = data["id"]?.int ?? 0
                    notify.describeData = data["description"]?.string ?? ""
                    notify.title = data["title"]?.string ?? ""
                    notify.created_at_for_humans = data["created_at_for_humans"]?.string ?? ""
                    
              
                    getnotifications.append(notify)
                    
                }
                
                completion(nil , getnotifications,true,nil)
            }
        }
    }
    
    //count notification
    class func CountNotifications(completion : @escaping (_ error : Error? , _ countnotify:General?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.CountNotifications
        print(url)
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        AF.request(url, method:.get, parameters:nil, encoding: JSONEncoding.default, headers: headers).responseJSON { (response) in
            
            switch response.result
            {
            case .failure(let error):
                print(error.localizedDescription)
                completion(error , nil,false,nil)
                
            case .success(let value):
                let json = JSON(value)
                print(json)
                
                if json["code"].intValue == 200
                    
                {
                    
                    let message = json["successMessage"].stringValue
                    
                    print(message)
                    completion(nil,nil, true,message)
                }
                
                else if json["code"].intValue == 401
                            
                {
                    
                    let message = json["errorMessage"].stringValue
                    
                    print(message)
                    completion(nil,nil, false,message)
                }
                
                else
                
                {
                    
                    let message = json["errorMessage"].stringValue
                    print(message)
                    completion(nil,nil, false,message)
                    
                    
                }
                
                let getcount = General()
                
                //                getcount.notifications_count = json["data"].intValue
                //                print( getcount.notifications_count)
                
                completion(nil , getcount,true,nil)
                
                
            }
        }
        
    }
    
    
    
}
