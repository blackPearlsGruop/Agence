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
    var created_at_for_humans:String = ""
    
    
    //profile
    var name:String = ""
    var phone:String = ""
    var image:String = ""
    
    //app info
    var describeData:String = ""
    
    
    //Orders
    var orderdescripe:String = ""
    var order_duration_in_days:Int = 0
    var order_number:String = ""
    var order_title:String = ""
    var price:String = ""
    var serviceprice:Int = 0
    var offerprice:Int = 0
    var orderprice:Int = 0
    var ordertax:Int = 0
    var categorytitle:String = ""
    var categorydescribe:String = ""
    var categoryicon:String = ""
    var categoryid:Int = 0
    var order_status:String = ""
    var order_type:String = ""
    var address:String = ""
    
    //Favourite
    var avg_rate:Float = 0.0
    var rate_count:Int = 0
    var price_start_from:Int = 0
    var company_logo:String = ""
    var created_at:String = ""
    var company_name:String = ""
    var orderid:Int = 0
    var companyid:Int = 0
    var is_added_favourite:Bool = false
    var has_offers:Bool = false
    var is_rated_before:Bool = false
    var work_file:String = ""
    
}
