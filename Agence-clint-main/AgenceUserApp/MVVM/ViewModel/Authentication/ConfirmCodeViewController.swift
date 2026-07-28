//
//  ConfirmCodeViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 12/02/2024.
//

import UIKit

class ConfirmCodeViewController: UIViewController, UITextFieldDelegate {
    var count = 60
    var fcmtoken:String?
    
    @IBOutlet weak var timerLabel: UILabel!
    @IBOutlet weak var phonelabel:UILabel!
    @IBOutlet weak var otpview:OTPStackView!
    @IBOutlet weak var backbutton:UIButton!
    @IBOutlet weak var verifyLabel: UILabel!
    @IBOutlet weak var enterverifyLabel: UILabel!
    @IBOutlet weak var receiveLabel: UILabel!
    @IBOutlet weak var digitLabel: UILabel!
    @IBOutlet weak var resendbutton:UIButton!
    @IBOutlet weak var confirmbutton:UIButton!
    var otp:String?
    var phonenumber:String?
    override func viewDidLoad() {
        super.viewDidLoad()
        settimer()
        verifyLabel.text = "Verification Code".localized()
        enterverifyLabel.text = "Enter the verification code".localized()
        receiveLabel.text = "Didn't receive the confirmation code?".localized()
        digitLabel.text = "A 4-digit code was sent to the mobile number".localized()
        
        resendbutton.setTitle("Resend".localized(), for: .normal)
        confirmbutton.setTitle("Confirm".localized(), for: .normal)
        
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
   
    override func viewWillAppear(_ animated: Bool) {
        phonelabel.text = phonenumber ?? ""

    }
   
    func settimer(){
      
            Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { (Timer) in
                if self.count > 0 {
                    print ("\(self.count) seconds")
                    self.timerLabel.text = "00:\(self.count)"
                    self.count -= 1
                  
                } else {
                  
                    Timer.invalidate()
                    self.count = 59
                  
                }
            }
            
        
      
    }
//    func getfcmtoken(){
//        
//        fcmtoken = Helper.getApiToken(key:"fcmToken")
//        print("Firebase registration token:\(fcmtoken ?? "")")
//        
//     
//        Api.GetFcmToken(fcm_token:fcmtoken ?? ""){[weak self](error : Error? , success : Bool , message1) in
//                if success {
//                    self?.view.makeToast(message1 ?? "", duration: 3.0, position: .top)
//                } else {
//                    self?.view.makeToast(message1 ?? "", duration: 3.0, position: .top)
//                }
//        }
//    }

    @IBAction func confiremButtonAction(_ sender: UIButton) {
        Helper.saveApiToken(value:"true",key:"isFirstlaunch")
        fcmtoken = Helper.getApiToken(key:"fcmToken")
        otp = otpview.getOTP()
        print(otp)
   
       if ((self.otp ?? "").trimmed.isEmpty) {
           self.view.makeToast("Code Required", duration: 3.0, position: .center)
          return
       }
      
       self.showLoader()
        Api.checkedotp(phone:phonenumber ?? "", otp_code: otp ?? "", device_token:"xfsdffsrfe"){[weak self](error: String? , success: Bool, message) in
            self?.dismissLoader()
                if success{
                    //self?.getfcmtoken()
                    print("success")
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                            let storyboard = UIStoryboard(name: "Home", bundle:nil)
                            let vc = storyboard.instantiateViewController(withIdentifier: "HomeTabBar") as! HomeTabBar
                            self?.navigationController?.pushViewController(vc, animated: true)

            }
                else{
            if message  == "Unauthenticated user".localized(){
                            Helper.removeId()
                            Helper.removeAccessToken()
                self?.view.makeToast(message, duration: 3.0, position: .center)
                            let storyboard = UIStoryboard(name: "Login", bundle:nil)
                            let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
                            self?.navigationController?.pushViewController(vc, animated: true)
                        }
                        
                        else
                        {
                            self?.view.makeToast(message, duration: 3.0, position: .center)
                        }
                  
                  
                }

            if Reachability.isConnectedToNetwork() {
                        print("Internet connection OK")
                    } else {
                        print("Internet connection FAILED")
                        self?.view.makeToast("No Internet Connection,Make sure your device is connected to the internet. ".localized(), duration: 3.0, position: .top)
                    }
        
        
    }
        
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
        
    }
    
    @IBAction func resendcodeButtonAction(_ sender: UIButton) {
       
         self.showLoader()
         Api.resendCode(phone:phonenumber ?? ""){[weak self](error: String? , success: Bool, message) in
             self?.dismissLoader()
                 if success{
                     print("success")
                     self?.view.makeToast(message, duration: 3.0, position: .center)
   
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
}
