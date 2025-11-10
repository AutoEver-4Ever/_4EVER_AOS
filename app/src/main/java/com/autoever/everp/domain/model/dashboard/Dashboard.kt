package com.autoever.everp.domain.model.dashboard

import java.time.LocalDate

data class DashboardWorkflows(
    val tabs: List<DashboardWorkflowTab>,
) {
    data class DashboardWorkflowTab(
        val tabCode: DashboardTapEnum,
        val items: List<DashboardWorkflowItem>,
    )

    data class DashboardWorkflowItem(
        val id: String,
        val number: String,
        val description: String,
        val createdBy: String,
        val status: String,
//        val createdAt: LocalDate,
        val createdAt: String,
    )
}

