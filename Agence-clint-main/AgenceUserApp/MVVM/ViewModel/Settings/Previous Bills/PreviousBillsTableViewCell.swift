//
//  PreviousBillsTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 24/02/2024.
//

import UIKit

class PreviousBillsTableViewCell: UITableViewCell {
    @IBOutlet weak var ordertitle: UILabel!
    @IBOutlet weak var ordernumber: UILabel!
    @IBOutlet weak var categoryname: UILabel!
    @IBOutlet weak var categorydate: UILabel!
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
