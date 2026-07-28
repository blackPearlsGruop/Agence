//
//  ConsultationHourViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/02/2024.
//

import UIKit

class ConsultationHourViewController: UIViewController {
    @IBOutlet weak var hourlabel:UILabel!
    @IBOutlet weak var determinehourlabel:UILabel!
    @IBOutlet weak var pricetextfield:UITextField!
    @IBOutlet weak var savebutton:UIButton!
    @IBOutlet weak var backButton:UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        savebutton.setTitle("Save".localized(), for: .normal)
        pricetextfield.placeholder = "Price per hour for consultation".localized()
        hourlabel.text = "Price per hour for consultation".localized()
        determinehourlabel.text = "Determine an hourly value for consultation".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    

    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    @IBAction func saveButtonAction(_ sender: UIButton) {
       
        showLoader()
        Api.pricePerHour(consultant_price:pricetextfield.text ?? ""){[weak self] (error : Error? , success : Bool , message) in
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
    

}
