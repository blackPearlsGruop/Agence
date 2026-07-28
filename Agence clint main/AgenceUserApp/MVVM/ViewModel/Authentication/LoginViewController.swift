//
//  LoginViewController.swift
//  Salon_App
//
//  Created by Eng Yoka on 04/02/2024.
//

import UIKit

class LoginViewController:UIViewController {
    var fcmtoken:String?
    @IBOutlet weak var phonetextfield:UITextField!
    @IBOutlet weak var passtextfield:UITextField!
    @IBOutlet weak var passbutton:UIButton!
    @IBOutlet weak var loginbutton:UIButton!
    @IBOutlet weak var regbutton:UIButton!
    @IBOutlet weak var loginlabel:UILabel!
    @IBOutlet weak var acountlabel:UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = true
        navigationController?.interactivePopGestureRecognizer?.isEnabled = false
        phonetextfield.placeholder = "Phone Number".localized()
      
        loginbutton.setTitle("Login".localized(), for: .normal)
        regbutton.setTitle("Register Now".localized(), for: .normal)
        loginlabel.text = "Login".localized()
        acountlabel.text = "You dont have account ?".localized()
 
      
    }
    
    var iconClick:Bool = true
    @IBAction func passButtonAction(_ sender: UIButton) {
        if iconClick {
              passtextfield.isSecureTextEntry = false
            passbutton.setImage(UIImage(named: "pass"), for: .normal)
           } else {
               passtextfield.isSecureTextEntry = true
               passbutton.setImage(UIImage(named:"pass"), for: .normal)
           }
           iconClick = !iconClick
       }
    
  
    @IBAction func loginButtonAction(_ sender: UIButton) {
        
        if ((self.phonetextfield.text ?? "").trimmed.isEmpty) {
            self.MAK_ShowToast(message:"Phone Required".localized())
            return
        }
        fcmtoken = Helper.getApiToken(key:"fcmToken")
        //, password:passtextfield.text ?? ""
        self.showLoader()
        Api.login(phone:phonetextfield.text ?? "", device_token:"xfsdffsrfe"){[weak self](error: String? , success: Bool, message) in
            self?.dismissLoader()
            if success{
                //     self?.getfcmtoken()
                print("success")
                self?.view.makeToast(message, duration: 3.0, position: .center)
                let storyboard =  UIStoryboard(name:"Login", bundle: nil)
                let vc = storyboard.instantiateViewController(withIdentifier:"ConfirmCodeViewController") as! ConfirmCodeViewController
                vc.phonenumber = self?.phonetextfield.text
                self?.navigationController?.pushViewController(vc, animated: true)
            }
            else{
                
                self?.view.makeToast(message, duration: 3.0, position: .center)
            }
            
            if Reachability.isConnectedToNetwork() {
                print("Internet connection OK")
            } else {
                print("Internet connection FAILED")
                self?.view.makeToast("No Internet Connection,Make sure your device is connected to the internet. ".localized(), duration: 3.0, position: .top)
            }
            
            
            
            
        }
        
    }
    
 
    @IBAction func registerButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Login", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "RegisterViewController") as! RegisterViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
}
