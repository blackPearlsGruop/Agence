//
//  OrdersViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 13/02/2024.
//

import UIKit
import AlamofireImage
class OrdersViewController: UIViewController {
    
    var NewordersArr:[General]=[]
    var FinishordersArr:[General]=[]
    var WaitingordersArr:[General]=[]
    @IBOutlet weak var orderlabel: UILabel!
   @IBOutlet weak var neworderbutt:UIButton!
   @IBOutlet weak var expiredorderbutt:UIButton!
   @IBOutlet weak var waitingorderbutt:UIButton!
   @IBOutlet weak var finishordersTableView: UITableView!
   @IBOutlet weak var newordersTableView: UITableView!
   @IBOutlet weak var waitingordersTableView: UITableView!
    @IBOutlet weak var Noorderlabel: UILabel!
   override func viewDidLoad() {
       super.viewDidLoad()
       orderlabel.text = "Orders".localized()
       neworderbutt.setTitle("New Order".localized(), for: .normal)
       expiredorderbutt.setTitle("Expired Order".localized(), for: .normal)
       waitingorderbutt.setTitle("Progress Order".localized(), for: .normal)
       Noorderlabel.text = "No Orders Available".localized()
       
       finishordersTableView.delegate =  self
       finishordersTableView.dataSource =  self
       
       newordersTableView.delegate =  self
       newordersTableView.dataSource =  self
       
       waitingordersTableView.delegate =  self
       waitingordersTableView.dataSource =  self
       
    
   }
   
   override func viewWillAppear(_ animated: Bool) {
      getNewOrder()
       Noorderlabel.isHidden = true
       newordersTableView.isHidden = false
       finishordersTableView.isHidden = true
       waitingordersTableView.isHidden = true
    
       neworderbutt.setTitleColor(UIColor.white, for: .normal)
       neworderbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
    
       
       expiredorderbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
       expiredorderbutt.backgroundColor = UIColor.white
      
       
       waitingorderbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
       waitingorderbutt.backgroundColor = UIColor.white
     
   }
   

   
   @IBAction func neworderButtonAction(_ sender: UIButton) {
       getNewOrder()
       neworderbutt.setTitleColor(UIColor.white, for: .normal)
       neworderbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
    
       
       expiredorderbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
       expiredorderbutt.backgroundColor = UIColor.white
       
       waitingorderbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
       waitingorderbutt.backgroundColor = UIColor.white
     
       newordersTableView.isHidden = false
       finishordersTableView.isHidden = true
       waitingordersTableView.isHidden = true
   }
   
   @IBAction func expiredorderButtonAction(_ sender: UIButton) {
      getCloseOrder()
       expiredorderbutt.setTitleColor(UIColor.white, for: .normal)
       expiredorderbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
     
       
       neworderbutt.setTitleColor(hexStringToUIColor(hex:"#2505ED"), for: .normal)
       neworderbutt.backgroundColor = UIColor.white
       
       waitingorderbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
       waitingorderbutt.backgroundColor = UIColor.white
     
       finishordersTableView.isHidden = false
       newordersTableView.isHidden = true
       waitingordersTableView.isHidden = true
   }
    
    @IBAction func waitingorderButtonAction(_ sender: UIButton) {
       getWaitOrder()
        waitingorderbutt.setTitleColor(UIColor.white, for: .normal)
        waitingorderbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
      
        
        neworderbutt.setTitleColor(hexStringToUIColor(hex:"#2505ED"), for: .normal)
        neworderbutt.backgroundColor = UIColor.white
        
        expiredorderbutt.setTitleColor(hexStringToUIColor(hex:"#2505ED"), for: .normal)
        expiredorderbutt.backgroundColor = UIColor.white
        
        waitingordersTableView.isHidden = false
        finishordersTableView.isHidden = true
        newordersTableView.isHidden = true
     
    }
   
   
   func hexStringToUIColor (hex:String) -> UIColor {
       var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()

       if (cString.hasPrefix("#")) {
           cString.remove(at: cString.startIndex)
       }

       if ((cString.count) != 6) {
           return UIColor.gray
       }

       var rgbValue:UInt64 = 0
       Scanner(string: cString).scanHexInt64(&rgbValue)

       return UIColor(
           red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
           green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
           blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
           alpha: CGFloat(1.0)
       )
   }
   

   
}
extension OrdersViewController:UITableViewDelegate,UITableViewDataSource{
       func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
           if tableView ==  finishordersTableView {
               return FinishordersArr.count
           }
           if tableView ==  newordersTableView {
               return NewordersArr.count
           }
           else{
               return WaitingordersArr.count
           }
           
           
       }
       
       func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
           if tableView ==  finishordersTableView {
               let cell = finishordersTableView.dequeueReusableCell(withIdentifier: "FinishOrdersTableViewCell", for: indexPath) as!
               FinishOrdersTableViewCell
               cell.categorytitle.text =  FinishordersArr[indexPath.row].categorytitle
               cell.ordertitle.text =  FinishordersArr[indexPath.row].title
               cell.ordertype.text =  FinishordersArr[indexPath.row].order_type
               cell.ordernumber.text =  "Order number:".localized() + " " +  FinishordersArr[indexPath.row].order_number
               cell.orderdate.text =  FinishordersArr[indexPath.row].created_at
               cell.orderduration.text = "Completion Time:".localized() + " " + "\(FinishordersArr[indexPath.row].order_duration_in_days)" + " " +  "Day".localized()
               //shdow for view
               cell.orderview.backgroundColor = UIColor.white
               cell.orderview.layer.masksToBounds = false
               cell.orderview.layer.shadowRadius = 3.0
               cell.orderview.layer.shadowColor = UIColor.gray.cgColor
               cell.orderview.layer.shadowOffset =  CGSize(width: 1, height: 3)
               cell.orderview.layer.shadowOpacity = 0.3
               cell.selectionStyle = .none
             
              
               return cell
           }
           else if tableView ==  newordersTableView {
               let cell = newordersTableView.dequeueReusableCell(withIdentifier: "NewOrdersTableViewCell", for: indexPath) as!
               NewOrdersTableViewCell
               
               cell.categorytitle.text =  NewordersArr[indexPath.row].categorytitle
               cell.ordertitle.text =  NewordersArr[indexPath.row].title
               cell.ordertype.text =  NewordersArr[indexPath.row].order_type
               cell.ordernumber.text =  "Order number:".localized() + " " +  NewordersArr[indexPath.row].order_number
               cell.orderdate.text =  NewordersArr[indexPath.row].created_at
               cell.orderdetails.text =  NewordersArr[indexPath.row].describeData
               cell.orderduration.text = "Completion Time:".localized() + " " +  "\(NewordersArr[indexPath.row].order_duration_in_days)" + " " +  "Day".localized()
               //shdow for view
               cell.orderview.backgroundColor = UIColor.white
               cell.orderview.layer.masksToBounds = false
               cell.orderview.layer.shadowRadius = 3.0
               cell.orderview.layer.shadowColor = UIColor.gray.cgColor
               cell.orderview.layer.shadowOffset =  CGSize(width: 1, height: 3)
               cell.orderview.layer.shadowOpacity = 0.3
               cell.selectionStyle = .none
             
              
               return cell
           }
           
           else{
               let cell = waitingordersTableView.dequeueReusableCell(withIdentifier: "WaitingOrdersTableViewCell", for: indexPath) as!
               WaitingOrdersTableViewCell
               cell.categorytitle.text =  WaitingordersArr[indexPath.row].categorytitle
               cell.ordertitle.text =  WaitingordersArr[indexPath.row].title
               cell.ordertype.text =  WaitingordersArr[indexPath.row].order_type
               cell.ordernumber.text =  "Order number:".localized() + " " +  WaitingordersArr[indexPath.row].order_number
               cell.orderdate.text =  WaitingordersArr[indexPath.row].created_at
               cell.orderdetails.text =  WaitingordersArr[indexPath.row].describeData
               cell.orderduration.text = "Completion Time:".localized() + " " + "\(WaitingordersArr[indexPath.row].order_duration_in_days)" + " " +  "Day".localized()
               //shdow for view
               cell.orderview.backgroundColor = UIColor.white
               cell.orderview.layer.masksToBounds = false
               cell.orderview.layer.shadowRadius = 3.0
               cell.orderview.layer.shadowColor = UIColor.gray.cgColor
               cell.orderview.layer.shadowOffset =  CGSize(width: 1, height: 3)
               cell.orderview.layer.shadowOpacity = 0.3
               cell.selectionStyle = .none
             
              
               return cell
           }
           

       }
       
   func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
       return 220.0
       
   }
   func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
       print("orders")
       if tableView ==  newordersTableView {
           let storyboard =  UIStoryboard(name:"Home", bundle: nil)
           let vc = storyboard.instantiateViewController(withIdentifier:"NewRequestViewController") as! NewRequestViewController
           vc.orderid = NewordersArr[indexPath.row].orderid
           self.navigationController?.pushViewController(vc, animated: true)
           
       }
       else if tableView ==  waitingordersTableView
       {
           let storyboard =  UIStoryboard(name:"Home", bundle: nil)
           let vc = storyboard.instantiateViewController(withIdentifier:"CurrentRequestViewController") as! CurrentRequestViewController
           vc.orderid = WaitingordersArr[indexPath.row].orderid
           self.navigationController?.pushViewController(vc, animated: true)
       }
       
       else{
           let storyboard =  UIStoryboard(name:"Home", bundle: nil)
           let vc = storyboard.instantiateViewController(withIdentifier:"CancelledRequestViewController") as! CancelledRequestViewController
           vc.orderid = FinishordersArr[indexPath.row].orderid
           self.navigationController?.pushViewController(vc, animated: true)
       }
   }
       
       
   }



extension OrdersViewController{
    //getneworder
    func getNewOrder(){
        showLoader()

        Api.AllNewOrder{[weak self](error: Error?,getneworder:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getNew = getneworder {
                self?.NewordersArr = getNew
                self?.newordersTableView.reloadData()
            }
          
            if self?.NewordersArr.count == 0 {
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
                self?.finishordersTableView.reloadData()
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
    
    
    //getWaitOrder
    func getWaitOrder(){
        showLoader()

        Api.AllWaitOrder{[weak self](error: Error?,getwaitorder:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getWait = getwaitorder {
                self?.WaitingordersArr = getWait
                self?.waitingordersTableView.reloadData()
            }
          
            if self?.WaitingordersArr.count == 0 {
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
