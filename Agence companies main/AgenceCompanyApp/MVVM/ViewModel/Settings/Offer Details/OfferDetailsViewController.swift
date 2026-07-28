//
//  OfferDetailsViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit
import AlamofireImage
class OfferDetailsViewController: UIViewController {
    var getofferdetails:General?
    var offerId:Int?
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
        offercontentlabel.text = "Offer Content".localized()
        offerdetailslabel.text = "Offer Details".localized()
        editbutt.setTitle("Edit".localized(), for: .normal)
        deletebutt.setTitle("Delete".localized(), for: .normal)
        if(Localize.currentLanguage().contains("ar") == true){
            backbutt.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
        OfferAddress.text = "Riyadh,Saudi arabia"
        print(offerId)
    }
  
    override func viewWillAppear(_ animated: Bool) {
        GetOfferDetails()
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
   
    
    @IBAction func deletButtonAction(_ sender: UIButton) {
        DeleteOffer()
      
    }
    
    func DeleteOffer(){
       print(offerId)
        self.showLoader()
        Api.DeleteOffer(offerid: offerId ?? 0){[weak self] (error : String? , success : Bool , message) in
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
    
    
    @IBAction func EditOfferButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Settings", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "EditOfferViewController") as! EditOfferViewController
        vc.offerid = offerId
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    
    func GetOfferDetails(){
        showLoader()
      
        Api.GetOfferDetails(offerid:offerId ?? 0){ [weak self](error: Error?, getofferdetails: General?,success,message) in
        self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
           
            if let getallservice = getofferdetails {
                self?.getofferdetails = getallservice
                
                let offername = self?.getofferdetails?.title
                self?.OfferName.text = offername
                
                let offerdescribe = self?.getofferdetails?.describeData
                self?.OfferDescribe.text = offerdescribe
                
             //   let adres = self?.servicedetails?.address
               // self?.OfferAddress.text = adres
              
                
                let price = self?.getofferdetails?.price
                self?.OfferPrice.text = "\(Int(price ?? 0))" + " " + "RS".localized()
               
                let time = self?.getofferdetails?.offer_duration_in_days
                self?.Offertime.text = "Completion Time:".localized() + "\(Int(time ?? 0))" + " " + "Day".localized()
                
                //handle image
//if let url = URL(string:self?.getofferdetails?.image ?? ""){
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

    
 
  
    





