package com.autoever.everp.domain.model.user

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * UserTypeEnum 테스트
 */
class UserTypeEnumTest {

    @Test
    fun `toKorean는 올바른 한글 문자열을 반환해야 함`() {
        assertEquals("미설정", UserTypeEnum.UNKNOWN.toKorean())
        assertEquals("고객사", UserTypeEnum.CUSTOMER.toKorean())
        assertEquals("공급사", UserTypeEnum.SUPPLIER.toKorean())
        assertEquals("내부직원", UserTypeEnum.INTERNAL.toKorean())
    }

    @Test
    fun `toApiString는 올바른 API 문자열을 반환해야 함`() {
        assertEquals("UNKNOWN", UserTypeEnum.UNKNOWN.toApiString())
        assertEquals("CUSTOMER", UserTypeEnum.CUSTOMER.toApiString())
        assertEquals("SUPPLIER", UserTypeEnum.SUPPLIER.toApiString())
        assertEquals("INTERNAL", UserTypeEnum.INTERNAL.toApiString())
    }

    @Test
    fun `isCustomer는 CUSTOMER일 때만 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.CUSTOMER.isCustomer())
        assertFalse(UserTypeEnum.SUPPLIER.isCustomer())
        assertFalse(UserTypeEnum.INTERNAL.isCustomer())
        assertFalse(UserTypeEnum.UNKNOWN.isCustomer())
    }

    @Test
    fun `isSupplier는 SUPPLIER일 때만 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.SUPPLIER.isSupplier())
        assertFalse(UserTypeEnum.CUSTOMER.isSupplier())
        assertFalse(UserTypeEnum.INTERNAL.isSupplier())
        assertFalse(UserTypeEnum.UNKNOWN.isSupplier())
    }

    @Test
    fun `isInternal는 INTERNAL일 때만 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.INTERNAL.isInternal())
        assertFalse(UserTypeEnum.CUSTOMER.isInternal())
        assertFalse(UserTypeEnum.SUPPLIER.isInternal())
        assertFalse(UserTypeEnum.UNKNOWN.isInternal())
    }

    @Test
    fun `isExternal는 CUSTOMER 또는 SUPPLIER일 때 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.CUSTOMER.isExternal())
        assertTrue(UserTypeEnum.SUPPLIER.isExternal())
        assertFalse(UserTypeEnum.INTERNAL.isExternal())
        assertFalse(UserTypeEnum.UNKNOWN.isExternal())
    }

    @Test
    fun `isValid는 UNKNOWN이 아닐 때 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.CUSTOMER.isValid())
        assertTrue(UserTypeEnum.SUPPLIER.isValid())
        assertTrue(UserTypeEnum.INTERNAL.isValid())
        assertFalse(UserTypeEnum.UNKNOWN.isValid())
    }

    @Test
    fun `canCreateQuotation는 CUSTOMER 또는 INTERNAL일 때 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.CUSTOMER.canCreateQuotation())
        assertTrue(UserTypeEnum.INTERNAL.canCreateQuotation())
        assertFalse(UserTypeEnum.SUPPLIER.canCreateQuotation())
        assertFalse(UserTypeEnum.UNKNOWN.canCreateQuotation())
    }

    @Test
    fun `canViewPurchaseOrder는 SUPPLIER 또는 INTERNAL일 때 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.SUPPLIER.canViewPurchaseOrder())
        assertTrue(UserTypeEnum.INTERNAL.canViewPurchaseOrder())
        assertFalse(UserTypeEnum.CUSTOMER.canViewPurchaseOrder())
        assertFalse(UserTypeEnum.UNKNOWN.canViewPurchaseOrder())
    }

    @Test
    fun `canViewSalesOrder는 CUSTOMER 또는 INTERNAL일 때 true를 반환해야 함`() {
        assertTrue(UserTypeEnum.CUSTOMER.canViewSalesOrder())
        assertTrue(UserTypeEnum.INTERNAL.canViewSalesOrder())
        assertFalse(UserTypeEnum.SUPPLIER.canViewSalesOrder())
        assertFalse(UserTypeEnum.UNKNOWN.canViewSalesOrder())
    }

    @Test
    fun `fromString는 올바른 문자열을 UserTypeEnum로 변환해야 함`() {
        assertEquals(UserTypeEnum.CUSTOMER, UserTypeEnum.fromString("CUSTOMER"))
        assertEquals(UserTypeEnum.SUPPLIER, UserTypeEnum.fromString("SUPPLIER"))
        assertEquals(UserTypeEnum.INTERNAL, UserTypeEnum.fromString("INTERNAL"))
        assertEquals(UserTypeEnum.UNKNOWN, UserTypeEnum.fromString("UNKNOWN"))
    }

    @Test
    fun `fromString는 대소문자 구분 없이 변환해야 함`() {
        assertEquals(UserTypeEnum.CUSTOMER, UserTypeEnum.fromString("customer"))
        assertEquals(UserTypeEnum.SUPPLIER, UserTypeEnum.fromString("supplier"))
        assertEquals(UserTypeEnum.INTERNAL, UserTypeEnum.fromString("internal"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `fromString는 잘못된 문자열에 대해 예외를 발생시켜야 함`() {
        UserTypeEnum.fromString("INVALID")
    }

    @Test
    fun `fromStringOrNull는 올바른 문자열을 UserTypeEnum로 변환해야 함`() {
        assertEquals(UserTypeEnum.CUSTOMER, UserTypeEnum.fromStringOrNull("CUSTOMER"))
        assertEquals(UserTypeEnum.SUPPLIER, UserTypeEnum.fromStringOrNull("SUPPLIER"))
    }

    @Test
    fun `fromStringOrNull는 잘못된 문자열에 대해 null을 반환해야 함`() {
        assertEquals(null, UserTypeEnum.fromStringOrNull("INVALID"))
        assertEquals(null, UserTypeEnum.fromStringOrNull(""))
    }

    @Test
    fun `fromStringOrDefault는 올바른 문자열을 UserTypeEnum로 변환해야 함`() {
        assertEquals(UserTypeEnum.CUSTOMER, UserTypeEnum.fromStringOrDefault("CUSTOMER"))
        assertEquals(UserTypeEnum.SUPPLIER, UserTypeEnum.fromStringOrDefault("SUPPLIER"))
    }

    @Test
    fun `fromStringOrDefault는 잘못된 문자열에 대해 기본값을 반환해야 함`() {
        assertEquals(UserTypeEnum.UNKNOWN, UserTypeEnum.fromStringOrDefault("INVALID"))
        assertEquals(UserTypeEnum.CUSTOMER, UserTypeEnum.fromStringOrDefault("INVALID", UserTypeEnum.CUSTOMER))
    }

    @Test
    fun `getAllValues는 모든 enum 값을 반환해야 함`() {
        val values = UserTypeEnum.getAllValues()
        assertEquals(4, values.size)
        assertTrue(values.contains("UNKNOWN"))
        assertTrue(values.contains("CUSTOMER"))
        assertTrue(values.contains("SUPPLIER"))
        assertTrue(values.contains("INTERNAL"))
    }

    @Test
    fun `getValidTypes는 UNKNOWN을 제외한 모든 타입을 반환해야 함`() {
        val validTypes = UserTypeEnum.getValidTypes()
        assertEquals(3, validTypes.size)
        assertTrue(validTypes.contains(UserTypeEnum.CUSTOMER))
        assertTrue(validTypes.contains(UserTypeEnum.SUPPLIER))
        assertTrue(validTypes.contains(UserTypeEnum.INTERNAL))
        assertFalse(validTypes.contains(UserTypeEnum.UNKNOWN))
    }

    @Test
    fun `getExternalTypes는 CUSTOMER와 SUPPLIER만 반환해야 함`() {
        val externalTypes = UserTypeEnum.getExternalTypes()
        assertEquals(2, externalTypes.size)
        assertTrue(externalTypes.contains(UserTypeEnum.CUSTOMER))
        assertTrue(externalTypes.contains(UserTypeEnum.SUPPLIER))
        assertFalse(externalTypes.contains(UserTypeEnum.INTERNAL))
        assertFalse(externalTypes.contains(UserTypeEnum.UNKNOWN))
    }
}

