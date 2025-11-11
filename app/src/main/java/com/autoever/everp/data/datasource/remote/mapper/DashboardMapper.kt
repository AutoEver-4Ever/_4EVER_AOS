package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.DashboardWorkflowsResponseDto
import com.autoever.everp.domain.model.dashboard.DashboardWorkflows

object DashboardMapper {
    fun toDomain(dto: DashboardWorkflowsResponseDto): DashboardWorkflows =
        DashboardWorkflows(
            tabs = dto.tabs.flatMap { tab ->
                tab.items.map { item ->
                    DashboardWorkflows.DashboardWorkflowTab(
                        tabCode = tab.tabCode,
                        id = item.itemId,
                        number = item.itemNumber,
                        description = item.itemTitle,
                        createdBy = item.name,
                        status = item.statusCode,
                        createdAt = item.date,
                    )
                }
            }
        )
}

