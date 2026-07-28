//
//  Api+Orders.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 04/05/2024.
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
                    order.price = data["price"]?.string ?? ""
                    order.order_number =
                    data["order_number"]?.string ?? ""
                    order.has_offers =
                    data["has_offers"]?.bool ?? true
                    order.is_rated_before =
                    data["is_rated_before"]?.bool ?? true
                    order.order_status = data["order_status"]?.string ?? ""
                    order.order_type = data["order_type"]?.string ?? ""
                    order.describeData = data["description"]?.string ?? ""
                    order.categorytitle = data["category"]!["title"].string ?? ""
                    order.categorydescribe = data["category"]!["description"].string ?? ""
                    order.categoryicon = data["category"]!["icon"].string ?? ""
                    order.companyid = data["company"]!["id"].int ?? 0
            order.price_start_from = data["company"]!["price_start_from"].int ?? 0
                    
            order.company_logo = data["company"]!["company_logo"].string ?? ""
        order.company_name = data["company"]!["title"].string ?? ""
        order.address = data["company"]!["address"].string ?? ""
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
                    order.orderdescripe = data["description"]?.string ?? ""
                    order.order_duration_in_days = data["order_duration_in_days"]?.int ?? 0
                    order.is_rated_before =  data["is_rated_before"]?.bool ?? true
                    order.price = data["price"]?.string ?? ""
                    order.order_number = data["order_number"]?.string ?? ""
                    order.order_type = data["order_type"]?.string ?? ""
                    order.orderprice = data["price"]?.int ?? 0
                    order.ordertax = data["tax_percentage"]?.int ?? 0
                    order.order_type = data["order_type"]?.string ?? ""
                    order.order_status = data["order_status"]?.string ?? ""
                    order.describeData = data["description"]?.string ?? ""
                    order.created_at = data["created_at"]?.string ?? ""
                    
                    order.categorytitle = data["category"]!["title"].string ?? ""
                    order.categoryid = data["company"]!["categories"][0]["id"].int ?? 0
                    print(order.categoryid)
                    order.categorydescribe = data["category"]!["description"].string ?? ""
                    order.categoryicon = data["category"]!["icon"].string ?? ""
                    order.companyid = data["company"]!["id"].int ?? 0
                    order.price_start_from = data["company"]!["price_start_from"].int ?? 0
                    
                    order.company_logo = data["company"]!["company_logo"].string ?? ""
                    order.company_name = data["company"]!["title"].string ?? ""
                    order.address = data["company"]!["address"].string ?? ""
                    getallorders.append(order)
                    
                }
                
                completion(nil , getallorders,true,nil)
            }
        }
    }
    
    
  
    
    //send order
    class func sendOrder(category_id:Int,order_type:String,order_title:String,order_description:String,order_duration_in_days:Int, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.SendOrder
        
        print(url)
        
        let parameters = [
            "category_id":category_id,
            "order_type":order_type,
            "order_title":order_title,
            "order_description":order_description,
            "order_duration_in_days":order_duration_in_days
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
                getorderdetails.company_name = json["data"]["company"]["title"].stringValue
                getorderdetails.describeData = json["data"]["company"]["description"].stringValue
                getorderdetails.price_start_from = json["data"]["company"]["price_start_from"].intValue
                getorderdetails.address = json["data"]["company"]["address"].stringValue
                getorderdetails.company_logo = json["data"]["company"]["company_logo"].stringValue
                getorderdetails.created_at = json["data"]["company"]["created_at"].stringValue
                getorderdetails.order_number = json["data"]["order_number"].stringValue
                
                getorderdetails.order_title = json["data"]["title"].stringValue
                completion(nil , getorderdetails,true,nil)
                
                 
                
            }
            
            
        }
    }
    
    
    //rate company
    class func RateCompany(order_id:Int,company_id:Int,rate:Int,review:String, completion :  @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.RateOrder
        
        let parameters = [
            "order_id" : order_id,
            "company_id" : company_id,
            "rate" : rate,
            "review" : review
        ] as [String : Any]
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
    
    
    //send order
    class func sendSpecialOrder(company_id:Int,order_type:String,order_title:String,order_description:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.SendOrder
        
        print(url)
        
        let parameters = [
            "company_id":company_id,
            "order_type":order_type,
            "order_title":order_title,
            "order_description":order_description
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
    
}
