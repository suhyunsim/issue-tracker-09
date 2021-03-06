//
//  MileStoneTableViewController.swift
//  IssueTracker
//
//  Created by Cloud on 2020/06/27.
//  Copyright © 2020 Cloud. All rights reserved.
//

import UIKit

final class MileStoneTableViewController: CategoryTableViewController {
    
    // MARK: - Properties
    private let headerViewTitle: String = "MileStone"
    let dataSource: MileStoneTableViewDataSource = .init()
    
    // MARK: - Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        registerCell(MileStoneTableViewCell.self,
                     identifier: MileStoneTableViewCell.identifier)
        tableView.dataSource = dataSource
    }
    
    // MARK: - Methods
    // MARK: Delegate
    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = tableView.dequeueReusableHeaderFooterView(withIdentifier: TitleHeaderView.identifier) as? TitleHeaderView
        headerView?.apply(title: headerViewTitle)
        headerView?.addButton.addTarget(self,
                                        action: #selector(presentCreateMileStoneViewController),
                                        for: .touchUpInside)
        
        return headerView
    }
    
    // MARK: Objc
    @objc func presentCreateMileStoneViewController() {
        // MARK: - Todo 생성 기능 구현
    }
}
