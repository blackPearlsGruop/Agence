//
//  RateViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//

import UIKit

class RateViewController: UIViewController {
    var rate:Int?
    var orderid:Int?
    var companyid:Int?
    @IBOutlet weak var ratetextview: UITextView!
    @IBOutlet weak var submitbutton: UIButton!
    @IBOutlet weak var button1: UIButton!
    @IBOutlet weak var button2: UIButton!
    @IBOutlet weak var button3: UIButton!
    @IBOutlet weak var button4: UIButton!
    @IBOutlet weak var button5: UIButton!
    @IBOutlet weak var ratelableloc: UILabel!
    @IBOutlet weak var ratecompanyloc: UILabel!
    @IBOutlet weak var ratebutton: UIButton!
    @IBOutlet weak var backbutt:UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        ratetextview.text = "Notes".localized()
        ratelableloc.text = "Rate".localized()
        ratecompanyloc.text = "Rate Company".localized()
        ratebutton.setTitle("Rate".localized(), for: .normal)
        if(Localize.currentLanguage().contains("ar") == true){
            backbutt.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    @IBAction func submittBut(_ sender: Any) {
        //rate func
        rateFunc()
    }
    var iconClick1:Bool = true
    @IBAction func oneBut(_ sender: Any) {
        rate = 1
        print(rate)
          
            button1.setImage(UIImage(named:"starfill"), for: .normal)

    }
    var iconClick2:Bool = true
    @IBAction func twoBut(_ sender: Any) {
        rate = 2
        print(rate)
            button2.setImage(UIImage(named:"starfill"), for: .normal)

    }
    var iconClick3:Bool = true
    @IBAction func threeBut(_ sender: Any) {
        rate = 3
        print(rate)
            button3.setImage(UIImage(named:"starfill"), for: .normal)

    }
    var iconClick4:Bool = true
    @IBAction func fourBut(_ sender: Any) {
        rate = 4
        print(rate)
            button4.setImage(UIImage(named:"starfill"), for: .normal)

    }
    var iconClick5:Bool = true
    @IBAction func fiveBut(_ sender: Any) {
        rate = 5
        print(rate)
            button5.setImage(UIImage(named:"starfill"), for: .normal)

    }
    
    func rateFunc(){
        print(orderid)
        print(companyid)
        print(rate)
        self.showLoader()
        Api.RateCompany(order_id:orderid ?? 0, company_id: companyid ?? 0, rate: rate ?? 1, review: ratetextview.text){[weak self] (error : String? , success : Bool , message) in
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
