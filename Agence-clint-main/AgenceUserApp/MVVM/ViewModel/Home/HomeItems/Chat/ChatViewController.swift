//
//  ChatViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 28/04/2024.
//

import UIKit

class ChatViewController: UIViewController {
    @IBOutlet weak var chatlabel: UILabel!
    @IBOutlet weak var cominglabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()

        chatlabel.text = "Chat".localized()
        cominglabel.text = "Coming Soon".localized()
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
