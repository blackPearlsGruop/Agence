//
//  Api+Favourite.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 04/05/2024.
//


import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    class func AllFavourite(completion : @escaping (_ error:Error? , _ getfavourite: [General]?, _ success : Bool , _ message:String?)->Void ) {
        
        
        let url = URLs.GetFavourite
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
                    fav.price_start_from = data["price_start_from"]?.int ?? 0
                    fav.title = data["title"]?.string ?? ""
                    
                    fav.describeData = data["description"]?.string ?? ""
                    fav.categorytitle = data["categories"]![0]["title"].string ?? ""
                  
                    fav.company_logo = data["company_logo"]?.string ?? ""
                    getallfav.append(fav)
                    
                }
                
                completion(nil , getallfav,true,nil)
            }
        }
    }
    
    //Addtofav
    class func Addtofav(company_id:Int, completion :  @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.AddFavourite
        
        let parameters = [
            "company_id" : company_id
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
    
   
}
