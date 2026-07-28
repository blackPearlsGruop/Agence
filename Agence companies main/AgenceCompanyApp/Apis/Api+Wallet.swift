//
//  Api+Wallet.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 06/07/2024.
//

import Foundation
import UIKit
import Alamofire
import SwiftyJSON
extension Api{
    //AddWallet
  
    class func AddToWallet(name:String,bank_account:String,bank_account_number:String,iban_number:String, completion : @escaping (_ error : String?  , _ success : Bool , _ message:String?) -> Void )  {
        
        let url = URLs.AddToWallet
        
        print(url)
        
        let parameters = [
            "name":"name",
            "bank_account":"bank_account",
            "bank_account_number":"bank_account_number",
            "iban_number":"iban_number"
        ]
        
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
