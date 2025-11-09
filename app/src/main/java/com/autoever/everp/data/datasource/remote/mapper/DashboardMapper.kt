package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.DashboardWorkflowsResponseDto
import com.autoever.everp.domain.model.dashboard.DashboardWorkflows

object DashboardMapper {
    fun toDomain(dto: DashboardWorkflowsResponseDto): DashboardWorkflows =
        DashboardWorkflows(
            tabs = dto.tabs.map { tab ->
                DashboardWorkflows.DashboardWorkflowTab(
                    tabCode = tab.tabCode,
                    items = tab.items.map { item ->
                        DashboardWorkflows.DashboardWorkflowItem(
                            id = item.itemId,
                            number = item.itemNumber,
                            description = item.itemTitle,
                            createdBy = item.name,
                            status = item.statusCode,
                            createdAt = item.date,
                        )
                    },
                )
            },
        )
}

