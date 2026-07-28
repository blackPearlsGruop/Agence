//
//  ViewAllCompaniesTableViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 24/04/2024.
//

import UIKit

class ViewAllCompaniesTableViewCell: UITableViewCell {
    @IBOutlet weak var companyname: UILabel!
    @IBOutlet weak var companydescribe: UILabel!
    @IBOutlet weak var companyprice: UILabel!
    @IBOutlet weak var companyrate: UILabel!
    @IBOutlet weak var companyimg: UIImageView!
    @IBOutlet weak var categoryname: UILabel!
    @IBOutlet weak var viewbutton:UIButton!
    @IBOutlet weak var favbutton: UIButton!
    var actionview: (()->())?
    var actionfav: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        viewbutton.setTitle("View".localized(), for: .normal)
    }

    
    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
    @IBAction func favButton(_ sender: UIButton) {
        actionfav?()
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
