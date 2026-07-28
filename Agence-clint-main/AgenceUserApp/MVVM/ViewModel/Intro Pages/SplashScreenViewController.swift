//
//  SplashScreenViewController.swift
//  Salon_App
//
//  Created by Eng Yoka on 04/02/2024.
//


import UIKit

class SplashScreenViewController: UIViewController, UIViewControllerTransitioningDelegate {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = true
        navigationController?.interactivePopGestureRecognizer?.isEnabled = false
        if  Helper.getApiToken(key:"isFirstlaunch") == nil{
            self.perform(#selector(self.presentWithCustomAnimation), with: nil, afterDelay: 3)
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
    
    //animation function
    @objc func presentWithCustomAnimation (){
        let storyboard =  UIStoryboard(name:"Start", bundle: nil)
        let userlogin = storyboard.instantiateViewController(withIdentifier: "FirstPageViewController") as! FirstPageViewController
        self.navigationController?.pushViewController(userlogin, animated: true)
    
}
}
