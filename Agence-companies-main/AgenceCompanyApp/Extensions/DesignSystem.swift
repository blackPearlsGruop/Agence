//
//  DesignSystem.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 03/10/2023.
//


import Foundation
import UIKit

//MARK: - Common constraints
class DesignSystem {
    //space
    static let headersVerticalSpace = (UIScreen.main.bounds.height * 0.04)
    static let verticalSpace = (UIScreen.main.bounds.height * 0.02)
    static let s_verticalSpace = (UIScreen.main.bounds.height * 0.005)
    static let horizontalSpace = (UIScreen.main.bounds.width * 0.04)
    static let s_horizontalSpace = (UIScreen.main.bounds.width * 0.01)
    
    // View
    static let viewHeight: CGFloat = UIScreen.main.bounds.height
    static let viewWidth: CGFloat = UIScreen.main.bounds.width
    static let halfWidthView: CGFloat = (((UIScreen.main.bounds.width * 0.9) - (horizontalSpace))/2)
    
    //Image
    static let imageHeight = (UIScreen.main.bounds.height * 0.08)
    
    //button
    static let buttonHeight = (UIScreen.main.bounds.height * 0.05)
    static let buttonWidth = (UIScreen.main.bounds.width * 0.34)
    static let s_buttonWidth = (UIScreen.main.bounds.width * 0.25)
    
    //text
    static let textFieldHeight = (UIScreen.main.bounds.height * 0.06)
    
    //SafeArea
    static let bottomGuide = UIApplication.shared.windows.first?.safeAreaInsets.bottom ?? 0
    static let topGuide = UIApplication.shared.windows.first?.safeAreaInsets.top ?? 0
}

//MARK: - Images
extension DesignSystem {

    enum sysImage: String {
        case welcome1
        case noData
        case background
        case homeBackground
        case cloud
        case dubaiLogo
        case logoWithOutTitle
        case vDubaiLogo
        case menu
        case topShadow
        case backArrow, forwardArrow
        case addProjct
        case oldProject
        //Templates
        case videoInstegram
        case videoTwitter
        case real
        case number
        case InstaCarousel
        case CreateImage
        case weekImage
        case home
        case moreoff
        case share
        case download
        case play
        case temp
        case music
        
        case sound
        case addition
        case text
        
        case cb
        case cb2
        case cb3
        
        case addCover
        case addImage
        
        case c1, c2, c3, c4, c5, c6
        case numberIcon, numberBackground, whiteLogo
        case QuotePostText
        case selectedRight

        var value: UIImage {
            return UIImage(named: self.rawValue) ?? UIImage()
        }
    }
    
    static func appImage(_ img: sysImage)-> UIImage {
        return img.value
    }
}

//MARK: - Colors
extension DesignSystem {
    
    enum sysColor: String {
        case secondry
        case primary, primary1
        case lightPrimary
        case lightGray
        
        case white
        case black
        
        case gray
        case gray2
        case mediumDarkGray
        case darkGray
        case secondGray
        
        case blue
        case lightBlue
        
        case error
        case success
        case warning
        case clear
        
        case c1, c2, c3, c4, c5
        var value: UIColor {
            return UIColor(named: self.rawValue) ?? UIColor.clear
        }
    }
    static func appColor(_ color:sysColor)->UIColor {
        return color.value
    }
}

//MARK: - Fonts
extension DesignSystem {
    enum sysFont {
        case h1
        case h2
        case h3
        case h4
        case h5
        case h6
        case h7
        case h8
        case h9
        case h10
        case h11
        case section
        enum FontWeight: String {
            case Regular
            case Medium
        }

        //value
        var value:UIFont {
        
            switch self {
            case .h1:
                return .systemFont(ofSize: 48,weight: .bold)
            case .h2:
                return .systemFont(ofSize: 30,weight: .bold)
            case .h3:
                return .systemFont(ofSize: 24,weight: .medium)
            case .h4:
                return .systemFont(ofSize: 20,weight: .regular)
            case .h5:
                return .systemFont(ofSize: 18,weight: .medium)
            case .h6:
                return .systemFont(ofSize: 16,weight: .regular)
            case .h7:
                return .systemFont(ofSize: 14,weight: .medium)
            case .h8:
                return .systemFont(ofSize: 12,weight: .regular)
            case .h9:
                return .systemFont(ofSize: 20,weight: .bold)
            case .h10:
                return .systemFont(ofSize: 17,weight: .bold)
            case .h11:
                return .systemFont(ofSize: 14,weight: .semibold)
            case .section:
                return .systemFont(ofSize: 16,weight: .semibold)
            }
        }
    }

    static func appFont(_ font:sysFont)->UIFont{
        return font.value
    }
}
