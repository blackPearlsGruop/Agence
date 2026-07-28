//
//  NotificationsTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 23/02/2024.
//

import UIKit

class NotificationsTableViewCell: UITableViewCell {
    @IBOutlet weak var notifytitlelabel: UILabel!
    @IBOutlet weak var notifydescribelabel: UILabel!
    @IBOutlet weak var notifydatelabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
