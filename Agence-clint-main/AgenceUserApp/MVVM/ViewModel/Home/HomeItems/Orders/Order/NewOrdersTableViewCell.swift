//
//  NewOrdersTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit

class NewOrdersTableViewCell: UITableViewCell {
    @IBOutlet weak var ordertitle:UILabel!
    @IBOutlet weak var ordernumber:UILabel!
    @IBOutlet weak var orderprice:UILabel!
    @IBOutlet weak var categoryname:UILabel!
    @IBOutlet weak var categorydescribe:UILabel!
    @IBOutlet weak var categoryimg:UIImageView!
    @IBOutlet weak var addressimg:UIImageView!
    @IBOutlet weak var orderview:UIView!
    @IBOutlet weak var ordernumberloc: UILabel!
    @IBOutlet weak var viewbutton: UIButton!
    @IBOutlet weak var chatbutton: UIButton!
    @IBOutlet weak var offerlabelloc:UILabel!
    var actionview: (()->())?
    var actionchat: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
        ordernumberloc.text = "Order number:".localized()
        viewbutton.setTitle("View".localized(), for: .normal)
        offerlabelloc.text = "No Offers Available".localized()
    }

    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
    @IBAction func chatButton(_ sender: UIButton) {
        
        
        actionchat?()
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
