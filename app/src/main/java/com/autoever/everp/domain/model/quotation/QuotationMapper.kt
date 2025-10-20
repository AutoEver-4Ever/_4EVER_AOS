package com.autoever.everp.domain.model.quotation

import java.time.LocalDate

fun Quotation.toQuotationListItem(): QuotationListItem =
    QuotationListItem(
        id = this.id,
        code = "QUO-${this.id}", // 예시로 견적서 코드를 생성
        customerName = this.customerName,
        managerName = "담당자 이름", // 담당자 이름은 별도로 매핑 필요
        status = "발행됨", // 상태 값은 별도로 매핑 필요
        issueDate = LocalDate.now(), // 발행일은 현재 날짜로 설정
        dueDate =
            LocalDate
                .now()
                .plusDays(30),
        // 납기일은 발행일로부터 30일 후로 설정
        totalAmount = this.amount.toInt(),
    )

fun Quotation.toQuotationDetail(): QuotationDetail =
    QuotationDetail(
        id = this.id,
        code = "QUO-${this.id}", // 예시로 견적서 코드를 생성
        customerName = this.customerName,
        managerName = "담당자 이름", // 담당자 이름은 별도로 매핑 필요
        status = "발행됨", // 상태 값은 별도로 매핑 필요
        issueDate =
            LocalDate
                .now()
                .toString(),
        // 발행일은 현재 날짜로 설정
        dueDate =
            LocalDate
                .now()
                .plusDays(30)
                .toString(),
        // 납기일은 발행일로부터 30일 후로 설정
        totalAmount = this.amount.toInt(),
        items = emptyList(), // 항목 리스트는 별도로 매핑 필요
    )
