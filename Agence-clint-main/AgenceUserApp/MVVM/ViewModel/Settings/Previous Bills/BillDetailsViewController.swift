//
//  BillDetailsViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 06/07/2024.
//

import UIKit
import AlamofireImage
class BillDetailsViewController: UIViewController {
    var orderdetails:General?
    var detailsdata:General?
    var orderid:Int?
    var companyid:Int?
    var pricevalue:Int?
    var taxvalue:Int?
    @IBOutlet weak var orderdetailslabel: UILabel!
    @IBOutlet weak var billdetailstlabel: UILabel!
    @IBOutlet weak var backbutt:UIButton!
    @IBOutlet weak var amountbeforetaxlabelloc: UILabel!
    @IBOutlet weak var taxlabelloc: UILabel!
    @IBOutlet weak var totallabelloc: UILabel!
    @IBOutlet weak var amountbeforetaxlabelloc1: UILabel!
    @IBOutlet weak var taxlabelloc1: UILabel!
    @IBOutlet weak var totallabelloc1: UILabel!
    @IBOutlet weak var ordernumberloc: UILabel!
    @IBOutlet weak var companynameloc: UILabel!
    @IBOutlet weak var dateandtimeloc: UILabel!
    //Data
    @IBOutlet weak var amountbeforetaxlabel: UILabel!
    @IBOutlet weak var taxlabel: UILabel!
    @IBOutlet weak var totallabel: UILabel!
    @IBOutlet weak var amountbeforetaxlabel1: UILabel!
    @IBOutlet weak var taxlabel1: UILabel!
    @IBOutlet weak var totallabel1: UILabel!
    @IBOutlet weak var OrderNumber:UILabel!
    @IBOutlet weak var companyName:UILabel!
    @IBOutlet weak var companyDate:UILabel!
    @IBOutlet weak var companyTime:UILabel!
    @IBOutlet weak var companyPrice:UILabel!
   
    override func viewDidLoad() {
        super.viewDidLoad()
        dateandtimeloc.text = "Date And Time".localized()
        companynameloc.text = "Company Name".localized()
        billdetailstlabel.text = "Bill Details".localized()
        orderdetailslabel.text = "Order Details".localized()
        amountbeforetaxlabelloc.text = "Order Price".localized()
        taxlabelloc.text = "Tax".localized()
        totallabelloc.text = "Total".localized()
        amountbeforetaxlabelloc1.text = "Price".localized()
        taxlabelloc1.text = "Tax".localized()
        totallabelloc1.text = "Total".localized()
        
        
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
                
                let orderbefore = self?.pricevalue
                self?.amountbeforetaxlabel.text = "\(Int(orderbefore ?? 0))" + " " + "RS".localized()
                print(orderbefore)
                let ordertax = self?.taxvalue
                self?.taxlabel.text = "\(Int(ordertax ?? 0))" + " " + "RS".localized()
                print(ordertax)
                let total = "\(Int(orderbefore ?? 0))" + "\(Int(ordertax ?? 0))"
                self?.totallabel.text = total  + " " + "RS".localized()
                print(total)
             
                
                let orderbefore1 = self?.pricevalue
                self?.amountbeforetaxlabel1.text = "\(Int(orderbefore1 ?? 0))" + " " + "RS".localized()
                
                
                let ordertax1 = self?.taxvalue
                self?.taxlabel1.text = "\(Int(ordertax1 ?? 0))" + " " + "RS".localized()
                
                let total1 = "\(Int(orderbefore1 ?? 0))" + "\(Int(ordertax1 ?? 0))"
                self?.totallabel1.text = total1  + " " + "RS".localized()
                
                let ordernumber = self?.orderdetails?.order_number
                self?.OrderNumber.text = ordernumber
                
                let companyname = self?.orderdetails?.company_name
                self?.companyName.text = companyname
                
                
                let companydate = self?.orderdetails?.created_at
                self?.companyDate.text = companydate
                
                let companytime = self?.orderdetails?.created_at
                self?.companyTime.text = companytime
                
                let price = self?.orderdetails?.price_start_from
                self?.companyPrice.text = "\(Int(price ?? 0))" + " " + "RS".localized()
               
                
               
             
             
                
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
