//
//  BehaviorRelay.swift
//  Nfhasha App
//
//  Created by Eng Yoka on 04/10/2023.
//

import Foundation
import RxCocoa
import RxSwift
import UIKit

/// BehaviorRelay is a wrapper for `BehaviorSubject`.
///
/// Unlike `BehaviorSubject` it can't terminate with error or completed.
public final class BehaviorRelay<Element>: ObservableType {
    private let subject: BehaviorSubject<Element>

    /// Accepts `event` and emits it to subscribers
    public func accept(_ event: Element) {
        self.subject.onNext(event)
    }

    /// Current value of behavior subject
    public var value: Element {
        // this try! is ok because subject can't error out or be disposed
        return try! self.subject.value()
    }

    /// Initializes behavior relay with initial value.
    public init(value: Element) {
        self.subject = BehaviorSubject(value: value)
    }

    /// Subscribes observer
    public func subscribe<Observer: ObserverType>(_ observer: Observer) -> Disposable where Observer.Element == Element {
        self.subject.subscribe(observer)
    }

    /// - returns: Canonical interface for push style sequence
    public func asObservable() -> Observable<Element> {
        self.subject.asObservable()
    }
}
