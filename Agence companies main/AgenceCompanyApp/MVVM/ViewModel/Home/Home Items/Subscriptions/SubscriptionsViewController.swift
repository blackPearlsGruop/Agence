//
//  SubscriptionsViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit

class SubscriptionsViewController:UIViewController {
    @IBOutlet weak var subscribelabel: UILabel!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var subscribeTableView: UITableView!
    
    var subscribeArr = ["Yearly","Monthly","Yearly","Monthly","Yearly","Monthly"]
    

    override func viewDidLoad() {
        super.viewDidLoad()
        subscribelabel.text = "Subscriptions".localized()
        subscribeTableView.delegate = self
        subscribeTableView.dataSource = self
        subscribeTableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: subscribeTableView.frame.size.width, height: 1))
        
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
  
}

extension SubscriptionsViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
            return subscribeArr.count
        
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       
            let cell = subscribeTableView.dequeueReusableCell(withIdentifier: "SubscriptionsTableViewCell", for: indexPath) as!
            SubscriptionsTableViewCell
             cell.subscribetitle.text = subscribeArr[indexPath.row]
        //shdow for view
        cell.subscribeview.backgroundColor = UIColor.white
        cell.subscribeview.layer.masksToBounds = false
        cell.subscribeview.layer.shadowRadius = 3.0
        cell.subscribeview.layer.shadowColor = UIColor.gray.cgColor
        cell.subscribeview.layer.shadowOffset =  CGSize(width: 1, height: 3)
        cell.subscribeview.layer.shadowOpacity = 0.3
        cell.selectionStyle = .none
            return cell
    
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 274.0
    }

}
