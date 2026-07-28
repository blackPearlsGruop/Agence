//
//  FirstPageViewController.swift
//  AgenceUserApp
//
//  Created by Eng Yoka on 12/02/2024.
//

import UIKit

class FirstPageViewController:UIViewController {
   // var introArr = [Home]()
   
    var introArr:[String] = ["1","2","3"]
    var introTitleArr :[String] = ["Get your work done in a click","A larger community with smarter services","Your market awaits"]
    var introDescribeArr :[String] = ["Complete your projects with easier communication and secure contracts tailored to your needs","A group of the best companies and industry experts in various fields","Your projects are ready in the shortest possible time and within a budget that suits you"]
    @IBOutlet weak var skipbutton:UIButton!
    @IBOutlet weak var nextbutton:UIButton!
    @IBOutlet weak var pagecontroller:UIPageControl!
    @IBOutlet weak var introcollectionView:UICollectionView!
    var currentPage = 0 {
        didSet {
            pagecontroller.currentPage = currentPage
           
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        
        introcollectionView.delegate = self
        introcollectionView.dataSource = self
      //  handleSplash()
    }
    
    @IBAction func nextButtonAction(_ sender: UIButton) {
        if currentPage == introArr.count - 1 {
            Helper.saveApiToken(value:"true",key:"isFirstlaunch")
                    pagecontroller.currentPage = currentPage
                    let storyboard = UIStoryboard(name: "Start", bundle:nil)
                    let vc = storyboard.instantiateViewController(withIdentifier: "ChoosePageViewController") as! ChoosePageViewController
                    self.navigationController?.pushViewController(vc, animated: true)
        } else {
            currentPage += 1
            let indexPath = IndexPath(item: currentPage, section: 0)
            introcollectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: true)
        }
    }
        

    
    @IBAction func skipButtonAction(_ sender: UIButton) {
        Helper.saveApiToken(value:"true",key:"isFirstlaunch")
        let storyboard = UIStoryboard(name: "Start", bundle:nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "ChoosePageViewController") as! ChoosePageViewController
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
//    func handleSplash(){
//        showLoader()
//        Api.getIntro{[weak self](error: Error?,getintro:[Home]?) in
//            self?.dismissLoader()
//            
//            if error != nil
//            {
//                print(error as Any)
//                
//            }
//            
//            if let getallintro = getintro {
//                self?.introArr = getallintro
//                self?.introcollectionView.reloadData()
//            }
//            
//            
//        }
//    }

    
}

//colection
extension FirstPageViewController:UICollectionViewDelegate,UICollectionViewDataSource ,UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
      
        return introArr.count
        
    }
    
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = introcollectionView.dequeueReusableCell(withReuseIdentifier: "IntroCollectionViewCell", for: indexPath) as!
        IntroCollectionViewCell
        
    cell.welcomlabel.text = introTitleArr[indexPath.row]
        cell.describelabel.text =  introDescribeArr[indexPath.row] //introArr[indexPath.row].describeData
        
        //handle image
//        if let url = URL(string:"\(introArr[indexPath.row].image)"){
//            print("\(url)")
//            cell.introimage.af_setImage(withURL:url)
//        }
//        else{
//            cell.introimage.image = UIImage(named:"welcome1")
//        }
     
        cell.introimage.image = UIImage(named:introArr[indexPath.row])
        
       
        return cell
    }

    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: collectionView.frame.height)
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let width = scrollView.frame.width
        currentPage = Int(scrollView.contentOffset.x / width)
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("Hello")
    }
    
    
}



