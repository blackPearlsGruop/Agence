//
//  OrdersViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//


import UIKit
import AlamofireImage
class OrdersViewController: UIViewController {
    @IBOutlet weak var orderlabel: UILabel!
     var NewordersArr = [General]()
    var FinishordersArr = [General]()
    @IBOutlet weak var neworderbutt:UIButton!
    @IBOutlet weak var expiredorderbutt:UIButton!
    @IBOutlet weak var finishordersTableView: UITableView!
    @IBOutlet weak var newordersTableView: UITableView!
    @IBOutlet weak var Noorderlabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        orderlabel.text = "Orders".localized()
        neworderbutt.setTitle("New Order".localized(), for: .normal)
        expiredorderbutt.setTitle("Closed Order".localized(), for: .normal)
        Noorderlabel.text = "No Orders Available".localized()
        finishordersTableView.delegate =  self
        finishordersTableView.dataSource =  self
       
        newordersTableView.delegate =  self
        newordersTableView.dataSource =  self
        Noorderlabel.isHidden =  true
     
    }
    
    override func viewWillAppear(_ animated: Bool) {
       getNewOrder()
        newordersTableView.isHidden = false
        finishordersTableView.isHidden = true
     
        neworderbutt.setTitleColor(UIColor.white, for: .normal)
        neworderbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
        neworderbutt.setImage(UIImage(named:"neworderwhite"), for: .normal)
        
        expiredorderbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
        expiredorderbutt.backgroundColor = UIColor.white
        expiredorderbutt.setImage(UIImage(named:"cancelordergray"), for: .normal)
    }
    
 
    
    @IBAction func neworderButtonAction(_ sender: UIButton) {
        getNewOrder()
        neworderbutt.setTitleColor(UIColor.white, for: .normal)
        neworderbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
        neworderbutt.setImage(UIImage(named:"neworderwhite"), for: .normal)
        
        expiredorderbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
        expiredorderbutt.backgroundColor = UIColor.white
        expiredorderbutt.setImage(UIImage(named:"cancelordergray"), for: .normal)
        newordersTableView.isHidden = false
        finishordersTableView.isHidden = true
    }
    
    @IBAction func expiredorderButtonAction(_ sender: UIButton) {
        getCloseOrder()
        expiredorderbutt.setTitleColor(UIColor.white, for: .normal)
        expiredorderbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
        expiredorderbutt.setImage(UIImage(named:"cancelorderwhite"), for: .normal)
        
        neworderbutt.setTitleColor(hexStringToUIColor(hex:"#2505ED"), for: .normal)
        neworderbutt.backgroundColor = UIColor.white
        neworderbutt.setImage(UIImage(named:"newordergray"), for: .normal)
        
        newordersTableView.isHidden = true
        finishordersTableView.isHidden = false
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
            else{
                return NewordersArr.count
            }
            
            
        }
        
        func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
            if tableView ==  finishordersTableView {
                let cell = finishordersTableView.dequeueReusableCell(withIdentifier: "FinishOrdersTableViewCell", for: indexPath) as!
                FinishOrdersTableViewCell
                
                cell.ordertitle.text =  FinishordersArr[indexPath.row].title
                cell.ordernumber.text =  FinishordersArr[indexPath.row].order_number
                cell.orderprice.text =  "\(FinishordersArr[indexPath.row].price_start_from)" + " " + "RS".localized()
                
               
                cell.categoryname.text =  FinishordersArr[indexPath.row].company_name
                
                cell.categorydescribe.text =  FinishordersArr[indexPath.row].address

                //handle image
                if let url = URL(string:"\(FinishordersArr[indexPath.row].company_logo)"){
                    print("\(url)")
                   cell.categoryimg.af_setImage(withURL:url)
                }
                
                //shdow for view
                cell.orderview.backgroundColor = UIColor.white
                cell.orderview.layer.masksToBounds = false
                cell.orderview.layer.shadowRadius = 3.0
                cell.orderview.layer.shadowColor = UIColor.gray.cgColor
                cell.orderview.layer.shadowOffset =  CGSize(width: 1, height: 3)
                cell.orderview.layer.shadowOpacity = 0.3
                cell.selectionStyle = .none
              
               //view
                cell.actionview = { [self] in
                    let storyboard = UIStoryboard(name: "Home", bundle:nil)
                    let vc = storyboard.instantiateViewController(withIdentifier: "ExpiredOrderDetailsViewController") as! ExpiredOrderDetailsViewController
                    vc.israted = FinishordersArr[indexPath.row].is_rated_before
                    vc.orderid = FinishordersArr[indexPath.row].orderid
                    vc.catid = FinishordersArr[indexPath.row].categoryid
                    vc.companyid = FinishordersArr[indexPath.row].companyid
                    vc.taxvalue =  FinishordersArr[indexPath.row].ordertax
                    vc.pricevalue = FinishordersArr[indexPath.row].orderprice
                    vc.detailsdata = FinishordersArr[indexPath.row]
                    self.navigationController?.pushViewController(vc, animated: true)
                }
                //reorder
                cell.actionreorder = { [self] in
       
                    print(FinishordersArr[indexPath.row].categoryid)
                    self.showLoader()
        Api.sendOrder(category_id:FinishordersArr[indexPath.row].categoryid, order_type:FinishordersArr[indexPath.row].order_type, order_title:FinishordersArr[indexPath.row].title, order_description:FinishordersArr[indexPath.row].orderdescripe, order_duration_in_days:FinishordersArr[indexPath.row].order_duration_in_days){[weak self] (error : String? , success : Bool , message) in
                        self?.dismissLoader()
                        if success {
                        
                                self?.view.makeToast(message, duration: 3.0, position: .center)
                           
                          
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
            else{
                let cell = newordersTableView.dequeueReusableCell(withIdentifier: "NewOrdersTableViewCell", for: indexPath) as!
                NewOrdersTableViewCell
                
                cell.ordertitle.text =  NewordersArr[indexPath.row].title
                cell.ordernumber.text =  NewordersArr[indexPath.row].order_number
                cell.orderprice.text =  "\(NewordersArr[indexPath.row].price_start_from)" + " " + "RS".localized()
               
                cell.categoryname.text =  NewordersArr[indexPath.row].company_name
                
                cell.categorydescribe.text =  NewordersArr[indexPath.row].address
                
        if NewordersArr[indexPath.row].has_offers == false || NewordersArr[indexPath.row].company_name == "" {
                    cell.categoryname.isHidden = true
                    cell.orderprice.isHidden = true
                    cell.categorydescribe.isHidden = true
                    cell.viewbutton.isHidden = true
                    cell.chatbutton.isHidden = true
                    cell.addressimg.isHidden = true
                    cell.categoryimg.isHidden = true
                    cell.offerlabelloc.isHidden = false
                    
                }
                else  if NewordersArr[indexPath.row].has_offers == true {
                    cell.categoryname.isHidden = false
                    cell.orderprice.isHidden = false
                    cell.categorydescribe.isHidden = false
                    cell.viewbutton.isHidden = false
                    cell.chatbutton.isHidden = false
                    cell.addressimg.isHidden = false
                    cell.categoryimg.isHidden = false
                    cell.offerlabelloc.isHidden = true
                }
                
                else{
                    print("Nothing to do")
                }
                //handle image
                if let url = URL(string:"\(NewordersArr[indexPath.row].company_logo)"){
                    print("\(url)")
                   cell.categoryimg.af_setImage(withURL:url)
                }
                
                //shdow for view
                cell.orderview.backgroundColor = UIColor.white
                cell.orderview.layer.masksToBounds = false
                cell.orderview.layer.shadowRadius = 3.0
                cell.orderview.layer.shadowColor = UIColor.gray.cgColor
                cell.orderview.layer.shadowOffset =  CGSize(width: 1, height: 3)
                cell.orderview.layer.shadowOpacity = 0.3
                cell.selectionStyle = .none
                //view
                cell.actionview = { [self] in
                     let storyboard = UIStoryboard(name: "Home", bundle:nil)
                     let vc = storyboard.instantiateViewController(withIdentifier: "NewOrderDetailsViewController") as! NewOrderDetailsViewController
                     vc.orderid = NewordersArr[indexPath.row].orderid
                     self.navigationController?.pushViewController(vc, animated: true)
                 }
                 //chat
                 cell.actionchat = {
                     
                 }
               
                return cell
            }

        }
        
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 220.0
        
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("Hello")
//        if tableView ==  finishordersTableView {
//            let storyboard = UIStoryboard(name: "Home", bundle:nil)
//            let vc = storyboard.instantiateViewController(withIdentifier: "ExpiredOrderDetailsViewController") as! ExpiredOrderDetailsViewController
//            vc.detailsdata = FinishordersArr[indexPath.row]
//            vc.orderid = FinishordersArr[indexPath.row].orderid
//            self.navigationController?.pushViewController(vc, animated: true)
//        }
//        else{
//            let storyboard = UIStoryboard(name: "Home", bundle:nil)
//            let vc = storyboard.instantiateViewController(withIdentifier: "NewOrderDetailsViewController") as! NewOrderDetailsViewController
//            vc.orderid = NewordersArr[indexPath.row].orderid
//            self.navigationController?.pushViewController(vc, animated: true)
//        }
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
}
