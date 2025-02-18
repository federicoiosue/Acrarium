/*
 * (C) Copyright 2018 Lukas Morawietz (https://github.com/F43nd1r)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.faendir.acra.ui.view.bug

import com.faendir.acra.i18n.Messages
import com.faendir.acra.navigation.LogicalParent
import com.faendir.acra.navigation.PARAM_APP
import com.faendir.acra.navigation.PARAM_BUG
import com.faendir.acra.navigation.RouteParams
import com.faendir.acra.navigation.View
import com.faendir.acra.persistence.app.AppId
import com.faendir.acra.persistence.bug.BugId
import com.faendir.acra.persistence.bug.BugRepository
import com.faendir.acra.ui.component.tabs.TabView
import com.faendir.acra.ui.view.app.tabs.BugTab
import com.faendir.acra.ui.view.bug.tabs.AdminTab
import com.faendir.acra.ui.view.bug.tabs.IdentifierTab
import com.faendir.acra.ui.view.bug.tabs.ReportTab
import com.faendir.acra.ui.view.bug.tabs.StatisticsTab
import com.faendir.acra.ui.view.main.MainView
import com.vaadin.flow.router.NotFoundException
import com.vaadin.flow.router.ParentLayout
import com.vaadin.flow.router.RoutePrefix

/**
 * @author lukas
 * @since 08.09.18
 */
@View
@RoutePrefix("app/:$PARAM_APP/bug/:$PARAM_BUG")
@ParentLayout(MainView::class)
@LogicalParent(BugTab::class)
class BugView(
    bugRepository: BugRepository,
    routeParams: RouteParams,
) : TabView(
    (bugRepository.find(routeParams.bugId()) ?: throw NotFoundException()).title,
    TabInfo(ReportTab::class, Messages.REPORTS),
    TabInfo(IdentifierTab::class, Messages.STACKTRACES),
    TabInfo(StatisticsTab::class, Messages.STATISTICS),
    TabInfo(AdminTab::class, Messages.ADMIN)
) {
    companion object {
        fun getNavigationParams(appId: AppId, bugId: BugId) = mapOf(PARAM_APP to appId.toString(), PARAM_BUG to bugId.toString())
    }
}