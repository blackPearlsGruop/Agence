//
//  EditServiceViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 19/03/2024.
//

import UIKit
import DropDown
import AlamofireImage
import PhotosUI
import Photos

class EditServiceViewController: UIViewController, UITextFieldDelegate,UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITextViewDelegate {
    var imagePicked = 0
    var service_id:Int?
    var servicedetails:General?
    var alert = UIAlertController(title:"Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
    var picker = UIImagePickerController()
    var viewController: UIViewController?
    @IBOutlet weak var logo: UIImageView!
    @IBOutlet weak var backbutt: UIButton!
    @IBOutlet weak var editbutt: UIButton!
    @IBOutlet weak var editservicelabel: UILabel!
    //Data
    @IBOutlet weak var servicename: UITextField!
    @IBOutlet weak var serviceprice: UITextField!
    @IBOutlet weak var implementationperiod: UITextField!
    @IBOutlet weak var servicedetail: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        editbutt.setTitle("Edit".localized(), for: .normal)
        editservicelabel.text = "Edit Service".localized()
        if(Localize.currentLanguage().contains("ar") == true){
            backbutt.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        GetServiceDetails()
    }

    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
}
extension EditServiceViewController {
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
    
    
}


extension EditServiceViewController {
   func GetServiceDetails(){
    showLoader()
   print(service_id)
    Api.GetServiceDetails(serviceid:service_id ?? 0){ [weak self](error: Error?, getservicedetails: General?,success,message) in
    self?.dismissLoader()
        if error != nil
        {
            print(error as Any)
            
        }
       
        if let getallservice = getservicedetails {
            self?.servicedetails = getallservice
            
            let offername = self?.servicedetails?.title
            self?.servicename.text = offername
            
            let offerdescribe = self?.servicedetails?.describeData
            self?.servicedetail.text = offerdescribe
            
         //   let adres = self?.servicedetails?.address
           // self?.OfferAddress.text = adres
          
            
            let price = self?.servicedetails?.price
            self?.serviceprice.text = "\(Int(price ?? 0))" + " " + "RS".localized()
           
            let time = self?.servicedetails?.service_duration_in_days
            self?.implementationperiod.text = "\(Int(time ?? 0))" + " " + "Day".localized()
            
      
            
//            //handle image
//        if let url = URL(string:self?.servicedetails?.image ?? ""){
//                print("\(url)")
//            self?.OfferImage.af_setImage(withURL: url)
//            }
//            
//            else {
//                self?.OfferImage.image = UIImage(named:"")
//            }
            
         
            
        }
        
        if success{
            print("success")
            self?.view.makeToast(message, duration: 3.0, position: .center)
      
    }

        else{
            
        if message  == "Unauthenticated user".localized(){
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


    @IBAction func editbuttonButton(_ sender: UIButton) {
      
        self.showLoader()
        Api.EditService(serviceid:service_id ?? 0, title:servicename.text ?? "", description: servicedetail.text ?? "", price:Int(serviceprice.text ?? "") ?? 0, service_duration_in_days:Int(implementationperiod.text ?? "") ?? 0, _method:"PUT"){[weak self] (error : String? , success : Bool , message) in
            self?.dismissLoader()
            if success {
            
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                self?.navigationController?.popViewController(animated:true)
              
            } else {
                
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

