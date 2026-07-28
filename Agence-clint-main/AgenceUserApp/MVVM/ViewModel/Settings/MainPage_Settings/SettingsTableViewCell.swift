//
//  SettingsTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit

class SettingsTableViewCell: UITableViewCell {
    @IBOutlet weak var settingtitle:UILabel!
    @IBOutlet weak var settingimg:UIImageView!
    @IBOutlet weak var arrow:UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
