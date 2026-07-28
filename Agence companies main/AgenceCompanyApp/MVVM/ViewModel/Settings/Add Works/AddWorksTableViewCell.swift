//
//  AddWorksTableViewCell.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/02/2024.
//

import UIKit

class AddWorksTableViewCell: UITableViewCell {
    @IBOutlet weak var worktitle:UILabel!
    @IBOutlet weak var workdescribe:UILabel!
    @IBOutlet weak var workdate:UILabel!
    @IBOutlet weak var workimage:UIImageView!
    @IBOutlet weak var workview:UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
