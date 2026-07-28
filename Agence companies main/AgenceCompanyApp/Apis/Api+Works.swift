//
//  Api+Works.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 26/06/2024.
//



import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    
    
    //getAllWorks
    
    class func getAllWorks(completion : @escaping (_ error:Error? , _ allworks: [General]?, _ success : Bool , _ message:String?)->Void) {
        
        
        let url = URLs.AllWorks
        
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
                
                
                var getallworks = [General]()
                for data in dataarray{
                    guard let data = data.dictionary else {return}
                    let work = General()
                    work.id = data["id"]!.int ?? 0
                    print(work.id)
                    work.work_file = data["work_file"]!.string ?? ""
                    work.title = data["title"]!.string ?? ""
                    work.describeData = data["description"]!.string ?? ""
                    work.created_at = data["created_at"]!.string ?? ""
                    getallworks.append(work)
                    
                }
                completion(nil , getallworks,true,nil)
            }
        }
    }
    

  
    //AddWork
    class func AddWork(title:String,descriptiondata:String,work_file:UIImage,completion :  @escaping (_ error : Error?  , _ success : Bool , _ message:String?) -> Void ) {
        
        let url = URLs.AddWork
        let headers: HTTPHeaders = [
            "Authorization": ("Bearer " + Helper.getApiToken(key:"token")!),
            "Accept-Language":"Lang".localized(),
            "Accept":"application/json",
            "Content-Type":"application/json"
            
        ]
        
        
        
        AF.upload(multipartFormData:{(form: MultipartFormData) in
            if let data = work_file.jpegData(compressionQuality:0.5)
            {    form.append(data,withName:"work_file", fileName:"work_file.jpeg", mimeType:"work_file.jpeg(.lowest)")
              
                
                
                form.append(title.data(using: String.Encoding.utf8)!, withName: "title")
        
                form.append(descriptiondata.data(using: String.Encoding.utf8)!, withName:"description")
                
            
                
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
