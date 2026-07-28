//
//  CompanyDetailsTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/06/2024.
//

import UIKit

class CompanyDetailsTableViewCell: UITableViewCell {
    @IBOutlet weak var offername: UILabel!
    @IBOutlet weak var offerdetails: UILabel!
    @IBOutlet weak var offerprice: UILabel!
    @IBOutlet weak var oviewdetailsbutton: UIButton!
    @IBOutlet weak var offerview: UIView!
    var actionview: (()->())?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        oviewdetailsbutton.setTitle("View Details".localized(), for: .normal)
    }
    @IBAction func viewButton(_ sender: UIButton) {
        
        actionview?()
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

   
}
