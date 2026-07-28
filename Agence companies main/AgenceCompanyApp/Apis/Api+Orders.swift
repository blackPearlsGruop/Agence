//
//  Api+Orders.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 26/06/2024.
//



import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    class func AllNewOrder(completion : @escaping (_ error:Error? , _ getneworder: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetNewOrder
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
                    order.orderid = data["id"]?.int ?? 0
                    order.title = data["title"]?.string ?? ""
                    order.created_at = data["created_at"]?.string ?? ""
                    order.order_duration_in_days = data["id"]?.int ?? 0
                    order.order_type =
                    data["order_type"]?.string ?? ""
                    order.order_number =
                    data["order_number"]?.string ?? ""
                    order.order_status = data["order_status"]?.string ?? ""
                    order.describeData = data["description"]?.string ?? ""
                    order.categorytitle = data["category"]!["title"].string ?? ""
                    getallorders.append(order)
                    
                }
                
                completion(nil , getallorders,true,nil)
            }
        }
    }
    
    //closedorder
    class func AllCloseOrder(completion : @escaping (_ error:Error? , _ getcloseorder: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetCloseOrder
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
                    order.orderid = data["id"]?.int ?? 0
                    order.title = data["title"]?.string ?? ""
                    order.created_at = data["created_at"]?.string ?? ""
                    order.order_duration_in_days = data["id"]?.int ?? 0
                    order.order_type =
                    data["order_type"]?.string ?? ""
                    order.order_number =
                    data["order_number"]?.string ?? ""
                    order.order_status = data["order_status"]?.string ?? ""
                    order.describeData = data["description"]?.string ?? ""
                    order.categorytitle = data["category"]!["title"].string ?? ""
                    getallorders.append(order)
                    
                }
                
                completion(nil , getallorders,true,nil)
            }
        }
    }
    
    
  
    //waitorder
    class func AllWaitOrder(completion : @escaping (_ error:Error? , _ getwaitorder: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetWaitOrder
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
                    order.orderid = data["id"]?.int ?? 0
                    order.title = data["title"]?.string ?? ""
                    order.created_at = data["created_at"]?.string ?? ""
                    order.order_duration_in_days = data["id"]?.int ?? 0
                    order.order_type =
                    data["order_type"]?.string ?? ""
                    order.order_number =
                    data["order_number"]?.string ?? ""
                    order.order_status = data["order_status"]?.string ?? ""
                    order.describeData = data["description"]?.string ?? ""
                    order.categorytitle = data["category"]!["title"].string ?? ""
                    getallorders.append(order)
                    
                }
                
                completion(nil , getallorders,true,nil)
            }
        }
    }
    
    
    
   
    //order details
    class func GetOrderDetails(orderid:Int,completion : @escaping (_ error : Error? , _ getorderdetails:General?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.OrderDetails + "\(orderid)"
        
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
                
                
                let getorderdetails = General()
                getorderdetails.title = json["data"]["title"].stringValue
                getorderdetails.describeData = json["data"]["description"].stringValue
              
                getorderdetails.order_number = json["data"]["order_number"].stringValue
                getorderdetails.categorytitle = json["data"]["category"]["title"].stringValue
                getorderdetails.title = json["data"]["title"].stringValue
                getorderdetails.order_type = json["data"]["order_type"].stringValue
                getorderdetails.created_at = json["data"]["created_at"].stringValue
                getorderdetails.describeData = json["data"]["description"].stringValue
                getorderdetails.order_duration_in_days = json["data"]["order_duration_in_days"].intValue
                getorderdetails.orderid = json["data"]["id"].intValue
                completion(nil , getorderdetails,true,nil)
                
                 
                
            }
            
            
        }
    }
    
  
    
}
