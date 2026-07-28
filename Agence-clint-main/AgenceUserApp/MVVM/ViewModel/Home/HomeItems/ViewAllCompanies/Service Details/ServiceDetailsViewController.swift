//
//  ServiceDetailsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 27/04/2024.
//

import UIKit
import AlamofireImage
import PLPaymentGateway
class ServiceDetailsViewController: UIViewController {
    var servicedetails:General?
    var orderid:Int?
    var gateway = PaylinkGateway(environment: .prod)
   @IBOutlet weak var offerdetailslabel: UILabel!
   @IBOutlet weak var servicecontentlabel: UILabel!
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
        self.gateway = PaylinkGateway(environment: PaylinkGateway.Environment.test,
                                paymentFormUrl: "https://merchants-website.com/mobile-payment-form.php",
                                platform: "ios")
        servicecontentlabel.text = "Service Content".localized()
        offerdetailslabel.text = "Service Details".localized()
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
    
    func showPaymentForm() {
            let transactionNo = "123456" // Example transaction number
        gateway.openPaymentForm(transactionNo: transactionNo, from: self, completion: { result in
                switch result {
                case .success(let (orderNumber, transactionNo)):
                    print("Order Number: \(orderNumber), Transaction No: \(transactionNo)")
                case .failure(let error):
                    print("Error: \(error.localizedDescription)")
                }
            }, loaded: {
                print("Payment form loaded successfully")
            })
        }
    
    @IBAction func PayButtonAction(_ sender: UIButton) {
        showPaymentForm()
//        let storyboard = UIStoryboard(name: "Home", bundle:nil)
//        let vc = storyboard.instantiateViewController(withIdentifier: "BillViewController") as! BillViewController
//        self.navigationController?.pushViewController(vc, animated: true)
        
//
            // Open Payment View Controller, transactionNo is received from the server side
//         gateway.openPaymentForm(transactionNo:"1wd2eodhiu23wfpjweof" , from:BillViewController()) {
//                result in
//                switch result {
//                    // After payment is completed (Paid or Declined), orderNumber
//                    // and transactionNo are returned.
//                case .success((let orderNumber, let transactionNo)):
//                    print("order number: \(3)")
//                    print("transaction no: \(transactionNo)")
//                    // Pass the transactionNo to the backend server to check the payment status
//                    // ...
//                    break;
//                case .failure(_):
//                    break;
//                }
//            } loaded: {
//                // .. code when the ViewController got loaded.
//            }
        }
    
    
    func GetOfferDetails(){
        showLoader()
        Api.GetServiceDetails(orderid:orderid ?? 0){ [weak self](error: Error?, getservicedetails: General?,success,message) in
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
                
                let adres = self?.servicedetails?.address
                self?.OfferAddress.text = adres
              
                
                let price = self?.servicedetails?.price_start_from
                self?.OfferPrice.text = "\(Int(price ?? 0))" + " " + "RS".localized()
               
                
                let rate = self?.servicedetails?.avg_rate
                self?.OfferRate.text = "\(Int(rate ?? 0))"
                
                //handle image
            if let url = URL(string:self?.servicedetails?.company_logo ?? ""){
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
