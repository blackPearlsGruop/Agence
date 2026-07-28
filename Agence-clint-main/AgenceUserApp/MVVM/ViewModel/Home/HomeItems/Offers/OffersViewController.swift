//
//  OffersViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit

class OffersViewController: UIViewController {
    @IBOutlet weak var offerTableView: UITableView!
    @IBOutlet weak var offerlabel: UILabel!
    @IBOutlet weak var Noofferlabel: UILabel!
    var offerArr = [General]()
    var orderId:Int?
    override func viewDidLoad() {
        super.viewDidLoad()
        offerTableView.delegate = self
        offerTableView.dataSource = self
        offerTableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: offerTableView.frame.size.width, height: 1))
        offerlabel.text = "Offers".localized()
        Noofferlabel.text = "No Offers Available".localized()
        Noofferlabel.isHidden =  true
    }
   
    override func viewWillAppear(_ animated: Bool) {
        AllOffer()
    }


}

extension OffersViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
            return offerArr.count
        
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = offerTableView.dequeueReusableCell(withIdentifier: "OffersTableViewCell", for: indexPath) as!
        OffersTableViewCell
        cell.offername.text = offerArr[indexPath.row].categorytitle
        cell.offerdescribe.text = offerArr[indexPath.row].categorydescribe
        cell.offerprice.text =
        "\(offerArr[indexPath.row].price_start_from)"  + " " + "RS".localized()
        //offerArr[indexPath.row].price + " " + "RS".localized()
        //        if offerArr[indexPath.row].price == ""  {
        //
        //            cell.offerprice.text =  "1000" + " " + "RS".localized()
        //        }
        cell.ordernumber.text = offerArr[indexPath.row].order_number
        //shdow for view
        orderId = offerArr[indexPath.row].id
        cell.offerview.layer.masksToBounds = false
        cell.offerview.layer.shadowRadius = 3.0
        cell.offerview.layer.shadowColor = UIColor.gray.cgColor
        cell.offerview.layer.shadowOffset =  CGSize(width: 1, height: 3)
        cell.offerview.layer.shadowOpacity = 0.3
        cell.selectionStyle = .none
        
        cell.actionview = { [self] in
            let storyboard = UIStoryboard(name: "Home", bundle:nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "OfferDetailsViewController") as! OfferDetailsViewController
            vc.orderid = offerArr[indexPath.row].id
            self.navigationController?.pushViewController(vc, animated: true)
        }
        
        cell.actionaccept = { [self] in
            orderId = offerArr[indexPath.row].id
            self.showLoader()
            Api.AcceptOffer(orderId:orderId ?? 0, payment_method:"online-payment"){[weak self] (error : String? , success : Bool , message) in
                self?.dismissLoader()
                if success {
                
                        self?.view.makeToast(message, duration: 3.0, position: .center)
                    self?.AllOffer()
                  
                } else {
                    
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
         
        cell.actionrefuse =
        { [self] in
            orderId = offerArr[indexPath.row].id
            self.showLoader()
            Api.RefuseOffer(orderId:orderId ?? 0){[weak self] (error : String? , success : Bool , message) in
                self?.dismissLoader()
                if success {
                
                        self?.view.makeToast(message, duration: 3.0, position: .center)
                    self?.AllOffer()
                  
                } else {
                    
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
            return cell
        
        

    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 250.0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "OfferDetailsViewController") as! OfferDetailsViewController
        vc.orderid = offerArr[indexPath.row].id
        self.navigationController?.pushViewController(vc, animated: true)
    }

}


extension OffersViewController{
   
    //alloffer
    func AllOffer(){
        showLoader()

        Api.AllOffer{[weak self](error: Error?,getoffer:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let alloffer = getoffer {
                self?.offerArr = alloffer
                self?.offerTableView.reloadData()
            }
          
            if self?.offerArr.count == 0 {
                self?.Noofferlabel.isHidden = false
            }
            else{
                self?.Noofferlabel.isHidden = true
            }
            if success{
                print("success")
                print(message)
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
                    print(message)
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
}
