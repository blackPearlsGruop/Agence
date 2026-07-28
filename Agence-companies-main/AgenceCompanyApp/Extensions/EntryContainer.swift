
import Foundation
import UIKit
import RxSwift
import RxCocoa

@IBDesignable
class EntryContainer: UIView {
    
    // MARK: - UI
    lazy private var stack: UIStackView = {
        let object = UIStackView()
        object.axis = .vertical
        object.spacing = DesignSystem.s_verticalSpace
        
        object.addArrangedSubview(lblFloating)
        object.addArrangedSubview(lblError)
        return object
    }()
    lazy private var lblFloating: UILabel = {
        let object = UILabel()
        object.prepare(textColor: .black, font: .h7, textAlignment: isAr() ? .right : .left)
        object.isHidden = true
        return object
    }()
    lazy private var lblError: UILabel = {
        let object = UILabel()
        object.prepare(textColor: .error, font: .h8, textAlignment: isAr() ? .right : .left)
        object.isHidden = true
        return object
    }()
    
    @IBInspectable  var floatingLabelText : String? {
        didSet{
            self.lblFloating.text = floatingLabelText?.localized()
        }
    }

    // MARK: - Properties
    
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.setupUI()
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func layoutSubviews() {
        super.layoutSubviews()
    
        self.stack.fillParentConstraints()
        self.lblFloating.withConstraints(withHeight: self.lblFloating.heightToFont)
        self.lblError.withConstraints(minHeight: self.lblError.heightToFont, maxHeight: self.lblError.heightToFont*2)
    }
    
    // MARK: - setup
    private func setupUI(){
        self.addSubview(stack)
    }
    
}
//MARK: - add subview Label
extension EntryContainer{
    func addEntry(_ entry:UIView){
        self.stack.insertArrangedSubview(entry, at: 1)
    }
}
//MARK: - Floating Label
extension EntryContainer{
    func showFloatingLabel(){
        let entry = self.stack.arrangedSubviews[1]
        if let textField = entry as? UITextField{
            self.lblFloating.text = textField.placeholder
        }else if let textView = entry as? EntryTextView{
            self.lblFloating.text = textView.placeholder
        }
        
        self.lblFloating.isHidden = false
    }
    func hideFloatingLabel(){
        self.lblFloating.isHidden = true
    }
    func focusing(_ isFocused: Bool = true){
        self.lblFloating.textColor = DesignSystem.appColor(.black)
    }
}
//MARK: - Errors
extension EntryContainer{
    func showErrorMessage(message: String?){
        self.lblError.text = message?.localized()
        self.lblError.isHidden = false
        self.lblFloating.textColor = DesignSystem.appColor(.error)
        let textField = self.stack.arrangedSubviews[1]
        
        textField.withBorder(lineColor: .error)
        textField.shake()
    }
    func hideErrorMessage(){
        self.lblError.text = nil
        self.lblError.isHidden = true
        self.lblFloating.textColor = DesignSystem.appColor(.black)
        
        let textField = self.stack.arrangedSubviews[1]
        let isEntry: Bool = ((textField is Textfield) || (textField is EntryTextView))
        textField.withBorder(lineColor: isEntry ? .darkGray:.clear)
    }
}
fileprivate extension UIView{
    func shake() {
        let animation = CAKeyframeAnimation(keyPath: "transform.translation.x")
        animation.timingFunction = CAMediaTimingFunction(name: .linear)
        animation.duration = 0.6
        animation.values = [-20.0, 20.0, -20.0, 20.0, -10.0, 10.0, -5.0, 5.0, 0.0 ]
        layer.add(animation, forKey: "shake")
    }
}
    //MARK: usage
extension UIView{
    var entryContainer:EntryContainer?{
        return (superview?.superview as? EntryContainer ??
                superview?.superview?.superview as? EntryContainer ??
                superview?.superview?.superview?.superview as? EntryContainer)
    }
    var withEntryContainer:UIView{
        let object = EntryContainer()
        object.addEntry(self)
        return object
    }
    
    func showError(_ message: String?){
        self.entryContainer?.showErrorMessage(message: message)
    }
    func hideError(){
        self.entryContainer?.hideErrorMessage()
    }
}
extension UIView {
    func isAr() -> Bool {
        return ("lang".localized() == "ar")
    }
  
 
  
}
extension UILabel{
    func prepare(textColor: DesignSystem.sysColor,
                 font: DesignSystem.sysFont,
                 textAlignment: NSTextAlignment = .natural,
                 numberOfLines:Int = 0,
                 lineBreakMode:NSLineBreakMode = .byWordWrapping) {
        
        self.textColor = DesignSystem.appColor(textColor)
        self.font = DesignSystem.appFont(font)
        self.textAlignment =  textAlignment
        self.lineBreakMode = lineBreakMode
        self.numberOfLines = numberOfLines
    }
    var heightToFont:CGFloat{
        return ceil(self.font!.lineHeight)
    }
    var widthToFont:CGFloat{
        return ceil(self.intrinsicContentSize.width)
    }
}

class EntryTextView: UITextView {

    @IBInspectable var placeholder: String = ""
    @IBInspectable var placeholderColor: UIColor = .placeholderText
    private var showingPlaceholder: Bool = true // Keeps track of whether the field is currently showing a placeholder
    
    /// Disable Floating Label when true.
    @IBInspectable  var disableFloatingLabel : Bool = false

    override func didMoveToWindow() {
        super.didMoveToWindow()
        
        if text.isEmpty {
            showPlaceholderText() // Load up the placeholder text when first appearing, but not if coming back to a view where text was already entered
        }
    }
    
    override func becomeFirstResponder() -> Bool {
        // If the current text is the placeholder, remove it
        if showingPlaceholder {
            text = nil
            textColor = DesignSystem.appColor(.black)//nil // Put the text back to the default, unmodified color
            showingPlaceholder = false
        }
        
        
        self.hideError()
      
        
        self.withBorder()
        self.entryContainer?.focusing()
        
        return super.becomeFirstResponder()
    }
    
  
    

    private func showPlaceholderText() {
        showingPlaceholder = true
        textColor = placeholderColor
        text = placeholder
    }
}
class Textfield: UITextField {
  
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.setupUI()
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.withConstraints(withHeight: DesignSystem.textFieldHeight)
        self.attributedPlaceholder = NSAttributedString(string: placeholder ?? "", attributes: [NSAttributedString.Key.foregroundColor: UIColor.lightGray])
       

    }
    // MARK: - setup
    private func setupUI(){
        self.borderStyle = .none
        self.autocapitalizationType = .none
        
      
        self.withRoundedCorner(10)
        self.withBorder(lineColor: .gray2)

        self.backgroundColor = DesignSystem.appColor(.white)
            
        self.textAlignment = isAr() ? .right : .left
        self.textColor = DesignSystem.appColor(.black)
        self.font = DesignSystem.appFont(.h6)
        
      
        
    }
}

extension UIView {

    //MARK: withRoundedCorner
    func withRoundedCorner(_ radius: CGFloat = DesignSystem.buttonHeight*0.5,topEdgesOnly:Bool = false,bottomEdgesOnly:Bool = false) {
        if topEdgesOnly {
            self.layer.maskedCorners = [.layerMinXMinYCorner,.layerMaxXMinYCorner]
        }
        if bottomEdgesOnly {
            self.layer.maskedCorners = [.layerMinXMaxYCorner,.layerMaxXMaxYCorner]
        }
        self.layer.cornerRadius = radius
        self.clipsToBounds = true
    }
    
    func withBorder(lineWidth: CGFloat = 1, lineColor: DesignSystem.sysColor = .mediumDarkGray ) {
        self.layer.borderWidth = lineWidth
        self.layer.borderColor = lineColor.value.cgColor
    }
    
    func withContainer( backgroundColor: DesignSystem.sysColor = .clear,
                        leading:CGFloat? = nil, trailing:CGFloat? = nil,
                        top:CGFloat? = nil, bottom:CGFloat? = nil,
                        withWidth:CGFloat? = nil, withHeight:CGFloat? = nil,
                        centerVertical:Bool = false,centerHorizontal:Bool = false) -> UIView{
        let container = UIView()
        container.backgroundColor = DesignSystem.appColor(backgroundColor)
        container.addSubview(self)
        self.withConstraints(leading: leading, trailing: trailing, top: top, bottom: bottom,
                             withWidth: withWidth, withHeight: withHeight,
                             centerVertical: centerVertical, centerHorizontal: centerHorizontal)
        return container
    }
}
