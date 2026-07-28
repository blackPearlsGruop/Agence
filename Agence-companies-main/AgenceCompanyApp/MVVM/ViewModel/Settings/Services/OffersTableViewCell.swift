//
//  OffersTableViewCell.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/02/2024.
//

import UIKit

class OffersTableViewCell: UITableViewCell {
    @IBOutlet weak var offertitle:UILabel!
    @IBOutlet weak var offerprice:UILabel!
    @IBOutlet weak var offerdescribe:UILabel!
    @IBOutlet weak var offerview:UIView!
    @IBOutlet weak var viewbutton: UIButton!
    var actionview: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        viewbutton.setTitle("Details".localized(), for: .normal)
    }

    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

