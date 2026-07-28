//
//  ServicesDetailsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 24/04/2024.
//

import UIKit

class ServicesDetailsViewController: UIViewController {
    var companyArr = [General]()
    var category:String?
    var companyId:Int?
    @IBOutlet weak var servicelabel: UILabel!
    @IBOutlet weak var backbutton: UIButton!
    @IBOutlet weak var categorylabel: UILabel!
    @IBOutlet weak var companyTableView: UITableView!
    @IBOutlet weak var serviceView: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        companyTableView.delegate = self
        companyTableView.dataSource = self
        
        
        serviceView.layer.masksToBounds = false
        serviceView.layer.shadowRadius = 3.0
        serviceView.layer.shadowColor = UIColor.gray.cgColor
        serviceView.layer.shadowOffset =  CGSize(width: 1, height: 3)
        serviceView.layer.shadowOpacity = 0.3
      
        
        servicelabel.text = "Services".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    override func viewWillAppear(_ animated: Bool) {
        categorylabel.text = category
        getAllCompany()
    }

}

extension ServicesDetailsViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
            return companyArr.count
        
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       
            let cell = companyTableView.dequeueReusableCell(withIdentifier: "ViewAllCompaniesTableViewCell", for: indexPath) as!
        ViewAllCompaniesTableViewCell
        
        cell.companyname.text = companyArr[indexPath.row].title
        cell.companydescribe.text = companyArr[indexPath.row].describeData
        cell.companyrate.text = "\(companyArr[indexPath.row].avg_rate)"
        cell.companyprice.text = "Price start from".localized() + " " +  "\(companyArr[indexPath.row].price_start_from)"  + " " + "RS".localized()
        cell.categoryname.text = companyArr[indexPath.row].categorytitle
        //handle image
        if let url = URL(string:"\(companyArr[indexPath.row].company_logo)"){
            print("\(url)")
           cell.companyimg.af_setImage(withURL:url)
        }        //shdow for view
       
        cell.selectionStyle = .none
         
        cell.actionview = { [self] in
            print("company details")
            let storyboard = UIStoryboard(name: "Home", bundle:nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "CompanyDetailsViewController") as! CompanyDetailsViewController
            vc.companyId = companyArr[indexPath.row].id
    self.navigationController?.pushViewController(vc, animated: true)
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
            return cell
        
        

    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 230.0
    }

}
extension ServicesDetailsViewController{
    
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
                self?.companyTableView.reloadData()
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
