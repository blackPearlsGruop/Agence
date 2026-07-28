//
//  ProfileViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 13/02/2024.
//

import UIKit
import DropDown
import AlamofireImage
import PhotosUI
import Photos

class ProfileViewController: UIViewController, UITextFieldDelegate,UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITextViewDelegate {
    var imagePicked = 0
    var alert = UIAlertController(title:"Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
    var picker = UIImagePickerController()
    var viewController: UIViewController?
    @IBOutlet weak var saveBtn:UIButton!
    @IBOutlet weak var passtextfield:UITextField!
    @IBOutlet weak var nametextfield:UITextField!
    @IBOutlet weak var phonetextfield:UITextField!
    @IBOutlet weak var passbutton:UIButton!
    @IBOutlet weak var profilelabel: UILabel!
    @IBOutlet weak var logo: UIImageView!
    @IBOutlet weak var namelabel: UILabel!
    @IBOutlet weak var savebutton:UIButton!
    @IBOutlet weak var changeBtn:UIButton!
    @IBOutlet weak var backButton:UIButton!
    var getprofileinformation:General?
    override func viewDidLoad() {
        super.viewDidLoad()
        changeBtn.setTitle("Change Image".localized(), for: .normal)
        saveBtn.setTitle("Save Image".localized(), for: .normal)
        savebutton.setTitle("Save".localized(), for: .normal)
        passtextfield.isSecureTextEntry = true
        profilelabel.text = "Profile".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    override func viewWillAppear(_ animated: Bool) {
        saveBtn.isHidden = true
        getprofileFunc()
    }
  
    var iconClick:Bool = true
    @IBAction func passButtonAction(_ sender: UIButton) {
        if iconClick {
              passtextfield.isSecureTextEntry = false
            passbutton.setImage(UIImage(named: "pass"), for: .normal)
           } else {
               passtextfield.isSecureTextEntry = true
               passbutton.setImage(UIImage(named:"pass"), for: .normal)
           }
           iconClick = !iconClick
       }
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    @IBAction func saveBtnAction(_ sender: UIButton) {
        let imagelogo =  logo.image
        if ((self.nametextfield.text ?? "").trimmed.isEmpty) {
            
            self.view.makeToast("Name Required".localized(), duration: 3.0, position: .top)
            return
        }
       
        if ((self.phonetextfield.text ?? "").trimmed.isEmpty) {
        
            self.view.makeToast("Phone Required".localized(), duration: 3.0, position: .top)
            return
        }
        
        guard let phonedata = phonetextfield.text?.trimmed, !phonedata.isEmpty else {
            self.view.makeToast("Invalid Phone Number".localized(), duration: 3.0, position: .top)
            return}
       
        if phonedata.count < 9 || phonedata.count > 15 {
            self.view.makeToast("Phone should start with 5 and not less than 10 numbers".localized(), duration: 3.0, position: .top)
            return
        }
        
       
        showLoader()
        Api.EditProfile(name:nametextfield.text ?? "", phone:phonetextfield.text ?? "", profile_image:imagelogo!, _method:"PUT"){[weak self](error:  Error? , success: Bool, message) in
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
extension ProfileViewController {
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any])
    {
        let imagePicker = info[UIImagePickerController.InfoKey.originalImage] as? UIImage
       
        
        logo.image = imagePicker
       
      //  saveBtn.isHidden = false
        
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

extension ProfileViewController{
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
                
                let name = self?.getprofileinformation?.name
                self?.nametextfield.text = name
                
                let namee = self?.getprofileinformation?.name
                self?.namelabel.text = namee
                
                let phone = self?.getprofileinformation?.phone
                self?.phonetextfield.text = phone
              
                //handle image
if let url = URL(string:self?.getprofileinformation?.image ?? ""){
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
