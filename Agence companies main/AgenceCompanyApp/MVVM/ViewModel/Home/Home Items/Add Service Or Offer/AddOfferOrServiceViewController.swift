//
//  AddOfferOrServiceViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit
import DropDown
import AlamofireImage
import PhotosUI
import Photos
class AddOfferOrServiceViewController: UIViewController, UITextFieldDelegate,UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITextViewDelegate  {
    var imagePicked = 0
    var alert = UIAlertController(title:"Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
    var picker = UIImagePickerController()
    var viewController: UIViewController?
    @IBOutlet weak var documentImage:UIImageView!
    @IBOutlet weak var uncheckbutton:UIButton!
    @IBOutlet weak var uncheck1button:UIButton!
    @IBOutlet weak var ServicenameTextField:UITextField!
    @IBOutlet weak var priceTextField:UITextField!
    @IBOutlet weak var documentTextField:UITextField!
    @IBOutlet weak var detailstextview:UITextField!
    @IBOutlet weak var addbutton:UIButton!
    //loc
    @IBOutlet weak var addofferornameloc:UILabel!
    @IBOutlet weak var serviceloc:UILabel!
    @IBOutlet weak var offerloc:UILabel!
    @IBOutlet weak var addimageloc:UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        //loc

      addbutton.setTitle("Add".localized(), for: .normal)
        addimageloc.text = "Add Image".localized()
        addofferornameloc.text = "Add Service Or Offer".localized()
        serviceloc.text = "Service".localized()
        offerloc.text = "Offer".localized()
        priceTextField.placeholder = "Price".localized()
        documentTextField.placeholder = "Impelementation period".localized()
       
        uncheckbutton.setImage(UIImage(named:"RadioButton"), for: .normal)
        ServicenameTextField.placeholder = "Service Name".localized()
        detailstextview.placeholder = "Service Details".localized()
    }
    
  
    var iconClick:Bool = true
    @IBAction func serviceButtonAction(_ sender: UIButton) {
    
        if iconClick {
           
            uncheckbutton.setImage(UIImage(named: "uncheck"), for: .normal)
         
            
        } else {
          
            uncheckbutton.setImage(UIImage(named:"RadioButton"), for: .normal)
            
               ServicenameTextField.placeholder = "Service Name".localized()
            detailstextview.placeholder = "Service Details".localized()
            uncheck1button.setImage(UIImage(named: "uncheck"), for: .normal)
        }
        iconClick = !iconClick
       

    }

    var iconClick1:Bool = true
    @IBAction func offerButtonAction(_ sender: UIButton) {
       
        if iconClick1 {
       
            uncheck1button.setImage(UIImage(named: "uncheck"), for: .normal)
          
          
        } else {
           
            uncheck1button.setImage(UIImage(named:"RadioButton"), for: .normal)
            detailstextview.placeholder = "Offer Details".localized()
            ServicenameTextField.placeholder = "Offer Name".localized()
            uncheckbutton.setImage(UIImage(named: "uncheck"), for: .normal)
        }
        iconClick1 = !iconClick1
        
    }
   
    @IBAction func addButtonAction(_ sender: UIButton) {
        if   ServicenameTextField.placeholder == "Offer Name".localized() {
            let imagelogo =  documentImage.image
            
            showLoader()
            Api.AddOfferToCompany(name:ServicenameTextField.text ?? "", descriptiondata:detailstextview.text ?? "", price:Int(priceTextField.text ?? "") ?? 0, offer_duration_in_days:Int(documentTextField.text ?? "") ?? 0, company_logo: imagelogo!){[weak self](error:  Error? , success: Bool, message) in
                self?.dismissLoader()
                if success {
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                    
                } else
                {
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                }
            }
        }
        else{
            let imagelogo =  documentImage.image
            showLoader()
    Api.AddServiceToCompany(name:ServicenameTextField.text ?? "", descriptiondata:detailstextview.text ?? "", price:Int(priceTextField.text ?? "") ?? 0, service_duration_in_days:Int(documentTextField.text ?? "") ?? 0, company_logo: imagelogo!){[weak self](error:  Error? , success: Bool, message) in
                self?.dismissLoader()
                if success {
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                    
                } else
                {
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                }
            }
        }
    }

}

extension AddOfferOrServiceViewController {
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any])
    {
        let imagePicker = info[UIImagePickerController.InfoKey.originalImage] as? UIImage
       
        
        documentImage.image = imagePicker
       
     
        
        self.dismiss(animated: true, completion: nil)
    }
    
    func openCamera(){
        if UIImagePickerController.isSourceTypeAvailable(.camera) {
            let imagePicker1 = UIImagePickerController()
            imagePicker1.delegate = self
            imagePicker1.sourceType = .camera;
            imagePicker1.allowsEditing = false
            self.present(imagePicker1, animated: true, completion: nil)
        }
     else {
         self.view.makeToast("your device not support camera".localized(), duration: 3.0, position: .top)
  
    }
    }
    
    func openGallery() {
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
            let imagePicker2 = UIImagePickerController()
            imagePicker2.delegate = self
            imagePicker2.sourceType = .photoLibrary;
            imagePicker2.allowsEditing = true
            self.present(imagePicker2, animated: true, completion: nil)
        }
    }
    
    func cancelsheet(){
        self.dismiss(animated:true)
    }
   
    @IBAction func ImgBtnAction(_ sender: Any) {
        let cameraAction1 = UIAlertAction(title: "Camera".localized(), style: .default){
                  UIAlertAction in
                  self.openCamera()
              }
        let galleryAction1 = UIAlertAction(title: "Gallery".localized(), style: .default){
                  UIAlertAction in
                  self.openGallery()
                  
              }
            
        
        let cancelAction1 = UIAlertAction(title: "Cancel".localized() , style: .cancel){
            UIAlertAction in
        }

        // Add the actions
        if alert.actions.count < 3 {
            picker.delegate = self
            alert.addAction(cameraAction1)
            alert.addAction(galleryAction1)
            alert.addAction(cancelAction1)
        }

        self.present(alert, animated: true, completion: nil)

      
        
    }
    
    
}
