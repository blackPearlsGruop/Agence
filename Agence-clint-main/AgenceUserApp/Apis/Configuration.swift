//
//  Configuration.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 04/10/2023.
//


import Foundation
struct URLs{
    
    static let main = "https://staging.agence.sa/api/v1/user/"
    
    //Authentication
    //login
    static let login = main + "auth/login"
    //register
    static let Register = main + "auth/register"
    //checked otp
    static let CheckedOtp = main + "auth/verify-otp"
    //resend code
    static let ResendCode = main + "auth/send-otp"
    
    //Profile
    //GetProfileData
    static let GetProfileData = main + "profile/me"
    //EditProfile
    static let EditProfile = main + "profile/update"
    //RemoveAccount
    static let RemoveAccount = main + "profile/delete-account"
    
    
    //Notifications
    //GetNotifications
    static let GetNotifications = main + "notification"
    static let CountNotifications = main + "countUnreadNotifications"
    
    
    //App Info
    //TermAndConditions
    static let TermAndConditions =
    "https://staging.agence.sa/api/v1/page/2"
    //ContactUs
    static let ContactUs = main + "contact-us"
    
    //Home
    //GetCategory
    static let GetCategory = "https://staging.agence.sa/api/v1/service"
    static let GetCat = "https://staging.agence.sa/api/v1/category"
    static let GetSlider = "https://staging.agence.sa/api/v1/banner"
    static let AllCompany = "https://staging.agence.sa/api/v1/companies"
    static let CompanyDetails = "https://staging.agence.sa/api/v1/companies/"
    static let GetServiceDetails = "https://staging.agence.sa/api/v1/category/"
    
    //Orders
    //GetNewOrder
    static let GetNewOrder = main + "order?status[]=in-progress&status[]=pending"
    //GetCloseOrder
    static let GetCloseOrder = main + "order?status[]=canceled&status[]=completed"
    //SendOrder
    static let SendOrder = main + "order"
    //OrderDetails
    static let OrderDetails = main + "order/"
    //RateOrder
    static let RateOrder = main + "rate-company"
    
    //Offers
    //GetOffers
    static let GetOffers = main + "company-offer"
    //AcceptOffer
    static let AcceptOffer = main + "accept-company-offer/"
    //RefuseOffer
    static let RefuseOffer = main + "reject-company-offer/"
    //OfferDetails
    static let OfferDetails = main + "company-offer/"
    
    
    
    //Favourite
    //GetFavourite
    static let GetFavourite = main + "favourite"
    //AddFavourite
    static let AddFavourite = main + "favourite"
}
