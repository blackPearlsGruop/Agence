//
//  ChatViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 13/02/2024.
//

import UIKit

class ChatViewController: UIViewController {
    @IBOutlet weak var chatlabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()

        chatlabel.text = "Chat".localized()
    }
    

}
