//
//  ChancesViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit

class ChancesViewController:UIViewController {
    @IBOutlet weak var chancelabel: UILabel!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var chnaceTableView: UITableView!
    
    var chanceArr = ["Entertainment Authority","Entertainment Authority","Entertainment Authority","Entertainment Authority","Entertainment Authority","Entertainment Authority"]
    

    override func viewDidLoad() {
        super.viewDidLoad()
        chancelabel.text = "Chances".localized()
        chnaceTableView.delegate = self
        chnaceTableView.dataSource = self
        chnaceTableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: chnaceTableView.frame.size.width, height: 1))
        
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

extension ChancesViewController:UITableViewDelegate,UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
            return chanceArr.count
        
    }
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
       
            let cell = chnaceTableView.dequeueReusableCell(withIdentifier: "ChancesTableViewCell", for: indexPath) as!
            ChancesTableViewCell
        cell.chancetitle.text = chanceArr[indexPath.row]
        //shdow for view
        cell.chanceview.backgroundColor = UIColor.white
        cell.chanceview.layer.masksToBounds = false
        cell.chanceview.layer.shadowRadius = 3.0
        cell.chanceview.layer.shadowColor = UIColor.gray.cgColor
        cell.chanceview.layer.shadowOffset =  CGSize(width: 1, height: 3)
        cell.chanceview.layer.shadowOpacity = 0.3
        cell.selectionStyle = .none
            return cell
    
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 225.0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let storyboard = UIStoryboard(name:"Home", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "ChancesDetailsViewController") as! ChancesDetailsViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }

}
