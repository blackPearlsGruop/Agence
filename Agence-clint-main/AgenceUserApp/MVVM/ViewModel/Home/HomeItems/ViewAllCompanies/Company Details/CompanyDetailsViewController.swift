//
//  CompanyDetailsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 25/04/2024.
//

import UIKit
import AlamofireImage
class CompanyDetailsViewController: UIViewController {
    var companyId:Int?
    var offerdetails:General?
    var fav:Bool?
    @IBOutlet weak var offerTableView: UITableView!
    @IBOutlet weak var serviceTableView: UITableView!
    @IBOutlet weak var workCollectionView: UICollectionView!
    @IBOutlet weak var companydetailsloc: UILabel!
    @IBOutlet weak var briefloc: UILabel!
    @IBOutlet weak var workloc: UILabel!
    @IBOutlet weak var servicesloc: UILabel!
    @IBOutlet weak var offersloc: UILabel!
    @IBOutlet weak var sendbutton: UIButton!
    @IBOutlet weak var backbutton: UIButton!
    @IBOutlet weak var favbutton: UIButton!
    
    //Data
    @IBOutlet weak var OfferImage:UIImageView!
    @IBOutlet weak var OfferName:UILabel!
    @IBOutlet weak var OfferAddress:UILabel!
    @IBOutlet weak var OfferRate:UILabel!
    @IBOutlet weak var OfferDescribe:UILabel!
    var offerArr = [General]()
    var serviceArr  = [General]()
    var workArr  = [General]()
    override func viewDidLoad() {
        super.viewDidLoad()
    
        servicesloc.text = "Services".localized()
        offersloc.text = "Offers".localized()
        workloc.text = "Our work".localized()
        briefloc.text = "Brief".localized()
        companydetailsloc.text = "Company Details".localized()
        sendbutton.setTitle("Send special request".localized(), for: .normal)
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
        offerTableView.delegate = self
        offerTableView.dataSource = self
        offerTableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: offerTableView.frame.size.width, height: 1))
      
        
        serviceTableView.delegate = self
        serviceTableView.dataSource = self
        serviceTableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: offerTableView.frame.size.width, height: 1))
     
        workCollectionView.delegate = self
        workCollectionView.dataSource = self
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if fav == true{
           favbutton.setImage(UIImage(named:"blueheart"), for: UIControl.State.normal)
        }
        else{
            favbutton.setImage(UIImage(named:"heart"), for: UIControl.State.normal)
        }
        
        GetCompanyDetails()
        getAllServices()
        getAllOffers()
        getAllWorks()
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    
    
    @IBAction func PayButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "SendSpecialRequestViewController") as! SendSpecialRequestViewController
        vc.companyId = companyId
        
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    @IBAction func favButtonAction(_ sender: UIButton) {
      
        //fav for productstore
        var productstoreunchecked = true
      
            if productstoreunchecked {
                
                favbutton.setImage(UIImage(named:"blueheart"), for: UIControl.State.normal)
                productstoreunchecked = false
                
                self.showLoader()
                Api.Addtofav(company_id: companyId ?? 0){[weak self] (error : String? , success : Bool , message) in
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
        
            
                
            else {
              favbutton.setImage(UIImage(named:"heart"), for: UIControl.State.normal)
                productstoreunchecked = true
                self.showLoader()
                Api.Addtofav(company_id: companyId ?? 0){[weak self] (error : String? , success : Bool , message) in
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

}
extension CompanyDetailsViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView ==  offerTableView {
            return offerArr.count
        }
        else{
            return serviceArr.count
        }
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if tableView ==  offerTableView {
            let cell = offerTableView.dequeueReusableCell(withIdentifier: "CompanyDetailsTableViewCell", for: indexPath) as!
            CompanyDetailsTableViewCell
            cell.offername.text = offerArr[indexPath.row].title
            Helper.saveApiToken(value:"\(offerArr[indexPath.row].title)", key:"companyname")
            Helper.saveApiToken(value:"\(offerArr[indexPath.row].company_logo)", key:"companylogo")
            cell.offerdetails.text = offerArr[indexPath.row].describeData
            cell.offerprice.text = "\(offerArr[indexPath.row].offerprice)"  + " " + "RS".localized()
            
            cell.actionview = { [self] in
                let storyboard = UIStoryboard(name: "Home", bundle:nil)
                let vc = storyboard.instantiateViewController(withIdentifier: "OfferDetailsViewController") as! OfferDetailsViewController
                vc.orderid = offerArr[indexPath.row].id
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
        else{
            let cell = serviceTableView.dequeueReusableCell(withIdentifier: "CompanyServicesTableViewCell", for: indexPath) as!
            CompanyServicesTableViewCell
            cell.servicename.text = serviceArr[indexPath.row].title
            cell.serviceprice.text = "\(100)" + " " + "RS".localized() //"\(serviceArr[indexPath.row].serviceprice)" + " " + "RS".localized()
            //handle image
            if let url = URL(string:"\(serviceArr[indexPath.row].image)"){
                print("\(url)")
                cell.serviceimage.af_setImage(withURL:url)
            }
            
            cell.actionview = { [self] in
                let storyboard = UIStoryboard(name: "Home", bundle:nil)
                let vc = storyboard.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
                vc.orderid = serviceArr[indexPath.row].id
                self.navigationController?.pushViewController(vc, animated: true)
            }
            //shdow for view
            
            cell.serviceview.layer.masksToBounds = false
            cell.serviceview.layer.shadowRadius = 3.0
            cell.serviceview.layer.shadowColor = UIColor.gray.cgColor
            cell.serviceview.layer.shadowOffset =  CGSize(width: 1, height: 3)
            cell.serviceview.layer.shadowOpacity = 0.3
            cell.selectionStyle = .none
            
            return cell
        }

    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if tableView ==  offerTableView {
            return 165.0
        }
        else{
            return 90.0
         
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("Hello")
//        if tableView ==  offerTableView {
//                    let storyboard = UIStoryboard(name: "Home", bundle:nil)
//                    let vc = storyboard.instantiateViewController(withIdentifier: "OfferDetailsViewController") as! OfferDetailsViewController
//            vc.orderid = companyId
//                    self.navigationController?.pushViewController(vc, animated: true)
//        }
        
//        else{
//            let storyboard = UIStoryboard(name: "Home", bundle:nil)
//            let vc = storyboard.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
//            vc.orderid = companyId
//            self.navigationController?.pushViewController(vc, animated: true)
//        }
    }

}


//colection
extension CompanyDetailsViewController:UICollectionViewDelegate,UICollectionViewDataSource ,UICollectionViewDelegateFlowLayout{
        func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
          
                return CGSize(width: 379, height: 200)
            
        }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
       
            return workArr.count
        
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
       
                    let cell = workCollectionView.dequeueReusableCell(withReuseIdentifier: "CompanyWorksCollectionViewCell", for: indexPath) as!
                    CompanyWorksCollectionViewCell
        cell.worktitle.text = workArr[indexPath.row].title
        cell.workdescribe.text = workArr[indexPath.row].describeData
        //handle image
        if let url = URL(string:"\(workArr[indexPath.row].work_file)"){
            print("\(url)")
            cell.companyimage.af_setImage(withURL:url)
        }       
        //shdow for view
       
                 
                    cell.layer.masksToBounds = false
                    cell.layer.shadowRadius = 3.0
                    cell.layer.shadowColor = UIColor.gray.cgColor
                    cell.layer.shadowOffset =  CGSize(width: 1, height: 3)
                    cell.layer.shadowOpacity = 0.3
        
                    return cell
          
        
        
        
        
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("soon")
    }
    
}

extension CompanyDetailsViewController {
    //company details
    func GetCompanyDetails(){
        showLoader()
        Api.GetCompanyDetails(companyid:companyId ?? 0){ [weak self](error: Error?, getofferdetails: General?,success,message) in
        self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
           
            if let getalldata = getofferdetails{
                self?.offerdetails = getalldata
                self?.fav = self?.offerdetails?.is_added_favourite
                let offername = self?.offerdetails?.company_name
                self?.OfferName.text = offername
                
                let offerdescribe = self?.offerdetails?.describeData
                self?.OfferDescribe.text = offerdescribe
                
                let adres = self?.offerdetails?.address
                self?.OfferAddress.text = adres
              
                
                
                let rate = self?.offerdetails?.avg_rate
                self?.OfferRate.text = "\(Int(rate ?? 0))"
                
                //handle image
            if let url = URL(string:self?.offerdetails?.company_logo ?? ""){
                    print("\(url)")
                self?.OfferImage.af_setImage(withURL: url)
                }
                
                else {
                    self?.OfferImage.image = UIImage(named:"")
                }
                
             
                
            }
            
            if success{
                print("success")
                self?.view.makeToast(message, duration: 3.0, position: .center)
          
        }
   
            else{
                
            if message  == "Unauthenticated user".localized(){
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
    
    //services
    //getAllServices
    func getAllServices(){
      //  showLoader()
        Api.CompanyService(companyid:companyId ?? 0){[weak self](error: Error?,getcategory:[General]?,success,message) in
          //  self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getallcategory = getcategory {
                self?.serviceArr = getallcategory
                self?.serviceTableView.reloadData()
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
    
    //works
    //getAllWorks
    func getAllWorks(){
      //  showLoader()
        Api.CompanyWork(companyid:companyId ?? 0){[weak self](error: Error?,getworks:[General]?,success,message) in
          //  self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getallworks = getworks {
                self?.workArr = getallworks
                self?.workCollectionView.reloadData()
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
    
    //offers
    //getAllOffers
    func getAllOffers(){
      //  showLoader()
        Api.CompanyOffers(companyid:companyId ?? 0){[weak self](error: Error?,getoffers:[General]?,success,message) in
          //  self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getalloffers = getoffers {
                self?.offerArr = getalloffers
                self?.offerTableView.reloadData()
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
