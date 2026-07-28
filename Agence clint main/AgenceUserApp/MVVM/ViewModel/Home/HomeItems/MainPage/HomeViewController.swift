//
//  HomeViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 22/02/2024.
//

import UIKit
import AlamofireImage
class HomeViewController: UIViewController {
    var companyId:Int?
    @IBOutlet weak var homelabel: UILabel!
    @IBOutlet weak var pagecontroller: UIPageControl!
    @IBOutlet weak var companyCollectionView: UICollectionView!
    @IBOutlet weak var serviceCollectionView: UICollectionView!
    @IBOutlet weak var sliderCollectionView: UICollectionView!
    @IBOutlet weak var servicelabel: UILabel!
    @IBOutlet weak var companylabel: UILabel!
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var viewallOne: UILabel!
    @IBOutlet weak var viewallTwo: UILabel!
    var companyArr = [General]()
    var serviceArr = [General]()
    var sliderArray = [General]()
    var currentindex = 0
    var timer:Timer?
    override func viewDidLoad() {
        super.viewDidLoad()
     
        print(pagecontroller.numberOfPages)
        companyCollectionView.delegate = self
        companyCollectionView.dataSource = self
        
        serviceCollectionView.delegate = self
        serviceCollectionView.dataSource = self
        
        sliderCollectionView.delegate = self
        sliderCollectionView.dataSource = self
        
        
        //shadow for collection
        sliderCollectionView.layer.masksToBounds = false
        sliderCollectionView.layer.shadowRadius = 3.0
        sliderCollectionView.layer.shadowColor = UIColor.gray.cgColor
        sliderCollectionView.layer.shadowOffset =  CGSize(width: 1, height: 3)
        sliderCollectionView.layer.shadowOpacity = 0.3
        
        servicelabel.text = "Services".localized()
        homelabel.text = "Home".localized()
        searchTf.placeholder = "Search Here".localized()
        searchTf.addPadding(.both(50))
        companylabel.text = "Companies".localized()
        viewallOne.text = "View All".localized()
        viewallTwo.text = "View All".localized()
       
        
        
        startTimer()
    }
   
    override func viewWillAppear(_ animated: Bool) {
        getAllCategories()
        getAllCompany()
        handleHomeSliders()
      
    }
    

}

extension HomeViewController {
    
    // start timer func
    func startTimer() {
        timer =  Timer.scheduledTimer(timeInterval:2.5, target: self, selector: #selector(self.movetocurrentindex), userInfo: nil, repeats: true)
        
    }
    @objc func movetocurrentindex(){
        if currentindex < sliderArray.count - 1{
            currentindex += 1
          
        }
        else{
            currentindex = 0
        
         
        }
        
        sliderCollectionView.scrollToItem(at:IndexPath(item:currentindex, section: 0) , at: .centeredHorizontally, animated: true)
        pagecontroller.currentPage = currentindex
    }
    
    //handle slider function
    func handleHomeSliders(){
        showLoader()
        
        Api.getHomeSlider{[weak self](error: Error?,gethomeslider:[General]?) in
            self?.dismissLoader()
            
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getallHomesliders = gethomeslider {
                self?.sliderArray = getallHomesliders
                self?.pagecontroller.numberOfPages = (self?.sliderArray.count)!
                self?.sliderCollectionView.reloadData()
            }
            
            
        }
    }
    
   
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
                self?.serviceArr = getallcategory
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
}

extension HomeViewController {
    @IBAction func searchButtonAction(_ sender: UIButton) {
       
        let storyboard =  UIStoryboard(name:"Home", bundle: nil)
        let userlogin = storyboard.instantiateViewController(withIdentifier:"SearchViewController") as! SearchViewController
        self.navigationController?.pushViewController(userlogin, animated: true)
        
        
      
    }
    
    @IBAction func viewallServicesButtonAction(_ sender: UIButton) {
       
        let storyboard =  UIStoryboard(name:"Home", bundle: nil)
        let userlogin = storyboard.instantiateViewController(withIdentifier:"ViewAllServicesViewController") as! ViewAllServicesViewController
        self.navigationController?.pushViewController(userlogin, animated: true)
        
        
      
    }
    
    @IBAction func viewallcompaniesButtonAction(_ sender: UIButton) {
       
        let storyboard =  UIStoryboard(name:"Home", bundle: nil)
        let userlogin = storyboard.instantiateViewController(withIdentifier:"ViewAllCompaniesViewController") as! ViewAllCompaniesViewController
        self.navigationController?.pushViewController(userlogin, animated: true)
        
        
      
    }
}


extension HomeViewController{
    
    //getAllCompany
    func getAllCompany(){
        showLoader()

        Api.AllCompany{[weak self](error: Error?,getfavourite:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getfav = getfavourite {
                self?.companyArr = getfav
                self?.companyCollectionView.reloadData()
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


//colection
extension HomeViewController:UICollectionViewDelegate,UICollectionViewDataSource ,UICollectionViewDelegateFlowLayout{
        func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            if collectionView == companyCollectionView {
                return CGSize(width: 373, height: 223)
            }
           
            else if collectionView == sliderCollectionView {
               // return CGSize(width: 412, height: 200)
  //  return CGSize(width: sliderCollectionView.bounds.size.width, height: sliderCollectionView.bounds.size.height)
        return CGSize(width: sliderCollectionView.frame.width, height: sliderCollectionView.frame.height)
           
            }
            
            else
            {
                return CGSize(width: 163, height: 47)
            }
            
            
        }
//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
//        return UIEdgeInsets(top: 0, left:10, bottom: 0, right:10)
//    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
        if collectionView == companyCollectionView{
            return companyArr.count
        }
        
       else if collectionView == sliderCollectionView{
            return sliderArray.count
        }
        
        else{
            return serviceArr.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
         if collectionView ==  companyCollectionView{
        let cell = companyCollectionView.dequeueReusableCell(withReuseIdentifier: "CompaniesCollectionViewCell", for: indexPath) as!
        CompaniesCollectionViewCell
             cell.companyname.text = companyArr[indexPath.row].title
             cell.companydescribe.text = companyArr[indexPath.row].describeData
             cell.companyrate.text =  "\(round(companyArr[indexPath.row].avg_rate))"
             cell.companyprice.text = "Price start from".localized() + " " +  "\(companyArr[indexPath.row].price_start_from)"  + " " + "RS".localized()
             cell.categoryname.text = companyArr[indexPath.row].categorytitle
             //handle image
             if let url = URL(string:"\(companyArr[indexPath.row].company_logo)"){
                 print("\(url)")
                cell.companyimg.af_setImage(withURL:url)
             }
             if companyArr[indexPath.row].is_added_favourite == true{
                 cell.favbutton.setImage(UIImage(named:"blueheart"), for: UIControl.State.normal)
             }
             else{
                 cell.favbutton.setImage(UIImage(named:"heart"), for: UIControl.State.normal)
             }
             //fav for productstore
             var productstoreunchecked = true
             cell.actionfav = {[self] in
                 companyId = companyArr[indexPath.row].id
                 if productstoreunchecked {
                     
                     cell.favbutton.setImage(UIImage(named:"blueheart"), for: UIControl.State.normal)
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
                     cell.favbutton.setImage(UIImage(named:"heart"), for: UIControl.State.normal)
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
             
             cell.actionview = { [self] in
                 print("company details")
                 
                 let storyboard = UIStoryboard(name: "Home", bundle:nil)
                 let vc = storyboard.instantiateViewController(withIdentifier: "CompanyDetailsViewController") as! CompanyDetailsViewController
                 vc.companyId = companyArr[indexPath.row].id
                 self.navigationController?.pushViewController(vc, animated: true)
             }
             //shdow for view
             
                cell.layer.masksToBounds = false
                cell.layer.shadowRadius = 3.0
                cell.layer.shadowColor = UIColor.gray.cgColor
                cell.layer.shadowOffset =  CGSize(width: 1, height: 3)
                cell.layer.shadowOpacity = 0.3
            
        return cell
         }
        
        else if collectionView == sliderCollectionView{
            let cell = sliderCollectionView.dequeueReusableCell(withReuseIdentifier: "SliderCollectionViewCell", for: indexPath) as!
            SliderCollectionViewCell
            cell.homeTitle.text = sliderArray[indexPath.row].title
            //handle image
            if let url = URL(string:"\(sliderArray[indexPath.row].image)"){
                print("\(url)")
                cell.homePicture.af_setImage(withURL:url)
            }
            
            return cell
        }
                else{
                    let cell = serviceCollectionView.dequeueReusableCell(withReuseIdentifier: "ServicesCollectionViewCell", for: indexPath) as!
                    ServicesCollectionViewCell
        
                    cell.servicename.text = serviceArr[indexPath.row].title
        
                 
                    cell.layer.masksToBounds = false
                    cell.layer.shadowRadius = 3.0
                    cell.layer.shadowColor = UIColor.gray.cgColor
                    cell.layer.shadowOffset =  CGSize(width: 1, height: 3)
                    cell.layer.shadowOpacity = 0.3
        
                    return cell
                }
        
        
        
        
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("soon")
    }
    
}



