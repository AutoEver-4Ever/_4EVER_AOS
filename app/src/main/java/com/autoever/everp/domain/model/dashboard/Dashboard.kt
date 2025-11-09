package com.autoever.everp.domain.model.dashboard

import java.time.LocalDate

data class DashboardWorkflows(
    val role: String,
    val tabs: List<DashboardWorkflowTab>,
) {
    data class DashboardWorkflowTab(
        val tabCode: String,
        val items: List<DashboardWorkflowItem>,
    )

    data class DashboardWorkflowItem(
        val workflowId: String,
        val count: Int,
        val workflowName: String,
        val name: String,
        val statusCode: String,
        val date: LocalDate,
    )
}

