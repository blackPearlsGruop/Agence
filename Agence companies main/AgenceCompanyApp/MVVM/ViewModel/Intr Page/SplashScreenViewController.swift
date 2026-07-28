//
//  SplashScreenViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 12/02/2024.
//

import UIKit

class SplashScreenViewController: UIViewController, UIViewControllerTransitioningDelegate {
    
    override func viewDidLoad() {
        super.viewDidLoad()
self.navigationController?.navigationBar.isHidden = true
        navigationController?.interactivePopGestureRecognizer?.isEnabled = false
        if  Helper.getApiToken(key:"isFirstlaunch") == nil{
        self.perform(#selector(self.presentWithCustomAnimation), with: nil, afterDelay:1.75)
        }

        else{
            if Helper.getApiToken(key:"token") == nil
            {
                //login page
                let storyboard =  UIStoryboard(name: "Login", bundle: nil)
                let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
                self.navigationController?.pushViewController(vc, animated: true)

            }
            else{
                print("home")
                //home page
                let storyboard =  UIStoryboard(name: "Home", bundle: nil)
                let userlogin = storyboard.instantiateViewController(withIdentifier: "HomeTabBar") as! HomeTabBar
                navigationController?.pushViewController(userlogin, animated: true)
            }
        }

    }
    
    override func viewWillAppear(_ animated: Bool) {
        
    }
    
    //animation function
    @objc func presentWithCustomAnimation (){
        let storyboard =  UIStoryboard(name:"Login", bundle: nil)
        let userlogin = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        self.navigationController?.pushViewController(userlogin, animated: true)
//    print("home")
                //home page
//            let storyboard =  UIStoryboard(name:"Home", bundle: nil)
//        let userlogin = storyboard.instantiateViewController(withIdentifier: "HomeTabBar") as! HomeTabBar
//        self.navigationController?.pushViewController(userlogin, animated: true)
}
}
