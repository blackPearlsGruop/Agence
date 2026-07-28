//
//  Api+Home.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 04/05/2024.
//



import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    class func AllCategories(completion : @escaping (_ error:Error? , _ getcategory: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetCategory
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
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    category.title = data["title"]?.string ?? ""
                    category.describeData = data["description"]?.string ?? ""
                    category.image = data["images"]?.string ?? ""
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    class func AllCategoy(completion : @escaping (_ error:Error? , _ getcategory: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetCat
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
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    category.title = data["title"]?.string ?? ""
                    category.describeData = data["description"]?.string ?? ""
                  
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    
    class func SearchAllCategories(search:String,completion : @escaping (_ error:Error? , _ getcategory: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetCategory + "?search=\(search)"
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
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    category.title = data["title"]?.string ?? ""
                    category.describeData = data["description"]?.string ?? ""
                    category.image = data["images"]?.string ?? ""
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    
    //home slider
    class func getHomeSlider(completion : @escaping (_ error : Error? , _ gethomeslider:[General]?)->Void ) {
        
        let url = URLs.GetSlider
        
        let headers: HTTPHeaders = [
            "Accept-Language":"Lang".localized()
          
        ]
        
        AF.request(url,method:.get, parameters:nil, encoding: JSONEncoding.default, headers: headers).responseJSON { (response) in
            
            switch response.result
            {
            case .failure(let error):
                print(error.localizedDescription)
                completion(error , nil)
                
            case .success(let value):
                
                print(value)
                print("success")
                print(response)
                let json = JSON(value)
                guard let dataarray = json["data"].array else{
                    completion(nil , nil)
                    return
                }
                
                var getHomeSlider = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let slider = General()
                    slider.title = data["title"]!.string ?? ""
                    slider.image = data["image"]!.string ?? ""
                    getHomeSlider.append(slider)
                    
                }
                completion(nil , getHomeSlider)
            }
        }
    }
    
    class func AllCompany(completion : @escaping (_ error:Error? , _ getfavourite: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.AllCompany
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
                
                var getallfav = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let fav = General()
                    fav.id = data["id"]?.int ?? 0
                    fav.avg_rate = data["avg_rate"]?.float ?? 0
                    fav.rate_count = data["rate_count"]?.int ?? 0
                    fav.price_start_from = data["price_start_from"]?.int ?? 0
                    fav.title = data["title"]?.string ?? ""
                    fav.is_added_favourite = data["is_added_favourite"]?.bool ?? false
                    fav.describeData = data["description"]?.string ?? ""
                    fav.categorytitle = data["categories"]![0]["title"].string ?? ""
                  
                    fav.company_logo = data["company_logo"]?.string ?? ""
                    getallfav.append(fav)
                    
                }
                
                completion(nil , getallfav,true,nil)
            }
        }
    }
    
    //offer details
    class func GetCompanyDetails(companyid:Int,completion : @escaping (_ error : Error? , _ getofferdetails:General?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.CompanyDetails + "\(companyid)"
        
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
                getoffer.companyid = json["data"]["id"].intValue
                getoffer.company_name = json["data"]["title"].stringValue
                getoffer.describeData = json["data"]["description"].stringValue
                getoffer.is_added_favourite = json["data"]["is_added_favourite"].boolValue
                getoffer.address = json["data"]["address"].stringValue
                getoffer.avg_rate = json["data"]["avg_rate"].floatValue
                getoffer.company_logo = json["data"]["company_logo"].stringValue
                completion(nil , getoffer,true,nil)
                
            }
            
            
        }
    }
    
    //company service
    
    class func CompanyService(companyid:Int,completion : @escaping (_ error:Error? , _ getcategory: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = URLs.CompanyDetails + "\(companyid)"
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
                guard let dataarray = json["data"]["categories"].array else{
                    
                    completion(nil , nil,true,nil)
                    return
                }
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    category.title = data["title"]?.string ?? ""
                   // category.serviceprice = data["price"]?.int ?? 0
                    category.image = data["icon"]?.string ?? ""
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    
    //company offers
    class func CompanyOffers(companyid:Int,completion : @escaping (_ error:Error? , _ getoffers: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.CompanyDetails + "\(companyid)"
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
                guard let dataarray = json["data"]["offers"].array else{
                    
                    completion(nil , nil,true,nil)
                    return
                }
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    category.title = data["title"]?.string ?? ""
                    category.describeData = data["description"]?.string ?? ""
                    category.company_logo = data["company_logo"]?.string ?? ""
                    category.offerprice = data["price"]?.int ?? 0
                    
                    
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    //company Work
    class func CompanyWork(companyid:Int,completion : @escaping (_ error:Error? , _ getworks: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.CompanyDetails + "\(companyid)"
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
                guard let dataarray = json["data"]["works"].array else{
                    
                    completion(nil , nil,true,nil)
                    return
                }
                
                var getcategories = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let category = General()
                    category.id = data["id"]?.int ?? 0
                    category.title = data["title"]?.string ?? ""
                    category.describeData = data["description"]?.string ?? ""
                    category.work_file = data["work_file"]?.string ?? ""
                   
                    
                    getcategories.append(category)
                    
                }
                
                completion(nil , getcategories,true,nil)
            }
        }
    }
    
    //offer details
    class func GetServiceDetails(orderid:Int,completion : @escaping (_ error : Error? , _ getservicedetails:General?, _ success : Bool , _ message:String?)->Void ) {
        
        let url = "https://staging.agence.sa/api/v1/category/" + "\(orderid)"
        
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
                getoffer.title = json["data"]["companies"]["title"].stringValue
                getoffer.describeData = json["data"]["companies"]["description"].stringValue
                getoffer.price_start_from = json["data"]["companies"]["price_start_from"].intValue
                getoffer.address = json["data"]["companies"]["address"].stringValue
                getoffer.avg_rate = json["data"]["companies"]["avg_rate"].floatValue
                getoffer.company_logo = json["data"]["companies"]["company_logo"].stringValue
                completion(nil , getoffer,true,nil)
                
                 
                
            }
            
            
        }
    }
}
