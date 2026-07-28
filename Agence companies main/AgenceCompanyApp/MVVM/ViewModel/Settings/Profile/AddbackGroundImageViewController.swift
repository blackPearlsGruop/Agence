//
//  AddbackGroundImageViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit
import DropDown
import AlamofireImage
import PhotosUI
import Photos
class AddbackGroundImageViewController: UIViewController, UITextFieldDelegate,UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITextViewDelegate {
    var imagePicked = 0
    var getprofileinformation:General?
    var alert = UIAlertController(title:"Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
    var picker = UIImagePickerController()
    var viewController: UIViewController?
    @IBOutlet weak var logo: UIImageView!
    @IBOutlet weak var changeBtn: UIButton!
    @IBOutlet weak var savebutton: UIButton!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var backlabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        changeBtn.setTitle("Change Image".localized(), for: .normal)
        savebutton.setTitle("Save".localized(), for: .normal)
        backlabel.text = "Add background image".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
       
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getprofileFunc()
    }
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }

}
extension AddbackGroundImageViewController {
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any])
    {
        let imagePicker = info[UIImagePickerController.InfoKey.originalImage] as? UIImage
       
        
        logo.image = imagePicker
       
       
        
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
    
    @IBAction func saveBtnAction(_ sender: UIButton) {
        let imagelogo =  logo.image
        
        showLoader()
        Api.EditBackground(company_background_image:imagelogo!){[weak self](error:  Error? , success: Bool, message) in
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


extension AddbackGroundImageViewController{
    func getprofileFunc()
    {
        showLoader()
        Api.GetProfileData{ [weak self](error: Error?, getprofile: General?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getprofiledata = getprofile{
                self?.getprofileinformation = getprofiledata
                
             
              
                //handle image
if let url = URL(string:self?.getprofileinformation?.companybackground ?? ""){
                    print("\(url)")
 
            self?.logo.af.setImage(withURL: url)
                }
                
                else {
                    self?.logo.image = UIImage(named:"")
                }
                
                
                
                
                
                
                
            }
            
            if success{
                print("success")
                self?.view.makeToast(message, duration: 3.0, position: .center)
                
            }
            
            else{
                
                if message  == "Unauthorized".localized(){
                    Helper.removeId()
                    Helper.removeAccessToken()
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                    let storyboard = UIStoryboard(name: "Login", bundle:nil)
                    let vc = storyboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
                    self?.navigationController?.pushViewController(vc, animated: true)
                }
                
                else
                {
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                }
            }
            
        }
        
    }

}
