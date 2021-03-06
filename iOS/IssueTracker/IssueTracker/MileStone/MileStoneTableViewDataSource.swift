//
//  MileStoneTableViewDataSource.swift
//  IssueTracker
//
//  Created by Cloud on 2020/06/27.
//  Copyright © 2020 Cloud. All rights reserved.
//

import UIKit

final class MileStoneTableViewDataSource: NSObject {
    // MARK: - Properties
    @Published var mileStones:[MileStone] = .init()
}

// MARK: - Extension
// MARK: TableViewDataSource
extension MileStoneTableViewDataSource: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return mileStones.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: MileStoneTableViewCell.identifier, for: indexPath) as? MileStoneTableViewCell else { return UITableViewCell() }

        return cell
    }
}
