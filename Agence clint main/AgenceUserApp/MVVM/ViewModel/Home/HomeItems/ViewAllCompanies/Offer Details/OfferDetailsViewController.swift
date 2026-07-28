//
//  OfferDetailsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 27/04/2024.
//

import UIKit

class OfferDetailsViewController: UIViewController {
    var offerdetails:General?
    var orderid:Int?
   @IBOutlet weak var offerdetailslabel: UILabel!
   @IBOutlet weak var offercontentlabel: UILabel!
   @IBOutlet weak var backbutt:UIButton!
   @IBOutlet weak var paybutt:UIButton!
    //Data
    @IBOutlet weak var OfferImage:UIImageView!
    @IBOutlet weak var OfferName:UILabel!
    @IBOutlet weak var OfferAddress:UILabel!
    @IBOutlet weak var OfferRate:UILabel!
    @IBOutlet weak var OfferPrice:UILabel!
    @IBOutlet weak var OfferDescribe:UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        offercontentlabel.text = "Offer Content".localized()
        offerdetailslabel.text = "Offer Details".localized()
        paybutt.setTitle("Pay".localized(), for: .normal)
        if(Localize.currentLanguage().contains("ar") == true){
            backbutt.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
  
    override func viewWillAppear(_ animated: Bool) {
        GetOfferDetails()
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    @IBAction func PayButtonAction(_ sender: UIButton) {
      
        let storyboard = UIStoryboard(name: "Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "BillViewController") as! BillViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    func GetOfferDetails(){
        showLoader()
        Api.GetOfferDetails(orderid:orderid ?? 0){ [weak self](error: Error?, getofferdetails: General?,success,message) in
        self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
           
            if let getalldata = getofferdetails{
                self?.offerdetails = getalldata
                let offername = self?.offerdetails?.title
                self?.OfferName.text = offername
                
                let offerdescribe = self?.offerdetails?.describeData
                self?.OfferDescribe.text = offerdescribe
                
                let adres = self?.offerdetails?.address
                self?.OfferAddress.text = adres
              
                
                let price = self?.offerdetails?.price_start_from
                self?.OfferPrice.text = "\(Int(price ?? 0))" + " " + "RS".localized()
               
                
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
    

}
