//
//  AppDelegate.swift
//  AgenceCompanyApp
//
//  Created by Eng Yoka on 08/02/2024.
//

import UIKit
import GoogleMaps
import GooglePlaces
import Firebase
import FirebaseMessaging
import UserNotifications
import IQKeyboardManagerSwift
import SwiftyJSON
@main

class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate{
    var window: UIWindow?
    let gcmMessageIDKey = "gcm.message_id"
    let locationManager = CLLocationManager()
    //notification center for listen
    //receive notification for driver
    
    let receiveNotification = Notification(name: Notification.Name(Defaults.NotificationKeys.NotificationListen), object: nil, userInfo: nil)
    
    let AcceptNotification = Notification(name: Notification.Name(Defaults.NotificationKeys.AcceptNotificationListen), object: nil, userInfo: nil)
    
    let pushnotification =  Notification(name: Notification.Name(Defaults.NotificationKeys.PushNotificationListen), object: nil, userInfo: nil)
    
    let pushnotificationdriver =  Notification(name: Notification.Name(Defaults.NotificationKeys.PushNotificationListendriver), object: nil, userInfo: nil)
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        // keyboard
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.resignOnTouchOutside = true
        IQKeyboardManager.shared.keyboardDistanceFromTextField = 50
        IQKeyboardManager.shared.toolbarConfiguration.placeholderConfiguration.showPlaceholder = false
      
        let attributes = [NSAttributedString.Key.font : UIFont(name:"Somar-SemiBold", size:20), NSAttributedString.Key.foregroundColor : UIColor.white]
        let y = UINavigationBar.appearance()
        y.titleTextAttributes = attributes
        
        
        LanguageHandler.setDefaultLanguage(.english)
        self.setUIElementsAppearanceWith(semanticContent: Localize.isCurrentLanguageEnglish() ? .forceLeftToRight : .forceRightToLeft)
        
        
        //google maps
        GMSServices.provideAPIKey("AIzaSyA0Z9_o0V_KIayrf5ViLm6qf67IwwfRyuk")
        GMSPlacesClient.provideAPIKey("AIzaSyA0Z9_o0V_KIayrf5ViLm6qf67IwwfRyuk")
        
        
        
        
        
         FirebaseApp.configure()
        Messaging.messaging().delegate = self
        registerForNotifications()
        
        //  if #available(iOS 13.0, *) {
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: {_, _ in })
        
        application.registerForRemoteNotifications()
        
        
        
        
        if #available(iOS 14.0, *) {
        }
        registerRemoteNotificatins(application)
        hasNotificationsPermission { hasPermission in
            
            self.requestPermession()
            
        }
        UIApplication.shared.windows.forEach { window in
            window.overrideUserInterfaceStyle = .dark
        }
        for family: String in UIFont.familyNames {
            print(family)
            for names: String in UIFont.fontNames(forFamilyName: family) {
                print("== \(names)")
            }
        }
        UIFont.overrideInitialize()
        return true
    }
    
    func setUIElementsAppearanceWith(semanticContent: UISemanticContentAttribute) {
        UIView.appearance().semanticContentAttribute = semanticContent
        UIScrollView.appearance().semanticContentAttribute = semanticContent
        UILabel.appearance().semanticContentAttribute = semanticContent
        UIButton.appearance().semanticContentAttribute = semanticContent
        UIImageView.appearance().semanticContentAttribute = semanticContent
        UITableView.appearance().semanticContentAttribute = semanticContent
        UICollectionView.appearance().semanticContentAttribute = semanticContent
        UINavigationBar.appearance().semanticContentAttribute = semanticContent
        UITabBar.appearance().semanticContentAttribute = semanticContent
        //  UITextField.appearance().textAlignment = (semanticContent == .forceRightToLeft) ? .right : .left
        //  UITextView.appearance().textAlignment = (semanticContent == .forceRightToLeft) ? .right : .left
        UISearchBar.appearance().semanticContentAttribute = semanticContent
        UIStackView.appearance().semanticContentAttribute = semanticContent
        
        let item = UIBarButtonItem.appearance(whenContainedInInstancesOf: [UINavigationBar.self])
        UISegmentedControl.appearance().semanticContentAttribute = semanticContent
        UISwitch.appearance().semanticContentAttribute = semanticContent
        item.tintColor = .clear
        
        
    }
    func reset() {
        let rootViewController: UIWindow = ((UIApplication.shared.delegate?.window)!)!
        let story = UIStoryboard(name: "Main", bundle: nil)
        rootViewController.rootViewController = story.instantiateViewController(withIdentifier: "SplashNavigation")
    }
    // MARK: UISceneSession Lifecycle
    
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func registerRemoteNotificatins(_ application: UIApplication) {
        
        // MARK: Firabase
        UNUserNotificationCenter.current().delegate = self
        //    FirebaseApp.configure()
        
        // if #available(iOS 13.0, *) {
        /// For iOS 10 display notification (sent via APNS)
        UNUserNotificationCenter.current().delegate = self
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(options: authOptions, completionHandler: {_, _ in })
        //                    } else {
        //
        //                        let settings: UIUserNotificationSettings =
        //                            UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
        //                        application.registerUserNotificationSettings(settings)
        //                    }
        
        Messaging.messaging().isAutoInitEnabled = true
        Messaging.messaging().delegate = self
        
        application.registerForRemoteNotifications()
        
        
    }
    
    func hasNotificationsPermission(completion: @escaping (_ hasPermession: Bool) -> ()) {
        let center = UNUserNotificationCenter.current()
        center.getNotificationSettings(completionHandler: { settings in
            switch settings.authorizationStatus {
            case .authorized, .provisional:
                completion(true)
            case .notDetermined:
                completion(false)
            default:
                completion(true)
            }
        })
    }
    
    func requestPermession() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge], completionHandler: { (_, _) in })
    }
    
    func registerForNotifications() {
        // Register for notification: This will prompt for the user's consent to receive notifications from this app.
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { (granted, error) in
            //            print(granted)
        }
    }
    
    
    
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
    
    
    // [END receive_message]
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Unable to register for remote notifications: \(error.localizedDescription)")
    }
    
    // This function is added here only for debugging purposes, and can be removed if swizzling is enabled.
    // If swizzling is disabled then this function must be implemented so that the APNs token can be paired to
    // the FCM registration token.
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("APNs token retrieved: \(deviceToken)")
        
        // With swizzling disabled you must set the APNs token here.
        Messaging.messaging().apnsToken = deviceToken
    }
    
    
}

extension AppDelegate: MessagingDelegate {
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        
        print("Firebase registration token:\(fcmToken ?? "")")
        Helper.saveApiToken(value:fcmToken ?? "", key:"fcmToken")
        
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void)
    {
        let userInfo = response.notification.request.content.userInfo
        // ...
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print full message.
        print(userInfo)
        
        
        //            let tripiddriver = userInfo[AnyHashable("id")] as! String
        //            Helper.saveApiToken(value:"\(tripiddriver)",key:"TripIdData")
        //            print(tripiddriver)
        NotificationCenter.default.post(pushnotification)
        NotificationCenter.default.post(pushnotificationdriver)
        
        
        completionHandler()
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
#if DEBUG
        print("receiving a notification message")
        print(userInfo)
#endif
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
#if DEBUG
        print("receiving a notification message")
        print(userInfo)
#endif
        
        //    NotificationCenter.default.post(receiveNotification)
        //        completionHandler(UIBackgroundFetchResult.newData)
    }
}

extension AppDelegate {
    // Receive displayed notifications for iOS 10 devices.
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification) async
    -> UNNotificationPresentationOptions {
        let userInfo = notification.request.content.userInfo
        // Print full message.
        print(userInfo)
        //        let tripid = userInfo[AnyHashable("id")] as! String
        //        Helper.saveApiToken(value:"\(tripid)",key:"TripId")
        //        print(tripid)
        
        NotificationCenter.default.post(AcceptNotification)
        //receive notification for user
        NotificationCenter.default.post(receiveNotification)
        
        
        
        // Change this to your preferred presentation option
        return [[.alert, .sound]]
    }
    
    
    
    
    
}






class Defaults {
    struct NotificationKeys {
        static let fromlocationchange = "fromlocationchange"
        static let tolocationchange = "tolocationchange"
        static let NotificationListen = "NotificationListen"
        static let AcceptNotificationListen = "AcceptNotificationListen"
        static let PushNotificationListen = "PushNotificationListen"
        static let PushNotificationListendriver = "PushNotificationListendriver"
        
    }
}




