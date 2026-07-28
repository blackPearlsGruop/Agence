//
//  LanguageHandler.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 03/10/2023.
//


import Foundation

// constants
let appleLanguageKey = "AppleLanguages"
private let firstTimeLanguage = "firstTimeLanguage"

/// L102Language
class LanguageHandler {

    class func currentLanguage() -> Language {
        let userdef = UserDefaults.standard
        let langArray = userdef.object(forKey: appleLanguageKey) as! NSArray
        let current = langArray.firstObject as! String
        if let hyphenIndex = current.firstIndex(of: "-") {
              return Language(rawValue: String(current[..<hyphenIndex])) ?? Language.english
        }
        return Language(rawValue: current) ?? Language.english
     }

    /// set @lang to be the first in Applelanguages list
    class func setLanguage(_ language: Language) {
        let userdef = UserDefaults.standard
        userdef.set([language.hyphenatedCode, currentLanguage().hyphenatedCode], forKey: appleLanguageKey)
        userdef.synchronize()
    }

    public class func setDefaultLanguage(_ language: Language) {
        if !UserDefaults.standard.bool(forKey: firstTimeLanguage) {
            LanguageHandler.setLanguage(language)
            UserDefaults.standard.set(true, forKey: firstTimeLanguage)
        }
    }

    class var isArabic: Bool {
        return LanguageHandler.currentLanguage().code == "ar"

    }
}
