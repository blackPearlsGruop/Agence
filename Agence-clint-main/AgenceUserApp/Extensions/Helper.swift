//
//  Helper.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 03/10/2023.
//

import Foundation

class Helper: NSObject {
    //save api token
    class func saveApiToken(value: String ,key: String){
        //save data to userdefaults
        let def = UserDefaults.standard
        def.setValue(value, forKey: key)
        def.synchronize()
    }
    
    //return api token
    
    class func getApiToken(key : String) -> String?{
        let def = UserDefaults.standard
        return def.object(forKey: key) as? String
        
    }
    
    
    class func removeId() {
        let defaults = UserDefaults.standard
        defaults.removeObject(forKey:"userid") //We Will delete the userDefaults
        defaults.synchronize() //Sync. the defaults.
        
    }
    
    
    class func removeAccessToken()  {
        let defaults = UserDefaults.standard
        defaults.removeObject(forKey:"token") //We Will delete the userDefaults
        defaults.synchronize() //Sync. the defaults.
        
    }
}
