import android.annotation.SuppressLint
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.zulfa.eabsensi.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.data.pref.UserModel
import com.zulfa.eabsensi.core.data.pref.UserPreference
import com.zulfa.eabsensi.core.data.pref.dataStore
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.core.domain.model.AttendanceDomain
import com.zulfa.eabsensi.presentation.components.AnnouncementDialog
import com.zulfa.eabsensi.presentation.helper.formatTimeToWITA
import com.zulfa.eabsensi.presentation.helper.getTodayDate
import com.zulfa.eabsensi.presentation.helper.isInsideCircle
import com.zulfa.eabsensi.presentation.helper.isLocationEnabled
import com.zulfa.eabsensi.presentation.helper.isMockLocation
import com.zulfa.eabsensi.presentation.main.home.HomeViewModel
import com.zulfa.eabsensi.presentation.theme.Poppins
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("HardwareIds")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
    fun HomeScreen(
        viewModel: HomeViewModel = koinViewModel()
    ) {

        val context = LocalContext.current
        val userPreference = remember { UserPreference.getInstance(context.dataStore) }
        val user by userPreference.getSession().collectAsState(initial = UserModel(
            "",
            "",
            "",
            "",
            false
        )
        )
        var showDialogIzin by remember { mutableStateOf(false) }
        var keteranganIzin by remember { mutableStateOf("") }

        var showDialogOutOfArea by remember { mutableStateOf(false) }
        var showDialogMock by remember { mutableStateOf(false) }
        var showDialogLocationOff by remember { mutableStateOf(false) }
        var showDialogAnnouncement by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        var currentTime by remember { mutableStateOf("") }
        val announcementState by viewModel.announcementState.collectAsState()
        val todayAttendanceState by viewModel.todayAttendanceState.collectAsState()
        val markAttendanceState by viewModel.markAttendanceState.collectAsState()
        val requestLeaveState by viewModel.requestLeaveState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getAnnouncement()
            viewModel.getTodayAttendance()
            while (true) {
                currentTime = LocalTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm", Locale("id", "ID")))
                delay(1000) // update tiap detik
            }
        }

    LaunchedEffect(announcementState) {
        if (announcementState is Resource.Success &&
            !(announcementState as Resource.Success<List<AnnouncementDomain>>).data.isNullOrEmpty() &&
            !viewModel.hasShownAnnouncement
        ) {
            showDialogAnnouncement = true
            viewModel.markAnnouncementShown()
        }
    }

    val attendanceData: AttendanceDomain? = when (todayAttendanceState) {
        is Resource.Success -> (todayAttendanceState as Resource.Success<AttendanceDomain>).data
        else -> null
    }

    val waktuMasuk = attendanceData?.checkInTime ?: "-"
    val posisiMasuk = attendanceData?.status?:"-"


    // 🔑 Show dialog only when state is Success + flag is true
    if (announcementState is Resource.Success &&
        showDialogAnnouncement
    ) {
        val announcements = (announcementState as Resource.Success<List<AnnouncementDomain>>).data
        AnnouncementDialog(
            announcements = announcements ?: emptyList(),
            onDismiss = { showDialogAnnouncement = false }
        )
    }

    // ✅ Location permission state
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )


    // ✅ Location client
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // ✅ Current location state
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isMock by remember { mutableStateOf(false) }
    var pendingLocation by remember { mutableStateOf<LatLng?>(null) }


    // ✅ Request location updates when permission is granted
    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            try {
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 2000L
                ).build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            result.lastLocation?.let { loc ->
                                currentLocation = LatLng(loc.latitude, loc.longitude)
                                isMock = loc.isMockLocation()
                            }
                        }
                    },
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            // 🚨 Ask for permission
            locationPermissionState.launchPermissionRequest()
        }
    }

    // ✅ Initial camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-6.200000, 106.816666), 10f)
    }

    // ✅ Update camera when location changes
    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 16f),
                1000
            )
        }
    }

    LaunchedEffect(markAttendanceState) {
        when (markAttendanceState) {
            is Resource.Success -> {
                Toast.makeText(context, "Absen berhasil!", Toast.LENGTH_SHORT).show()
                viewModel.clearMarkAttendanceState()

            }
            is Resource.Error -> {
                errorMessage = markAttendanceState.message ?: "Terjadi kesalahan"
                showErrorDialog = true
                viewModel.clearMarkAttendanceState()

            }
            else -> Unit
        }
    }

    LaunchedEffect(requestLeaveState) {
        when (requestLeaveState) {
            is Resource.Success -> {
                val message = (requestLeaveState as Resource.Success).data?.message ?: "Pengajuan Izin berhasil!"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearRequestLeaveState()
            }
            is Resource.Error -> {
                errorMessage = requestLeaveState.message ?: "Terjadi kesalahan"
                showErrorDialog = true
                viewModel.clearRequestLeaveState()

            }
            else -> Unit
        }
    }

    val officeLocation = LatLng(-3.3089332, 114.613662)
    val radius = 100.0 // meter


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ✅ Header
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,

                ) {
                Column {
                    Text(
                        "Halo,",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(user.name, fontFamily = Poppins, fontSize = 14.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        getTodayDate(),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        currentTime,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)

        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                cameraPositionState = cameraPositionState
            ) {
                currentLocation?.let { loc ->
                    Marker(
                        state = MarkerState(position = loc),
                        title = "You are here"
                    )
                }
                Circle(
                    center = officeLocation,
                    radius = radius,
                    fillColor = Color(0x2200FF00),
                    strokeColor = Color(0xFF00FF00),
                    strokeWidth = 2f
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)

        ){
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Waktu Masuk Tercatat", fontFamily = Poppins, fontWeight = FontWeight.Bold)
                        Text(formatTimeToWITA(waktuMasuk), color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Posisi Tercatat",fontFamily = Poppins, fontWeight = FontWeight.Bold)
                        Text(posisiMasuk, color = Color(0xFF03A9F4), fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = {        when {
                        // 🚨 Location permission not granted
                        !locationPermissionState.status.isGranted -> {
                            showDialogLocationOff = true
                            locationPermissionState.launchPermissionRequest()
                        }

                        // 🚨 Location service (GPS) off
                        !isLocationEnabled(context) -> {
                            showDialogLocationOff = true
                        }

                        else -> {
                            currentLocation?.let { loc ->
                                when {
                                    isMock -> showDialogMock = true
                                    !isInsideCircle(loc, officeLocation, radius) -> {
                                        pendingLocation = loc
                                        showDialogOutOfArea = true
                                    }
                                    else -> {
                                        viewModel.markAttendance(
                                            userId = user.id,
                                            latitude = loc.latitude,
                                            longitude = loc.longitude,
                                            androidId = Settings.Secure.getString(
                                                context.contentResolver,
                                                Settings.Secure.ANDROID_ID
                                            )
                                        )
                                    }
                                }
                            } ?: run {
                                // 🚨 No location yet (maybe still loading)
                                showDialogLocationOff = true
                            }
                        }
                    }
                    },
                    modifier = Modifier
                        .width(210.dp)
                        .height(60.dp),
                    border = BorderStroke(width = 2.dp, color = Color(0xFF000000)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F2FE)), // blue color
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        text = "Rekam Waktu Masuk",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = Color(0xFF0284C7)
                        )
                    )
                }
                OutlinedButton(
                    onClick = { showDialogIzin = true },
                    modifier = Modifier
                        .width(210.dp)
                        .height(60.dp),
                    border = BorderStroke(width = 2.dp, color = Color(0xFF000000)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F2FE)), // blue color
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        text = "Ajukan Izin",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = Color(0xFF0284C7)
                        )
                    )
                }
            }
        }
    }
    if (showDialogIzin) {
        AlertDialog(
            onDismissRequest = { showDialogIzin = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.requestLeave(
                            userId = user.id,
                            notes = keteranganIzin
                        )
                        keteranganIzin = ""
                        showDialogIzin = false
                    },
                    enabled = keteranganIzin.isNotBlank()
                ) {
                    Text("Kirim", color = Color(0xFF0284C7))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogIzin = false }) {
                    Text("Batal", color = Color.Gray)
                }
            },
            title = { Text("Ajukan Izin",
                style = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                color = Color(0xFF0284C7))
            ) },
            text = {
                Column {
                    Text("Masukkan keterangan izin:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = keteranganIzin,
                        onValueChange = { keteranganIzin = it },
                        placeholder = { Text("Keterangan") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = false,
                        maxLines = 3,
                        isError = keteranganIzin.isBlank()
                    )
                    if (keteranganIzin.isBlank()) {
                        Text(
                            text = "Keterangan tidak boleh kosong",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        )
    }
    // ✅ Dialog: Out of Area
    if (showDialogOutOfArea) {
        AlertDialog(
            onDismissRequest = { showDialogOutOfArea = false },
            confirmButton = {
                TextButton(onClick = {
                    pendingLocation?.let { loc ->
                        viewModel.markAttendance(
                            userId = user.id,
                            latitude = loc.latitude,
                            longitude = loc.longitude,
                            androidId = Settings.Secure.getString(
                                context.contentResolver,
                                Settings.Secure.ANDROID_ID
                            )
                        )
                    }
                    showDialogOutOfArea = false
                }) {
                    Text("Lanjut Absen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogOutOfArea = false }) {
                    Text("Batal")
                }
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location_warning_2),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 8.dp)
                    )
                    Text("Peringatan", style = TextStyle(
                            fontFamily = Poppins,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = Color(0xFFA72929)
                    ))
                }
            },
            text = { Text("Lokasi Anda berada di luar area yang diizinkan.") }
        )
    }

    if (showDialogMock) {
        AlertDialog(
            onDismissRequest = { showDialogMock = false },
            confirmButton = {
                TextButton(onClick = {

                    showDialogMock = false
                }) {
                    Text("OK")
                }
            },
            title = {
                Column(modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.location_warning),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 8.dp)
                    )
                    Text("Peringatan",style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = Color(0xFFA72929)
                    ))
                }
            },
            text = { Text("Lokasi terdeteksi menggunakan mock location.") }
        )
    }

    if (showDialogLocationOff) {
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = { showDialogLocationOff = false },
            confirmButton = {
                Row {
                    // 🔹 Button to open device location settings
                    TextButton(
                        onClick = {
                            showDialogLocationOff = false
                            val intent = android.content.Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Buka Pengaturan", color = Color(0xFF0284C7))
                    }

                    // 🔹 OK button to close dialog
                    TextButton(onClick = { showDialogLocationOff = false }) {
                        Text("OK", color = Color(0xFF0284C7))
                    }
                }
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location_warning_2),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        "Lokasi Tidak Aktif atau Belum diizinkan",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = Color(0xFFA72929)
                        )
                    )
                }
            },
            text = {
                Text("Aktifkan GPS atau layanan lokasi untuk merekam waktu masuk.")
            }
        )
    }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Terjadi Kesahalan") },
            text = { Text(errorMessage) }
        )
    }
}
