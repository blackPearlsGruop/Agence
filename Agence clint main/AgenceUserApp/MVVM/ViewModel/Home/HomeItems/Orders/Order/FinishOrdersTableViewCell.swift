//
//  FinishOrdersTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit

class FinishOrdersTableViewCell: UITableViewCell {
    @IBOutlet weak var ordertitle:UILabel!
    @IBOutlet weak var ordernumber:UILabel!
    @IBOutlet weak var orderprice:UILabel!
    @IBOutlet weak var categoryname:UILabel!
    @IBOutlet weak var categorydescribe:UILabel!
    @IBOutlet weak var categoryimg:UIImageView!
    @IBOutlet weak var orderview:UIView!
    @IBOutlet weak var ordernumberloc: UILabel!
    @IBOutlet weak var viewbutton: UIButton!
    @IBOutlet weak var reorderbutton: UIButton!
    var actionview: (()->())?
    var actionreorder: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
        ordernumberloc.text = "Order number:".localized()
        viewbutton.setTitle("View".localized(), for: .normal)
        reorderbutton.setTitle("Reorder".localized(), for: .normal)
    }
    
    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
    @IBAction func reorderButton(_ sender: UIButton) {
        
        
        actionreorder?()
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
