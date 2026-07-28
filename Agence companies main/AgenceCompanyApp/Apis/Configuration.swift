//
//  Configuration.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 04/10/2023.
//

import Foundation
struct URLs{
    
    static let main = "https://staging.agence.sa/api/v1/company/"
    
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
    //EditBackground
    static let EditBackground = main + "profile/update-background-image"
    //RemoveAccount
    static let RemoveAccount = main + "profile/delete-account"
    
    //Wallet
static let AddToWallet = main + "send-withdrawal-request"
    
    
    //Notifications
    //GetNotifications
    static let GetNotifications = main + "notification"
    static let CountNotifications = main + "countUnreadNotifications"
    
    
    //App Info
    //TermAndConditions
    static let TermAndConditions = "https://staging.agence.sa/api/v1/page/2"
    //ContactUs
    static let ContactUs = main + "contact-us"
    //Consultation
    static let Consultation = main + "profile/update-consultation-price"
    
    
    //Services
    //AllService
    static let AllService = main + "service"
    //ServiceDetails
    static let ServiceDetails = main + "service/"
    //EditService
    static let EditService = main + "service/"
    //DeleteService
    static let DeleteService = main + "service/"
    //AddServiceToCompany
    static let AddServiceToCompany = main + "service"
    
    //AllOffer
    static let AllOffer = main + "offer"
    //OfferDetails
    static let OfferDetails = main + "offer/"
    //EditOffer
    static let EditOffer = main + "offer/"
    //DeleteOffer
    static let DeleteOffer = main + "offer/"
    //AddOffer
    static let AddOffer = main + "order"
    //AddOfferToCompany
    static let AddOfferToCompany = main + "offer"
    
    //Availability
    static let Availability = main + "profile/availability"
    

    //AllWorks
    static let AllWorks = main + "work"
    //AddWork
    static let AddWork = main + "work"
   
    //Orders
    //GetNewOrder
    static let GetNewOrder = main + "order?status[]=pending"
    //GetCloseOrder
    static let GetCloseOrder = main + "order?status[]=canceled"
    //GetWaitOrder
    static let GetWaitOrder = main + "order?status[]=in-progress"
    //OrderDetails
    static let OrderDetails = main + "order/"
    
    //General
    //GetCategory
    static let GetCategory = "https://staging.agence.sa/api/v1/category"
    static let GetService = "https://staging.agence.sa/api/v1/service"
    //GetCountry
    static let GetCountry = "https://staging.agence.sa/api/v1/city"
}
