//
//  ServicesTableViewCell.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/02/2024.
//

import UIKit

class ServicesTableViewCell: UITableViewCell {
    @IBOutlet weak var servicetitle:UILabel!
    @IBOutlet weak var servicimage:UIImageView!
    @IBOutlet weak var serviceprice:UILabel!
    @IBOutlet weak var serviceview:UIView!
    @IBOutlet weak var viewbutton: UIButton!
     var actionview: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
      
        viewbutton.setTitle("View".localized(), for: .normal)
    }
    
    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
}
