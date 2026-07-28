//
//  Extensions+Help.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 03/10/2023.
//


import Foundation
import UIKit
import KRProgressHUD
import Toast_Swift

class KRProgressHUDAppearance {
    /// Default style.
    public var style = KRProgressHUDStyle.white
    /// Default mask type.
    public var maskType = KRProgressHUDMaskType.custom(color:DesignSystem.appColor(DesignSystem.sysColor.secondry))
    /// Default KRActivityIndicatorView colors
    public var activityIndicatorColors = [UIColor]([.black, .lightGray])
    /// Default message label font.
    public var font = UIFont(name:"Cairo-Bold",size:15)
    /// Default HUD center offset of y axis.
    public var viewOffset = CGFloat(0.0)
    /// Default duration to show HUD.
    public var duration = Double(1.0)
}
extension String {
    var trimmed : String {
        return self.trimmingCharacters(in: .whitespacesAndNewlines)
    }
}

extension UIViewController {

    func showAlert(title: String , message: String) {
        let alert = UIAlertController(title: title as String, message: message as String, preferredStyle:.alert)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.cancel, handler:nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func showLoader(){
        KRProgressHUD.set(style: KRProgressHUDStyle.custom(background: UIColor.clear, text:UIColor.clear, icon: UIColor.clear))
        KRProgressHUD.set(maskType: KRProgressHUDMaskType.custom(color:DesignSystem.appColor(DesignSystem.sysColor.secondry)))
        KRProgressHUD.set(font:UIFont(name:"Somar-SemiBold",size:15)! )
        KRProgressHUD.set(activityIndicatorViewColors: [UIColor]([.blue, .lightGray]))
        KRProgressHUD.show()
        //KRProgressHUD.show(withMessage:"Loading...".localized(), completion: nil)
//        KRProgressHUD.showImage(UIImage(named:"logo")!,message: "Loading...")
//        KRProgressHUD.set(style: .custom(background: .white, text: .black, icon: nil))
        
    }
    
  
    
    func showSuccess(){
        KRProgressHUD.showSuccess()
        
    }
    
    
    func dismissLoader(){
       
        DispatchQueue.main.asyncAfter(deadline: .now()+1) {
           KRProgressHUD.dismiss()
        }
    }
    
    func showtoast(){
        
        var style = ToastStyle()
        style.messageColor = .black
        style.backgroundColor = .white
        self.view.makeToast("", duration: 3.0, position: .top, style: style)
    }
    
    func toast(){
        self.view.makeToast("", duration: 3.0, position: .top)
    }
    
    func dismisstoast(){
     self.view.hideToast()
        
    }
    
    
}
extension UIImage {
    func rotate(radians: Float) -> UIImage? {
        var newSize = CGRect(origin: CGPoint.zero, size: self.size).applying(CGAffineTransform(rotationAngle: CGFloat(radians))).size
        // Trim off the extremely small float value to prevent core graphics from rounding it up
        newSize.width = floor(newSize.width)
        newSize.height = floor(newSize.height)
        
        UIGraphicsBeginImageContextWithOptions(newSize, false, self.scale)
        let context = UIGraphicsGetCurrentContext()!
        
        // Move origin to middle
        context.translateBy(x: newSize.width/2, y: newSize.height/2)
        // Rotate around middle
        context.rotate(by: CGFloat(radians))
        // Draw the image at its center
        self.draw(in: CGRect(x: -self.size.width/2, y: -self.size.height/2, width: self.size.width, height: self.size.height))
        
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage
    }
    
    func resized(withPercentage percentage: CGFloat) -> UIImage? {
        let canvasSize = CGSize(width: size.width * percentage, height: size.height * percentage)
        UIGraphicsBeginImageContextWithOptions(canvasSize, false, scale)
        defer { UIGraphicsEndImageContext() }
        draw(in: CGRect(origin: .zero, size: canvasSize))
        return UIGraphicsGetImageFromCurrentImageContext()
    }
    
    func resizedTo200MB() -> UIImage? {
        guard let imageData = self.pngData() else { return nil }
        let targetBytes = 200.0

        var resizingImage = self
        var imageSizeKB = Double(imageData.count) / 1024.0

        while imageSizeKB > targetBytes {
            guard let resizedImage = resizingImage.resized(withPercentage: 0.8),
            let imageData = resizedImage.pngData() else { return nil }

            resizingImage = resizedImage
            imageSizeKB = Double(imageData.count) / 1024
        }

        return resizingImage
    }
    
  
}

extension String {
    
    func trim() -> String {
        return self.trimmingCharacters(in: .whitespacesAndNewlines)
    }
    
    func localized() -> String {
        return NSLocalizedString(self, comment: "")
    }
    
    var isValidEmail: Bool {
        let regularExpressionForEmail = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let testEmail = NSPredicate(format:"SELF MATCHES %@", regularExpressionForEmail)
        return testEmail.evaluate(with: self)
    }
    
    var isValidPhone: Bool {
        let regularExpressionForPhone = "^[6-9]\\d{9}$"
           // "^\\d{3}-\\d{3}-\\d{4}$"
        let testPhone = NSPredicate(format:"SELF MATCHES %@", regularExpressionForPhone)
        return testPhone.evaluate(with: self)
    }
    
    func validate(value: String) -> Bool {
               let PHONE_REGEX = "^\\d{3}-\\d{3}-\\d{4}$"
               let phoneTest = NSPredicate(format: "SELF MATCHES %@", PHONE_REGEX)
               let result = phoneTest.evaluate(with: value)
               return result
           }
    var html2AttributedString: NSAttributedString? {
        return Data(utf8).html2AttributedString
    }
    var html2String: String {
        return html2AttributedString?.string ?? ""
    }
    
    func toDate(format: String)-> Date?{
           let dateFormatter = DateFormatter()
           dateFormatter.locale = Locale(identifier: "en_US_POSIX")
           dateFormatter.dateFormat = format
           let date = dateFormatter.date(from: self)
           return date

       }
    
    func hasRange(_ range: NSRange) -> Bool {
        return Range(range, in: self) != nil
    }
    
}
extension Data {
var html2AttributedString: NSAttributedString? {
    do {
        return try NSAttributedString(data: self, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding: String.Encoding.utf8.rawValue], documentAttributes: nil)
    } catch {
        print("error:", error)
        return  nil
}}}
extension String {
    func getFormattedDat(fromFormatter:String, toFormat:String) -> String{
        let dateFormatterGet = DateFormatter()
        dateFormatterGet.dateFormat = fromFormatter
        dateFormatterGet.timeZone = TimeZone(identifier: "UTC")
        dateFormatterGet.locale = Locale(identifier: "en_US_POSIX")

        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = toFormat
        dateFormatterPrint.timeZone = TimeZone.current
        dateFormatterPrint.locale = Locale(identifier: "en_US_POSIX")

        let date: Date? = dateFormatterGet.date(from: self)
        return dateFormatterPrint.string(from: date!);
    }
    
}
extension UIViewController {
    //MARK: - Toast
    func MAK_ShowToast(backgroundColor: UIColor = DesignSystem.appColor(.success), message: String = "Done Successfully".localized(),_ closure: (() -> Void)? = nil ) {
        let toastContainer = UIView(frame: CGRect())
        toastContainer.backgroundColor = backgroundColor.withAlphaComponent(0.8)
        toastContainer.alpha = 0.0
        toastContainer.layer.cornerRadius = 10;
        toastContainer.clipsToBounds  =  true
        
        let toastLabel = UILabel(frame: CGRect())
        toastLabel.textColor = UIColor.white
        toastLabel.textAlignment = .center;
        toastLabel.font.withSize(12.0)
        toastLabel.text = message
        toastLabel.clipsToBounds  =  true
        toastLabel.numberOfLines = 0
        
        toastContainer.addSubview(toastLabel)
        view.addSubview(toastContainer)
        
        //view.inserts
        toastLabel.translatesAutoresizingMaskIntoConstraints = false
        toastContainer.translatesAutoresizingMaskIntoConstraints = false
        
        let a1 = NSLayoutConstraint(item: toastLabel, attribute: .leading, relatedBy: .equal, toItem: toastContainer, attribute: .leading, multiplier: 1, constant: 15)
        let a2 = NSLayoutConstraint(item: toastLabel, attribute: .trailing, relatedBy: .equal, toItem: toastContainer, attribute: .trailing, multiplier: 1, constant: -15)
        let a3 = NSLayoutConstraint(item: toastLabel, attribute: .bottom, relatedBy: .equal, toItem: toastContainer, attribute: .bottom, multiplier: 1, constant: -10)
        let a4 = NSLayoutConstraint(item: toastLabel, attribute: .top, relatedBy: .equal, toItem: toastContainer, attribute: .top, multiplier: 1, constant: 10)
        toastContainer.addConstraints([a1, a2, a3, a4])
        
        let c1 = NSLayoutConstraint(item: toastContainer, attribute: .leading, relatedBy: .equal, toItem: view, attribute: .leading, multiplier: 1, constant: 65)
        let c2 = NSLayoutConstraint(item: toastContainer, attribute: .trailing, relatedBy: .equal, toItem: view, attribute: .trailing, multiplier: 1, constant: -65)
        let c3 = NSLayoutConstraint(item: toastContainer, attribute: .bottom, relatedBy: .equal, toItem: view, attribute: .bottom, multiplier: 1, constant: -75)
        view.addConstraints([c1, c2, c3])
        
        UIView.animate(withDuration: 0.5, delay: 0.0, options: .curveEaseIn, animations: {
            toastContainer.alpha = 1.0
        }, completion: { _ in
            UIView.animate(withDuration: 0.5, delay: 0.5, options: .curveEaseOut, animations: {
                toastContainer.alpha = 0.0
            }, completion: {_ in
                toastContainer.removeFromSuperview()
                (closure ?? {})()
            })
        })
    }
    
    //MARK: - AlertMessage
    func alertMessage(title : String, btnTitle : String, message : String? = nil, _ closure: @escaping () -> Void) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: btnTitle, style: .destructive, handler: { (action) in
            closure()
            
            alert.dismiss(animated: false, completion: nil)
        }) )
        alert.addAction(UIAlertAction(title: "Cancel".localized(), style: .cancel, handler: { (action) in
            
            alert.dismiss(animated: false, completion: nil)
        }) )
        present(alert, animated: false, completion: nil)
        alert.view.tintColor = DesignSystem.appColor(.primary)
    }
}

extension String {
    var htmlToAttributedString: NSAttributedString? {
        guard let data = data(using: .utf8) else { return nil }
        do {
            return try NSAttributedString(data: data, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding:String.Encoding.utf8.rawValue], documentAttributes: nil)
        } catch {
            return nil
        }
    }
    var htmlToString: String {
        return htmlToAttributedString?.string ?? ""
    }
}
