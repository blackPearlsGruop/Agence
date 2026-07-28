//
//  AddWorksViewController.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 25/02/2024.
//

import UIKit
import AlamofireImage
class AddWorksViewController: UIViewController, UITextFieldDelegate,UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITextViewDelegate  {
    var imagePicked = 0
    var alert = UIAlertController(title:"Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
    var picker = UIImagePickerController()
    var viewController: UIViewController?
    var deletedRow = 0
    var WorkArr:[General] = []
    @IBOutlet weak var documentImage:UIImageView!
   @IBOutlet weak var workTableView: UITableView!
    @IBOutlet weak var backButton:UIButton!
    @IBOutlet weak var savebutton:UIButton!
    @IBOutlet weak var addworkslabel:UILabel!
    @IBOutlet weak var emptywork:UILabel!
    @IBOutlet weak var addimage:UILabel!
    @IBOutlet weak var addtitleTF:UITextField!
    @IBOutlet weak var adddescribeTF:UITextField!
   override func viewDidLoad() {
       super.viewDidLoad()
       addimage.text = "Add Image".localized()
       addtitleTF.placeholder = "Add Title".localized()
       adddescribeTF.placeholder = "Add Description".localized()
       emptywork.text = "No Works Added".localized()
       addworkslabel.text = "Add Works".localized()
       savebutton.setTitle("Add Work".localized(), for: .normal)
       addtitleTF.addPadding(.both(20))
       adddescribeTF.addPadding(.both(20))
       if(Localize.currentLanguage().contains("ar") == true){
           backButton.transform = CGAffineTransform(scaleX: -1.0, y: 1.0)
       } else{
           print("english")
           
       }
     
       workTableView.delegate =  self
       workTableView.dataSource =  self
       
    
   }
   
   override func viewWillAppear(_ animated: Bool) {
       getAllWorks()
   }
   
    
    @IBAction func backButtonAction(_ sender: UIButton) {
        navigationController?.popViewController(animated: true)
      
    }
    
    
    @IBAction func saveButtonAction(_ sender: UIButton) {
        let imagelogo =  documentImage.image
        
        showLoader()
        Api.AddWork(title:addtitleTF.text ?? "", descriptiondata: adddescribeTF.text ?? "", work_file:imagelogo!){[weak self](error:  Error? , success: Bool, message) in
            self?.dismissLoader()
            if success {
                self?.getAllWorks()
                self?.view.makeToast(message, duration: 3.0, position: .center)
                
            } else
            {
                self?.view.makeToast(message, duration: 3.0, position: .center)
            }
        }
        
    }
   
}
extension AddWorksViewController:UITableViewDelegate,UITableViewDataSource{
       func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
         
               return WorkArr.count
         
           
           
       }
       
       func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
      
               let cell = workTableView.dequeueReusableCell(withIdentifier: "AddWorksTableViewCell", for: indexPath) as!
           AddWorksTableViewCell
               
           cell.worktitle.text =  WorkArr[indexPath.row].title
           cell.workdescribe.text =  WorkArr[indexPath.row].describeData
           cell.workdate.text =  WorkArr[indexPath.row].created_at
            //handle image
           if let url = URL(string:"\(WorkArr[indexPath.row].work_file)"){
               print("\(url)")
              cell.workimage.af_setImage(withURL:url)
           }
           
               //shdow for view
               cell.workview.backgroundColor = UIColor.white
               cell.workview.layer.masksToBounds = false
               cell.workview.layer.shadowRadius = 3.0
               cell.workview.layer.shadowColor = UIColor.gray.cgColor
               cell.workview.layer.shadowOffset =  CGSize(width: 1, height: 3)
               cell.workview.layer.shadowOpacity = 0.3
               cell.selectionStyle = .none
             
              
               return cell
       

       }
       
   func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
      
           return 150.0
      
   }
   func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
       print("orders")
   }
     
    
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
    
            let action = UIContextualAction(style: .normal, title: nil) { action, view, completion in
                
                action.backgroundColor = .white
                print("Delete")
                self.deleteData(at:indexPath)
                
            }
            
            action.image = UIImage(named:"Delete")
            action.backgroundColor = .white
            
            let configuration = UISwipeActionsConfiguration(actions: [action])
            configuration.performsFirstActionWithFullSwipe = false
            
            return configuration
        
    }
 
     func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
       
                return true
          
     }


     func deleteData(at indexPath: IndexPath) {
         print(indexPath.row)
         deletedRow = indexPath.row
         self.performSelector(onMainThread: #selector(self.showDeletePopup), with: nil, waitUntilDone: false)
     }

    @objc func showDeletePopup() {
        print("delete")
      
    }
}



       
   


extension AddWorksViewController {
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



extension AddWorksViewController{
    
    //getAllWorks
    func getAllWorks(){
        showLoader()
        Api.getAllWorks{[weak self](error: Error?,allworks:[General]?,success,message) in
            self?.dismissLoader()
            if error != nil
            {
                print(error as Any)
                
            }
            
            if let getallworks = allworks {
                self?.WorkArr = getallworks
                self?.workTableView.reloadData()
            }
            
            
            if self?.WorkArr.count == 0 {
                self?.emptywork.isHidden = false
            }
            else{
                self?.emptywork.isHidden = true
            }
            
            if success{
                print("success")
                print(message)
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
                    print(message)
                    self?.view.makeToast(message, duration: 3.0, position: .center)
                }
            }
            
            
            if Reachability.isConnectedToNetwork() {
                print("Internet connection OK")
            } else {
                print("Internet connection FAILED")
                self?.view.makeToast("No Internet Connection,Make sure your device is connected to the internet. ", duration: 3.0, position: .top)
                
            }
        }
    }
    
    
    //Add Work
    
}
