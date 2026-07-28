//
//  CompaniesCollectionViewCell.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 22/02/2024.
//

import UIKit

class CompaniesCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var companyname: UILabel!
    @IBOutlet weak var companydescribe: UILabel!
    @IBOutlet weak var companyprice: UILabel!
    @IBOutlet weak var companyrate: UILabel!
    @IBOutlet weak var companyimg: UIImageView!
    @IBOutlet weak var categoryname: UILabel!
    @IBOutlet weak var viewbutton: UIButton!
    @IBOutlet weak var favbutton: UIButton!
    var actionview: (()->())?
    var actionfav: (()->())?
   
    override func awakeFromNib() {
        super.awakeFromNib()
      
        viewbutton.setTitle("View".localized(), for: .normal)
    }
    
    @IBAction func favButton(_ sender: UIButton) {
        actionfav?()
        
    }
    
    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
}

