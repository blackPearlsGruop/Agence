//
//  FavouriteViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 24/02/2024.
//

import UIKit
import AlamofireImage
class FavouriteViewController: UIViewController {
    var FavArr = [General]()
    @IBOutlet weak var favouriteTableView: UITableView!
    @IBOutlet weak var favouritelabel: UILabel!
    @IBOutlet weak var backButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        favouriteTableView.delegate = self
        favouriteTableView.dataSource = self
        favouritelabel.text = "Favourite".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getAllFav()
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
   

}

extension FavouriteViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
            return FavArr.count
        
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       
            let cell = favouriteTableView.dequeueReusableCell(withIdentifier: "FavouriteTableViewCell", for: indexPath) as!
            FavouriteTableViewCell
        cell.companyname.text = FavArr[indexPath.row].title
        cell.companydescribe.text = FavArr[indexPath.row].describeData
        cell.companyrate.text = "\(FavArr[indexPath.row].avg_rate)"
        cell.companyprice.text = "Price start from".localized() + " " +  "\(FavArr[indexPath.row].price_start_from)"  + " " + "RS".localized()
        cell.categoryname.text = FavArr[indexPath.row].categorytitle
        //handle image
        if let url = URL(string:"\(FavArr[indexPath.row].company_logo)"){
            print("\(url)")
           cell.companyimg.af_setImage(withURL:url)
        }
        cell.actionview = { [self] in
            print("company details")
            let storyboard = UIStoryboard(name: "Home", bundle:nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "CompanyDetailsViewController") as! CompanyDetailsViewController
            vc.companyId = FavArr[indexPath.row].id
            self.navigationController?.pushViewController(vc, animated: true)
        }
        //shdow for view
       
        cell.selectionStyle = .none
         
            return cell
        
        

    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 235.0
    }

}

extension FavouriteViewController{
    
    //getallFav
    func getAllFav(){
        showLoader()

        Api.AllFavourite{[weak self](error: Error?,getfavourite:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getfav = getfavourite {
                self?.FavArr = getfav
                self?.favouriteTableView.reloadData()
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
