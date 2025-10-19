package com.zulfa.eabsensi.presentation.main.history

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.data.pref.UserModel
import com.zulfa.eabsensi.core.data.pref.UserPreference
import com.zulfa.eabsensi.core.data.pref.dataStore
import com.zulfa.eabsensi.core.domain.model.HistoryAttendanceDomain
import com.zulfa.eabsensi.presentation.components.HistoryCard
import com.zulfa.eabsensi.presentation.helper.formatDateToIndonesian
import com.zulfa.eabsensi.presentation.helper.formatTimeToWITA
import com.zulfa.eabsensi.presentation.helper.getTodayDate
import com.zulfa.eabsensi.presentation.helper.responsiveFontSize
import com.zulfa.eabsensi.presentation.theme.Poppins
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnusedBoxWithConstraintsScope")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val historyState by viewModel.historyState.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState()
    var currentTime by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userPreference = remember { UserPreference.getInstance(context.dataStore) }
    val user by userPreference.getSession().collectAsState(
        initial = UserModel("", "", "", "", false)
    )

    LaunchedEffect(Unit) {
        viewModel.getAttendanceHistory()
        while (true) {
            currentTime = LocalTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm", Locale("id", "ID")))
            delay(1000)
        }
    }

    LaunchedEffect(logoutState) {
        if (logoutState) onLogout()
    }

    // 🔹 Responsiveness container
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val horizontalPadding = screenWidth * 0.06f
        val verticalSpacing = screenHeight * 0.02f
        val buttonHeight = screenHeight * 0.065f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // ✅ Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            "Halo,",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = responsiveFontSize(screenWidth, 18.sp),
                            color = Color.Black
                        )
                        Text(
                            user.name,
                            fontFamily = Poppins,
                            fontSize = responsiveFontSize(screenWidth, 14.sp),
                            color = Color.Gray
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            getTodayDate(),
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = responsiveFontSize(screenWidth, 14.sp)
                        )
                        Text(
                            currentTime,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = responsiveFontSize(screenWidth, 18.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(verticalSpacing))

            // ✅ Logout button
            OutlinedButton(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .fillMaxWidth()
                    .height(buttonHeight),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCEC9)),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "LOGOUT",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = responsiveFontSize(screenWidth , 20.sp),
                        color = Color(0xFFA61F1F)
                    )
                )
            }

            Spacer(modifier = Modifier.height(verticalSpacing))

            // ✅ History list (scrollable)
            when (historyState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    val data = (historyState as Resource.Success<List<HistoryAttendanceDomain>>).data
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = horizontalPadding),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(data ?: emptyList()) { item ->
                            HistoryCard(
                                date = formatDateToIndonesian(item.date),
                                time = formatTimeToWITA(item.checkInTime),
                                status = item.status,
                                Width = screenWidth
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    val message = (historyState as Resource.Error).message
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = message ?: "Error loading data",
                            fontSize = responsiveFontSize(screenWidth, 14.sp)
                        )
                    }
                }
            }
        }
    }
}