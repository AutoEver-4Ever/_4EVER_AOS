package com.autoever.everp.domain.model.dashboard

data class DashboardWorkflows(
    val tabs: List<DashboardWorkflowTab>,
) {
    data class DashboardWorkflowTab(
        val tabCode: DashboardTapEnum,
        val id: String,
        val number: String,
        val description: String,
        val createdBy: String,
        val status: String,
//        val createdAt: LocalDate,
        val createdAt: String,
    )
}

