//
//  ServiceDetailsViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit
import AlamofireImage
class ServiceDetailsViewController: UIViewController {
    var servicedetails:General?
    var serviceId:Int?
   @IBOutlet weak var offerdetailslabel: UILabel!
   @IBOutlet weak var offercontentlabel: UILabel!
   @IBOutlet weak var backbutt:UIButton!
   @IBOutlet weak var editbutt:UIButton!
   @IBOutlet weak var deletebutt:UIButton!
    //Data
    @IBOutlet weak var OfferImage:UIImageView!
    @IBOutlet weak var OfferName:UILabel!
    @IBOutlet weak var OfferAddress:UILabel!
    @IBOutlet weak var Offertime:UILabel!
    @IBOutlet weak var OfferPrice:UILabel!
    @IBOutlet weak var OfferDescribe:UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print(serviceId ?? 0)
        offercontentlabel.text = "Service Content".localized()
        offerdetailslabel.text = "Service Details".localized()
        editbutt.setTitle("Edit".localized(), for: .normal)
        deletebutt.setTitle("Delete".localized(), for: .normal)
        if(Localize.currentLanguage().contains("ar") == true){
            backbutt.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
        
        OfferAddress.text = "Riyadh,Saudi arabia"
        GetServiceDetails()
    }
  
    override func viewWillAppear(_ animated: Bool) {
       
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    @IBAction func EditServiceButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Settings", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "EditServiceViewController") as! EditServiceViewController
        vc.service_id = serviceId
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    @IBAction func deletButtonAction(_ sender: UIButton) {
      
        DeleteService()
    }
    
    
    func DeleteService(){
       print(serviceId)
        self.showLoader()
        Api.DeleteService(serviceid: serviceId ?? 0){[weak self] (error : String? , success : Bool , message) in
            self?.dismissLoader()
            if success {
                self?.navigationController?.popViewController(animated:true)
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
    
    func GetServiceDetails(){
        showLoader()
     print(serviceId ?? 0)
    Api.GetServiceDetails(serviceid:serviceId ?? 0){ [weak self](error: Error?, getservicedetails: General?,success,message) in
        self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
           
            if let getallservice = getservicedetails {
                self?.servicedetails = getallservice
                
                let offername = self?.servicedetails?.title
                self?.OfferName.text = offername
                
                let offerdescribe = self?.servicedetails?.describeData
                self?.OfferDescribe.text = offerdescribe
                
             //   let adres = self?.servicedetails?.address
               // self?.OfferAddress.text = adres
              
                
                let price = self?.servicedetails?.price
                self?.OfferPrice.text = "\(Int(price ?? 0))" + " " + "RS".localized()
               
                let time = self?.servicedetails?.service_duration_in_days
                self?.Offertime.text = "Completion Time:".localized() + "\(Int(time ?? 0))" + " " + "Day".localized()
                
          
                
                //handle image
//if let url = URL(string:self?.servicedetails?.serviceimage ?? ""){
//                    print("\(url)")
//                self?.OfferImage.af_setImage(withURL: url)
//                }
//                
//                else {
//                    self?.OfferImage.image = UIImage(named:"")
//                }
                
             
                
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

    
 
  
    


