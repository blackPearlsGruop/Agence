//
//  General.swift
//  Salon_App
//
//  Created by Eng Yoka on 04/02/2024.
//

import Foundation
class General:NSObject {
    //notification
    var id:Int = 0
    var title:String = ""
    var image:String = ""
    var serviceimage:String = ""
    var content:String = ""
    var created_at_for_humans:String = ""
    
    
    //profile
    var companyaddress:String = ""
    var companyservice:String = ""
    var companyname:String = ""
    var companydescribe:String = ""
    var companyimage:String = ""
    var companybackground:String = ""
    
    //app info
    var describeData:String = ""
    //works
    var work_file:String = ""
    var created_at:String = ""
    //order
    var order_number:String = ""
    var orderid:Int = 0
    var order_duration_in_days:Int = 0
    var order_type:String = ""
    var order_status:String = ""
    var categorytitle:String = ""

    //service
    var price:Int = 0
    var service_duration_in_days:Int = 0
    var offer_duration_in_days:Int = 0
}
