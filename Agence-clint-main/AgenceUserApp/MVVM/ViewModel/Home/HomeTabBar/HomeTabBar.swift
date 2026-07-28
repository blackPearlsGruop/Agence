//
//  HomeTabBar.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 13/02/2024.
//
//

import UIKit
import Floaty
@IBDesignable
class HomeTabBar: UITabBarController , UITabBarControllerDelegate{
   //  var count:Home?
    var Floatbtn = UIButton(type: .custom)
    public  let coustmeTabBarView:UIView = {
      
        //  daclare coustmeTabBarView as view
        let view = UIView(frame: .zero)
        
        // to make the cornerRadius of coustmeTabBarView
        view.backgroundColor = .white
        //        view.layer.cornerRadius = 20
        //        view.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        //        view.clipsToBounds = true
        
        // to make the shadow of coustmeTabBarView
        view.layer.masksToBounds = false
        view.layer.shadowColor = UIColor.gray.cgColor
        view.layer.shadowOffset = CGSize(width: 1, height: 1)
        view.layer.shadowOpacity = 0.5
        view.layer.shadowRadius = 10.0
        return view
    }()
   
    override func viewDidLoad() {
        super.viewDidLoad()
        self.delegate = self
       
      
        // tabBar.layer.cornerRadius = 20
        addcoustmeTabBarView()
        hideTabBarBorder()
//        let floaty = Floaty()
//        floaty.addItem(title: "Hello, World!")
//        self.view.addSubview(floaty)
        //create a new button
        let button = UIButton(type: .custom)
        //set image for button
        button.setImage(UIImage(named:"notifi"), for: .normal)
        //add function for button
        button.addTarget(self, action: #selector(notifyButton), for: .touchUpInside)
        //set frame
        button.frame = CGRect(x: 0, y: 0, width: 10, height: 10)
        
        let barButton = UIBarButtonItem(customView: button)
        //assign button to navigationbar
        self.navigationItem.leftBarButtonItem = barButton
        
        
        //create a new button
        let button2 = UIButton(type: .custom)
        //set image for button
        button2.setImage(UIImage(named:"loop"), for: .normal)
    
        //add function for button
        button2.addTarget(self, action: #selector(profileButton), for: .touchUpInside)
        //set frame
        button2.frame = CGRect(x: 0, y: 0, width: 10, height: 10)
        
        let barButton2 = UIBarButtonItem(customView: button2)
        //assign button to navigationbar
        self.navigationItem.rightBarButtonItem = barButton2
        
        UITabBarItem.appearance().setTitleTextAttributes([NSAttributedString.Key.font: UIFont(name:"Somar-Bold", size:22)!], for: .normal)
        setText()
       
        
    }
    


    func setFloatingButton(){
      
        Floatbtn.frame = CGRect(x: 20, y: 10, width: 130, height: 50)
        Floatbtn.frame.origin = CGPoint(x:10, y:self.view.frame.size.height - Floatbtn.frame.size.height - 140)

        Floatbtn.titleLabel?.font =  UIFont(name:"Somar-Bold",size:25)
        Floatbtn.imageEdgeInsets = UIEdgeInsets(top: 0, left: 10, bottom: 0, right:10)
        Floatbtn.setTitle("  " + "Quick Order".localized(), for: .normal)
        Floatbtn.backgroundColor = UIColor.blue
        Floatbtn.setImage(UIImage(named:"plus"), for:.normal)
        Floatbtn.clipsToBounds = true
        Floatbtn.layer.cornerRadius = 16
        Floatbtn.addTarget(self, action: #selector(fastorderButton), for: .touchUpInside)
        if let window = UIApplication.shared.keyWindow {
            window.addSubview(Floatbtn)
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
    Floatbtn.removeFromSuperview()
    }
//    
   
    override func viewWillAppear(_ animated: Bool) {
        
        super.viewWillAppear(animated)
         // getCountFunc()
        setFloatingButton()
        self.navigationController?.navigationBar.isHidden = false
    }
   
   
 
    
    
    //handle count notification
    //func getCountFunc()
//    {
//        Api.CountNotifications{ [weak self](error: Error?, countnotify: Home?) in
//            if error != nil
//            {
//                print(error as Any)
//                
//            }
//    
//            if let getscount = countnotify{
//                self?.count = getscount
//                let countname = "\(Int((self?.count!.notifications_count)!))"
//                print(countname)
//                self?.addBadge(itemvalue:countname ?? "")
//   
//            }
//            else{
//                self?.addBadge(itemvalue:"0")
//            }
//            
//        }
//    }
 
    
    func addBadge(itemvalue: String) {
        
        let bagButton = BadgeButton()
        bagButton.frame = CGRect(x: 0, y: 0, width: 44, height: 44)
        bagButton.tintColor = UIColor.white
        //bagButton.setImage(UIImage(named: "notify")?.withRenderingMode(.alwaysTemplate), for: .normal)
        bagButton.setImage(UIImage(named:"notification"), for: .normal)
        bagButton.badgeEdgeInsets = UIEdgeInsets(top: 20, left: 0, bottom: 0, right: 10)
        bagButton.badge = itemvalue
        //add function for button
        bagButton.addTarget(self, action: #selector(notifyButton), for: .touchUpInside)
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(customView: bagButton)
    }
   
    
    
    //handle count notification
    //func getCountFunc()
    //    {
    //        Api.CountNotifications{ [weak self](error: Error?, countnotify: Notify?) in
    //            if error != nil
    //            {
    //                print(error as Any)
    //
    //            }
    //
    //                if let getscount = countnotify{
    //                    self?.count = getscount
    //                    let countname = "\(Int((self?.count!.notifications_count)!))"
    //                    print(countname)
    //                    self?.addBadge(itemvalue:countname ?? "")
    //
    //
    //            }
    //
    //
    //            else{
    //                self?.addBadge(itemvalue:"0")
    //            }
    //
    //        }
    //    }
    @objc func setText() {
        tabBar.items?[0].title = "Home".localized()
        tabBar.items?[1].title = "Offers".localized()
        tabBar.items?[2].title = "Orders".localized()
        tabBar.items?[3].title = "Chat".localized()
    }
    
   
    @IBAction func fastorderButton(_ sender: UIBarButtonItem) {
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "FastOrderViewController") as! FastOrderViewController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func profileButton(_ sender: UIBarButtonItem) {
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "SettingsViewController") as! SettingsViewController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func notifyButton(_ sender: UIBarButtonItem) {
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "NotificationsViewController") as! NotificationsViewController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    
    
    
    func hideTab (){
        self.coustmeTabBarView.alpha = 0
        self.tabBar.isHidden = true
        
    }
    
    func showTab (){
        self.coustmeTabBarView.alpha = 1
        self.tabBar.isHidden = false
        
    }

 
 
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        var tabFrame = self.tabBar.frame
        tabFrame.size.height = 100
        tabFrame.origin.y = self.view.frame.size.height - 100
        coustmeTabBarView.frame = tabFrame
        
        
    }
    
    //    override func viewWillLayoutSubviews() {
    //        var tabFrame = self.tabBar.frame
    //        // - 40 is editable , the default value is 49 px, below lowers the tabbar and above increases the tab bar size
    //        tabFrame.size.height = 200
    //       // tabFrame.origin.y = self.view.frame.size.height - 100
    //        self.tabBar.frame = tabFrame
    //    }
    
    
    private func addcoustmeTabBarView() {
        //
        coustmeTabBarView.frame = tabBar.frame
        view.addSubview(coustmeTabBarView)
        view.bringSubviewToFront(self.tabBar)
    }
    
    
    func hideTabBarBorder()  {
        let tabBar = self.tabBar
        tabBar.backgroundImage = UIImage.from(color: .clear)
        tabBar.shadowImage = UIImage()
        tabBar.clipsToBounds = true
        
    }
    
    
}


extension UIImage {
    static func from(color: UIColor) -> UIImage {
        let rect = CGRect(x: 0, y: 0, width: 1, height:1)
        UIGraphicsBeginImageContext(rect.size)
        let context = UIGraphicsGetCurrentContext()
        context!.setFillColor(color.cgColor)
        context!.fill(rect)
        let img = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return img!
    }
}
