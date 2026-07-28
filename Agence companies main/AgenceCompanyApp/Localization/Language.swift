//
//  Language.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 03/10/2023.
//



import Foundation
import UIKit

protocol LanguageProtocol {
    var code: String { get }
    var apiHeaderValue: String { get }
    var hyphenatedCode: String { get }
    var name: String { get }
    var localizedName: String { get }
    var direction: UISemanticContentAttribute { get }
    var interfaceDirection: UIUserInterfaceLayoutDirection { get }
}

enum Language: String{
  
  
    case arabic = "ar"
    case english = "en"

    var code: String {
        switch self {
        case .arabic:
            return "ar"
        case .english:
            return "en"
        }
    }

    var apiHeaderValue: String {
        switch self {
        case .arabic:
            return "ar"
        case .english:
            return "en"
        }
    }

    var hyphenatedCode: String {
        switch self {
        case .arabic:
            return "ar"
        case .english:
            return "en_US"
        }
    }

    var name: String {
        switch self {
        case .arabic:
            return "arabic"
        case .english:
            return "english"
        }
    }


    var direction: UISemanticContentAttribute {
        switch self {
        case .arabic:
            return .forceRightToLeft
        case .english:
            return .forceLeftToRight
        }
    }

    var interfaceDirection: UIUserInterfaceLayoutDirection {
        switch self {
        case .arabic:
            return .rightToLeft
        case .english:
            return .leftToRight
        }
    }
}



public enum AppLanguage: String {
    case arabic = "ar"
    case english = "en"
}
extension Localize {
    
    public class func isCurrentLanguageArabic() -> Bool {
        return Localize.currentLanguage() == "ar"
    }
    public class func isCurrentLanguageEnglish() -> Bool {
        return Localize.currentLanguage() == "en"
    }
    public class func setEnglishCurrentLanguage() {
        Localize.setCurrentLanguage("en")
    }
    public class func setArabicCurrentLanguage() {
        Localize.setCurrentLanguage("ar")
    }
    public class func getCurrentLanguage() -> AppLanguage {
        if  Localize.isCurrentLanguageEnglish() {
            return AppLanguage(rawValue: "en")!
        }
        return AppLanguage(rawValue: "ar")!
    }
    
}
