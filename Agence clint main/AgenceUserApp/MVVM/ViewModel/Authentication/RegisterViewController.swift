//
//  RegisterViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 12/02/2024.
//

import UIKit

class RegisterViewController: UIViewController {
    var check:Int?
    var check1:String?
    var fcmtoken:String?
    @IBOutlet weak var passtextfield:UITextField!
    @IBOutlet weak var passbutton:UIButton!
    @IBOutlet weak var nametextfield:UITextField!
    @IBOutlet weak var phonetextfield:UITextField!
    @IBOutlet weak var registerbutton:UIButton!
    @IBOutlet weak var checkbutton:UIButton!
    @IBOutlet weak var backButton:UIButton!
    @IBOutlet weak var loginbutton:UIButton!
    @IBOutlet weak var termsbutton:UIButton!
    @IBOutlet weak var reglabel:UILabel!
    @IBOutlet weak var accountlabel:UILabel!
    @IBOutlet weak var agreelabel:UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = true
        nametextfield.placeholder = "Name".localized()
        phonetextfield.placeholder = "Phone Number".localized()
        
        reglabel.text = "Register".localized()
        agreelabel.text = "I Agree".localized()
        accountlabel.text = "Already have account ?".localized()
        termsbutton.setTitle("The terms and conditions".localized(), for: .normal)
        registerbutton.setTitle("Register".localized(), for: .normal)
        loginbutton.setTitle("Login".localized(), for: .normal)
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
   
    @IBAction func loginButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Login", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    var bRec:Bool = true
    @IBAction func checkButtonAction(_ sender: UIButton) {
      
        bRec = !bRec
        if bRec {
            checkbutton.setImage(UIImage(named:"check"), for: .normal)
            check = 1
            check1 = "agree"
            
        }
        else{
            checkbutton.setImage(UIImage(named: "uncheck"), for: .normal)
            check1 = ""
           
        }
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
    
    @IBAction func registerButtonAction(_ sender: UIButton) {
        passtextfield.text = "aaaaaa"
        if ((self.nametextfield.text ?? "").trimmed.isEmpty) {
            self.MAK_ShowToast(message:"Name Required".localized())
            return
        }
        if ((self.phonetextfield.text ?? "").trimmed.isEmpty) {
            self.MAK_ShowToast(message:"Phone Required".localized())
            return
        }
        
//        if ((self.passtextfield.text ?? "").trimmed.isEmpty) {
//            self.MAK_ShowToast(message:"Password Required".localized())
//            return
//        }
        
        guard let data =  check1 ,!data.isEmpty  else {
            self.MAK_ShowToast(message:"Please accept terms and conditions".localized())
            return}
     
        
        self.showLoader()
        Api.register(name: nametextfield.text ?? "", phone:  phonetextfield.text ?? "", password:passtextfield.text ?? "", password_confirmation:passtextfield.text ?? "", accept_terms_and_conditions:check ?? 0, device_token:"xfsdffsrfe"){[weak self](error: String? , success: Bool, message) in
            self?.dismissLoader()
            if success{
                print("success")
                
                
                let storyboard = UIStoryboard(name: "Login", bundle:nil)
                let vc = storyboard.instantiateViewController(withIdentifier: "ConfirmCodeViewController") as! ConfirmCodeViewController
        vc.phonenumber = Helper.getApiToken(key:"userphonereg")!
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
  
    @IBAction func backButton(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

}
