//
//  ArabicTextField.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 12/10/2023.
//

import Foundation
import UIKit
class ArabicTextField: UITextField {
    override var textInputMode: UITextInputMode? {
        UITextInputMode.activeInputModes.filter
        { $0.primaryLanguage == "ar" }.first ?? super.textInputMode }
}
