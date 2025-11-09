package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.DashboardWorkflowsResponseDto
import com.autoever.everp.domain.model.dashboard.DashboardWorkflows

object DashboardMapper {
    fun toDomain(dto: DashboardWorkflowsResponseDto): DashboardWorkflows =
        DashboardWorkflows(
            role = dto.role,
            tabs = dto.tabs.map { tab ->
                DashboardWorkflows.DashboardWorkflowTab(
                    tabCode = tab.tabCode,
                    items = tab.items.map { item ->
                        DashboardWorkflows.DashboardWorkflowItem(
                            workflowId = item.workflowId,
                            count = item.count,
                            workflowName = item.workflowName,
                            name = item.name,
                            statusCode = item.statusCode,
                            date = item.data,
                        )
                    },
                )
            },
        )
}

