//
//  OffersTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit

class OffersTableViewCell: UITableViewCell {
    @IBOutlet weak var offername: UILabel!
    @IBOutlet weak var offerdescribe: UILabel!
    @IBOutlet weak var offerprice: UILabel!
    @IBOutlet weak var ordernumber: UILabel!
    @IBOutlet weak var offerview: UIView!
    @IBOutlet weak var ordernumberloc: UILabel!
    @IBOutlet weak var viewbutton: UIButton!
    @IBOutlet weak var acceptbutton: UIButton!
    @IBOutlet weak var refusebutton: UIButton!
    var actionview: (()->())?
    var actionaccept: (()->())?
    var actionrefuse: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
        ordernumberloc.text = "Order number:".localized()
        viewbutton.setTitle("View".localized(), for: .normal)
        acceptbutton.setTitle("Accept".localized(), for: .normal)
        refusebutton.setTitle("Refuse".localized(), for: .normal)
    }
   
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
    
    @IBAction func acceptButton(_ sender: UIButton) {
        
        
        actionaccept?()
        
    }
    
    @IBAction func refuseButton(_ sender: UIButton) {
        
        
        actionrefuse?()
        
    }
}
