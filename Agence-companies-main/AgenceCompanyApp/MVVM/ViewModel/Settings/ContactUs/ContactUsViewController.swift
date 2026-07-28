//
//  ContactUsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 24/02/2024.
//

import UIKit

class ContactUsViewController: UIViewController {
    @IBOutlet weak var titletextfield:UITextField!
    @IBOutlet weak var messagetextfield:UITextField!
    @IBOutlet weak var contactlabel:UILabel!
    @IBOutlet weak var whatslabel:UILabel!
    @IBOutlet weak var backButton:UIButton!
    @IBOutlet weak var orangebackButton:UIButton!
    @IBOutlet weak var sendButton:UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        sendButton.setTitle("Send".localized(), for: .normal)
        titletextfield.placeholder = "Message Title".localized()
        messagetextfield.placeholder = "Message Content".localized()
        contactlabel.text = "Contact Us".localized()
        whatslabel.text = "WhatsApp".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
           
        } else{
            print("english")
         
        }
    }
 
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    @IBAction func sendButtonAction(_ sender: UIButton) {
       
        showLoader()
        Api.contactUs(title:titletextfield.text ?? "", descriptiondata:messagetextfield.text ?? "") {[weak self] (error : Error? , success : Bool , message) in
            self?.dismissLoader()
            if success {
                self?.view.makeToast(message, duration: 3.0, position: .center)
               
                DispatchQueue.main.asyncAfter(deadline: .now()+1) {
                    self?.navigationController?.popViewController(animated:true)
                }
                
            }
            
            else{
                
            if message  == "Unauthorized".localized(){
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
            
        }
    }
    
    
    @IBAction func openwhatssappButtonAction(_ sender: UIButton) {
        let phoneNumber =  "+989160000000" // you need to change this number
        let appURL = URL(string: "https://wa.me/\(phoneNumber)")!
        if UIApplication.shared.canOpenURL(appURL) {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(appURL, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(appURL)
            }
        }
      
    }

}
