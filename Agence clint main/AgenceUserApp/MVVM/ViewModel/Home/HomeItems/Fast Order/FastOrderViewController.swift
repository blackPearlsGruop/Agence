//
//  FastOrderViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 25/02/2024.
//

import UIKit
import DropDown
class FastOrderViewController: UIViewController {
    var FastOrderArray = [General]()
    var catId:Int?
    var period:Int?
    var orderdetails:General?
    @IBOutlet weak var serviceTF: UITextField!
    @IBOutlet weak var orderaddressTF: UITextField!
    @IBOutlet weak var completionperiodTF: UITextField!
    @IBOutlet weak var completionperiodLabel: UILabel!
    @IBOutlet weak var orderDetailsTV: UITextView!
    @IBOutlet weak var serviceTypeBtn: UIButton!
    @IBOutlet weak var ordertitleloc: UILabel!
    @IBOutlet weak var completionperiodLabelloc: UILabel!
    @IBOutlet weak var orderdetailsloc: UILabel!
    @IBOutlet weak var quickorderloc: UILabel!
    @IBOutlet weak var sendtoallBtn: UIButton!
    @IBOutlet weak var backbutton: UIButton!
    var servicedropdown = DropDown()
    override func viewDidLoad() {
        super.viewDidLoad()
    
        serviceTF.addPadding(.both(10))
        orderaddressTF.addPadding(.both(10))
        completionperiodTF.addPadding(.both(10))
        ordertitleloc.text = "Order Title".localized()
        orderaddressTF.placeholder = "Order Title".localized()
        completionperiodLabelloc.text = "Completion Period".localized()
        completionperiodTF.placeholder = "Completion Period".localized()
        
        completionperiodLabel.text = "The completion period is calculated from the company’s acceptance of the order and payment".localized()
        serviceTF.placeholder = "Services".localized()
        orderdetailsloc.text = "Order Details".localized()
        sendtoallBtn.setTitle("Send to all".localized(), for: .normal)
        quickorderloc.text = "Quick Order".localized()
        
        //from regio
        serviceTF.inputView = servicedropdown
        servicedropdown.customCellConfiguration = { (index: Int, item: String, cell: DropDownCell) -> Void in
            cell.optionLabel.textAlignment = .center
        }
        servicedropdown.anchorView = serviceTypeBtn
        servicedropdown.direction = .bottom
        
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
        
        handleGetCategory()
        
    }
 
    
    //handle get Area function
    func handleGetCategory(){
        showLoader()
       
        Api.AllCategoy{[weak self](error: Error?,getcategory:[General]?,success,message) in
             self?.dismissLoader()
           
             if error != nil
             {
                 print(error as Any)
                 
             }
 
             if let getallcat = getcategory {
                 self?.FastOrderArray = getallcat
                 self?.servicedropdown.dataSource = getcategory?.compactMap({
                     $0.title
                   
                 }) ?? []
                               self?.servicedropdown.selectionAction = { [weak self] (index, item) in
                     self?.serviceTF.text = item
                   //  self?.title = item
                     self?.catId = getcategory?[index].id
                     print(getcategory?[index])
                     print( self?.catId )
                    
                 }

            
             
         }
     }
     }
    
    
    @IBAction func serviceBtnPressed(_ sender: Any) {
    
        servicedropdown.show()
      
    }
    
    @IBAction func backButton(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
      }
    
    @IBAction func sendbuttonButton(_ sender: UIButton) {
        period = Int(completionperiodTF.text ?? "")
        self.showLoader()
        Api.sendOrder(category_id: catId ?? 0, order_type:"quick", order_title:orderaddressTF.text ?? "", order_description:orderDetailsTV.text ?? "", order_duration_in_days:period ?? 0 ){[weak self] (error : String? , success : Bool , message) in
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
