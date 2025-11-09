package com.autoever.everp.ui.customer

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoever.everp.utils.state.UiResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun QuotationCreateScreen(
    navController: NavController,
    viewModel: QuotationCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val items by viewModel.items.collectAsState()
    val selected by viewModel.selected.collectAsState()
    val dueDate by viewModel.dueDate.collectAsState()
    val note by viewModel.note.collectAsState()
    val totalAmount = viewModel.totalAmount

    var showItemDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "견적 요청",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 납기일 선택
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "납기일",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            val context = LocalContext.current
            val currentDate = dueDate ?: LocalDate.now()
            val datePickerDialog = remember(dueDate) {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        viewModel.setDueDate(LocalDate.of(year, month + 1, dayOfMonth))
                        showDatePicker = false
                    },
                    currentDate.year,
                    currentDate.monthValue - 1,
                    currentDate.dayOfMonth,
                )
            }

            OutlinedTextField(
                value = dueDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("납기일 선택") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                trailingIcon = {
                    Row {
                        if (dueDate != null) {
                            IconButton(
                                onClick = {
                                    viewModel.setDueDate(null)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "날짜 삭제",
                                    tint = Color.Gray,
                                )
                            }
                        }
                        IconButton(
                            onClick = { showDatePicker = true },
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "날짜 선택",
                            )
                        }
                    }
                },
            )
            if (showDatePicker) {
                datePickerDialog.show()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 품목 선택 드롭다운
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "품목 선택",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Box {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("품목을 선택하세요") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showItemDropdown = true },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "품목 선택",
                        )
                    },
                )
                DropdownMenu(
                    expanded = showItemDropdown,
                    onDismissRequest = { showItemDropdown = false },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text("${item.itemName} (${item.uomName}) - ${formatCurrency(item.unitPrice)}") },
                            onClick = {
                                viewModel.addItem(item)
                                showItemDropdown = false
                            },
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 선택된 품목 목록
        if (selected.isNotEmpty()) {
            Text(
                text = "선택된 품목",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(selected.values.toList()) { selectedItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = selectedItem.item.itemName,
                                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "단위: ${selectedItem.item.uomName}",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    )
                                    Text(
                                        text = "단가: ${formatCurrency(selectedItem.unitPrice)}",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    )
                                }
                                IconButton(onClick = { viewModel.removeItem(selectedItem.item.itemId) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "삭제",
                                        tint = Color.Red,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    TextButton(
                                        onClick = { viewModel.updateQuantity(selectedItem.item.itemId, selectedItem.quantity - 1) },
                                        enabled = selectedItem.quantity > 1,
                                    ) {
                                        Text("-")
                                    }
                                    Text(
                                        text = "${selectedItem.quantity}",
                                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                    )
                                    TextButton(
                                        onClick = { viewModel.updateQuantity(selectedItem.item.itemId, selectedItem.quantity + 1) },
                                    ) {
                                        Text("+")
                                    }
                                }
                                Text(
                                    text = "합계: ${formatCurrency(selectedItem.totalPrice)}",
                                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 총 금액
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "총 금액",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = formatCurrency(totalAmount),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 비고
        OutlinedTextField(
            value = note,
            onValueChange = { viewModel.note.value = it },
            label = { Text("비고") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 제출 버튼
        Button(
            onClick = {
                viewModel.submit { success ->
                    if (success) navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selected.isNotEmpty() && uiState !is UiResult.Loading,
        ) {
            if (uiState is UiResult.Loading) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("견적 검토 요청")
        }
    }
}
