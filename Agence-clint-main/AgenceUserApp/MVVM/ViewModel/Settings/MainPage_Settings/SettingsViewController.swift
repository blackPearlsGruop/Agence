//
//  SettingsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit
import AlamofireImage

class SettingsViewController: UIViewController, NavigationDelegateProtocollogout, NavigationDelegateProtocolremove {
    func pushViewController() {
        let storyboard = UIStoryboard(name: "Login", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    var getprofileinformation:General?
    var settingsArr : [String] = []
    var settingsArrImages:[String] = []
    @IBOutlet weak var settingsTableView: UITableView!
    @IBOutlet weak var settinglable: UILabel!
    @IBOutlet weak var logo: UIImageView!
    @IBOutlet weak var namelable: UILabel!
    @IBOutlet weak var backButton:UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
      
        settingsArr = ["Profile".localized(),"Language".localized(),"Orders".localized(),"Favourite".localized(),"Previous Bills".localized(),"Contact Us".localized(),"Terms And Conditions".localized(),"Remove Account".localized(),"Log Out".localized()]
        settingsArrImages = [   "profile","lang","orders","fav","bill","contact","terms","deleteaccount","logout"]
        settingsTableView.delegate = self
        settingsTableView.dataSource = self
        settinglable.text = "Settings".localized()
        namelable.textAlignment = .center
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
       
    }
  
    override func viewWillAppear(_ animated: Bool) {
        getprofileFunc()
    }
    private func setAppSemantic() {
        // app direction
        if Localize.isCurrentLanguageEnglish(){
            UIView.appearance().semanticContentAttribute = .forceLeftToRight
            UILabel.appearance().semanticContentAttribute = .forceLeftToRight
            UITextField.appearance().textAlignment = .left
            UITextView.appearance().textAlignment = .left
            UITableView.appearance().semanticContentAttribute = .forceLeftToRight
            UITabBar.appearance().semanticContentAttribute = .forceLeftToRight
        } else {
            UIView.appearance().semanticContentAttribute = .forceRightToLeft
            UILabel.appearance().semanticContentAttribute = .forceRightToLeft
            UITextField.appearance().textAlignment = .right
            UITextView.appearance().textAlignment = .right
            UITableView.appearance().semanticContentAttribute = .forceRightToLeft
            UITabBar.appearance().semanticContentAttribute = .forceRightToLeft
        }
    }
    
    
    func gotoHome() {
        DispatchQueue.main.async {
            let window = UIApplication.shared.windows.filter({return $0.isKeyWindow}).first!
            let storyboard = UIStoryboard(name: "Home", bundle: nil)
            let navVC = storyboard.instantiateViewController(identifier: "homeNavigation") as! UINavigationController
            window.rootViewController = navVC
            if let snapshot = window.snapshotView(afterScreenUpdates: true) {
                navVC.view.addSubview(snapshot)
                window.rootViewController = navVC
                window.makeKeyAndVisible()
                
                UIView.animate(withDuration: 0.4, animations: {
                    snapshot.layer.opacity = 0
                }, completion: { _ in
                    snapshot.removeFromSuperview()
                })
            }
        }
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
  
    
}

extension SettingsViewController:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return settingsArr.count
   
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = settingsTableView.dequeueReusableCell(withIdentifier: "SettingsTableViewCell", for: indexPath) as!
        SettingsTableViewCell
        cell.settingtitle.text =  settingsArr[indexPath.row]
        cell.settingimg.image = UIImage(named:settingsArrImages[indexPath.row])
        
        if(Localize.currentLanguage().contains("ar") == true){
            cell.arrow.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
            
           
                  } else{
                    print("english")
       
                  }

        cell.selectionStyle = .none
        
        return cell
        
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60.0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        switch indexPath.row  {
        case 0://Profile
            print("Profile")
            let storyboard =  UIStoryboard(name:"Settings", bundle: nil)
            let userlogin = storyboard.instantiateViewController(withIdentifier:"ProfileViewController") as! ProfileViewController
            self.navigationController?.pushViewController(userlogin, animated: true)
        case 1:// Language
            
            print("Language")
              let optionMenu = UIAlertController(title: "Change Language".localized(), message: nil, preferredStyle: .actionSheet)
              
              let action1 = UIAlertAction(title: "English", style: .default, handler: {
                  [unowned self] (alert: UIAlertAction!) -> Void in
                 
                      Localize.setCurrentLanguage("en")
                      if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
                          appDelegate.setUIElementsAppearanceWith(semanticContent: .forceLeftToRight)
                          Localizer.DoTheExhange()
                          self.setAppSemantic()
                          
                      }
                      
                      gotoHome()
                      
                  
  
              })
              let action2 = UIAlertAction(title: "العربية".localized(), style: .default, handler: {
                  [unowned self] (alert: UIAlertAction!) -> Void in
                 
                      Localize.setCurrentLanguage("ar")
                      if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
                          appDelegate.setUIElementsAppearanceWith(semanticContent: .forceRightToLeft)
                          
                          Localizer.DoTheExhange()
                          self.setAppSemantic()
                      }
                      gotoHome()
                  
                  
                
                  
              })
              
              
              
              
              let cancelAction = UIAlertAction(title: "Cancel".localized(), style: .cancel, handler: nil)
              
              optionMenu.addAction(action1)
              optionMenu.addAction(action2)
              optionMenu.addAction(cancelAction)
              present(optionMenu, animated: true, completion: nil)
          
          
            
        case 2: // Orders
            print("Orders")
            let storyboard =  UIStoryboard(name:"Settings", bundle: nil)
            let userlogin = storyboard.instantiateViewController(withIdentifier:"SettingsOrdersViewController") as! SettingsOrdersViewController
            self.navigationController?.pushViewController(userlogin, animated: true)
        case 3: // Favourite
            print("Favourite")
            let storyboard =  UIStoryboard(name:"Settings", bundle: nil)
            let userlogin = storyboard.instantiateViewController(withIdentifier:"FavouriteViewController") as! FavouriteViewController
            self.navigationController?.pushViewController(userlogin, animated: true)
        case 4: // Previous Bills
            print("Previous Bills")
            let storyboard =  UIStoryboard(name:"Settings", bundle: nil)
            let userlogin = storyboard.instantiateViewController(withIdentifier:"PreviosBillsViewController") as! PreviosBillsViewController
            self.navigationController?.pushViewController(userlogin, animated: true)
            
        case 5: // Contact Us
            print("Contact Us")
            let storyboard =  UIStoryboard(name:"Settings", bundle: nil)
            let userlogin = storyboard.instantiateViewController(withIdentifier:"ContactUsViewController") as! ContactUsViewController
            self.navigationController?.pushViewController(userlogin, animated: true)
        case 6: // Terms And Conditions
            print("Terms And Conditions")
            let storyboard =  UIStoryboard(name:"Settings", bundle: nil)
            let userlogin = storyboard.instantiateViewController(withIdentifier:"TermsAndConditionsViewController") as! TermsAndConditionsViewController
            self.navigationController?.pushViewController(userlogin, animated: true)
        case 7: // remove account
            print("Remove Account")
            let storyboard = UIStoryboard(name: "Login", bundle:nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "RemoveAccountViewController") as! RemoveAccountViewController
            vc.delegate = self
            vc.modalPresentationStyle = .overCurrentContext
            self.present(vc, animated: true, completion: nil)
        case 8:// sign out
            print("Sign Out")
            let storyboard = UIStoryboard(name: "Login", bundle:nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "LogOutViewController") as! LogOutViewController
            vc.delegate = self
            vc.modalPresentationStyle = .overCurrentContext
            self.present(vc, animated: true, completion: nil)
            
        default:
            break
        }
    }
    
    
}


extension SettingsViewController{
    func getprofileFunc()
    {
        showLoader()
        Api.GetProfileData{ [weak self](error: Error?, getprofile: General?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getprofiledata = getprofile{
                self?.getprofileinformation = getprofiledata
                
                let name = self?.getprofileinformation?.name
                self?.namelable.text = name
              
                //handle image
if let url = URL(string:self?.getprofileinformation?.image ?? ""){
                    print("\(url)")
 
            self?.logo.af.setImage(withURL: url)
                }
                
                else {
                    self?.logo.image = UIImage(named:"")
                }
                
                
                
                
                
                
                
            }
            
            if success{
                print("success")
                self?.view.makeToast(message, duration: 3.0, position: .center)
                
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
