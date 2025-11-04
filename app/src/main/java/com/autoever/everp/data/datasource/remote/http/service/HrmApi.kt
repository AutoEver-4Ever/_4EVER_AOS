package com.autoever.everp.data.datasource.remote.http.service

/**
 * 인사 관리(HRM, Human Resource Management) 관련 API Service
 * Base URL: /business/hrm
 */
interface HrmApi {

    companion object {
        private const val BASE_URL = "/business/hrm"
    }
}

/*
// ========== 통계 ==========
@GET("$BASE_URL/statistics")
suspend fun getStatistics(
    @Query("periods") periods: String? = null,
): ApiResponse<HRStatisticsDto>

// ========== 직원 관리 ==========
@POST("$BASE_URL/employee/signup")
suspend fun createEmployee(
    @Body request: EmployeeCreateRequestDto,
): ApiResponse<CreateAuthUserResultDto>

@PATCH("$BASE_URL/employee/{employeeId}")
suspend fun updateEmployee(
    @Path("employeeId") employeeId: String,
    @Body request: EmployeeUpdateRequestDto,
): ApiResponse<Unit>

@GET("$BASE_URL/employee")
suspend fun getEmployeeList(
    @Query("departmentId") departmentId: String? = null,
    @Query("positionId") positionId: String? = null,
    @Query("name") name: String? = null,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<PageResponse<EmployeeListItemDto>>

@GET("$BASE_URL/employees/{employeeId}")
suspend fun getEmployeeDetail(
    @Path("employeeId") employeeId: String,
): ApiResponse<EmployeeDetailDto>

@GET("$BASE_URL/employees/by-internel-user/{internelUserId}")
suspend fun getEmployeeByInternalUser(
    @Path("internelUserId") internelUserId: String,
): ApiResponse<EmployeeWithTrainingDto>

@GET("$BASE_URL/employees/by-internel-user/{internelUserId}/available-trainings")
suspend fun getAvailableTrainings(
    @Path("internelUserId") internelUserId: String,
): ApiResponse<List<TrainingProgramSimpleDto>>

// ========== 부서 관리 ==========
@GET("$BASE_URL/departments")
suspend fun getDepartmentList(
    @Query("status") status: String? = null, // ACTIVE, INACTIVE
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<DepartmentListResponseDto>

@GET("$BASE_URL/departments/all")
suspend fun getAllDepartments(): ApiResponse<List<DepartmentSimpleDto>>

@GET("$BASE_URL/departments/{departmentId}/members")
suspend fun getDepartmentMembers(
    @Path("departmentId") departmentId: String,
): ApiResponse<List<DepartmentMemberDto>>

// ========== 직위 관리 ==========
@GET("$BASE_URL/positions")
suspend fun getPositionList(): ApiResponse<List<PositionListItemDto>>

@GET("$BASE_URL/positions/{positionId}")
suspend fun getPositionDetail(
    @Path("positionId") positionId: String,
): ApiResponse<PositionDetailDto>

@GET("$BASE_URL/{departmentId}/positions/all")
suspend fun getPositionsByDepartment(
    @Path("departmentId") departmentId: String,
): ApiResponse<List<PositionSimpleDto>>

// ========== 근태 관리 ==========
@GET("$BASE_URL/attendance")
suspend fun getAttendanceList(
    @Query("employeeId") employeeId: String? = null,
    @Query("startDate") startDate: String? = null,
    @Query("endDate") endDate: String? = null,
    @Query("status") status: String? = null, // NORMAL, LATE, EARLY_LEAVE, ABSENT
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<PageResponse<AttendanceListItemDto>>

@PATCH("$BASE_URL/attendance/check-in")
suspend fun checkIn(
    @Query("internelUserId") internelUserId: String? = null,
): ApiResponse<Unit>

@PATCH("$BASE_URL/attendance/check-out")
suspend fun checkOut(
    @Query("internelUserId") internelUserId: String? = null,
): ApiResponse<Unit>

@GET("$BASE_URL/employees/by-internel-user/{internelUserId}/attendance-records")
suspend fun getAttendanceRecords(
    @Path("internelUserId") internelUserId: String,
): ApiResponse<List<AttendanceRecordDto>>

@GET("$BASE_URL/attendance/statuses")
suspend fun getAttendanceStatuses(): ApiResponse<List<AttendanceStatusDto>>

// ========== 급여 관리 ==========
@GET("$BASE_URL/payroll")
suspend fun getPayrollList(
    @Query("year") year: Int,
    @Query("month") month: Int,
    @Query("name") name: String? = null,
    @Query("department") department: String? = null,
    @Query("position") position: String? = null,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<PageResponse<PayrollListItemDto>>

@POST("$BASE_URL/payroll/complete")
suspend fun completePayroll(
    @Body request: PayrollCompleteRequestDto,
): ApiResponse<Unit>

@GET("$BASE_URL/payroll/generate")
suspend fun generatePayroll(): ApiResponse<Unit>

@GET("$BASE_URL/payroll/{payrollId}")
suspend fun getPayrollDetail(
    @Path("payrollId") payrollId: String,
): ApiResponse<PaystubDetailDto>

@GET("$BASE_URL/payroll/statuses")
suspend fun getPayrollStatuses(): ApiResponse<List<PayrollStatusDto>>

// ========== 근무 기록 ==========
@PUT("$BASE_URL/time-record/{timerecordId}")
suspend fun updateTimeRecord(
    @Path("timerecordId") timerecordId: String,
    @Body request: TimeRecordUpdateRequestDto,
): ApiResponse<Unit>

@GET("$BASE_URL/time-record")
suspend fun getTimeRecordList(
    @Query("department") department: String? = null,
    @Query("position") position: String? = null,
    @Query("name") name: String? = null,
    @Query("date") date: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<PageResponse<TimeRecordListItemDto>>

@GET("$BASE_URL/time-record/{timerecordId}")
suspend fun getTimeRecordDetail(
    @Path("timerecordId") timerecordId: String,
): ApiResponse<TimeRecordDetailDto>

// ========== 휴가 관리 ==========
@POST("$BASE_URL/leave/request")
suspend fun createLeaveRequest(
    @Body request: LeaveRequestDto,
): ApiResponse<Unit>

@PATCH("$BASE_URL/leave/request/{requestId}/release")
suspend fun approveLeaveRequest(
    @Path("requestId") requestId: String,
): ApiResponse<Unit>

@PATCH("$BASE_URL/leave/request/{requestId}/reject")
suspend fun rejectLeaveRequest(
    @Path("requestId") requestId: String,
): ApiResponse<Unit>

@GET("$BASE_URL/leave-request")
suspend fun getLeaveRequestList(
    @Query("department") department: String? = null,
    @Query("position") position: String? = null,
    @Query("name") name: String? = null,
    @Query("type") type: String? = null, // ANNUAL, SICK
    @Query("sortOrder") sortOrder: String = "DESC",
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<PageResponse<LeaveRequestListItemDto>>

// ========== 교육 관리 ==========
@POST("$BASE_URL/employee/request")
suspend fun createTrainingRequest(
    @Body request: TrainingRequestDto,
): ApiResponse<Unit>

@POST("$BASE_URL/program/{employeeId}")
suspend fun assignProgram(
    @Path("employeeId") employeeId: String,
    @Body request: ProgramAssignRequestDto,
): ApiResponse<Unit>

@POST("$BASE_URL/program")
suspend fun createProgram(
    @Body request: ProgramCreateRequestDto,
): ApiResponse<Unit>

@PATCH("$BASE_URL/program/{programId}")
suspend fun modifyProgram(
    @Path("programId") programId: String,
    @Body request: ProgramModifyRequestDto,
): ApiResponse<Unit>

@GET("$BASE_URL/trainings/categories")
suspend fun getTrainingCategories(): ApiResponse<List<TrainingCategoryDto>>

@GET("$BASE_URL/trainings/programs")
suspend fun getTrainingPrograms(): ApiResponse<List<TrainingProgramSimpleDto>>

@GET("$BASE_URL/trainings/completion-statuses")
suspend fun getTrainingCompletionStatuses(): ApiResponse<List<TrainingCompletionStatusDto>>

@GET("$BASE_URL/training-status")
suspend fun getTrainingStatus(
    @Query("department") department: String? = null,
    @Query("position") position: String? = null,
    @Query("name") name: String? = null,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<TrainingStatusResponseDto>

@GET("$BASE_URL/training/employee/{employeeId}")
suspend fun getEmployeeTrainingSummary(
    @Path("employeeId") employeeId: String,
): ApiResponse<EmployeeTrainingSummaryDto>

@GET("$BASE_URL/program")
suspend fun getProgramList(
    @Query("name") name: String? = null,
    @Query("status") status: String? = null,
    @Query("category") category: String? = null,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<PageResponse<TrainingListItemDto>>

@GET("$BASE_URL/program/{programId}")
suspend fun getProgramDetail(
    @Path("programId") programId: String,
): ApiResponse<TrainingResponseDto>
// 공통 DTO 정의 (간략화)
@Serializable
data class HRStatisticsDto(val data: Map<String, Any>)

@Serializable
data class EmployeeCreateRequestDto(val data: Map<String, Any>)

@Serializable
data class EmployeeUpdateRequestDto(val data: Map<String, Any>)

@Serializable
data class CreateAuthUserResultDto(val userId: String)

@Serializable
data class EmployeeListItemDto(val employeeId: String, val name: String)

@Serializable
data class EmployeeDetailDto(val employeeId: String, val name: String)

@Serializable
data class EmployeeWithTrainingDto(val employeeId: String, val name: String)

@Serializable
data class TrainingProgramSimpleDto(val programId: String, val name: String)

@Serializable
data class DepartmentListResponseDto(val departments: List<DepartmentSimpleDto>)

@Serializable
data class DepartmentSimpleDto(val departmentId: String, val name: String)

@Serializable
data class DepartmentMemberDto(val employeeId: String, val name: String)

@Serializable
data class PositionListItemDto(val positionId: String, val name: String)

@Serializable
data class PositionDetailDto(val positionId: String, val name: String)

@Serializable
data class PositionSimpleDto(val positionId: String, val name: String)

@Serializable
data class AttendanceListItemDto(val attendanceId: String, val status: String)

@Serializable
data class AttendanceRecordDto(val date: String, val status: String)

@Serializable
data class AttendanceStatusDto(val code: String, val label: String)

@Serializable
data class PayrollListItemDto(val payrollId: String, val employeeName: String)

@Serializable
data class PayrollCompleteRequestDto(val payrollIds: List<String>)

@Serializable
data class PaystubDetailDto(val payrollId: String, val amount: Double)

@Serializable
data class PayrollStatusDto(val code: String, val label: String)

@Serializable
data class TimeRecordUpdateRequestDto(val data: Map<String, Any>)

@Serializable
data class TimeRecordListItemDto(val timerecordId: String, val date: String)

@Serializable
data class TimeRecordDetailDto(val timerecordId: String, val date: String)

@Serializable
data class LeaveRequestDto(val data: Map<String, Any>)

@Serializable
data class LeaveRequestListItemDto(val requestId: String, val type: String)

@Serializable
data class TrainingRequestDto(val data: Map<String, Any>)

@Serializable
data class ProgramAssignRequestDto(val programId: String)

@Serializable
data class ProgramCreateRequestDto(val data: Map<String, Any>)

@Serializable
data class ProgramModifyRequestDto(val data: Map<String, Any>)

@Serializable
data class TrainingCategoryDto(val code: String, val label: String)

@Serializable
data class TrainingCompletionStatusDto(val code: String, val label: String)

@Serializable
data class TrainingStatusResponseDto(val data: Map<String, Any>)

@Serializable
data class EmployeeTrainingSummaryDto(val data: Map<String, Any>)

@Serializable
data class TrainingListItemDto(val programId: String, val name: String)

@Serializable
data class TrainingResponseDto(val programId: String, val name: String)

*/
