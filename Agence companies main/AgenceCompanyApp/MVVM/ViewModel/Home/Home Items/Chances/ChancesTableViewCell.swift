//
//  ChancesTableViewCell.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit

class ChancesTableViewCell:UITableViewCell {
    @IBOutlet weak var chancetitle:UILabel!
    @IBOutlet weak var chanceview:UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
