//
//  RemoveAccountViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//


import UIKit
protocol NavigationDelegateProtocolremove:AnyObject{
    func pushViewController()
   
}
class RemoveAccountViewController: UIViewController {
    weak var delegate:NavigationDelegateProtocolremove?
    var getprofileinformation:General?
    @IBOutlet weak var Removebutton: UIButton!
    @IBOutlet weak var removettitle: UILabel!
    @IBOutlet weak var welcometitle: UILabel!
    @IBOutlet weak var suretitle: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        suretitle.text = "Are you sure you want to remove account ?".localized()
        removettitle.text = "Remove Account".localized()
        Removebutton.setTitle("Remove Account".localized(), for: .normal)
    
       
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
    @IBAction func removeBut(_ sender: Any) {
        print("remove account")
     
        
        showLoader()
        Api.RemoveAccount{[weak self](error:  Error? , success: Bool, message) in
            self?.dismissLoader()
            if success {
                self?.MAK_ShowToast(message:message ?? "")
                self?.dismiss(animated: true) {
                    self?.delegate?.pushViewController()
                }
            } else
            {
                self?.MAK_ShowToast(message:message ?? "")
            }
        }
    }
}
