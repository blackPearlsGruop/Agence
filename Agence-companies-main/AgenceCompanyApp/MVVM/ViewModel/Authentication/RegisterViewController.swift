//
//  RegisterViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 13/02/2024.
//
import UIKit
import DropDown
import AlamofireImage
import PhotosUI
import Photos

class RegisterViewController: UIViewController, UITextFieldDelegate,UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITextViewDelegate  {
    var categoryids:[Int] = []
    var type:String?
    var check:Int?
    var check1:String?
    var imagePicked = 0
    var FastOrderArray = [General]()
    var CountryArray = [General]()
    var catId:Int?
    var countryId:Int?
    var alert = UIAlertController(title:"Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
    var picker = UIImagePickerController()
    var viewController: UIViewController?
    @IBOutlet weak var documentImage:UIImageView!
    @IBOutlet weak var uncheckbutton:UIButton!
    @IBOutlet weak var uncheck1button:UIButton!
    @IBOutlet weak var backbutton:UIButton!
    @IBOutlet weak var checkbutton:UIButton!
    @IBOutlet weak var nationalview:UIView!
    @IBOutlet weak var phonetextfield:UITextField!
    @IBOutlet weak var nationaltextfield:UITextField!
    @IBOutlet weak var countrytextfield:UITextField!
    @IBOutlet weak var servicetextfield:UITextField!
    @IBOutlet weak var nametextfield:UITextField!
    @IBOutlet weak var documentlabel:UILabel!
    @IBOutlet weak var registerbutton:UIButton!
    @IBOutlet weak var backButton:UIButton!
    @IBOutlet weak var loginbutton:UIButton!
    @IBOutlet weak var termsbutton:UIButton!
    @IBOutlet weak var reglabel:UILabel!
    @IBOutlet weak var accountlabel:UILabel!
    @IBOutlet weak var agreelabel:UILabel!
    @IBOutlet weak var companylabel:UILabel!
    @IBOutlet weak var indvlabel:UILabel!
    @IBOutlet weak var serviceTypeBtn: UIButton!
    @IBOutlet weak var countryTypeBtn: UIButton!
    var servicedropdown = DropDown()
    var countrydropdown = DropDown()
    override func viewDidLoad() {
        super.viewDidLoad()
        uncheckbutton.setImage(UIImage(named:"RadioButton"), for: .normal)
        nametextfield.placeholder = "Name".localized()
        phonetextfield.placeholder = "Phone Number".localized()
        nationaltextfield.placeholder = "National Id".localized()
        countrytextfield.placeholder = "Countries".localized()
        servicetextfield.placeholder = "Service".localized()
        reglabel.text = "Register".localized()
        agreelabel.text = "I Agree".localized()
        companylabel.text = "Company".localized()
        indvlabel.text = "Individual".localized()
        documentlabel.text = "Identification document".localized()
        accountlabel.text = "Already have account ?".localized()
        termsbutton.setTitle("The terms and conditions".localized(), for: .normal)
        registerbutton.setTitle("Register".localized(), for: .normal)
        loginbutton.setTitle("Login".localized(), for: .normal)
        //service
        servicetextfield.inputView = servicedropdown
        servicedropdown.customCellConfiguration = { (index: Int, item: String, cell: DropDownCell) -> Void in
            cell.optionLabel.textAlignment = .center
        }
        servicedropdown.anchorView = serviceTypeBtn
        servicedropdown.direction = .bottom
        
        
        //country
        countrytextfield.inputView = countrydropdown
        countrydropdown.customCellConfiguration = { (index: Int, item: String, cell: DropDownCell) -> Void in
            cell.optionLabel.textAlignment = .center
        }
        countrydropdown.anchorView = countryTypeBtn
        countrydropdown.direction = .bottom
        
        
        if(Localize.currentLanguage().contains("ar") == true){
            backbutton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
        } else{
            print("english")
            
        }
        
        handleGetCategory()
        handleGetCity()
    }
 
    override func viewWillAppear(_ animated: Bool) {
        nationalview.isHidden = false
        type = "personal"
    }
    
    var iconClick:Bool = true
    @IBAction func individualButtonAction(_ sender: UIButton) {
    
        if iconClick {
           
            uncheckbutton.setImage(UIImage(named: "uncheck"), for: .normal)
         
        documentlabel.text = "Identification document".localized()
            type = "personal"
         
        } else {
          
            uncheckbutton.setImage(UIImage(named:"RadioButton"), for: .normal)
            nationalview.isHidden = false
            uncheck1button.setImage(UIImage(named: "uncheck"), for: .normal)
        }
        iconClick = !iconClick
       

    }
 
    var iconClick1:Bool = true
    @IBAction func companyButtonAction(_ sender: UIButton) {
       
        if iconClick1 {
       
            uncheck1button.setImage(UIImage(named: "uncheck"), for: .normal)
            documentlabel.text = "Commercial register certificate".localized()
            type = "company"
        } else {
           
            uncheck1button.setImage(UIImage(named:"RadioButton"), for: .normal)
            nationalview.isHidden = true
            uncheckbutton.setImage(UIImage(named: "uncheck"), for: .normal)
        }
        iconClick1 = !iconClick1
        
    }
    
    var bRec:Bool = true
    @IBAction func checkButtonAction(_ sender: UIButton) {
      
        bRec = !bRec
        if bRec {
            checkbutton.setImage(UIImage(named:"check"), for: .normal)
            check = 1
            check1 = "agree"
        }
        else{
            checkbutton.setImage(UIImage(named: "uncheck"), for: .normal)
            check1 = ""
        }
    }
    
    @IBAction func backButton(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
   
    @IBAction func loginButtonAction(_ sender: UIButton) {
       
        let storyboard =  UIStoryboard(name:"Login", bundle: nil)
        let userlogin = storyboard.instantiateViewController(withIdentifier:"LoginViewController") as! LoginViewController
        self.navigationController?.pushViewController(userlogin, animated: true)
        
        
      
    }
    
    @IBAction func registerButtonAction(_ sender: UIButton) {
        if ((self.nametextfield.text ?? "").trimmed.isEmpty) {
            self.MAK_ShowToast(message:"Name Required".localized())
            return
        }
        if ((self.phonetextfield.text ?? "").trimmed.isEmpty) {
            self.MAK_ShowToast(message:"Phone Required".localized())
            return
        }
       
        guard let data =  check1 ,!data.isEmpty  else {
            self.MAK_ShowToast(message:"Please accept terms and conditions".localized())
            return}
     
        
        self.showLoader()
    
        Api.register(account_type:type ?? "", name: nametextfield.text ?? "", phone: phonetextfield.text ?? "", country_id: 1, city_id:countryId ?? 0,accept_terms_and_conditions: check ?? 0, device_token: "xfsdffsrfe", commercial_licence:documentImage.image!, categyid:catId ?? 0, nationality_id:Int(nationaltextfield.text ?? "") ?? 0){[weak self](error: Error? , success: Bool, message) in
            self?.dismissLoader()
            if success{
                print("success")
                
                
                let storyboard = UIStoryboard(name: "Login", bundle:nil)
                let vc = storyboard.instantiateViewController(withIdentifier: "ConfirmCodeViewController") as! ConfirmCodeViewController
                vc.phonenumber = self?.phonetextfield.text
                self?.navigationController?.pushViewController(vc, animated: true)
                
            }
            else{
                
                self?.view.makeToast(message, duration: 3.0, position: .center)
            }
            
            if Reachability.isConnectedToNetwork() {
                print("Internet connection OK")
            } else {
                print("Internet connection FAILED")
                self?.view.makeToast("No Internet Connection,Make sure your device is connected to the internet. ".localized(), duration: 3.0, position: .top)
            }
            
            
        }
        
    }

}

extension RegisterViewController {
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

extension RegisterViewController{
    //handle get Area function
    func handleGetCategory(){
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
                     self?.servicetextfield.text = item
                   //  self?.title = item
                     self?.catId = getcategory?[index].id
          //  self?.categoryids.append( self?.catId ?? 0)
                   //  print(getcategory?[index])
                     print( self?.catId )
                    
                 }

            
             
         }
     }
     }
    
    
    @IBAction func serviceBtnPressed(_ sender: Any) {
    
        servicedropdown.show()
      
    }
    
    
    //city
    //handle get Area function
    func handleGetCity(){
        showLoader()
       
        Api.AllCities{[weak self](error: Error?,getcity:[General]?,success,message) in
             self?.dismissLoader()
           
             if error != nil
             {
                 print(error as Any)
                 
             }
 
             if let getallcity = getcity {
                 self?.CountryArray = getallcity
                 self?.countrydropdown.dataSource = getcity?.compactMap({
                     $0.title
                   
                 }) ?? []
                               self?.countrydropdown.selectionAction = { [weak self] (index, item) in
                     self?.countrytextfield.text = item
                   //  self?.title = item
                     self?.countryId = getcity?[index].id
                     print(getcity?[index])
                     print( self?.countryId )
                    
                 }

            
             
         }
     }
     }
    
    
    @IBAction func countryBtnPressed(_ sender: Any) {
    
        countrydropdown.show()
      
    }
}
