//
//  BillViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//

import UIKit

class BillViewController: UIViewController {
    @IBOutlet weak var billOneloc: UILabel!
    @IBOutlet weak var billTwoloc: UILabel!
    @IBOutlet weak var companynameloc: UILabel!
    @IBOutlet weak var serviceloc: UILabel!
    @IBOutlet weak var amountbeforetaxloc: UILabel!
    @IBOutlet weak var taxloc: UILabel!
    @IBOutlet weak var totalloc: UILabel!
    @IBOutlet weak var choosepaymentloc: UILabel!
    @IBOutlet weak var visaloc: UILabel!
    @IBOutlet weak var walletloc: UILabel!
    @IBOutlet weak var paybutton: UIButton!
    @IBOutlet weak var backbutton: UIButton!
  
    override func viewDidLoad() {
        super.viewDidLoad()
      
        billOneloc.text = "Bill".localized()
        billTwoloc.text = "Bill".localized()
        companynameloc.text = "Company Name".localized()
        serviceloc.text = "Service".localized()
        amountbeforetaxloc.text = "Amount before tax".localized()
        taxloc.text = "Tax".localized()
        totalloc.text = "Total".localized()
        visaloc.text = "Visa".localized()
        walletloc.text = "Wallet".localized()
        choosepaymentloc.text = "Choose payment method".localized()
        paybutton.setTitle("Pay".localized(), for: .normal)
     
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    

    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    //transactionNo
    @IBAction func payButtonAction(_ sender: UIButton) {}
    
}



