package com.autoever.everp.ui

import com.autoever.everp.domain.model.user.UserTypeEnum
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * MainViewModel 테스트
 */
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    @Test
    fun `초기 상태는 UNKNOWN이어야 함`() {
        assertEquals(UserTypeEnum.UNKNOWN, viewModel.userRole.value)
    }

    @Test
    fun `setUserRole로 역할 설정 시 상태가 변경되어야 함`() {
        // Given
        val role = UserTypeEnum.CUSTOMER

        // When
        viewModel.setUserRole(role)

        // Then
        assertEquals(role, viewModel.userRole.value)
    }

    @Test
    fun `updateUserRole로 문자열 역할 설정 시 상태가 변경되어야 함`() {
        // Given
        val roleString = "SUPPLIER"

        // When
        viewModel.updateUserRole(roleString)

        // Then
        assertEquals(UserTypeEnum.SUPPLIER, viewModel.userRole.value)
    }

    @Test
    fun `updateUserRole로 잘못된 문자열 입력 시 UNKNOWN으로 설정되어야 함`() {
        // Given
        val invalidRoleString = "INVALID"

        // When
        viewModel.updateUserRole(invalidRoleString)

        // Then
        assertEquals(UserTypeEnum.UNKNOWN, viewModel.userRole.value)
    }

    @Test
    fun `clearUserRole 호출 시 UNKNOWN으로 초기화되어야 함`() {
        // Given
        viewModel.setUserRole(UserTypeEnum.CUSTOMER)

        // When
        viewModel.clearUserRole()

        // Then
        assertEquals(UserTypeEnum.UNKNOWN, viewModel.userRole.value)
    }

    @Test
    fun `isCustomer는 CUSTOMER일 때 true를 반환해야 함`() {
        // Given
        viewModel.setUserRole(UserTypeEnum.CUSTOMER)

        // When & Then
        assertTrue(viewModel.isCustomer())
    }

    @Test
    fun `isCustomer는 CUSTOMER가 아닐 때 false를 반환해야 함`() {
        // Given
        viewModel.setUserRole(UserTypeEnum.SUPPLIER)

        // When & Then
        assertFalse(viewModel.isCustomer())
    }

    @Test
    fun `isVendor는 SUPPLIER일 때 true를 반환해야 함`() {
        // Given
        viewModel.setUserRole(UserTypeEnum.SUPPLIER)

        // When & Then
        assertTrue(viewModel.isVendor())
    }

    @Test
    fun `isVendor는 SUPPLIER가 아닐 때 false를 반환해야 함`() {
        // Given
        viewModel.setUserRole(UserTypeEnum.CUSTOMER)

        // When & Then
        assertFalse(viewModel.isVendor())
    }
}

