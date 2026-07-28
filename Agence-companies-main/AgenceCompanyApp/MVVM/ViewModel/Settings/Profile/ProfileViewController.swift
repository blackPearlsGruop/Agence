//
//  ProfileViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 13/02/2024.
//

import UIKit
import DropDown
import AlamofireImage
import PhotosUI
import Photos
import DropDown
class ProfileViewController: UIViewController, UITextFieldDelegate,UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITextViewDelegate {
    var imagePicked = 0
    var catId:Int?
    var categoryids:[Int] = []
    var alert = UIAlertController(title:"Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
    var picker = UIImagePickerController()
    var viewController: UIViewController?
    var FastOrderArray = [General]()
    @IBOutlet weak var saveBtn:UIButton!
    @IBOutlet weak var profilelabel: UILabel!
    @IBOutlet weak var logo: UIImageView!
    @IBOutlet weak var namelabel: UILabel!
    @IBOutlet weak var nameTF: UITextField!
    @IBOutlet weak var addressTF: UITextField!
    @IBOutlet weak var describeTF: UITextView!
    @IBOutlet weak var serviceTF: UITextField!
    @IBOutlet weak var serviceTypeBtn: UIButton!
    @IBOutlet weak var savebutton:UIButton!
    @IBOutlet weak var changeBtn:UIButton!
    @IBOutlet weak var backButton:UIButton!
    @IBOutlet weak var backgroundButton:UIButton!
    var getprofileinformation:General?
    var servicedropdown = DropDown()
    override func viewDidLoad() {
        super.viewDidLoad()
      //  saveBtn.setTitle("Save Image".localized(), for: .normal)
      
        
        serviceTF.inputView = servicedropdown
        servicedropdown.customCellConfiguration = { (index: Int, item: String, cell: DropDownCell) -> Void in
            cell.optionLabel.textAlignment = .center
        }
        servicedropdown.anchorView = serviceTypeBtn
        servicedropdown.direction = .bottom
        handleGetRegion()
        
        changeBtn.setTitle("Change Image".localized(), for: .normal)
      //  saveBtn.setTitle("Save Image".localized(), for: .normal)
        backgroundButton.setTitle("Add background image".localized(), for: .normal)
        savebutton.setTitle("Save".localized(), for: .normal)
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
    //handle getregion function
    func handleGetRegion(){
        showLoader()
       
        Api.AllCategories{[weak self](error: Error?,getcategory:[General]?,success,message) in
             self?.dismissLoader()
           
             if error != nil
             {
                 print(error as Any)
                 
             }
 
             if let getallcat = getcategory {
                 self?.FastOrderArray = getallcat
                 self?.servicedropdown.dataSource = getcategory?.compactMap({
                     $0.title
                   
                 }) ?? []
                               self?.servicedropdown.selectionAction = { [weak self] (index, item) in
                     self?.serviceTF.text = item
                   //  self?.title = item
                     self?.catId = getcategory?[index].id
                    self?.categoryids.append( self?.catId ?? 0)
                     print(getcategory?[index])
                     print( self?.catId )
                    
                 }

            
             
         }
     }
     }
    
    
    @IBAction func serviceBtnPressed(_ sender: Any) {
    
        servicedropdown.show()
      
    }
   
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    @IBAction func saveBtnAction(_ sender: UIButton) {
        let imagelogo =  logo.image
        
        showLoader()
        Api.EditProfile(name:nameTF.text ?? "", descriptiondata: describeTF.text ?? "", address: addressTF.text ?? "", serviceid:categoryids ?? [], company_logo:imagelogo!){[weak self](error:  Error? , success: Bool, message) in
            self?.dismissLoader()
            if success {
                self?.view.makeToast(message, duration: 3.0, position: .center)
                
            } else
            {
                self?.view.makeToast(message, duration: 3.0, position: .center)
            }
        }
        
    }
                        
    
    @IBAction func addbackgroundimageBtnAction(_ sender: UIButton) {
        
        let storyboard = UIStoryboard(name:"Settings", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "AddbackGroundImageViewController") as! AddbackGroundImageViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
        
    }

}
extension ProfileViewController {
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any])
    {
        let imagePicker = info[UIImagePickerController.InfoKey.originalImage] as? UIImage
       
        
        logo.image = imagePicker
       
       // saveBtn.isHidden = false
        
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
                
                let name = self?.getprofileinformation?.companyname
                self?.nameTF.text = name
                
                let namee = self?.getprofileinformation?.companyname
                self?.namelabel.text = namee
                
                let address = self?.getprofileinformation?.companyaddress
                self?.addressTF.text = address
                
                let desc = self?.getprofileinformation?.companydescribe
                self?.describeTF.text = desc
                
                let service =  self?.getprofileinformation?.companyservice
                self?.serviceTF.text = service
              
                //handle image
if let url = URL(string:self?.getprofileinformation?.companyimage ?? ""){
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
