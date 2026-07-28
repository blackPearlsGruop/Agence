//
//  NewOrdersTableViewCell.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit

class NewOrdersTableViewCell: UITableViewCell {
    @IBOutlet weak var categorytitle:UILabel!
    @IBOutlet weak var ordertitle:UILabel!
    @IBOutlet weak var ordernumber:UILabel!
    @IBOutlet weak var ordertype:UILabel!
    @IBOutlet weak var orderdate:UILabel!
    @IBOutlet weak var orderdetails:UILabel!
    @IBOutlet weak var orderduration:UILabel!
    @IBOutlet weak var orderview:UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
