//
//  ViewAllServicesViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 24/04/2024.
//

import UIKit
import AlamofireImage
class ViewAllServicesViewController: UIViewController {
    @IBOutlet weak var serviceCollectionView: UICollectionView!
    @IBOutlet weak var servicelabel: UILabel!
    @IBOutlet weak var backbutton: UIButton!
    var ViewAllserviceArr = [General]()
    override func viewDidLoad() {
        super.viewDidLoad()
        serviceCollectionView.delegate = self
        serviceCollectionView.dataSource = self
        servicelabel.text = "Services".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getAllCategories()
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
}
//colection
extension ViewAllServicesViewController:UICollectionViewDelegate,UICollectionViewDataSource ,UICollectionViewDelegateFlowLayout{
        func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            return CGSize(width: serviceCollectionView.bounds.size.width, height:195)
             //   return CGSize(width: 200, height: 220)
          
        }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
     
            return ViewAllserviceArr.count
       
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
     
                    let cell = serviceCollectionView.dequeueReusableCell(withReuseIdentifier: "ViewAllServicesCollectionViewCell", for: indexPath) as!
                    ViewAllServicesCollectionViewCell
        
      
        cell.servicename.text = ViewAllserviceArr[indexPath.row].title
        cell.servicedescribe.text = ViewAllserviceArr[indexPath.row].describeData
            //image
        //handle image
        if let url = URL(string:"\(ViewAllserviceArr[indexPath.row].image)"){
            print("\(url)")
           cell.serviceimage.af_setImage(withURL:url)
        }
        
        cell.actionview = { [self] in
            let storyboard = UIStoryboard(name: "Home", bundle:nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "ServicesDetailsViewController") as! ServicesDetailsViewController
            vc.category = ViewAllserviceArr[indexPath.row].title
            self.navigationController?.pushViewController(vc, animated: true)
        }
                    cell.layer.masksToBounds = false
                    cell.layer.shadowRadius = 3.0
                    cell.layer.shadowColor = UIColor.gray.cgColor
                    cell.layer.shadowOffset =  CGSize(width: 1, height: 3)
                    cell.layer.shadowOpacity = 0.3
        
                    return cell
               
        
        
        
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("service details")
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "ServicesDetailsViewController") as! ServicesDetailsViewController
        vc.category = ViewAllserviceArr[indexPath.row].title
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
}

extension ViewAllServicesViewController{
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
}
