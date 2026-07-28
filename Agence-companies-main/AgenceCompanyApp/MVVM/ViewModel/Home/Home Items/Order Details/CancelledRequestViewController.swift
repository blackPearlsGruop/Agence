//
//  CancelledRequestViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit

class CancelledRequestViewController: UIViewController {
    var orderid:Int?
    var orderdetails:General?
    @IBOutlet weak var orderlabel: UILabel!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var categorytitle:UILabel!
    @IBOutlet weak var ordertitle:UILabel!
    @IBOutlet weak var ordernumber:UILabel!
    @IBOutlet weak var ordertype:UILabel!
    @IBOutlet weak var orderdate:UILabel!
    @IBOutlet weak var orderdetail:UITextView!
    @IBOutlet weak var orderduration:UILabel!
    @IBOutlet weak var orderdet:UILabel!


    override func viewDidLoad() {
        super.viewDidLoad()
        orderlabel.text = "Order Details".localized()
        orderdet.text = "Order Details".localized()
       
        
       
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
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
                //cattitle
                let cattitle = self?.orderdetails?.categorytitle
                self?.categorytitle.text = cattitle
                //ordertitle
                let ordertitle = self?.orderdetails?.title
                self?.ordertitle.text = ordertitle
                //ordernumber
                let ordernumber = "Order number:".localized() + " " +  (self?.orderdetails?.order_number ?? "")
                self?.ordernumber.text = ordernumber
                //orderdate
                let orderdate = self?.orderdetails?.created_at
                self?.orderdate.text = orderdate
               
                //orderdur
                let orderdur =  "Completion Time:".localized() + " " + "\(self?.orderdetails?.order_duration_in_days ?? 0)"  + " " +  "Day".localized()
                self?.orderduration.text = orderdur
                //orderdetails
                let orderdetails = self?.orderdetails?.describeData
                self?.orderdetail.text = orderdetails
                //ordertype
                let ordertype = self?.orderdetails?.order_type
                self?.ordertype.text = ordertype
                
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
