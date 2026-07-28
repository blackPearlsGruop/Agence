//
//  UIView.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 03/10/2023.
//



import Foundation
import UIKit

extension UIView {
    //MARK: - Constraints
    func fillParentConstraints(widthPercentage:CGFloat = 1,heightPercentage:CGFloat = 1){
        guard let superview = self.superview else{return}
        self.centerXAnchor.constraint(equalTo:  superview.centerXAnchor).isActive = true
        self.centerYAnchor.constraint(equalTo:  superview.centerYAnchor).isActive = true
        
        self.widthAnchor.constraint(equalTo:  superview.widthAnchor, multiplier: widthPercentage).isActive = true
        self.heightAnchor.constraint(equalTo:  superview.heightAnchor, multiplier: heightPercentage).isActive = true
        
        self.translatesAutoresizingMaskIntoConstraints = false
    }
    // Constraints Function
    func withConstraints(toView:UIView? = nil,
                         leading:CGFloat? = nil, trailing:CGFloat? = nil,
                         top:CGFloat? = nil, bottom:CGFloat? = nil,
                         
                         withWidth:CGFloat? = nil, withHeight:CGFloat? = nil,
                         
                         minWidth:CGFloat? = nil, minHeight:CGFloat? = nil,
                         maxWidth:CGFloat? = nil, maxHeight:CGFloat? = nil,
                         
                         widthPercentage:CGFloat? = nil, heightPercentage:CGFloat? = nil,
                         
                         centerVertical:Bool = false,centerHorizontal:Bool = false,
                         
                         leadingToViewTrailing:CGFloat? = nil, trailingToViewLeading:CGFloat? = nil,
                         topToViewBottom:CGFloat? = nil, bottomToViewTop:CGFloat? = nil,
                         toSafeArea:Bool = true){
        
        guard let superview = toView ?? self.superview else{return}
        
        //Normal
        if let leading = leading{
            self.leadingAnchor.constraint(equalTo:  !toSafeArea ? superview.leadingAnchor:superview.safeAreaLayoutGuide.leadingAnchor,constant: leading).isActive = true
        }
        if let trailing = trailing{
            self.trailingAnchor.constraint(equalTo:  !toSafeArea  ? superview.trailingAnchor:superview.safeAreaLayoutGuide.trailingAnchor,constant: trailing).isActive = true
        }
        if let top = top{
            self.topAnchor.constraint(equalTo:  !toSafeArea  ? superview.topAnchor:superview.safeAreaLayoutGuide.topAnchor,constant: top).isActive = true
        }
        if let bottom = bottom{
            self.bottomAnchor.constraint(equalTo:  !toSafeArea  ? superview.bottomAnchor:superview.safeAreaLayoutGuide.bottomAnchor,constant: bottom).isActive = true
        }
        
        //Upnormal
        if let leadingToViewTrailing = leadingToViewTrailing{
            self.leadingAnchor.constraint(equalTo:  !toSafeArea  ? superview.trailingAnchor:superview.safeAreaLayoutGuide.trailingAnchor,constant: leadingToViewTrailing).isActive = true
        }
        if let trailingToViewLeading = trailingToViewLeading{
            self.trailingAnchor.constraint(equalTo:  !toSafeArea  ? superview.leadingAnchor:superview.safeAreaLayoutGuide.leadingAnchor,constant: trailingToViewLeading).isActive = true
        }
        if let topToViewBottom = topToViewBottom{
            self.topAnchor.constraint(equalTo:  !toSafeArea  ? superview.bottomAnchor:superview.safeAreaLayoutGuide.bottomAnchor,constant: topToViewBottom).isActive = true
        }
        if let bottomToViewTop = bottomToViewTop{
            self.bottomAnchor.constraint(equalTo:  !toSafeArea  ? superview.topAnchor:superview.safeAreaLayoutGuide.topAnchor,constant: bottomToViewTop).isActive = true
        }
        
        //Centering
        if centerVertical{
            self.centerYAnchor.constraint(equalTo:  superview.centerYAnchor).isActive = true
        }
        if centerHorizontal{
            self.centerXAnchor.constraint(equalTo:  superview.centerXAnchor).isActive = true
        }
        
        //withExactSizeConstraints
        if let withWidth = withWidth{
            self.widthAnchor.constraint(equalToConstant: withWidth).isActive = true
        }
        if let withHeight = withHeight{
            self.heightAnchor.constraint(equalToConstant: withHeight).isActive = true
        }
        
        //Min size
        if let minWidth = minWidth {
            self.widthAnchor.constraint(greaterThanOrEqualToConstant: minWidth).isActive = true
        }
        if let minHeight = minHeight {
            self.heightAnchor.constraint(greaterThanOrEqualToConstant: minHeight).isActive = true
        }
        
        //Max size
        if let maxWidth = maxWidth {
            self.widthAnchor.constraint(lessThanOrEqualToConstant: maxWidth).isActive = true
        }
        if let maxHeight = maxHeight {
            self.heightAnchor.constraint(lessThanOrEqualToConstant: maxHeight).isActive = true
        }
        
        if let widthPercentage = widthPercentage{
            self.widthAnchor.constraint(equalTo:  superview.widthAnchor, multiplier: widthPercentage).isActive = true
        }
        if let heightPercentage = heightPercentage{
            self.heightAnchor.constraint(equalTo:  superview.heightAnchor, multiplier: heightPercentage).isActive = true
        }
        
        self.translatesAutoresizingMaskIntoConstraints = false
    }
}



//MARK: - Design
@IBDesignable
class HSeparatorView: UIView {
    override func layoutSubviews() {
        super.layoutSubviews()
        self.backgroundColor = DesignSystem.appColor(.mediumDarkGray)
        self.withConstraints(withHeight: UIScreen.main.bounds.height * 0.001)
    }
}
@IBDesignable
class VSeparatorView: UIView {
    override func layoutSubviews() {
        super.layoutSubviews()
        self.backgroundColor = DesignSystem.appColor(.mediumDarkGray)
        self.withConstraints(withWidth: UIScreen.main.bounds.width * 0.001)
    }
}


//MARK: - RX - Cycle
import RxSwift
import RxCocoa

public extension Reactive where Base: UIView {
    var layoutSubviews: ControlEvent<Void> {
      let source = self.methodInvoked(#selector(Base.layoutSubviews)).map { _ in }
      return ControlEvent(events: source)
    }
}

import Foundation


extension Date{
    func toString(dateFormat: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone.current
        dateFormatter.locale = Locale(identifier: "en_US")
        dateFormatter.dateFormat = dateFormat
        return dateFormatter.string(from: self)
    }
}

import Foundation
import UIKit

extension String{
    var asAttributed: NSMutableAttributedString{
        return NSMutableAttributedString(string: self)
    }
}
extension NSMutableAttributedString {
    func setAttributed(_ text: String,
                       color: DesignSystem.sysColor ,
                       font: DesignSystem.sysFont,
                       alignment:NSTextAlignment = .natural,
                       lineSpacing: CGFloat = DesignSystem.verticalSpace,
                       hasUnderLine: Bool = false,
                       hasMiddleLine: Bool = false
    ) {
        
        let range: NSRange = self.mutableString.range(of: text, options: .caseInsensitive)
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.lineSpacing = lineSpacing
        paragraphStyle.alignment = alignment
        if hasMiddleLine{
            self.addAttribute(.strikethroughStyle, value: NSUnderlineStyle.single.rawValue, range: range)
        }
        if hasUnderLine{
            self.addAttribute(.underlineStyle, value: NSUnderlineStyle.single.rawValue, range: range)
        }
        self.addAttribute(NSAttributedString.Key.paragraphStyle, value: paragraphStyle, range: range)
        self.addAttribute(NSAttributedString.Key.foregroundColor, value: DesignSystem.appColor(color), range: range)
        self.addAttribute(NSAttributedString.Key.font, value: DesignSystem.appFont(font), range: range )
    }
}
