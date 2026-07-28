//
//  TermsAndConditionsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 24/02/2024.
//

import UIKit

class TermsAndConditionsViewController: UIViewController {
    @IBOutlet var termstextview: UITextView!
    @IBOutlet var termlabel: UILabel!
    @IBOutlet var backButton: UIButton!
    var termsdata:General?
    override func viewDidLoad() {
        super.viewDidLoad()
        termlabel.text = "Terms And Conditions".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
           
        } else{
            print("english")
            
        }
        TermsFunc()
    }
    

    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    //handle termsandconditions
    func TermsFunc()
    {

        showLoader()
        Api.TermsAndConditions{ [weak self](error: Error?, terms: General?) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let termsdata = terms{
                self?.termsdata = termsdata

                let termsdescription = self?.termsdata?.describeData
               

                self?.termstextview.text = termsdescription?.html2String
              
               
            }
        }
    }
    
    

}
