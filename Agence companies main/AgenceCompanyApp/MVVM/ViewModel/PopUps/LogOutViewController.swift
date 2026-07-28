//
//  LogOutViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//
import UIKit
protocol NavigationDelegateProtocollogout:AnyObject{
    func pushViewController()
   
}
class LogOutViewController: UIViewController {
    weak var delegate:NavigationDelegateProtocollogout?
    var getprofileinformation:General?
    @IBOutlet weak var confirmbutton: UIButton!
    @IBOutlet weak var logouttitle: UILabel!
    @IBOutlet weak var welcometitle: UILabel!
    @IBOutlet weak var suretitle: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        confirmbutton.setTitle("Confirm".localized(), for: .normal)
      
        suretitle.text = "Are you sure you will go to login?".localized()
        logouttitle.text = "Log Out".localized()
      
    }
    override func viewWillAppear(_ animated: Bool) {
        getprofileFunc()
    }
    func getprofileFunc()
    {
        Api.GetProfileData{ [weak self](error: Error?, getprofile: General?,success,message) in
            
            if error != nil
            {
                print(error as Any)
                
            }
            
            
            if let getprofiledata = getprofile{
                self?.getprofileinformation = getprofiledata
                
                let name = self?.getprofileinformation?.companyname
                self?.welcometitle.text = "Welcome".localized() + " "
                + name!
                
            }
            
        }
        
    }
    @IBAction func dismissBut(_ sender: Any) {
        self.dismiss(animated: true)
    }
    @IBAction func confiemBut(_ sender: Any) {
        print("sign out")
        Helper.removeId()
        Helper.removeAccessToken()
        self.dismiss(animated: true) {
            self.delegate?.pushViewController()
        }
    }
}
