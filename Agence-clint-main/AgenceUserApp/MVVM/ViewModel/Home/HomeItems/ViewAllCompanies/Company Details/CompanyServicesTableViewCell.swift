//
//  CompanyServicesTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 27/04/2024.
//

import UIKit

class CompanyServicesTableViewCell: UITableViewCell {
    @IBOutlet weak var servicename: UILabel!
    @IBOutlet weak var serviceprice: UILabel!
    @IBOutlet weak var serviceimage: UIImageView!
    @IBOutlet weak var serviceview: UIView!
    @IBOutlet weak var viewbutton: UIButton!
    var actionview: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        viewbutton.setTitle("View".localized(), for: .normal)
    }
    @IBAction func viewButton(_ sender: UIButton) {
        
        actionview?()
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
