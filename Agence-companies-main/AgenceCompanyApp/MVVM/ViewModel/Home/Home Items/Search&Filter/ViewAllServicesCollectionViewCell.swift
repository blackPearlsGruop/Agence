//
//  ViewAllServicesCollectionViewCell.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/06/2024.
//

import UIKit

class ViewAllServicesCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var servicename: UILabel!
    @IBOutlet weak var servicedescribe: UILabel!
    @IBOutlet weak var serviceimage: UIImageView!
    @IBOutlet weak var viewbutton: UIButton!
     var actionview: (()->())?
    override func awakeFromNib() {
        super.awakeFromNib()
      
        viewbutton.setTitle("View".localized(), for: .normal)
    }
    
    @IBAction func viewButton(_ sender: UIButton) {
        
        
        actionview?()
        
    }
}
