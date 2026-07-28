//
//  PreviosBillsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/06/2024.
//

import UIKit

class PreviosBillsViewController: UIViewController {
    var FinishordersArr = [General]()
    @IBOutlet weak var previousTableView: UITableView!
    @IBOutlet weak var previouslabel: UILabel!
    @IBOutlet weak var Noorderlabel: UILabel!
    @IBOutlet weak var backButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        previousTableView.delegate = self
        previousTableView.dataSource = self
        previouslabel.text = "Previous Bills".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
        Noorderlabel.isHidden =  true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getCloseOrder()
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    }
  



extension PreviosBillsViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
            return FinishordersArr.count
        
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       
            let cell = previousTableView.dequeueReusableCell(withIdentifier: "PreviousBillsTableViewCell", for: indexPath) as!
            PreviousBillsTableViewCell
        cell.ordertitle.text =  FinishordersArr[indexPath.row].title
        cell.ordernumber.text =  FinishordersArr[indexPath.row].order_number
        cell.categoryname.text =  FinishordersArr[indexPath.row].company_name
        cell.categorydate.text =  FinishordersArr[indexPath.row].created_at
        
         //view
          cell.actionview = { [self] in
              let storyboard = UIStoryboard(name: "Settings", bundle:nil)
              let vc = storyboard.instantiateViewController(withIdentifier: "BillDetailsViewController") as! BillDetailsViewController
              vc.orderid = FinishordersArr[indexPath.row].orderid
              vc.companyid = FinishordersArr[indexPath.row].companyid
              vc.taxvalue =  FinishordersArr[indexPath.row].ordertax
              vc.pricevalue = FinishordersArr[indexPath.row].orderprice
              self.navigationController?.pushViewController(vc, animated: true)
          }
        cell.selectionStyle = .none
         
            return cell
        
        

    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 120.0
    }

}

extension PreviosBillsViewController{
    //getcloseorder
    func getCloseOrder(){
        showLoader()

        Api.AllCloseOrder{[weak self](error: Error?,getcloseorder:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getClose = getcloseorder {
                self?.FinishordersArr = getClose
                self?.previousTableView.reloadData()
            }
          
            if self?.FinishordersArr.count == 0 {
                self?.Noorderlabel.isHidden = false
            }
            else{
                self?.Noorderlabel.isHidden = true
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
