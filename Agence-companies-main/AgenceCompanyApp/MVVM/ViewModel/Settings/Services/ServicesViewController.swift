//
//  ServicesViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/02/2024.
//

import UIKit
import AlamofireImage
class ServicesViewController: UIViewController {
    var deletedRow = 0
    var editedRow = 0
    var ServiceArr = [General]()
   var OfferArr = [General]()
    var service_Id:Int?
    var offer_Id:Int?
   @IBOutlet weak var servicebutt:UIButton!
   @IBOutlet weak var offerbutt:UIButton!
   @IBOutlet weak var serviceTableView: UITableView!
   @IBOutlet weak var offerTableView: UITableView!
   @IBOutlet weak var emptyservice:UILabel!
   @IBOutlet weak var emptyoffer:UILabel!
   @IBOutlet weak var servicelabel:UILabel!
    @IBOutlet weak var backButton:UIButton!
   override func viewDidLoad() {
       super.viewDidLoad()
     
       servicebutt.setTitle("Services".localized(), for: .normal)
       offerbutt.setTitle("Offers".localized(), for: .normal)
       
       serviceTableView.delegate =  self
       serviceTableView.dataSource =  self
       
       offerTableView.delegate =  self
       offerTableView.dataSource =  self
       
       emptyoffer.isHidden =  true
       emptyservice.isHidden =  true
       
       servicelabel.text = "Services".localized()
       if(Localize.currentLanguage().contains("ar") == true){
           backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
       } else{
           print("english")
           
       }
   }
   
   override func viewWillAppear(_ animated: Bool) {
      getAllServices()
       serviceTableView.isHidden = false
       offerTableView.isHidden = true
    
       servicebutt.setTitleColor(UIColor.white, for: .normal)
       servicebutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
      
       
       offerbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
       offerbutt.backgroundColor = UIColor.white
     
   }
   

    
   
   @IBAction func serviceButtonAction(_ sender: UIButton) {
       getAllServices()
       servicebutt.setTitleColor(UIColor.white, for: .normal)
       servicebutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
      
       
       offerbutt.setTitleColor( hexStringToUIColor(hex:"#2505ED"), for: .normal)
       offerbutt.backgroundColor = UIColor.white
     
       serviceTableView.isHidden = false
       offerTableView.isHidden = true
   }
   
   @IBAction func eofferButtonAction(_ sender: UIButton) {
      getAllOffers()
       offerbutt.setTitleColor(UIColor.white, for: .normal)
       offerbutt.backgroundColor = hexStringToUIColor(hex:"#2505ED")
    
       
       servicebutt.setTitleColor(hexStringToUIColor(hex:"#2505ED"), for: .normal)
       servicebutt.backgroundColor = UIColor.white
      
       
       serviceTableView.isHidden = true
       offerTableView.isHidden = false
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
extension ServicesViewController:UITableViewDelegate,UITableViewDataSource{
       func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
           if tableView ==  serviceTableView {
               return ServiceArr.count
           }
           else{
               return OfferArr.count
           }
           
           
       }
       
       func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
           if tableView ==  serviceTableView {
               let cell = serviceTableView.dequeueReusableCell(withIdentifier: "ServicesTableViewCell", for: indexPath) as!
               ServicesTableViewCell
              
               cell.servicetitle.text =  ServiceArr[indexPath.row].title
               cell.serviceprice.text =   "\(ServiceArr[indexPath.row].price)" + " " + "RS".localized()
               //handle image
               if let url = URL(string:"\(ServiceArr[indexPath.row].image)"){
                   print("\(url)")
                  cell.servicimage.af_setImage(withURL:url)
               }
               
               cell.actionview = { [self] in
                   service_Id = ServiceArr[indexPath.row].id
                   print(service_Id)
                   let storyboard = UIStoryboard(name: "Settings", bundle:nil)
                   let vc = storyboard.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
                   vc.serviceId = service_Id
                   self.navigationController?.pushViewController(vc, animated: true)
               }
               //shdow for view
               cell.serviceview.backgroundColor = UIColor.white
               cell.serviceview.layer.masksToBounds = false
               cell.serviceview.layer.shadowRadius = 3.0
               cell.serviceview.layer.shadowColor = UIColor.gray.cgColor
               cell.serviceview.layer.shadowOffset =  CGSize(width: 1, height: 3)
               cell.serviceview.layer.shadowOpacity = 0.3
               cell.selectionStyle = .none
             
              
               return cell
           }
           else{
               let cell = offerTableView.dequeueReusableCell(withIdentifier: "OffersTableViewCell", for: indexPath) as!
               OffersTableViewCell
             
               cell.offertitle.text =  OfferArr[indexPath.row].title
               cell.offerprice.text =   "\(OfferArr[indexPath.row].price)" + " " + "RS".localized()
               cell.offerdescribe.text =  OfferArr[indexPath.row].describeData
               
               cell.actionview = { [self] in
                   offer_Id = OfferArr[indexPath.row].id
                   let storyboard = UIStoryboard(name: "Settings", bundle:nil)
                   let vc = storyboard.instantiateViewController(withIdentifier: "OfferDetailsViewController") as! OfferDetailsViewController
                   vc.offerId =  offer_Id
                   self.navigationController?.pushViewController(vc, animated: true)
               }
               //shdow for view
               cell.offerview.layer.masksToBounds = false
               cell.offerview.layer.shadowRadius = 3.0
               cell.offerview.layer.shadowColor = UIColor.gray.cgColor
               cell.offerview.layer.shadowOffset =  CGSize(width: 1, height: 3)
               cell.offerview.layer.shadowOpacity = 0.3
               cell.selectionStyle = .none
             
              
               return cell
           }

       }
       
   func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
       if tableView ==  serviceTableView {
           return 110.0
       }
       else {
           return 205.0
          
       }
   }
   func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {

       if tableView ==  serviceTableView {
           service_Id = ServiceArr[indexPath.row].id
           print(service_Id)
           let storyboard = UIStoryboard(name: "Settings", bundle:nil)
           let vc = storyboard.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
           vc.serviceId = service_Id
           self.navigationController?.pushViewController(vc, animated: true)
       }
       
       else{
           offer_Id = OfferArr[indexPath.row].id
           let storyboard = UIStoryboard(name: "Settings", bundle:nil)
           let vc = storyboard.instantiateViewController(withIdentifier: "OfferDetailsViewController") as! OfferDetailsViewController
           vc.offerId =  offer_Id
           self.navigationController?.pushViewController(vc, animated: true)
       }
         
         
           
     
   }
     
    
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
    
            let action = UIContextualAction(style: .normal, title: nil) { action, view, completion in
                
                action.backgroundColor = .white
                print("Delete")
                self.deleteData(at:indexPath)
                
            }
            
            action.image = UIImage(named:"Delete")
            action.backgroundColor = .white
            
            let configuration = UISwipeActionsConfiguration(actions: [action])
            configuration.performsFirstActionWithFullSwipe = false
            
            return configuration
        
    }
    
    func tableView(_ tableView: UITableView, leadingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
        let action = UIContextualAction(style: .normal, title: nil) { action, view, completion in
            view.backgroundColor = .white
            print("Edit")
            self.editData(at: indexPath)
        }
        action.image = UIImage(named:"Edit")
        action.backgroundColor = .white
        
        let configuration = UISwipeActionsConfiguration(actions: [action])
        configuration.performsFirstActionWithFullSwipe = false
        
        return configuration
    }

     func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
         if tableView == serviceTableView {
                return true
            } else {
                return false
            }
     }


     func deleteData(at indexPath: IndexPath) {
         print(indexPath.row)
         deletedRow = indexPath.row
         self.performSelector(onMainThread: #selector(self.showDeletePopup), with: nil, waitUntilDone: false)
     }

     func editData(at indexPath: IndexPath) {
         print(indexPath.row)
         editedRow = indexPath.row
         self.performSelector(onMainThread: #selector(self.showEditPopup), with: nil, waitUntilDone: false)
     }
    
    @objc func showEditPopup() {
        print("edit")
    
        let servicId = Int(Helper.getApiToken(key:"serviceid") ?? "")!
        print(servicId)
        let storyboard = UIStoryboard(name: "Settings", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "EditServiceViewController") as! EditServiceViewController
        vc.service_id = servicId
        self.navigationController?.pushViewController(vc, animated: true)
       
    }
    @objc func showDeletePopup() {
        print("delete")
        DeleteService()
    }
    
    func DeleteService(){
       print(service_Id)
        self.showLoader()
        Api.DeleteService(serviceid: service_Id ?? 0){[weak self] (error : String? , success : Bool , message) in
            self?.dismissLoader()
            if success {
            
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                self?.getAllServices()
              
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
}



       
   


extension ServicesViewController {
    //getAllServices
    func getAllServices(){
        showLoader()
     
        Api.getAllServices{[weak self](error: Error?,getservice:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getallservices = getservice {
                self?.ServiceArr = getallservices
                self?.serviceTableView.reloadData()
            }
            
            
            if self?.ServiceArr.count == 0 {
                self?.emptyservice.isHidden = false
            }
            else{
                self?.emptyservice.isHidden = true
            }
            
            if success{
                print("success")
                print(message)
                self?.view.makeToast(message, duration: 3.0, position: .center)
          
        }
   
            else{
                
            if message  == "".localized(){
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
    
    
    
    //getAllOffers
    func getAllOffers(){
        showLoader()
     
        Api.getAllOffers{[weak self](error: Error?,getoffer:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getalloffers = getoffer {
                self?.OfferArr = getalloffers
                self?.offerTableView.reloadData()
            }
            
            
            if self?.OfferArr.count == 0 {
                self?.emptyoffer.isHidden = false
            }
            else{
                self?.emptyoffer.isHidden = true
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
