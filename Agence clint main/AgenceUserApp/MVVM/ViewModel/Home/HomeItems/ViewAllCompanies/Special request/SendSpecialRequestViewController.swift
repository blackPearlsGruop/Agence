//
//  SendSpecialRequestViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//

import UIKit
import AlamofireImage
class SendSpecialRequestViewController: UIViewController {
    var companyId:Int?
    var offerdetails:General?
    @IBOutlet weak var ordertitleTF: UITextField!
    @IBOutlet weak var orderdetailsTF: UITextField!
    @IBOutlet weak var ordertitleloc: UILabel!
    @IBOutlet weak var orderdetailsloc: UILabel!
    @IBOutlet weak var specialorderloc: UILabel!
    @IBOutlet weak var specialrequestloc: UILabel!
    @IBOutlet weak var companynamelabel:UILabel!
    @IBOutlet weak var companyimage:UIImageView!
    @IBOutlet weak var sendbutton:UIButton!
    @IBOutlet weak var backbutton:UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
     //   ordertitleTF.placeholder = "Order Headline".localized()
        ordertitleloc.text = "Order Headline".localized()
      //  orderdetailsTF.placeholder = "Request Details".localized()
        orderdetailsTF.addPadding(.both(20))
        ordertitleTF.addPadding(.both(20))
        orderdetailsloc.text = "Request Details".localized()
        sendbutton.setTitle("Send".localized(), for: .normal)
        specialorderloc.text = "Special Order".localized()
        specialrequestloc.text = "Special Request".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }

    override func viewWillAppear(_ animated: Bool) {
//        companynamelabel.text =  Helper.getApiToken(key:"companyname")
//        //handle image
//        if let url = URL(string:Helper.getApiToken(key:"companylogo")!){
//            print("\(url)")
//           companyimage.af_setImage(withURL:url)
//        }
        
        GetCompanyDetails()
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    @IBAction func sendbuttonButton(_ sender: UIButton) {
      
        self.showLoader()
        Api.sendSpecialOrder(company_id: companyId ?? 0, order_type:"private", order_title:ordertitleTF.text ?? "", order_description:orderdetailsTF.text ?? "" ){[weak self] (error : String? , success : Bool , message) in
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

extension SendSpecialRequestViewController{
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
               
                let offername = self?.offerdetails?.company_name
                self?.companynamelabel.text = offername
                
              
                //handle image
            if let url = URL(string:self?.offerdetails?.company_logo ?? ""){
                    print("\(url)")
                self?.companyimage.af_setImage(withURL: url)
                }
                
                else {
                    self?.companyimage.image = UIImage(named:"")
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
}
