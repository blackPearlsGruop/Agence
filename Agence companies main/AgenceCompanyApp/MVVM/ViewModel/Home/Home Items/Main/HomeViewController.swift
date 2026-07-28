//
//  HomeViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 13/02/2024.
//

import UIKit
import AlamofireImage
class HomeViewController: UIViewController {
    var ViewAllserviceArr = [General]()
    var FinishordersArr:[General]=[]
    var availabLabel: String?
    @IBOutlet weak var orderlabel: UILabel!
    @IBOutlet weak var switchbutton:UISwitch!
    @IBOutlet weak var showview:UIView!
    @IBOutlet weak var ordersCollectionView: UICollectionView!
    @IBOutlet weak var serviceCollectionView: UICollectionView!
    @IBOutlet weak var chanceCollectionView: UICollectionView!
    @IBOutlet weak var subscriptionCollectionView: UICollectionView!
    @IBOutlet weak var viewallOne: UILabel!
    @IBOutlet weak var viewallTwo: UILabel!
    @IBOutlet weak var viewallThree: UILabel!
    @IBOutlet weak var servicelabel: UILabel!
    @IBOutlet weak var chancelabel: UILabel!
    @IBOutlet weak var subscribelabel: UILabel!
    @IBOutlet weak var orderslabel: UILabel!
    @IBOutlet weak var availablelabel: UILabel!
    @IBOutlet weak var receivelabel: UILabel!
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var nosubscribeloc: UILabel!
    @IBOutlet weak var nochanceloc: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        nosubscribeloc.text = "No Subscription Available".localized()
        nochanceloc.text = "No Offers Available".localized()
        orderlabel.text = "Home".localized()
        receivelabel.text = "Receiving Requests".localized()
        availablelabel.text = "Not Available".localized()
        subscribelabel.text = "Subscriptions".localized()
        chancelabel.text = "Chance".localized()
        servicelabel.text = "Services".localized()
        orderslabel.text = "Orders".localized()
        searchTf.placeholder = "Search Here".localized()
        viewallOne.text = "View All".localized()
        viewallTwo.text = "View All".localized()
        viewallThree.text = "View All".localized()
        
        switchbutton.isOn = false
        // showview.isHidden = false
        searchTf.addPadding(.both(50))
        switchbutton.onTintColor = UIColor.orange
        ordersCollectionView.delegate = self
        ordersCollectionView.dataSource = self
        //        ordersCollectionView.clipsToBounds = true
        
        serviceCollectionView.delegate = self
        serviceCollectionView.dataSource = self
        
        //        chanceCollectionView.delegate = self
        //        chanceCollectionView.dataSource = self
        //
        //
        //        subscriptionCollectionView.delegate = self
        //        subscriptionCollectionView.dataSource = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        viewallOne.isHidden = true
        getAllCategories()
        getCloseOrder()
        if Helper.getApiToken(key:"availabel") == "available" {
            
            switchbutton.isOn = true
            availabLabel = "available"
            availablelabel.text = "Available".localized()
        }
        
        else
        
        {
            switchbutton.isOn = false
            availabLabel = "not_available"
            availablelabel.text = "Not Available".localized()
        }
    }
    
    @IBAction func switchOnePressed(_ sender: UISwitch) {
        availablelabel.text = switchbutton.isOn ? "Available".localized() : "Not Available".localized()
        
        availabLabel = switchbutton.isOn ? "available" : "not_available"
        print(availabLabel)
        Helper.saveApiToken(value:availabLabel ?? "", key:"availabel")
        print(Helper.saveApiToken(value:availabLabel ?? "", key:"availabel"))
        available()
    }
    
    func available(){
        showLoader()
        Api.availability(method:"PUT"){[weak self] (error : String? , success : Bool , message) in
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
}
extension HomeViewController {
    @IBAction func searchButtonAction(_ sender: UIButton) {
       
        let storyboard =  UIStoryboard(name:"Home", bundle: nil)
        let userlogin = storyboard.instantiateViewController(withIdentifier:"SearchViewController") as! SearchViewController
        self.navigationController?.pushViewController(userlogin, animated: true)
        
        
      
    }
}
//colection
extension HomeViewController:UICollectionViewDelegate,UICollectionViewDataSource ,UICollectionViewDelegateFlowLayout{
        func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            if collectionView == ordersCollectionView {
                return CGSize(width: 370, height: 223)
            }
           
            else
            {
                return CGSize(width: 153, height: 191)
            }
        }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
        if collectionView == ordersCollectionView{
            return FinishordersArr.count
        }
        
        else{
            return ViewAllserviceArr.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
         if collectionView ==  ordersCollectionView{
        let cell = ordersCollectionView.dequeueReusableCell(withReuseIdentifier: "HomeOrdersCollectionViewCell", for: indexPath) as!
           HomeOrdersCollectionViewCell
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
             
                 return cell
         }
                else{
                    let cell = serviceCollectionView.dequeueReusableCell(withReuseIdentifier: "HomeServicesCollectionViewCell", for: indexPath) as!
                    HomeServicesCollectionViewCell
        
                    
                      cell.servicename.text = ViewAllserviceArr[indexPath.row].title
                      cell.servicedescribe.text = ViewAllserviceArr[indexPath.row].describeData
                          //image
                      //handle image
                      if let url = URL(string:"\(ViewAllserviceArr[indexPath.row].image)"){
                          print("\(url)")
        cell.serviceimage.af_setImage(withURL:url)
                      }
                      
                      cell.actionview = { [self] in
                          let storyboard = UIStoryboard(name: "Settings", bundle:nil)
                          let vc = storyboard.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
    vc.serviceId = ViewAllserviceArr[indexPath.row].id
                          self.navigationController?.pushViewController(vc, animated: true)
                      }
                                  cell.layer.masksToBounds = false
                                  cell.layer.shadowRadius = 3.0
                                  cell.layer.shadowColor = UIColor.gray.cgColor
                                  cell.layer.shadowOffset =  CGSize(width: 1, height: 3)
                                  cell.layer.shadowOpacity = 0.3
                      
                                  return cell
                             
                }
        
        
        
        
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == serviceCollectionView {
            
        }
        else{
            let storyboard =  UIStoryboard(name:"Home", bundle: nil)
            let vc = storyboard.instantiateViewController(withIdentifier:"CancelledRequestViewController") as! CancelledRequestViewController
            vc.orderid = FinishordersArr[indexPath.row].orderid
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
}


extension HomeViewController {
    @IBAction func viewAllSubscriptionButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "SubscriptionsViewController") as! SubscriptionsViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    @IBAction func viewAllChancesButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name:"Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "ChancesViewController") as! ChancesViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
}


extension HomeViewController{
    //getAllCategories
    func getAllCategories(){
        showLoader()
      
        Api.AllCategories{[weak self](error: Error?,getcategory:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getallcategory = getcategory {
                self?.ViewAllserviceArr = getallcategory
                self?.serviceCollectionView.reloadData()
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
                self?.ordersCollectionView.reloadData()
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
extension UICollectionViewFlowLayout {
    
    open override var flipsHorizontallyInOppositeLayoutDirection: Bool {
        return true
    }
    
}
