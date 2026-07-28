//
//  Map.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 03/10/2023.
//

import Foundation
import GoogleMaps

private struct MapPath : Decodable{
    var routes : [Route]?
}

private struct Route : Decodable{
    var overview_polyline : OverView?
}

private struct OverView : Decodable {
    var points : String?
}

extension GMSMapView {

    //MARK:- Call API for polygon points

    func drawPolygon(from source: CLLocationCoordinate2D, to destination: CLLocationCoordinate2D){

        let config = URLSessionConfiguration.default
        let session = URLSession(configuration: config)

        guard let url = URL(string: "https://maps.googleapis.com/maps/api/directions/json?origin=\(source.latitude),\(source.longitude)&destination=\(destination.latitude),\(destination.longitude)&sensor=false&mode=driving&key=AIzaSyA0Z9_o0V_KIayrf5ViLm6qf67IwwfRyuk") else {
            return
        }

        DispatchQueue.main.async {

            session.dataTask(with: url) { (data, response, error) in

                guard data != nil else {
                    return
                }
                do {

                    let route = try JSONDecoder().decode(MapPath.self, from: data!)

                    if let points = route.routes?.first?.overview_polyline?.points {
                        self.drawPath(with: points)
                    }
                    print(route.routes?.first?.overview_polyline?.points)

                } catch let error {

                    print("Failed to draw ",error.localizedDescription)
                }
                }.resume()
            }
    }

    //MARK:- Draw polygon
    private func drawPath(with points : String){
        DispatchQueue.main.async {
            let path = GMSPath(fromEncodedPath: points)
            let polyline = GMSPolyline(path: path)
            polyline.strokeWidth = 5.0
            polyline.strokeColor =  UIColor.uicolorFromHex(000080, alpha: 1)
            polyline.map = self
        }
    }
}
extension UIColor
{
    class func uicolorFromHex(_ rgbValue:UInt32, alpha : CGFloat)->UIColor

    {
        let red = CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0
        let green = CGFloat((rgbValue & 0xFF00) >> 8) / 255.0
        let blue = CGFloat(rgbValue & 0xFF) / 255.0
        return UIColor(red:red, green:green, blue:blue, alpha: alpha)
    }
}
