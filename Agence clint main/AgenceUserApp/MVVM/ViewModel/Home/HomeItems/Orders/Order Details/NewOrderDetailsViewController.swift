//
//  NewOrderDetailsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//

import UIKit
import AlamofireImage
class NewOrderDetailsViewController: UIViewController {
    var orderdetails:General?
    var orderid:Int?
    
   @IBOutlet weak var orderdetailslabel: UILabel!
   @IBOutlet weak var oordercontentlabel: UILabel!
   @IBOutlet weak var backbutt:UIButton!
   @IBOutlet weak var chatbutt:UIButton!
    @IBOutlet weak var amountbeforetaxlabelloc: UILabel!
    @IBOutlet weak var taxlabelloc: UILabel!
    @IBOutlet weak var totallabelloc: UILabel!
    @IBOutlet weak var ordernumberloc: UILabel!
    //Data
    @IBOutlet weak var amountbeforetaxlabel: UILabel!
    @IBOutlet weak var taxlabel: UILabel!
    @IBOutlet weak var totallabel: UILabel!
    @IBOutlet weak var OrderName:UILabel!
    @IBOutlet weak var OrderNumber:UILabel!
    @IBOutlet weak var companyImage:UIImageView!
    @IBOutlet weak var companyName:UILabel!
    @IBOutlet weak var companyAddress:UILabel!
    @IBOutlet weak var companyDate:UILabel!
    @IBOutlet weak var companyPrice:UILabel!
    @IBOutlet weak var companyDescribe:UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        oordercontentlabel.text = "Order Content".localized()
        orderdetailslabel.text = "Order Details".localized()
        chatbutt.setTitle("Chat With Company".localized(), for: .normal)
        amountbeforetaxlabelloc.text = "Amount before tax".localized()
        taxlabelloc.text = "Tax".localized()
        totallabelloc.text = "Total".localized()
        ordernumberloc.text = "Order number:".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backbutt.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
  
    override func viewWillAppear(_ animated: Bool) {
        GetOrderDetails()
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    @IBAction func chatButtonAction(_ sender: UIButton) {
      
        
    }
    
    func GetOrderDetails(){
        showLoader()
        Api.GetOrderDetails(orderid:orderid ?? 0){ [weak self](error: Error?, getorderdetails: General?,success,message) in
        self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
           
            if let getalldata = getorderdetails{
                self?.orderdetails = getalldata
                let ordername = self?.orderdetails?.order_title
                self?.OrderName.text = ordername
                
                let ordernumber = self?.orderdetails?.order_number
                self?.OrderNumber.text = ordernumber
                
                let companydescribe = self?.orderdetails?.describeData
                self?.companyDescribe.text = companydescribe
                
                let adres = self?.orderdetails?.address
                self?.companyAddress.text = adres
              
                let companyname = self?.orderdetails?.company_name
                self?.companyName.text = companyname
                
                
                let companydate = self?.orderdetails?.created_at
                self?.companyDate.text = companydate
                
                
                let price = self?.orderdetails?.price_start_from
                self?.companyPrice.text = "\(Int(price ?? 0))" + " " + "RS".localized()
               
                
               
                
                //handle image
            if let url = URL(string:self?.orderdetails?.company_logo ?? ""){
                    print("\(url)")
                self?.companyImage.af_setImage(withURL: url)
                }
                
                else {
                    self?.companyImage.image = UIImage(named:"")
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
