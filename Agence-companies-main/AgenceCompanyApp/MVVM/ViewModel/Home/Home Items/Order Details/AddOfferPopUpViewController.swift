//
//  AddOfferPopUpViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit

class AddOfferPopUpViewController: UIViewController {
    var orderId:Int?
    @IBOutlet weak var addofferlabel:UILabel!
    @IBOutlet weak var amountlabel:UILabel!
    @IBOutlet weak var amounttextfield:UITextField!
    @IBOutlet weak var sendButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        addofferlabel.text = "Add Offer".localized()
        amountlabel.text = "Amount".localized()
        amounttextfield.placeholder = "Enter amount".localized()
        amounttextfield.addPadding(.both(20))
        sendButton.setTitle("Send".localized(), for:.normal)
    }
  
    @IBAction func dismissBut(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @IBAction func addbuttonButton(_ sender: UIButton) {
      
        self.showLoader()
        Api.AddOffer(order_id:orderId ?? 0, price:Int(amounttextfield.text ?? "") ?? 0 ){[weak self] (error : String? , success : Bool , message) in
            self?.dismissLoader()
            if success {
            
                self?.view.makeToast(message, duration: 3.0, position: .center)
              
              
            } else {
                
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

}
