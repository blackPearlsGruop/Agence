//
//  ChoosePageViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 12/02/2024.
//

import UIKit

class ChoosePageViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func ClientButtonAction(_ sender: UIButton) {
     
        let storyboard = UIStoryboard(name: "Login", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    @IBAction func ProviderButtonAction(_ sender: UIButton) {
     
        let storyboard = UIStoryboard(name: "Login", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    @IBAction func GuestButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Login", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }

}
