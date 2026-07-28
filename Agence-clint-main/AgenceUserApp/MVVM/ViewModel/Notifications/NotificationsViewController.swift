//
//  NotificationsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit

class NotificationsViewController:UIViewController {
    var notifyArr:[General] = []
    var pageNo:Int = 1
    var ispageRefreshing:Bool?
    let refreshControl = UIRefreshControl()
    @IBOutlet weak var notifylabel: UILabel!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var notifyTableView: UITableView!
    @IBOutlet weak var emptylabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        notifylabel.text = "Notifications".localized()
        emptylabel.text = "No Notifications Founded".localized()
        notifyTableView.delegate = self
        notifyTableView.dataSource = self
        notifyTableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: notifyTableView.frame.size.width, height: 1))
        
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
        
        //refresh
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh".localized())
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        notifyTableView.addSubview(refreshControl)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        emptylabel.isHidden = true
        allnotifications()
    }
    @objc func refresh(_ sender: AnyObject) {
        allnotifications()
        refreshControl.endRefreshing()
    }
    //handle allnotifications
    func allnotifications(){
        showLoader()
        Api.AllNotifications(page:pageNo,count_paginate:10){[weak self](error: Error?,getnotify:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getnotifications = getnotify {
                self?.notifyArr.append(contentsOf:getnotifications)
                self?.notifyTableView.reloadData()
            }
            if (self?.notifyArr.count ?? 0) > 0{
                self?.ispageRefreshing = false
                self?.pageNo += 1
            }
            
            if  self?.notifyArr.count == 0 {
                self?.emptylabel.isHidden = false
            }
            else{
                self?.emptylabel.isHidden = true
              
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
            
            if Reachability.isConnectedToNetwork() {
                print("Internet connection OK")
            } else {
                print("Internet connection FAILED")
                self?.view.makeToast("No Internet Connection,Make sure your device is connected to the internet. ", duration: 3.0, position: .top)
                
            }
        }
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
  


}

extension NotificationsViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
            return notifyArr.count
        
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       
            let cell = notifyTableView.dequeueReusableCell(withIdentifier: "NotificationsTableViewCell", for: indexPath) as!
            NotificationsTableViewCell
        cell.notifytitlelabel.text = notifyArr[indexPath.row].title
        cell.notifydescribelabel.text = notifyArr[indexPath.row].describeData
        cell.notifydatelabel.text = notifyArr[indexPath.row].created_at_for_humans
      
            cell.selectionStyle = .none
            return cell
        
        

    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 90.0
    }

}
