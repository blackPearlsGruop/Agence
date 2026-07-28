//
//  WalletViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 13/02/2024.
//

import UIKit

class WalletViewController: UIViewController {
    @IBOutlet weak var walletlabel: UILabel!
    @IBOutlet weak var availablebalancelabelloc: UILabel!
    @IBOutlet weak var availablebalancelabel: UILabel!
    @IBOutlet weak var usernameTF: UITextField!
    @IBOutlet weak var banknameTF: UITextField!
    @IBOutlet weak var accountnumberTF: UITextField!
    @IBOutlet weak var ibannumberTF: UITextField!
    @IBOutlet weak var balancebutton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        availablebalancelabel.text = "0.00" + " " + "RS".localized()
        availablebalancelabelloc.text = "Available Balance".localized()
        walletlabel.text = "Wallet".localized()
        usernameTF.placeholder = "User Name".localized()
        banknameTF.placeholder = "Bank Name".localized()
accountnumberTF.placeholder = "Account Number".localized()
ibannumberTF.placeholder = "Iban Number".localized()
    balancebutton.setTitle("Balance Withdrawal".localized() ,for:.normal)
    }
    @IBAction func balanceButtonAction(_ sender: UIButton) {
        
        self.showLoader()
        Api.AddToWallet(name:usernameTF.text ?? "", bank_account: banknameTF.text ?? "", bank_account_number: accountnumberTF.text ?? "", iban_number:ibannumberTF.text ?? ""){[weak self] (error : String? , success : Bool , message) in
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
