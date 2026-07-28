//
//  ChancesDetailsViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit

class ChancesDetailsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    @IBAction func sendvalueButtonAction(_ sender: UIButton) {
    
        let storyboard = UIStoryboard(name:"Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "SendValuePopUpViewController") as! SendValuePopUpViewController
        vc.modalPresentationStyle = .overCurrentContext
        self.present(vc, animated: true, completion: nil)
    }


}
